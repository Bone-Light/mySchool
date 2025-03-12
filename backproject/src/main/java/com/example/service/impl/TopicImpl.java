package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Mapper.*;
import com.example.entity.dto.*;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.response.TopicDetailVO;
import com.example.entity.vo.response.TopicPreviewVO;
import com.example.entity.vo.response.TopicTopVO;
import com.example.service.TopicService;
import com.example.util.CacheUtils;
import com.example.util.Const;
import com.example.util.FlowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TopicImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {
    @Resource
    TopicTypeMapper topicTypeMapper;

    @Resource
    FlowUtils flowUtils;

    @Resource
    CacheUtils cacheUtils;

    @Resource
    AccountMapper accountMapper;

    @Resource
    AccountDetailsMapper accountDetailsMapper;

    @Resource
    AccountPrivacyMapper accountPrivacyMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    private void initTypes() {
        types = this.listTypes()
                .stream()
                .map(TopicType::getId)
                .collect(Collectors.toSet());
    }
    private Set<Integer> types = null;

    @Override
    public List<TopicType> listTypes() {
        return topicTypeMapper.selectList(null);
    }

    @Override
    public String createTopic(int uid, TopicCreateVO vo) {
        if(!textLimitCheck(vo.getContent(), 20000)) return "文章内容太多，发文失败" ;
        if(!types.contains(vo.getType())) return "文章类型非法";

        String key = Const.FORUM_TOPIC_CREATE_COUNTER + uid;
        if(!flowUtils.limitPeriodCountCheck(key, 3, 1800)) return "发文频繁请稍后再试";
        Topic topic = new Topic();
        BeanUtils.copyProperties(vo, topic);
        topic.setContent(vo.getContent().toJSONString());
        topic.setUid(uid);
        topic.setTime(new Date());
        if(this.save(topic)) {
            cacheUtils.deleteCachePattern(Const.FORUM_TOPIC_CREATE_COUNTER + "*");
            return null;
        } else {
            return "内部错误请联系管理员";
        }
    }

    @Override
    public List<TopicPreviewVO> listTopicByPage(int pageNumber, int type) {
        String key = Const.FORUM_TOPIC_PREVIEW_CACHE + pageNumber + ":" + type;
        List<TopicPreviewVO> list = cacheUtils.takeListFromCache(key, TopicPreviewVO.class);
        if(list != null) return list;
        Page<Topic> page = Page.of(pageNumber, 10);
        if(type == 0) {
            baseMapper.selectPage(page, Wrappers.<Topic>query().orderByDesc("time"));
        } else {
            baseMapper.selectPage(page, Wrappers.<Topic>query().eq("type", type).orderByDesc("time"));
        }
        List<Topic> topics = page.getRecords();
        if(topics.isEmpty()) return null;
        list = topics.stream().map(this::resolveToPreview).toList();
        cacheUtils.saveListToCache(key, list, 60);
        return list;
    }

    @Override
    public List<TopicTopVO> listTopTopics() {
        List<Topic> topics = baseMapper.selectList(Wrappers.<Topic>query()
                .select("id", "title", "time")
                .eq("top", 1));

        return topics.stream().map(topic -> {
            TopicTopVO topicTopVO = new TopicTopVO();
            BeanUtils.copyProperties(topic, topicTopVO);
            return topicTopVO;
        }).toList();
    }

    @Override
    public TopicDetailVO getTopic(int tid) {
        TopicDetailVO topicDetailVO = new TopicDetailVO();
        Topic topic = baseMapper.selectById(tid);
        BeanUtils.copyProperties(topic, topicDetailVO);
        TopicDetailVO.User user =  new TopicDetailVO.User();
        topicDetailVO.setUser(this.fillUserDetailsByPrivacy(user, topic.getUid()));
        return topicDetailVO;
    }

    @Override
    public void interact(Interact interact, boolean state) {
        String type = interact.getType();
        synchronized (type.intern()) {
            stringRedisTemplate.opsForHash().put(type, interact.toKey(), state);
            this.saveInteractSchedule(type);
        }
    }

    private final Map<String, Boolean> state = new HashMap<>();
    ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
    private void saveInteractSchedule(String type){
        if(!state.getOrDefault(type,false)){
            state.put(type,true);
            service.schedule(()->{
                this.saveInteractSchedule(type);
                state.put(type,false);
            }, 3, TimeUnit.SECONDS);

        }
    }

    private void saveInteract(String type) {
        synchronized (type.intern()) {
            List<Interact> check = new LinkedList<>();
            List<Interact> uncheck = new LinkedList<>();
            stringRedisTemplate.opsForHash().entries(type).forEach((k, v) -> {
                if(Boolean.parseBoolean(v.toString()))
                    check.add(Interact.parseInteract(k.toString(), type));
                else
                    uncheck.add(Interact.parseInteract(k.toString(), type));
            });
            if(!check.isEmpty())
                baseMapper.addInteract(check, type);
            if(!uncheck.isEmpty())
                baseMapper.deleteInteract(uncheck, type);
            stringRedisTemplate.delete(type);
        }
    }

    private <T> T fillUserDetailsByPrivacy(T target, int uid){
        AccountDetails details = accountDetailsMapper.selectById(uid);
        Account account = accountMapper.selectById(uid);
        AccountPrivacy accountPrivacy = accountPrivacyMapper.selectById(uid);
        String[] ignores = accountPrivacy.hiddenFields();
        BeanUtils.copyProperties(details, target, ignores);
        BeanUtils.copyProperties(account, target, ignores);
        return target;
    }

    private TopicPreviewVO resolveToPreview(Topic topic) {
        TopicPreviewVO vo = new TopicPreviewVO();
        BeanUtils.copyProperties(accountMapper.selectById(topic.getUid()), vo);
        BeanUtils.copyProperties(topic, vo);
        List<String> images = new ArrayList<>();
        StringBuilder previewText = new StringBuilder();
        JSONArray ops = JSONObject.parseObject(topic.getContent()).getJSONArray("ops");
        for(Object op : ops) {
            Object insert = JSONObject.from(op).get("insert");
            if(insert instanceof String text) {
                if(previewText.length() >= 300) continue;
                previewText.append(text);
            } else if(insert instanceof Map<?,?> map) {
                Optional.ofNullable(map.get("image"))
                        .ifPresent(obj -> {images.add(obj.toString());});
            }
        }
        vo.setText(previewText.length() > 300 ?  previewText.substring(0, 300) : previewText.toString());
        vo.setImages(images);
        return vo;
    }

    private boolean textLimitCheck(JSONObject object, long max) {
        if(object == null) {
            System.out.println("空的");
            return false;
        }
        long length = 0;
        for (Object op : object.getJSONArray("ops")) {
            length += JSONObject.from(op).getString("insert").length();
            if(length > max) return false;
        }
        return true;
    }
}

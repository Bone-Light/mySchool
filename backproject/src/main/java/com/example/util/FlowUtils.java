package com.example.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FlowUtils {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public boolean limitOnceCheck(String key, int coldTime){
        if(stringRedisTemplate.hasKey(key)){
            return false;
        } else {
            stringRedisTemplate.opsForValue().set(key, "", coldTime, TimeUnit.SECONDS);
            return true;
        }
    }
}

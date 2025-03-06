<script setup>
import {computed, reactive, ref} from "vue";
import LightCard from "@/components/LightCard.vue";
import {Calendar, Collection, EditPen, Link} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {get} from "@/net/index.js";
import {ElMessage} from "element-plus";
import TopicEditor from "@/components/TopicEditor.vue";

const editor = ref(false);

const today = computed(() => {
  const date = new Date();
  return `${date.getFullYear()} 年 ${date.getMonth()+1} 月 ${date.getDate()} 日`;
});

const weather = reactive({
  location: {},
  now: {},
  hourly: [],
  success: false,
});

navigator.geolocation.getCurrentPosition(position => {
  const longitude = position.coords.longitude;
  const latitude = position.coords.latitude;
  get(`/api/forum/weather?longitude=${longitude}&latitude=${latitude}`, data => {
    Object.assign(weather, data);
    weather.success = true;
  })
}, error => {
  console.info(error)
  ElMessage.warning('位置信息获取超时，请检测网络设置')
  get(`api/forum/weather?longitude=116.40&latitude=39.90`, data => {
    Object.assign(weather, data);
    weather.success = true;
  })
}, {
  timeout: 5000,
  enableHighAccuracy: true,
})
</script>

<template>
  <div style="display: flex; margin: 20px auto; gap: 20px; max-width: 900px;">
    <div style="flex: 1">
      <LightCard>
        <div class="create-topic" @click="editor=true">
          <el-icon style="translate: 0 2px"><EditPen/></el-icon>
          点击发表主题 ......
        </div>
      </LightCard>
      <light-card style="margin-top: 10px; height: 30px"></light-card>
      <div style="margin-top: 10px">
          <lightCard style="height: 150px" v-for="item in 10">

          </lightCard>
      </div>
    </div>
    <div style="width: 280px;">
      <div style="position:sticky; top: 20px">
        <LightCard>
          <el-icon style="translate: 0 2px"><Collection/></el-icon>
          论坛公告
        </LightCard>
        <el-divider style="margin: 10px 0"/>
        <div style="font-size: 14px; margin: 10px; color: gray">
          MySQL 8.0 移除查询缓存的决策，是权衡性能、维护成本和实际效用后的结果。
          虽然查询缓存在特定场景（如静态数据重复查询）可能有效，但其全局锁、高维护成本及低命中率使其在高并发、动态数据环境下弊大于利。
          建议通过外部缓存工具或优化 InnoDB 缓冲池来替代。
        </div>
        <LightCard>
          <el-icon><calendar/></el-icon>
          天气信息
          <el-divider style="margin: 10px 0"/>
          <Weather :data="weather" />
        </LightCard>
        <div style="margin-top: 10px">
          <LightCard>
            <div class="info-text">
              <div>当前日期</div>
              <div>{{today}}</div>
            </div>
            <div class="info-text" style="margin-top: 10px">
              <div>当前ip地址</div>
              <div>127.0.0.1</div>
            </div>
          </LightCard>
        </div>
        <div style="font-size: 14px; margin-top: 10px; color: gray">
          <el-icon style="translate: 0 2px"><Link/></el-icon>
          友情链接
          <el-divider style="margin: 10px 0"/>
        </div>
        <div style="display: grid; grid-template-columns: repeat(2, 1fr); grid-gap: 10px; margin-top: 10px">
          <div class="friend-link">
            <a href="https://www.xiaomiev.com/ultra">
            <el-image style="height: 100%" src="https://tse2-mm.cn.bing.net/th/id/OIP-C.S7KOLex5iCFR-OmnjyG6GAHaD_?rs=1&pid=ImgDetMain"/>
            </a>
          </div>
          <div class="friend-link">
            <a href="https://www.xiaomiev.com/ultra">
            <el-image style="height: 100%" src="https://tse3-mm.cn.bing.net/th/id/OIP-C.xkX7k2KgAyUoam4ITGLUkQHaE2?w=246&h=180&c=7&r=0&o=5&pid=1.7"/>
            </a>
          </div>
          <div class="friend-link">
            <a href="https://www.xiaomiev.com/ultra">
            <el-image style="height: 100%" src="https://tse1-mm.cn.bing.net/th/id/OIP-C.YbiGuuE12YNGyQIzbqMb2QHaFj?w=215&h=180&c=7&r=0&o=5&pid=1.7"/>
            </a>
          </div>
          <div class="friend-link">
            <a href="https://www.xiaomiev.com/ultra">
              <el-image style="height: 100%" src="https://tse1-mm.cn.bing.net/th/id/OIP-C.Umup2WRG2IfDHncTCPc6JQHaE0?w=273&h=180&c=7&r=0&o=5&pid=1.7"/>
            </a>
          </div>
        </div>
      </div>
    </div>
    <TopicEditor :show="editor" @close="editor=false"></TopicEditor>
  </div>
</template>


<style scoped>
.info-text{
  display: flex;
  justify-content: space-between;
  color: grey;
  font-size: 14px;
}

.friend-link{
  border-radius: 5px;
  overflow: hidden;
}

.create-topic {
  background: #ededed;
  border-radius: 5px;
  height: 40px;
  font-size: 14px;
  line-height: 40px;
  padding: 0 20px;
  color: #737373;
  &:hover {
    cursor: pointer;
  }
}
</style>
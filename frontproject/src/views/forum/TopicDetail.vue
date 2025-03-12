<script setup>
import {reactive,computed} from "vue";
import {useRoute} from "vue-router";
import {get} from "@/net/index.js";
import axios from "axios";
import {ArrowLeft, Female, Male} from "@element-plus/icons-vue";
import {QuillDeltaToHtmlConverter} from "quill-delta-to-html";
import Card from "@/components/Card.vue";
import router from "@/router/index.js";
import TopicTag from "@/components/TopicTag.vue";
const route = useRoute()
const tid = route.params.tid

const topic = reactive({
  data: null,
  comment: [],

});

get(`api/forum/topic?tid=${tid}`, data => {
  topic.data = data;
  // console.log(topic.data);
})

const content = computed(() => {
  const ops = JSON.parse(topic.data.content).ops;
  const converter = new QuillDeltaToHtmlConverter(ops, {inlineStyles: true})
  return converter.convert();
})

</script>

<template>
  <div class="topic-page" v-if="topic.data" >
    <div class="topic-main" style="z-index: 10; position: sticky; top: 0;">
      <Card style="display: flex; width: 100%;">
        <el-button :icon="ArrowLeft" type="info" size="small" plain round
          @click="router.push('/index')">返回列表</el-button>
        <div style="text-align: center; flex: 1; font-weight: bold;">
          <TopicTag :type="topic.data.type"/>
          <span style="margin-left: 7px">{{topic.data.title}}</span>
        </div>
      </Card>
    </div>

    <div class="topic-main">
      <div class="topic-main-left">
        <el-avatar :src="axios.defaults.baseURL + '/image' + topic.data.user.avatar" :size="60"/>
        <div>
          <div style="font-size: 18px; font-weight: bold;">
            {{topic.data.user.username}}
            <span style="color: hotpink" v-if="topic.data.user.gender === 1">
              <el-icon><Male/></el-icon>
            </span>
            <span style="color: dodgerblue" v-if="topic.data.user.gender === 0">
              <el-icon><Female/></el-icon>
            </span>
          </div>
          <div class="description">{{topic.data.user.email}}</div>
        </div>
        <el-divider style="margin: 10px 0"/>
        <div style="text-align: left; margin: 0 5px" >
            <div class="description">手机号：{{topic.data.user.phone || '已隐藏或未填写'}}</div>
            <div class="description">QQ：{{topic.data.user.qq || '已隐藏或未填写'}}</div>
            <div class="description">wx：{{topic.data.user.wx || '已隐藏或未填写'}}</div>
        </div>
        <el-divider style="margin: 10px 0"/>
        <div class="description" style="margin: 0  5px">{{topic.data.user.description}}</div>
      </div>
      <div class="topic-main-right">
        <div class="topic-content" v-html="content"></div>
      </div>
    </div>

    <div>
    </div>
  </div>

</template>

<style scoped>
.topic-page {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px 0;

}

.topic-main{
  display: flex;
  border-radius: 7px;
  margin: 0 auto;
  background-color: var(--el-bg-color);
  width: 800px;

  .topic-main-left{
    width: 200px;
    padding: 10px;
    text-align: center;
    border-right: solid 1px var(--el-border-color);
  }

  .topic-main-right{
    width: 600px;
    padding: 10px 20px;

    .topic-content{
      font-size: 14px;
      line-height: 22px;
      opacity: 0.8;
    }

    .description{
      font-size: 12px;
      color: gray;
    }
  }

}


</style>

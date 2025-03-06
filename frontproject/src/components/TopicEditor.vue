<script setup>
import {reactive,ref} from "vue";
import {ElOption} from "element-plus";
import {Check, Document} from "@element-plus/icons-vue"
import MyEditor from "@/components/MyEditor.vue";
defineProps({
  show:Boolean,
})

const emit = defineEmits(["close"]);

const editor = reactive({
  type: null,
  title: '',
  text: ''
})

const types = [
  {id: 1, name: '日常闲聊', desc: '在这里分享你的各种日常'},
  {id: 2, name: '技术前沿', desc: '探讨最新科技动态与开发技巧'},
  {id: 3, name: '玩家天地', desc: '分享游戏心得与组队开黑'},
  {id: 4, name: '学霸联盟', desc: '交流学习方法和资源互助'},
  {id: 5, name: '影视之声', desc: '推荐好剧好片，聊聊追剧日常'},
  {id: 6, name: '健身狂人', desc: '打卡运动计划，分享增肌减脂经验'},
  {id: 7, name: '旅行印记', desc: '发布游记攻略，展示旅途风光'},
]

</script>

<template>
  <div>
    <el-drawer :model-value="show"
               :direction="'btt'"
               :size="650"
               :close-on-click-modal="false"
               @close="emit('close')"
    >
      <template #header>
        <div>
          <div style="font-weight: bold">发表新的帖子</div>
          <div style="font-size: 13px">孩子们，不要在我的网站发表不文明言论</div>
        </div>
      </template>

      <div style="display: flex; gap: 10px">
        <div style="width: 160px">
          <el-select placeholder="请选择帖子类型..." v-model="editor.type">
            <el-option v-for="item in types" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </div>
        <div style="flex: 1;">
          <el-input placeholder="请输入帖子标题" :prefix-icon="Document" v-model="editor.text"></el-input>
        </div>
      </div>

      <div style="height: 460px">
          <MyEditor style="margin-top: 10px; height: calc(100% - 60px);"
                    :content="editor.text" placeholder="今天想分享点什么呢 ..."/>
      </div>
      <div style="display: flex; justify-content: space-between; margin-top: 15px">
        <div style="color: gray; font-size: 13px">
          当前字数：666 (最大支持 30000 字)
        </div>
        <div>
          <el-button type="success" :icon="Check" plain>立刻发表主题</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>
:deep(.el-drawer){
  width: 800px;
  margin:auto;
  border-radius: 10px 10px 0 0;
}

:deep(.ql-toolbar){
  border-radius: 10px 10px 0 0;
  border-color: var(--el-border-color);
}

:deep(.ql-container){
  border-radius: 0 0 10px 10px;
  border-color: var(--el-border-color);
}
:deep(.ql-snow .ql-tooltip){
  transform: translate(85px, 10px);
}
:deep(.el-drawer__header){
  margin: 0;
}
</style>
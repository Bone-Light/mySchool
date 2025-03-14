<script setup>
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {Delta} from "quill";
import {post} from "@/net/index.js";
import MyEditor from "@/components/MyEditor.vue";

const props = defineProps({
  show: Boolean,
  tid: String,
  quote: Number,
})

const content = ref()
const emit = defineEmits(['close', 'comment'])

function submitComment() {
  post('api/forum/add-comment', {
    tid: props.tid,
    quote: props.quote,
    content: JSON.stringify(content.value),
  }, () => {
    ElMessage.success('评论发表成功');
    emit('close');
  })
}

</script>

<template>
  <div>
    <el-drawer :model-value="show"
               title="发表评论"
               @close="emit('close')"
               direction="btt" :size="360"
               :close-on-click-modal="false">
      <div>
        <div>
          <MyEditor style="margin-top: 10px; height: 120px; border-radius: 5px"
                    v-model="content"
                    placeholder="今天想分享点什么呢 ..."
                    content-type="delta"
                    element-loading-text="正在上传图片，请稍后..."
                    v-if="show"
          />
        </div>
        <div style="margin-top: 90px; text-align: right">
          <el-button type="success" @click="submitComment" plain>发表评论</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<style class="less" scoped>
:deep(.el-drawer){
  width: 800px;
  margin: 20px auto;
  border-radius: 10px;
}

:deep(.ql-toolbar){
  border-radius: 10px 10px 0 0;
  border-color: var(--el-border-color);
}

:deep(.ql-container){
  border-radius: 0 0 10px 10px;
  border-color: var(--el-border-color);
}

:deep(.el-drawer__header){
  margin: 0;
}
:deep(.el-drawer__body){
  padding: 10px;
}
</style>
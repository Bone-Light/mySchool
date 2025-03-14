<script setup>
import {ref} from "vue";
import {ElMessage} from "element-plus";
import {Delta} from "quill";
import {post} from "@/net/index.js";
import MyEditor from "@/components/MyEditor.vue";

const props = defineProps({
  show: Boolean,
  tid: String,
  quote: Object,
})

const content = ref()
const emit = defineEmits(['close', 'comment'])
const init = ()=> content.value = new Delta();

function submitComment() {
  post('api/forum/add-comment', {
    tid: props.tid,
    quote: props.quote ? props.quote.id : -1,
    content: JSON.stringify(content.value),
  }, () => {
    ElMessage.success('评论发表成功');
    emit('comment');
  })
}

function deltaToSimpleText(delta) {
  let str = '';
  for(let op of JSON.parse(delta).ops) {
    str += op.insert;
  }
  if(str.length > 35) str = str.substring(0, 35);
  return str;
}

</script>

<template>
  <div>
    <el-drawer :model-value="show"
               :title="quote ? `发表对评论 ${deltaToSimpleText(quote.content)}的回复`: '发表帖子回复'"
               @open="init"
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
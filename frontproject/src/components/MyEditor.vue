<template>
  <div>
    <div class="editor"></div>
  </div>
</template>

<script>
import 'quill/dist/quill.snow.css'
import Quill, {Delta} from 'quill'
import { ImageExtend, QuillWatch } from "quill-image-super-solution-module";
import {ElMessage} from "element-plus";
import axios from "axios";
import {accessHeader} from "@/net/index.js";

Quill.register('modules/ImageExtend', ImageExtend)
export default {
  name: 'editor',
  props: {
    modelValue: Object, // 对应 v-model
    placeholder: {
      type: String,
      default: '点击输入...'
    },
    loading: false
  },
  data() {
    return {
      quill:null,
      quillReady: false,
      options: {
        theme: 'snow',
        modules: {
          toolbar: {
            container: [
              ['bold', 'italic', 'underline', 'strike'],
              ['blockquote', 'code-block'],
              [{ 'header': 1 }, { 'header': 2 }],
              [{ 'list': 'ordered' }, { 'list': 'bullet' }],
              [{ 'script': 'sub' }, { 'script': 'super' }],
              [{ 'indent': '-1' }, { 'indent': '+1' }],
              [{ 'direction': 'rtl' }],
              [{ 'size': ['small', false, 'large', 'huge'] }],
              [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
              [{ 'color': [] }, { 'background': [] }],
              [{ 'font': [] }],
              [{ 'align': [] }],
              ['clean'],
              ['link', 'image', 'video'],
              [
                { 'table': 'TD' },
                { 'table-insert-row': 'TIR' },
                { 'table-insert-column': 'TIC' },
                { 'table-delete-row': 'TDR' },
                { 'table-delete-column': 'TDC' }
              ]
            ],
            handlers: {
              'table': function (val) {
                this.quill.getModule('table').insertTable(2, 3)
              },
              'table-insert-row': function () {
                this.quill.getModule('table').insertRowBelow()
              },
              'table-insert-column': function () {
                this.quill.getModule('table').insertColumnRight()
              },
              'table-delete-row': function () {
                this.quill.getModule('table').deleteRow()
              },
              'table-delete-column': function () {
                this.quill.getModule('table').deleteColumn()
              },
              'image': function () {
                QuillWatch.emit(this.quill.id);
              }
            },
          },
          ImageExtend: {
            action:  axios.defaults.baseURL + '/api/image/cache',
            name: 'file',
            size: 5,
            loading: true,
            accept: 'image/png, image/jpeg',
            response: (resp) => {
              if(resp.data) {
                return axios.defaults.baseURL + '/images' + resp.data
              } else {
                return null
              }
            },
            methods: 'POST',
            headers: xhr => {
              xhr.setRequestHeader('Authorization', accessHeader().Authorization);
            },
            success: () => {
              ElMessage.success('图片上传成功!')
            },
            error: () => {
              ElMessage.warning('图片上传失败，请联系管理员!')
            }
          },
        },
        placeholder: this.placeholder,
      },
    }
  },
  mounted() {
    this.$nextTick(() => {
      let dom = this.$el.querySelector('.editor');
      this.quill = new Quill(dom, this.options);
      this.quill.on('text-change', () => {
        this.$emit('update:modelValue', this.quill.getContents());
      });

      this.$el.querySelector('.ql-table-insert-row').innerHTML = `—`
      this.$el.querySelector('.ql-table-insert-column').innerHTML = `|`
      this.$el.querySelector('.ql-table-delete-row').innerHTML = `-—`
      this.$el.querySelector('.ql-table-delete-column').innerHTML = `-|`

      this.quillReady = true // ✅ 标记初始化完成
    });
  },
}


</script>

<style scoped>
</style>
<template>
  <div>
    <div class="editor"></div>
  </div>
</template>

<script>
import Quill from 'quill'
import 'quill/dist/quill.snow.css'

export default {
  name: 'editor',
  props: {
    value: Object,
    placeholder: {
      type: String,
      default: '点击输入...'
    }
  },

  data() {
    return {
      quill:null,
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
            },
          },
          table: true,
        },
        placeholder: this.placeholder,
      },
    }
  },
  mounted() {
    this.$nextTick(() => {
      let dom = this.$el.querySelector('.editor');
      this.quill = new Quill(dom, this.options);

      // 事件监听前确保实例存在
      this.quill.on('text-change', () => {
        this.$emit('input', this.quill.getContents());
      });
      this.$el.querySelector('.ql-table-insert-row').innerHTML = `—`
      this.$el.querySelector('.ql-table-insert-column').innerHTML = `|`
      this.$el.querySelector('.ql-table-delete-row').innerHTML = `-—`
      this.$el.querySelector('.ql-table-delete-column').innerHTML = `-|`
    });
  }
}


</script>

<style scoped>
</style>
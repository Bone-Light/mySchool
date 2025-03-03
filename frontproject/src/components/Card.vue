<script setup>
import {Comment} from "@element-plus/icons-vue";

defineProps({
  icon:Object,
  title: String,
  description: String,
})
</script>

<template>
  <div class="card">
    <div class="card-header" v-if="title">
      <div>
        <el-icon style="margin-right: 3px; translate: 0 2px">
          <component :is="icon"/>
        </el-icon>
        {{ title }}
      </div>
        <div>{{ description }}</div>
    </div>
    <slot/>
  </div>
</template>

<style scoped>
.card {
  border-radius: 5px;
  border: 1px solid var(--el-border-color);
  background-color: var(--el-bg-color);
  box-sizing: border-box;
  min-height: 20px;
  padding: 10px;
}

.card-header {
  border-bottom: 1px solid var(--el-border-color);
  padding-bottom: 5px;
  margin-bottom: 10px;

  &>:first-child {
    font-size:18px;
    font-weight: bold;
  }

  &>:last-child {
    font-size:12px;
    color: gray;
}

}


</style>
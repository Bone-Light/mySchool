<script setup>

</script>

<template>
  <div class="light-card">
      <slot></slot>
  </div>
</template>

<style scoped>
.light-card {
  background-color: var(--el-bg-color);
  border-radius: 5px;
  padding: 10px;
  margin: 10px 0;
}
</style>
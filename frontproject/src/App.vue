<script setup>
import {useDark} from "@vueuse/core";

useDark({
  selector: 'html',
  attribute: 'class',
  valueDark: 'dark',
  valueLight: 'light'
})
</script>

<template>
  <div>
    <router-view/>
  </div>
</template>

<style scoped>

</style>

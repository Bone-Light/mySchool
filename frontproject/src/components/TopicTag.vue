<script setup>
import {useStore} from "@/store/index.js";

const store = useStore();
defineProps({
  type: Number,
})
</script>

<template>
  <div class="topic-type" :style="{
                    color: store.findTypeById(type)?.name + 'EE',
                    'border-color': store.findTypeById(type)?.color + '77',
                    'background-color': store.findTypeById(type)?.color + '22',
                  }">
    {{store.findTypeById(type)?.name}}
  </div>
</template>

<style scoped>
.topic-type {
  display: inline-block;
  border: solid 1px gray;
  border-radius: 5px;
  font-size: 12px;
  padding: 0 5px;
  margin-right: 5px;
  translate: 0 -2px;
  color: gray;
}
</style>
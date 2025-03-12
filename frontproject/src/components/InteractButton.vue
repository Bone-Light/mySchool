<script setup>
defineProps({
  name: String,
  color: String,
  check: Boolean,
  checkName: String,
})

const emit = defineEmits(['check'])
</script>

<template>
  <div class="interact-button">
    <span class="icon" :style="{'color': check ? color:'unset'}" @click="emit('check')">
      <slot/>
    </span>
    <span class="name" :style="{'color': check ? color:'unset'}">
      {{check? checkName : name}}
    </span>
  </div>
</template>

<style scoped>
.interact-button {
  display: inline-block;
  height: 20px;

  .name {
    font-size: 13px;
    margin-right: 10px;
    opacity: 0.7;
  }

  .icon {
    vertical-align: middle;
    transition: .3s;

    &:hover {
      cursor: pointer;
      font-size: 18px;
    }
  }
}
</style>
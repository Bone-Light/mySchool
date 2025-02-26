<script setup>

</script>

<template>
  <div style="width: 100vw; height:100vh; overflow: hidden; display: flex">
    <div style="flex: 1; background-color: black">
      <el-image style="width: 100%; height:100%" fit="cover"
          src="https://img1.baidu.com/it/u=4097856652,4033702227&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800"/>
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px; font-weight: bold">欢迎来到影灯湖</div>
      <div style="margin-top: 10px">在这里你可以得到***</div>
      <div style="margin-top: 5px">你可以***，因为***</div>
    </div>
    <div class="right-card">
      <router-view v-slot="{ Component }" >
        <transition name="el-fade-in-linear" mode="out-in" >
          <component :is="Component" style="height: 100%"/>
        </transition>
      </router-view>
      <router-view/>
    </div>
  </div>

</template>

<style scoped>
.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px white;
}

.right-card {
  width: 25vw;
  z-index: 1;
  background-color: white;
}
</style>
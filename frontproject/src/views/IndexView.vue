<template>
  <div class="main-content" v-loading="loading" element-loading-text="正在进入,请稍后...">
    <el-container style="height: 100%" v-if="!loading">
      <el-header class="main-content-header">
        <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"></el-image>
        <div style="flex: 1; padding:0 20px; text-align: center">
            <el-input style="width:100%; max-width: 500px" placeholder="搜索内容相关内容...">
              <template #prefix>
                <el-icon><Search/></el-icon>
              </template>

              <template #append>
                <el-select  style="width: 120px" v-model="searchInput.type">
                  <el-option value="1" label="帖子广场"></el-option>
                  <el-option value="2" label="表白墙"></el-option>
                  <el-option value="3" label="校园活动"></el-option>
                  <el-option value="4" label="失物招领"></el-option>
                  <el-option value="5" label="教务通知"></el-option>
                </el-select>

              </template>
            </el-input>
        </div>
        <div class="user-info">
          <div class="profile">
            <div>{{store.user.username}}</div>
            <div>{{store.user.email}}</div>
          </div>
          <el-dropdown>
            <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
            <template #dropdown>
              <el-dropdown-item>
                <el-icon><Operation/></el-icon>
                个人设置
              </el-dropdown-item>
              <el-dropdown-item>
                <el-icon><Message/></el-icon>
                个人信息
              </el-dropdown-item>
              <el-dropdown-item @click="userLogout()" divided>
                <el-icon><Back/></el-icon>
                退出登录
              </el-dropdown-item>

            </template>>
          </el-dropdown>
        </div>
      </el-header>

      <el-container>
        <el-aside width="230px">
          <el-scrollbar style="height: calc(100% - 55px)">
            <el-menu
                router
                :default-active="$route.path"
                style="height: calc(100% - 55px)">
              <el-sub-menu index="1">
                <template #title>
                  <el-icon><Location/></el-icon>
                  <span><b>校园论坛</b></span>
                </template>
                <el-menu-item index="1-1">
                  <template #title>
                    <el-icon><ChatDotSquare/></el-icon> 帖子广场
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><switch-filled/></el-icon> 表白墙
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Notification/></el-icon> 校园活动
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Shop/></el-icon> 失物招领
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><School/></el-icon> 海文考研
                    <el-tag style="margin-left: 10px" size="small">合作机构</el-tag>
                  </template>
                </el-menu-item>

              </el-sub-menu>
              <el-sub-menu index="2">
                <template #title>
                  <el-icon><Soccer/></el-icon>
                  <span><b>探索与发现</b></span>
                </template>
                <el-menu-item>
                  <template #title>
                    <el-icon><switch-filled/></el-icon> 班级课程表
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Fries/></el-icon> 成绩查询
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Orange/></el-icon> 教务系统
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Notebook/></el-icon> 在线图书馆
                  </template>
                </el-menu-item>
                <el-menu-item>
                  <template #title>
                    <el-icon><Flag/></el-icon> 预约教室
                  </template>
                </el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="3">
                <template #title>
                  <el-icon><User/></el-icon>
                  <span><b>个人设置</b></span>
                </template>
                <el-menu-item index="/index/user-setting">
                  <template #title>
                    <el-icon><Medal/></el-icon> 个人信息设置
                  </template>
                </el-menu-item>
                <el-menu-item index="/index/privacy-setting">
                  <template #title>
                    <el-icon><Aim/></el-icon> 账号安全设置
                  </template>
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-scrollbar>
        </el-aside>
        <el-main style="height: 100%; padding: 0;" class="main-content-page">
            <el-scrollbar style="height: calc(100% - 55px)">
              <router-view v-slot="{ Component }">
                <transition name="el-fade-in-linear" mode="out-in">
                  <component :is="Component" style="height: 100%"/>
                </transition>
              </router-view>
            </el-scrollbar>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script setup>
import {ref,reactive} from "vue";
import {ElHeader, ElAside, ElMain, ElSubMenu, ElMenuItem, ElOption, ElDropdownItem, ElMessage} from 'element-plus'
import {get} from '@/net'
import {useStore} from "@/store";
import {
  Aim, Back,
  ChatDotSquare,
  Flag, Fries,
  Location, Medal, Message, MostlyCloudy,
  Notebook,
  Notification, Operation, Orange,
  School, Search,
  Shop, Soccer,
  SwitchFilled, User
} from "@element-plus/icons-vue";
import router from "@/router";

const loading = ref(true);
const store = useStore();
const searchInput = reactive({
  type: '1',
  text: ''
});

get('api/user/info', (data)=>{
  store.user = data;
  loading.value = false;
})

function userLogout() {
  router.push('/');
  ElMessage.success("成功退出，欢迎再次使用");
}
</script>

<style lang="less" scoped>
.main-content {
  height: 100vh;
  width: 100vw;
}

.main-content-header {
  border-bottom: solid 1px var(--el-border-color);
  height: 55px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
}

.main-content-page {
  padding: 0;
  background-color: #f7f8fa;
}

.dark .main-content-page {
  padding: 0;
  background-color: #242628;
}


.logo{
  height: 32px;
}

.user-info{
  display: flex;
  justify-content: flex-end;
  align-items: center;

  .profile {
    text-align: right;
    margin-right: 20px;

    :first-child {
      font-size: 16px;
      font-weight: bold;
      line-height: 20px;
    }

    :last-child {
      font-size: 10px;
      color: gray;
    }
  }
}

.el-avatar:hover {
  cursor: pointer;
}

</style>
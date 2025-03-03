<script setup>
import {computed, reactive, ref} from "vue";
  import {Lock, User} from "@element-plus/icons-vue";
import {ElFormItem, ElRow, ElCol, ElMessage} from "element-plus";
import {get, login} from "@/net"
  import router from "@/router/index.js";
  const formRef = ref();

  const form = reactive({
    username: '',
    password: '',
    remember: false
  })

  const rule = {
    username: [
      {required: true, message: '请输入用户名'}
    ],
    password: [
      {required: true, message: '请输入密码'}
    ]
  }

  function userLogin() {
    formRef.value.validate((isValid) => {
      if(isValid) {
        login(form.username, form.password, form.remember, () => router.push("/index"))
      }
    });
  }
</script>

<template>
  <div style="text-align: center; margin: 0 20px">
      <div style="margin-top: 150px">
        <div style="font-size: 25px; font-weight: bold">登录</div>
        <div style="font-size: 14px; color:gray">在进入系统之前，请输入用户名密码进行登录</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="username">
            <el-input v-model="form.username" maxlength="30" type="text" placeholder="用户名/邮箱">
              <template #prefix>
                <el-icon><User/></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
              <template #prefix>
                <el-icon><Lock/></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-row>
            <el-col :span="12" style="text-align: left">
              <el-form-item prop="remember">
                <el-checkbox :model="form.remember" label="记住我"/>
              </el-form-item>
            </el-col>
            <el-col :span="12" style="text-align: right">
              <el-link @click="router.push('/reset')">忘记密码?</el-link>
            </el-col>
          </el-row>
        </el-form>
      </div>

      <div style="margin-top: 40px">
        <el-button @click="userLogin()" style="width: 270px" type="success" plain>立刻登录</el-button>
        <el-divider>
          <span style="font-size: 13px; color:gray">没有账号</span>
        </el-divider>
        <div>
          <el-button @click="router.push('/register')" style="width: 270px" type="warning" plain>立即注册</el-button>
        </div>
      </div>
  </div>

</template>

<style scoped>

</style>
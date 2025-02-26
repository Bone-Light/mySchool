<script setup>
import {ref, reactive, computed} from "vue";
import {ElFormItem, ElRow, ElCol, ElMessage, ElStep, ElForm} from "element-plus";
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {get, post} from "@/net/index.js";
import router from "@/router/index.js";

const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: '',
})

const formRef = ref();
const active = ref(0);
const coldTime = ref(0);
const isValidateEmail = computed(() => (/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email)));

const validatePassword = (rule, value, callback) => {
  if(value === '') {
    callback(new Error("请再次输入密码"));
  } else if(value !== form.password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
}

const rule = {
  password: [
    {required: true, trigger: ['blur', 'change']},
    {min: 6, max:20, message: "密码长度必须在6-20个字符之间", trigger: ['blur', 'change']},
  ],
  password_repeat: [
    {validator: validatePassword, trigger: ['blur', 'change']},
  ],
  email: [
    {required: true, trigger: ['blur', 'change']},
    {type: 'email', message: '请输入邮箱', trigger: ['blur', 'change']},
  ],
  code: [
    {required: true, trigger: ['blur']},
  ]
}

function askCode() {
  if(isValidateEmail) {
    get(`/api/auth/ask-code?email=${form.email}&type=reset`, ()=>{
      coldTime.value = 60;
      ElMessage.success(`验证码已发送到邮箱: ${form.email}，请注意查收`);
      setInterval(() => {})
      const handle = setInterval(() => {
        coldTime.value--;
        if(coldTime.value === 0) {
          clearInterval(handle)
        }
      }, 1000)
    }, (message) => {
      ElMessage.warning(message);
      coldTime.value = 0;
    })
  } else {
    ElMessage.warning("请输入正确的邮箱")
  }
}

function resetPassword() {
  formRef.value.validate(async (valid) => {
    if (valid) {
      post('api/auth/reset-confirm', {
        email: form.email,
        code: form.code,
      }, ()=> active.value++)
    } else {
      ElMessage.warning("请输入完整的表单")
    }
  })
}

function doReset(){
  formRef.value.validate(async (valid) => {
    if (valid) {
      post('api/auth/reset-password', {...form}, () => {
        ElMessage.success("密码重置成功，请重新登录");
        router.push('/')
      })
    }
  })
}
</script>

<template>
  <div style="text-align: center; margin: 30px 20px">
    <div style="margin-top: 30px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="验证电子邮件"/>
        <el-step title="重新设定密码"/>
      </el-steps>
    </div>

    <div style="height: 100%" v-if="active === 0">
      <div style="font-size: 25px;font-weight: bold; margin-top: 20px">重置密码</div>
      <div style="font-size: 14px;color: gray;   margin-top: 20px">请输入要重置密码的邮箱</div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email" type="text" placeholder="电子邮件地址">
              <template #prepend>
                <el-icon><Message/></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-row :gutter="20" style="margin-top: 20px">
              <el-col :span="17">
                <el-input v-model="form.code" maxlength="6" type="text" placeholder="请输入验证码">
                  <template #prepend>
                    <el-icon><EditPen/></el-icon>
                  </template>
                </el-input>
              </el-col>
              <el-col :span="5">
                <el-button @click="askCode()" type="success" :disabled="coldTime || !isValidateEmail">
                  {{coldTime > 0 ? '请稍后 ' + coldTime + ' 秒' : '获取验证码'}}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 80px">
        <el-button @click="resetPassword()" style="width: 270px " type="warning" plain>开始重置</el-button>
      </div>
    </div>

    <div style="height: 100%" v-if="active === 1">
        <div style="margin-top: 80px">
          <div style="font-size: 25px; font-weight: bold; margin-top: 20px">重置密码</div>
          <div style="font-size: 14px; color: gray;">请填写你的新密码，务必牢记，防止丢失</div>
        </div>
        <div style="margin-top: 50px">
          <el-form :model="form" :rules="rule" ref="formRef">
            <el-form-item prop="password">
              <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
                <template #prefix>
                  <el-icon><Lock/></el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password_repeat">
              <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="重复密码">
                <template #prefix>
                  <el-icon><Lock/></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <div style="margin-top: 80px">
              <el-button @click="doReset()" style="width: 270px " type="danger" plain>立即重置</el-button>
            </div>
          </el-form>
        </div>
    </div>
  </div>


</template>

<style scoped>

</style>
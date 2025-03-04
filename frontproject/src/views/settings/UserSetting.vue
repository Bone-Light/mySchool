<script setup>
import Card from "@/components/Card.vue";
import {ElFormItem, ElRadioGroup, ElCol, ElRow, ElMessage} from "element-plus"
import {Message, Select, User} from "@element-plus/icons-vue";
import {useStore} from "@/store/index.js";
import {computed,ref,reactive} from "vue";
import axios from "axios";
import {accessHeader, get, post} from "@/net"
const description = ref();
const baseFormRef = ref();
const emailFormRef = ref();
const baseForm = reactive({
  username: "",
  gender: 0,
  phone: "",
  qq: "",
  wx: "",
  description: ""
});

const emailForm = reactive({
  email: "",
  code: ""
});

const loading = reactive({
  form: true,
  base: false
})


const store = useStore();
const registerTime = computed(() => new Date(store.user.registerTime).toLocaleString());
const validateUsername = (rule, value, callback) => {
  if(value === '') {
    callback(new Error('请输入用户名'));
  } else if(!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符'));
  } else {
    callback();
  }
}

const rules = {
  username: [
    {validator: validateUsername, trigger: ['blur', 'change']},
    { min:2, max:10, message: "用户名必须在 2-10 个字符之间", trigger: ['blur', 'change']},
  ],
  email: [
    {required: true, trigger: ['blur', 'change']},
    {type: 'email', message: '请输入邮箱', trigger: ['blur', 'change']},
  ],
}

function saveDetails(){
  baseFormRef.value.validate(
      isValid => {
        if(isValid){
          loading.base = true;
          post('api/user/save-details', baseForm, ()=>{
            ElMessage.success("用户信息保存成功");
            store.user.username = baseForm.username;
            description.value = baseForm.description;
            loading.base = false;
          }, (message)=>{
            ElMessage.warning(message);
            loading.base = false;
          })
        }
      }
  )
}

get('/api/user/details', data => {
  baseForm.username = store.user.username;
  baseForm.gender = data.gender;
  baseForm.wx = data.wx;
  baseForm.phone = data.phone;
  baseForm.qq = data.qq;
  baseForm.description = description.value = data.description;
  emailForm.email = store.user.email;
  loading.form = false;
})

const coldTime = ref(0);
const isEmailValid = ref(true)
const onValidate = (prop, isValid) => {
  if (prop === 'email')
    isEmailValid.value = isValid
}

function sendEmailCode(){
  coldTime.value = 60;
  emailFormRef.value.validate(isValid => {
    if(isValid){
      get(`/api/auth/ask-code?email=${emailForm.email}&type=modify`, ()=>{
        ElMessage.success(`验证码已成功发送到邮箱:${emailForm.email}, 请注意查收`);
        const handle = setInterval(() => {
          coldTime.value--;
          if(coldTime.value === 0) clearInterval(coldTime.value);
        }, 1000)
      }, (message) => {
        ElMessage.warning(message);
        coldTime.value = 0;
      })
    }
  })
}

function modifyEmail(){
  emailFormRef.value.validate(isValid => {
    if(isValid){
      post('/api/user/modify-email', emailForm, ()=>{
        ElMessage.success("邮件修改成功");
        store.user.email = emailForm.email;
        emailForm.code = ''
      })
    }
  })
}

function beforeAvatarUpload(rawFile){
  if(rawFile.type !== "image/jpeg" && rawFile.type !== "image/png"){
    ElMessage.error("头像只能是 JPG/PNG 类型")
    return false;
  } else if(rawFile.size / 1024 > 1024){
    rawFile.error('头像大小不能大于 1MB');
    return false;
  }
  return true;
}

function uploadSuccess(response) {
  ElMessage.success('头像上传成功')
  store.user.avatar = response.data;
}
</script>

<template>
  <div style="display:flex; max-width: 1000px; margin: auto">
    <div class="setting-left">
      <card :icon="User" title="账号信息设置" description="在这里编辑您的个人信息,您可以在隐私设置中选则是否展示这些信息" v-loading="loading.form">
        <el-form label-position="top" style="margin: 0 10px 10px 10px" :model="baseForm" :rules="rules" ref="baseFormRef">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="baseForm.username"/>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="baseForm.gender">
              <el-radio :value="0">男</el-radio>
              <el-radio :value="1">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="baseForm.phone" />
          </el-form-item>
          <el-form-item label="QQ号" prop="qq">
            <el-input v-model="baseForm.qq" />
          </el-form-item>
          <el-form-item label="微信号" prop="wx">
            <el-input v-model="baseForm.wx" />
          </el-form-item>
          <el-form-item label="个人简介" prop="description">
            <el-input type="textarea" :rows="6" v-model="baseForm.description" />
          </el-form-item>
          <div>
            <el-button @click="saveDetails" :icon="Select" type="success" :loading="loading.base">保存用户信息</el-button>
          </div>
        </el-form>
      </card>
      <card style="margin-top:10px" :icon="Message" title="电子邮件设置" description="您可以在这里设置默认邮件地址">
        <el-form label-position="top"  @validate="onValidate" style="margin:0 10px 10px 10px" :model="emailForm" :rules="rules" ref="emailFormRef">
          <el-form-item label="电子邮件" prop="email">
            <el-input v-model="emailForm.email"/>
          </el-form-item>
          <el-form-item prop="code">
            <el-row style="width:100%" :gutter="10">
              <el-col :span="18">
                <el-input placeholder="验证码" v-model="emailForm.code"/>
              </el-col>
              <el-col :span="6">
                <el-button @click="sendEmailCode()" type="success" style="width: 100%" :disabled="!isEmailValid || coldTime > 0" plain>
                  {{coldTime > 0? `请稍等 ${coldTime} 秒`: "获取验证码"}}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <div>
            <el-button  :icon="Select" type="success" @click="modifyEmail">更新电子邮件</el-button>
          </div>
        </el-form>
      </card>
    </div>
    <div class="setting-right">
      <div style="position: sticky; top: 20px">
        <card>
          <div style="text-align: center; padding: 5px 15px 0 15px">
            <div>
              <el-avatar :size="70" :src="store.avatarUrl"/>
              <div>
                <el-upload
                    :action="axios.defaults.baseURL + '/api/image/avatar'"
                    :show-file-list="false"
                    :before-upload="beforeAvatarUpload"
                    :on-success="uploadSuccess"
                    :headers="accessHeader()"
                >
                  <el-button size="small" round>修改头像</el-button>
                </el-upload>
              </div>
              <div style="font-weight: bold">你好, {{store.user.username}}</div>
              <el-divider style="margin: 10px 0"/>
              <div style="font-size: 14px; color:gray; padding:10px;">
                {{description || "这个用户很懒，没有填写个人简介"}}
              </div>
            </div>
          </div>
        </card>
        <card style="margin-top: 10px; font-size: 14px">
            <div>账号注册时间: {{registerTime}}</div>
            <div style="color:gray">欢迎加入我们的学习平台</div>
        </card>
      </div>
    </div>
  </div>

</template>

<style scoped>
.setting-left{
  flex: 1;
  margin: 20px;
}

.setting-right{
  width: 300px;
  margin: 20px 30px 20px 0;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
<script setup>
import {ref, reactive} from "vue"
import Card from "@/components/Card.vue";
import {Flag, Setting, Lock, Switch} from "@element-plus/icons-vue";
import {ElFormItem, ElMessage} from "element-plus"
import {get,post} from '@/net'

const form = reactive({
  password: "",
  new_password: "",
  new_password_repeat: "",
});


const valid = ref(false);
const onValidate = (prop, isValid) => valid.value = isValid;
const formRef = ref();

const validatePassword = (rule, value, callback) => {
  if(value === '') {
    callback(new Error("请再次输入密码"));
  } else if(value !== form.new_password) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
}

const rules = {
  password: [
    {required: true, message: '请输入原本的密码', trigger: 'blur'},
  ],
  new_password: [
    {required: true, message: '请输入新的密码', trigger: 'blur'},
    {min:6 , max:6, message: "密码的长度必须在6-16个字符之间", trigger: 'blur'},
  ],
  new_password_repeat: [
    {required: true, message: '请输入原本的密码', trigger: 'blur'},
    {validator: validatePassword, trigger: ['blur', 'change']},
  ]
}

function resetPassword() {
  formRef.value.validate(valid => {
    if (valid) {
      console.log(form);
      post('/api/user/change-password', form, () =>{
          ElMessage.success("修改密码成功!");
          formRef.value.resetFields();
      })
    }
  })
}
const saving = ref(true);
const privacy = reactive({
  phone: false,
  wx: false,
  qq: false,
  email: false,
  gender: false,
})

get('api/user/privacy', data => {
  privacy.phone = data.phone;
  privacy.email = data.email;
  privacy.wx = data.wx;
  privacy.qq = data.qq;
  privacy.gender = data.gender;
  saving.value = false;
})

function savePrivacy(type, status){
  saving.value = true;
  post('/api/user/save-privacy',  {
      type: type,
      status: status
  },()=>{
      ElMessage.success("隐私设置成功");
      saving.value = false;
  })
}

</script>

<template>
  <div style="margin: auto; max-width: 600px;">
    <div style="margin-top: 20px;">
      <card :icon="Setting" title="隐私设置" description="在这里可以设置哪些内容可以被其他人看到 @v@" :v-loading="saving">
        <div class="checkbox-list">
          <el-checkbox @change="savePrivacy('phone', privacy.phone)" v-model="privacy.phone">公开展示我的手机号</el-checkbox>
          <el-checkbox @change="savePrivacy('email', privacy.email)" v-model="privacy.email">公开展示我的电子邮件地址</el-checkbox>
          <el-checkbox @change="savePrivacy('gender', privacy.gender)" v-model="privacy.gender">公开展示我的性别</el-checkbox>
          <el-checkbox @change="savePrivacy('qq', privacy.qq)" v-model="privacy.qq">公开展示我的qq号</el-checkbox>
          <el-checkbox @change="savePrivacy('wx', privacy.wx)" v-model="privacy.wx">公开展示我的微信号</el-checkbox>
        </div>
      </card>
      <card style="margin:10px 0" :icon="Flag" title="修改密码" description="在这里可以修改密码">
        <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef" label-width="100" style="margin:20px 0">
          <el-form-item label="旧密码" prop="password">
            <el-input type="password" :prefix-icon="Lock" v-model="form.password" placeholder="旧密码" maxlength="20"></el-input>
          </el-form-item>

          <el-form-item label="新密码" prop="new_password">
            <el-input type="password" :prefix-icon="Lock" v-model="form.new_password" placeholder="新密码" maxlength="20"></el-input>
          </el-form-item>

          <el-form-item label="重复新密码" prop="new_password_repeat">
            <el-input type="password" :prefix-icon="Lock" v-model="form.new_password_repeat" placeholder="重复新密码" maxlength="20"></el-input>
          </el-form-item>

          <div style="text-align: center">
            <el-button @click="resetPassword" :icon="Switch" type="success">立即重置密码</el-button>
          </div>
        </el-form>
      </card>
    </div>
  </div>
</template>

<style scoped>
.checkbox-list{
  margin: 20px 0 0 10px;
  display: flex;
  flex-direction: column;
}




</style>
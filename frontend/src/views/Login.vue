<template>
    <div>
      <div class="login-container">
        <div style="width: 350px" class="login-box">
          <div style="font-weight: bold;text-align: center;margin-bottom: 30px">登 录</div>
          <el-form :model="data.form" ref="formRef" :rules="rules">
            <el-form-item prop="username">
              <el-input prefix-icon="user" v-model="data.form.username" placeholder="请输入账号"/>
            </el-form-item>
            <el-form-item prop="password">
              <el-input prefix-icon="lock" v-model="data.form.password" placeholder="请输入密码"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" style="width: 100%" @click="login">登 录</el-button>
            </el-form-item>
          </el-form>
          <div style="margin-top: 30px;text-align: right">
            还没有账号？请<a @click.prevent="toRegister" href="#">注册</a>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref } from 'vue';
  import { ElMessage } from 'element-plus';
  import { useRouter } from 'vue-router';
  
  const data = reactive({
    form: {
      username: '',
      password: ''
    }
  });
  
  const rules = reactive({
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ]
  });
  
  const formRef = ref();
  const router = useRouter();
  
  const login = () => {
    formRef.value.validate((valid) => {
      if (valid) {
        if (data.form.username && data.form.password) {
          ElMessage.success('登录成功');
          router.push('/dashboard'); // 跳转到 warehouse 页面
        } else {
          ElMessage.error('请输入用户名和密码');
        }
      }
    });
  };
  
  const toRegister = () => {
    router.push('/register');
  };
  </script>
  
  <style scoped>
  .login-container {
    min-height: 98vh;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    background-image: url("@/imgs/scut.jpeg");
    background-size: cover;
  }
  
  .login-box {
    background-color: rgba(255,255,255,0.7);
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    padding: 30px;
    border-radius: 5px;
  }
  </style>
  
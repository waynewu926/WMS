<template>
    <div>
      <div class="login-container">
        <div style="width: 350px" class="login-box">
          <div style="font-weight: bold;text-align: center;margin-bottom: 30px">注 册</div>
          <el-form :model="data.form" ref="formRef" :rules="rules">
            <el-form-item prop="username">
              <el-input prefix-icon="user" v-model="data.form.username" placeholder="请输入用户名"/>
            </el-form-item>
            <el-form-item prop="password">
              <el-input type="password" prefix-icon="lock" v-model="data.form.password" placeholder="请输入密码"/>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input type="password" prefix-icon="lock" v-model="data.form.confirmPassword" placeholder="请再次输入密码"/>
            </el-form-item>
            <el-form-item prop="type">
              <el-select v-model="data.form.type" placeholder="请选择用户类型" style="margin-bottom: 30px; width: 100%;">
                <el-option
                  v-for="item in options"
                  :key="item.type"
                  :label="item.label"
                  :value="item.type">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" style="width: 100%" @click="register">注 册</el-button>
            </el-form-item>
          </el-form>
          <div style="margin-top: 30px;text-align: right">
            已经注册了账号？请<a @click.prevent="toLogin" href="#">登录</a>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref } from "vue";
  import { ElMessage } from "element-plus";
  import { useRouter } from "vue-router";
  
  const options = ref([
    { type: '1', label: '管理员' },
    { type: '2', label: '公司' },
    { type: '3', label: '用户' },
  ]);
  
  const data = reactive({
    form: {
      username: '',
      password: '',
      confirmPassword: '',
      type: ''
    }
  });
  
  const validateConfirmPassword = (rule, value, callback) => {
    if (value === '') {
      callback(new Error('请再次输入密码'));
    } else if (value !== data.form.password) {
      callback(new Error('两次输入的密码不一致'));
    } else {
      callback();
    }
  };
  
  const rules = reactive({
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
    ],
    confirmPassword: [
      { required: true, message: '请再次输入密码', trigger: 'blur' },
      { validator: validateConfirmPassword, trigger: 'blur' }
    ],
    type: [
      { required: true, message: '请选择用户类型', trigger: 'change' }
    ]
  });
  
  const formRef = ref();
  const router = useRouter();
  
  const register = () => {
    formRef.value.validate((valid) => {
      if (valid) {
        // 模拟注册成功
        ElMessage.success('注册成功');
        router.push('/login'); // 跳转到登录页面
      } else {
        ElMessage.error('请填写所有必填项');
      }
    });
  };
  
  const toLogin = () => {
    router.push('/login');
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
  
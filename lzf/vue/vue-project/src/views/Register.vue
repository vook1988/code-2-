<template>
  <el-form :model="registerForm" label-width="80px" style="max-width: 400px; margin: 100px auto;">
    <el-form-item label="用户名">
      <el-input v-model="registerForm.username" placeholder="请输入用户名" />
    </el-form-item>
    <el-form-item label="密码">
      <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="handleRegister">注册</el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
    <el-form-item>
      <span>已有账号？</span>
      <el-link type="primary" @click="goLogin">去登录</el-link>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { reactive } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';

const registerForm = reactive({
  username: '',
  password: ''
});

const router = useRouter();


// 注册逻辑
const handleRegister = async () => {
  try {
    const response = await axios.post('http://localhost:8080/user/register', {
      username: registerForm.username,
      password: registerForm.password
    });
    //  console.log(response.data); // 打印响应数据，便于调试
    if (response.data.code === 200) {
      ElMessage.success('注册成功，请登录');
      router.push('/login');
    } else {
      ElMessage.error(response.data.msg);
    }
    //  console.log(response.data); // 打印响应数据，便于调试
  } catch (error) {
    ElMessage.error('注册失败，请检查网络或参数');
  }
};

// 重置表单
const resetForm = () => {
  registerForm.username = '';
  registerForm.password = '';
};

// 跳转登录页
const goLogin = () => {
  router.push('/login');
};
</script>
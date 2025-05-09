<template>
    <el-form :model="loginForm" label-width="80px" style="max-width: 400px; margin: 100px auto;">
        <el-form-item label="用户名">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="handleLogin">登录</el-button>
            <el-button @click="resetForm">重置</el-button>
            <el-button @click="goToRegister">注册</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { reactive } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';

// 表单数据
const loginForm = reactive({
    username: '',
    password: ''
});

// 路由实例
const router = useRouter();

// 登录逻辑
const handleLogin = async () => {
    try {
        // 调用后端登录接口
        const response = await axios.post('http://localhost:8080/user/login', {
            username: loginForm.username,
            password: loginForm.password
        });

        if (response.data.code === 200) {
            // 登录成功：存储用户信息到 localStorage
            localStorage.setItem('userId', response.data.data.userId);
            localStorage.setItem('username', response.data.data.username);

            // 跳转到主页
            router.push('/home');
            ElMessage.success('登录成功');
        } else {
            ElMessage.error(response.data.msg);
        }
    } catch (error) {
        ElMessage.error('登录失败，请检查网络或参数');
    }
};

// 重置表单
const resetForm = () => {
    loginForm.username = '';
    loginForm.password = '';
};
//跳转到注册页面
const goToRegister = () => {
    router.push('/register');
};
</script>
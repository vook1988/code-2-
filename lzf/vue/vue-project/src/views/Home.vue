<template>
    <el-container class="home-container">
        <!-- 顶部导航栏 -->
        <el-header class="app-header">
            <div class="logo" @click="navigateToHome">
                <img src="../assets/logo.png" alt="标注平台" class="logo-img" />
                <span class="logo-text">标注平台</span>
            </div>

            <div class="header-right">
                <el-breadcrumb separator-class="el-icon-arrow-right">
                    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item>{{ currentPage }}</el-breadcrumb-item>
                </el-breadcrumb>

                <div class="user-info">
                    <el-avatar :size="32" icon="el-icon-user" class="user-avatar"></el-avatar>
                    <span class="user-name">欢迎您，{{ username }}</span>
                    <el-button type="text" size="small" @click="handleLogout" class="logout-btn">
                        <el-icon>
                            <Logout />
                        </el-icon>
                        <span>退出登录</span>
                    </el-button>
                </div>
            </div>
        </el-header>

        <!-- 侧边栏导航 -->
        <el-container class="main-content">
            <el-aside class="app-sidebar" :width="isSidebarCollapsed ? '64px' : '240px'">
                <el-menu :default-active="activeMenu" router mode="vertical" background-color="transparent"
                    text-color="#e0e0e0" active-text-color="#409eff" class="sidebar-menu">
                    <el-sub-menu index="dashboard">
                        <template #title>
                            <el-icon>
                                <Monitor />
                            </el-icon>
                            <span>控制面板</span>
                        </template>
                        <el-menu-item index="upload-imagepage">
                            <el-icon>
                                <Upload />
                            </el-icon>
                            <span>图像上传与预览</span>
                        </el-menu-item>
                        <el-menu-item index="annotate">
                            <el-icon>
                                <EditPen />
                            </el-icon>
                            <span>标注工具</span>
                        </el-menu-item>
                        <el-menu-item index="label-manage">
                            <el-icon>
                                <Memo />
                            </el-icon>
                            <span>标签管理</span>
                        </el-menu-item>

                    </el-sub-menu>
                    <el-sub-menu index="system">
                        <template #title>
                            <el-icon>
                                <Setting />
                            </el-icon>
                            <span>系统管理</span>
                        </template>
                        <!-- <el-menu-item index="user-manage">
                            <el-icon>
                                <User />
                            </el-icon>
                            <span>用户管理</span>
                        </el-menu-item>
                        <el-menu-item index="permission">
                            <el-icon>
                                <Lock />
                            </el-icon>
                            <span>权限设置</span>
                        </el-menu-item> -->
                    </el-sub-menu>
                </el-menu>
            </el-aside>

            <!-- 内容区域 -->
            <el-main class="app-main">
                <router-view />
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();
const activeMenu = ref(route.path);
const currentPage = ref('');
const username = ref(localStorage.getItem('username') || '管理员');
const isSidebarCollapsed = ref(false);

// 监听路由变化更新面包屑和激活菜单
watch(route, (newRoute) => {
  activeMenu.value = newRoute.path;
  
  switch(newRoute.name) {
    case 'upload-imagepage':
      currentPage.value = '图像上传与预览';
      break;
    case 'annotate':
      currentPage.value = '标注工具';
      break;
    case 'label-manage':
      currentPage.value = '标签管理';
      break;
    case 'user-manage':
      currentPage.value = '用户管理';
      break;
    case 'permission':
      currentPage.value = '权限设置';
      break;
    default:
      currentPage.value = newRoute.name || '未知页面';
  }
});

// 退出登录
const handleLogout = () => {
  localStorage.removeItem('isLoggedIn');
  localStorage.removeItem('username');
  router.push('/login');
};

// 返回首页
const navigateToHome = () => {
  router.push('/home');
};

// 侧边栏折叠/展开
const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
};

// 响应式处理
const handleResize = () => {
  if (window.innerWidth < 768) {
    isSidebarCollapsed.value = true;
  } else {
    isSidebarCollapsed.value = false;
  }
};

onMounted(() => {
  handleResize();
  window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
/* 基础布局 */
.home-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f8fafc;
}

/* 导航栏样式 */
.app-header {
  background: linear-gradient(90deg, #545c64 0%, #3a4149 100%);
  color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 10;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  height: 100%;
}

.logo-img {
  width: 40px;
  height: 40px;
  margin-right: 10px;
  border-radius: 4px;
  transition: transform 0.3s ease;
}

.logo:hover .logo-img {
  transform: scale(1.05);
}

.logo-text {
  font-size: 20px;
  font-weight: 500;
  color: #fff;
  letter-spacing: 1px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.header-right {
  display: flex;
  align-items: center;
}

.el-breadcrumb {
  font-size: 14px;
  margin-right: 20px;
}

.el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #ffd04b;
  font-weight: 500;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  margin-right: 10px;
  background-color: #409eff;
  transition: transform 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.1);
}

.user-name {
  margin-right: 10px;
  font-size: 14px;
  font-weight: 400;
}

.logout-btn {
  color: #fff;
  transition: all 0.3s;
  
  &:hover {
    color: #ffd04b;
    transform: translateX(2px);
  }
  
  .el-icon {
    margin-right: 5px;
  }
}

/* 侧边栏样式 */
.main-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.app-sidebar {
  background: linear-gradient(180deg, #3a4149 0%, #2c3e50 100%);
  transition: width 0.3s ease;
  height: calc(100vh - 60px);
  overflow-y: auto;
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-menu {
  border-right: none;
}

.el-menu-item,
.el-sub-menu__title {
  height: 50px;
  line-height: 50px;
  transition: all 0.3s;
  position: relative;
}

.el-menu-item:hover,
.el-sub-menu__title:hover {
  background-color: rgba(255, 255, 255, 0.05);
  transform: translateX(2px);
}

.el-menu-item.is-active {
  background-color: rgba(64, 158, 255, 0.1);
  border-right: 3px solid #409eff;
}

.el-sub-menu .el-menu-item {
  padding-left: 50px !important;
}

/* 内容区域样式 */
.app-main {
  padding: 20px;
  background-color: #f8fafc;
  overflow-y: auto;
  transition: all 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-sidebar {
    width: 64px !important;
  }
  
  .el-menu--collapse .el-sub-menu__title span {
    height: 0;
    width: 0;
    overflow: hidden;
    visibility: hidden;
    display: inline-block;
  }
  
  .app-main {
    padding: 10px;
  }
  
  .user-name {
    display: none;
  }
  
  .el-breadcrumb {
    display: none;
  }
}
</style>
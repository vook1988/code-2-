import { createRouter, createWebHistory } from 'vue-router'
import Annotate from '../views/Annotate.vue';
import LabelManage from '../views/LabelManage.vue';
import Home from '../views/Home.vue';
import UploadImagePage from '../views/UploadImagePage.vue';


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        // 根路径重定向到登录页
        {
            path: '/',
            redirect: '/login'  //默认跳转登录页
        },
        // 登录页
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/Login.vue')
        },
        {
            path: '/register',
            name: 'register',
            component: () => import('../views/Register.vue')
        },
        // 主界面（嵌套子路由）
        {
            path: '/home',
            name: 'home',
            component: Home,
            children: [
                // 标注工具页
               
                {
                    path: '/annotate',
                    name: 'annotate',
                    component: Annotate 
                },
                // 标签管理页
                {
                    path: '/label-manage',
                    name: 'label-manage',
                    component: LabelManage
                },
                {
                    path: '/upload-imagepage',
                    name: 'upload-imagepage',
                    component: UploadImagePage
                },
            ]
        },
        // 通配符路由（处理404）
        {
            path: '/:pathMatch(.*)*',
            redirect: '/login'  // 无效路径统一跳转登录页
        }
    ]
});
// // 路由守卫：检查登录状态
router.beforeEach((to, from, next) => {
    if (to.path !== '/login' && !localStorage.getItem('userId')) {
        next('/login');
    } else {
        next();
    }
});


export default router;
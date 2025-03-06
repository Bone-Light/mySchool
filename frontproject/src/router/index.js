import {createRouter, createWebHistory} from 'vue-router'
import {unauthorized} from "@/net/index.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.VITE_ROUTES),
    routes: [
        {
            path: '/',
            name: 'Welcome',
            component: () => import('@/views/WelcomeView.vue'),
            children: [
                {
                    path: '',
                    name: 'Welcome-login',
                    component: () => import('@/views/welcome/LoginPage.vue')
                } ,{
                    path: 'register',
                    name: 'Welcome-register',
                    component: () => import('@/views/welcome/RegisterPage.vue')
                } ,{
                    path: 'reset',
                    name: 'Welcome-reset',
                    component: () => import('@/views/welcome/ResetPage.vue')
                }
            ]
        },{
            path: '/index',
            name: 'index',
            component: () => import('@/views/IndexView.vue'),
            children: [
                {
                  path: 'topic-list',
                  name: 'topic-list',
                  component: () => import('@/views/forum/TopicList.vue'),
                },
                {
                    path: 'user-setting',
                    name: 'user-setting',
                    component: () => import('@/views/settings/UserSetting.vue')
                },
                {
                    path: 'privacy-setting',
                    name: 'privacy-setting',
                    component: () => import('@/views/settings/PrivacySetting.vue')
                },
            ]
        }
    ]
})

router.beforeEach((to, from, next) => {
    const isUnauthorized = unauthorized();
    if(to.name.startsWith('welcome-') && isUnauthorized) {
        next('/index');
    } else if(to.fullPath.startsWith('/index') && isUnauthorized) {
        next('/');
    } else {
        next();
    }
})

export default router
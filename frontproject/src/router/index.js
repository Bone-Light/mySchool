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
                }
            ]
        },{
            path: '/index',
            name: 'Index',
            component: () => import('@/views/IndexView.vue')
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
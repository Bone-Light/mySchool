import {createRouter, createWebHistory} from 'vue-router'

const routes = createRouter({
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
        }
    ]
})

export default routes
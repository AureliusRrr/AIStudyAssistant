import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AppLayout from '@/layouts/AppLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { public: true }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue'),
      meta: { public: true }
    },
    {
      path: '/',
      component: AppLayout,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/DashboardView.vue')
        },
        {
          path: 'notes',
          name: 'notes',
          component: () => import('@/views/NotesView.vue')
        },
        {
          path: 'notes/:id',
          name: 'note-edit',
          component: () => import('@/views/NoteEditView.vue'),
          props: true
        },
        {
          path: 'documents',
          name: 'documents',
          component: () => import('@/views/DocumentsView.vue')
        },
        {
          path: 'ai',
          name: 'ai',
          component: () => import('@/views/AiView.vue')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ]
})

// 全局前置守卫
router.beforeEach((to) => {
  const authStore = useAuthStore()
  if (!to.meta.public && !authStore.token) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.meta.public && authStore.token) {
    return '/'
  }
  return true
})

export default router

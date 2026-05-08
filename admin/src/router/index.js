import router from './router-static'
import storage from '@/utils/storage'

const whiteList = ['/login', '/register']

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token') || storage.get('Token')
  if (token) {
    if (to.path === '/login') {
      next('/admin/index')
      return
    }
    next()
    return
  }
  if (whiteList.includes(to.path)) {
    next()
    return
  }
  next('/login')
})

export default router

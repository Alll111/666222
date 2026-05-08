import router from './router-static'
import storage from '@/utils/storage'

const whiteList = ['/login', '/register']

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token') || storage.get('Token')

  if (token) {
    if (to.path === '/login') {
      next(false)
      return
    }
    next()
    return
  }

  if (whiteList.includes(to.path)) {
    next()
    return
  }

  next({ path: '/login', replace: true })
})

export default router

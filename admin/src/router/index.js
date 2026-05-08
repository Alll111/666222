import router from './router-static'
import storage from '@/utils/storage'

const whiteList = ['/login', '/register', '/', '/front']

const isAdminUser = () => {
  const sessionTable = storage.get('sessionTable')
  const role = storage.get('role')
  return sessionTable === 'users' || role === '管理员' || role === 'admin'
}

const isBackendRoute = (path) => {
  return path === '/index' || path.startsWith('/index/') || path === '/admin/index'
}

const isFrontRoute = (path) => {
  return path === '/' || path === '/front' || path.startsWith('/front/')
}

const getDefaultPath = () => {
  return isAdminUser() ? '/index' : '/front/home'
}

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token') || storage.get('Token')

  if (token) {
    if (to.path === '/login') {
      next({ path: getDefaultPath(), replace: true })
      return
    }

    if (isBackendRoute(to.path) && !isAdminUser()) {
      next({ path: '/front/home', replace: true })
      return
    }

    next()
    return
  }

  if (whiteList.includes(to.path) || isFrontRoute(to.path)) {
    next()
    return
  }

  next({ path: '/login', replace: true })
})

export default router

import axios from 'axios'
import router from '@/router'
import storage from '@/utils/storage'

const LOGIN_STORAGE_KEYS = [
  'token',
  'Token',
  'role',
  'sessionTable',
  'adminName',
  'loginRole',
  'loginTable',
  'userid',
  'menus',
  'userInfo',
  'frontUserInfo'
]

const ROUTE_CACHE_KEYS = ['menuList', 'dynamicMenuRoutes']

function getStoredValue(key) {
  return storage.get(key) || localStorage.getItem(key) || ''
}

function getTokenHeaders() {
  const token = getStoredValue('token') || getStoredValue('Token')
  if (!token) {
    return {}
  }
  const cleanToken = String(token).replace(/^"(.*)"$/, '$1')
  return {
    Authorization: `Bearer ${cleanToken}`,
    Token: cleanToken
  }
}

export function clearLoginState() {
  LOGIN_STORAGE_KEYS.forEach(key => {
    storage.remove(key)
    localStorage.removeItem(key)
  })
  ROUTE_CACHE_KEYS.forEach(key => {
    sessionStorage.removeItem(key)
  })
  sessionStorage.clear()
}

export function redirectToLogin() {
  const routePath = router.currentRoute && router.currentRoute.value
    ? router.currentRoute.value.path
    : ''
  if (routePath === '/login') {
    return Promise.resolve()
  }
  return router.push('/login').catch(() => {})
}

export function requestLogout() {
  const http = axios.create({
    baseURL: '/api',
    timeout: 1000 * 20,
    withCredentials: true
  })

  return http.get('/logout', {
    headers: getTokenHeaders()
  })
}

export function logoutAndRedirect(options = {}) {
  const { onRequestError } = options
  return requestLogout()
    .catch(error => {
      if (typeof onRequestError === 'function') {
        onRequestError(error)
      }
      return null
    })
    .finally(() => {
      clearLoginState()
      redirectToLogin()
    })
}

import axios from 'axios'
import { ElMessage } from 'element-plus'
import { clearLoginState, redirectToLogin } from '@/utils/auth'

const request = axios.create({
  baseURL: '/api',
  timeout: 1000 * 60,
  withCredentials: true
})

request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token') || localStorage.getItem('Token')
    if (token) {
      const cleanToken = token.replace(/^"(.*)"$/, '$1')
      config.headers.Authorization = `Bearer ${cleanToken}`
      config.headers.Token = cleanToken
    }
    return config
  },
  error => Promise.reject(error)
)

request.interceptors.response.use(
  response => {
    const data = response.data || {}
    if (data.code === 401) {
      clearLoginState()
      redirectToLogin()
      ElMessage.error(data.message || data.msg || '登录已过期，请重新登录')
    }
    return response
  },
  error => {
    const status = error && error.response ? error.response.status : 0
    const message =
      (error && error.response && error.response.data && (error.response.data.message || error.response.data.msg)) ||
      error.message ||
      '请求失败'
    if (status === 401) {
      clearLoginState()
      redirectToLogin()
    }
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default request

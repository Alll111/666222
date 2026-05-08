import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router/router-static'
import storage from '@/utils/storage'

const http = axios.create({
    timeout: 1000 * 86400,
    withCredentials: true,
    baseURL: '/api',
    headers: {
        'Content-Type': 'application/json; charset=utf-8'
    }
})
// 请求拦截
http.interceptors.request.use(config => {
    config.headers['Token'] = storage.get('Token') // 请求头带上token
    return config
}, error => {
    Message.error('请求发送失败，请稍后重试')
    return Promise.reject(error)
})
// 响应拦截
http.interceptors.response.use(response => {
    if (response.data && response.data.code === 401) { // 401, token失效
        const currentRoute = router.currentRoute || {}
        if (currentRoute.name !== 'login') {
            router.push({ name: 'login' }).catch(() => {})
        }
    }
    return response
}, error => {
    let msg = '请求失败，请稍后重试'
    if (error && error.code === 'ECONNABORTED') {
        msg = '请求超时，请稍后重试'
    } else if (error && error.response) {
        if (error.response.status === 401) {
            msg = '登录已过期，请重新登录'
            const currentRoute = router.currentRoute || {}
            storage.remove && storage.remove('Token')
            if (currentRoute.name !== 'login') {
                router.push({ name: 'login' }).catch(() => {})
            }
        } else if (error.response.data && error.response.data.msg) {
            msg = error.response.data.msg
        } else {
            msg = `请求失败(${error.response.status})`
        }
    } else if (error && error.message) {
        msg = error.message
    }
    Message.error(msg)
    return Promise.resolve({
        data: {
            code: 500,
            msg
        }
    })
})
export default http

import { logoutAndRedirect } from '@/utils/auth'

export default {
  computed: {
    isAdmin() {
      const role = this.$storage.get('role')
      const table = this.$storage.get('sessionTable')
      return role === '管理员' || role === 'admin' || table === 'users'
    },
    isLoggedIn() {
      return !!(localStorage.getItem('token') || this.$storage.get('Token'))
    },
    currentUserId() {
      return Number(this.$storage.get('userid') || localStorage.getItem('userid') || 0)
    },
    currentUserName() {
      return this.$storage.get('adminName') || localStorage.getItem('adminName') || ''
    },
    currentTable() {
      return this.$storage.get('sessionTable') || localStorage.getItem('sessionTable') || ''
    }
  },
  methods: {
    normalizeUploadPath(path) {
      if (!path) {
        return ''
      }
      return String(path)
        .split(',')
        .map(item => {
          let cleanPath = String(item || '').trim()
          if (!cleanPath) {
            return ''
          }
          cleanPath = cleanPath.replace(/^https?:\/\/[^/]+/i, '')
          cleanPath = cleanPath.replace(/^\/+api(?=\/)/i, '')
          cleanPath = cleanPath.replace(/^\/+/, '')
          if (cleanPath.startsWith('upload/')) {
            return cleanPath
          }
          return `upload/${cleanPath}`
        })
        .filter(Boolean)
        .join(',')
    },
    getImageUrl(path) {
      if (!path) {
        return ''
      }
      const rawPath = String(path).split(',')[0].trim()
      if (/^https?:\/\//.test(rawPath)) {
        return rawPath
      }
      const cleanPath = this.normalizeUploadPath(rawPath).split(',')[0]
      return `${this.$base.url.replace(/\/+$/, '')}/${cleanPath}`
    },
    normalizeRichText(html) {
      if (!html) {
        return ''
      }
      return String(html).replace(/(\.\.\/)+upload/g, `${this.$base.url}upload`)
    },
    stripHtml(value) {
      return String(value || '')
        .replace(/<[^>]+>/g, '')
        .replace(/&nbsp;/gi, ' ')
        .trim()
    },
    formatDate(value) {
      if (!value) {
        return '暂无时间'
      }
      return String(value).replace('T', ' ').slice(0, 19)
    },
    requireLogin(tip) {
      if (this.isLoggedIn) {
        return true
      }
      if (tip) {
        this.$message.warning(tip)
      }
      this.$router.push('/login').catch(() => {})
      return false
    },
    logoutFront() {
      this.$confirm('确定要退出当前登录账号吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        logoutAndRedirect({
          onRequestError: () => {
            this.$message.warning('登出接口请求失败，已清除本地登录状态')
          }
        })
      }).catch(() => {})
    },
    goFront(path) {
      this.$router.push(path).catch(() => {})
    },
    loadSwiper(targetKey = 'swiperList') {
      return this.$http({
        url: 'config/list',
        method: 'get',
        params: { page: 1, limit: 5 }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          const list = (((data.data || {}).list) || []).filter(item => item && item.value)
          this[targetKey] = list.map(item => ({
            img: this.getImageUrl(item.value)
          }))
        } else {
          this[targetKey] = []
        }
      }).catch(() => {
        this[targetKey] = []
      })
    },
    fetchCurrentUser() {
      const table = this.currentTable
      if (!table) {
        localStorage.removeItem('frontUserInfo')
        return Promise.resolve(null)
      }
      return this.$http({
        url: `${table}/session`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          const user = data.data || {}
          if (user.id) {
            this.$storage.set('userid', user.id)
            localStorage.setItem('userid', user.id)
          }
          localStorage.setItem('frontUserInfo', JSON.stringify(user))
          return user
        }
        localStorage.removeItem('frontUserInfo')
        return null
      }).catch(() => {
        localStorage.removeItem('frontUserInfo')
        return null
      })
    }
  }
}

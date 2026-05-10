<template>
  <div class="front-page">
    <header class="front-header">
      <div class="header-top">
        <div class="brand" @click="goFront('/front/home')">{{ projectName }}</div>
        <div class="header-actions">
          <button v-if="!isLoggedIn" class="text-btn" @click="goFront('/login')">登录</button>
          <button v-if="!isLoggedIn" class="text-btn" @click="goFront('/register')">注册</button>
          <button v-if="isAdmin" class="text-btn" @click="goAdmin">后台管理</button>
          <button v-if="isLoggedIn" class="icon-btn" @click="goMessageCenter" aria-label="消息通知">
            <el-badge :hidden="!totalUnread" :value="totalUnread" :max="99" class="front-message-badge">
              <el-icon><Bell /></el-icon>
            </el-badge>
          </button>
          <div v-if="isLoggedIn" class="avatar-box" @click="goCenter">
            <img v-if="avatarUrl" :src="avatarUrl" alt="用户头像" class="avatar-image">
            <span v-else class="avatar-fallback">{{ avatarText }}</span>
          </div>
          <button v-if="isLoggedIn" class="text-btn" @click="goCenter">个人中心</button>
          <button v-if="isLoggedIn" class="primary-btn" @click="logoutFront">退出登录</button>
        </div>
      </div>
      <div class="header-nav">
        <button
          v-for="item in navItems"
          :key="item.path"
          class="nav-item"
          :class="{ active: isActive(item) }"
          @click="goFront(item.path)"
        >
          {{ item.name }}
        </button>
      </div>
    </header>

    <main class="front-main">
      <router-view />
    </main>

    <footer class="front-footer">
      <div class="footer-title">{{ projectName }}</div>
      <div class="footer-text">校友社交系统 · 连接你我，重温同窗时光</div>
    </footer>
  </div>
</template>

<script>
import { Bell } from '@element-plus/icons-vue'
import frontMixin from './front-mixin'

export default {
  name: 'FrontIndex',
  mixins: [frontMixin],
  components: { Bell },
  data() {
    return {
      projectName: '校友社交系统',
      friendCount: 0,
      chatCount: 0,
      messagePollTimer: null,
      userInfo: null,
      navItems: [
        { name: '首页', path: '/front/home', match: '/front/home' },
        { name: '线下活动', path: '/front/xianxiahuodong', match: '/front/xianxiahuodong' },
        { name: '交友信息', path: '/front/jiaoyouxinxi', match: '/front/jiaoyouxinxi' },
        { name: '交流论坛', path: '/front/forum', match: '/front/forum' },
        { name: '公告信息', path: '/front/news', match: '/front/news' }
      ]
    }
  },
  computed: {
    totalUnread() {
      return Number(this.friendCount || 0) + Number(this.chatCount || 0)
    },
    avatarUrl() {
      const touxiang = this.userInfo && this.userInfo.touxiang ? this.userInfo.touxiang : ''
      return this.getImageUrl(touxiang)
    },
    avatarText() {
      const name = (this.userInfo && (this.userInfo.xingming || this.userInfo.zhanghao || this.userInfo.username)) || this.currentUserName || '校友'
      return String(name).trim().slice(0, 1) || '校'
    }
  },
  created() {
    this.syncFrontUserInfo()
    this.startMessagePolling()
    window.addEventListener('refresh-message-count', this.loadMessageCount)
  },
  beforeUnmount() {
    this.stopMessagePolling()
    window.removeEventListener('refresh-message-count', this.loadMessageCount)
  },
  watch: {
    '$route.fullPath'() {
      this.syncFrontUserInfo()
      this.loadMessageCount()
    }
  },
  methods: {
    isActive(item) {
      return this.$route.path === item.path || this.$route.path.startsWith(`${item.match}/`)
    },
    syncFrontUserInfo() {
      const cache = localStorage.getItem('frontUserInfo')
      this.userInfo = cache ? JSON.parse(cache) : null
      if (this.isLoggedIn) {
        this.fetchCurrentUser().then(user => {
          this.userInfo = user || this.userInfo
        }).catch(() => {})
      }
    },
    startMessagePolling() {
      this.stopMessagePolling()
      this.loadMessageCount()
      if (!this.isLoggedIn) {
        return
      }
      this.messagePollTimer = window.setInterval(() => {
        this.loadMessageCount()
      }, 15000)
    },
    stopMessagePolling() {
      if (this.messagePollTimer) {
        window.clearInterval(this.messagePollTimer)
        this.messagePollTimer = null
      }
    },
    loadMessageCount() {
      if (!this.isLoggedIn) {
        this.friendCount = 0
        this.chatCount = 0
        return
      }
      this.$http({ url: '/siliao/messageSummary', method: 'get' }).then(({ data }) => {
        const summary = data && data.code === 0 ? (data.data || {}) : {}
        this.friendCount = Number(summary.friendUnreadCount || 0)
        this.chatCount = Number(summary.chatUnreadCount || 0)
      }).catch(() => {
        this.friendCount = 0
        this.chatCount = 0
      })
    },
    goMessageCenter() {
      if (!this.requireLogin('请先登录后查看消息')) {
        return
      }
      const tab = this.friendCount > 0 ? 'friend' : (this.chatCount > 0 ? 'chat' : 'friend')
      this.goFront(`/front/center?tab=${tab}`)
    },
    goCenter() {
      if (!this.requireLogin('请先登录后访问个人中心')) {
        return
      }
      this.goFront('/front/center')
    },
    goAdmin() {
      this.$router.push('/index').catch(() => {})
    }
  }
}
</script>

<style scoped>
.front-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #eef5ff 0%, #f7fbff 34%, #f4f8ff 100%);
  color: #1f2d3d;
}

.front-header {
  position: sticky;
  top: 0;
  z-index: 30;
  background: rgba(255, 255, 255, 0.94);
  backdrop-filter: blur(10px);
  box-shadow: 0 12px 28px rgba(15, 23, 42, 0.08);
}

.header-top,
.header-nav,
.front-main,
.front-footer {
  width: min(1180px, calc(100% - 32px));
  margin: 0 auto;
}

.header-top {
  min-height: 68px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.brand {
  font-size: 24px;
  font-weight: 700;
  color: #1f2d3d;
  cursor: pointer;
}

.header-actions,
.header-nav {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.header-nav {
  padding: 0 0 16px;
}

.nav-item,
.text-btn,
.icon-btn,
.primary-btn {
  border: 0;
  cursor: pointer;
  transition: all 0.22s ease;
}

.nav-item,
.text-btn {
  height: 38px;
  padding: 0 16px;
  border-radius: 999px;
  background: #f5f9ff;
  color: #48658f;
  border: 1px solid #dbe7ff;
  font-weight: 600;
}

.nav-item.active,
.nav-item:hover,
.text-btn:hover {
  color: #1f6fff;
  background: #eef5ff;
  border-color: #cddfff;
}

.icon-btn {
  width: 40px;
  height: 40px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 999px;
  background: #f5f9ff;
  color: #48658f;
  border: 1px solid #dbe7ff;
  font-size: 18px;
}

.icon-btn:hover {
  color: #1f6fff;
  background: #eef5ff;
  border-color: #cddfff;
}

:deep(.front-message-badge .el-badge__content) {
  border: 0;
  background: linear-gradient(135deg, #ff7a6c, #ff4d4f);
  box-shadow: 0 6px 14px rgba(255, 77, 79, 0.28);
}

.avatar-box {
  width: 40px;
  height: 40px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 999px;
  overflow: hidden;
  cursor: pointer;
  border: 1px solid #dbe7ff;
  background: #eef5ff;
  flex-shrink: 0;
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-fallback {
  color: #1f6fff;
  font-size: 15px;
  font-weight: 700;
}

.primary-btn {
  height: 40px;
  padding: 0 18px;
  border-radius: 999px;
  font-weight: 600;
  color: #fff;
  background: linear-gradient(135deg, #2d7dff 0%, #1f6fff 100%);
  box-shadow: 0 14px 24px rgba(31, 111, 255, 0.18);
}

.primary-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 18px 30px rgba(31, 111, 255, 0.24);
}

.front-main {
  padding: 28px 0 36px;
}

.front-footer {
  padding: 12px 0 40px;
  text-align: center;
  color: #7a90b6;
}

.footer-title {
  color: #1f2d3d;
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 8px;
}

.footer-text {
  max-width: 760px;
  margin: 0 auto;
  line-height: 1.8;
}

@media (max-width: 768px) {
  .header-top,
  .header-nav,
  .front-main,
  .front-footer {
    width: min(1180px, calc(100% - 20px));
  }

  .header-top {
    padding: 14px 0;
    align-items: flex-start;
    flex-direction: column;
  }

  .header-actions {
    width: 100%;
  }
}
</style>

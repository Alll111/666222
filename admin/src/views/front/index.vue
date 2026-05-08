<template>
  <div class="front-page">
    <header class="front-header">
      <div class="header-top">
        <div class="brand" @click="goFront('/front/home')">{{ projectName }}</div>
        <div class="header-actions">
          <button class="text-btn" @click="goFront('/front/forum')">交流论坛</button>
          <button class="text-btn" @click="goCenter">个人中心</button>
          <button v-if="!isLoggedIn" class="text-btn" @click="goFront('/login')">登录</button>
          <button v-if="!isLoggedIn" class="text-btn" @click="goFront('/register')">注册</button>
          <button v-if="isAdmin" class="text-btn" @click="goAdmin">后台管理</button>
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
      <div class="footer-text">前台功能已迁移到新的 Vue 页面中，可直接完成浏览、发帖、报名、收藏和个人信息维护。</div>
    </footer>
  </div>
</template>

<script>
import frontMixin from './front-mixin'

export default {
  name: 'FrontIndex',
  mixins: [frontMixin],
  data() {
    return {
      projectName: '校友社交系统',
      navItems: [
        { name: '首页', path: '/front/home', match: '/front/home' },
        { name: '线下活动', path: '/front/xianxiahuodong', match: '/front/xianxiahuodong' },
        { name: '交友信息', path: '/front/jiaoyouxinxi', match: '/front/jiaoyouxinxi' },
        { name: '交流论坛', path: '/front/forum', match: '/front/forum' },
        { name: '个人中心', path: '/front/center', match: '/front/center' }
      ]
    }
  },
  methods: {
    isActive(item) {
      return this.$route.path === item.path || this.$route.path.startsWith(`${item.match}/`)
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
}
</style>

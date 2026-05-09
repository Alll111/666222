<template>
  <div>
    <section class="hero-section">
      <div class="hero-left">
        <div class="hero-tag">WELCOME</div>
        <h1>校友社交系统前台首页</h1>
        <p>
          系统提供线下活动、交友信息、交流论坛和公告信息等核心功能，
          支持数据正常加载、列表浏览、详情查看和登录后互动操作。
        </p>
        <div class="hero-actions">
          <button class="primary-btn" @click="goFront('/front/xianxiahuodong')">浏览线下活动</button>
          <button class="ghost-btn" @click="goFront('/front/jiaoyouxinxi')">查看交友信息</button>
          <button class="ghost-btn" @click="goFront('/front/forum')">进入交流论坛</button>
        </div>
      </div>
      <div class="hero-right">
        <el-carousel v-if="swiperList.length" :interval="3000" arrow="never" height="360px">
          <el-carousel-item v-for="(item, index) in swiperList" :key="index">
            <img class="hero-image" :src="item.img" :alt="projectName">
          </el-carousel-item>
        </el-carousel>
        <div v-else class="hero-empty">暂无轮播图</div>
      </div>
    </section>

    <section class="entry-section">
      <article v-for="item in quickEntries" :key="item.path" class="entry-card" @click="goFront(item.path)">
        <div class="entry-icon">{{ item.icon }}</div>
        <div class="entry-title">{{ item.name }}</div>
        <div class="entry-desc">{{ item.desc }}</div>
      </article>
    </section>

    <section class="content-section">
      <div class="section-head">
        <div>
          <div class="section-tag">Activities</div>
          <h2>线下活动</h2>
        </div>
        <el-button link @click="goFront('/front/xianxiahuodong')">查看更多</el-button>
      </div>
      <div class="card-grid">
        <article v-for="item in activityList" :key="item.id" class="card-item" @click="goFront(`/front/xianxiahuodong/${item.id}`)">
          <img class="card-cover" :src="getImageUrl(item.huodongtupian)" :alt="item.huodongmingcheng">
          <div class="card-body">
            <h3>{{ item.huodongmingcheng }}</h3>
            <p>{{ item.huodongleixing || '未填写活动类型' }}</p>
            <div class="card-meta">{{ item.huodongdidian || '未填写地点' }}</div>
            <div class="card-meta">{{ formatDate(item.huodongshijian) }}</div>
          </div>
        </article>
        <div v-if="!activityList.length" class="empty-card">暂无活动信息</div>
      </div>
    </section>

    <section class="content-section">
      <div class="section-head">
        <div>
          <div class="section-tag">Recommend</div>
          <h2>交友信息推荐</h2>
        </div>
        <el-button link @click="goFront('/front/jiaoyouxinxi')">查看更多</el-button>
      </div>
      <div class="card-grid">
        <article v-for="item in recommendList" :key="item.id" class="card-item" @click="goFront(`/front/jiaoyouxinxi/${item.id}`)">
          <img class="card-cover" :src="getImageUrl(item.jiaoyoutupian)" :alt="item.zhanghao">
          <div class="card-body">
            <h3>{{ item.zhanghao || '匿名用户' }}</h3>
            <p>{{ item.xingming || '未填写姓名' }}</p>
            <div class="card-meta">浏览 {{ item.clicknum || 0 }}</div>
            <div class="card-text">{{ stripHtml(item.gerenjianjie || item.jiaoyoumude).slice(0, 40) || '欢迎发现更多校友动态。' }}</div>
          </div>
        </article>
        <div v-if="!recommendList.length" class="empty-card">暂无交友信息</div>
      </div>
    </section>

    <section class="content-section">
      <div class="section-head">
        <div>
          <div class="section-tag">Forum</div>
          <h2>交流论坛</h2>
        </div>
        <el-button link @click="goFront('/front/forum')">查看更多</el-button>
      </div>
      <div class="forum-list">
        <article v-for="item in forumList" :key="item.id" class="forum-item" @click="goFront(`/front/forum/${item.id}`)">
          <div class="forum-title">{{ item.title }}</div>
          <div class="forum-meta">发布人：{{ item.username || '匿名用户' }}</div>
          <div class="forum-meta">{{ formatDate(item.addtime) }}</div>
        </article>
        <div v-if="!forumList.length" class="empty-card">暂无论坛帖子</div>
      </div>
    </section>

    <section class="content-section">
      <div class="section-head">
        <div>
          <div class="section-tag">News</div>
          <h2>公告信息</h2>
        </div>
      </div>
      <div class="news-layout">
        <article v-if="newsList.length" class="news-highlight">
          <img class="news-highlight-cover" :src="getImageUrl(newsList[0].picture)" :alt="newsList[0].title">
          <div class="news-highlight-body">
            <div class="news-date">{{ formatDate(newsList[0].addtime) }}</div>
            <h3>{{ newsList[0].title }}</h3>
            <p>{{ newsList[0].introduction || '暂无公告简介。' }}</p>
          </div>
        </article>
        <div class="news-list">
          <article v-for="item in sideNewsList" :key="item.id" class="news-item">
            <div class="news-item-date">{{ formatDate(item.addtime) }}</div>
            <div class="news-item-content">
              <h4>{{ item.title }}</h4>
              <p>{{ item.introduction || '暂无简介。' }}</p>
            </div>
          </article>
          <div v-if="!newsList.length" class="empty-card">暂无公告信息</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import frontMixin from './front-mixin'

export default {
  name: 'FrontHome',
  mixins: [frontMixin],
  data() {
    return {
      projectName: '校友社交系统',
      swiperList: [],
      recommendList: [],
      newsList: [],
      forumList: [],
      activityList: [],
      quickEntries: [
        { name: '线下活动', path: '/front/xianxiahuodong', icon: '活', desc: '查看活动详情并在线报名' },
        { name: '交友信息', path: '/front/jiaoyouxinxi', icon: '友', desc: '浏览推荐、收藏互动、发布信息' },
        { name: '交流论坛', path: '/front/forum', icon: '论', desc: '查看帖子、发布交流内容和评论' },
        { name: '公告信息', path: '/front/news', icon: '告', desc: '查看系统公告、通知和最新资讯' }
      ]
    }
  },
  computed: {
    sideNewsList() {
      return this.newsList.slice(1, 5)
    }
  },
  created() {
    this.loadHomeData()
  },
  methods: {
    loadHomeData() {
      this.loadSwiper()
      this.loadRecommend()
      this.loadNews()
      this.loadForum()
      this.loadActivity()
    },
    loadRecommend() {
      this.$http({
        url: 'jiaoyouxinxi/list',
        method: 'get',
        params: { page: 1, limit: 6, sort: 'clicknum', order: 'desc', sfsh: '是' }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.recommendList = (((data.data || {}).list) || []).slice(0, 6)
        } else {
          this.recommendList = []
        }
      }).catch(() => {
        this.recommendList = []
      })
    },
    loadActivity() {
      this.$http({
        url: 'xianxiahuodong/list',
        method: 'get',
        params: { page: 1, limit: 6, sort: 'huodongshijian', order: 'desc' }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.activityList = (((data.data || {}).list) || []).slice(0, 6)
        } else {
          this.activityList = []
        }
      }).catch(() => {
        this.activityList = []
      })
    },
    loadForum() {
      this.$http({
        url: 'forum/flist?isdone=开放&sort=addtime&order=desc',
        method: 'get',
        params: { page: 1, limit: 5 }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.forumList = (data.data || {}).list || []
        } else {
          this.forumList = []
        }
      }).catch(() => {
        this.forumList = []
      })
    },
    loadNews() {
      this.$http({
        url: 'news/list',
        method: 'get',
        params: { page: 1, limit: 5, sort: 'addtime', order: 'desc' }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.newsList = (data.data || {}).list || []
        } else {
          this.newsList = []
        }
      }).catch(() => {
        this.newsList = []
      })
    }
  }
}
</script>

<style scoped>
.hero-section,
.content-section,
.entry-section {
  margin-bottom: 28px;
}

.hero-section {
  display: grid;
  grid-template-columns: 1.1fr 1fr;
  gap: 24px;
  align-items: center;
  min-height: 420px;
  padding: 28px;
  border-radius: 20px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98), rgba(238, 245, 255, 0.98));
  border: 1px solid #e6efff;
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.08);
}

.hero-tag,
.section-tag {
  display: inline-flex;
  align-items: center;
  padding: 6px 14px;
  border-radius: 999px;
  background: #eef5ff;
  color: #1f6fff;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 2px;
}

.hero-left h1 {
  margin: 18px 0 14px;
  font-size: 38px;
  line-height: 1.2;
}

.hero-left p {
  margin: 0;
  color: #667085;
  font-size: 15px;
  line-height: 1.8;
}

.hero-actions {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
  margin-top: 26px;
}

.primary-btn,
.ghost-btn {
  height: 42px;
  padding: 0 20px;
  border-radius: 999px;
  border: 0;
  cursor: pointer;
  font-weight: 600;
}

.primary-btn {
  color: #fff;
  background: linear-gradient(135deg, #2d7dff 0%, #1f6fff 100%);
  box-shadow: 0 14px 24px rgba(31, 111, 255, 0.18);
}

.ghost-btn {
  color: #1f6fff;
  background: #eef5ff;
  border: 1px solid #dbe7ff;
}

.hero-image,
.hero-empty {
  width: 100%;
  height: 360px;
  border-radius: 16px;
}

.hero-image {
  object-fit: cover;
}

.hero-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f6f9ff;
  color: #8aa0c6;
  border: 1px dashed #d5e3ff;
}

.entry-section {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

.entry-card,
.card-item,
.news-highlight,
.news-item,
.forum-item,
.empty-card {
  border-radius: 18px;
  border: 1px solid #e6efff;
  background: #fff;
  box-shadow: 0 14px 30px rgba(15, 23, 42, 0.06);
}

.entry-card {
  padding: 24px 20px;
  cursor: pointer;
  transition: transform 0.24s ease, box-shadow 0.24s ease;
}

.entry-card:hover,
.card-item:hover,
.forum-item:hover,
.news-highlight:hover,
.news-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 38px rgba(15, 23, 42, 0.10);
}

.entry-icon {
  width: 48px;
  height: 48px;
  line-height: 48px;
  text-align: center;
  border-radius: 14px;
  background: linear-gradient(135deg, #2d7dff, #5aa2ff);
  color: #fff;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 16px;
}

.entry-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 8px;
}

.entry-desc {
  color: #667085;
  line-height: 1.7;
}

.content-section {
  padding: 28px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.96);
  border: 1px solid #e6efff;
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.06);
}

.section-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 22px;
}

.section-head h2 {
  margin: 10px 0 0;
  font-size: 28px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
}

.card-item {
  overflow: hidden;
  cursor: pointer;
}

.card-cover {
  width: 100%;
  height: 220px;
  object-fit: cover;
  background: #f5f7fb;
}

.card-body {
  padding: 18px;
}

.card-body h3 {
  margin: 0 0 10px;
  font-size: 18px;
}

.card-body p,
.card-text,
.card-meta,
.forum-meta {
  color: #667085;
  line-height: 1.7;
}

.forum-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.forum-item {
  padding: 20px;
  cursor: pointer;
}

.forum-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 10px;
}

.news-layout {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 20px;
}

.news-highlight {
  overflow: hidden;
}

.news-highlight-cover {
  width: 100%;
  height: 240px;
  object-fit: cover;
  background: #f5f7fb;
}

.news-highlight-body {
  padding: 20px;
}

.news-highlight-body h3,
.news-item-content h4 {
  margin: 0 0 10px;
}

.news-date,
.news-item-date {
  color: #1f6fff;
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 10px;
}

.news-highlight-body p,
.news-item-content p {
  margin: 0;
  color: #667085;
  line-height: 1.8;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.news-item {
  padding: 18px;
}

.empty-card {
  min-height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #8aa0c6;
}

@media (max-width: 992px) {
  .hero-section,
  .news-layout,
  .card-grid,
  .entry-section,
  .forum-list {
    grid-template-columns: 1fr;
  }

  .hero-left h1 {
    font-size: 30px;
  }
}
</style>

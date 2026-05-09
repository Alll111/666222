<template>
  <div>
    <section v-if="mode === 'list'" class="page-card">
      <div class="page-head">
        <div>
          <div class="page-tag">News</div>
          <h2>公告信息</h2>
        </div>
      </div>

      <div class="news-list">
        <article v-for="item in list" :key="item.id" class="news-item" @click="openNews(item)">
          <img class="news-cover" :src="getImageUrl(item && item.picture)" :alt="getNewsTitle(item)">
          <div class="news-body">
            <div class="news-date">{{ formatDate(item && item.addtime) }}</div>
            <h3>{{ getNewsTitle(item) }}</h3>
            <p>{{ getNewsIntroduction(item) }}</p>
          </div>
        </article>
        <div v-if="!list.length" class="empty-card">暂无公告信息</div>
      </div>

      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          v-model:current-page="page"
          :page-size="limit"
          :total="total"
          @current-change="loadList"
        />
      </div>
    </section>

    <section v-else class="page-card">
      <div class="page-head">
        <el-button link @click="goFront('/front/news')">返回公告列表</el-button>
      </div>
      <article class="news-detail">
        <img class="news-detail-cover" :src="getImageUrl(detail && detail.picture)" :alt="getNewsTitle(detail)">
        <div class="news-date">{{ formatDate(detail && detail.addtime) }}</div>
        <h1>{{ getNewsTitle(detail) }}</h1>
        <p class="news-introduction">{{ getNewsIntroduction(detail) }}</p>
        <div class="rich-text" v-html="getNewsContent(detail)"></div>
      </article>
    </section>
  </div>
</template>

<script>
import frontMixin from './front-mixin'

export default {
  name: 'FrontNews',
  mixins: [frontMixin],
  data() {
    return {
      list: [],
      detail: {},
      page: 1,
      limit: 8,
      total: 0
    }
  },
  computed: {
    mode() {
      return this.$route.params.id ? 'detail' : 'list'
    }
  },
  watch: {
    '$route.fullPath': {
      immediate: true,
      handler() {
        if (this.mode === 'detail') {
          this.loadDetail()
          return
        }
        this.loadList()
      }
    }
  },
  methods: {
    getNewsId(item) {
      return item && item.id ? item.id : ''
    },
    getNewsTitle(item) {
      return item && item.title ? item.title : '未命名公告'
    },
    getNewsIntroduction(item) {
      return item && item.introduction ? item.introduction : '暂无公告简介'
    },
    getNewsContent(item) {
      return item && item.content ? item.content : ''
    },
    openNews(item) {
      const id = this.getNewsId(item)
      if (!id) {
        return
      }
      this.goFront(`/front/news/${id}`)
    },
    loadList() {
      this.$http({
        url: 'news/list',
        method: 'get',
        params: {
          page: this.page,
          limit: this.limit,
          sort: 'addtime',
          order: 'desc'
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.list = (data.data || {}).list || []
          this.total = (data.data || {}).total || 0
        } else {
          this.list = []
          this.total = 0
        }
      }).catch(() => {
        this.list = []
        this.total = 0
      })
    },
    loadDetail() {
      this.$http({
        url: `news/detail/${this.$route.params.id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.detail = data.data || {}
          this.detail.content = this.normalizeRichText(this.detail.content)
        } else {
          this.detail = {}
        }
      }).catch(() => {
        this.detail = {}
      })
    }
  }
}
</script>

<style scoped>
.page-card {
  padding: 28px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.96);
  border: 1px solid #e6efff;
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.06);
}

.page-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 22px;
}

.page-tag {
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

.page-head h2 {
  margin: 10px 0 0;
  font-size: 28px;
}

.news-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.news-item,
.empty-card,
.news-detail {
  border-radius: 18px;
  border: 1px solid #e6efff;
  background: #fff;
  box-shadow: 0 14px 30px rgba(15, 23, 42, 0.06);
}

.news-item {
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.24s ease, box-shadow 0.24s ease;
}

.news-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 38px rgba(15, 23, 42, 0.10);
}

.news-cover,
.news-detail-cover {
  width: 100%;
  object-fit: cover;
  background: #f5f7fb;
}

.news-cover {
  height: 220px;
}

.news-detail-cover {
  height: 320px;
  border-radius: 16px;
  margin-bottom: 20px;
}

.news-body {
  padding: 20px;
}

.news-body h3,
.news-detail h1 {
  margin: 0 0 10px;
}

.news-date {
  color: #1f6fff;
  font-size: 13px;
  font-weight: 700;
  margin-bottom: 10px;
}

.news-body p,
.news-introduction,
.rich-text {
  color: #667085;
  line-height: 1.8;
}

.news-detail {
  padding: 24px;
}

.pager {
  display: flex;
  justify-content: center;
  margin-top: 22px;
}

.empty-card {
  min-height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #8aa0c6;
}

@media (max-width: 992px) {
  .news-list {
    grid-template-columns: 1fr;
  }
}
</style>

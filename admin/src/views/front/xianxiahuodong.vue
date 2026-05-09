<template>
  <div>
    <section v-if="!detailMode" class="page-card">
      <div class="page-head">
        <div>
          <div class="page-tag">Activities</div>
          <h2>线下活动</h2>
        </div>
      </div>

      <el-form :inline="true" :model="filters" class="filter-bar">
        <el-form-item label="活动名称">
          <el-input v-model="filters.huodongmingcheng" placeholder="活动名称" clearable />
        </el-form-item>
        <el-form-item label="活动类型">
          <el-input v-model="filters.huodongleixing" placeholder="活动类型" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetFilters">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="list-grid">
        <article
          v-for="item in list"
          :key="item.id"
          class="card-item"
          @click="openActivity(item)"
        >
          <img class="card-cover" :src="getImageUrl(item && item.huodongtupian)" :alt="getActivityName(item)">
          <div class="card-body">
            <h3>{{ getActivityName(item) }}</h3>
            <p>{{ getActivityType(item) }}</p>
            <div class="meta-item">地点：{{ getActivityAddress(item) }}</div>
            <div class="meta-item">时间：{{ formatDate(item && item.huodongshijian) }}</div>
          </div>
        </article>
        <div v-if="!list.length" class="empty-card">暂无活动信息</div>
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

    <div v-else class="detail-layout">
      <section class="detail-card">
        <div class="detail-header">
          <el-button link @click="goFront('/front/xianxiahuodong')">返回列表</el-button>
          <div class="detail-actions">
            <el-button type="primary" @click="openSignupDialog">报名活动</el-button>
            <el-button @click="toggleStoreup">{{ storeupFlag ? '取消收藏' : '点我收藏' }}</el-button>
          </div>
        </div>

        <div class="detail-main">
          <div class="detail-gallery">
            <el-carousel v-if="swiperList.length" height="360px" arrow="always">
              <el-carousel-item v-for="(item, index) in swiperList" :key="index">
                <img class="detail-image" :src="getImageUrl(item)" :alt="getActivityName(detail)">
              </el-carousel-item>
            </el-carousel>
            <div v-else class="hero-empty">暂无活动图片</div>
          </div>
          <div class="detail-info">
            <h1>{{ getActivityName(detail) }}</h1>
            <div class="info-item"><span>活动类型：</span>{{ getActivityType(detail) }}</div>
            <div class="info-item"><span>活动地点：</span>{{ getActivityAddress(detail) }}</div>
            <div class="info-item"><span>活动时间：</span>{{ formatDate(detail && detail.huodongshijian) }}</div>
          </div>
        </div>

        <el-tabs class="detail-tabs">
          <el-tab-pane label="活动内容">
            <div class="rich-text" v-html="getActivityContent(detail)" />
          </el-tab-pane>
          <el-tab-pane label="评论">
            <div class="comment-form">
              <el-input
                type="textarea"
                :rows="4"
                v-model="commentForm.content"
                placeholder="请输入评论内容"
              />
              <div class="comment-actions">
                <el-button type="primary" @click="submitComment">立即提交</el-button>
              </div>
            </div>
            <div class="comment-list">
              <article v-for="item in comments" :key="item.id" class="comment-item">
                <div class="comment-user">{{ item && item.nickname ? item.nickname : '匿名用户' }}</div>
                <div class="comment-content">{{ item && item.content ? item.content : '' }}</div>
                <div v-if="item.reply" class="comment-reply">回复：{{ item.reply }}</div>
              </article>
              <div v-if="!comments.length" class="empty-card">暂无评论信息</div>
            </div>
            <div class="pager">
              <el-pagination
                background
                layout="prev, pager, next"
                v-model:current-page="commentPage"
                :page-size="commentLimit"
                :total="commentTotal"
                @current-change="loadComments"
              />
            </div>
          </el-tab-pane>
        </el-tabs>
      </section>
    </div>

    <el-dialog v-model="signupVisible" title="活动报名" width="640px">
      <el-form ref="signupForm" :model="signupForm" label-width="96px">
        <el-form-item label="活动名称">
          <el-input v-model="signupForm.huodongmingcheng" readonly />
        </el-form-item>
        <el-form-item label="活动类型">
          <el-input v-model="signupForm.huodongleixing" readonly />
        </el-form-item>
        <el-form-item label="报名时间">
          <el-date-picker
            v-model="signupForm.baomingshijian"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="报名时间"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="报名说明">
          <el-input v-model="signupForm.baomingshuoming" placeholder="请输入报名说明" />
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="signupForm.zhanghao" readonly />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="signupForm.xingming" readonly />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="signupVisible = false">取消</el-button>
        <el-button type="primary" @click="submitSignup">提交报名</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import frontMixin from './front-mixin'

export default {
  name: 'FrontXianxiahuodong',
  mixins: [frontMixin],
  data() {
    return {
      filters: {
        huodongmingcheng: '',
        huodongleixing: ''
      },
      list: [],
      page: 1,
      limit: 6,
      total: 0,
      detail: {},
      swiperList: [],
      comments: [],
      commentForm: {
        content: ''
      },
      commentPage: 1,
      commentLimit: 10,
      commentTotal: 0,
      storeupFlag: 0,
      signupVisible: false,
      signupForm: {
        huodongmingcheng: '',
        huodongleixing: '',
        baomingshijian: '',
        baomingshuoming: '',
        zhanghao: '',
        xingming: ''
      }
    }
  },
  computed: {
    detailMode() {
      return !!this.$route.params.id
    }
  },
  watch: {
    '$route.fullPath': {
      immediate: true,
      handler() {
        this.initPage()
      }
    }
  },
  methods: {
    getActivityId(item) {
      return item && item.id ? item.id : ''
    },
    getActivityName(item) {
      return item && item.huodongmingcheng ? item.huodongmingcheng : '未命名活动'
    },
    getActivityType(item) {
      return item && item.huodongleixing ? item.huodongleixing : '未填写'
    },
    getActivityAddress(item) {
      return item && item.huodongdidian ? item.huodongdidian : '未填写'
    },
    getActivityContent(item) {
      return item && item.huodongneirong ? item.huodongneirong : ''
    },
    openActivity(item) {
      const id = this.getActivityId(item)
      if (!id) {
        return
      }
      this.goFront(`/front/xianxiahuodong/${id}`)
    },
    initPage() {
      if (this.detailMode) {
        this.loadDetail()
      } else {
        this.loadList()
      }
    },
    handleSearch() {
      this.page = 1
      this.loadList()
    },
    resetFilters() {
      this.filters.huodongmingcheng = ''
      this.filters.huodongleixing = ''
      this.handleSearch()
    },
    loadList() {
      const params = {
        page: this.page,
        limit: this.limit
      }
      if (this.filters.huodongmingcheng) {
        params.huodongmingcheng = `%${this.filters.huodongmingcheng}%`
      }
      if (this.filters.huodongleixing) {
        params.huodongleixing = `%${this.filters.huodongleixing}%`
      }
      this.$http({
        url: 'xianxiahuodong/list',
        method: 'get',
        params
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
        url: `xianxiahuodong/detail/${this.$route.params.id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.detail = data.data || {}
          this.detail.huodongneirong = this.normalizeRichText(this.detail.huodongneirong)
          this.swiperList = this.detail.huodongtupian ? this.detail.huodongtupian.split(',') : []
          this.prepareSignupForm()
          this.loadComments()
          this.checkStoreup()
        }
      })
    },
    loadComments() {
      if (!this.detail.id) {
        return
      }
      this.$http({
        url: 'discussxianxiahuodong/list',
        method: 'get',
        params: {
          page: this.commentPage,
          limit: this.commentLimit,
          refid: this.detail.id
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.comments = (data.data || {}).list || []
          this.commentTotal = (data.data || {}).total || 0
        } else {
          this.comments = []
          this.commentTotal = 0
        }
      }).catch(() => {
        this.comments = []
        this.commentTotal = 0
      })
    },
    submitComment() {
      if (!this.requireLogin('请先登录后再评论')) {
        return
      }
      const content = (this.commentForm.content || '').trim()
      if (!content) {
        this.$message.warning('请输入评论内容')
        return
      }
      this.$http({
        url: 'discussxianxiahuodong/save',
        method: 'post',
        data: {
          refid: this.detail.id,
          userid: this.currentUserId,
          nickname: this.currentUserName,
          content
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success('评论成功')
          this.commentForm.content = ''
          this.commentPage = 1
          this.loadComments()
        } else {
          this.$message.error((data && data.msg) || '评论失败')
        }
      })
    },
    checkStoreup() {
      if (!this.isLoggedIn || !this.detail.id) {
        this.storeupFlag = 0
        return
      }
      this.$http({
        url: 'storeup/list',
        method: 'get',
        params: {
          page: 1,
          limit: 1,
          type: 1,
          refid: this.detail.id,
          tablename: 'xianxiahuodong',
          userid: this.currentUserId
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.storeupFlag = ((data.data || {}).list || []).length
        }
      }).catch(() => {
        this.storeupFlag = 0
      })
    },
    toggleStoreup() {
      if (!this.requireLogin('请先登录后再收藏')) {
        return
      }
      this.$http({
        url: 'storeup/list',
        method: 'get',
        params: {
          page: 1,
          limit: 1,
          type: 1,
          refid: this.detail.id,
          tablename: 'xianxiahuodong',
          userid: this.currentUserId
        }
      }).then(({ data }) => {
        const list = (((data || {}).data || {}).data || {}).list || ((data || {}).data || {}).list || []
        if (list.length) {
          return this.$http({
            url: 'storeup/delete',
            method: 'post',
            data: [list[0].id]
          }).then(({ data: res }) => {
            if (res && res.code === 0) {
              this.$message.success('取消成功')
              this.storeupFlag = 0
            }
          })
        }
        return this.$http({
          url: 'storeup/save',
          method: 'post',
          data: {
            userid: this.currentUserId,
            name: this.detail.huodongmingcheng,
            type: 1,
            picture: this.detail.huodongtupian,
            refid: this.detail.id,
            tablename: 'xianxiahuodong'
          }
        }).then(({ data: res }) => {
          if (res && res.code === 0) {
            this.$message.success('收藏成功')
            this.storeupFlag = 1
          }
        })
      })
    },
    openSignupDialog() {
      if (!this.requireLogin('请先登录后再报名')) {
        return
      }
      this.fetchCurrentUser().then(user => {
        this.signupForm.zhanghao = user && user.zhanghao ? user.zhanghao : this.currentUserName
        this.signupForm.xingming = user && user.xingming ? user.xingming : ''
        this.signupForm.baomingshijian = this.getCurDateTime()
        this.signupVisible = true
      })
    },
    prepareSignupForm() {
      this.signupForm.huodongmingcheng = this.detail.huodongmingcheng || ''
      this.signupForm.huodongleixing = this.detail.huodongleixing || ''
    },
    submitSignup() {
      if (!this.signupForm.zhanghao || !this.signupForm.xingming) {
        this.$message.warning('请先登录有效的用户账号')
        return
      }
      this.$http({
        url: 'huodongbaoming/add',
        method: 'post',
        data: this.signupForm
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success('报名成功')
          this.signupVisible = false
        } else {
          this.$message.error((data && data.msg) || '报名失败')
        }
      })
    }
  }
}
</script>

<style scoped>
.page-card,
.detail-card {
  padding: 28px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.96);
  border: 1px solid #e6efff;
  box-shadow: 0 16px 36px rgba(15, 23, 42, 0.06);
}

.page-head,
.detail-header {
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

.filter-bar {
  margin-bottom: 20px;
}

.list-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
}

.card-item,
.empty-card,
.comment-item {
  border-radius: 18px;
  border: 1px solid #e6efff;
  background: #fff;
  box-shadow: 0 14px 30px rgba(15, 23, 42, 0.06);
}

.card-item {
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.24s ease, box-shadow 0.24s ease;
}

.card-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 38px rgba(15, 23, 42, 0.10);
}

.card-cover {
  width: 100%;
  height: 220px;
  object-fit: cover;
}

.card-body {
  padding: 18px;
}

.card-body h3 {
  margin: 0 0 10px;
  font-size: 18px;
}

.card-body p,
.meta-item {
  color: #667085;
  line-height: 1.8;
}

.detail-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.detail-main {
  display: grid;
  grid-template-columns: 420px 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.detail-image,
.hero-empty {
  width: 100%;
  height: 360px;
  border-radius: 16px;
}

.detail-image {
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

.detail-info h1 {
  margin: 0 0 22px;
  font-size: 32px;
}

.info-item {
  padding: 12px 0;
  border-bottom: 1px solid #edf2ff;
  color: #475467;
}

.info-item span {
  color: #101828;
  font-weight: 600;
}

.detail-tabs {
  margin-top: 8px;
}

.rich-text {
  line-height: 1.9;
  color: #475467;
}

.comment-form {
  margin-bottom: 18px;
}

.comment-actions {
  margin-top: 14px;
  text-align: right;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.comment-item {
  padding: 18px;
}

.comment-user {
  font-weight: 700;
  margin-bottom: 8px;
}

.comment-content,
.comment-reply {
  color: #667085;
  line-height: 1.8;
}

.comment-reply {
  margin-top: 8px;
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
  .list-grid,
  .detail-main {
    grid-template-columns: 1fr;
  }
}
</style>

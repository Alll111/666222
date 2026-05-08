<template>
  <div>
    <section v-if="mode === 'list'" class="page-card">
      <div class="page-head">
        <div>
          <div class="page-tag">Friends</div>
          <h2>交友信息</h2>
        </div>
        <el-button type="primary" @click="goAdd">发布交友信息</el-button>
      </div>

      <el-form :inline="true" :model="filters" class="filter-bar">
        <el-form-item label="姓名">
          <el-input v-model="filters.xingming" placeholder="姓名" clearable />
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
          @click="goFront(`/front/jiaoyouxinxi/${item.id}`)"
        >
          <img class="card-cover" :src="getImageUrl(item.jiaoyoutupian)" :alt="item.zhanghao">
          <div class="card-body">
            <h3>{{ item.zhanghao || '匿名用户' }}</h3>
            <p>{{ item.xingming || '未填写姓名' }}</p>
            <div class="meta-item">兴趣：{{ item.xingquaihao || '未填写' }}</div>
            <div class="meta-item">目的：{{ item.jiaoyoumude || '未填写' }}</div>
          </div>
        </article>
        <div v-if="!list.length" class="empty-card">暂无交友信息</div>
      </div>

      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          :current-page.sync="page"
          :page-size="limit"
          :total="total"
          @current-change="loadList"
        />
      </div>
    </section>

    <section v-else-if="mode === 'add'" class="page-card">
      <div class="page-head">
        <div>
          <div class="page-tag">Publish</div>
          <h2>{{ form.id ? '编辑交友信息' : '发布交友信息' }}</h2>
        </div>
        <el-button type="text" @click="goFront('/front/jiaoyouxinxi')">返回列表</el-button>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="92px" class="publish-form">
        <el-row :gutter="20">
          <el-col :md="12" :xs="24">
            <el-form-item label="账号" prop="zhanghao">
              <el-input v-model="form.zhanghao" readonly />
            </el-form-item>
          </el-col>
          <el-col :md="12" :xs="24">
            <el-form-item label="姓名" prop="xingming">
              <el-input v-model="form.xingming" readonly />
            </el-form-item>
          </el-col>
          <el-col :md="12" :xs="24">
            <el-form-item label="性别" prop="xingbie">
              <el-input v-model="form.xingbie" readonly />
            </el-form-item>
          </el-col>
          <el-col :md="12" :xs="24">
            <el-form-item label="年龄" prop="nianling">
              <el-input v-model="form.nianling" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="交友图片" prop="jiaoyoutupian">
              <file-upload
                tip="点击上传交友图片"
                action="file/upload"
                :limit="3"
                :multiple="true"
                :fileUrls="form.jiaoyoutupian || ''"
                @change="handleUploadChange"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="兴趣爱好" prop="xingquaihao">
              <el-input v-model="form.xingquaihao" type="textarea" :rows="4" placeholder="请输入兴趣爱好" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="交友目的" prop="jiaoyoumude">
              <el-input v-model="form.jiaoyoumude" type="textarea" :rows="4" placeholder="请输入交友目的" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="个人简介" prop="gerenjianjie">
              <editor v-model="form.gerenjianjie" action="file/upload" />
            </el-form-item>
          </el-col>
        </el-row>
        <div class="form-actions">
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="goFront('/front/jiaoyouxinxi')">取消</el-button>
        </div>
      </el-form>
    </section>

    <section v-else class="detail-card">
      <div class="detail-header">
        <el-button type="text" @click="goFront('/front/jiaoyouxinxi')">返回列表</el-button>
        <div class="detail-actions">
          <el-button type="primary" @click="addFriend">添加好友</el-button>
          <el-button @click="toggleStoreup">{{ storeupFlag ? '取消收藏' : '点我收藏' }}</el-button>
        </div>
      </div>

      <div class="detail-main">
        <div class="detail-gallery">
          <el-carousel v-if="swiperList.length" height="360px" arrow="always">
            <el-carousel-item v-for="(item, index) in swiperList" :key="index">
              <img class="detail-image" :src="getImageUrl(item)" :alt="detail.zhanghao">
            </el-carousel-item>
          </el-carousel>
          <div v-else class="hero-empty">暂无交友图片</div>

          <div class="tool-box">
            <button class="tool-btn" @click="toggleThumb('21')">{{ thumbsupFlag ? '取消赞' : '赞一下' }} ({{ detail.thumbsupnum || 0 }})</button>
            <button class="tool-btn" @click="toggleThumb('22')">{{ crazilyFlag ? '取消踩' : '踩一下' }} ({{ detail.crazilynum || 0 }})</button>
          </div>
        </div>

        <div class="detail-info">
          <h1>{{ detail.zhanghao }}</h1>
          <div class="info-item"><span>姓名：</span>{{ detail.xingming || '未填写' }}</div>
          <div class="info-item"><span>性别：</span>{{ detail.xingbie || '未填写' }}</div>
          <div class="info-item"><span>年龄：</span>{{ detail.nianling || '未填写' }}</div>
          <div class="info-item"><span>兴趣爱好：</span>{{ detail.xingquaihao || '未填写' }}</div>
          <div class="info-item"><span>交友目的：</span>{{ detail.jiaoyoumude || '未填写' }}</div>
          <div class="info-item"><span>点击次数：</span>{{ detail.clicknum || 0 }}</div>
        </div>
      </div>

      <el-tabs class="detail-tabs">
        <el-tab-pane label="个人简介">
          <div class="rich-text" v-html="detail.gerenjianjie" />
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
              <div class="comment-user">{{ item.nickname || '匿名用户' }}</div>
              <div class="comment-content">{{ item.content }}</div>
              <div v-if="item.reply" class="comment-reply">回复：{{ item.reply }}</div>
            </article>
            <div v-if="!comments.length" class="empty-card">暂无评论信息</div>
          </div>
          <div class="pager">
            <el-pagination
              background
              layout="prev, pager, next"
              :current-page.sync="commentPage"
              :page-size="commentLimit"
              :total="commentTotal"
              @current-change="loadComments"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </section>
  </div>
</template>

<script>
import frontMixin from './front-mixin'

export default {
  name: 'FrontJiaoyouxinxi',
  mixins: [frontMixin],
  data() {
    return {
      filters: {
        xingming: ''
      },
      list: [],
      page: 1,
      limit: 6,
      total: 0,
      detail: {},
      swiperList: [],
      storeupFlag: 0,
      thumbsupFlag: 0,
      crazilyFlag: 0,
      comments: [],
      commentForm: {
        content: ''
      },
      commentPage: 1,
      commentLimit: 10,
      commentTotal: 0,
      form: {
        id: '',
        zhanghao: '',
        xingming: '',
        xingbie: '',
        nianling: '',
        xingquaihao: '',
        jiaoyoumude: '',
        gerenjianjie: '',
        jiaoyoutupian: ''
      },
      rules: {
        xingquaihao: [{ required: true, message: '请输入兴趣爱好', trigger: 'blur' }],
        jiaoyoumude: [{ required: true, message: '请输入交友目的', trigger: 'blur' }]
      }
    }
  },
  computed: {
    mode() {
      if (this.$route.path.endsWith('/add')) {
        return 'add'
      }
      if (this.$route.params.id) {
        return 'detail'
      }
      return 'list'
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
    initPage() {
      if (this.mode === 'list') {
        this.loadList()
      } else if (this.mode === 'add') {
        this.prepareForm()
      } else {
        this.loadDetail()
      }
    },
    handleSearch() {
      this.page = 1
      this.loadList()
    },
    resetFilters() {
      this.filters.xingming = ''
      this.handleSearch()
    },
    loadList() {
      const params = {
        page: this.page,
        limit: this.limit,
        sfsh: '是'
      }
      if (this.filters.xingming) {
        params.xingming = `%${this.filters.xingming}%`
      }
      this.$http({
        url: 'jiaoyouxinxi/list',
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
    goAdd() {
      if (!this.requireLogin('请先登录后再发布交友信息')) {
        return
      }
      this.goFront('/front/jiaoyouxinxi/add')
    },
    prepareForm() {
      if (!this.requireLogin('请先登录后再发布交友信息')) {
        return
      }
      this.fetchCurrentUser().then(user => {
        if (!user) {
          return
        }
        this.form = Object.assign({}, this.form, {
          zhanghao: user.zhanghao || '',
          xingming: user.xingming || '',
          xingbie: user.xingbie || '',
          nianling: user.nianling || ''
        })
        const editId = this.$route.query.id
        if (editId) {
          this.$http({
            url: `jiaoyouxinxi/info/${editId}`,
            method: 'get'
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.form = Object.assign({}, this.form, data.data || {})
              this.form.gerenjianjie = this.normalizeRichText(this.form.gerenjianjie)
            }
          })
        }
      })
    },
    handleUploadChange(fileUrls) {
      this.form.jiaoyoutupian = fileUrls
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) {
          return
        }
        const payload = Object.assign({}, this.form, {
          jiaoyoutupian: this.normalizeUploadPath(this.form.jiaoyoutupian)
        })
        this.$http({
          url: `jiaoyouxinxi/${payload.id ? 'update' : 'add'}`,
          method: 'post',
          data: payload
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success(payload.id ? '更新成功' : '发布成功')
            this.goFront('/front/jiaoyouxinxi')
          } else {
            this.$message.error((data && data.msg) || '提交失败')
          }
        })
      })
    },
    loadDetail() {
      this.$http({
        url: `jiaoyouxinxi/detail/${this.$route.params.id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.detail = data.data || {}
          this.detail.gerenjianjie = this.normalizeRichText(this.detail.gerenjianjie)
          this.swiperList = this.detail.jiaoyoutupian ? this.detail.jiaoyoutupian.split(',') : []
          this.loadComments()
          this.checkStoreup()
          this.checkVoteState()
        }
      })
    },
    loadComments() {
      if (!this.detail.id) {
        return
      }
      this.$http({
        url: 'discussjiaoyouxinxi/list',
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
        url: 'discussjiaoyouxinxi/save',
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
    addFriend() {
      if (!this.requireLogin('请先登录后再添加好友')) {
        return
      }
      this.$http({
        url: 'haoyoushenqing/send',
        method: 'post',
        data: {
          toZhanghao: this.detail.zhanghao,
          refJiaoyouxinxiId: this.detail.id
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success('已发送好友申请')
        } else {
          this.$message.error((data && data.msg) || '发送失败')
        }
      })
    },
    checkStoreup() {
      if (!this.isLoggedIn) {
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
          tablename: 'jiaoyouxinxi',
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
          tablename: 'jiaoyouxinxi',
          userid: this.currentUserId
        }
      }).then(({ data }) => {
        const list = ((data.data || {}).list) || []
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
            type: 1,
            name: this.detail.zhanghao,
            picture: this.detail.jiaoyoutupian,
            refid: this.detail.id,
            tablename: 'jiaoyouxinxi'
          }
        }).then(({ data: res }) => {
          if (res && res.code === 0) {
            this.$message.success('收藏成功')
            this.storeupFlag = 1
          }
        })
      })
    },
    checkVoteState() {
      if (!this.isLoggedIn) {
        this.thumbsupFlag = 0
        this.crazilyFlag = 0
        return
      }
      this.$http({
        url: 'storeup/list',
        method: 'get',
        params: {
          page: 1,
          limit: 1,
          type: '%2%',
          refid: this.detail.id,
          tablename: 'jiaoyouxinxi',
          userid: this.currentUserId
        }
      }).then(({ data }) => {
        const list = ((data.data || {}).list) || []
        this.thumbsupFlag = list.length && list[0].type === '21' ? 1 : 0
        this.crazilyFlag = list.length && list[0].type === '22' ? 1 : 0
      }).catch(() => {
        this.thumbsupFlag = 0
        this.crazilyFlag = 0
      })
    },
    toggleThumb(type) {
      if (!this.requireLogin('请先登录后再操作')) {
        return
      }
      this.$http({
        url: 'storeup/list',
        method: 'get',
        params: {
          page: 1,
          limit: 1,
          type: '%2%',
          refid: this.detail.id,
          tablename: 'jiaoyouxinxi',
          userid: this.currentUserId
        }
      }).then(({ data }) => {
        const list = ((data.data || {}).list) || []
        if (list.length) {
          return this.$http({
            url: 'storeup/delete',
            method: 'post',
            data: [list[0].id]
          }).then(({ data: res }) => {
            if (res && res.code === 0) {
              if (list[0].type === '21') {
                this.detail.thumbsupnum = Math.max(0, Number(this.detail.thumbsupnum || 0) - 1)
              } else {
                this.detail.crazilynum = Math.max(0, Number(this.detail.crazilynum || 0) - 1)
              }
              return this.syncVoteCounts('取消成功')
            }
          })
        }
        return this.$http({
          url: 'storeup/save',
          method: 'post',
          data: {
            userid: this.currentUserId,
            type,
            name: this.detail.zhanghao,
            picture: this.detail.jiaoyoutupian,
            refid: this.detail.id,
            tablename: 'jiaoyouxinxi'
          }
        }).then(({ data: res }) => {
          if (res && res.code === 0) {
            if (type === '21') {
              this.detail.thumbsupnum = Number(this.detail.thumbsupnum || 0) + 1
            } else {
              this.detail.crazilynum = Number(this.detail.crazilynum || 0) + 1
            }
            return this.syncVoteCounts(type === '21' ? '点赞成功' : '点踩成功')
          }
        })
      })
    },
    syncVoteCounts(successText) {
      this.$http({
        url: 'jiaoyouxinxi/update',
        method: 'post',
        data: this.detail
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success(successText)
          this.checkVoteState()
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
.meta-item,
.comment-content,
.comment-reply,
.rich-text {
  color: #667085;
  line-height: 1.8;
}

.publish-form {
  max-width: 960px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 14px;
  margin-top: 20px;
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

.tool-box {
  display: flex;
  gap: 10px;
  margin-top: 14px;
}

.tool-btn {
  flex: 1;
  height: 42px;
  border-radius: 12px;
  border: 1px solid #dbe7ff;
  background: #f8fbff;
  color: #1f6fff;
  cursor: pointer;
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

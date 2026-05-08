<template>
  <div>
    <section v-if="mode === 'list'" class="page-card">
      <div class="page-head">
        <div>
          <div class="page-tag">Forum</div>
          <h2>交流论坛</h2>
        </div>
        <div class="page-actions">
          <el-button @click="goMyPosts">我的发布</el-button>
          <el-button type="primary" @click="goAdd">发布帖子</el-button>
        </div>
      </div>

      <el-form :inline="true" :model="filters" class="filter-bar">
        <el-form-item label="标题">
          <el-input v-model="filters.title" placeholder="标题" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetFilters">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="forum-list">
        <article v-for="item in list" :key="item.id" class="forum-item" @click="goFront(`/front/forum/${item.id}`)">
          <h3>{{ item.title }}</h3>
          <div class="forum-meta">发布人：{{ item.username || '匿名用户' }}</div>
          <div class="forum-meta">时间：{{ formatDate(item.addtime) }}</div>
        </article>
        <div v-if="!list.length" class="empty-card">暂无论坛帖子</div>
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

    <section v-else-if="mode === 'my'" class="page-card">
      <div class="page-head">
        <div>
          <div class="page-tag">Mine</div>
          <h2>我的发布</h2>
        </div>
        <el-button type="text" @click="goFront('/front/forum')">返回论坛</el-button>
      </div>

      <el-table :data="list" border>
        <el-table-column prop="title" label="标题" min-width="260" />
        <el-table-column prop="addtime" label="发布时间" width="180" />
        <el-table-column label="操作" width="220">
          <template slot-scope="{ row }">
            <el-button size="mini" @click="goFront(`/front/forum/${row.id}`)">详情</el-button>
            <el-button size="mini" type="primary" @click="goFront(`/front/forum/add?id=${row.id}`)">编辑</el-button>
            <el-button size="mini" type="danger" @click="removePost(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pager">
        <el-pagination
          background
          layout="prev, pager, next"
          :current-page.sync="page"
          :page-size="limit"
          :total="total"
          @current-change="loadMyPosts"
        />
      </div>
    </section>

    <section v-else-if="mode === 'add'" class="page-card">
      <div class="page-head">
        <div>
          <div class="page-tag">Publish</div>
          <h2>{{ form.id ? '编辑帖子' : '发布帖子' }}</h2>
        </div>
        <el-button type="text" @click="goFront('/front/forum')">返回论坛</el-button>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" class="publish-form">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="类型" prop="isdone">
          <el-radio-group v-model="form.isdone">
            <el-radio label="开放">公开</el-radio>
            <el-radio label="关闭">私人</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <editor v-model="form.content" action="file/upload" />
        </el-form-item>
        <div class="form-actions">
          <el-button type="primary" @click="submitForm">发布帖子</el-button>
          <el-button @click="goFront('/front/forum')">取消</el-button>
        </div>
      </el-form>
    </section>

    <section v-else class="page-card">
      <div class="page-head">
        <el-button type="text" @click="goFront('/front/forum')">返回论坛</el-button>
      </div>
      <article class="forum-detail">
        <h1>{{ detail.title }}</h1>
        <div class="forum-meta">发布人：{{ detail.username || '匿名用户' }} | 时间：{{ formatDate(detail.addtime) }}</div>
        <div class="rich-text" v-html="detail.content" />
      </article>

      <div class="comment-card">
        <div class="comment-head">
          <div>评论列表</div>
        </div>
        <div class="comment-form">
          <el-input type="textarea" :rows="4" v-model="commentForm.content" placeholder="请输入评论内容" />
          <div class="comment-actions">
            <el-button type="primary" @click="submitComment">点击评论</el-button>
          </div>
        </div>
        <div class="comment-list">
          <article v-for="item in comments" :key="item.id" class="comment-item">
            <div class="comment-user">{{ item.username || '匿名用户' }}</div>
            <div class="comment-content">{{ item.content }}</div>
          </article>
          <div v-if="!comments.length" class="empty-card">暂无评论信息</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import frontMixin from './front-mixin'

export default {
  name: 'FrontForum',
  mixins: [frontMixin],
  data() {
    return {
      filters: {
        title: ''
      },
      list: [],
      page: 1,
      limit: 10,
      total: 0,
      detail: {},
      comments: [],
      commentForm: {
        content: ''
      },
      form: {
        id: '',
        title: '',
        isdone: '开放',
        content: '',
        parentid: 0,
        username: ''
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        isdone: [{ required: true, message: '请选择类型', trigger: 'change' }]
      }
    }
  },
  computed: {
    mode() {
      if (this.$route.path.endsWith('/add')) {
        return 'add'
      }
      if (this.$route.path.endsWith('/my')) {
        return 'my'
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
      } else if (this.mode === 'my') {
        this.loadMyPosts()
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
      this.filters.title = ''
      this.handleSearch()
    },
    loadList() {
      this.$http({
        url: 'forum/flist?isdone=开放&sort=addtime&order=desc',
        method: 'get',
        params: {
          page: this.page,
          limit: this.limit,
          title: `%${this.filters.title || ''}%`
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
    loadMyPosts() {
      if (!this.requireLogin('请先登录后查看我的发布')) {
        return
      }
      this.$http({
        url: 'forum/page?parentid=0&sort=addtime&order=desc',
        method: 'get',
        params: {
          page: this.page,
          limit: this.limit
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
    goAdd() {
      if (!this.requireLogin('请先登录后再发帖')) {
        return
      }
      this.goFront('/front/forum/add')
    },
    goMyPosts() {
      if (!this.requireLogin('请先登录后查看我的发布')) {
        return
      }
      this.goFront('/front/forum/my')
    },
    prepareForm() {
      if (!this.requireLogin('请先登录后再发帖')) {
        return
      }
      this.form.username = this.currentUserName
      const editId = this.$route.query.id
      if (editId) {
        this.$http({
          url: `forum/info/${editId}`,
          method: 'get'
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.form = Object.assign({}, this.form, data.data || {})
          }
        })
      }
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) {
          return
        }
        const payload = Object.assign({}, this.form, {
          username: this.currentUserName,
          parentid: 0
        })
        this.$http({
          url: payload.id ? 'forum/update' : 'forum/add',
          method: 'post',
          data: payload
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success(payload.id ? '更新成功' : '发布成功')
            this.goFront('/front/forum')
          } else {
            this.$message.error((data && data.msg) || '提交失败')
          }
        })
      })
    },
    loadDetail() {
      this.$http({
        url: `forum/list/${this.$route.params.id}`,
        method: 'get'
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.detail = data.data || {}
          this.detail.content = this.normalizeRichText(this.detail.content)
          this.comments = this.detail.childs || []
        }
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
        url: 'forum/add',
        method: 'post',
        data: {
          title: `回复：${this.detail.title}`,
          content,
          isdone: this.detail.isdone || '开放',
          parentid: this.detail.id,
          username: this.currentUserName,
          userid: this.currentUserId
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success('发表成功')
          this.commentForm.content = ''
          this.loadDetail()
        } else {
          this.$message.error((data && data.msg) || '评论失败')
        }
      })
    },
    removePost(id) {
      this.$confirm('确定删除该帖子吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'forum/delete',
          method: 'post',
          data: [id]
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.loadMyPosts()
          }
        })
      }).catch(() => {})
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

.page-head,
.comment-head {
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

.page-actions {
  display: flex;
  gap: 12px;
}

.filter-bar {
  margin-bottom: 20px;
}

.forum-list,
.comment-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.forum-item,
.comment-item,
.empty-card,
.comment-card {
  border-radius: 18px;
  border: 1px solid #e6efff;
  background: #fff;
  box-shadow: 0 14px 30px rgba(15, 23, 42, 0.06);
}

.forum-item {
  padding: 20px;
  cursor: pointer;
  transition: transform 0.24s ease, box-shadow 0.24s ease;
}

.forum-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 18px 32px rgba(15, 23, 42, 0.10);
}

.forum-item h3,
.forum-detail h1 {
  margin: 0 0 10px;
}

.forum-meta,
.comment-content,
.rich-text {
  color: #667085;
  line-height: 1.8;
}

.forum-detail {
  padding-bottom: 20px;
  border-bottom: 1px solid #edf2ff;
}

.comment-card {
  margin-top: 24px;
  padding: 22px;
}

.comment-form {
  margin-bottom: 18px;
}

.comment-actions {
  text-align: right;
  margin-top: 14px;
}

.comment-item {
  padding: 16px 18px;
}

.comment-user {
  font-weight: 700;
  margin-bottom: 8px;
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

.pager {
  display: flex;
  justify-content: center;
  margin-top: 22px;
}

.empty-card {
  min-height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #8aa0c6;
}
</style>

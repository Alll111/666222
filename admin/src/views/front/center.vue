<template>
  <div class="center-page">
    <section class="page-card">
      <div class="page-head">
        <div>
          <div class="page-tag">Center</div>
          <h2>个人中心</h2>
        </div>
      </div>

      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <el-tab-pane label="个人资料" name="profile">
          <el-form ref="profileFormRef" :model="profileForm" :rules="profileRules" label-width="92px" class="profile-form">
            <el-row :gutter="20">
              <el-col :md="12" :xs="24">
                <el-form-item label="账号" prop="zhanghao">
                  <el-input v-model="profileForm.zhanghao" readonly />
                </el-form-item>
              </el-col>
              <el-col :md="12" :xs="24">
                <el-form-item label="密码" prop="mima">
                  <el-input v-model="profileForm.mima" show-password />
                </el-form-item>
              </el-col>
              <el-col :md="12" :xs="24">
                <el-form-item label="姓名" prop="xingming">
                  <el-input v-model="profileForm.xingming" />
                </el-form-item>
              </el-col>
              <el-col :md="12" :xs="24">
                <el-form-item label="性别" prop="xingbie">
                  <el-select v-model="profileForm.xingbie" placeholder="请选择性别" style="width: 100%;">
                    <el-option label="男" value="男" />
                    <el-option label="女" value="女" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :md="12" :xs="24">
                <el-form-item label="年龄" prop="nianling">
                  <el-input v-model="profileForm.nianling" />
                </el-form-item>
              </el-col>
              <el-col :md="12" :xs="24">
                <el-form-item label="手机" prop="shouji">
                  <el-input v-model="profileForm.shouji" />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="头像" prop="touxiang">
                  <file-upload
                    tip="点击上传头像"
                    action="file/upload"
                    :limit="3"
                    :multiple="true"
                    :fileUrls="profileForm.touxiang || ''"
                    @change="handleAvatarChange"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <div class="form-actions">
              <el-button type="primary" @click="submitProfile">更新信息</el-button>
              <el-button @click="logoutFront">退出登录</el-button>
            </div>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="我的收藏" name="storeup">
          <div class="list-grid">
            <article v-for="item in storeupList" :key="item.id" class="entry-card" @click="openStoreup(item)">
              <img class="entry-cover" :src="getImageUrl(item.picture)" :alt="item.name">
              <div class="entry-body">
                <h3>{{ item.name }}</h3>
                <p>{{ item.tablename }}</p>
              </div>
            </article>
            <div v-if="!storeupList.length" class="empty-card">暂无收藏内容</div>
          </div>
          <div class="pager">
            <el-pagination
              background
              layout="prev, pager, next"
              :current-page.sync="storeupPage"
              :page-size="limit"
              :total="storeupTotal"
              @current-change="loadStoreups"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="活动报名" name="signup">
          <el-table :data="signupList" border>
            <el-table-column prop="huodongmingcheng" label="活动名称" min-width="220" />
            <el-table-column prop="huodongleixing" label="活动类型" width="120" />
            <el-table-column prop="baomingshijian" label="报名时间" width="180" />
            <el-table-column prop="baomingshuoming" label="报名说明" min-width="220" />
          </el-table>
          <div class="pager">
            <el-pagination
              background
              layout="prev, pager, next"
              :current-page.sync="signupPage"
              :page-size="limit"
              :total="signupTotal"
              @current-change="loadSignups"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="我的帖子" name="post">
          <el-table :data="postList" border>
            <el-table-column prop="title" label="标题" min-width="240" />
            <el-table-column prop="isdone" label="类型" width="100" />
            <el-table-column prop="addtime" label="发布时间" width="180" />
            <el-table-column label="操作" width="180">
              <template slot-scope="{ row }">
                <el-button size="mini" @click="goFront(`/front/forum/${row.id}`)">查看</el-button>
                <el-button size="mini" type="primary" @click="goFront(`/front/forum/add?id=${row.id}`)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pager">
            <el-pagination
              background
              layout="prev, pager, next"
              :current-page.sync="postPage"
              :page-size="limit"
              :total="postTotal"
              @current-change="loadPosts"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="好友申请" name="friend">
          <el-table :data="friendList" border>
            <el-table-column prop="fromZhanghao" label="账号" width="120" />
            <el-table-column prop="fromXingming" label="姓名" width="120" />
            <el-table-column prop="addtime" label="时间" width="180" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column prop="reply" label="回复" min-width="220" />
            <el-table-column label="操作" width="240">
              <template slot-scope="{ row }">
                <el-button v-if="row.status === '待处理'" size="mini" type="primary" @click="handleAudit(row, '同意')">同意</el-button>
                <el-button v-if="row.status === '待处理'" size="mini" type="danger" @click="handleAudit(row, '拒绝')">拒绝</el-button>
                <el-button size="mini" @click="deleteFriendReq(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pager">
            <el-pagination
              background
              layout="prev, pager, next"
              :current-page.sync="friendPage"
              :page-size="limit"
              :total="friendTotal"
              @current-change="loadFriendList"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="私聊消息" name="chat">
          <siliao-panel ref="siliaoPanel" />
        </el-tab-pane>
      </el-tabs>
    </section>
  </div>
</template>

<script>
import frontMixin from './front-mixin'
import SiliaoPanel from '@/components/common/SiliaoPanel'

export default {
  name: 'FrontCenter',
  components: { SiliaoPanel },
  mixins: [frontMixin],
  data() {
    return {
      activeTab: 'profile',
      limit: 8,
      profileForm: {
        id: '',
        zhanghao: '',
        mima: '',
        xingming: '',
        xingbie: '',
        nianling: '',
        shouji: '',
        touxiang: ''
      },
      profileRules: {
        mima: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
        xingming: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
        nianling: [{ pattern: /^\d+$/, message: '年龄应输入数字', trigger: 'blur' }],
        shouji: [{ pattern: /^1\d{10}$/, message: '手机应输入手机格式', trigger: 'blur' }]
      },
      storeupList: [],
      storeupPage: 1,
      storeupTotal: 0,
      signupList: [],
      signupPage: 1,
      signupTotal: 0,
      postList: [],
      postPage: 1,
      postTotal: 0,
      friendList: [],
      friendPage: 1,
      friendTotal: 0
    }
  },
  created() {
    if (!this.requireLogin('请先登录后访问个人中心')) {
      return
    }
    this.activeTab = this.$route.query.tab || 'profile'
    this.loadProfile()
    this.loadByTab(this.activeTab)
  },
  methods: {
    handleTabChange() {
      this.loadByTab(this.activeTab)
      this.$router.replace({ path: '/front/center', query: { tab: this.activeTab } }).catch(() => {})
    },
    loadByTab(tab) {
      if (tab === 'storeup') {
        this.loadStoreups()
      } else if (tab === 'signup') {
        this.loadSignups()
      } else if (tab === 'post') {
        this.loadPosts()
      } else if (tab === 'friend') {
        this.loadFriendList()
      } else if (tab === 'chat') {
        this.$nextTick(() => {
          if (this.$refs.siliaoPanel) {
            this.$refs.siliaoPanel.open()
          }
        })
      }
    },
    loadProfile() {
      this.fetchCurrentUser().then(user => {
        if (user) {
          this.profileForm = Object.assign({}, this.profileForm, user)
        }
      })
    },
    handleAvatarChange(fileUrls) {
      this.profileForm.touxiang = fileUrls
    },
    submitProfile() {
      this.$refs.profileFormRef.validate(valid => {
        if (!valid) {
          return
        }
        const payload = Object.assign({}, this.profileForm, {
          touxiang: this.normalizeUploadPath(this.profileForm.touxiang)
        })
        this.$http({
          url: 'yonghu/update',
          method: 'post',
          data: payload
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('修改成功')
            this.loadProfile()
          } else {
            this.$message.error((data && data.msg) || '修改失败')
          }
        })
      })
    },
    loadStoreups() {
      this.$http({
        url: 'storeup/list',
        method: 'get',
        params: {
          page: this.storeupPage,
          limit: this.limit,
          type: 1,
          userid: this.currentUserId
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.storeupList = (data.data || {}).list || []
          this.storeupTotal = (data.data || {}).total || 0
        } else {
          this.storeupList = []
          this.storeupTotal = 0
        }
      }).catch(() => {
        this.storeupList = []
        this.storeupTotal = 0
      })
    },
    openStoreup(item) {
      const pathMap = {
        xianxiahuodong: `/front/xianxiahuodong/${item.refid}`,
        jiaoyouxinxi: `/front/jiaoyouxinxi/${item.refid}`
      }
      if (pathMap[item.tablename]) {
        this.goFront(pathMap[item.tablename])
      }
    },
    loadSignups() {
      this.$http({
        url: 'huodongbaoming/page',
        method: 'get',
        params: {
          page: this.signupPage,
          limit: this.limit
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.signupList = (data.data || {}).list || []
          this.signupTotal = (data.data || {}).total || 0
        } else {
          this.signupList = []
          this.signupTotal = 0
        }
      }).catch(() => {
        this.signupList = []
        this.signupTotal = 0
      })
    },
    loadPosts() {
      this.$http({
        url: 'forum/page?parentid=0&sort=addtime&order=desc',
        method: 'get',
        params: {
          page: this.postPage,
          limit: this.limit
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.postList = (data.data || {}).list || []
          this.postTotal = (data.data || {}).total || 0
        } else {
          this.postList = []
          this.postTotal = 0
        }
      }).catch(() => {
        this.postList = []
        this.postTotal = 0
      })
    },
    loadFriendList() {
      this.$http({
        url: 'haoyoushenqing/inboxList',
        method: 'get',
        params: {
          page: this.friendPage,
          limit: this.limit
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.friendList = (data.data || {}).list || []
          this.friendTotal = (data.data || {}).total || 0
        } else {
          this.friendList = []
          this.friendTotal = 0
        }
      }).catch(() => {
        this.friendList = []
        this.friendTotal = 0
      })
    },
    handleAudit(row, status) {
      this.$prompt('请输入审核回复', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.$http({
          url: 'haoyoushenqing/audit',
          method: 'post',
          data: {
            id: row.id,
            status,
            reply: value
          }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('处理成功')
            this.loadFriendList()
          } else {
            this.$message.error((data && data.msg) || '处理失败')
          }
        })
      }).catch(() => {})
    },
    deleteFriendReq(row) {
      this.$confirm('确定删除该申请记录？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'haoyoushenqing/delete',
          method: 'post',
          data: [row.id]
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.loadFriendList()
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

.page-head {
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

.profile-form {
  max-width: 980px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 14px;
  margin-top: 20px;
}

.list-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

.entry-card,
.empty-card {
  border-radius: 18px;
  border: 1px solid #e6efff;
  background: #fff;
  box-shadow: 0 14px 30px rgba(15, 23, 42, 0.06);
}

.entry-card {
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.24s ease, box-shadow 0.24s ease;
}

.entry-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 38px rgba(15, 23, 42, 0.10);
}

.entry-cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  background: #f5f7fb;
}

.entry-body {
  padding: 16px;
}

.entry-body h3 {
  margin: 0 0 8px;
  font-size: 16px;
}

.entry-body p {
  margin: 0;
  color: #667085;
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

@media (max-width: 992px) {
  .list-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 768px) {
  .list-grid {
    grid-template-columns: 1fr;
  }
}
</style>

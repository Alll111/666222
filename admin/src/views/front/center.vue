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
              <img class="entry-cover" :src="getImageUrl(item && item.picture)" :alt="getStoreupName(item)">
              <div class="entry-body">
                <h3>{{ getStoreupName(item) }}</h3>
                <p>{{ getStoreupTableName(item) }}</p>
              </div>
            </article>
            <div v-if="!storeupList.length" class="empty-card">暂无收藏内容</div>
          </div>
          <div class="pager">
            <el-pagination
              background
              layout="prev, pager, next"
              v-model:current-page="storeupPage"
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
              v-model:current-page="signupPage"
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
              <template #default="scope">
                <el-button size="small" @click="openPost(scope?.row)">查看</el-button>
                <el-button size="small" type="primary" @click="editPost(scope?.row)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pager">
            <el-pagination
              background
              layout="prev, pager, next"
              v-model:current-page="postPage"
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
            <el-table-column label="状态" width="100">
              <template #default="scope">
                {{ getFriendStatus(scope?.row) }}
              </template>
            </el-table-column>
            <el-table-column prop="reply" label="回复" min-width="220" />
            <el-table-column label="操作" width="240">
              <template #default="scope">
                <el-button
                  v-if="canAuditFriend(scope?.row)"
                  size="small"
                  type="primary"
                  @click="handleAudit(scope?.row, '同意')"
                >
                  同意
                </el-button>
                <el-button
                  v-if="canAuditFriend(scope?.row)"
                  size="small"
                  type="danger"
                  @click="handleAudit(scope?.row, '拒绝')"
                >
                  拒绝
                </el-button>
                <el-button size="small" @click="deleteFriendReq(scope?.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pager">
            <el-pagination
              background
              layout="prev, pager, next"
              v-model:current-page="friendPage"
              :page-size="limit"
              :total="friendTotal"
              @current-change="loadFriendList"
            />
          </div>
        </el-tab-pane>

        <el-tab-pane label="交友信息" name="jiaoyou">
          <div class="section-card">
            <div class="section-head">
              <div>
                <h3>已添加好友</h3>
                <p>查看好友近况、未读消息并快速发起私聊</p>
              </div>
            </div>
            <el-table :data="addedFriendList" border empty-text="暂无已添加好友">
              <el-table-column label="头像" width="88">
                <template #default="scope">
                  <el-avatar v-if="getImageUrl(scope?.row?.touxiang)" :src="getImageUrl(scope?.row?.touxiang)" />
                  <el-avatar v-else>{{ getAvatarText(scope?.row) }}</el-avatar>
                </template>
              </el-table-column>
              <el-table-column prop="zhanghao" label="账号" width="120" />
              <el-table-column prop="xingming" label="姓名" width="120" />
              <el-table-column prop="xingbie" label="性别" width="90" />
              <el-table-column label="在线状态" width="120">
                <template #default="scope">
                  <el-tag :type="getOnlineStatus(scope?.row) === '在线' ? 'success' : 'info'">
                    {{ getOnlineStatus(scope?.row) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="未读消息" width="110">
                <template #default="scope">
                  <el-badge :value="getUnreadCount(scope?.row)" :hidden="getUnreadCount(scope?.row) <= 0">
                    <span class="minor-text">{{ getUnreadCount(scope?.row) > 0 ? '有新消息' : '暂无未读' }}</span>
                  </el-badge>
                </template>
              </el-table-column>
              <el-table-column label="最近互动" min-width="180">
                <template #default="scope">
                  {{ formatDate(scope?.row?.lastTime || scope?.row?.friendAddtime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="250">
                <template #default="scope">
                  <el-button size="small" type="primary" @click="openFriendChat(scope?.row)">私聊</el-button>
                  <el-button size="small" @click="openFriendDetail(scope?.row)">详情</el-button>
                  <el-button size="small" type="danger" @click="deleteAddedFriend(scope?.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pager">
              <el-pagination
                background
                layout="prev, pager, next"
                v-model:current-page="addedFriendPage"
                :page-size="limit"
                :total="addedFriendTotal"
                @current-change="loadAddedFriendList"
              />
            </div>
          </div>

          <div class="section-card">
            <div class="section-head section-head-wrap">
              <div>
                <h3>我发布的交友信息</h3>
                <p>支持按审核状态筛选、查看详情、修改、查看评论和删除</p>
              </div>
              <div class="section-actions">
                <el-select v-model="myJiaoyouStatus" clearable placeholder="全部状态" style="width: 150px;" @change="handleMyJiaoyouFilterChange">
                  <el-option label="待审核" value="待审核" />
                  <el-option label="是" value="是" />
                  <el-option label="否" value="否" />
                </el-select>
                <el-button type="primary" @click="goAddJiaoyou">发布交友信息</el-button>
              </div>
            </div>
            <el-table :data="myJiaoyouList" border empty-text="你还没有发布交友信息，快去发布一条吧">
              <el-table-column label="配图" width="92">
                <template #default="scope">
                  <el-avatar v-if="getImageUrl(scope?.row?.jiaoyoutupian)" shape="square" :size="52" :src="getImageUrl(scope?.row?.jiaoyoutupian)" />
                  <el-avatar v-else shape="square" :size="52">图</el-avatar>
                </template>
              </el-table-column>
              <el-table-column prop="jiaoyoumude" label="标题/目的" min-width="180" show-overflow-tooltip />
              <el-table-column label="描述" min-width="220" show-overflow-tooltip>
                <template #default="scope">
                  {{ getIntroSummary(scope?.row) }}
                </template>
              </el-table-column>
              <el-table-column label="审核状态" width="110">
                <template #default="scope">
                  <el-tag :type="getReviewStatusType(scope?.row)">
                    {{ getReviewStatus(scope?.row) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="发布时间" width="180">
                <template #default="scope">
                  {{ formatDate(scope?.row?.addtime) }}
                </template>
              </el-table-column>
              <el-table-column label="拒绝原因" min-width="180" show-overflow-tooltip>
                <template #default="scope">
                  {{ getRejectReason(scope?.row) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="320">
                <template #default="scope">
                  <el-button size="small" @click="openMyJiaoyouDetail(scope?.row)">详情</el-button>
                  <el-button size="small" type="primary" @click="editMyJiaoyou(scope?.row)">修改</el-button>
                  <el-button size="small" @click="openMyJiaoyouComments(scope?.row)">查看评论</el-button>
                  <el-button size="small" type="danger" @click="deleteMyJiaoyou(scope?.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pager">
              <el-pagination
                background
                layout="prev, pager, next"
                v-model:current-page="myJiaoyouPage"
                :page-size="limit"
                :total="myJiaoyouTotal"
                @current-change="loadMyJiaoyouList"
              />
            </div>
          </div>
          <siliao-dialog ref="siliaoDialog" />
        </el-tab-pane>

        <el-tab-pane label="私聊消息" name="chat">
          <siliao-panel ref="siliaoPanel" @refreshCount="refreshMessageBadge" />
        </el-tab-pane>
      </el-tabs>
    </section>
  </div>
</template>

<script>
import frontMixin from './front-mixin'
import SiliaoPanel from '@/components/common/SiliaoPanel'
import SiliaoDialog from '@/components/common/SiliaoDialog'

export default {
  name: 'FrontCenter',
  components: { SiliaoPanel, SiliaoDialog },
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
      friendTotal: 0,
      addedFriendList: [],
      addedFriendPage: 1,
      addedFriendTotal: 0,
      myJiaoyouList: [],
      myJiaoyouPage: 1,
      myJiaoyouTotal: 0,
      myJiaoyouStatus: ''
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
    getRowId(row) {
      return row && row.id ? row.id : ''
    },
    getStoreupRefId(item) {
      return item && item.refid ? item.refid : ''
    },
    getStoreupTableName(item) {
      return item && item.tablename ? item.tablename : ''
    },
    getStoreupName(item) {
      return item && item.name ? item.name : '未命名内容'
    },
    getFriendStatus(row) {
      return row && row.status ? row.status : ''
    },
    getAvatarText(row) {
      const text = (row && (row.xingming || row.zhanghao)) || '友'
      return String(text).slice(0, 1)
    },
    getOnlineStatus(row) {
      return row && row.onlineStatus ? row.onlineStatus : '离线'
    },
    getUnreadCount(row) {
      return Number((row && row.unreadCount) || 0)
    },
    getReviewStatus(row) {
      return row && row.sfsh ? row.sfsh : '待审核'
    },
    getReviewStatusType(row) {
      const status = this.getReviewStatus(row)
      if (status === '是') {
        return 'success'
      }
      if (status === '否') {
        return 'danger'
      }
      return 'warning'
    },
    getRejectReason(row) {
      if (!row) {
        return '暂无'
      }
      if (this.getReviewStatus(row) !== '否') {
        return row.shhf || '暂无'
      }
      return row.shhf || '未填写'
    },
    getIntroSummary(row) {
      const text = this.stripHtml(row && row.gerenjianjie ? row.gerenjianjie : '')
      return text ? text.slice(0, 50) : '暂无简介'
    },
    canAuditFriend(row) {
      return this.getFriendStatus(row) === '待处理'
    },
    openPost(row) {
      const id = this.getRowId(row)
      if (!id) {
        return
      }
      this.goFront(`/front/forum/${id}`)
    },
    editPost(row) {
      const id = this.getRowId(row)
      if (!id) {
        return
      }
      this.goFront(`/front/forum/add?id=${id}`)
    },
    refreshMessageBadge() {
      window.dispatchEvent(new Event('refresh-message-count'))
    },
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
      } else if (tab === 'jiaoyou') {
        this.loadAddedFriendList()
        this.loadMyJiaoyouList()
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
      const refId = this.getStoreupRefId(item)
      const tableName = this.getStoreupTableName(item)
      const pathMap = {
        xianxiahuodong: `/front/xianxiahuodong/${refId}`,
        jiaoyouxinxi: `/front/jiaoyouxinxi/${refId}`
      }
      if (refId && pathMap[tableName]) {
        this.goFront(pathMap[tableName])
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
    loadAddedFriendList() {
      this.$http({
        url: 'haoyoushenqing/friendList',
        method: 'get',
        params: {
          page: this.addedFriendPage,
          limit: this.limit
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.addedFriendList = ((data.data || {}).list) || []
          this.addedFriendTotal = Number((data.data || {}).total || 0)
        } else {
          this.addedFriendList = []
          this.addedFriendTotal = 0
        }
      }).catch(() => {
        this.addedFriendList = []
        this.addedFriendTotal = 0
      })
    },
    openFriendChat(row) {
      if (!row || !row.zhanghao) {
        this.$message.warning('未找到好友账号')
        return
      }
      this.$nextTick(() => {
        if (this.$refs.siliaoDialog) {
          this.$refs.siliaoDialog.openByZhanghao(row.zhanghao)
        }
      })
    },
    openFriendDetail(row) {
      const id = row && row.jiaoyouxinxiId
      if (!id) {
        this.$message.warning('该好友暂未发布交友信息')
        return
      }
      this.goFront(`/front/jiaoyouxinxi/${id}`)
    },
    deleteAddedFriend(row) {
      const friendUserId = row && row.friendUserId
      if (!friendUserId) {
        this.$message.warning('未找到好友信息')
        return
      }
      this.$confirm('确定删除该好友吗？删除后双方好友关系都会解除。', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'haoyoushenqing/friendDelete',
          method: 'post',
          data: { friendUserId }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.loadAddedFriendList()
          } else {
            this.$message.error((data && data.msg) || '删除失败')
          }
        })
      }).catch(() => {})
    },
    handleMyJiaoyouFilterChange() {
      this.myJiaoyouPage = 1
      this.loadMyJiaoyouList()
    },
    goAddJiaoyou() {
      this.goFront('/front/jiaoyouxinxi/add')
    },
    loadMyJiaoyouList() {
      const params = {
        page: this.myJiaoyouPage,
        limit: this.limit
      }
      if (this.myJiaoyouStatus) {
        params.sfsh = this.myJiaoyouStatus
      }
      this.$http({
        url: 'jiaoyouxinxi/myPage',
        method: 'get',
        params
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.myJiaoyouList = ((data.data || {}).list) || []
          this.myJiaoyouTotal = Number((data.data || {}).total || 0)
        } else {
          this.myJiaoyouList = []
          this.myJiaoyouTotal = 0
        }
      }).catch(() => {
        this.myJiaoyouList = []
        this.myJiaoyouTotal = 0
      })
    },
    openMyJiaoyouDetail(row) {
      const id = this.getRowId(row)
      if (!id) {
        return
      }
      this.goFront(`/front/jiaoyouxinxi/${id}`)
    },
    editMyJiaoyou(row) {
      const id = this.getRowId(row)
      if (!id) {
        return
      }
      this.goFront(`/front/jiaoyouxinxi/add?id=${id}`)
    },
    openMyJiaoyouComments(row) {
      const id = this.getRowId(row)
      if (!id) {
        return
      }
      this.goFront(`/front/jiaoyouxinxi/${id}?tab=comment`)
    },
    deleteMyJiaoyou(row) {
      const id = this.getRowId(row)
      if (!id) {
        this.$message.warning('未找到交友信息')
        return
      }
      this.$confirm('确定删除这条交友信息吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'jiaoyouxinxi/myDelete',
          method: 'post',
          data: [id]
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.loadMyJiaoyouList()
          } else {
            this.$message.error((data && data.msg) || '删除失败')
          }
        })
      }).catch(() => {})
    },
    handleAudit(row, status) {
      const id = this.getRowId(row)
      if (!id) {
        this.$message.warning('未找到申请记录')
        return
      }
      this.$prompt('请输入审核回复', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.$http({
          url: 'haoyoushenqing/audit',
          method: 'post',
          data: {
            id,
            status,
            reply: value
          }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('处理成功')
            this.loadFriendList()
            this.refreshMessageBadge()
          } else {
            this.$message.error((data && data.msg) || '处理失败')
          }
        })
      }).catch(() => {})
    },
    deleteFriendReq(row) {
      const id = this.getRowId(row)
      if (!id) {
        this.$message.warning('未找到申请记录')
        return
      }
      this.$confirm('确定删除该申请记录？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$http({
          url: 'haoyoushenqing/delete',
          method: 'post',
          data: [id]
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('删除成功')
            this.loadFriendList()
            this.refreshMessageBadge()
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

.section-card {
  margin-bottom: 22px;
  padding: 20px;
  border-radius: 18px;
  border: 1px solid #e6efff;
  background: #fbfdff;
}

.section-card:last-child {
  margin-bottom: 0;
}

.section-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 16px;
}

.section-head h3 {
  margin: 0 0 6px;
  font-size: 18px;
  color: #101828;
}

.section-head p {
  margin: 0;
  color: #667085;
  font-size: 13px;
}

.section-head-wrap {
  flex-wrap: wrap;
}

.section-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.minor-text {
  color: #667085;
  font-size: 13px;
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

  .section-head,
  .section-actions {
    align-items: flex-start;
  }
}
</style>

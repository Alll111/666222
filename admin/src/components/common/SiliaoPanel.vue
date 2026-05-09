<template>
  <div class="siliao-wrap">
    <div class="siliao-left">
      <div class="siliao-left-head">
        <el-input size="small" v-model="kw" placeholder="搜索"></el-input>
      </div>
      <div class="siliao-sessions">
        <div
          v-for="s in filteredSessions"
          :key="s.sessionId"
          class="siliao-session"
          :class="{active: activeSessionId===s.sessionId}"
          @click="selectSession(s)"
        >
          <el-badge :value="s.unreadCount" v-if="s.unreadCount>0" class="u-badge">
            <img class="avatar" :src="avatarUrl(s.peerTouxiang)" />
          </el-badge>
          <img v-else class="avatar" :src="avatarUrl(s.peerTouxiang)" />
          <div class="meta">
            <div class="name">{{s.peerXingming||s.peerZhanghao}}</div>
            <div class="last">{{s.lastMsg||''}}</div>
          </div>
          <div class="time">{{fmtTime(s.lastTime)}}</div>
        </div>
      </div>
    </div>
    <div class="siliao-right">
      <div class="siliao-right-head" v-if="activePeer">
        <img class="avatar" :src="avatarUrl(activePeer.peerTouxiang)" />
        <div class="name">{{activePeer.peerXingming||activePeer.peerZhanghao}}</div>
      </div>
      <div class="siliao-history" ref="history">
        <div v-for="m in messages" :key="m.id" class="msg-row" :class="{me: Number(m.fromUserid)===meId}">
          <img class="avatar" :src="avatarUrl(m.fromTouxiang)" />
          <div class="bubble">
            <div class="t">{{fmtTime(m.addtime)}}</div>
            <div class="c">{{m.content}}</div>
          </div>
        </div>
      </div>
      <div class="siliao-input" v-if="activePeer">
        <el-input type="textarea" :rows="3" v-model="content"></el-input>
        <el-button type="primary" @click="send">发送</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SiliaoPanel',
  data() {
    return {
      kw: '',
      sessions: [],
      activeSessionId: null,
      activePeer: null,
      messages: [],
      content: '',
      meId: null,
    }
  },
  computed: {
    filteredSessions() {
      if (!this.kw) return this.sessions
      const k = this.kw.trim()
      return this.sessions.filter(s => (s.peerXingming || '').includes(k) || (s.peerZhanghao || '').includes(k))
    }
  },
  methods: {
    open() {
      this.meId = Number(this.$storage.get('userid') || 0)
      this.refreshSessions()
    },
    refreshSessions() {
      return this.$http({ url: '/siliao/sessions', method: 'get', params: { page: 1, limit: 50 } })
        .then(({ data }) => {
          if (data && data.code === 0) this.sessions = data.data.list || []
          else this.sessions = []
        }).catch(() => {
          this.sessions = []
        })
    },
    selectSession(s) {
      this.activeSessionId = s.sessionId
      this.activePeer = s
      this.loadHistory()
      this.$http({ url: '/siliao/markRead', method: 'post', data: { sessionId: s.sessionId } }).then(() => {
        this.refreshSessions()
        this.$emit('refreshCount')
      }).catch(() => {})
    },
    loadHistory() {
      if (!this.activeSessionId) return
      this.$http({ url: '/siliao/history', method: 'get', params: { sessionId: this.activeSessionId, page: 1, limit: 200 } })
        .then(({ data }) => {
          if (data && data.code === 0) {
            this.messages = data.data.list || []
            this.$nextTick(() => this.scrollBottom())
          } else this.messages = []
        }).catch(() => {
          this.messages = []
        })
    },
    send() {
      const c = (this.content || '').trim()
      if (!c || !this.activePeer) return
      this.$http({
        url: '/siliao/send',
        method: 'post',
        data: { sessionId: this.activeSessionId, peerUserId: this.activePeer.peerUserId, content: c }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.content = ''
          this.loadHistory()
          this.refreshSessions()
          this.$emit('refreshCount')
        } else {
          this.$message.error((data && data.msg) || '发送失败')
        }
      }).catch((error) => {
        this.$message.error((error && error.message) || '发送失败')
      })
    },
    scrollBottom() {
      const el = this.$refs.history
      if (!el) return
      el.scrollTop = el.scrollHeight
    },
    avatarUrl(v) {
      if (!v) return ''
      if (v.startsWith('http')) return v
      return this.$base.url + v
    },
    fmtTime(v) {
      if (!v) return ''
      const d = new Date(v)
      if (isNaN(d.getTime())) return v
      const mm = (d.getMonth() + 1).toString().padStart(2, '0')
      const dd = d.getDate().toString().padStart(2, '0')
      const hh = d.getHours().toString().padStart(2, '0')
      const mi = d.getMinutes().toString().padStart(2, '0')
      return `${mm}-${dd} ${hh}:${mi}`
    }
  }
}
</script>

<style scoped>
.siliao-wrap{display:flex;height:520px}
.siliao-left{width:300px;border-right:1px solid #eee;display:flex;flex-direction:column}
.siliao-left-head{padding:8px}
.siliao-sessions{flex:1;overflow:auto}
.siliao-session{display:flex;align-items:center;padding:10px;cursor:pointer;border-bottom:1px solid #f3f3f3}
.siliao-session.active{background:#f5f7fa}
.siliao-session .avatar{width:36px;height:36px;border-radius:6px;object-fit:cover;background:#f0f0f0}
.siliao-session .meta{flex:1;margin-left:10px;overflow:hidden}
.siliao-session .name{font-size:14px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}
.siliao-session .last{font-size:12px;color:#999;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;margin-top:2px}
.siliao-session .time{font-size:12px;color:#999;margin-left:6px;white-space:nowrap}
.siliao-right{flex:1;display:flex;flex-direction:column}
.siliao-right-head{height:52px;display:flex;align-items:center;border-bottom:1px solid #eee;padding:0 12px;gap:10px}
.siliao-right-head .avatar{width:34px;height:34px;border-radius:6px;object-fit:cover;background:#f0f0f0}
.siliao-history{flex:1;overflow:auto;padding:12px;background:#fafafa}
.msg-row{display:flex;gap:10px;margin-bottom:12px;align-items:flex-start}
.msg-row.me{flex-direction:row-reverse}
.msg-row .avatar{width:32px;height:32px;border-radius:6px;object-fit:cover;background:#f0f0f0}
.bubble{max-width:65%}
.bubble .t{font-size:12px;color:#999;margin-bottom:4px;text-align:center}
.bubble .c{background:#fff;border-radius:8px;padding:8px 10px;white-space:pre-wrap;word-break:break-word}
.msg-row.me .bubble .c{background:#cfe9ff}
.siliao-input{border-top:1px solid #eee;padding:10px;display:flex;gap:10px;align-items:flex-end}
.siliao-input .el-textarea{flex:1}
</style>

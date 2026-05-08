<template>
	<div class="navbar" :style="{background:heads.headBgColor,height:heads.headHeight,boxShadow:heads.headBoxShadow,lineHeight:heads.headHeight}">
		<div class="title-menu" :style="{justifyContent:heads.headTitleStyle=='1'?'flex-start':'center'}">
			<el-image v-if="heads.headTitleImg" class="title-img" :style="{width:heads.headTitleImgWidth,height:heads.headTitleImgHeight,boxShadow:heads.headTitleImgBoxShadow,borderRadius:heads.headTitleImgBorderRadius}" :src="heads.headTitleImgUrl" fit="cover"></el-image>
			<div class="title-name" :style="{color:heads.headFontColor,fontSize:heads.headFontSize}">{{this.$project.projectName}}</div>
		</div>
		<div class="right-menu">
			<el-badge v-if="this.$storage.get('adminName')!='admin' && messageCount>0" :value="messageCount" class="message-badge">
				<i class="el-icon-bell message-icon" @click="openMessageDialog"></i>
			</el-badge>
			<i v-else-if="this.$storage.get('adminName')!='admin'" class="el-icon-bell message-icon" @click="openMessageDialog"></i>
			<div class="user-info" :style="{color:heads.headUserInfoFontColor,fontSize:heads.headUserInfoFontSize}">{{this.$storage.get('role')}} {{this.$storage.get('adminName')}}</div>
			<div v-if="this.$storage.get('role')!='管理员'" class="logout" :style="{color:heads.headLogoutFontColor,fontSize:heads.headLogoutFontSize}" @click="goToFront">退出到前台</div>
			<div class="logout" :style="{color:heads.headLogoutFontColor,fontSize:heads.headLogoutFontSize}" @click="onLogout">退出登录</div>
		</div>
		<el-dialog title="消息" :visible.sync="messageDialogVisible" width="900px" :modal="false">
			<el-tabs v-model="activeMsgTab">
				<el-tab-pane label="好友申请" name="friend">
					<el-table :data="messageList" border style="width: 100%">
						<el-table-column prop="fromZhanghao" label="账号" width="120"></el-table-column>
						<el-table-column prop="fromXingming" label="姓名" width="120"></el-table-column>
						<el-table-column prop="addtime" label="时间"></el-table-column>
						<el-table-column prop="status" label="状态" width="90"></el-table-column>
						<el-table-column prop="reply" label="回复"></el-table-column>
						<el-table-column label="操作" width="220">
							<template slot-scope="scope">
								<el-button v-if="scope.row.status==='待处理'" size="mini" type="primary" @click="handleAudit(scope.row,'同意')">同意</el-button>
								<el-button v-if="scope.row.status==='待处理'" size="mini" type="danger" @click="handleAudit(scope.row,'拒绝')">拒绝</el-button>
								<el-button size="mini" @click="deleteFriendReq(scope.row)">删除申请记录</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div style="display:flex;justify-content:flex-end;margin-top:10px;">
						<el-pagination
							@current-change="handlePageChange"
							:current-page="page"
							:page-size="limit"
							layout="prev, pager, next"
							:total="total">
						</el-pagination>
					</div>
				</el-tab-pane>
				<el-tab-pane label="私聊" name="chat">
					<siliao-panel ref="siliaoPanel" @refreshCount="loadMessageCount"></siliao-panel>
				</el-tab-pane>
			</el-tabs>
		</el-dialog>
	</div>
</template>

<script>
	import SiliaoPanel from "@/components/common/SiliaoPanel";
	export default {
		components: { SiliaoPanel },
		data() {
			return {
				dialogVisible: false,
				ruleForm: {},
				user: {},
				messageCount: 0,
				friendCount: 0,
				chatUnreadCount: 0,
				messageDialogVisible: false,
				activeMsgTab: 'friend',
				messageList: [],
				page: 1,
				limit: 10,
				total: 0,
				heads: {"headLogoutFontHoverColor":"rgba(255, 255, 255, 1)","headFontSize":"20px","headUserInfoFontColor":"#333","headBoxShadow":"0 1px 0px rgba(255,255,255, 1)","headTitleImgHeight":"44px","headLogoutFontHoverBgColor":"rgba(214, 238, 247, 1)","headFontColor":"#000","headTitleImg":false,"headHeight":"62px","headTitleImgBorderRadius":"22px","headTitleImgUrl":"http://codegen.caihongy.cn/20201021/cc7d45d9c8164b58b18351764eba9be1.jpg","headBgColor":"linear-gradient(rgba(255,255,255, 1), rgba(214,238,247, 1),rgba(56,182,230, 1))","headTitleImgBoxShadow":"0 1px 6px #444","headLogoutFontColor":"#333","headUserInfoFontSize":"18px","headTitleImgWidth":"44px","headTitleStyle":"1","headLogoutFontSize":"18px"},
			};
		},
		created() {
			this.setHeaderStyle()
			this.loadMessageCount()
			window.addEventListener('refresh-message-count', this.loadMessageCount)
		},
		mounted() {
			let sessionTable = this.$storage.get("sessionTable") || 'users'
			this.$http({
				url: sessionTable + '/session',
				method: "get"
			}).then(({ data }) => {
				if (data && data.code === 0) {
					this.user = data.data;
					this.$storage.set('userid',data.data.id);
				} else {
					this.$message.error((data && data.msg) || '加载用户信息失败');
				}
			}).catch((error) => {
				this.$message.error((error && error.message) || '加载用户信息失败');
			});
		},
		methods: {
			onLogout() {
				let storage = this.$storage
				let router = this.$router
				storage.clear()
				router.replace({ name: "login" });
			},
			goToFront() {
				this.$storage.remove('Token')
				localStorage.removeItem('token')
				localStorage.removeItem('Token')
				this.$router.push('/')
			},
			loadMessageCount() {
				if (this.$storage.get('adminName')=='admin') {
					this.messageCount = 0
					this.friendCount = 0
					this.chatUnreadCount = 0
					return
				}
				Promise.all([
					this.$http({ url: '/haoyoushenqing/inboxCount', method: 'get' }),
					this.$http({ url: '/siliao/unreadCount', method: 'get' })
				]).then(([a, b]) => {
					const da = a && a.data
					const db = b && b.data
					this.friendCount = da && da.code === 0 ? (da.count || 0) : 0
					this.chatUnreadCount = db && db.code === 0 ? (db.count || 0) : 0
					this.messageCount = this.friendCount + this.chatUnreadCount
				}).catch(() => {
					this.friendCount = 0
					this.chatUnreadCount = 0
					this.messageCount = 0
				})
			},
			loadMessageList() {
				this.$http({
					url: '/haoyoushenqing/inboxList',
					method: 'get',
					params: { page: this.page, limit: this.limit }
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.messageList = data.data.list || []
						this.total = data.data.total || 0
					}
				}).catch(() => {
					this.messageList = []
					this.total = 0
				})
			},
			openMessageDialog() {
				this.messageDialogVisible = true
				this.page = 1
				this.activeMsgTab = this.friendCount > 0 ? 'friend' : 'chat'
				this.loadMessageList()
				this.$nextTick(() => {
					if (this.activeMsgTab === 'chat' && this.$refs.siliaoPanel) {
						this.$refs.siliaoPanel.open()
					}
				})
			},
			handlePageChange(p) {
				this.page = p
				this.loadMessageList()
			},
			handleAudit(row, status) {
				this.$prompt('请输入审核回复', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消'
				}).then(({ value }) => {
					this.$http({
						url: '/haoyoushenqing/audit',
						method: 'post',
						data: { id: row.id, status: status, reply: value }
					}).then(({ data }) => {
						if (data && data.code === 0) {
							this.$message.success('处理成功')
							this.loadMessageCount()
							this.loadMessageList()
						} else {
							this.$message.error(data.msg || '处理失败')
						}
					}).catch((error) => {
						this.$message.error((error && error.message) || '处理失败')
					})
				}).catch(() => {});
			},
			deleteFriendReq(row) {
				this.$confirm('确定删除该申请记录？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: '/haoyoushenqing/delete',
						method: 'post',
						data: [row.id]
					}).then(({ data }) => {
						if (data && data.code === 0) {
							this.$message.success('删除成功')
							this.loadMessageCount()
							this.loadMessageList()
						} else {
							this.$message.error(data.msg || '删除失败')
						}
					}).catch((error) => {
						this.$message.error((error && error.message) || '删除失败')
					})
				}).catch(() => {});
			},
			setHeaderStyle() {
				this.$nextTick(()=>{
					document.querySelectorAll('.navbar .right-menu .logout').forEach(el=>{
						el.addEventListener("mouseenter", e => {
							e.stopPropagation()
							el.style.backgroundColor = this.heads.headLogoutFontHoverBgColor
							el.style.color = this.heads.headLogoutFontHoverColor
						})
						el.addEventListener("mouseleave", e => {
							e.stopPropagation()
							el.style.backgroundColor = "transparent"
							el.style.color = this.heads.headLogoutFontColor
						})
					})
				})
			},
		}
	};
</script>

<style lang="scss" scoped>
	.navbar {
		height: 60px;
		line-height: 60px;
		width: 100%;
		padding: 0 34px;
		box-sizing: border-box;
		background-color: #ff00ff;
		position: relative;
		z-index: 111;

		.right-menu {
			position: absolute;
			right: 34px;
			top: 0;
			height: 100%;
			display: flex;
			justify-content: flex-end;
			align-items: center;
			z-index: 111;

			.user-info {
				font-size: 16px;
				color: red;
				padding: 0 12px;
			}

			.logout {
				font-size: 16px;
				color: red;
				padding: 0 12px;
				cursor: pointer;
			}

			.message-icon {
				cursor: pointer;
				padding: 0 12px;
			}

			.message-badge {
				margin-right: -6px;
			}

			::v-deep .message-badge .el-badge__content {
				top: 12px;
				right: 14px;
			}
		}

		.title-menu {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			width: 100%;
			height: 100%;

			.title-img {
				width: 44px;
				height: 44px;
				border-radius: 22px;
				box-shadow: 0 1px 6px #444;
				margin-right: 16px;
			}

			.title-name {
				font-size: 24px;
				color: #fff;
				font-weight: 700;
			}
		}
	}
</style>

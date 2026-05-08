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
			<div class="user-info" :style="{color:heads.headUserInfoFontColor,fontSize:heads.headUserInfoFontSize}">
				<i class="el-icon-user-solid action-icon"></i>
				<span>{{this.$storage.get('role')}} {{this.$storage.get('adminName')}}</span>
			</div>
			<div v-if="this.$storage.get('role')!='管理员'" class="logout" :style="{color:heads.headLogoutFontColor,fontSize:heads.headLogoutFontSize}" @click="goToFront">
				<i class="el-icon-monitor action-icon"></i>
				<span>退出到前台</span>
			</div>
			<div class="logout" :style="{color:heads.headLogoutFontColor,fontSize:heads.headLogoutFontSize}" @click="onLogout">
				<i class="el-icon-switch-button action-icon"></i>
				<span>退出登录</span>
			</div>
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
				heads: {"headLogoutFontHoverColor":"#1f6fff","headFontSize":"20px","headUserInfoFontColor":"#35507a","headBoxShadow":"0 10px 28px rgba(15, 23, 42, 0.08)","headTitleImgHeight":"40px","headLogoutFontHoverBgColor":"rgba(31, 111, 255, 0.08)","headFontColor":"#1f2d3d","headTitleImg":false,"headHeight":"64px","headTitleImgBorderRadius":"20px","headTitleImgUrl":"http://codegen.caihongy.cn/20201021/cc7d45d9c8164b58b18351764eba9be1.jpg","headBgColor":"rgba(255,255,255,0.96)","headTitleImgBoxShadow":"0 8px 20px rgba(15, 23, 42, 0.10)","headLogoutFontColor":"#35507a","headUserInfoFontSize":"14px","headTitleImgWidth":"40px","headTitleStyle":"1","headLogoutFontSize":"14px"},
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
				this.$router.push('/front/home')
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
		height: 64px;
		line-height: 64px;
		width: 100%;
		padding: 0 24px 0 28px;
		box-sizing: border-box;
		background: #ffffff !important;
		position: relative;
		z-index: 111;
		border-bottom: 1px solid rgba(223, 232, 247, 0.9);
		box-shadow: 0 10px 28px rgba(15, 23, 42, 0.08) !important;
		backdrop-filter: blur(12px);

		.right-menu {
			position: absolute;
			right: 28px;
			top: 0;
			height: 100%;
			display: flex;
			justify-content: flex-end;
			align-items: center;
			gap: 10px;
			z-index: 111;

			.action-icon {
				font-size: 15px;
				line-height: 1;
			}

			.user-info {
				font-size: 14px;
				color: #35507a;
				padding: 0 14px;
				height: 38px;
				line-height: 38px;
				border-radius: 999px;
				background: #f6f9ff;
				border: 1px solid #dbe7ff;
				box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.78);
				display: inline-flex;
				align-items: center;
				gap: 8px;
				font-weight: 600;
			}

			.logout {
				font-size: 14px;
				color: #35507a;
				padding: 0 14px;
				height: 38px;
				line-height: 38px;
				border-radius: 999px;
				background: #ffffff;
				border: 1px solid #dbe7ff;
				cursor: pointer;
				display: inline-flex;
				align-items: center;
				gap: 8px;
				font-weight: 600;
				transition: background-color 0.2s ease, transform 0.2s ease, box-shadow 0.2s ease, color 0.2s ease, border-color 0.2s ease;

				&:hover {
					transform: translateY(-1px);
					color: #1f6fff;
					border-color: #c5d8ff;
					background: #f5f9ff;
					box-shadow: 0 10px 18px rgba(31, 111, 255, 0.12);
				}
			}

			.message-icon {
				cursor: pointer;
				width: 38px;
				height: 38px;
				line-height: 38px;
				text-align: center;
				padding: 0;
				font-size: 18px;
				color: #35507a;
				border-radius: 50%;
				background: #f6f9ff;
				border: 1px solid #dbe7ff;
				transition: transform 0.2s ease, background-color 0.2s ease, color 0.2s ease, box-shadow 0.2s ease;

				&:hover {
					transform: translateY(-1px);
					color: #1f6fff;
					background: #eef5ff;
					box-shadow: 0 10px 18px rgba(31, 111, 255, 0.10);
				}
			}

			.message-badge {
				margin-right: -2px;
			}

			::v-deep .message-badge .el-badge__content {
				top: 6px;
				right: 4px;
				border: 0;
				background: linear-gradient(135deg, #ff7a6c, #ff4d4f);
				box-shadow: 0 6px 14px rgba(255, 77, 79, 0.32);
			}
		}

		.title-menu {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			width: 100%;
			height: 100%;
			padding-right: 320px;

			.title-img {
				width: 44px;
				height: 44px;
				border-radius: 50%;
				box-shadow: 0 10px 22px rgba(9, 24, 56, 0.20);
				margin-right: 14px;
			}

			.title-name {
				font-size: 24px;
				color: #1f2d3d;
				font-weight: 700;
				letter-spacing: 1px;
				text-shadow: none;
			}
		}
	}
</style>

<template>
			<el-main class="index-main">
    		<bread-crumbs :title="title" class="bread-crumbs"></bread-crumbs>
		<router-view class="router-view"></router-view>
	</el-main>
</template>

<script>
	import menu from "@/utils/menu";
	export default {
		data() {
			return {
				menuList: [],
				role: "",
				currentIndex: -2,
				itemMenu: [],
				title: '',
			};
		},
		mounted() {
			let menus = menu.list();
			this.menuList = menus;
			this.role = this.$storage.get("role");
		},
		created() {
			this.init();
		},
		methods: {
			init(){
				this.$nextTick(()=>{
					// let h = document.getElementsByClassName('el-aside')[0].clientHeight
					// document.getElementsByClassName('el-main')[0].style.minHeight = "calc(100vh - 62px - " + h+'px)'
				})
			},
			menuHandler(menu) {
				if (this.$router.currentRoute.value.name !== menu.tableName) {
					this.$router.push({
						name: menu.tableName
					});
					this.title = menu.menu;
				}
			},
			titleChange(index, menus) {
				this.currentIndex = index
				this.itemMenu = menus;
				console.log(menus);
			},
			homeChange(index) {
				this.itemMenu = [];
				this.title = ""
				this.currentIndex = index
				if (this.$router.currentRoute.value.name !== 'index') {
					this.$router.push({
						name: 'index'
					});
				}
			},
			centerChange(index) {
				this.itemMenu = [{
					"buttons": ["新增", "查看", "修改", "删除"],
					"menu": "修改密码",
					"tableName": "updatePassword"
				}, {
					"buttons": ["新增", "查看", "修改", "删除"],
					"menu": "个人信息",
					"tableName": "center"
				}];
				this.title = ""
				this.currentIndex = index
				if (this.$router.currentRoute.value.name !== 'index') {
					this.$router.push({
						name: 'index'
					});
				}
				
			}
		}
	};
</script>
<style lang="scss" scoped>
	a {
		text-decoration: none;
		color: #555;
	}

	a:hover {
		color: #1f6fff;
	}

	.nav-list {
		width: 100%;
		margin: 0 auto;
		text-align: left;
		margin-top: 20px;

		.nav-title {
			display: inline-block;
			font-size: 15px;
			color: #333;
			padding: 15px 25px;
			border: none;
		}

		.nav-title.active {
			color: #555;
			cursor: default;
			background-color: #fff;
		}
	}

	.nav-item {
		margin-top: 20px;
		background: #FFFFFF;
		padding: 15px 0;

		.menu {
			padding: 15px 25px;
		}
	}

	.index-main,
	.el-main {
		padding: 14px 28px 28px;
		min-height: 0;
		height: 100%;
		background: #f5f7fb;
		display: flex;
		flex: 1;
		flex-direction: column;
		box-sizing: border-box;
	}

	.router-view {
		padding: 0;
		margin-top: 0;
		background: transparent;
		box-sizing: border-box;
		flex: 1;
		min-height: 0;
	}

	.bread-crumbs {
		width: 100%;
		margin-top: 0;
		margin-bottom: 14px;
		box-sizing: border-box;
		flex-shrink: 0;
	}
	
	.detail-form-content {
	    background: transparent;
	}
</style>

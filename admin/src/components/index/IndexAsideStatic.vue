<template>
  <el-aside class="index-aside" width="220px">
    <div class="index-aside-inner menulist">
      <div v-for="item in filteredMenuList" :key="item.roleName" class="menulist-item">
        <div class="menulistImg" v-if="false && 2 == 2">
          <el-image :style='{"padding":"0","boxShadow":"0 0 6px rgba(0,0,0,0)","margin":"0","borderColor":"rgba(0,0,0,0)","borderRadius":"0","borderWidth":"0","width":"100%","borderStyle":"solid","height":"auto"}' v-if="'http://codegen.caihongy.cn/20201021/cc7d45d9c8164b58b18351764eba9be1.jpg'" src="http://codegen.caihongy.cn/20201021/cc7d45d9c8164b58b18351764eba9be1.jpg" fit="cover" />
        </div>
		<el-menu :mode="2 == 1? 'horizontal':'vertical'" :unique-opened="true" class="el-menu-demo" default-active="0">
          <el-menu-item index="0" @click="menuHandler('')"><i v-if="true" class="el-icon-menu el-icon-s-home" />首页</el-menu-item>
          <el-submenu :index="1+''">
            <template #title>
				<i v-if="true" class="el-icon-menu el-icon-user-solid" />
				<span>个人中心</span>
            </template>
            <el-menu-item index="1-1" @click="menuHandler('updatePassword')">修改密码</el-menu-item>
            <el-menu-item index="1-2" @click="menuHandler('center')">个人信息</el-menu-item>
          </el-submenu>
          <el-submenu v-for=" (menu,index) in item.backMenu" :key="menu.menu" :index="index+2+''">
            <template #title>
				<i v-if="true" class="el-icon-menu" :class="icons[index]" />
				<span>{{ menu.menu }}</span>
            </template>
            <el-menu-item v-for=" (child,sort) in menu.child" :key="sort" :index="(index+2)+'-'+sort" @click="menuHandler(child.tableName)">{{ child.menu }}</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>
    </div>
  </el-aside>
</template>

<script>
import menu from '@/utils/menu'
export default {
  computed: {
    filteredMenuList() {
      return (this.menuList || []).filter(item => this.role === item.roleName)
    }
  },
  data() {
    return {
      menuList: [],
      dynamicMenuRoutes: [],
      role: '',
      icons: [
        'el-icon-s-cooperation',
        'el-icon-s-order',
        'el-icon-s-platform',
        'el-icon-s-fold',
        'el-icon-s-unfold',
        'el-icon-s-operation',
        'el-icon-s-promotion',
        'el-icon-s-release',
        'el-icon-s-ticket',
        'el-icon-s-management',
        'el-icon-s-open',
        'el-icon-s-shop',
        'el-icon-s-marketing',
        'el-icon-s-flag',
        'el-icon-s-comment',
        'el-icon-s-finance',
        'el-icon-s-claim',
        'el-icon-s-custom',
        'el-icon-s-opportunity',
        'el-icon-s-data',
        'el-icon-s-check',
        'el-icon-s-grid',
        'el-icon-menu',
        'el-icon-chat-dot-square',
        'el-icon-message',
        'el-icon-postcard',
        'el-icon-position',
        'el-icon-microphone',
        'el-icon-close-notification',
        'el-icon-bangzhu',
        'el-icon-time',
        'el-icon-odometer',
        'el-icon-crop',
        'el-icon-aim',
        'el-icon-switch-button',
        'el-icon-full-screen',
        'el-icon-copy-document',
        'el-icon-mic',
        'el-icon-stopwatch',
      ],
      menulistStyle: '${template.back.menulist.menulistStyle}',
	  menulistBorderBottom: {},
    }
  },
  mounted() {
    this.role = this.$storage.get('role')
    this.loadMenu()
  },
  created(){
    setTimeout(()=>{
      this.menulistStyleChange()
    },10)
    this.icons.sort(()=>{
      return (0.5-Math.random())
    })
	this.lineBorder()
  },
  methods: {
	loadMenu() {
		const localMenus = menu.list() || []
		const localRoleMenu = localMenus.find(item => item.roleName === (this.role || '管理员'))
		if (localRoleMenu && Array.isArray(localRoleMenu.backMenu) && localRoleMenu.backMenu.length) {
			this.menuList = localMenus
			this.$storage.set('menus', localMenus)
			return
		}
		this.$http({
			url: '/menu/list',
			method: 'get'
		}).then(({ data }) => {
			const parsedMenu = this.parseBackendMenu(data)
			if (parsedMenu.length > 0) {
				this.menuList = [{
					roleName: this.role || '管理员',
					backMenu: parsedMenu
				}]
				this.$storage.set('menus', this.menuList)
				return
			}
			this.useLocalMenu()
		}).catch(() => {
			this.useLocalMenu()
		})
	},
	parseBackendMenu(data) {
		const menuArray = Array.isArray(data) ? data : (data && Array.isArray(data.data) ? data.data : [])
		return menuArray.map(item => {
			if (item && Array.isArray(item.child)) {
				return item
			}
			const name = item && (item.name || item.menu) ? (item.name || item.menu) : ''
			const rawPath = item && (item.path || item.tableName) ? (item.path || item.tableName) : ''
			return {
				menu: name,
				child: [{
					menu: name,
					tableName: this.getTableName(rawPath)
				}]
			}
		}).filter(item => item.menu && Array.isArray(item.child) && item.child.length > 0 && item.child[0].tableName)
	},
	useLocalMenu() {
		const menus = menu.list()
		this.menuList = menus || []
	},
	getTableName(path) {
		if (!path) {
			return ''
		}
		const parts = path.split('/').filter(Boolean)
		return parts.length ? parts[parts.length - 1] : ''
	},
	lineBorder() {
		let style = '${template.back.menulist.menulistStyle}'
		let w = '${template.back.menulist.menulistLineWidth}'
		let s = '${template.back.menulist.menulistLineStyle}'
		let c = '${template.back.menulist.menulistLineColor}'
		if(style == 'vertical') {
			this.menulistBorderBottom = {
				borderBottomWidth: w,
				borderBottomStyle: s,
				borderBottomColor: c
			}
		} else {
			this.menulistBorderBottom = {
				borderRightWidth: w,
				borderRightStyle: s,
				borderRightColor: c
			}
		}
	},
    menuHandler(name) {
      let router = this.$router
      let targetPath = '/index'
      if (name) {
        targetPath = name.startsWith('/') ? name : `/index/${name}`
      }
      if (router.currentRoute.value.path !== targetPath) {
        router.push(targetPath).catch(() => {})
      }
    },
    // 菜单
    setMenulistHoverColor(){
      let that = this
	  return;
      this.$nextTick(()=>{
        document.querySelectorAll('.menulist .el-menu-item').forEach(el=>{
          el.addEventListener("mouseenter", e => {
            e.stopPropagation()
            el.style.backgroundColor = "${template.back.menulist.menulistHoverColor}"
          })
          el.addEventListener("mouseleave", e => {
            e.stopPropagation()
            // el.style.backgroundColor = "${template.back.menulist.menulistBgColor}"
			el.style.background = "none"
          })
          el.addEventListener("focus", e => {
            e.stopPropagation()
            el.style.backgroundColor = "${template.back.menulist.menulistHoverColor}"
          })
        })
        document.querySelectorAll('.menulist .el-submenu__title').forEach(el=>{
          el.addEventListener("mouseenter", e => {
            e.stopPropagation()
            el.style.backgroundColor = "${template.back.menulist.menulistHoverColor}"
          })
          el.addEventListener("mouseleave", e => {
            e.stopPropagation()
            // el.style.backgroundColor = "${template.back.menulist.menulistBgColor}"
			el.style.background = "none"
          })
        })
      })
    },
    setMenulistIconColor() {
      this.$nextTick(()=>{
        document.querySelectorAll('.menulist .el-submenu__title .el-submenu__icon-arrow').forEach(el=>{
          el.style.color = "${template.back.menulist.menulistIconColor}"
        })
      })
    },
    menulistStyleChange() {
      this.setMenulistIconColor()
      this.setMenulistHoverColor()
      this.setMenulistStyleHeightChange()
      let str = "2"
      if(1 == str) {
        this.$nextTick(()=>{
          document.querySelectorAll('.el-container .el-container').forEach(el=>{
            el.style.display = "block"
            el.style.paddingTop = "62px" // header 高度
          })
          document.querySelectorAll('.el-aside').forEach(el=>{
            el.style.width = "100%"
            el.style.height = "100%"
            el.style.paddingTop = '0'
          })
          document.querySelectorAll('.index-aside .index-aside-inner').forEach(el=>{
            el.style.paddingTop = '0'
			el.style.width = "100%"
          })
        })
      }
      if(2 === str) {
        this.$nextTick(()=>{
          document.querySelectorAll('.index-aside .index-aside-inner').forEach(el=>{
            el.style.paddingTop = "62px"
          })
        })
      }
    },
    setMenulistStyleHeightChange() {
		return;
      this.$nextTick(()=>{
        document.querySelectorAll('.menulist-item>.el-menu--horizontal>.el-menu-item').forEach(el=>{
          el.style.height = "${template.back.menulist.menulistHeight}"
          el.style.lineHeight = "${template.back.menulist.menulistHeight}"
        })
        document.querySelectorAll('.menulist-item>.el-menu--horizontal>.el-submenu>.el-submenu__title').forEach(el=>{
          el.style.height = "${template.back.menulist.menulistHeight}"
          el.style.lineHeight = "${template.back.menulist.menulistHeight}"
        })
      })
    },
  }
}
</script>
<style lang="scss" scoped>
  .index-aside {
    position: relative;
    overflow: hidden;
	display: flex;
	flex-wrap: wrap;
    background: #ffffff;
    border-right: 1px solid rgba(223, 232, 247, 0.95);
    box-shadow: 8px 0 22px rgba(15, 23, 42, 0.04);

    .menulistImg {
		font-size: 0;
		box-sizing: border-box;

      .el-image {
        margin: 0 auto;
        width: 100px;
        height: 100px;
        border-radius: 100%;
        display: block;
      }
    }
	
	    .index-aside-inner {
      height: 100%;
      margin-right: -17px;
      margin-bottom: -17px;
      overflow: scroll;
      overflow-x: hidden !important;
      padding: 80px 0 18px;
      box-sizing: border-box;

      &:focus {
        outline: none;
      }

      & :deep(.el-menu){
        border: 0;
		background-color: transparent;
      }
    }
		
	.index-aside .index-aside-inner.menulist {
		height: 100% !important;
	}
	.menulist-item {
		width: 220px;
		padding: 0;
		margin: 0;
		border-radius: 0;
		border-width: 0 !important;
		border-style: solid !important;
		border-color: rgba(0,0,0,.3) !important;
		background: transparent !important;
		box-shadow: none;
		box-sizing: border-box;
	}
	.el-menu-demo {
		box-sizing: border-box;
		min-height: calc(100vh - 62px);
		padding: 0 14px 12px;
			
		&>.el-menu-item {
			width: 100%;
			height: 44px !important;
			line-height: 44px !important;
			padding: 0 16px !important;
			margin: 0 0 8px;
			color: #284369 !important;
			font-size: 14px;
			font-weight: 600;
			border-radius: 12px;
			border: 1px solid transparent !important;
			background-color: transparent !important;
			box-shadow: none;
			display: flex;
			align-items: center;
			justify-content: flex-start;
			box-sizing: border-box;
			transition: background-color 0.2s ease, color 0.2s ease, box-shadow 0.2s ease, transform 0.2s ease;
            
            .el-icon-menu {
            	margin: 0 12px 0 0;
            	padding: 0;
            	width: 18px;
            	line-height: 18px;
            	color: #7b93ba;
            	font-size: 16px;
            	border-radius: 0;
            	border-width: 0;
            	border-style: solid;
            	border-color: #fff;
            	background-color: rgba(255,255,255,0);
            	box-shadow: 0 0 0px rgba(255,255,255,0);
            }

            &:hover,
            &.is-active {
              background: #eef5ff !important;
              color: #1f6fff !important;
              border-color: #dce8ff !important;
              box-shadow: 0 10px 20px rgba(31, 111, 255, 0.10);
              transform: translateX(2px);

              .el-icon-menu {
                color: #1f6fff;
              }
            }
		}
		
		.el-submenu {
			margin: 0 0 10px;
		}
		
		& :deep(.el-submenu__title){
			width: 100%;
			height: 44px !important;
			line-height: 44px !important;
			padding: 0 16px !important;
			color: #284369 !important;
			font-size: 14px;
			font-weight: 600;
			border-radius: 12px;
			border: 1px solid transparent !important;
			background-color: transparent !important;
			box-shadow: none;
			display: flex;
			align-items: center;
			justify-content: flex-start;
			box-sizing: border-box;
			transition: background-color 0.2s ease, color 0.2s ease, box-shadow 0.2s ease, transform 0.2s ease;
			
			.el-icon-menu {
				margin: 0 12px 0 0;
				padding: 0;
				width: 18px;
				line-height: 18px;
				color: #7b93ba;
				font-size: 16px;
				border-radius: 0;
				border-width: 0;
				border-style: solid;
				border-color: #fff;
				background-color: rgba(255,255,255,0);
				box-shadow: 0 0 0px rgba(255,255,255,0);
			}
			
			.el-submenu__icon-arrow {
				margin: 0 4px 0 0;
				padding: 0;
				width: 12px;
				line-height: 12px;
				color: #6b87b4 !important;
				font-size: 12px;
				position: absolute;
				top: 50%;
				right: 0;
				transform: translateY(-50%);
				text-align: center;
				display: block;
			}

      &:hover {
        background: rgba(31, 111, 255, 0.06) !important;
        color: #1f6fff !important;
        border-color: #dce8ff !important;
        transform: translateX(2px);

        .el-icon-menu,
        .el-submenu__icon-arrow {
          color: #1f6fff !important;
        }
      }
		}
		
		& :deep(.el-menu.el-menu--inline){
			width: 100%;
			height: auto;
			padding: 8px 0 0;
			margin: 0;
			border-radius: 0;
			border-width: 0;
			border-style: solid;
			border-color: rgba(0,0,0,.3);
			background-color: rgba(255, 215, 0, 0);
			box-shadow: 0 0 0px rgba(0, 0, 0, .3);
			
			.el-menu-item {
				width: auto;
				height: 38px;
				line-height: 38px;
				padding: 0 14px 0 46px !important;
				margin: 0 0 6px;
				color: #5a729a !important;
				font-size: 13px;
				border-radius: 10px;
				border-width: 0;
				border-style: double;
				border-color: rgba(255, 255, 255, 1);
				background-color: transparent !important;
				box-shadow: none;
				text-align: left;
				min-width: auto;
        transition: all 0.2s ease;
                
                &.is-active {
                    color: #1f6fff !important;
                    font-size: 13px;
                    font-weight: 600;
                    border-radius: 10px;
                    border-width: 0;
                    border-style: solid;
                    border-color: rgba(0, 0, 0, 0);
                    background-color: rgba(31, 111, 255, 0.10) !important;
                    box-shadow: none;
                    text-align: left;
                }
				 
				&:hover {
					color: #1f6fff !important;
					border-radius: 10px;
					border-width: 0;
					border-style: solid;
					border-color: rgba(0, 0, 0, 0);
					background-color: rgba(31, 111, 255, 0.07) !important;
					box-shadow: none;
					text-align: left;
				}
			}
		}
	}
  }
</style>
<style>

.el-menu--horizontal .el-menu--popup {
		width: 100%;
		height: auto;
		padding: 8px;
		margin: 0;
		border-radius: 12px;
		border: 1px solid rgba(219, 231, 255, 0.95);
		background-color: #fff;
		box-shadow: 0 16px 28px rgba(15, 23, 42, 0.12);
		min-width: auto;
		}
.el-menu--horizontal .el-menu--popup .el-menu-item {
			width: 100%;
			height: 40px;
			line-height: 40px;
			padding: 0 14px;
			margin: 0 0 8px;
			color: #5a729a !important;
			font-size: 13px;
			border-radius: 10px;
			border-width: 0;
			border-style: double;
			border-color: rgba(255, 255, 255, 1);
			background-color: transparent !important;
			box-shadow: none;
			text-align: left;
			min-width: auto;
			} 
	.el-menu--horizontal .el-menu--popup .el-menu-item:hover {
				color: #1f6fff !important;
				border-radius: 10px;
				border-width: 0;
				border-style: solid;
				border-color: rgba(0, 0, 0, 0);
				background-color: rgba(31, 111, 255, 0.08) !important;
				box-shadow: none;
				text-align: left;
			}
</style>

<template>
  <el-breadcrumb class="app-breadcrumb" separator="//" style="height:50px;backgroundColor:rgba(255, 0, 0, 0);borderRadius:4px;padding:0px 20px 0px 20px;boxShadow:0px 0px 0px #f903d4;borderWidth:0;borderStyle: solid ;borderColor:rgba(255, 215, 0, 1);">
    <transition-group name="breadcrumb" class="box" :style="2==1?'justifyContent:flex-start;':2==2?'justifyContent:center;':'justifyContent:flex-end;'">
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <span v-if="item.redirect==='noRedirect'||index==levelList.length-1" class="no-redirect" style="color:rgba(0, 0, 0, 1)">{{ item.name }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.name }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
import pathToRegexp from 'path-to-regexp'
import { generateTitle } from '@/utils/i18n'
export default {
  data() {
    return {
      levelList: null
    }
  },
  watch: {
    $route() {
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
    this.breadcrumbStyleChange()
  },
  methods: {
    generateTitle,
    getBreadcrumb() {
      // only show routes with meta.title
      let route = this.$route
      let matched = route.matched.filter(item => item.meta)
      const first = matched[0]
      matched = [{ path: '/index' }].concat(matched)

      this.levelList = matched.filter(item => item.meta)
    },
    isDashboard(route) {
      const name = route && route.name
      if (!name) {
        return false
      }
      return name.trim().toLocaleLowerCase() === 'Index'.toLocaleLowerCase()
    },
    pathCompile(path) {
      // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink(item) {
      const { redirect, path } = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(path)
    },
    breadcrumbStyleChange(val) {
      this.$nextTick(()=>{
        document.querySelectorAll('.app-breadcrumb .el-breadcrumb__separator').forEach(el=>{
          el.innerText = "//"
          el.style.color = "rgba(0, 0, 0, 1)"
        })
        document.querySelectorAll('.app-breadcrumb .el-breadcrumb__inner a').forEach(el=>{
          el.style.color = "rgba(0, 0, 0, 1)"
        })
        document.querySelectorAll('.app-breadcrumb .el-breadcrumb__inner .no-redirect').forEach(el=>{
          el.style.color = "rgba(0, 0, 0, 1)"
        })
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.app-breadcrumb {
  display: block;
  font-size: 14px;
  line-height: 50px;
  padding: 0 18px !important;
  margin-top: 0 !important;
  border: 1px solid rgba(219, 231, 255, 0.95);
  border-radius: 6px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.08);

  .box {
    display: flex;
    width: 100%;
    height: 100%;
    justify-content: flex-start;
    align-items: center;
  }

  .no-redirect {
    color: #1f2d3d;
    cursor: text;
  }

  :deep(.el-breadcrumb__separator){
    margin: 0 10px;
    color: #9ab1d8 !important;
  }

  :deep(.el-breadcrumb__inner,
  .el-breadcrumb__inner a){
    color: #3f5f93 !important;
    font-weight: 600;
  }

  :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner){
    color: #1f6fff !important;
  }
}
</style>

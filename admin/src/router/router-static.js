import { createRouter, createWebHashHistory } from 'vue-router'
//1.创建组件
import Index from '@/views/index'
import Home from '@/views/home'
import Login from '@/views/login'
import NotFound from '@/views/404'
import UpdatePassword from '@/views/update-password'
import pay from '@/views/pay'
import register from '@/views/register'
import center from '@/views/center'
import FrontIndex from '@/views/front/index'
import FrontHome from '@/views/front/home'
import FrontXianxiahuodong from '@/views/front/xianxiahuodong'
import FrontJiaoyouxinxi from '@/views/front/jiaoyouxinxi'
import FrontForum from '@/views/front/forum'
import FrontNews from '@/views/front/news'
import FrontCenter from '@/views/front/center'
    import forum from '@/views/modules/forum/list'
    import news from '@/views/modules/news/list'
    import yonghu from '@/views/modules/yonghu/list'
    import discussxianxiahuodong from '@/views/modules/discussxianxiahuodong/list'
    import huodongbaoming from '@/views/modules/huodongbaoming/list'
    import xianxiahuodong from '@/views/modules/xianxiahuodong/list'
    import discussjiaoyouxinxi from '@/views/modules/discussjiaoyouxinxi/list'
    import jiaoyouxinxi from '@/views/modules/jiaoyouxinxi/list'
    import config from '@/views/modules/config/list'
    import storeup from '@/views/modules/storeup/list'
    import users from '@/views/modules/users/list'


//2.配置路由   注意：名字
const routes = [{
    path: '/index',
    component: Index,
    children: [{
      path: 'home',
      alias: '',
      name: 'index',
      component: Home,
      meta: {icon:'', title:'首页'}
    }, {
      path: 'updatePassword',
      alias: '/updatePassword',
      name: 'updatePassword',
      component: UpdatePassword,
      meta: {icon:'', title:'修改密码'}
    }, {
      path: 'pay',
      alias: '/pay',
      name: 'pay',
      component: pay,
      meta: {icon:'', title:'支付'}
    }, {
      path: 'center',
      alias: '/center',
      name: 'center',
      component: center,
      meta: {icon:'', title:'个人信息'}
    }
      ,{
    path: 'forum',
        alias: '/forum',
        name: 'forum',
        component: forum
      }
      ,{
    path: 'news',
        alias: '/news',
        name: 'news',
        component: news
      }
      ,{
    path: 'yonghu',
        alias: '/yonghu',
        name: 'yonghu',
        component: yonghu
      }
      ,{
    path: 'discussxianxiahuodong',
        alias: '/discussxianxiahuodong',
        name: 'discussxianxiahuodong',
        component: discussxianxiahuodong
      }
      ,{
    path: 'huodongbaoming',
        alias: '/huodongbaoming',
        name: 'huodongbaoming',
        component: huodongbaoming
      }
      ,{
    path: 'xianxiahuodong',
        alias: '/xianxiahuodong',
        name: 'xianxiahuodong',
        component: xianxiahuodong
      }
      ,{
    path: 'discussjiaoyouxinxi',
        alias: '/discussjiaoyouxinxi',
        name: 'discussjiaoyouxinxi',
        component: discussjiaoyouxinxi
      }
      ,{
    path: 'jiaoyouxinxi',
        alias: '/jiaoyouxinxi',
        name: 'jiaoyouxinxi',
        component: jiaoyouxinxi
      }
      ,{
    path: 'config',
        alias: '/config',
        name: 'config',
        component: config
      }
      ,{
    path: 'storeup',
        alias: '/storeup',
        name: 'storeup',
        component: storeup
      }
      ,{
    path: 'users',
        alias: '/users',
        name: 'users',
        component: users
      }
    ]
  },
  {
    path: '/',
    redirect: '/front/home'
  },
  {
    path: '/front',
    component: FrontIndex,
    meta: {icon:'', title:'front'},
    children: [
      {
        path: '',
        redirect: 'home'
      },
      {
        path: 'home',
        name: 'frontHome',
        component: FrontHome,
        meta: { icon: '', title: '前台首页' }
      },
      {
        path: 'xianxiahuodong',
        name: 'frontXianxiahuodong',
        component: FrontXianxiahuodong,
        meta: { icon: '', title: '线下活动' }
      },
      {
        path: 'xianxiahuodong/:id',
        name: 'frontXianxiahuodongDetail',
        component: FrontXianxiahuodong,
        meta: { icon: '', title: '活动详情' }
      },
      {
        path: 'jiaoyouxinxi',
        name: 'frontJiaoyouxinxi',
        component: FrontJiaoyouxinxi,
        meta: { icon: '', title: '交友信息' }
      },
      {
        path: 'jiaoyouxinxi/add',
        name: 'frontJiaoyouxinxiAdd',
        component: FrontJiaoyouxinxi,
        meta: { icon: '', title: '发布交友信息' }
      },
      {
        path: 'jiaoyouxinxi/:id',
        name: 'frontJiaoyouxinxiDetail',
        component: FrontJiaoyouxinxi,
        meta: { icon: '', title: '交友详情' }
      },
      {
        path: 'forum',
        name: 'frontForum',
        component: FrontForum,
        meta: { icon: '', title: '交流论坛' }
      },
      {
        path: 'forum/my',
        name: 'frontForumMy',
        component: FrontForum,
        meta: { icon: '', title: '我的帖子' }
      },
      {
        path: 'forum/add',
        name: 'frontForumAdd',
        component: FrontForum,
        meta: { icon: '', title: '发布帖子' }
      },
      {
        path: 'forum/:id',
        name: 'frontForumDetail',
        component: FrontForum,
        meta: { icon: '', title: '帖子详情' }
      },
      {
        path: 'news',
        name: 'frontNews',
        component: FrontNews,
        meta: { icon: '', title: '公告信息' }
      },
      {
        path: 'news/:id',
        name: 'frontNewsDetail',
        component: FrontNews,
        meta: { icon: '', title: '公告详情' }
      },
      {
        path: 'center',
        name: 'frontCenter',
        component: FrontCenter,
        meta: { icon: '', title: '个人中心' }
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: Login,
    meta: {icon:'', title:'login'}
  },
  {
    path: '/register',
    name: 'register',
    component: register,
    meta: {icon:'', title:'register'}
  },
  {
    path: '/admin/index',
    redirect: '/index'
  }, /*默认跳转路由*/
  {
    path: '/:pathMatch(.*)*',
    name: '404',
    component: NotFound
  }
]
//3.实例化VueRouter  注意：名字
const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router;

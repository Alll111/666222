import Vue from 'vue';
//配置路由
import VueRouter from 'vue-router'
Vue.use(VueRouter);
//1.创建组件
import Index from '@/views/index'
import Home from '@/views/home'
import Login from '@/views/login'
import NotFound from '@/views/404'
import UpdatePassword from '@/views/update-password'
import pay from '@/views/pay'
import register from '@/views/register'
import center from '@/views/center'
    import forum from '@/views/modules/forum/list'
    import news from '@/views/modules/news/list'
    import yonghu from '@/views/modules/yonghu/list'
    import discussxianxiahuodong from '@/views/modules/discussxianxiahuodong/list'
    import huodongbaoming from '@/views/modules/huodongbaoming/list'
    import xianxiahuodong from '@/views/modules/xianxiahuodong/list'
    import discussjiaoyouxinxi from '@/views/modules/discussjiaoyouxinxi/list'
    import jiaoyouxinxi from '@/views/modules/jiaoyouxinxi/list'
    import config from '@/views/modules/config/list'


//2.配置路由   注意：名字
const routes = [{
    path: '/index',
    component: Index,
    redirect: '/index/home',
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
    path: '/',
    redirect: '/admin/index'
  },
  {
    path: '/admin/index',
    redirect: '/index/home'
  }, /*默认跳转路由*/
  {
    path: '*',
    name: '404',
    component: NotFound
  }
]
//3.实例化VueRouter  注意：名字
const router = new VueRouter({
  mode: 'hash',
  /*hash模式改为history*/
  routes // （缩写）相当于 routes: routes
})

export default router;

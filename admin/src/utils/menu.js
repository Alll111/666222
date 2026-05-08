const menu = {
    list() {
        return [
            {
                backMenu: [
                    {
                        menu: '用户管理',
                        child: [
                            { appFrontIcon: 'cuIcon-phone', buttons: ['新增', '查看', '修改', '删除'], menu: '用户', menuJump: '列表', tableName: 'yonghu' }
                        ]
                    },
                    {
                        menu: '线下活动管理',
                        child: [
                            { appFrontIcon: 'cuIcon-clothes', buttons: ['新增', '查看', '报名', '修改', '删除', '查看评论'], menu: '线下活动', menuJump: '列表', tableName: 'xianxiahuodong' }
                        ]
                    },
                    {
                        menu: '交友信息管理',
                        child: [
                            { appFrontIcon: 'cuIcon-taxi', buttons: ['新增', '查看', '修改', '删除', '审核', '查看评论'], menu: '交友信息', menuJump: '列表', tableName: 'jiaoyouxinxi' }
                        ]
                    },
                    {
                        menu: '活动报名管理',
                        child: [
                            { appFrontIcon: 'cuIcon-album', buttons: ['新增', '查看', '修改', '删除', '审核'], menu: '活动报名', menuJump: '列表', tableName: 'huodongbaoming' }
                        ]
                    },
                    {
                        menu: '交流论坛',
                        child: [
                            { appFrontIcon: 'cuIcon-group', buttons: ['新增', '查看', '修改', '删除'], menu: '交流论坛', tableName: 'forum' }
                        ]
                    },
                    {
                        menu: '互动管理',
                        child: [
                            { appFrontIcon: 'cuIcon-comment', buttons: ['新增', '查看', '修改', '删除', '查看评论'], menu: '活动评论', tableName: 'discussxianxiahuodong' },
                            { appFrontIcon: 'cuIcon-comment', buttons: ['新增', '查看', '修改', '删除', '查看评论'], menu: '交友评论', tableName: 'discussjiaoyouxinxi' },
                            { appFrontIcon: 'cuIcon-favor', buttons: ['新增', '查看', '修改', '删除'], menu: '收藏管理', tableName: 'storeup' }
                        ]
                    },
                    {
                        menu: '系统管理',
                        child: [
                            { appFrontIcon: 'cuIcon-news', buttons: ['新增', '查看', '修改', '删除'], menu: '公告信息', tableName: 'news' },
                            { appFrontIcon: 'cuIcon-album', buttons: ['查看', '修改', '删除'], menu: '轮播图管理', tableName: 'config' },
                            { appFrontIcon: 'cuIcon-settings', buttons: ['新增', '查看', '修改', '删除'], menu: '管理员账户', tableName: 'users' }
                        ]
                    }
                ],
                frontMenu: [
                    {
                        menu: '线下活动模块',
                        child: [
                            { appFrontIcon: 'cuIcon-copy', buttons: ['查看', '报名'], menu: '线下活动列表', menuJump: '列表', tableName: 'xianxiahuodong' }
                        ]
                    },
                    {
                        menu: '交友信息模块',
                        child: [
                            { appFrontIcon: 'cuIcon-addressbook', buttons: ['查看'], menu: '交友信息列表', menuJump: '列表', tableName: 'jiaoyouxinxi' }
                        ]
                    }
                ],
                hasBackLogin: '是',
                hasBackRegister: '否',
                hasFrontLogin: '否',
                hasFrontRegister: '否',
                roleName: '管理员',
                tableName: 'users'
            },
            {
                backMenu: [
                    {
                        menu: '交友信息管理',
                        child: [
                            { appFrontIcon: 'cuIcon-taxi', buttons: ['新增', '查看', '修改', '删除', '查看评论'], menu: '交友信息', menuJump: '列表', tableName: 'jiaoyouxinxi' }
                        ]
                    },
                    {
                        menu: '活动报名管理',
                        child: [
                            { appFrontIcon: 'cuIcon-album', buttons: ['查看'], menu: '活动报名', menuJump: '列表', tableName: 'huodongbaoming' }
                        ]
                    }
                ],
                frontMenu: [
                    {
                        menu: '线下活动模块',
                        child: [
                            { appFrontIcon: 'cuIcon-copy', buttons: ['查看', '报名'], menu: '线下活动列表', menuJump: '列表', tableName: 'xianxiahuodong' }
                        ]
                    },
                    {
                        menu: '交友信息模块',
                        child: [
                            { appFrontIcon: 'cuIcon-addressbook', buttons: ['查看'], menu: '交友信息列表', menuJump: '列表', tableName: 'jiaoyouxinxi' }
                        ]
                    }
                ],
                hasBackLogin: '是',
                hasBackRegister: '否',
                hasFrontLogin: '是',
                hasFrontRegister: '是',
                roleName: '用户',
                tableName: 'yonghu'
            }
        ]
    }
}

export default menu

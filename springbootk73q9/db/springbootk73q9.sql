/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : springbootk73q9

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 24/04/2026 17:02:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '配置参数值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '配置文件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'picture1', 'upload/picture1.jpg');
INSERT INTO `config` VALUES (2, 'picture2', 'upload/picture2.jpg');
INSERT INTO `config` VALUES (3, 'picture3', 'upload/picture3.jpg');

-- ----------------------------
-- Table structure for discussjiaoyouxinxi
-- ----------------------------
DROP TABLE IF EXISTS `discussjiaoyouxinxi`;
CREATE TABLE `discussjiaoyouxinxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint NOT NULL COMMENT '关联表id',
  `userid` bigint NOT NULL COMMENT '用户id',
  `nickname` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '评论内容',
  `reply` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1648994850821 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '交友信息评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of discussjiaoyouxinxi
-- ----------------------------
INSERT INTO `discussjiaoyouxinxi` VALUES (91, '2022-04-03 21:47:07', 1, 1, '用户名1', '评论内容1', '回复内容1');
INSERT INTO `discussjiaoyouxinxi` VALUES (92, '2022-04-03 21:47:07', 2, 2, '用户名2', '评论内容2', '回复内容2');
INSERT INTO `discussjiaoyouxinxi` VALUES (93, '2022-04-03 21:47:07', 3, 3, '用户名3', '评论内容3', '回复内容3');
INSERT INTO `discussjiaoyouxinxi` VALUES (94, '2022-04-03 21:47:07', 4, 4, '用户名4', '评论内容4', '回复内容4');
INSERT INTO `discussjiaoyouxinxi` VALUES (95, '2022-04-03 21:47:07', 5, 5, '用户名5', '评论内容5', '回复内容5');
INSERT INTO `discussjiaoyouxinxi` VALUES (96, '2022-04-03 21:47:07', 6, 6, '用户名6', '评论内容6', '回复内容6');
INSERT INTO `discussjiaoyouxinxi` VALUES (1648994850820, '2022-04-03 22:07:30', 36, 1648994822324, '111', '22222222222', NULL);

-- ----------------------------
-- Table structure for discussxianxiahuodong
-- ----------------------------
DROP TABLE IF EXISTS `discussxianxiahuodong`;
CREATE TABLE `discussxianxiahuodong`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint NOT NULL COMMENT '关联表id',
  `userid` bigint NOT NULL COMMENT '用户id',
  `nickname` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '评论内容',
  `reply` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1776773252000 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '线下活动评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of discussxianxiahuodong
-- ----------------------------
INSERT INTO `discussxianxiahuodong` VALUES (81, '2022-04-03 21:47:07', 1, 1, '用户名1', '评论内容1', '回复内容1');
INSERT INTO `discussxianxiahuodong` VALUES (82, '2022-04-03 21:47:07', 2, 2, '用户名2', '评论内容2', '回复内容2');
INSERT INTO `discussxianxiahuodong` VALUES (83, '2022-04-03 21:47:07', 3, 3, '用户名3', '评论内容3', '回复内容3');
INSERT INTO `discussxianxiahuodong` VALUES (84, '2022-04-03 21:47:07', 4, 4, '用户名4', '评论内容4', '回复内容4');
INSERT INTO `discussxianxiahuodong` VALUES (85, '2022-04-03 21:47:07', 5, 5, '用户名5', '评论内容5', '回复内容5');
INSERT INTO `discussxianxiahuodong` VALUES (86, '2022-04-03 21:47:07', 6, 6, '用户名6', '评论内容6', '回复内容6');
INSERT INTO `discussxianxiahuodong` VALUES (1648994871756, '2022-04-03 22:07:50', 21, 1648994822324, '111', '222', 'xxx');
INSERT INTO `discussxianxiahuodong` VALUES (1776773251999, '2026-04-21 20:07:31', 21, 20, '吕布', '666', NULL);

-- ----------------------------
-- Table structure for forum
-- ----------------------------
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '帖子内容',
  `parentid` bigint NULL DEFAULT NULL COMMENT '父节点id',
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `isdone` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1648994906453 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '交流论坛' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of forum
-- ----------------------------
INSERT INTO `forum` VALUES (51, '2022-04-03 21:47:07', '帖子标题1', '帖子内容1', 0, 1, '用户名1', '开放');
INSERT INTO `forum` VALUES (52, '2022-04-03 21:47:07', '帖子标题2', '帖子内容2', 0, 2, '用户名2', '开放');
INSERT INTO `forum` VALUES (53, '2022-04-03 21:47:07', '帖子标题3', '帖子内容3', 0, 3, '用户名3', '开放');
INSERT INTO `forum` VALUES (54, '2022-04-03 21:47:07', '帖子标题4', '帖子内容4', 0, 4, '用户名4', '开放');
INSERT INTO `forum` VALUES (55, '2022-04-03 21:47:07', '帖子标题5', '帖子内容5', 0, 5, '用户名5', '开放');
INSERT INTO `forum` VALUES (56, '2022-04-03 21:47:07', '帖子标题6', '帖子内容6', 0, 6, '用户名6', '开放');
INSERT INTO `forum` VALUES (1648994894980, '2022-04-03 22:08:14', NULL, '111', 51, 1648994822324, '111', NULL);
INSERT INTO `forum` VALUES (1648994906452, '2022-04-03 22:08:25', '1111111111', '<p>请输入内容</p>', 0, 1648994822324, '111', '开放');

-- ----------------------------
-- Table structure for haoyou
-- ----------------------------
DROP TABLE IF EXISTS `haoyou`;
CREATE TABLE `haoyou`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint NOT NULL COMMENT '用户id',
  `friend_userid` bigint NOT NULL COMMENT '好友用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1776774611050 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '好友关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of haoyou
-- ----------------------------
INSERT INTO `haoyou` VALUES (1773296860535, '2026-03-12 14:27:40', 11, 15);
INSERT INTO `haoyou` VALUES (1773296861254, '2026-03-12 14:27:40', 15, 11);
INSERT INTO `haoyou` VALUES (1773301856427, '2026-03-12 15:50:55', 1773301208939, 1773301184192);
INSERT INTO `haoyou` VALUES (1773301856486, '2026-03-12 15:50:55', 1773301184192, 1773301208939);
INSERT INTO `haoyou` VALUES (1773824498764, '2026-03-18 17:01:38', 1773301184192, 1773824351051);
INSERT INTO `haoyou` VALUES (1773824499128, '2026-03-18 17:01:38', 1773824351051, 1773301184192);
INSERT INTO `haoyou` VALUES (1776774610803, '2026-04-21 20:30:10', 18, 20);
INSERT INTO `haoyou` VALUES (1776774611049, '2026-04-21 20:30:10', 20, 18);

-- ----------------------------
-- Table structure for haoyoushenqing
-- ----------------------------
DROP TABLE IF EXISTS `haoyoushenqing`;
CREATE TABLE `haoyoushenqing`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `from_userid` bigint NOT NULL COMMENT '申请人用户id',
  `from_zhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '申请人账号',
  `from_xingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '申请人姓名',
  `to_userid` bigint NOT NULL COMMENT '被申请人用户id',
  `to_zhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '被申请人账号',
  `to_xingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '被申请人姓名',
  `status` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '待处理' COMMENT '状态(待处理/同意/拒绝)',
  `reply` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '审核回复',
  `ref_jiaoyouxinxi_id` bigint NULL DEFAULT NULL COMMENT '关联交友信息id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1776774580168 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '好友申请' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of haoyoushenqing
-- ----------------------------
INSERT INTO `haoyoushenqing` VALUES (1773301806184, '2026-03-12 15:50:05', 1773301184192, '张飞', '张飞', 1773301208939, '关云长', '关羽', '同意', '好的', 1773301627864);
INSERT INTO `haoyoushenqing` VALUES (1773824482461, '2026-03-18 17:01:21', 1773301184192, '张飞', '张飞', 1773824351051, '吕布', '吕布', '同意', '1', 1773824384858);
INSERT INTO `haoyoushenqing` VALUES (1776773769484, '2026-04-21 20:16:08', 18, '张飞', '张飞', 20, '吕布', '吕布', '同意', NULL, 1773824384858);

-- ----------------------------
-- Table structure for huodongbaoming
-- ----------------------------
DROP TABLE IF EXISTS `huodongbaoming`;
CREATE TABLE `huodongbaoming`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `huodongmingcheng` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '活动名称',
  `huodongleixing` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '活动类型',
  `baomingshijian` datetime NULL DEFAULT NULL COMMENT '报名时间',
  `baomingshuoming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '报名说明',
  `zhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账号',
  `xingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `sfsh` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '否' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '审核回复',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1648994878097 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '活动报名' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of huodongbaoming
-- ----------------------------
INSERT INTO `huodongbaoming` VALUES (41, '2022-04-03 21:47:07', '活动名称1', '活动类型1', '2022-04-03 21:47:07', '报名说明1', '账号1', '姓名1', '是', '');
INSERT INTO `huodongbaoming` VALUES (42, '2022-04-03 21:47:07', '活动名称2', '活动类型2', '2022-04-03 21:47:07', '报名说明2', '账号2', '姓名2', '是', '');
INSERT INTO `huodongbaoming` VALUES (43, '2022-04-03 21:47:07', '活动名称3', '活动类型3', '2022-04-03 21:47:07', '报名说明3', '账号3', '姓名3', '是', '');
INSERT INTO `huodongbaoming` VALUES (44, '2022-04-03 21:47:07', '活动名称4', '活动类型4', '2022-04-03 21:47:07', '报名说明4', '账号4', '姓名4', '是', '');
INSERT INTO `huodongbaoming` VALUES (45, '2022-04-03 21:47:07', '活动名称5', '活动类型5', '2022-04-03 21:47:07', '报名说明5', '账号5', '姓名5', '是', '');
INSERT INTO `huodongbaoming` VALUES (46, '2022-04-03 21:47:07', '活动名称6', '活动类型6', '2022-04-03 21:47:07', '报名说明6', '账号6', '姓名6', '是', '');
INSERT INTO `huodongbaoming` VALUES (1648994878096, '2022-04-03 22:07:57', '活动名称1', '活动类型1', '2022-04-03 22:06:52', 'xxxx', '111', '张三', '是', 'xxx');

-- ----------------------------
-- Table structure for jiaoyouxinxi
-- ----------------------------
DROP TABLE IF EXISTS `jiaoyouxinxi`;
CREATE TABLE `jiaoyouxinxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账号',
  `xingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `xingbie` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '性别',
  `nianling` int NULL DEFAULT NULL COMMENT '年龄',
  `xingquaihao` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '兴趣爱好',
  `jiaoyoumude` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '交友目的',
  `gerenjianjie` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '个人简介',
  `jiaoyoutupian` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '交友图片',
  `sfsh` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '否' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '审核回复',
  `thumbsupnum` int NULL DEFAULT 0 COMMENT '赞',
  `crazilynum` int NULL DEFAULT 0 COMMENT '踩',
  `clicktime` datetime NULL DEFAULT NULL COMMENT '最近点击时间',
  `clicknum` int NULL DEFAULT 0 COMMENT '点击次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1777018941348 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '交友信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of jiaoyouxinxi
-- ----------------------------
INSERT INTO `jiaoyouxinxi` VALUES (31, '2022-04-03 21:47:07', '账号1', '姓名1', '性别1', 1, '兴趣爱好1', '交友目的1', '<p>个人简介1</p>', 'upload/1777020576582.jpg', '是', '', 1, 1, '2026-04-24 16:49:25', 42);
INSERT INTO `jiaoyouxinxi` VALUES (32, '2022-04-03 21:47:07', '账号2', '姓名2', '性别2', 2, '兴趣爱好2', '交友目的2', '<p>个人简介2</p>', 'upload/1777020760238.jpg', '是', '', 2, 2, '2026-04-24 16:52:35', 10);
INSERT INTO `jiaoyouxinxi` VALUES (33, '2022-04-03 21:47:07', '账号3', '姓名3', '性别3', 3, '兴趣爱好3', '交友目的3', '<p>个人简介3</p>', 'upload/1777020811938.jpg', '是', '', 3, 3, '2026-04-24 16:53:57', 5);
INSERT INTO `jiaoyouxinxi` VALUES (34, '2022-04-03 21:47:07', '账号4', '姓名4', '性别4', 4, '兴趣爱好4', '交友目的4', '<p>个人简介4</p>', 'upload/1777020891394.jpg', '是', '', 4, 4, '2026-04-24 16:54:45', 7);
INSERT INTO `jiaoyouxinxi` VALUES (35, '2022-04-03 21:47:07', '账号5', '姓名5', '性别5', 5, '兴趣爱好5', '交友目的5', '<p>个人简介5</p>', 'upload/1777020594597.jpg', '是', '', 5, 5, '2026-04-24 16:49:47', 44);
INSERT INTO `jiaoyouxinxi` VALUES (36, '2022-04-03 21:47:07', '账号6', '姓名6', '性别6', 6, '兴趣爱好6', '交友目的6', '<p>个人简介6</p>', 'upload/1777020985009.jpg', '是', '', 7, 6, '2026-04-24 16:56:21', 12);
INSERT INTO `jiaoyouxinxi` VALUES (1648994990257, '2022-04-03 22:09:49', '111', '张三', '男', 20, 'XXXXXXXXXXXXXX', 'XXXXXXXXXXXXXXX', '<p>XXXXXXXXXXXXXXXXXX</p>', 'upload/1648994983994.png', '是', 'xxx', 0, 0, NULL, 0);
INSERT INTO `jiaoyouxinxi` VALUES (1773301544548, '2026-03-12 15:45:44', '张飞', '张飞', '男', 21, '结识好友', '桃园三结义', '<p>俺也一样</p>', 'upload/1773301498787.jpg', '是', '张飞，通过', 0, 0, '2026-03-12 15:46:58', 1);
INSERT INTO `jiaoyouxinxi` VALUES (1773301627864, '2026-03-12 15:47:07', '关云长', '关羽', '男', 25, '耍大刀', '结识好友12', '<p>大哥</p>', 'upload/1773301604448.jpg', '是', '关羽，通过', 0, 0, '2026-03-12 15:50:05', 3);
INSERT INTO `jiaoyouxinxi` VALUES (1773824384858, '2026-03-18 16:59:44', '吕布', '吕布', '男', 28, '貂蝉', '貂蝉', '<p>貂蝉</p>', '', '是', '', 0, 0, '2026-04-24 16:19:57', 4);
INSERT INTO `jiaoyouxinxi` VALUES (1777018941347, '2026-04-24 16:22:20', '罗', '罗', '男', 22, '唱跳', 'rap篮球', '', 'upload/1777018922330.jpg', '是', '', 1, 0, '2026-04-24 16:23:57', 2);

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '标题',
  `introduction` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '简介',
  `picture` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '图片',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1648995089259 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '公告信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (71, '2022-04-03 21:47:07', '有梦想，就要努力去实现', '不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。', 'upload/news_picture1.jpg', '<p>不管你想要怎样的生活，你都要去努力争取，不多尝试一些事情怎么知道自己适合什么、不适合什么呢?</p><p>你说你喜欢读书，让我给你列书单，你还问我哪里有那么多时间看书;你说自己梦想的职业是广告文案，问我如何成为一个文案，应该具备哪些素质;你说你计划晨跑，但总是因为学习、工作辛苦或者身体不舒服第二天起不了床;你说你一直梦想一个人去长途旅行，但是没钱，父母觉得危险。其实，我已经厌倦了你这样说说而已的把戏，我觉得就算我告诉你如何去做，你也不会照做，因为你根本什么都不做。</p><p>真正有行动力的人不需要别人告诉他如何做，因为他已经在做了。就算碰到问题，他也会自己想办法，自己动手去解决或者主动寻求可以帮助他的人，而不是等着别人为自己解决问题。</p><p>首先要学习独立思考。花一点时间想一下自己喜欢什么，梦想是什么，不要别人说想环游世界，你就说你的梦想是环游世界。</p><p>很多人说现实束缚了自己，其实在这个世界上，我们一直都可以有很多选择，生活的决定权也—直都在自己手上，只是我们缺乏行动力而已。</p><p>如果你觉得安于现状是你想要的，那选择安于现状就会让你幸福和满足;如果你不甘平庸，选择一条改变、进取和奋斗的道路，在这个追求的过程中，你也一样会感到快乐。所谓的成功，即是按照自己想要的生活方式生活。最糟糕的状态，莫过于当你想要选择一条不甘平庸、改变、进取和奋斗的道路时，却以一种安于现状的方式生活，最后抱怨自己没有得到想要的人生。</p><p>因为喜欢，你不是在苦苦坚持，也因为喜欢，你愿意投入时间、精力，长久以往，获得成功就是自然而然的事情。</p>');
INSERT INTO `news` VALUES (72, '2022-04-03 21:47:07', '又是一年毕业季', '又是一年毕业季，感慨万千，还记的自己刚进学校那时候的情景，我拖着沉重的行李箱站在偌大的教学楼前面，感叹自己未来的日子即将在这个陌生的校园里度过，而如今斗转星移，浮光掠影，弹指之间，那些青葱岁月如同白驹过隙般悄然从指缝溜走。过去的种种在胸口交集纠结，像打翻的五味瓶，甜蜜，酸楚，苦涩，一并涌上心头。', 'upload/news_picture2.jpg', '<p>又是一年毕业季，感慨万千，还记的自己刚进学校那时候的情景，我拖着沉重的行李箱站在偌大的教学楼前面，感叹自己未来的日子即将在这个陌生的校园里度过，而如今斗转星移，浮光掠影，弹指之间，那些青葱岁月如同白驹过隙般悄然从指缝溜走。</p><p>过去的种种在胸口交集纠结，像打翻的五味瓶，甜蜜，酸楚，苦涩，一并涌上心头。一直都是晚会的忠实参与者，无论是台前还是幕后，忽然间，角色转变，那种感觉确实难以用语言表达。</p><p>	过去的三年，总是默默地期盼着这个好雨时节，因为这时候，会有灿烂的阳光，会有满目的百花争艳，会有香甜的冰激凌，这是个毕业的季节，当时不经世事的我们会殷切地期待学校那一大堆的活动，期待穿上绚丽的演出服或者礼仪服，站在大礼堂镁光灯下尽情挥洒我们的澎拜的激情。</p><p>百感交集，隔岸观火与身临其境的感觉竟是如此不同。从来没想过一场晚会送走的是我们自己的时候会是怎样的感情，毕业就真的意味着结束吗?倔强的我们不愿意承认，谢谢学弟学妹们慷慨的将这次的主题定为“我们在这里”。我知道，这可能是他们对我们这些过来人的尊敬和施舍。</p><p>没有为这场晚会排练、奔波，没有为班级、学生会、文学院出点力，还真有点不习惯，百般无奈中，用“工作忙”个万能的借口来搪塞自己，欺骗别人。其实自己心里明白，那只是在逃避，只是不愿面对繁华落幕后的萧条和落寞。大四了，大家各奔东西，想凑齐班上的人真的是难上加难，敏燕从越南回来，刚落地就匆匆回了学校，那么恋家的人也启程回来了，睿睿学姐也是从家赶来跟我们团圆。大家—如既往的寒暄、打趣、调侃对方，似乎一切又回到了当初的单纯美好。</p><p>看着舞台上活泼可爱的学弟学妹们，如同一群机灵的小精灵，清澈的眼神，稚嫩的肢体，轻快地步伐，用他们那热情洋溢的舞姿渲染着在场的每一个人，我知道，我不应该羡慕嫉妒他们，不应该顾自怜惜逝去的青春，不应该感叹夕阳无限好，曾经，我们也拥有过，曾经，我们也年轻过，曾经，我们也灿烂过。我深深地告诉自己，人生的每个阶段都是美的，年轻有年轻的活力，成熟也有成熟的魅力。多—份稳重、淡然、优雅，也是漫漫时光掠影遗留下的.珍贵赏赐。</p>');
INSERT INTO `news` VALUES (73, '2022-04-03 21:47:07', '挫折路上，坚持常在心间', '回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。', 'upload/news_picture3.jpg', '<p>回头看看，你会不会发现，曾经的你在这里摔倒过;回头看看，你是否发现，一次次地重复着，却从没爬起过。而如今，让我们把视线转向前方，那一道道金色的弧线，是流星飞逝的痕迹，或是成功运行的轨道。今天的你，是否要扬帆起航，让幸福来敲门?</p><p>清晨的太阳撒向大地，神奇的宇宙赋予它神奇的色彩，大自然沐浴着春光，世界因太阳的照射而精彩，林中百鸟啾啾，河水轻轻流淌，汇成清宁的山间小调。</p><p>是的，面对道途上那无情的嘲讽，面对步伐中那重复的摔跤，面对激流与硬石之间猛烈的碰撞，我们必须选择那富于阴雨，却最终见到彩虹的荆棘路。也许，经历了那暴风雨的洗礼，我们便会变得自信，幸福也随之而来。</p><p>司马迁屡遭羞辱，却依然在狱中撰写《史记》，作为一名史学家，不因王权而极度赞赏，也不因卑微而极度批判，然而他在坚持自己操守的同时，却依然要受统治阶级的阻碍，他似乎无权选择自己的本职。但是，他不顾于此，只是在面对道途的阻隔之时，他依然选择了走下去的信念。终于一部开山巨作《史记》诞生，为后人留下一份馈赠，也许在他完成毕生的杰作之时，他微微地笑了，没有什么比梦想实现更快乐的了......</p><p>	或许正如“长风破浪会有时，直挂云帆济沧海”一般，欣欣然地走向看似深渊的崎岖路，而在一番耕耘之后，便会发现这里另有一番天地。也许这就是困难与快乐的交融。</p><p>也许在形形色色的社会中，我们常能看到一份坚持，一份自信，但这里却还有一类人。这类人在暴风雨来临之际，只会闪躲，从未懂得这也是一种历炼，这何尝不是一份快乐。在阴暗的角落里，总是独自在哭，带着伤愁，看不到一点希望。</p><p>我们不能堕落于此，而要像海燕那般，在苍茫的大海上，高傲地飞翔，任何事物都无法阻挡，任何事都是幸福快乐的。</p>');
INSERT INTO `news` VALUES (74, '2022-04-03 21:47:07', '挫折是另一个生命的开端', '当遇到挫折或失败，你是看见失败还是看见机会?挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。', 'upload/news_picture4.jpg', '<p>当遇到挫折或失败，你是看见失败还是看见机会?</p><p>挫折是我们每个人成长的必经之路，它不是你想有就有，想没有就没有的。有句名言说的好，如果你想一生摆脱苦难，你就得是神或者是死尸。这句话形象地说明了挫折是伴随着人生的，是谁都逃不掉的。</p><p>人生在世，从古到今，不分天子平民，机遇虽有不同，但总不免有身陷困境或遭遇难题之处，这时候唯有通权达变，才能使人转危为安，甚至反败为胜。</p><p>大部分的人，一生当中，最痛苦的经验是失去所爱的人，其次是丢掉一份工作。其实，经得起考验的人，就算是被开除也不会惊慌，要学会面对。</p><p>	“塞翁失马，焉知非福。”人生的道路，并不是每一步都迈向成功，这就是追求的意义。我们还要认识到一点，挫折作为一种情绪状态和一种个人体验，各人的耐受性是大不相同的，有的人经历了一次次挫折，就能够坚忍不拔，百折不挠;有的人稍遇挫折便意志消沉，一蹶不振。所以，挫折感是一种主观感受，因为人的目的和需要不同，成功标准不同，所以同一种活动对于不同的人可能会造成不同的挫折感受。</p><p>凡事皆以平常心来看待，对于生命顺逆不要太执著。能够“破我执”是很高层的人生境界。</p><p>人事的艰难就是一种考验。就像—支剑要有磨刀来磨，剑才会利:一块璞玉要有粗石来磨，才会发出耀眼的光芒。我们能够做到的，只是如何减少、避免那些由于自身的原因所造成的挫折，而在遇到痛苦和挫折之后，则力求化解痛苦，争取幸福。我们要知道，痛苦和挫折是双重性的，它既是我们人生中难以完全避免的，也是我们在争取成功时，不可缺少的一种动力。因为我认为，推动我们奋斗的力量，不仅仅是对成功的渴望，还有为摆脱痛苦和挫折而进行的奋斗。</p>');
INSERT INTO `news` VALUES (75, '2022-04-03 21:47:07', '你要去相信，没有到不了的明天', '有梦想就去努力，因为在这一辈子里面，现在不去勇敢的努力，也许就再也没有机会了。你要去相信，一定要相信，没有到不了的明天。不要被命运打败，让自己变得更强大。不管你现在是一个人走在异乡的街道上始终没有找到一丝归属感，还是你在跟朋友们一起吃饭开心址笑着的时候闪过一丝落寞。', 'upload/news_picture5.jpg', '<p>有梦想就去努力，因为在这一辈子里面，现在不去勇敢的努力，也许就再也没有机会了。你要去相信，一定要相信，没有到不了的明天。不要被命运打败，让自己变得更强大。</p><p>不管你现在是一个人走在异乡的街道上始终没有找到一丝归属感，还是你在跟朋友们一起吃饭开心址笑着的时候闪过一丝落寞。</p><p>	不管你现在是在图书馆里背着怎么也看不进去的英语单词，还是你现在迷茫地看不清未来的方向不知道要往哪走。</p><p>不管你现在是在努力着去实现梦想却没能拉近与梦想的距离，还是你已经慢慢地找不到自己的梦想了。</p><p>你都要去相信，没有到不了的明天。</p><p>	有的时候你的梦想太大，别人说你的梦想根本不可能实现;有的时候你的梦想又太小，又有人说你胸无大志;有的时候你对死党说着将来要去环游世界的梦想，却换来他的不屑一顾，于是你再也不提自己的梦想;有的时候你突然说起将来要开个小店的愿望，却发现你讲述的那个人，并没有听到你在说什么。</p><p>不过又能怎么样呢，未来始终是自己的，梦想始终是自己的，没有人会来帮你实现它。</p><p>也许很多时候我们只是需要朋友的一句鼓励，一句安慰，却也得不到。但是相信我，世界上还有很多人，只是想要和你说说话。</p><p>因为我们都一样。一样的被人说成固执，一样的在追逐他们眼里根本不在意的东西。</p><p>所以，又有什么关系呢，别人始终不是你、不能懂你的心情，你又何必多去解释呢。这个世界会来阻止你，困难也会接踵而至，其实真正关键的只有自己，有没有那个倔强。</p><p>这个世界上没有不带伤的人，真正能治愈自己的，只有自己。</p>');
INSERT INTO `news` VALUES (76, '2022-04-03 21:47:07', '离开是一种痛苦，是一种勇气，但同样也是一个考验，是一个新的开端', '无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。', 'upload/news_picture6.jpg', '<p>无穷无尽是离愁，天涯海角遍寻思。当离别在即之时，当面对着相濡以沫兄弟般的朋友时，当面对着经历了四年的磨合而形成的真挚友谊之时，我内心激动无语，说一声再见，道一声珍重都很难出口。回想自己四年大学的风风雨雨，回想我们曾经共同经历的岁月流年，我感谢大家的相扶相依，感谢朋友们的莫大支持与帮助。虽然舍不得，但离别的脚步却不因我们的挚情而停滞。离别的确是一种痛苦，但同样也是我们走入社会，走向新环境、新领域的一个开端，希望大家在以后新的工作岗位上能够确定自己的新起点，坚持不懈，向着更新、更高的目标前进，因为人生最美好的东西永远都在最前方!</p><p>忆往昔峥嵘岁月，看今朝潮起潮落，望未来任重而道远。作为新时代的我们，就应在失败时，能拼搏奋起，去谱写人生的辉煌。在成功时，亦能居安思危，不沉湎于一时的荣耀、鲜花和掌声中，时时刻刻怀着一颗积极寻找自己新的奶酪的心，处变不惊、成败不渝，始终踏着自己坚实的步伐，从零开始，不断向前迈进，这样才能在这风起云涌、变幻莫测的社会大潮中成为真正的弄潮儿!</p>');
INSERT INTO `news` VALUES (1648995089258, '2022-04-03 22:11:28', 'xxx', 'xxx', 'upload/1648995087790.png', '<p>xxx</p>');

-- ----------------------------
-- Table structure for siliao_huihua
-- ----------------------------
DROP TABLE IF EXISTS `siliao_huihua`;
CREATE TABLE `siliao_huihua`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `user1_id` bigint NOT NULL COMMENT '用户1(小id)',
  `user2_id` bigint NOT NULL COMMENT '用户2(大id)',
  `last_msg` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '最后一条消息',
  `last_time` datetime NULL DEFAULT NULL COMMENT '最后消息时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_pair`(`user1_id` ASC, `user2_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1776774638113 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '私聊会话' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of siliao_huihua
-- ----------------------------
INSERT INTO `siliao_huihua` VALUES (1773300301117, '2026-03-12 15:25:00', 11, 15, '你有推荐的嘛', '2026-03-12 15:31:02');
INSERT INTO `siliao_huihua` VALUES (1773301262024, '2026-03-12 15:41:01', 1, 11, NULL, '2026-03-12 15:41:02');
INSERT INTO `siliao_huihua` VALUES (1773301892003, '2026-03-12 15:51:31', 1773301184192, 1773301208939, '大哥，好像在看书', '2026-03-12 15:52:58');
INSERT INTO `siliao_huihua` VALUES (1773824508629, '2026-03-18 17:01:48', 1773301184192, 1773824351051, '我是奉先', '2026-03-18 17:01:58');
INSERT INTO `siliao_huihua` VALUES (1776774638112, '2026-04-21 20:30:37', 18, 20, '111', '2026-04-21 20:31:04');

-- ----------------------------
-- Table structure for siliao_xiaoxi
-- ----------------------------
DROP TABLE IF EXISTS `siliao_xiaoxi`;
CREATE TABLE `siliao_xiaoxi`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `session_id` bigint NOT NULL COMMENT '会话id',
  `from_userid` bigint NOT NULL COMMENT '发送人id',
  `to_userid` bigint NOT NULL COMMENT '接收人id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '内容',
  `is_read` int NULL DEFAULT 0 COMMENT '是否已读(0/1)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_to_read`(`to_userid` ASC, `is_read` ASC) USING BTREE,
  INDEX `idx_session`(`session_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1776774663938 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '私聊消息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of siliao_xiaoxi
-- ----------------------------
INSERT INTO `siliao_xiaoxi` VALUES (1773300361504, '2026-03-12 15:26:00', 1773300301117, 15, 11, '你好哇', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1773300413741, '2026-03-12 15:26:52', 1773300301117, 11, 15, '在的哈', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1773300628599, '2026-03-12 15:30:28', 1773300301117, 15, 11, '今天去哪玩啊', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1773300647715, '2026-03-12 15:30:47', 1773300301117, 11, 15, '我想想哈', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1773300661987, '2026-03-12 15:31:01', 1773300301117, 11, 15, '你有推荐的嘛', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1773301933065, '2026-03-12 15:52:13', 1773301892003, 1773301208939, 1773301184192, '三弟，大哥去哪了', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1773301950732, '2026-03-12 15:52:30', 1773301892003, 1773301208939, 1773301184192, '三弟', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1773301978697, '2026-03-12 15:52:58', 1773301892003, 1773301184192, 1773301208939, '大哥，好像在看书', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1773824518522, '2026-03-18 17:01:58', 1773824508629, 1773824351051, 1773301184192, '我是奉先', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1776774648400, '2026-04-21 20:30:47', 1776774638112, 20, 18, '11', 1);
INSERT INTO `siliao_xiaoxi` VALUES (1776774663937, '2026-04-21 20:31:03', 1776774638112, 18, 20, '111', 1);

-- ----------------------------
-- Table structure for storeup
-- ----------------------------
DROP TABLE IF EXISTS `storeup`;
CREATE TABLE `storeup`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint NOT NULL COMMENT '用户id',
  `refid` bigint NULL DEFAULT NULL COMMENT '收藏id',
  `tablename` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表名',
  `name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '收藏名称',
  `picture` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '收藏图片',
  `type` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '1' COMMENT '类型(1:收藏,21:赞,22:踩)',
  `inteltype` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '推荐类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1777019036270 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storeup
-- ----------------------------
INSERT INTO `storeup` VALUES (1648994844209, '2022-04-03 22:07:23', 1648994822324, 36, 'jiaoyouxinxi', '账号6', 'upload/jiaoyouxinxi_jiaoyoutupian6.jpg', '1', NULL);
INSERT INTO `storeup` VALUES (1648994846329, '2022-04-03 22:07:25', 1648994822324, 36, 'jiaoyouxinxi', '账号6', 'upload/jiaoyouxinxi_jiaoyoutupian6.jpg', '21', NULL);
INSERT INTO `storeup` VALUES (1648994868414, '2022-04-03 22:07:47', 1648994822324, 21, 'xianxiahuodong', '活动名称1', 'upload/xianxiahuodong_huodongtupian1.jpg', '1', NULL);
INSERT INTO `storeup` VALUES (1773298577524, '2026-03-12 14:56:16', 11, 35, 'jiaoyouxinxi', '账号5', 'upload/jiaoyouxinxi_jiaoyoutupian5.jpg', '1', NULL);
INSERT INTO `storeup` VALUES (1777019036269, '2026-04-24 16:23:55', 1777018723038, 1777018941347, 'jiaoyouxinxi', '罗', 'upload/1777018922330.jpg', '21', NULL);

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint NOT NULL COMMENT '用户id',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `tablename` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'token表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1, 1648994822324, '111', 'yonghu', '用户', 'yfwueso09pnzropn5walxi5knwfg69oe', '2022-04-03 22:07:06', '2022-04-03 23:11:37');
INSERT INTO `token` VALUES (2, 1, 'abo', 'users', '管理员', '0lld0z4ch6l6j40qstt0uo0kiq4gz82o', '2022-04-03 22:10:32', '2026-04-24 17:22:46');
INSERT INTO `token` VALUES (3, 11, '账号1', 'yonghu', '用户', 'ozf8onsccx32sooqd5wuxg0nla518xus', '2026-03-12 10:42:21', '2026-04-24 17:48:41');
INSERT INTO `token` VALUES (4, 12, '账号2', 'yonghu', '用户', 'ghkmakvz3kg9f92zhzm5ual7hgtloch3', '2026-03-12 10:48:24', '2026-04-24 17:51:51');
INSERT INTO `token` VALUES (5, 15, '账号5', 'yonghu', '用户', 'gdq3xiyc75gxbo1yjb617gg36n0mzbbb', '2026-03-12 14:18:07', '2026-04-24 17:55:17');
INSERT INTO `token` VALUES (6, 1773301184192, '张飞', 'yonghu', '用户', 'z5g0n13l64pvw2sbdt46b4gotmtlafqc', '2026-03-12 15:40:16', '2026-03-18 18:11:22');
INSERT INTO `token` VALUES (7, 1773301208939, '关云长', 'yonghu', '用户', 'pll4wnlpfz7t8grmwv8pqb7sou3i696l', '2026-03-12 15:46:30', '2026-03-12 16:49:49');
INSERT INTO `token` VALUES (8, 1773824351051, '吕布', 'yonghu', '用户', 'hf9k0taa40ll8m7zohpjeifofgbv0r48', '2026-03-18 16:59:19', '2026-03-18 17:59:19');
INSERT INTO `token` VALUES (9, 20, '吕布', 'yonghu', '用户', 'i0bg0cafjo6ewm5h0sj3o6phl4xtebnz', '2026-04-21 19:41:13', '2026-04-24 17:18:14');
INSERT INTO `token` VALUES (10, 18, '张飞', 'yonghu', '用户', 'b660akodlnkfe5kto21tr32cxvwegal4', '2026-04-21 20:09:47', '2026-04-21 21:25:10');
INSERT INTO `token` VALUES (11, 1777018723038, '罗', 'yonghu', '用户', '1loyx3gz5vtd5x2iva8q27fb2wdjd1is', '2026-04-24 16:19:17', '2026-04-24 17:23:30');
INSERT INTO `token` VALUES (12, 13, '账号3', 'yonghu', '用户', '61ppgy2gr5kvm1gz1pzuah1vahbfiecp', '2026-04-24 16:53:08', '2026-04-24 17:53:08');
INSERT INTO `token` VALUES (13, 14, '账号4', 'yonghu', '用户', 'dphkmnatz6czwsklsctnjwgsqtqkxajy', '2026-04-24 16:54:32', '2026-04-24 17:54:32');
INSERT INTO `token` VALUES (14, 16, '账号6', 'yonghu', '用户', '8j1fqqry552x2ozms543kzcz0sk6w9nc', '2026-04-24 16:56:09', '2026-04-24 17:56:10');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `role` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '123456', '管理员', '2022-04-03 21:47:07');

-- ----------------------------
-- Table structure for xianxiahuodong
-- ----------------------------
DROP TABLE IF EXISTS `xianxiahuodong`;
CREATE TABLE `xianxiahuodong`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `huodongmingcheng` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '活动名称',
  `huodongleixing` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '活动类型',
  `huodongdidian` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '活动地点',
  `huodongshijian` datetime NULL DEFAULT NULL COMMENT '活动时间',
  `huodongneirong` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '活动内容',
  `huodongtupian` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '活动图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1648995049870 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '线下活动' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of xianxiahuodong
-- ----------------------------
INSERT INTO `xianxiahuodong` VALUES (21, '2022-04-03 21:47:07', '活动名称1', '活动类型1', '活动地点1', '2022-04-03 21:47:07', '活动内容1', 'upload/xianxiahuodong_huodongtupian1.jpg');
INSERT INTO `xianxiahuodong` VALUES (22, '2022-04-03 21:47:07', '活动名称2', '活动类型2', '活动地点2', '2022-04-03 21:47:07', '活动内容2', 'upload/xianxiahuodong_huodongtupian2.jpg');
INSERT INTO `xianxiahuodong` VALUES (23, '2022-04-03 21:47:07', '活动名称3', '活动类型3', '活动地点3', '2022-04-03 21:47:07', '活动内容3', 'upload/xianxiahuodong_huodongtupian3.jpg');
INSERT INTO `xianxiahuodong` VALUES (24, '2022-04-03 21:47:07', '活动名称4', '活动类型4', '活动地点4', '2022-04-03 21:47:07', '活动内容4', 'upload/xianxiahuodong_huodongtupian4.jpg');
INSERT INTO `xianxiahuodong` VALUES (25, '2022-04-03 21:47:07', '活动名称5', '活动类型5', '活动地点5', '2022-04-03 21:47:07', '活动内容5', 'upload/xianxiahuodong_huodongtupian5.jpg');
INSERT INTO `xianxiahuodong` VALUES (26, '2022-04-03 21:47:07', '活动名称6', '活动类型6', '活动地点6', '2022-04-03 21:47:07', '活动内容6', 'upload/xianxiahuodong_huodongtupian6.jpg');
INSERT INTO `xianxiahuodong` VALUES (1648995049869, '2022-04-03 22:10:49', 'xxxx', 'xxx', 'xxx', '2022-04-04 00:00:00', '<p>xxxxxxxxxxxxx</p>', 'upload/1648995047652.png');

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zhanghao` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '账号',
  `mima` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `xingming` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '姓名',
  `xingbie` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '性别',
  `nianling` float NULL DEFAULT NULL COMMENT '年龄',
  `shouji` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机',
  `touxiang` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `zhanghao`(`zhanghao` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1777018723039 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES (11, '2022-04-03 21:47:07', '账号1', '123456', '姓名1', '男', 1, '13823888881', 'upload/1777020545395.jpg');
INSERT INTO `yonghu` VALUES (12, '2022-04-03 21:47:07', '账号2', '123456', '姓名2', '男', 2, '13823888882', 'upload/1777020743274.jpg');
INSERT INTO `yonghu` VALUES (13, '2022-04-03 21:47:07', '账号3', '123456', '姓名3', '男', 3, '13823888883', 'upload/1777020794682.jpg');
INSERT INTO `yonghu` VALUES (14, '2022-04-03 21:47:07', '账号4', '123456', '姓名4', '男', 4, '13823888884', 'upload/1777020878789.jpg');
INSERT INTO `yonghu` VALUES (15, '2022-04-03 21:47:07', '账号5', '123456', '姓名5', '男', 5, '13823888885', 'upload/1777020927527.jpg');
INSERT INTO `yonghu` VALUES (16, '2022-04-03 21:47:07', '账号6', '123456', '姓名6', '男', 6, '13823888886', 'upload/1777020975740.jpg');
INSERT INTO `yonghu` VALUES (17, '2022-04-03 22:07:02', '111', '111', '张三', '男', 20, '18154541454', 'upload/1648994920242.png');
INSERT INTO `yonghu` VALUES (18, '2026-03-12 15:39:44', '张飞', '123456', '张飞', NULL, 21, '13111111111', 'upload/1773825102577.png');
INSERT INTO `yonghu` VALUES (19, '2026-03-12 15:40:08', '关云长', '123456', '关羽', NULL, 25, '', 'upload/1773301917915.jpg');
INSERT INTO `yonghu` VALUES (20, '2026-03-18 16:59:11', '吕布', '123456', '吕布', '', 28, '13111111111', 'upload/1776771716829.jpg');
INSERT INTO `yonghu` VALUES (1777018723038, '2026-04-24 16:18:43', '罗', '123456', '罗', '男', 22, '19155565412', 'upload/1777018764713.jpg');

SET FOREIGN_KEY_CHECKS = 1;

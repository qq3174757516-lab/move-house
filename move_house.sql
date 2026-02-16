/*
 Navicat Premium Dump SQL

 Source Server         : local@mysql01
 Source Server Type    : MySQL
 Source Server Version : 80028 (8.0.28)
 Source Host           : localhost:3001
 Source Schema         : move_house

 Target Server Type    : MySQL
 Target Server Version : 80028 (8.0.28)
 File Encoding         : 65001

 Date: 12/04/2025 17:10:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '202cb962ac59075b964b07152d234b70');

-- ----------------------------
-- Table structure for car
-- ----------------------------
DROP TABLE IF EXISTS `car`;
CREATE TABLE `car`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车辆名称',
  `car_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车牌号',
  `repair_year` smallint NOT NULL COMMENT '检修年份',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车辆类型',
  `status` bit(1) NOT NULL DEFAULT b'0' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of car
-- ----------------------------
INSERT INTO `car` VALUES (1, '奥迪A5', '湘A45644', 2023, '小型车', b'1');
INSERT INTO `car` VALUES (2, '比亚迪dim', '湘B34533', 2023, '中型车', b'0');
INSERT INTO `car` VALUES (3, '宝马1系', '粤B35568', 2023, '小型车', b'0');

-- ----------------------------
-- Table structure for complaint
-- ----------------------------
DROP TABLE IF EXISTS `complaint`;
CREATE TABLE `complaint`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `driver_id` bigint NOT NULL COMMENT '司机id',
  `driver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '司机名称',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '投诉理由',
  `imgs` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '图片',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of complaint
-- ----------------------------
INSERT INTO `complaint` VALUES (1, 1, 2, '李四', 1, 'ygq', '服务态度很不好', '/file/2023/05/19/c69cb491af4a4f9c980f51516030f196.jpeg', '2025-04-10 11:02:54');

-- ----------------------------
-- Table structure for driver
-- ----------------------------
DROP TABLE IF EXISTS `driver`;
CREATE TABLE `driver`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `car_id` bigint NULL DEFAULT NULL COMMENT '车辆id',
  `id_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `gender` bit(1) NOT NULL DEFAULT b'0' COMMENT '性别',
  `age` int NOT NULL DEFAULT 18 COMMENT '年龄',
  `car_age` int NOT NULL COMMENT '车龄',
  `income` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '收入',
  `complaint_num` int NOT NULL DEFAULT 0 COMMENT '投诉数量',
  `status` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0 COMMENT '0待审核, 1正常, 2解雇',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of driver
-- ----------------------------
INSERT INTO `driver` VALUES (2, 'd1', 1, '48958958959856889', '/file/2023/05/19/1fe53efd35434818b05f496b6150ae46.jpeg', '李四', '18985898587', 'a591024321c5e2bdbd23ed35f0574dde', b'0', 18, 10, 150.00, 1, 1);
INSERT INTO `driver` VALUES (3, 'zs', NULL, '485456200512035487', '', '张三', '18754526585', 'a591024321c5e2bdbd23ed35f0574dde', b'0', 18, 5, 0.00, 0, 0);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `driver_id` bigint NOT NULL COMMENT '司机id',
  `driver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '司机名称',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0未完成, 1已完成',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `arrive_time` datetime NULL DEFAULT NULL COMMENT '到达时间',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, '569140431252', 1, 'hc', 2, '李四', 200.00, 2, 'xxx', '2025-04-09 10:00:56', '2025-04-09 10:43:38', '2025-04-10 10:46:12');
INSERT INTO `orders` VALUES (2, '137772868917', 1, 'hc', 2, '李四', 150.00, 2, '湖南省长沙市岳麓区xxx大道xx楼栋341', '2025-04-10 10:18:56', '2025-04-10 10:21:51', '2025-04-10 10:23:49');

-- ----------------------------
-- Table structure for publish
-- ----------------------------
DROP TABLE IF EXISTS `publish`;
CREATE TABLE `publish`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面',
  `imgs` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详情图片',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
  `driver_id` bigint NULL DEFAULT NULL COMMENT '接单司机id',
  `valuation` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '价格',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of publish
-- ----------------------------
INSERT INTO `publish` VALUES (1, 1, 'hc', '出租速搬', '这个出租屋我已经xxxx, 这是一段描述, 现在我要搬家, 有xxx, 电冰箱', '/file/2023/05/18/bd23ff40cee443c5abbca2e8ded8bcda.jpeg', '/file/2023/05/18/229cc440061b45fca5d8dbbea9d3f20d.jpeg,/file/2023/05/18/9e749b3174a9435597921820e3f9b32d.jpeg', '湖南省长沙市岳麓区xxx大道xx楼栋341', 2, 150.00, 2, '2025-04-10 21:01:38');
INSERT INTO `publish` VALUES (2, 1, 'hc', 'xxx', 'xxx', '/file/2023/05/19/df75e3fa372649a194c8ae05c3a15064.jpeg', '', 'xxx', 2, 200.00, 2, '2025-04-10 08:58:48');

-- ----------------------------
-- Table structure for tip
-- ----------------------------
DROP TABLE IF EXISTS `tip`;
CREATE TABLE `tip`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `status` bit(1) NOT NULL DEFAULT b'0' COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tip
-- ----------------------------
INSERT INTO `tip` VALUES (5, '测试通知1', '<p style=\"text-align: center;\">测试通知内容</p><p style=\"text-align: center;\"><img src=\"http://localhost:8181/file/2023/05/18/c3b460cd722d41898dee73d36cee5878.jpeg\" alt=\"\" data-href=\"\" style=\"width: 50%;\"></p>', b'1', '2025-04-10 15:22:23');
INSERT INTO `tip` VALUES (7, '测试通知2', '<p>这是通知2的内容, 昨天有个司机没有按时到达顾客家, 遭遇投诉, 特此批评, 望大家引以为戒</p>', b'1', '2025-04-10 15:25:47');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'hc', '18374756858', 'a591024321c5e2bdbd23ed35f0574dde', 'hc', '/file/2023/05/19/779dad42899f4f1a839e5074bb00f492.jpeg', 650.00, '2025-04-10 20:27:12');
INSERT INTO `user` VALUES (2, 'she', '15958478596', '202cb962ac59075b964b07152d234b70', 'test', NULL, 0.00, '2025-04-10 11:49:44');

SET FOREIGN_KEY_CHECKS = 1;

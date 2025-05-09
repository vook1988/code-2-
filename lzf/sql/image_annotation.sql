/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : localhost:3306
 Source Schema         : image_annotation

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 09/05/2025 20:30:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_annotation
-- ----------------------------
DROP TABLE IF EXISTS `t_annotation`;
CREATE TABLE `t_annotation`  (
  `annotation_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标注ID（主键）',
  `image_id` int UNSIGNED NOT NULL COMMENT '关联图像ID（外键）',
  `label_id` int UNSIGNED NOT NULL COMMENT '关联标签ID（外键）',
  `coordinates` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标注坐标数据',
  `annotation_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标注类型（rectangle/rect：矩形；polygon：多边形；point：点）',
  `created_by` int UNSIGNED NOT NULL COMMENT '创建用户ID（外键）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '标注创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `label_file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签文件存储路径',
  `format` enum('VOC','COCO') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'VOC',
  `voc_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'VOC格式标注内容',
  PRIMARY KEY (`annotation_id`) USING BTREE,
  INDEX `image_id`(`image_id` ASC) USING BTREE,
  INDEX `label_id`(`label_id` ASC) USING BTREE,
  INDEX `created_by`(`created_by` ASC) USING BTREE,
  CONSTRAINT `t_annotation_ibfk_1` FOREIGN KEY (`image_id`) REFERENCES `t_image` (`image_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `t_annotation_ibfk_2` FOREIGN KEY (`label_id`) REFERENCES `t_label` (`label_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_annotation_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `t_user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图像标注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_annotation
-- ----------------------------
INSERT INTO `t_annotation` VALUES (1, 1, 7, '[[746.2557293455984, 305.79916668441155], [1152.1351054743989, 726.9371659759489], [563.152251392756, 275.28192035893784], [630.2901933087983, 723.8854413434016], [734.0488308154089, 818.48890495237], [1088.048888190904, 839.8509773802017], [1457.3075687291362, 772.7130354641595], [1463.4110179942309, 733.0406152410436], [1371.8592790178095, 415.661253456117], [1234.531670553178, 357.6784854377169], [1057.5316418654304, 336.3164130098853], [746.2557293455984, 305.79916668441155]]', 'polygon', 4, '2025-05-08 18:19:30', '2025-05-09 15:28:01', '1', 'VOC', '2');
INSERT INTO `t_annotation` VALUES (2, 1, 8, '[[229.5357360839844, 74.4434585571289], [495, 252]]', 'polygon', 4, '2025-05-08 18:31:04', '2025-05-09 15:28:01', '1', 'VOC', '2');
INSERT INTO `t_annotation` VALUES (3, 1, 9, '[[349.23809814453125, 134.09821701049805], [558, 334]]', 'polygon', 4, '2025-05-08 18:35:10', '2025-05-09 15:28:01', '1', 'VOC', '3');
INSERT INTO `t_annotation` VALUES (4, 1, 10, '[[218.2380981445312, 62.955360412597656], [512, 264]]', '', 4, '2025-05-08 20:45:20', '2025-05-09 15:28:01', '1', 'VOC', '4');
INSERT INTO `t_annotation` VALUES (5, 1, 11, '[[237.2380981445312, 102.95536041259766], [483, 282]]', 'rect', 4, '2025-05-08 20:57:23', '2025-05-09 15:28:01', '1', 'VOC', '5');
INSERT INTO `t_annotation` VALUES (7, 4, 13, '[[155.23809814453125, 294.955358505249], [450, 592]]', 'rect', 4, '2025-05-09 08:09:08', '2025-05-09 15:28:01', '1', 'VOC', '1');
INSERT INTO `t_annotation` VALUES (8, 1, 14, '[[221.23809814453122,89.95536804199219], [485,253]]', 'rect', 4, '2025-05-09 16:26:56', '2025-05-09 16:26:56', NULL, 'VOC', NULL);
INSERT INTO `t_annotation` VALUES (9, 1, 15, '[[257.23809814453125,64.95536804199219], [486,264]]', 'rect', 4, '2025-05-09 16:39:05', '2025-05-09 16:39:05', NULL, 'VOC', NULL);
INSERT INTO `t_annotation` VALUES (10, 1, 16, '[[860.792458813127,847.8680220026922],[866.828065307579,552.1233037745469],[845.7034425769972,244.30737255749773],[1041.8606536466853,96.43501344342508],[1244.0534712108256,105.488423185103],[1358.7299946054125,262.41419204085355],[1316.4807491442489,497.8028453244794],[1225.9466517274695,636.6217946968741],[1078.074292613397,781.4763505637208],[981.5045887021658,838.8146122610143],[860.792458813127,847.8680220026922]]', 'polygon', 4, '2025-05-09 16:39:19', '2025-05-09 16:39:19', NULL, 'VOC', NULL);
INSERT INTO `t_annotation` VALUES (11, 1, 17, '[[622.4434105228406,316.53192213024914],[952.0248325214188,608.1510860975775],[1285.0573688864743,690.9778308930318],[1450.7108584773828,452.85093960610084],[1340.2751987501103,212.99849113593137],[1262.6251255043721,202.6451480364996],[1262.6251255043721,200.91959085326098],[622.4434105228406,316.53192213024914]]', 'polygon', 4, '2025-05-09 16:40:20', '2025-05-09 16:40:20', NULL, 'VOC', NULL);

-- ----------------------------
-- Table structure for t_image
-- ----------------------------
DROP TABLE IF EXISTS `t_image`;
CREATE TABLE `t_image`  (
  `image_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '图像ID（主键）',
  `user_id` int UNSIGNED NOT NULL COMMENT '上传用户ID（外键）',
  `storage_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图像存储路径（如：/images/2024/05/abc.jpg）',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原始文件名（如：cat.jpg）',
  `file_size` int UNSIGNED NULL DEFAULT NULL COMMENT '文件大小（单位：字节）',
  `upload_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `annotated` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`image_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `t_image_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图像信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_image
-- ----------------------------
INSERT INTO `t_image` VALUES (1, 4, 'cat.jpg', 'cat.jpg', 123, '2025-05-05 20:34:07', 0);
INSERT INTO `t_image` VALUES (2, 4, 'bay.jpg', 'bay.jpg', 123, '2025-05-05 20:34:52', 0);
INSERT INTO `t_image` VALUES (4, 4, '6.jpg', '6.jpg', 22530, '2025-05-06 12:03:06', 0);
INSERT INTO `t_image` VALUES (7, 4, '1746522440971_logo.png', 'logo.png', 2064, '2025-05-06 17:07:21', 0);

-- ----------------------------
-- Table structure for t_label
-- ----------------------------
DROP TABLE IF EXISTS `t_label`;
CREATE TABLE `t_label`  (
  `label_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签ID（主键）',
  `label_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称（如“猫”、“车辆”）',
  `created_by` int UNSIGNED NOT NULL COMMENT '创建用户ID（外键，关联用户表）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `label_color` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#FF5733' COMMENT '标签颜色',
  `voc_class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'VOC格式中的类别名称',
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_label
-- ----------------------------
INSERT INTO `t_label` VALUES (1, 'cat', 4, '2022-07-06 13:27:27', '#334EFF', NULL);
INSERT INTO `t_label` VALUES (2, 'Nguyen', 5, '2003-06-06 05:50:01', '#FF5733', NULL);
INSERT INTO `t_label` VALUES (7, '猫', 4, '2025-05-08 18:19:30', '#FF5733', NULL);
INSERT INTO `t_label` VALUES (8, '猫2', 4, '2025-05-08 18:31:04', '#FF5733', NULL);
INSERT INTO `t_label` VALUES (9, 'mm', 4, '2025-05-08 18:35:10', '#0065F2', NULL);
INSERT INTO `t_label` VALUES (10, 'mmm', 4, '2025-05-08 20:45:21', '#FF5733', NULL);
INSERT INTO `t_label` VALUES (11, '123m', 4, '2025-05-08 20:57:23', '#5D514E', NULL);
INSERT INTO `t_label` VALUES (12, '果1', 4, '2025-05-09 04:27:47', '#33FF92', NULL);
INSERT INTO `t_label` VALUES (13, '123', 4, '2025-05-09 08:09:08', '#FF5733', NULL);
INSERT INTO `t_label` VALUES (14, '123', 4, '2025-05-09 16:26:56', '#FF5733', NULL);
INSERT INTO `t_label` VALUES (15, '123', 4, '2025-05-09 16:39:05', '#FF5733', NULL);
INSERT INTO `t_label` VALUES (16, '猫', 4, '2025-05-09 16:39:19', '#FF5733', NULL);
INSERT INTO `t_label` VALUES (17, '1111', 4, '2025-05-09 16:40:20', '#FF5733', NULL);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `user_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键）',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名（唯一）',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (4, '123', '123', '2025-05-01 02:43:48');
INSERT INTO `t_user` VALUES (5, '123123', '123123', '2025-05-03 16:26:38');

SET FOREIGN_KEY_CHECKS = 1;

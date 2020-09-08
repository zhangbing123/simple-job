/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : simple-job

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 06/09/2020 12:07:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_execute_job
-- ----------------------------
DROP TABLE IF EXISTS `t_execute_job`;
CREATE TABLE `t_execute_job` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `job_id` varchar(50) NOT NULL COMMENT '任务id',
  `status` varchar(10) NOT NULL COMMENT '执行状态：SUCESSFUL成功 FAIL 失败',
  `exception` varchar(1024) DEFAULT NULL COMMENT '执行失败原因',
  `excute_date` bigint(20) NOT NULL COMMENT '任务执行时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
   PRIMARY KEY (`id`),
   KEY `idx_job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

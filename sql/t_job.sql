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

 Date: 06/09/2020 22:14:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_job
-- ----------------------------
DROP TABLE IF EXISTS `t_job`;
CREATE TABLE `t_job` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '任务名称',
  `type` varchar(10) NOT NULL COMMENT '任务类型BEAN:springbean方式执行的任务   HTTP:http请求任务',
  `url` varchar(1024) DEFAULT NULL COMMENT 'type为Http时必填',
  `http_method` varchar(10) DEFAULT NULL COMMENT '请求方式 例如：GET POST',
  `bean_name` varchar(255) DEFAULT NULL COMMENT 'type为BEAN时必填',
  `method_name` varchar(255) DEFAULT NULL COMMENT 'type为Bean时指定执行的bean方法',
  `is_period` tinyint(1) NOT NULL COMMENT '是否周期任务 0否 1是',
  `period_time` bigint NOT NULL DEFAULT '0' COMMENT '周期时间 单位ms ',
  `time` bigint NOT NULL COMMENT '执行时间，周期任务时 表示多少ms后开始第一次执行 非周期任务表示时间戳',
  `args` varchar(1024) DEFAULT NULL COMMENT '执行任务的参数，仅支持字符串或json字符串',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务描述',
  `cron` varchar(50) DEFAULT NULL COMMENT 'cron表达式  可以使用cron表达式替代time和period_time',
  `status` varchar(10) NOT NULL DEFAULT 'RUNNING' COMMENT 'RUNNING：正常执行 启用状态 STOP：禁用',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除 0：否 1：是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_beanName` (`bean_name`),
  KEY `idx_methodName` (`method_name`),
  KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

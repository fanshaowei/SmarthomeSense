/*
Navicat MySQL Data Transfer

Source Server         : 10.30.30.30
Source Server Version : 50634
Source Host           : 10.30.30.30:3306
Source Database       : smarthome

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2017-08-30 09:17:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sense_device
-- ----------------------------
DROP TABLE IF EXISTS `sense_device`;
CREATE TABLE `sense_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_device` varchar(16) DEFAULT NULL,
  `name_device` varchar(30) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL COMMENT '设备是否在线',
  `type_device` varchar(30) DEFAULT NULL,
  `deviceParam` varchar(200) DEFAULT NULL,
  `id_gateway` varchar(16) DEFAULT NULL,
  `id_family` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_device` (`id_device`)
) ENGINE=InnoDB AUTO_INCREMENT=2993 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sense_device_scene_relate
-- ----------------------------
DROP TABLE IF EXISTS `sense_device_scene_relate`;
CREATE TABLE `sense_device_scene_relate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jobName` varchar(200) NOT NULL,
  `id_family` int(11) NOT NULL,
  `id_gateway` varchar(16) NOT NULL,
  `id_device` varchar(16) NOT NULL,
  `triggerSourceJson` varchar(200) DEFAULT NULL,
  `sceneJson` varchar(5000) NOT NULL,
  `isValid` varchar(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=194 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sense_device_state_log
-- ----------------------------
DROP TABLE IF EXISTS `sense_device_state_log`;
CREATE TABLE `sense_device_state_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_gateway` varchar(255) DEFAULT NULL,
  `id_device` varchar(255) DEFAULT NULL,
  `device_type` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34581 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sense_gateway
-- ----------------------------
DROP TABLE IF EXISTS `sense_gateway`;
CREATE TABLE `sense_gateway` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_gateway` varchar(16) DEFAULT NULL,
  `name_gateway` varchar(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `idFamily` int(11) DEFAULT NULL,
  `createTime` varchar(30) DEFAULT NULL,
  `creater` varchar(30) DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_gateway` (`id_gateway`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

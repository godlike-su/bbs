/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.39 : Database - bbs
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bbs` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `bbs`;

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `cat1` int(11) DEFAULT NULL COMMENT '1级板块',
  `cat2` int(11) DEFAULT NULL COMMENT '2级板块',
  `cat3` int(11) DEFAULT NULL,
  `ref1` bigint(20) DEFAULT NULL COMMENT '引文ID(楼主)',
  `ref2` bigint(20) DEFAULT NULL COMMENT '回复ID(第二楼主)',
  `refId` int(11) DEFAULT NULL COMMENT '回复楼的Id',
  `refstr` varchar(100) DEFAULT NULL COMMENT '回复楼的名字Name',
  `timeCreate` datetime DEFAULT NULL,
  `timeUpdate` datetime DEFAULT NULL,
  `niceFlag` int(4) DEFAULT NULL COMMENT '精华标识',
  `topFlag` int(4) DEFAULT NULL COMMENT '置顶标题',
  `banFlag` tinyint(1) DEFAULT NULL COMMENT 'reserved',
  `delFlag` tinyint(1) NOT NULL COMMENT '删除标识',
  `closeFlag` tinyint(1) DEFAULT NULL COMMENT '完结标识',
  `numReply` int(5) DEFAULT NULL COMMENT '回复数',
  `numLike` int(5) DEFAULT NULL COMMENT '点赞数',
  `storePath` varchar(200) DEFAULT NULL COMMENT '图片存储路径',
  `imgCount` int(2) DEFAULT NULL,
  `img1` varchar(100) DEFAULT NULL COMMENT '图片1',
  `img2` varchar(100) DEFAULT NULL COMMENT '图片2',
  `img3` varchar(100) DEFAULT NULL COMMENT '图片3',
  `replyUser` int(11) DEFAULT NULL COMMENT '最近一次回复',
  `replyName` varchar(50) DEFAULT NULL COMMENT '回复者的名称',
  `replyTime` datetime DEFAULT NULL,
  `replyText` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`,`delFlag`),
  KEY `topFlag` (`topFlag`),
  KEY `replyTime` (`replyTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `message` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `qqid` varchar(50) DEFAULT NULL COMMENT 'qq的openid',
  `qq` varchar(30) DEFAULT NULL,
  `qqFlag` tinyint(1) DEFAULT NULL,
  `qqName` varchar(100) DEFAULT NULL COMMENT '最初的QQ昵称',
  `email` varchar(100) DEFAULT NULL,
  `emailFlag` tinyint(1) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `phoneFlag` tinyint(1) DEFAULT NULL,
  `thumb` varchar(200) DEFAULT NULL COMMENT '头像',
  `experience` int(11) DEFAULT NULL COMMENT '经验',
  `level` int(11) DEFAULT NULL COMMENT '用户等级',
  `vip` tinyint(4) DEFAULT NULL COMMENT 'VIP等级',
  `vipName` varchar(30) DEFAULT NULL COMMENT '头衔',
  `isAdmin` tinyint(1) DEFAULT NULL COMMENT '是否超级管理',
  `timeCreate` datetime DEFAULT NULL COMMENT '注册时间',
  `timeUpdate` datetime DEFAULT NULL,
  `timeLogin` datetime DEFAULT NULL COMMENT '最后一次登录',
  PRIMARY KEY (`id`),
  UNIQUE KEY `qqid` (`qqid`),
  UNIQUE KEY `qq` (`qq`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`password`,`sex`,`qqid`,`qq`,`qqFlag`,`qqName`,`email`,`emailFlag`,`phone`,`phoneFlag`,`thumb`,`experience`,`level`,`vip`,`vipName`,`isAdmin`,`timeCreate`,`timeUpdate`,`timeLogin`) values (1,'admin','c4ca4238a0b923820dcc509a6f75849b',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,29,1,6,'尊享会员',1,NULL,NULL,NULL);

/*Table structure for table `user_ability` */

DROP TABLE IF EXISTS `user_ability`;

CREATE TABLE `user_ability` (
  `userId` int(11) NOT NULL COMMENT '用户ID',
  `banFlag` int(11) DEFAULT NULL COMMENT '临时禁言标识',
  `banDate` datetime DEFAULT NULL,
  `imageCount` int(11) DEFAULT NULL,
  `imageMax` int(11) DEFAULT NULL COMMENT '上传图片额度',
  `msgCount` int(11) DEFAULT NULL,
  `msgMax` int(11) DEFAULT NULL COMMENT '发帖的额度',
  `replyCount` int(11) DEFAULT NULL,
  `replyMax` int(11) DEFAULT NULL COMMENT '回复的额度',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_ability` */

insert  into `user_ability`(`userId`,`banFlag`,`banDate`,`imageCount`,`imageMax`,`msgCount`,`msgMax`,`replyCount`,`replyMax`) values (1,0,NULL,99999,99999,99999,99999,99999,99999);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

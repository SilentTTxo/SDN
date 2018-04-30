SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `T_NE_INFO`
-- ----------------------------
DROP TABLE IF EXISTS `T_NE_INFO`;
CREATE TABLE `T_NE_INFO` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `info` text,
  `ip` bigint(11) DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ipAddr` (`ip`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1365 DEFAULT CHARSET=utf8;
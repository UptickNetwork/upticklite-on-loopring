SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for asset
-- ----------------------------
DROP TABLE IF EXISTS `asset`;
CREATE TABLE `asset` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract_address` varchar(80) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `creator` varchar(255) DEFAULT NULL,
  `owner` varchar(60) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `nft_data` varchar(255) DEFAULT NULL,
  `nft_id` varchar(255) NOT NULL,
  `channel` varchar(255) DEFAULT NULL,
  `royalty_percentage` int(11) DEFAULT NULL,
  `original_royalty_percentage` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `sale_status` int(11) DEFAULT NULL,
  `list_amount` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `nft_type` varchar(255) DEFAULT NULL,
  `market_price` decimal(20,6) DEFAULT NULL,
  `market_time` timestamp NULL DEFAULT NULL,
  `market_owner` varchar(255) DEFAULT NULL,
  `market_token_type` varchar(255) DEFAULT NULL,
  `maker_order_id` bigint(20) DEFAULT NULL,
  `maker_type` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  `json` text,
  `metadata_url` varchar(255) DEFAULT NULL,
  `properties` text,
  `lincense_code` varchar(255) DEFAULT NULL,
  `asset_status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `owner` (`owner`) USING BTREE,
  KEY `nft_id` (`nft_id`) USING BTREE,
  KEY `level` (`level`) USING BTREE,
  KEY `address` (`contract_address`) USING BTREE,
  KEY `creator` (`creator`) USING BTREE,
  KEY `sale_status` (`sale_status`) USING BTREE,
  KEY `maker_order_id` (`maker_order_id`) USING BTREE,
  KEY `name` (`name`) USING BTREE,
  KEY `delete` (`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1269478 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for asset_extension
-- ----------------------------
DROP TABLE IF EXISTS `asset_extension`;
CREATE TABLE `asset_extension` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nft_id` varchar(255) DEFAULT NULL,
  `create_owner` varchar(255) DEFAULT NULL,
  `like_count` bigint(11) DEFAULT '0',
  `view_count` bigint(11) DEFAULT '0',
  `issuer_price` decimal(20,7) DEFAULT NULL,
  `eth_total_price` decimal(20,7) DEFAULT NULL,
  `lrc_total_price` decimal(20,7) DEFAULT NULL,
  `usdt_total_price` decimal(20,7) DEFAULT NULL,
  `dai_total_price` decimal(20,7) DEFAULT NULL,
  `usdc_total_price` decimal(20,7) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  `json` text,
  `total_inventory` bigint(20) DEFAULT NULL,
  `issuer_token_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `nft_id` (`nft_id`) USING BTREE,
  KEY `deleted` (`deleted`) USING BTREE,
  KEY `owner` (`create_owner`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8457 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for contract
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '',
  `address` varchar(100) NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `img_url` varchar(50) DEFAULT '',
  `owner` varchar(50) NOT NULL DEFAULT '',
  `collection_metadata` varchar(255) DEFAULT NULL,
  `base_url` varchar(255) DEFAULT NULL,
  `amount` int(20) DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT '100',
  `json` varchar(1000) DEFAULT '',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `link_url` varchar(255) DEFAULT NULL,
  `recommend_start_time` timestamp NULL DEFAULT NULL,
  `recommend_stop_time` timestamp NULL DEFAULT NULL,
  `recommend_disabled` bit(1) DEFAULT NULL,
  `contract_status` int(2) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `link_url` (`link_url`) USING BTREE,
  KEY `address` (`address`) USING BTREE,
  KEY `weight` (`weight`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3361 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for core_menu
-- ----------------------------
DROP TABLE IF EXISTS `core_menu`;
CREATE TABLE `core_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `url` varchar(2550) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `menutype` enum('0','1','2') DEFAULT '0',
  `display` int(11) DEFAULT '1',
  `parentid` int(48) DEFAULT '0',
  `creator` int(11) DEFAULT '0',
  `weight` int(11) DEFAULT '0',
  `version` int(11) DEFAULT '0',
  `createtime` timestamp NULL DEFAULT NULL,
  `updateuser` int(11) DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT NULL,
  `flag` enum('0','1') DEFAULT '1',
  `permission` varchar(255) DEFAULT NULL,
  `json` varchar(255) DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for core_role
-- ----------------------------
DROP TABLE IF EXISTS `core_role`;
CREATE TABLE `core_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `create_user` bigint(20) DEFAULT '0',
  `description` varchar(200) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for core_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `core_role_menu`;
CREATE TABLE `core_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  `flag` enum('1','0') DEFAULT '1',
  `create_user` bigint(20) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`,`role_id`) USING BTREE,
  UNIQUE KEY `role_id_menu_id` (`role_id`,`menu_id`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=317 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for core_user
-- ----------------------------
DROP TABLE IF EXISTS `core_user`;
CREATE TABLE `core_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `password` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `flag` enum('1','0') DEFAULT '1',
  `login_time` timestamp NULL DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for core_user_role
-- ----------------------------
DROP TABLE IF EXISTS `core_user_role`;
CREATE TABLE `core_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `create_user` bigint(20) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  UNIQUE KEY `user_id_role_id_unique` (`user_id`,`role_id`) USING BTREE,
  KEY `id` (`id`) USING BTREE,
  KEY `user_id_role_id` (`user_id`,`role_id`) USING BTREE,
  KEY `FK_core_user_role_core_role` (`role_id`) USING BTREE,
  CONSTRAINT `core_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `core_role` (`id`),
  CONSTRAINT `core_user_role_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `core_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_map
-- ----------------------------
DROP TABLE IF EXISTS `data_map`;
CREATE TABLE `data_map` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(10000) DEFAULT NULL,
  `json` text,
  `description` text,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of data_map
-- ----------------------------
BEGIN;
INSERT INTO `data_map` VALUES (1, 'BLOCK_ID', '7410', NULL, NULL, '2022-05-13 17:07:38', '2022-10-21 14:01:04', b'0', NULL);
INSERT INTO `data_map` VALUES (2, 'SCORE_BY_USDT', '2', NULL, NULL, NULL, NULL, b'0', NULL);
INSERT INTO `data_map` VALUES (3, 'SCORE_BY_USDC', '2', NULL, NULL, NULL, NULL, b'0', NULL);
INSERT INTO `data_map` VALUES (4, 'SCORE_BY_ETH', '2', NULL, NULL, NULL, NULL, b'0', NULL);
INSERT INTO `data_map` VALUES (5, 'SCORE_BY_LRC', '2', NULL, NULL, NULL, NULL, b'0', NULL);
INSERT INTO `data_map` VALUES (6, 'SCORE_BY_DIA', '2', NULL, NULL, NULL, NULL, b'0', NULL);
INSERT INTO `data_map` VALUES (7, 'loyalty', '5', NULL, 'Royalty', '2022-07-21 10:10:12', NULL, b'0', NULL);
INSERT INTO `data_map` VALUES (8, 'uptickServiceFee', '8', NULL, 'Platform fee', '2022-07-21 10:10:28', NULL, b'0', NULL);
COMMIT;

-- ----------------------------
-- Table structure for maker_order
-- ----------------------------
DROP TABLE IF EXISTS `maker_order`;
CREATE TABLE `maker_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `valid_until` bigint(20) DEFAULT NULL,
  `maker_json` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `signature` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `contract_address` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `nft_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `signer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `sale_limit` int(11) DEFAULT '0',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `json` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `lock_asset_hash` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4173 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for order_details
-- ----------------------------
DROP TABLE IF EXISTS `order_details`;
CREATE TABLE `order_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(60) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `pay_price` decimal(20,6) DEFAULT NULL,
  `token_type` varchar(255) DEFAULT NULL,
  `order_status` int(11) DEFAULT NULL,
  `sender` varchar(60) DEFAULT NULL,
  `recipient` varchar(60) DEFAULT NULL,
  `tx_id` varchar(100) DEFAULT NULL,
  `contract_address` varchar(255) DEFAULT NULL,
  `asset_id` bigint(20) DEFAULT NULL,
  `nft_id` varchar(255) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  `json` text,
  `order_market_type` int(11) DEFAULT NULL,
  `royalty_percentage` decimal(20,6) DEFAULT NULL,
  `original_royalty_percentage` decimal(20,6) DEFAULT NULL,
  `platform_fee` decimal(20,6) DEFAULT NULL,
  `commission_charge` decimal(20,6) DEFAULT NULL,
  `reconciliation_loopring` bit(1) DEFAULT b'0',
  `reconciliation_loopring_time` timestamp NULL DEFAULT NULL,
  `reconciliation_creator` bit(1) DEFAULT b'0',
  `reconciliation_creator_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FKy5umkyx22348bvsyv1h27thf` (`asset_id`) USING BTREE,
  KEY `tx_id` (`tx_id`) USING BTREE,
  KEY `UK_jg8u2tfysi4cvnm45ql5ie707` (`order_no`) USING BTREE,
  KEY `owner` (`sender`) USING BTREE,
  KEY `to_owner` (`recipient`) USING BTREE,
  KEY `nft_id` (`nft_id`) USING BTREE,
  KEY `level` (`level`) USING BTREE,
  KEY `delete` (`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=553 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(60) DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `nft_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `nft_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `order_price` decimal(19,4) DEFAULT NULL,
  `token_type` varchar(255) DEFAULT NULL,
  `quantity` int(11) DEFAULT '1',
  `level` int(11) DEFAULT '1',
  `order_status` int(11) DEFAULT NULL,
  `sender` varchar(60) DEFAULT NULL,
  `recipient` varchar(60) DEFAULT NULL,
  `contract_address` varchar(255) DEFAULT NULL,
  `tx_id` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  `json` text,
  `nft_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK_jg8u2tfysi4cvnm45ql5ie707` (`order_no`) USING BTREE,
  KEY `tx_id` (`tx_id`) USING BTREE,
  KEY `owner` (`sender`) USING BTREE,
  KEY `to_owner` (`recipient`) USING BTREE,
  KEY `nft_id` (`nft_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=598 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for taker_order
-- ----------------------------
DROP TABLE IF EXISTS `taker_order`;
CREATE TABLE `taker_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `maker_order_id` bigint(20) DEFAULT NULL,
  `valid_until` bigint(20) DEFAULT NULL,
  `maker_json` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL,
  `signature` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `contract_address` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `nft_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `signer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `token_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `token_amount` decimal(20,6) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `json` text COLLATE utf8mb4_bin,
  `weight` int(11) DEFAULT NULL,
  `lock_hash` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=298 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for transfer_log
-- ----------------------------
DROP TABLE IF EXISTS `transfer_log`;
CREATE TABLE `transfer_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract_address` varchar(255) DEFAULT NULL,
  `nft_id` varchar(255) DEFAULT NULL,
  `asset_id` bigint(20) DEFAULT NULL,
  `tx_id` varchar(255) DEFAULT NULL,
  `from` varchar(255) DEFAULT NULL,
  `to` varchar(255) DEFAULT NULL,
  `amount` decimal(20,6) DEFAULT NULL,
  `tx_type` int(11) DEFAULT NULL,
  `serial_no` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `weight` int(11) DEFAULT NULL,
  `json` text CHARACTER SET utf8mb4,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11191 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  `json` text CHARACTER SET utf8,
  `email` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `domain` varchar(100) DEFAULT NULL,
  `description` text,
  `profile_photo` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `background` varchar(255) DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 NOT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `account_status` int(11) NOT NULL,
  `total_score` decimal(20,2) DEFAULT NULL,
  `current_score` decimal(20,2) DEFAULT NULL,
  `grade` int(11) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`) USING BTREE,
  KEY `user_addrss` (`address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38633 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

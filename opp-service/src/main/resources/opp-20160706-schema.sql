CREATE DATABASE  IF NOT EXISTS `opp` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `opp`;
-- MySQL dump 10.13  Distrib 5.6.19, for osx10.7 (i386)
--
-- Host: secret    Database: opp
-- ------------------------------------------------------
-- Server version	5.6.29-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `api_logs`
--

DROP TABLE IF EXISTS `api_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uri` varchar(255) NOT NULL,
  `method` varchar(6) NOT NULL,
  `params` text,
  `api_key` varchar(40) NOT NULL,
  `ip_address` varchar(45) NOT NULL,
  `time` int(11) NOT NULL,
  `rtime` float DEFAULT NULL,
  `authorized` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=129366 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(50) NOT NULL,
  `team_name` varchar(45) DEFAULT NULL,
  `newrelic` varchar(45) DEFAULT NULL,
  `appdynamics` varchar(45) DEFAULT NULL,
  `webpagetest` varchar(45) DEFAULT NULL,
  `code_coverage_id` varchar(45) DEFAULT NULL,
  `security_id` varchar(45) DEFAULT NULL,
  `regression_results_id` varchar(45) DEFAULT NULL,
  `kqi_app_name` varchar(45) DEFAULT NULL,
  `splunk` varchar(45) DEFAULT NULL,
  `dynatrace` varchar(45) DEFAULT NULL,
  `is_client_side` bit(1) DEFAULT b'0',
  `is_server_side` bit(1) DEFAULT b'0',
  `in_cd_pipeline_client` bit(1) DEFAULT b'0',
  `in_cd_pipeline_server` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_key_UNIQUE` (`app_key`),
  KEY `fk_application_application_team_team_name_idx` (`team_name`),
  CONSTRAINT `fk_application_application_team_team_name` FOREIGN KEY (`team_name`) REFERENCES `application_team` (`team_name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=234 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `application_team`
--

DROP TABLE IF EXISTS `application_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_team` (
  `team_name` varchar(50) NOT NULL,
  `owner` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`team_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `graphite_metric`
--

DROP TABLE IF EXISTS `graphite_metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `graphite_metric` (
  `graphite_id` int(11) NOT NULL AUTO_INCREMENT,
  `application_id` int(11) NOT NULL,
  `app_key` varchar(45) NOT NULL,
  `name` varchar(100) NOT NULL,
  `graphite_path` varchar(400) NOT NULL,
  PRIMARY KEY (`graphite_id`),
  KEY `application_id` (`application_id`),
  KEY `app_key` (`app_key`),
  CONSTRAINT `fk__graphite_metric_application_id` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk__graphite_metric_application_key` FOREIGN KEY (`app_key`) REFERENCES `application` (`app_key`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `load_sla`
--

DROP TABLE IF EXISTS `load_sla`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `load_sla` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `custom_name` varchar(200) DEFAULT NULL,
  `custom_value` float DEFAULT NULL,
  `load_sla_group_id` int(11) NOT NULL,
  `margin_of_error` float DEFAULT NULL,
  `max` int(11) DEFAULT NULL,
  `median` int(11) DEFAULT NULL,
  `min` int(11) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `pct75` int(11) DEFAULT NULL,
  `pct90` int(11) DEFAULT NULL,
  `avg` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_load_sla_group_id_load_sla_idx` (`load_sla_group_id`),
  CONSTRAINT `fk_load_sla_group_id_load_sla` FOREIGN KEY (`load_sla_group_id`) REFERENCES `load_sla_group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=228 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `load_sla_group`
--

DROP TABLE IF EXISTS `load_sla_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `load_sla_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `load_sla_test_group`
--

DROP TABLE IF EXISTS `load_sla_test_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `load_sla_test_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creation_date` datetime DEFAULT NULL,
  `is_active` tinyint(4) DEFAULT NULL,
  `load_sla_group_id` int(11) NOT NULL,
  `load_test_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC0BB38825BB7E56B` (`load_sla_group_id`),
  KEY `FKC0BB38822CDF330A` (`load_test_id`),
  CONSTRAINT `fk_load_sla_test_group_load_sla_group_id` FOREIGN KEY (`load_sla_group_id`) REFERENCES `load_sla_group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_load_sla_test_group_load_test_id` FOREIGN KEY (`load_test_id`) REFERENCES `load_test` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=930 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `load_test`
--

DROP TABLE IF EXISTS `load_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `load_test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_under_test` varchar(255) DEFAULT NULL,
  `app_under_test_version` varchar(255) DEFAULT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  `description` varchar(2000) NOT NULL,
  `environment` varchar(255) NOT NULL,
  `start_time` bigint(20) NOT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `test_name` varchar(255) NOT NULL,
  `test_sub_name` varchar(255) DEFAULT NULL,
  `test_tool` varchar(255) NOT NULL,
  `test_tool_version` varchar(255) DEFAULT NULL,
  `vuser_count` int(11) NOT NULL,
  `sla_group_id` int(11) DEFAULT NULL,
  `external_test_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4963 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `load_test_aggregate`
--

DROP TABLE IF EXISTS `load_test_aggregate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `load_test_aggregate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `call_count` int(11) NOT NULL,
  `load_test_id` int(11) NOT NULL,
  `resp_avg` int(11) NOT NULL,
  `resp_max` int(11) NOT NULL,
  `resp_median` int(11) NOT NULL,
  `resp_min` int(11) NOT NULL,
  `resp_pct75` int(11) NOT NULL,
  `resp_pct90` int(11) NOT NULL,
  `resp_stddev` float NOT NULL,
  `total_bytes_received` bigint(20) NOT NULL,
  `total_bytes_sent` bigint(20) NOT NULL,
  `tps_median` float(10,5) NOT NULL DEFAULT '0.00000',
  `tps_max` float(10,5) NOT NULL DEFAULT '0.00000',
  `error_count` int(11) NOT NULL DEFAULT '0',
  `transaction_name` varchar(255) NOT NULL,
  `custom_name1` varchar(255) DEFAULT NULL,
  `custom_name2` varchar(255) DEFAULT NULL,
  `custom_name3` varchar(255) DEFAULT NULL,
  `custom_value1` float DEFAULT NULL,
  `custom_value2` float DEFAULT NULL,
  `custom_value3` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1537AECB99D0AC4E` (`load_test_id`),
  CONSTRAINT `fk_load_test_aggregate_load_test_id` FOREIGN KEY (`load_test_id`) REFERENCES `load_test` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=85846 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `load_test_application_coverage`
--

DROP TABLE IF EXISTS `load_test_application_coverage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `load_test_application_coverage` (
  `application_id` int(11) NOT NULL,
  `load_test_name` varchar(100) NOT NULL,
  PRIMARY KEY (`application_id`,`load_test_name`),
  CONSTRAINT `fk_lt_app_coverage_application_id` FOREIGN KEY (`application_id`) REFERENCES `application` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `load_test_data`
--

DROP TABLE IF EXISTS `load_test_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `load_test_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bytes_received` int(11) DEFAULT NULL,
  `bytes_sent` int(11) DEFAULT NULL,
  `connection_time` int(11) DEFAULT NULL,
  `dns_lookup` int(11) DEFAULT NULL,
  `error_count` int(11) DEFAULT NULL,
  `load_test_id` int(11) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `operation` varchar(255) DEFAULT NULL,
  `receive_time` int(11) DEFAULT NULL,
  `response_time` int(11) NOT NULL,
  `send_time` int(11) DEFAULT NULL,
  `server` varchar(255) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `target` varchar(255) DEFAULT NULL,
  `transaction_name` varchar(255) NOT NULL,
  `ttfb` int(11) DEFAULT NULL,
  `ttlb` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_load_test_data_load_test_id_idx` (`load_test_id`),
  CONSTRAINT `fk_load_test_data_load_test_id` FOREIGN KEY (`load_test_id`) REFERENCES `load_test` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=54396468 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `load_test_sla_group`
--

DROP TABLE IF EXISTS `load_test_sla_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `load_test_sla_group` (
  `load_sla_group_id` int(11) NOT NULL,
  `load_test_id` int(11) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `is_active` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`load_sla_group_id`,`load_test_id`),
  KEY `FK_loadtest_loadtestslagroup_idx` (`load_test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `load_test_summary_trend`
--

DROP TABLE IF EXISTS `load_test_summary_trend`;
/*!50001 DROP VIEW IF EXISTS `load_test_summary_trend`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `load_test_summary_trend` AS SELECT 
 1 AS `load_test_id`,
 1 AS `app_under_test`,
 1 AS `app_under_test_version`,
 1 AS `comments`,
 1 AS `description`,
 1 AS `environment`,
 1 AS `start_time`,
 1 AS `end_time`,
 1 AS `test_name`,
 1 AS `test_sub_name`,
 1 AS `test_tool`,
 1 AS `test_tool_version`,
 1 AS `vuser_count`,
 1 AS `external_test_id`,
 1 AS `transaction_name`,
 1 AS `total_call_count`,
 1 AS `error_count`,
 1 AS `resp_avg`,
 1 AS `resp_median`,
 1 AS `resp_pct90`,
 1 AS `total_bytes`,
 1 AS `tps_median`,
 1 AS `tps_max`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `load_test_tps`
--

DROP TABLE IF EXISTS `load_test_tps`;
/*!50001 DROP VIEW IF EXISTS `load_test_tps`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `load_test_tps` AS SELECT 
 1 AS `load_test_id`,
 1 AS `requests`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `load_test_with_aggregate`
--

DROP TABLE IF EXISTS `load_test_with_aggregate`;
/*!50001 DROP VIEW IF EXISTS `load_test_with_aggregate`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `load_test_with_aggregate` AS SELECT 
 1 AS `load_test_id`,
 1 AS `app_under_test`,
 1 AS `app_under_test_version`,
 1 AS `comments`,
 1 AS `description`,
 1 AS `environment`,
 1 AS `start_time`,
 1 AS `end_time`,
 1 AS `test_name`,
 1 AS `test_sub_name`,
 1 AS `test_tool`,
 1 AS `test_tool_version`,
 1 AS `vuser_count`,
 1 AS `external_test_id`,
 1 AS `transaction_name`,
 1 AS `call_count`,
 1 AS `resp_avg`,
 1 AS `resp_max`,
 1 AS `resp_min`,
 1 AS `resp_median`,
 1 AS `resp_pct75`,
 1 AS `resp_pct90`,
 1 AS `resp_stddev`,
 1 AS `total_bytes_received`,
 1 AS `total_bytes_sent`,
 1 AS `tps_median`,
 1 AS `tps_max`,
 1 AS `error_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'opp'
--

--
-- Final view structure for view `load_test_summary_trend`
--

/*!50001 DROP VIEW IF EXISTS `load_test_summary_trend`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`oppuser`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `load_test_summary_trend` AS select `lt`.`id` AS `load_test_id`,`lt`.`app_under_test` AS `app_under_test`,`lt`.`app_under_test_version` AS `app_under_test_version`,`lt`.`comments` AS `comments`,`lt`.`description` AS `description`,`lt`.`environment` AS `environment`,`lt`.`start_time` AS `start_time`,`lt`.`end_time` AS `end_time`,`lt`.`test_name` AS `test_name`,`lt`.`test_sub_name` AS `test_sub_name`,`lt`.`test_tool` AS `test_tool`,`lt`.`test_tool_version` AS `test_tool_version`,`lt`.`vuser_count` AS `vuser_count`,`lt`.`external_test_id` AS `external_test_id`,`lta`.`transaction_name` AS `transaction_name`,sum(`lta`.`call_count`) AS `total_call_count`,sum(`lta`.`error_count`) AS `error_count`,round(avg(`lta`.`resp_avg`),0) AS `resp_avg`,round(avg(`lta`.`resp_median`),0) AS `resp_median`,round(avg(`lta`.`resp_pct90`),0) AS `resp_pct90`,(sum(`lta`.`total_bytes_received`) + sum(`lta`.`total_bytes_sent`)) AS `total_bytes`,round(sum(`lta`.`tps_median`),0) AS `tps_median`,round(sum(`lta`.`tps_max`),0) AS `tps_max` from (`load_test` `lt` join `load_test_aggregate` `lta`) where (`lt`.`id` = `lta`.`load_test_id`) group by `lt`.`id` order by `lt`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `load_test_tps`
--

/*!50001 DROP VIEW IF EXISTS `load_test_tps`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`oppuser`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `load_test_tps` AS select `load_test_data`.`load_test_id` AS `load_test_id`,(count(0) / 60) AS `requests` from `load_test_data` group by `load_test_data`.`load_test_id`,date_format(from_unixtime((`load_test_data`.`start_time` / 1000)),'%H:%i') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `load_test_with_aggregate`
--

/*!50001 DROP VIEW IF EXISTS `load_test_with_aggregate`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`oppuser`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `load_test_with_aggregate` AS select `lt`.`id` AS `load_test_id`,`lt`.`app_under_test` AS `app_under_test`,`lt`.`app_under_test_version` AS `app_under_test_version`,`lt`.`comments` AS `comments`,`lt`.`description` AS `description`,`lt`.`environment` AS `environment`,`lt`.`start_time` AS `start_time`,`lt`.`end_time` AS `end_time`,`lt`.`test_name` AS `test_name`,`lt`.`test_sub_name` AS `test_sub_name`,`lt`.`test_tool` AS `test_tool`,`lt`.`test_tool_version` AS `test_tool_version`,`lt`.`vuser_count` AS `vuser_count`,`lt`.`external_test_id` AS `external_test_id`,`lta`.`transaction_name` AS `transaction_name`,`lta`.`call_count` AS `call_count`,`lta`.`resp_avg` AS `resp_avg`,`lta`.`resp_max` AS `resp_max`,`lta`.`resp_min` AS `resp_min`,`lta`.`resp_median` AS `resp_median`,`lta`.`resp_pct75` AS `resp_pct75`,`lta`.`resp_pct90` AS `resp_pct90`,`lta`.`resp_stddev` AS `resp_stddev`,`lta`.`total_bytes_received` AS `total_bytes_received`,`lta`.`total_bytes_sent` AS `total_bytes_sent`,`lta`.`tps_median` AS `tps_median`,`lta`.`tps_max` AS `tps_max`,`lta`.`error_count` AS `error_count` from (`load_test_aggregate` `lta` left join `load_test` `lt` on((`lta`.`load_test_id` = `lt`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-06 15:47:37

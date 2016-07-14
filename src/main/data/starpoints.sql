-- MySQL dump 10.13  Distrib 5.7.12, for Linux (x86_64)
--
-- Host: localhost    Database: starpointsapp
-- ------------------------------------------------------
-- Server version	5.7.12

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
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,'Article court','BLOG_POST','Article publié sur le site intranet de Softeam',1),(2,'Article long','BLOG_POST','Article publié sur le site intranet de Softeam',3),(3,'Présentation d\'un 12@13','BROWN_BAG_LUNCH','Livraison du support',3),(4,'Formation','TRAINING','Livraison du support',4);
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `community`
--

LOCK TABLES `community` WRITE;
/*!40000 ALTER TABLE `community` DISABLE KEYS */;
INSERT INTO `community` VALUES (1,'Startech Java',2),(2,'Startech Agile',NULL),(3,'Startech .NET',NULL);
/*!40000 ALTER TABLE `community` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `community_members`
--

LOCK TABLES `community_members` WRITE;
/*!40000 ALTER TABLE `community_members` DISABLE KEYS */;
INSERT INTO `community_members` VALUES (1,1),(1,2),(2,2);
/*!40000 ALTER TABLE `community_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `contribution`
--

LOCK TABLES `contribution` WRITE;
/*!40000 ALTER TABLE `contribution` DISABLE KEYS */;
INSERT INTO `contribution` VALUES (1,'2016-07-28','','Support présentation 12@13 Intellij',NULL,NULL,NULL,3,1);
/*!40000 ALTER TABLE `contribution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `databasechangelog`
--

LOCK TABLES `databasechangelog` WRITE;
/*!40000 ALTER TABLE `databasechangelog` DISABLE KEYS */;
INSERT INTO `databasechangelog` VALUES ('00000000000001','jhipster','classpath:config/liquibase/changelog/00000000000000_initial_schema.xml','2016-07-12 21:15:46',1,'EXECUTED','7:fb53c2c4cdc65104a8cbae8b42549555','createTable, createIndex (x2), createTable (x2), addPrimaryKey, createTable, addForeignKeyConstraint (x3), loadData, dropDefaultValue, loadData (x2), createTable (x2), addPrimaryKey, createIndex (x2), addForeignKeyConstraint','',NULL,'3.4.2',NULL,NULL),('20160620202523-1','jhipster','classpath:config/liquibase/changelog/20160620202523_added_entity_Level.xml','2016-07-12 21:15:46',2,'EXECUTED','7:71441d0eb4c412765d1455e30bee8441','createTable','',NULL,'3.4.2',NULL,NULL),('20160620202522-1','jhipster','classpath:config/liquibase/changelog/20160620202522_added_entity_Activity.xml','2016-07-12 21:15:46',3,'EXECUTED','7:297e0176f5a274b6ed1402c22b8c997f','createTable','',NULL,'3.4.2',NULL,NULL),('20160620202524-1','jhipster','classpath:config/liquibase/changelog/20160620202524_added_entity_Community.xml','2016-07-12 21:15:46',4,'EXECUTED','7:49ff42647397f6d60871686e9b8460db','createTable (x2), addPrimaryKey','',NULL,'3.4.2',NULL,NULL),('20160620202525-1','jhipster','classpath:config/liquibase/changelog/20160620202525_added_entity_Contribution.xml','2016-07-12 21:15:46',5,'EXECUTED','7:647a868f769975fe01a273fb79b347a2','createTable','',NULL,'3.4.2',NULL,NULL),('20160622210114-1','jhipster','classpath:config/liquibase/changelog/20160622210114_added_entity_Person.xml','2016-07-12 21:15:46',6,'EXECUTED','7:c2b41d3f8a58467c8eacabda8f4fd7c7','createTable','',NULL,'3.4.2',NULL,NULL),('20160620202521-1','jhipster','classpath:config/liquibase/changelog/20160620202521_added_entity_Scale.xml','2016-07-12 21:15:46',7,'EXECUTED','7:8749034de9f64af80f16857fe91ab815','createTable','',NULL,'3.4.2',NULL,NULL),('20160620202522-2','jhipster','classpath:config/liquibase/changelog/20160620202522_added_entity_constraints_Activity.xml','2016-07-12 21:15:46',8,'EXECUTED','7:d4c11b5e0181c74c4d6c3dfa4a64584f','addForeignKeyConstraint','',NULL,'3.4.2',NULL,NULL),('20160620202524-2','jhipster','classpath:config/liquibase/changelog/20160620202524_added_entity_constraints_Community.xml','2016-07-12 21:15:46',9,'EXECUTED','7:18655fda85a3c696f00e25bc1a36ebe7','addForeignKeyConstraint (x3)','',NULL,'3.4.2',NULL,NULL),('20160620202525-2','jhipster','classpath:config/liquibase/changelog/20160620202525_added_entity_constraints_Contribution.xml','2016-07-12 21:15:46',10,'EXECUTED','7:296bee4a0b5dec57f292ea4b426edb81','addForeignKeyConstraint (x2)','',NULL,'3.4.2',NULL,NULL),('20160622210114-2','jhipster','classpath:config/liquibase/changelog/20160622210114_added_entity_constraints_Person.xml','2016-07-12 21:15:46',11,'EXECUTED','7:d41d8cd98f00b204e9800998ecf8427e','Empty','',NULL,'3.4.2',NULL,NULL),('20160620202521-2','jhipster','classpath:config/liquibase/changelog/20160620202521_added_entity_constraints_Scale.xml','2016-07-12 21:15:46',12,'EXECUTED','7:4c713ec7c39bd5ede966a51d48e4846f','addForeignKeyConstraint','',NULL,'3.4.2',NULL,NULL);
/*!40000 ALTER TABLE `databasechangelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `databasechangeloglock`
--

LOCK TABLES `databasechangeloglock` WRITE;
/*!40000 ALTER TABLE `databasechangeloglock` DISABLE KEYS */;
INSERT INTO `databasechangeloglock` VALUES (1,'\0',NULL,NULL);
/*!40000 ALTER TABLE `databasechangeloglock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_authority`
--

LOCK TABLES `jhi_authority` WRITE;
/*!40000 ALTER TABLE `jhi_authority` DISABLE KEYS */;
INSERT INTO `jhi_authority` VALUES ('ROLE_ADMIN'),('ROLE_USER');
/*!40000 ALTER TABLE `jhi_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_persistent_audit_event`
--

LOCK TABLES `jhi_persistent_audit_event` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_event` DISABLE KEYS */;
INSERT INTO `jhi_persistent_audit_event` VALUES (1,'admin','2016-07-12 21:43:38','AUTHENTICATION_SUCCESS');
/*!40000 ALTER TABLE `jhi_persistent_audit_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_persistent_audit_evt_data`
--

LOCK TABLES `jhi_persistent_audit_evt_data` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` DISABLE KEYS */;
INSERT INTO `jhi_persistent_audit_evt_data` VALUES (1,'remoteAddress','172.18.0.1'),(1,'sessionId','D8ABA4573678DECDF6F9EE8F33865527');
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_persistent_token`
--

LOCK TABLES `jhi_persistent_token` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_token` DISABLE KEYS */;
INSERT INTO `jhi_persistent_token` VALUES ('sTEG46S0xB7Kxvw0S8A2aQ==',3,'JpC9lxj36FTDMvT7UJUFqQ==','2016-07-12','172.18.0.1','Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36');
/*!40000 ALTER TABLE `jhi_persistent_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_user`
--

LOCK TABLES `jhi_user` WRITE;
/*!40000 ALTER TABLE `jhi_user` DISABLE KEYS */;
INSERT INTO `jhi_user` VALUES (1,'system','$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG','System','System','system@localhost','','fr',NULL,NULL,'system','2016-07-12 21:15:46',NULL,NULL,NULL),(2,'anonymoususer','$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO','Anonymous','User','anonymous@localhost','','fr',NULL,NULL,'system','2016-07-12 21:15:46',NULL,NULL,NULL),(3,'admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost','','fr',NULL,NULL,'system','2016-07-12 21:15:46',NULL,NULL,NULL),(4,'user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','User','User','user@localhost','','fr',NULL,NULL,'system','2016-07-12 21:15:46',NULL,NULL,NULL);
/*!40000 ALTER TABLE `jhi_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jhi_user_authority`
--

LOCK TABLES `jhi_user_authority` WRITE;
/*!40000 ALTER TABLE `jhi_user_authority` DISABLE KEYS */;
INSERT INTO `jhi_user_authority` VALUES (1,'ROLE_ADMIN'),(3,'ROLE_ADMIN'),(1,'ROLE_USER'),(3,'ROLE_USER'),(4,'ROLE_USER');
/*!40000 ALTER TABLE `jhi_user_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` VALUES (1,'1','Article de blog N1'),(2,'2','Article de blog N2'),(3,'3','Article de blog N3'),(4,'4','Article pour le blog externe de la société');
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'bgiegel@gmail.com','Bastien','Giegel','2008-02-01'),(2,'ardemius@gmail.com','Thomas','Schwender','2003-02-13');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `scale`
--

LOCK TABLES `scale` WRITE;
/*!40000 ALTER TABLE `scale` DISABLE KEYS */;
INSERT INTO `scale` VALUES (1,'2016-07-04','2018-07-04','10',1),(2,'2016-07-04','2018-07-04','50',2),(3,'2016-07-04','2018-07-04','150',3),(4,'2016-07-04','2018-07-04','300',4);
/*!40000 ALTER TABLE `scale` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-13  0:03:33

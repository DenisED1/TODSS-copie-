-- MySQL dump 10.13  Distrib 5.7.19, for Linux (x86_64)
--
-- Host: localhost    Database: spellingsoftware
-- ------------------------------------------------------
-- Server version	5.7.19-0ubuntu0.16.04.1

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
-- Table structure for table `AnalyzeSpellingAntwoord`
--

DROP TABLE IF EXISTS `AnalyzeSpellingAntwoord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AnalyzeSpellingAntwoord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `antwoordDefinitief` varchar(255) DEFAULT NULL,
  `antwoordVersie1` varchar(255) DEFAULT NULL,
  `vraag` varchar(255) DEFAULT NULL,
  `regel_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6xfwy6hv1a9t1ov4362dvctlh` (`regel_id`),
  CONSTRAINT `FK6xfwy6hv1a9t1ov4362dvctlh` FOREIGN KEY (`regel_id`) REFERENCES `AnalyzeSpellingOpdracht` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AnalyzeSpellingAntwoord`
--

LOCK TABLES `AnalyzeSpellingAntwoord` WRITE;
/*!40000 ALTER TABLE `AnalyzeSpellingAntwoord` DISABLE KEYS */;
/*!40000 ALTER TABLE `AnalyzeSpellingAntwoord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AnalyzeSpellingOpdracht`
--

DROP TABLE IF EXISTS `AnalyzeSpellingOpdracht`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AnalyzeSpellingOpdracht` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `beginTijd` tinyblob,
  `eindTijd` tinyblob,
  `leerling_id` int(11) DEFAULT NULL,
  `spellingRegel_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjq17dt9n8yu8dlnmwsb9vxmqk` (`leerling_id`),
  KEY `FKk8yot7ekf6fik7iu8u9nx1l6w` (`spellingRegel_id`),
  CONSTRAINT `FKjq17dt9n8yu8dlnmwsb9vxmqk` FOREIGN KEY (`leerling_id`) REFERENCES `Gebruiker` (`id`),
  CONSTRAINT `FKk8yot7ekf6fik7iu8u9nx1l6w` FOREIGN KEY (`spellingRegel_id`) REFERENCES `SpellingRegel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AnalyzeSpellingOpdracht`
--

LOCK TABLES `AnalyzeSpellingOpdracht` WRITE;
/*!40000 ALTER TABLE `AnalyzeSpellingOpdracht` DISABLE KEYS */;
/*!40000 ALTER TABLE `AnalyzeSpellingOpdracht` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Categorie`
--

DROP TABLE IF EXISTS `Categorie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Categorie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `naam` varchar(255) NOT NULL,
  `spellingRegel_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh2gc8lqrrveax6gplkhl46207` (`spellingRegel_id`),
  CONSTRAINT `FKh2gc8lqrrveax6gplkhl46207` FOREIGN KEY (`spellingRegel_id`) REFERENCES `SpellingRegel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Categorie`
--

LOCK TABLES `Categorie` WRITE;
/*!40000 ALTER TABLE `Categorie` DISABLE KEYS */;
/*!40000 ALTER TABLE `Categorie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Gebruiker`
--

DROP TABLE IF EXISTS `Gebruiker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Gebruiker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `achternaam` varchar(255) DEFAULT NULL,
  `functie` varchar(255) DEFAULT NULL,
  `geboortedatum` datetime DEFAULT NULL,
  `gebruikersnaam` varchar(255) NOT NULL,
  `klas` varchar(255) DEFAULT NULL,
  `uuid` varchar(30) NOT NULL,
  `voornaam` varchar(255) DEFAULT NULL,
  `wachtwoord` varchar(255) NOT NULL,
  `begeleider_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9vr11myjn5da29ethn50hk2ap` (`gebruikersnaam`),
  UNIQUE KEY `UK_lst47wgkuvfpvil44ogre4l3c` (`uuid`),
  UNIQUE KEY `UK_ownpbc1p2ercj2f7aqblks4c3` (`uuid`),
  KEY `FK7jxsi53g98mheaonx2720dii4` (`begeleider_id`),
  CONSTRAINT `FK7jxsi53g98mheaonx2720dii4` FOREIGN KEY (`begeleider_id`) REFERENCES `Gebruiker` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Gebruiker`
--

LOCK TABLES `Gebruiker` WRITE;
/*!40000 ALTER TABLE `Gebruiker` DISABLE KEYS */;
INSERT INTO `Gebruiker` VALUES (1,'De Engh','administrator','2012-06-01 00:00:00','AdminDeEngh','','ol1tsi4j9bk4326c0867mlimb9','Praktijk','f2fae4cdd3cb5090af279eeba3512d13922cbbf824a78f5f1e9da5f73207d1a905cc4687dd34ecc0834d0901eab08d73950cccc59f9a9c51dc5fb6732f57caf5',NULL);
/*!40000 ALTER TABLE `Gebruiker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SpellingRegel`
--

DROP TABLE IF EXISTS `SpellingRegel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SpellingRegel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `korteUitleg` varchar(255) DEFAULT NULL,
  `naam` varchar(255) DEFAULT NULL,
  `opdrachtUitleg` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SpellingRegel`
--

LOCK TABLES `SpellingRegel` WRITE;
/*!40000 ALTER TABLE `SpellingRegel` DISABLE KEYS */;
/*!40000 ALTER TABLE `SpellingRegel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Uitleg`
--

DROP TABLE IF EXISTS `Uitleg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Uitleg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `uuid` varchar(30) NOT NULL,
  `volgorde` int(11) DEFAULT NULL,
  `spellingRegel_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_p52b0maml4sddjpgfcpwsl2ts` (`uuid`),
  UNIQUE KEY `UK_i8d4ydjkxm3kqlem6ogx39cng` (`uuid`),
  KEY `FK3o6u685amhyq4hfu1oxvccn7h` (`spellingRegel_id`),
  CONSTRAINT `FK3o6u685amhyq4hfu1oxvccn7h` FOREIGN KEY (`spellingRegel_id`) REFERENCES `SpellingRegel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Uitleg`
--

LOCK TABLES `Uitleg` WRITE;
/*!40000 ALTER TABLE `Uitleg` DISABLE KEYS */;
/*!40000 ALTER TABLE `Uitleg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UitlegRegel`
--

DROP TABLE IF EXISTS `UitlegRegel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UitlegRegel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `controleren` bit(1) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `iconText` varchar(255) DEFAULT NULL,
  `intikken` bit(1) DEFAULT NULL,
  `overschrijven` bit(1) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `uitspraak` varchar(255) DEFAULT NULL,
  `uuid` varchar(30) NOT NULL,
  `wachttijd` int(11) DEFAULT NULL,
  `uitleg_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_5bh7jem68dflemtht3gt32bcc` (`uuid`),
  UNIQUE KEY `UK_qd8l83maeqi0rnhf2qw7ltllu` (`uuid`),
  KEY `FK923hlksk2u4xuv5nm16da81bj` (`uitleg_id`),
  CONSTRAINT `FK923hlksk2u4xuv5nm16da81bj` FOREIGN KEY (`uitleg_id`) REFERENCES `Uitleg` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UitlegRegel`
--

LOCK TABLES `UitlegRegel` WRITE;
/*!40000 ALTER TABLE `UitlegRegel` DISABLE KEYS */;
/*!40000 ALTER TABLE `UitlegRegel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Woord`
--

DROP TABLE IF EXISTS `Woord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Woord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `moeilijkheidsgraad` varchar(255) NOT NULL,
  `uitspraak` varchar(255) NOT NULL,
  `woord` varchar(50) NOT NULL,
  `categorie_id` int(11) DEFAULT NULL,
  `spellingRegel_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsr2p16sb8kpgxfknnt2vt9qfo` (`categorie_id`),
  KEY `FKqr1hjedwwn8nlbkj6rkld7608` (`spellingRegel_id`),
  CONSTRAINT `FKqr1hjedwwn8nlbkj6rkld7608` FOREIGN KEY (`spellingRegel_id`) REFERENCES `SpellingRegel` (`id`),
  CONSTRAINT `FKsr2p16sb8kpgxfknnt2vt9qfo` FOREIGN KEY (`categorie_id`) REFERENCES `Categorie` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Woord`
--

LOCK TABLES `Woord` WRITE;
/*!40000 ALTER TABLE `Woord` DISABLE KEYS */;
/*!40000 ALTER TABLE `Woord` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-10-31 14:58:45

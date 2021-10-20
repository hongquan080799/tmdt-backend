-- MySQL dump 10.13  Distrib 8.0.25, for Linux (x86_64)
--
-- Host: localhost    Database: TMDT
-- ------------------------------------------------------
-- Server version	8.0.25-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `TMDT`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `TMDT` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `TMDT`;

--
-- Table structure for table `ct_phatsinh`
--

DROP TABLE IF EXISTS `ct_phatsinh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ct_phatsinh` (
  `MAPHIEU` int NOT NULL,
  `MASP` varchar(10) NOT NULL,
  `SOLUONG` int DEFAULT NULL,
  `DONGIA` float DEFAULT NULL,
  PRIMARY KEY (`MAPHIEU`,`MASP`),
  KEY `MASP` (`MASP`),
  CONSTRAINT `ct_phatsinh_ibfk_1` FOREIGN KEY (`MAPHIEU`) REFERENCES `phatsinh` (`ID`),
  CONSTRAINT `ct_phatsinh_ibfk_2` FOREIGN KEY (`MASP`) REFERENCES `sanpham` (`MASP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ct_phatsinh`
--

LOCK TABLES `ct_phatsinh` WRITE;
/*!40000 ALTER TABLE `ct_phatsinh` DISABLE KEYS */;
INSERT INTO `ct_phatsinh` VALUES (6,'SP34993752',1,100),(8,'SP55825962',10,100),(11,'SP34993752',9,100);
/*!40000 ALTER TABLE `ct_phatsinh` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`hongquan`@`localhost`*/ /*!50003 TRIGGER `CAPNHATSLT_FOR_INSERT` AFTER INSERT ON `ct_phatsinh` FOR EACH ROW BEGIN
	DECLARE LOAIPHIEU CHAR(1);
	SELECT ps.loai into LOAIPHIEU FROM phatsinh ps WHERE ps.id = NEW.maphieu;
	IF LOAIPHIEU = 'N' THEN
	BEGIN
		UPDATE sanpham
		SET soluong = soluong + NEW.soluong
		WHERE masp = NEW.masp;
	END;
	ELSE 
	BEGIN
		IF NEW.soluong > (SELECT soluong FROM sanpham WHERE masp = NEW.masp) THEN
			SIGNAL SQLSTATE '42927' SET MESSAGE_TEXT = 'Error Generated';
		ELSE
			UPDATE sanpham 
			SET soluong = soluong - NEW.soluong
			WHERE masp = NEW.masp;
		END IF;
	END;
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`hongquan`@`localhost`*/ /*!50003 TRIGGER `CAPNHATSLT_FOR_UPDATE` AFTER UPDATE ON `ct_phatsinh` FOR EACH ROW BEGIN
	DECLARE LOAIPHIEU CHAR(1);
	DECLARE SOLUONGD INT;
	SELECT ps.loai into LOAIPHIEU FROM phatsinh ps where ps.id = OLD.maphieu;
	SET SOLUONGD = NEW.soluong - OLD.soluong;
	IF LOAIPHIEU = 'X' THEN
	BEGIN
		IF SOLUONGD <= (SELECT soluong FROM sanpham WHERE masp = OLD.masp) THEN
		BEGIN
			UPDATE sanpham 
			SET soluong = soluong - SOLUONGD	
			WHERE masp = OLD.masp;
		END;
		ELSE
			SIGNAL SQLSTATE '42927' SET MESSAGE_TEXT = 'Error Generated';
		END IF;
	END;
	ELSE
	BEGIN
		IF SOLUONGD >= 0 THEN
		BEGIN
			UPDATE sanpham
			SET soluong = soluong + SOLUONGD	
			WHERE masp = OLD.masp;
		END;
		ELSEIF((SELECT soluong FROM sanpham WHERE masp = OLD.masp) + SOLUONGD) >= 0 THEN
		BEGIN
			UPDATE sanpham 
			SET soluong = soluong + SOLUONGD	
			WHERE masp = OLD.masp;
		END;
		ELSE
			SIGNAL SQLSTATE '42927' SET MESSAGE_TEXT = 'Error Generated';
		END IF;	
	END;
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`hongquan`@`localhost`*/ /*!50003 TRIGGER `CAPNHATSLT_FOR_DELETE` AFTER DELETE ON `ct_phatsinh` FOR EACH ROW BEGIN
	DECLARE loaiphieu CHAR(1);
	SELECT ps.loai INTO LOAIPHIEU FROM phatsinh ps where ps.id = OLD.maphieu;
	IF @LOAIPHIEU = 'X' THEN
	BEGIN
		UPDATE sanpham 
		SET soluong = soluong + OLD.soluong
		WHERE MAVT = (SELECT sanpham FROM OLD.masp);
	END;
	ELSE
	BEGIN
		IF OLD.soluong > (SELECT soluong FROM sanpham WHERE masp = OLD.masp) THEN
			SIGNAL SQLSTATE '42927' SET MESSAGE_TEXT = 'Error Generated';
		ELSE
			UPDATE sanpham
			SET soluong = soluong - OLD.soluong
			WHERE masp = OLD.masp;
		END IF;
	END;
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ctdh`
--

DROP TABLE IF EXISTS `ctdh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ctdh` (
  `MADH` varchar(10) NOT NULL,
  `MASP` varchar(10) NOT NULL,
  `SOLUONG` int DEFAULT NULL,
  `GIA` float DEFAULT NULL,
  PRIMARY KEY (`MADH`,`MASP`),
  KEY `MASP` (`MASP`),
  CONSTRAINT `ctdh_ibfk_1` FOREIGN KEY (`MADH`) REFERENCES `donhang` (`MADH`),
  CONSTRAINT `ctdh_ibfk_2` FOREIGN KEY (`MASP`) REFERENCES `sanpham` (`MASP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ctdh`
--

LOCK TABLES `ctdh` WRITE;
/*!40000 ALTER TABLE `ctdh` DISABLE KEYS */;
INSERT INTO `ctdh` VALUES ('DH10162167','SP55825962',1,180),('DH18489641','SP91754246',1,NULL),('DH18704398','SP34993752',1,NULL),('DH18768029','SP92489377',1,90),('DH18768029','SP92593334',1,90),('DH26832217','SP34993752',3,90),('DH26832217','SP55825962',1,90),('DH26832217','SP91754246',1,198.9),('DH27683579','SP34993752',3,90),('DH27683579','SP55825962',1,90),('DH30630744','SP34993752',5,90),('DH30686708','SP34993752',5,90),('DH31771676','SP34993752',7,180),('DH32370104','SP34993752',5,90),('DH33603341','SP34993752',1,90),('DH33651815','SP55825962',4,90),('DH33816384','SP91754246',5,198.9),('DH42513977','SP34993752',1,90),('DH42513977','SP55825962',1,90),('DH42513977','SP91754246',1,198.9),('DH52056025','SP19112715',6,90),('DH52056025','SP34993752',2,180),('DH52470718','SP19112715',8,90),('DH58881563','SP34993752',6,NULL),('DH59226674','SP34993752',6,NULL),('DH60073736','SP55825962',1,NULL),('DH60222908','SP55825962',1,NULL),('DH60274174','SP55825962',1,NULL),('DH60405668','SP91754246',1,NULL),('DH60752545','SP91754246',1,NULL),('DH61359248','SP34993752',1,NULL),('DH61359248','SP91754246',1,NULL),('DH61456589','SP91754246',1,NULL),('DH61552615','SP91754246',2,NULL),('DH69382068','SP91754246',1,198.9),('DH71018386','SP91754246',1,198.9),('DH71095976','SP91754246',1,198.9),('DH71615833','SP91754246',1,198.9),('DH97563806','SP55825962',1,180),('DH97563806','SP92489377',1,90),('DH97563806','SP92593334',1,90);
/*!40000 ALTER TABLE `ctdh` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`hongquan`@`localhost`*/ /*!50003 TRIGGER `CAPNHATSLT_FOR_INSERT_DH` AFTER INSERT ON `ctdh` FOR EACH ROW BEGIN 
    UPDATE sanpham  
    SET SOLUONG = SOLUONG - NEW.soluong  WHERE MASP = NEW.masp;
	END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `danhgia`
--

DROP TABLE IF EXISTS `danhgia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `danhgia` (
  `MASP` varchar(10) NOT NULL,
  `MAKH` int NOT NULL,
  `DANHGIA` int DEFAULT NULL,
  PRIMARY KEY (`MASP`,`MAKH`),
  KEY `MAKH` (`MAKH`),
  CONSTRAINT `danhgia_ibfk_1` FOREIGN KEY (`MAKH`) REFERENCES `khachhang` (`ID`),
  CONSTRAINT `danhgia_ibfk_2` FOREIGN KEY (`MASP`) REFERENCES `sanpham` (`MASP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `danhgia`
--

LOCK TABLES `danhgia` WRITE;
/*!40000 ALTER TABLE `danhgia` DISABLE KEYS */;
INSERT INTO `danhgia` VALUES ('SP19112715',61,2),('SP34993752',4,3),('SP55825962',4,4),('SP55825962',63,3);
/*!40000 ALTER TABLE `danhgia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `danhmuc`
--

DROP TABLE IF EXISTS `danhmuc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `danhmuc` (
  `MADM` varchar(10) NOT NULL,
  `TENDM` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`MADM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `danhmuc`
--

LOCK TABLES `danhmuc` WRITE;
/*!40000 ALTER TABLE `danhmuc` DISABLE KEYS */;
INSERT INTO `danhmuc` VALUES ('DM001','Iphone xịn xò'),('DM002','Motorola products'),('SP11832193','Vsmart good price ');
/*!40000 ALTER TABLE `danhmuc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diachi`
--

DROP TABLE IF EXISTS `diachi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diachi` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `MAKH` int DEFAULT NULL,
  `PROVINCE_ID` int DEFAULT NULL,
  `PROVINCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `DISTRICT_ID` int DEFAULT NULL,
  `DISTRICT_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `WARD_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ADDRESS_DETAIL` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `WARD_CODE` varchar(20) DEFAULT NULL,
  `IS_HOME_ADDRESS` bit(1) DEFAULT b'0',
  `IS_SHIP_ADDRESS` bit(1) DEFAULT b'0',
  PRIMARY KEY (`ID`),
  KEY `MAKH` (`MAKH`),
  CONSTRAINT `diachi_ibfk_1` FOREIGN KEY (`MAKH`) REFERENCES `khachhang` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diachi`
--

LOCK TABLES `diachi` WRITE;
/*!40000 ALTER TABLE `diachi` DISABLE KEYS */;
INSERT INTO `diachi` VALUES (1,59,231,'Nam Định',3319,NULL,'Xã Đại Thắng','Thôn Thống Nhất',NULL,_binary '\0',_binary ''),(3,NULL,267,'Hòa Bình',2163,'Huyện Mai Châu','Xã Vạn Mai','Thông thống nhất ','230323',_binary '\0',_binary '\0'),(4,NULL,268,'Hưng Yên',2194,'Huyện Phù Cừ','Xã Quang Hưng','Thông thống nhất ','220710',_binary '\0',_binary '\0'),(7,4,265,'Điện Biên',2123,'Huyện Điện Biên Đông','Xã Phì Nhừ','Thông thống nhất ','620709',_binary '\0',_binary '\0'),(11,51,268,'Hưng Yên',2194,'Huyện Phù Cừ','Xã Tống Phan','Thông thống nhất ','220713',_binary '\0',_binary ''),(12,51,268,'Hưng Yên',2046,'Huyện Văn Lâm','Xã Lương Tài','dss','220907',_binary '\0',_binary '\0'),(14,4,238,'Quảng Trị',1936,'Huyện Gio Linh','Xã Trung Hải','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM','320419',_binary '',_binary '\0'),(15,60,250,'Hậu Giang',3445,'Huyện Long Mỹ','Xã Vĩnh Viễn A','Thiên đình','640307',_binary '',_binary ''),(16,61,250,'Hậu Giang',3218,'Thị xã Long Mỹ','Xã Long Phú','Thôn thống nhất','640806',_binary '',_binary ''),(17,61,268,'Hưng Yên',2046,'Huyện Văn Lâm','Xã Lương Tài','Thông đồng','220907',_binary '\0',_binary '\0'),(22,62,258,'Bình Thuận',3196,NULL,'Xã Tân Thắng','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM','470608',_binary '',_binary ''),(23,1,262,'Bình Định',1771,'Thị xã Hoài Nhơn','Xã Hoài Thanh',NULL,'370413',_binary '',_binary '\0'),(25,1,265,'Điện Biên',2021,'Huyện Tủa Chùa','Xã Tả Phìn','Thông thống nhất ','620608',_binary '\0',_binary ''),(26,63,268,'Hưng Yên',2046,'Huyện Văn Lâm','Xã Minh Hải','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM','220908',_binary '',_binary '\0'),(28,63,267,'Hòa Bình',2146,'Huyện Kim Bôi','Xã Thượng Tiến','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM','230824',_binary '\0',_binary ''),(30,4,258,'Bình Thuận',1781,'Huyện Tuy Phong','Xã Vĩnh Hảo','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM','470211',_binary '\0',_binary '');
/*!40000 ALTER TABLE `diachi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donhang`
--

DROP TABLE IF EXISTS `donhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donhang` (
  `MADH` varchar(10) NOT NULL,
  `HINHTHUCTHANHTOAN` int DEFAULT NULL,
  `NGAYDAT` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `TONGTIEN` float DEFAULT NULL,
  `TRANGTHAI` int DEFAULT NULL,
  `MAKH` int DEFAULT NULL,
  `MANV` int DEFAULT NULL,
  `MADH_GHN` varchar(100) DEFAULT NULL,
  `diachi` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`MADH`),
  UNIQUE KEY `MADH_GHN` (`MADH_GHN`),
  KEY `MAKH` (`MAKH`),
  KEY `MANV` (`MANV`),
  CONSTRAINT `donhang_ibfk_1` FOREIGN KEY (`MAKH`) REFERENCES `khachhang` (`ID`),
  CONSTRAINT `donhang_ibfk_2` FOREIGN KEY (`MANV`) REFERENCES `nhanvien` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donhang`
--

LOCK TABLES `donhang` WRITE;
/*!40000 ALTER TABLE `donhang` DISABLE KEYS */;
INSERT INTO `donhang` VALUES ('DH10162167',1,'2021-09-10 21:42:42',180,0,1,NULL,'ZX3U8','Thông thống nhất , Xã Tả Phìn, Huyện Tủa Chùa, Điện Biên'),('DH18489641',1,'2021-08-24 15:21:30',198.9,0,4,NULL,NULL,NULL),('DH18704398',1,'2021-08-24 15:25:04',90,3,4,NULL,'ZXP08',NULL),('DH18768029',1,'2021-09-12 03:52:48',180,4,63,NULL,NULL,'Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Thượng Tiến, Huyện Kim Bôi, Hòa Bình'),('DH26832217',0,'2021-09-05 07:27:12',558.9,3,1,4,'ZXOUH','Thông thống nhất , Xã Tả Phìn, Huyện Tủa Chùa, Điện Biên'),('DH27683579',0,'2021-08-30 12:48:04',360,4,51,NULL,'ZXBHI',NULL),('DH30630744',1,'2021-08-31 17:23:51',450,4,4,NULL,'ZXBR0','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Phú Lai, Huyện Yên Thủy, Hòa Bình'),('DH30686708',1,'2021-08-31 17:24:47',450,4,4,NULL,'ZXBR8','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Phú Lai, Huyện Yên Thủy, Hòa Bình'),('DH31771676',1,'2021-09-12 07:29:32',1260,0,63,NULL,NULL,'Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Thượng Tiến, Huyện Kim Bôi, Hòa Bình'),('DH32370104',1,'2021-08-31 17:52:50',450,4,4,NULL,'ZXBRJ','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Phú Lai, Huyện Yên Thủy, Hòa Bình'),('DH33603341',1,'2021-08-31 18:13:23',90,4,4,NULL,'ZXBRM','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Phú Lai, Huyện Yên Thủy, Hòa Bình'),('DH33651815',1,'2021-08-31 18:14:12',360,4,4,NULL,'ZXBRE','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Phú Lai, Huyện Yên Thủy, Hòa Bình'),('DH33816384',1,'2021-08-31 18:16:56',994.5,4,4,NULL,'ZXBMI','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Phú Lai, Huyện Yên Thủy, Hòa Bình'),('DH42513977',0,'2021-08-28 09:21:54',378.9,1,4,2,'ZXBO8',NULL),('DH52056025',0,'2021-09-12 13:07:36',900,4,4,NULL,NULL,'Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Vĩnh Hảo, Huyện Tuy Phong, Bình Thuận'),('DH52470718',1,'2021-09-12 13:14:31',720,0,61,NULL,NULL,'Thôn thống nhất, Xã Long Phú, Thị xã Long Mỹ, Hậu Giang'),('DH58881563',1,'2021-08-22 19:01:22',540,0,4,NULL,NULL,NULL),('DH59226674',1,'2021-08-22 19:07:07',540,0,4,NULL,NULL,NULL),('DH60073736',1,'2021-08-22 19:21:14',90,0,4,NULL,NULL,NULL),('DH60222908',1,'2021-08-22 19:23:43',90,0,4,NULL,NULL,NULL),('DH60274174',1,'2021-08-22 19:24:34',90,0,4,NULL,NULL,NULL),('DH60405668',1,'2021-08-22 19:26:46',198.9,0,4,NULL,NULL,NULL),('DH60752545',1,'2021-08-22 19:32:33',198.9,3,4,2,NULL,NULL),('DH61359248',1,'2021-08-22 19:42:39',288.9,0,4,NULL,NULL,NULL),('DH61456589',1,'2021-08-22 19:44:17',198.9,1,4,2,NULL,NULL),('DH61552615',1,'2021-08-22 19:45:53',397.8,4,4,NULL,NULL,NULL),('DH69382068',1,'2021-09-02 07:56:22',198.9,4,60,NULL,'ZXOFR','Thiên đình, Xã Vĩnh Viễn A, Huyện Long Mỹ, Hậu Giang'),('DH71018386',1,'2021-09-02 08:23:38',198.9,4,61,NULL,'ZXOFM','Thôn thống nhất, Xã Long Phú, Thị xã Long Mỹ, Hậu Giang'),('DH71095976',1,'2021-09-02 08:24:56',198.9,4,61,4,'ZXOFE','Thôn thống nhất, Xã Long Phú, Thị xã Long Mỹ, Hậu Giang'),('DH71615833',1,'2021-09-02 08:33:36',198.9,4,61,NULL,'ZXO7I','Thôn thống nhất, Xã Long Phú, Thị xã Long Mỹ, Hậu Giang'),('DH97563806',1,'2021-09-10 18:12:44',360,4,63,NULL,NULL,'Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM, Xã Nam La, null, Lạng Sơn');
/*!40000 ALTER TABLE `donhang` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`hongquan`@`localhost`*/ /*!50003 TRIGGER `CAPNHATSLT_FOR_UPDATE_DH` AFTER UPDATE ON `donhang` FOR EACH ROW BEGIN
	IF NEW.trangthai = 4 THEN
		UPDATE sanpham SET SOLUONG = SOLUONG + (SELECT SOLUONG FROM ctdh WHERE MADH = NEW.MADH AND MASP = sanpham.MASP )
		WHERE MASP IN (SELECT MASP FROM ctdh WHERE MADH = NEW.madh);
	END IF;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `giohang`
--

DROP TABLE IF EXISTS `giohang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `giohang` (
  `MASP` varchar(10) NOT NULL,
  `MAKH` int NOT NULL,
  `SOLUONG` int DEFAULT NULL,
  PRIMARY KEY (`MASP`,`MAKH`),
  KEY `MAKH` (`MAKH`),
  CONSTRAINT `giohang_ibfk_1` FOREIGN KEY (`MASP`) REFERENCES `sanpham` (`MASP`),
  CONSTRAINT `giohang_ibfk_2` FOREIGN KEY (`MAKH`) REFERENCES `khachhang` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `giohang`
--

LOCK TABLES `giohang` WRITE;
/*!40000 ALTER TABLE `giohang` DISABLE KEYS */;
INSERT INTO `giohang` VALUES ('SP34993752',5,5),('SP55825962',5,13),('SP55825962',59,1),('SP91754246',64,1);
/*!40000 ALTER TABLE `giohang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hinhanh`
--

DROP TABLE IF EXISTS `hinhanh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hinhanh` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `PHOTO` varchar(500) DEFAULT NULL,
  `MASP` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MASP` (`MASP`),
  CONSTRAINT `hinhanh_ibfk_1` FOREIGN KEY (`MASP`) REFERENCES `sanpham` (`MASP`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hinhanh`
--

LOCK TABLES `hinhanh` WRITE;
/*!40000 ALTER TABLE `hinhanh` DISABLE KEYS */;
INSERT INTO `hinhanh` VALUES (127,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2F637553049638412708_ss-s20-fe-xanhla-dd.jpg?alt=media&token=9eaef313-9112-45ad-8479-e0409999dff6','SP55825962'),(128,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc1.jpg?alt=media&token=cf240c72-6c50-4026-a99f-9d1069d6c5be','SP55825962'),(129,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc4.jpeg?alt=media&token=06e4bc69-c0a1-4ef8-8ab1-88cfb2de53b8','SP91754246'),(131,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc5.jpeg?alt=media&token=634ab7a0-d476-4659-8903-5fc7dad926c4','SP92376631'),(132,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc4.jpg?alt=media&token=6ad90777-8706-43c3-b09f-cd8c2b4116c0','SP92439660'),(133,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc2.jpg?alt=media&token=1d405ad0-c047-44f5-ba9c-24a6e1c4ac67','SP92489377'),(134,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc3.jpg?alt=media&token=3a0e994a-db73-4a8c-96c3-46f0e3eb5560','SP92593334'),(135,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc6.jpg?alt=media&token=f1b5483c-02f7-4da0-9a90-ddcf4cefc6a4','SP92650457'),(139,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fsamsung.jpeg?alt=media&token=096220d7-ffb7-4093-9204-ac372245f59c','SP34993752'),(140,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2F637553049638412708_ss-s20-fe-xanhla-dd.jpg?alt=media&token=6a797a62-b39e-46f1-92ba-365196013424','SP34993752'),(144,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc2.jpg?alt=media&token=d9e6e4c0-ff21-437a-88ac-a31e8cef2b5d','SP19112715'),(145,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc6.jpg?alt=media&token=b260ac61-fb63-4dd2-9ba6-66bc3e26f5cd','SP19112715'),(146,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc5.jpeg?alt=media&token=b0ef2a7f-56ac-4202-ac0f-543f80d11cdf','SP19112715'),(147,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fpc4.jpeg?alt=media&token=0505d8ae-2759-4511-8913-a4fb6fc4bd6d','SP19112715'),(148,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2F637553049638412708_ss-s20-fe-xanhla-dd.jpg?alt=media&token=9ec31a94-4d02-4133-84aa-5dea3d603a25','SP52641330');
/*!40000 ALTER TABLE `hinhanh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khachhang`
--

DROP TABLE IF EXISTS `khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khachhang` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(50) DEFAULT NULL,
  `HO` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `TEN` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `gioitinh` int DEFAULT '-1',
  `SDT` varchar(11) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `photo` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`),
  CONSTRAINT `khachhang_ibfk_1` FOREIGN KEY (`USERNAME`) REFERENCES `taikhoan` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khachhang`
--

LOCK TABLES `khachhang` WRITE;
/*!40000 ALTER TABLE `khachhang` DISABLE KEYS */;
INSERT INTO `khachhang` VALUES (1,'GO108220210304385802787','Trần','Hồng Quân',1,'0332626317','hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(4,'FB2880945922159126','Trần Hồng','Quân',1,'0332626313','hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fthanh.jpg?alt=media&token=5d479458-3658-4a5d-ae27-db3b3bcc23f5'),(5,'tranhongquan','tran hong','quan',1,NULL,'issacnewton321@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(7,'thienhuynh','tran hong','quan',1,NULL,'issacnewton321@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(9,'thiednhuynh','tran hosng','quasn',1,NULL,'dsds','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(11,'thiedndhuynh','tran hosng','quasn',1,NULL,'dsds','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(12,'thiedndehuynh','tran hosng','quasn',1,NULL,'dsds','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(13,'thiedndehudynh','tran hosng','quasn',1,NULL,'dsds','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(14,'thiednddehudynh','tran hosng','quasn',1,NULL,'dsds','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(18,'thiedndddehudynh',NULL,'quasn',1,NULL,'dsds','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(20,'hoquan031',NULL,'quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(21,'hoquan0d31',NULL,'quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(22,'hoqddduadnd0d31',NULL,'quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(24,'hoqddduaddnd0d31',NULL,'quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(25,'hoqddduaddndx0d31',NULL,'quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(27,'hoqddduadddndx0d31','tran','quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(28,'hoqdddduadddndx0d31','tran','quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(29,'hoqddddduadddndx0d31','tran','quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(30,'hoqddddduadddndx0dd31','tran','quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(31,'hoqdddddusadddndx0dd31','tran','quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(32,'hoqddddddusadddndx0dd31','tran','quasn',1,NULL,'n17dccn139@student.ptithcm.edu.vn','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(33,'hoqdddddddusadddndx0dd31','tran','quasn',1,NULL,'n17dccn139@student.ptithcm.edu.vn','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(35,'hoqdddddddusadddndxd0dd31','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(36,'hoqdddddddusaddddndxd0dd31','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(37,'hoqdddddddusadddddndxd0dd31','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(38,'hoqdddddddusadddsddndxd0dd31','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(39,'hoqdddddddusaddddsddndxd0dd31','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(40,'hoqddddddddusaddddsddndxd0dd31','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(41,'hoqddddddddusaddsddsddndxd0dd31','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(42,'redd','tran','quasn',1,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(43,'rsssedd','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(44,'rsssedsssd','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(45,'rsdssedsssd','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(46,'rsdssedssssd','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(47,'rsdssedssssdd','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(48,'rsdswsedssssdd','tran','quasn',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(49,'hongquan','quan','tran',0,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(50,'vanthanh','nguyen van','thanh',1,NULL,'hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(51,'tientien','Trần','Tiên',0,'0332626317','hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(59,'tientien2','Trần','Tiên',0,NULL,'hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=df79e598-c3a3-409b-a00a-0591130e536e'),(60,'khachhang','Tăng','Phạm',1,'0336781801','hongquan080799@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=69698c16-bc4d-4770-854d-41def59884d2'),(61,'GO102529045376323717210','TRAN HONG QUAN','D17CQCN03-N',0,'0336781801','n17dccn139@student.ptithcm.edu.vn','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fthanh.jpg?alt=media&token=7716e430-a850-4076-a6f8-d326a3a3c284'),(62,'thanhvan','Nguyễn','Thành',1,'0332626317','hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fthanh.jpg?alt=media&token=f64a442d-db02-40fa-a4d6-af907be66dfc'),(63,'huongpham','Phạm','Hương',1,'0332626317','hongquan879879@gmail.com','https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fq.jpg?alt=media&token=5730a818-63d2-4669-9e12-a390c7fc6f6e'),(64,'GO102173254716656468837','Trần','Quân',0,NULL,'hongquan879879@gmail.com','https://lh3.googleusercontent.com/a/AATXAJykua0O76gNtLBU-ODbWP9d_fIoJWPkSdAD9xD-=s96-c'),(65,'GO114124244144027042252','Trần','NguyễnTrân',0,NULL,'hongquan080799@gmail.com','https://lh3.googleusercontent.com/a/AATXAJxOKXQIVbXSIXV9cZzkllaYEd-tmtsi5d2Oa7k=s96-c');
/*!40000 ALTER TABLE `khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhanvien`
--

DROP TABLE IF EXISTS `nhanvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhanvien` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(50) DEFAULT NULL,
  `HO` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `TEN` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `GIOITINH` int DEFAULT NULL,
  `SDT` varchar(11) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `LUONG` float DEFAULT NULL,
  `photo` varchar(500) DEFAULT NULL,
  `DIACHI` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`),
  CONSTRAINT `nhanvien_ibfk_1` FOREIGN KEY (`USERNAME`) REFERENCES `taikhoan` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhanvien`
--

LOCK TABLES `nhanvien` WRITE;
/*!40000 ALTER TABLE `nhanvien` DISABLE KEYS */;
INSERT INTO `nhanvien` VALUES (1,'nhanvien','trần ','Quân',0,NULL,'hongquan879879@gmail.com',0,'https://img.icons8.com/material-sharp/96/000000/user-male-circle.png','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM'),(2,'nhanvien2','trần ','Quân',0,'0332626317','hongquan879879@gmail.com',0,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Ficon.png?alt=media&token=ac3eafb4-07c0-46b5-bd40-0557fb0b0bff','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM'),(3,'nhanvien3','trần ','Quân',0,'0332626317','hongquan879879@gmail.com',0,'https://img.icons8.com/material-sharp/96/000000/user-male-circle.png','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM'),(4,'nhanvien4','trần ','Thư',0,'0332626317','hongquan879879@gmail.com',0,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fthanh.jpg?alt=media&token=df4681a3-da03-4a3c-a13f-7aaac9dd09be','Chung cư k26 Dương Quảng Hàm p7 Q.Gò vấp TPHCM'),(5,'nhanvien5','nguyen van','thanh',0,'0336781801','hongquan879879@gmail.com',0,'https://img.icons8.com/material-sharp/96/000000/user-male-circle.png','dong thap'),(6,'nhanvien6','tran','quan',0,'0336781801','issacnewton321@gmail.com',0,'https://firebasestorage.googleapis.com/v0/b/tmdt-17064.appspot.com/o/images%2Fthanh.jpg?alt=media&token=9d851b5f-50aa-4cd3-b2cf-a97c4c39da6a','chung cu');
/*!40000 ALTER TABLE `nhanvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phatsinh`
--

DROP TABLE IF EXISTS `phatsinh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phatsinh` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `NGAY` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `LOAI` char(1) DEFAULT NULL,
  `MANV` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MANV` (`MANV`),
  CONSTRAINT `phatsinh_ibfk_1` FOREIGN KEY (`MANV`) REFERENCES `nhanvien` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phatsinh`
--

LOCK TABLES `phatsinh` WRITE;
/*!40000 ALTER TABLE `phatsinh` DISABLE KEYS */;
INSERT INTO `phatsinh` VALUES (6,'2021-09-01 22:20:26','N',NULL),(8,'2021-09-01 23:51:35','N',2),(9,'2021-09-05 07:38:10','N',4),(10,'2021-09-07 14:57:35','N',3),(11,'2021-09-12 03:56:37','X',4),(12,'2021-09-12 13:21:19','N',6);
/*!40000 ALTER TABLE `phatsinh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quyen`
--

DROP TABLE IF EXISTS `quyen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quyen` (
  `MAQUYEN` int NOT NULL,
  `TENQUYEN` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`MAQUYEN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quyen`
--

LOCK TABLES `quyen` WRITE;
/*!40000 ALTER TABLE `quyen` DISABLE KEYS */;
INSERT INTO `quyen` VALUES (1,'ADMIN'),(2,'KHACHHANG'),(3,'NHANVIEN');
/*!40000 ALTER TABLE `quyen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reset_password`
--

DROP TABLE IF EXISTS `reset_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reset_password` (
  `username` varchar(10) NOT NULL,
  `code` int DEFAULT NULL,
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `validate` int NOT NULL DEFAULT '240',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reset_password`
--

LOCK TABLES `reset_password` WRITE;
/*!40000 ALTER TABLE `reset_password` DISABLE KEYS */;
INSERT INTO `reset_password` VALUES ('huongpham',16312961,'2021-09-10 17:48:27',240000),('tientien',16308183,'2021-09-05 05:05:24',240000);
/*!40000 ALTER TABLE `reset_password` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sanpham`
--

DROP TABLE IF EXISTS `sanpham`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sanpham` (
  `MASP` varchar(10) NOT NULL,
  `TENSP` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `SOLUONG` int DEFAULT NULL,
  `DONGIA` float DEFAULT NULL,
  `KHUYENMAI` float DEFAULT NULL,
  `MOTA_NGAN` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `MOTA_CHITIET` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `MADM` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`MASP`),
  KEY `MADM` (`MADM`),
  CONSTRAINT `sanpham_ibfk_1` FOREIGN KEY (`MADM`) REFERENCES `danhmuc` (`MADM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sanpham`
--

LOCK TABLES `sanpham` WRITE;
/*!40000 ALTER TABLE `sanpham` DISABLE KEYS */;
INSERT INTO `sanpham` VALUES ('SP19112715','Iphone XS',2,100,0.1,'Sản phẩm tốt trong tầm giá','Sản phẩm tốt trong tầm giá','DM001'),('SP34993752','Samsung 9s',3,200,0.1,'Sản phẩm tốt','xin','DM001'),('SP52641330','Vsmart bee 3',10,100,0.1,'Sản phẩm tốt trong tầm giá','Sản phẩm tốt trong tầm giá','DM001'),('SP55825962','Samsung Galaxy ',8,200,0.1,'Sản phẩm xịn giá cả phải chăng','Sản phẩm thật là tốt không chê vào đây được \nđó là mong muốn thật sự của những người có xiền','DM001'),('SP91754246','Samsung note 9',11,221,0.1,'sản phẩm tốt trong tầm giá','Sản phẩm thật là tốt không chê vào đây được \nđó là mong muốn thật sự của những người có xiền','DM001'),('SP92376631','Vsmart bee 3',10,100,0.1,'Sản phẩm tốt trong tầm giá','Sản phẩm thật là tốt không chê vào đây được \nđó là mong muốn thật sự của những người có xiền','SP11832193'),('SP92439660','Samsumg Fold 3',10,100,0.1,'Sản phẩm tốt trong tầm giá','Sản phẩm thật là tốt không chê vào đây được \nđó là mong muốn thật sự của những người có xiền','DM002'),('SP92489377','Iphone 13s Pro max',10,100,0.1,'Sản phẩm tốt trong tầm giá','Sản phẩm thật là tốt không chê vào đây được \nđó là mong muốn thật sự của những người có xiền','DM001'),('SP92593334','Vivo 5G',10,100,0.1,'Sản phẩm tốt trong tầm giá','Sản phẩm thật là tốt không chê vào đây được \nđó là mong muốn thật sự của những người có xiền','DM002'),('SP92650457','Iphone 12 ProMax',10,100,0.1,'Sản phẩm tốt trong tầm giá','Sản phẩm thật là tốt không chê vào đây được \nđó là mong muốn thật sự của những người có xiền','DM001');
/*!40000 ALTER TABLE `sanpham` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taikhoan`
--

DROP TABLE IF EXISTS `taikhoan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taikhoan` (
  `USERNAME` varchar(50) NOT NULL,
  `PASSWORD` varchar(500) DEFAULT NULL,
  `MAQUYEN` int DEFAULT NULL,
  `VERIFICATION_CODE` varchar(200) DEFAULT NULL,
  `STATUS` int DEFAULT '0',
  PRIMARY KEY (`USERNAME`),
  KEY `MAQUYEN` (`MAQUYEN`),
  CONSTRAINT `taikhoan_ibfk_1` FOREIGN KEY (`MAQUYEN`) REFERENCES `quyen` (`MAQUYEN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taikhoan`
--

LOCK TABLES `taikhoan` WRITE;
/*!40000 ALTER TABLE `taikhoan` DISABLE KEYS */;
INSERT INTO `taikhoan` VALUES ('FB2880945922159126','123',2,NULL,1),('GO102173254716656468837','$2a$10$d5J/HwrzNpu2SORNsHl3AeSJ0.LZtUK.VNezRMpFC2HCMwiTcdsrq',2,NULL,1),('GO102529045376323717210','$2a$10$UUz/Rnixn.p5DJ3cme6Tn.UkszsoSBsSihB3cmnJCI68DHKGX2dJO',2,NULL,1),('GO108220210304385802787','$2a$10$nsSSbLL2Idgq4F7m8FwGwe5e4BQEFFV.5MbDqrRJI9h9kYuYU5y1.',2,NULL,1),('GO114124244144027042252','$2a$10$NT23/VZggCz5SDJP5bxstuoaI93LOilVClIDT.OME7QydA6XIGKDq',2,NULL,1),('Hongquan','$2a$10$GHx9xe7s/DeU2Jo08Q3TsOvxgNqFleVQVA.uwMlCKNBC9ock4PDKi',1,'hongquaniPJ20rIPbuAABiyN5wKud6ZTYsgtX65hI0ZQM0ZTZAiPpLTZiT',1),('hoqddddddddusaddddsddndxd0dd31','$2a$10$CTj87roCOEVPTRFhUyAK5uZiEvBmKFsQH2tjqtFCnTZSSCKtrz1wa',2,'hoqddddddddusaddddsddndxd0dd31wYq1o3i5JWu6VoV5eyjOFnYHxePMG3',0),('hoqddddddddusaddsddsddndxd0dd31','$2a$10$Gp47aEVp5PrFw2D/Aq459OkVDdOyw8fq5DHzA2WTmr8qzzP921hA.',2,'hoqddddddddusaddsddsddndxd0dd31uyKI8J6WaZmu2j21XWnZYTwyIy7i4Q',0),('hoqdddddddusadddddndxd0dd31','$2a$10$1/IWwLDbCciebCZenKLlEeBTUPf8.eXKIAngMu0EoRKWoERJKz.DK',2,'hoqdddddddusadddddndxd0dd31mypxUyxxxY',0),('hoqdddddddusaddddndxd0dd31','$2a$10$GIiOk2VxaXJrBvsqibFKVO/rZ5TItHLf6P4Dz9T5/P71tKBlNCc22',2,'hoqdddddddusaddddndxd0dd31BpErsTMyDQ',0),('hoqdddddddusaddddsddndxd0dd31','$2a$10$TjiXPM6sYOYqmix.5V0/vusGQ29b.O3ab4uFFUS/V7MUWeBycddB.',2,'hoqdddddddusaddddsddndxd0dd31OVBSg5uEgW',0),('hoqdddddddusadddndx0dd31','$2a$10$JvL63EDAEnf21KE6J07gleoTbR8P0itqKL7PN6N74mDWmjVv4He1a',2,'hoqdddddddusadddndx0dd31bffTKFdTxt',0),('hoqdddddddusadddndxd0dd31','$2a$10$yS1rVTxeK7BtbO2s/QDcSumliS4ujVjObcVN3ilgs835QXZ19CTh2',2,'hoqdddddddusadddndxd0dd31JEvkkOcHcV',0),('hoqdddddddusadddsddndxd0dd31','$2a$10$zwALidvkQcydzjGW7XloNeOHxTaOQfMUD3JRzTOtQBtLIH4t6uwnK',2,'hoqdddddddusadddsddndxd0dd31kjXop0vCsQ',0),('hoqddddddusadddndx0dd31','$2a$10$3rfv9czyW5IAq348Kr8Tq.9xeGV2fiVHmZwH3SwGen6t21VcKjXi6',2,'hoqddddddusadddndx0dd31Kvrcm2gDkE',0),('hoqddddduadddndx0d31','$2a$10$/7dgVapYWdIkvF3afIvePe7ONSsvvUpYJsOtxpZxUDj9HbYFCW23y',2,'hoqddddduadddndx0d31lXjwZk8f6m',0),('hoqddddduadddndx0dd31','$2a$10$LkkopTLGTJ8CkEmMBFVQ9uQfhIVORyHeoI7WGucBm5UNtOEFRYoX2',2,'hoqddddduadddndx0dd31khNib4QiWK',0),('hoqdddddusadddndx0dd31','$2a$10$7lnvvnV.onAcva4SPGtpHulJGPOEH6kaKin0ipmFhKk43pFM2U9uq',2,'hoqdddddusadddndx0dd31aBHwYpGpw9',0),('hoqdddduadddndx0d31','$2a$10$oXe0Eq0LCtqLTDTGhMkdPuzlY9OFbPcb7JRoLtuonGrFvKOJz9gRq',2,'hoqdddduadddndx0d31df5BMM1lyv',0),('hoqddduadddndx0d31','$2a$10$WNAHoPkF0cZYhw28kYONGuZ.gHI5bzzpwjbRbuaQGJWUqmS1ja8Y6',2,'hoqddduadddndx0d317bWmmaJsYy',0),('hoqddduaddnd0d31','$2a$10$s.AQR3gozZDjpxFbEGF5PuOkRr9p/OrrtbL8Hsv3fN9gqIYDVKOTK',2,'hoqddduaddnd0d317nQTAypMXZ',0),('hoqddduaddndx0d31','$2a$10$tWPBVLP3CpBUTuuhJZvv6.KWZ21KpQwAocvKZwGo.muYgNvffbQhm',2,'hoqddduaddndx0d31uHjrd605eN',0),('hoqddduadnd0d31','$2a$10$HCpUOyw/BOqQCwZ29jaFs.TVQ3J.Bh9gKKlwMuxiaGLCc364pNOR6',2,'hoqddduadnd0d31zqgaozJJv1',0),('hoquan031','$2a$10$L8Pb/Ff1CDVgmmSfQrRVPO1fqqWDnrCnQgf/AB3B2iEcCJUSeqkZa',2,'hoquan031QCWkXWbvMK',0),('hoquan0d31','$2a$10$SsFySrqIwMpQr5aSk/LNWely8OKMNLZD9Vy0UWwtsBL9AdtQleLLe',2,'hoquan0d31D3Pj3mdxf4',0),('huongpham','$2a$10$1ostW.lRfbwfuIt0.E3.Fer4CMJY6OUx2bDHcXVQAejpx61IcoFm.',2,'huongphamwuAA9300gsRjrEaJeZ6QtdrjwujuGvn0YeGiazNjmuvMlWiN1X',1),('khachhang','$2a$10$AGoHa9lS6rzXnS/97tfYEeYA60vsF9V8sSYjOEVgRFUcTRq/t5zmO',2,'khachhang9YMmrabz7Ktshqe4SUDc2pPhzQha5gtAXwm4iyCY6BAvIfkVld',1),('nhanvien','$2a$10$lutp4Oj6f70F73mair0k7OFmPyK3mlBwAkU1gpM214q5LW4Bl1GkO',3,NULL,0),('nhanvien2','$2a$10$A.JHVVRnWYrrMZXn19bZA.x7aUZROy8RcVxnVkGfQqFDXGtkKE9mq',3,NULL,1),('nhanvien3','$2a$10$UInxRlHApFYPEuT5ne.jrO8p/NtXpl2OIOaolI.Htj1A33U7qyXf2',3,NULL,1),('nhanvien4','$2a$10$7vj0ZAk.x0hBuvxpxsBOIu5bAepuA9oMetOsV/rgbNc0kdVQ4nhUi',3,NULL,1),('nhanvien5','$2a$10$U6lTWmXzxhwE.CEa8qIhcuYhze1OKY96KdZyAQNiRTDw8yETzqlfG',3,NULL,1),('nhanvien6','$2a$10$w/h.2maeASlhBTkmjIPoG.8byP/8HGNkKw3XvHojECZR2YOgEDnkS',3,NULL,1),('redd','$2a$10$QOKbnZlcnLySLIt4YR.de.WmUPgb8bnpYPISba56SnjOo/mVDE3rq',2,'reddJEl4jcZ2xn0NVEmdSPYha96K4dH0pr',0),('rsdssedsssd','$2a$10$yDIfDYyL4DV4Cl7L/KZctuovgqMF.CEsTxWf4xR8sx6mS2XB1XT/K',2,'rsdssedsssdA6zYb6YpPDgTfCTa6rH2JRgCapsqCazvt2zRlE1WG46sDid1ZK',0),('rsdssedssssd','$2a$10$ISUPjH550Onw8n1IABdnpexAFT9D5bgNfkXXdFm7GMh/IUB7yeHS6',2,'rsdssedssssdolYoUV6x7oSMd6bUN8z5socvDOeEv11dl9LanrXl6wPx2Wnmm3',0),('rsdssedssssdd','$2a$10$YPMt5fQZNinAzUkNp/HRD.0PQ2AXcryhLSPdfdXJYzps5SDgL5UGe',2,'rsdssedssssdd2kYY6G9bb4aGYYNj4bzFOnU6mWVkLAZZsXQP3A5Wsq281sHGgv',0),('rsdswsedssssdd','$2a$10$VmGHLkkYmdSSfE8WhK7zuunjSNerhq4sS/RS6CqplukTyPeLK3kQK',2,'rsdswsedssssddttWYOtAZ2dpJVVh2CX9IVOpfkyLAF7XbJNce8OqrAGzrBhd3GC',0),('rsssedd','$2a$10$vFOigbYIOwnI69lbkf0ed.6kSeoNRQKzLzW45tGxJpt1FZuvuujRm',2,'rssseddgwOvFvvQoCK1LA007YLOEPDyTn0Ywv',0),('rsssedsssd','$2a$10$i8S.9V8A6R.ceHSghweDoOUwYAxs2FbTiNpFn2p.Bn3oQ2RuqiQ9m',2,'rsssedsssd4y5ao2hXPiKAW4kzazFjbJEbc9zqfbFpVAAZDFojCLNOoAACrL',0),('thanhvan','$2a$10$BYSHQm6PkUlFxKNJhRV9fubfLTGp25FWfxog5ekpYm/izUDBqaIyy',2,'thanhvanZR2sWHMbNBMTNFiBaq0wcC36EfWNGa2kYZOdvqRlA1GQYBpfuZ',1),('thiedndddehudynh','$2a$10$m7l/ddLTx4MApXpllC3EiuRHPj7yLkRb.y0pgVMgEedzmG5Dn29xy',2,NULL,0),('thiednddehudynh','$2a$10$hBtFZtyoIDODWWWl/XJhGeIYdmQpIZNl1XidHUk7m/H9IG7B7EOYi',2,NULL,0),('thiedndehudynh','$2a$10$TYEMR94T07sNSR6ZnjcW6.uWXFHOT.BIrqJbxbvp1E5os9umfG.4K',2,NULL,0),('thiedndehuynh','$2a$10$/AmvP1NzpBPnh5mV5BLT8eqmHWQkonUwOOgyjvluHLA17qVCt1b2.',2,NULL,0),('thiedndhuynh','$2a$10$Se1i.AaH6jIOyNE8p2y2oeJuqYPf8sKBlWLul6llGIZ.2zlZm4XG.',2,NULL,0),('thiednhuynh','$2a$10$Vx9x.keDZsjVM6WdnxuErOhOkz1ojEJujst1MPMkTrWTjQJhq7dSy',2,NULL,0),('thienhuynh','$2a$10$lUwQRoDjfjCdkL.OG7NhJuBaEYrPXQEUbMVJyiDHTuEWn8UHzVKHy',2,NULL,1),('tientien','$2a$10$KiSH4ORNInhzRwEbysm/VOYt/pyTeMbP6QmLHAYxnncZq/y8GcH8O',2,'tientienbIfyGAh9dyx4YPf2E6PEx6LqjUgpbrbKE7emqml9QgvVXCDXao',1),('tientien2','$2a$10$oeQfG03iG9Wkk27ePb/Hm.C4Flk08pjVD64LDVJm.IYXm3lyM7tkW',2,'tientien2EJlsg3gC5hw0WKlLyrowMsR7EIQnyrxiX8VqRNnkclgoYhCvmg',1),('tranhongquan','$2a$10$l7Bnas9BetrGC2mS1dnF2epxZdqjbve27vijD5Ktlz8vVXc9.WCY6',2,NULL,1),('vanthanh','$2a$10$MaLEvJiOax066PNYfUVH4ODHv6.lSxAj3bJlMyhqDHzr6Bt4bpI4q',2,'vanthanhekgVkmsRCAOBZJpLHeLZUX93JbU7P7S1bg7A3YIbUqB1SyTyVM',1);
/*!40000 ALTER TABLE `taikhoan` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-10-20 12:17:42

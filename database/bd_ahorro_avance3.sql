-- MySQL dump 10.13  Distrib 9.3.0, for Linux (x86_64)
--
-- Host: localhost    Database: bd_ahorro
-- ------------------------------------------------------
-- Server version	9.3.0

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
-- Table structure for table `fuentes_ingreso`
--

DROP TABLE IF EXISTS `fuentes_ingreso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fuentes_ingreso` (
  `id_ingreso` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `descripcion` varchar(150) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `fecha_ingreso` date NOT NULL,
  `categoria` varchar(100) DEFAULT NULL,
  `observaciones` text,
  PRIMARY KEY (`id_ingreso`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `fuentes_ingreso_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuentes_ingreso`
--

LOCK TABLES `fuentes_ingreso` WRITE;
/*!40000 ALTER TABLE `fuentes_ingreso` DISABLE KEYS */;
INSERT INTO `fuentes_ingreso` VALUES (14,3,'TRABAJO - APORTE MENSUAL',900.00,'2025-06-11','TRABAJO','ESTE MONTO ES EL MENSUAL'),(15,3,'CTS',500.00,'2025-06-11','TEMPORAL','TEMPORAL');
/*!40000 ALTER TABLE `fuentes_ingreso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gestion_contrasena`
--

DROP TABLE IF EXISTS `gestion_contrasena`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gestion_contrasena` (
  `id_gestion` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `contrasena_hash` varchar(255) NOT NULL,
  `fecha_cambio` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tipo_cambio` varchar(60) DEFAULT 'Creacion',
  `realizado_por` int DEFAULT NULL,
  PRIMARY KEY (`id_gestion`),
  KEY `id_usuario` (`id_usuario`),
  KEY `realizado_por` (`realizado_por`),
  CONSTRAINT `gestion_contrasena_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `gestion_contrasena_ibfk_2` FOREIGN KEY (`realizado_por`) REFERENCES `usuario` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gestion_contrasena`
--

LOCK TABLES `gestion_contrasena` WRITE;
/*!40000 ALTER TABLE `gestion_contrasena` DISABLE KEYS */;
INSERT INTO `gestion_contrasena` VALUES (1,2,'1234','2025-05-11 05:00:00','Cambio Manual',2),(5,2,'123456','2025-05-11 05:00:00','Recuperacion',2),(6,2,'1234','2025-05-11 05:00:00','Cambio Manual',2),(7,2,'123456789','2025-05-12 05:00:00','Renovacion',2),(8,2,'1234','2025-05-12 05:00:00','Admin',2),(9,2,'1234322a','2025-05-12 00:00:00','Admin',2),(10,2,'1234322a','2025-05-12 00:00:00','Cambio Manual',2),(11,2,'1234','2025-05-12 00:00:00','Cambio Manual',2),(12,2,'1234a','2025-05-12 00:00:00','Cambio Manual',2),(13,2,'1234a','2025-05-12 00:00:00','Cambio Manual',3),(14,2,'1234aaa','2025-05-12 00:00:00','Cambio Manual',3),(15,2,'contrasena123','2025-05-12 00:00:00','Cambio Manual',3),(16,1,'123456','2025-05-15 05:00:00','Admin',1),(17,2,'123456','2025-05-16 05:00:00','Cambio Manual',2),(18,2,'','2025-05-16 05:00:00','Cambio Manual',2),(19,2,'','2025-05-16 05:00:00','Cambio Manual',2),(20,2,'123456','2025-05-18 05:00:00','Cambio Manual',2),(21,3,'12345678','2025-05-18 05:00:00','Admin',2),(22,2,'60fe74406e7f353ed979f350f2fbb6a2e8690a5fa7d1b0c32983d1d8b3f95f67','2025-05-18 05:00:00','Cambio Manual',2),(23,3,'3498be6f5b7e15bd81f0a5fc0d14527de0a1b3afaf440d5abf82dddcf0509cc8','2025-05-18 05:00:00','Cambio Manual',1),(24,3,'3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2','2025-05-20 05:00:00','Admin',1);
/*!40000 ALTER TABLE `gestion_contrasena` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meta`
--

DROP TABLE IF EXISTS `meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre_meta` varchar(100) NOT NULL,
  `Importe` decimal(18,7) DEFAULT NULL,
  `fecha_creacion` date DEFAULT NULL,
  `Fecha_fin` date DEFAULT NULL,
  `Estado` varchar(100) NOT NULL,
  `Importe_inicial_e` decimal(18,7) NOT NULL,
  `Importe_Final_e` decimal(18,7) NOT NULL,
  `Importe_inicial_r` decimal(18,7) NOT NULL,
  `Importe_final_r` decimal(18,7) NOT NULL,
  `Situacion` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta`
--

LOCK TABLES `meta` WRITE;
/*!40000 ALTER TABLE `meta` DISABLE KEYS */;
INSERT INTO `meta` VALUES (1,'auto comprar',12000.0000000,'2025-05-12','2025-06-11','PENDIENTE',0.0000000,1200.0000000,0.0000000,1121.0000000,'A'),(3,'Vacaciones Familiares 2025',8000.0000000,'2024-02-01','2025-01-15','COMPLETADO',0.0000000,8000.0000000,0.0000000,8200.0000000,'C'),(4,'Educación Universitaria Hijos',45000.0000000,'2024-06-01','2028-12-31','EN PROGRESO',0.0000000,45000.0000000,0.0000000,11250.0000000,'A'),(5,'Fondo de Emergencia',15000.0000000,'2024-11-01','2025-11-01','PENDIENTE',0.0000000,15000.0000000,0.0000000,0.0000000,'A'),(6,'Renovar Electrodomésticos',12000.0000000,'2024-08-01','2025-08-01','PAUSADO',0.0000000,12000.0000000,0.0000000,2400.0000000,'I'),(9,'Equipos de Tecnología',10000.0000000,'2024-12-01','2025-12-01','EN PROGRESO',0.0000000,10000.0000000,0.0000000,1000.0000000,'A'),(10,'Fondo de Jubilación',100000.0000000,'2024-01-01','2035-01-01','EN PROGRESO',0.0000000,100000.0000000,0.0000000,30000.0000000,'A');
/*!40000 ALTER TABLE `meta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meta_aporte`
--

DROP TABLE IF EXISTS `meta_aporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meta_aporte` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_meta` int NOT NULL,
  `id_usuario` int NOT NULL,
  `Aporte_Estimado` decimal(18,7) NOT NULL,
  `aporte_real` decimal(18,7) DEFAULT NULL,
  `Fecha_Registro` date NOT NULL,
  `Situacion` varchar(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_aporte`
--

LOCK TABLES `meta_aporte` WRITE;
/*!40000 ALTER TABLE `meta_aporte` DISABLE KEYS */;
INSERT INTO `meta_aporte` VALUES (49,1,3,900.0000000,720.0000000,'2025-06-11','A'),(50,1,3,900.0000000,400.0000000,'2025-06-11','A'),(52,1,1,1.0000000,1.0000000,'2005-12-12','ACT'),(53,1,1,5000.0000000,5000.0000000,'2024-01-20','A'),(54,1,2,3000.0000000,3000.0000000,'2024-02-15','A'),(55,1,3,2500.0000000,2500.0000000,'2024-03-10','A'),(56,1,4,4000.0000000,4000.0000000,'2024-04-05','A'),(57,1,5,3500.0000000,3500.0000000,'2024-05-12','A'),(58,1,6,2750.0000000,2750.0000000,'2024-06-08','A'),(59,1,8,3000.0000000,3000.0000000,'2024-07-15','A'),(60,2,1,15000.0000000,15000.0000000,'2024-03-10','A'),(61,2,2,12000.0000000,12000.0000000,'2024-04-15','A'),(62,2,3,8000.0000000,8000.0000000,'2024-05-20','A'),(63,2,4,7000.0000000,7000.0000000,'2024-06-25','A'),(64,2,5,6000.0000000,6000.0000000,'2024-07-30','A'),(65,3,1,2000.0000000,2000.0000000,'2024-02-05','A'),(66,3,2,1500.0000000,1500.0000000,'2024-03-12','A'),(67,3,3,1200.0000000,1200.0000000,'2024-04-18','A'),(68,3,4,1300.0000000,1300.0000000,'2024-05-22','A'),(69,3,5,1000.0000000,1000.0000000,'2024-06-28','A'),(70,3,6,800.0000000,800.0000000,'2024-07-15','A'),(71,3,8,400.0000000,400.0000000,'2024-08-10','A'),(72,4,1,4000.0000000,4000.0000000,'2024-06-10','A'),(73,4,2,3000.0000000,3000.0000000,'2024-07-15','A'),(74,4,3,2250.0000000,2250.0000000,'2024-08-20','A'),(75,4,4,2000.0000000,2000.0000000,'2024-09-25','A'),(76,6,1,1200.0000000,1200.0000000,'2024-08-10','A'),(77,6,2,800.0000000,800.0000000,'2024-09-15','A'),(78,6,3,400.0000000,400.0000000,'2024-10-20','A'),(79,7,1,6000.0000000,6000.0000000,'2024-05-10','A'),(80,7,2,4000.0000000,4000.0000000,'2024-06-15','A'),(81,7,3,3000.0000000,3000.0000000,'2024-07-20','A'),(82,7,4,2000.0000000,2000.0000000,'2024-08-25','A'),(83,8,1,2000.0000000,2000.0000000,'2024-01-10','A'),(84,8,2,1500.0000000,1500.0000000,'2024-02-15','A'),(85,8,3,1200.0000000,1200.0000000,'2024-03-20','A'),(86,8,4,800.0000000,800.0000000,'2024-04-25','A'),(87,8,5,500.0000000,500.0000000,'2024-05-30','A'),(88,9,1,500.0000000,500.0000000,'2024-12-05','A'),(89,9,2,300.0000000,300.0000000,'2024-12-15','A'),(90,9,3,200.0000000,200.0000000,'2024-12-25','A'),(91,10,1,10000.0000000,10000.0000000,'2024-01-15','A'),(92,10,2,8000.0000000,8000.0000000,'2024-02-20','A'),(93,10,3,6000.0000000,6000.0000000,'2024-03-25','A'),(94,10,4,4000.0000000,4000.0000000,'2024-04-30','A'),(95,10,5,2000.0000000,2000.0000000,'2024-05-15','A'),(96,1,7,1000.0000000,1000.0000000,'2024-08-01','I'),(97,2,7,2000.0000000,2000.0000000,'2024-08-05','I'),(98,4,7,1500.0000000,1500.0000000,'2024-08-10','I');
/*!40000 ALTER TABLE `meta_aporte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meta_prioridad`
--

DROP TABLE IF EXISTS `meta_prioridad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meta_prioridad` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_meta` int NOT NULL,
  `prioridad` int NOT NULL,
  `Situacion` varchar(10) NOT NULL,
  `porcentaje` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_prioridad`
--

LOCK TABLES `meta_prioridad` WRITE;
/*!40000 ALTER TABLE `meta_prioridad` DISABLE KEYS */;
INSERT INTO `meta_prioridad` VALUES (1,1,1,'A',80);
/*!40000 ALTER TABLE `meta_prioridad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `Situacion` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'Administrador','A'),(2,'Integrante Familiar','A'),(3,'Coordinador de Finanzas','A');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_documento`
--

DROP TABLE IF EXISTS `tipo_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_documento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(100) NOT NULL,
  `Situacion` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_documento`
--

LOCK TABLES `tipo_documento` WRITE;
/*!40000 ALTER TABLE `tipo_documento` DISABLE KEYS */;
INSERT INTO `tipo_documento` VALUES (1,'DNI','A'),(2,'CE','A');
/*!40000 ALTER TABLE `tipo_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_rol` int DEFAULT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido` varchar(100) NOT NULL,
  `ID_Tipo_documento` int NOT NULL,
  `Numero_doc` varchar(15) NOT NULL,
  `sexo` varchar(5) NOT NULL,
  `Situacion` varchar(3) NOT NULL,
  `Contrasenia` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,1,'ADMIH','ADMIN',1,'1','F','A','1'),(2,1,'admin','admin',1,'1234567910','M','A','60fe74406e7f353ed979f350f2fbb6a2e8690a5fa7d1b0c32983d1d8b3f95f67'),(3,1,'alondra','aguilar',1,'12345636','F','I','3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2'),(4,1,'misae','challco',1,'71345668','M','I',NULL),(5,1,'misae','challco',2,'2','m','I','121'),(7,1,'Juan','Pérez',1,'12345678','M','A','pass123'),(8,1,'María','Gómez',1,'87654321','F','ACT','pass456'),(9,2,'Carlos','Ramírez',1,'11223344','M','ACT','pass789'),(10,1,'juanca','juanca',1,'12345678','m','A',NULL),(11,2,'Juan ','Perez',1,'22446688','M','I',NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_ingreso`
--

DROP TABLE IF EXISTS `usuario_ingreso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_ingreso` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `Aporte` decimal(18,7) NOT NULL,
  `Situacion` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_ingreso`
--

LOCK TABLES `usuario_ingreso` WRITE;
/*!40000 ALTER TABLE `usuario_ingreso` DISABLE KEYS */;
INSERT INTO `usuario_ingreso` VALUES (1,3,900.0000000,'A');
/*!40000 ALTER TABLE `usuario_ingreso` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-12  5:25:20

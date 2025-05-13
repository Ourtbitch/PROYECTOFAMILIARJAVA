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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuentes_ingreso`
--

LOCK TABLES `fuentes_ingreso` WRITE;
/*!40000 ALTER TABLE `fuentes_ingreso` DISABLE KEYS */;
INSERT INTO `fuentes_ingreso` VALUES (4,2,'holsa',200.00,'2019-09-09','Salario','ew'),(5,2,'abono',100.00,'2023-08-09','Otros','pago de abonos'),(6,2,'ahorros del mes',400.00,'2022-10-09','Salario','ahorros'),(7,2,'fuentes de ahorro',200.00,'2024-08-09','Salario','todo oka'),(8,2,'guardado',700.00,'2025-01-09','Ahorro','guardado del mes'),(9,1,'trabajo',1200.00,'2025-05-12','Salario',''),(10,1,'Propinas',100.00,'2025-05-12','Extras','');
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gestion_contrasena`
--

LOCK TABLES `gestion_contrasena` WRITE;
/*!40000 ALTER TABLE `gestion_contrasena` DISABLE KEYS */;
INSERT INTO `gestion_contrasena` VALUES (1,2,'1234','2025-05-11 05:00:00','Cambio Manual',2),(5,2,'123456','2025-05-11 05:00:00','Recuperacion',2),(6,2,'1234','2025-05-11 05:00:00','Cambio Manual',2),(7,2,'123456789','2025-05-12 05:00:00','Renovacion',2),(8,2,'1234','2025-05-12 05:00:00','Admin',2),(9,2,'1234322a','2025-05-12 00:00:00','Admin',2),(10,2,'1234322a','2025-05-12 00:00:00','Cambio Manual',2),(11,2,'1234','2025-05-12 00:00:00','Cambio Manual',2),(12,2,'1234a','2025-05-12 00:00:00','Cambio Manual',2),(13,2,'1234a','2025-05-12 00:00:00','Cambio Manual',3),(14,2,'1234aaa','2025-05-12 00:00:00','Cambio Manual',3),(15,2,'contrasena123','2025-05-12 00:00:00','Cambio Manual',3);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta`
--

LOCK TABLES `meta` WRITE;
/*!40000 ALTER TABLE `meta` DISABLE KEYS */;
INSERT INTO `meta` VALUES (1,'auto comprar',12000.0000000,'2025-05-12','2025-05-12','Pendiente',0.0000000,1200.0000000,0.0000000,0.0000000,'A'),(2,'auto',1200.0000000,'2025-05-12','2025-05-12','Pendiente',0.0000000,1200.0000000,0.0000000,0.0000000,'I'),(3,'casdd',50000.0000000,'2025-05-12','2025-05-12','Pendiente',0.0000000,50000.0000000,0.0000000,0.0000000,'A'),(4,'aa',12.0000000,'2025-05-12','2025-05-12','Inhabilitado',0.0000000,12.0000000,0.0000000,0.0000000,'A'),(5,'depa',50000.0000000,'2025-05-12','2025-05-12','Pendiente',0.0000000,50000.0000000,0.0000000,0.0000000,'I');
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_aporte`
--

LOCK TABLES `meta_aporte` WRITE;
/*!40000 ALTER TABLE `meta_aporte` DISABLE KEYS */;
INSERT INTO `meta_aporte` VALUES (1,1,1,1000.0000000,500.0000000,'2023-04-01','ACT'),(4,1,3,1000.0000000,800.0000000,'2023-04-01','ACT'),(6,1,3,1500.0000000,1900.0000000,'2023-04-10','INA'),(8,5,4,1000.0000000,800.0000000,'2023-04-01','ACT'),(9,3,5,5000.0000000,4500.0000000,'2024-05-15','ACT'),(11,1,3,1200.0000000,1050.0000000,'2025-02-14','ACT'),(12,2,4,1025.0000000,600.0000000,'2025-04-03','ACT'),(13,1,3,1200.0000000,1050.0000000,'2025-05-11','ACT');
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_prioridad`
--

LOCK TABLES `meta_prioridad` WRITE;
/*!40000 ALTER TABLE `meta_prioridad` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,1,'ADMIH','ADMIN',1,'12345678','F','I','121'),(2,1,'admin','admin',1,'12345679','M','A','contrasena123'),(3,1,'alondra','aguilar',1,'12345636','F','A',NULL),(4,1,'misae','challco',1,'71345668','M','I',NULL),(5,1,'misae','challco',2,'2','m','I','121'),(7,1,'Juan','Pérez',1,'12345678','M','ACT','pass123'),(8,1,'María','Gómez',1,'87654321','F','ACT','pass456'),(9,2,'Carlos','Ramírez',1,'11223344','M','ACT','pass789');
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_ingreso`
--

LOCK TABLES `usuario_ingreso` WRITE;
/*!40000 ALTER TABLE `usuario_ingreso` DISABLE KEYS */;
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

-- Dump completed on 2025-05-13  5:29:11

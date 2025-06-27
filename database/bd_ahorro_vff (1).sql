-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 26-06-2025 a las 23:55:15
-- Versión del servidor: 10.1.38-MariaDB
-- Versión de PHP: 5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bd_ahorro_vff`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `Cerrar_X_META` (IN `ID_META_I` INT, IN `ANIO_MES` VARCHAR(7))  BEGIN
		DELETE FROM meta_aporte 
        WHERE id_meta = ID_META_I
          AND DATE_FORMAT(Fecha_Registro, '%Y-%m') = ANIO_MES;
        
        INSERT INTO meta_aporte (
            id_meta, id_usuario, Aporte_Estimado, aporte_real, Fecha_Registro, Situacion
        )
        
     	SELECT 
        ID_META_I AS ID_META,
        F.id_usuario,
        UI.Aporte,
        F.monto*((select mp.porcentaje from meta_prioridad mp where 			mp.id_meta = ID_META_I)/100) as Aporte_Real,
        F.fecha_ingreso,
        'A' AS SITUACION
        FROM fuentes_ingreso F
        LEFT JOIN usuario_ingreso UI ON F.id_usuario = UI.id_usuario
        LEFT JOIN meta_aporte MA ON DATE_FORMAT(F.fecha_ingreso, '%Y-%m') = 		DATE_FORMAT(MA.Fecha_Registro, '%Y-%m') AND 				    		DATE_FORMAT(F.fecha_ingreso, '%Y-%m') = ANIO_MES
        LEFT JOIN meta_prioridad MP ON MP.id_meta = MA.id_meta
        LEFT JOIN meta M ON M.id = MP.id_meta AND M.id = ID_META_I  AND 		M.Estado  IN ('PENDIENTE') where DATE_FORMAT(F.fecha_ingreso, '%Y-%m') = ANIO_MES;
        
        UPDATE meta M
        JOIN (
            SELECT id_meta, SUM(aporte_real) AS total_aporte,max(Fecha_Registro) as Fecha_Registro
            FROM meta_aporte
            GROUP BY id_meta
        ) AS MA ON M.id = MA.id_meta
        SET M.Importe_final_r = MA.total_aporte,M.Fecha_fin = ma.Fecha_Registro
        WHERE M.id = ID_META_I;
        
        UPDATE meta
        SET Estado = CASE
            WHEN Importe_Final_e = Importe_final_r THEN 'CERRADO'
            WHEN Importe_Final_e > Importe_final_r THEN 'PENDIENTE'
            WHEN Importe_Final_e < Importe_final_r THEN 'CERRADO'
            ELSE  'PENDIENTE'
        END
        WHERE id = ID_META_I;


END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `Trasladar_Saldo_Meta` (IN `p_id_origen` INT, IN `p_id_destino` INT, IN `p_monto` DECIMAL(18,7), IN `p_id_usuario` INT)  BEGIN
    DECLARE saldo_origen DECIMAL(18,7);

    -- Obtener el saldo actual de la meta origen
    SELECT Importe_final_r INTO saldo_origen
    FROM meta
    WHERE id = p_id_origen;

    -- Verificar si hay saldo suficiente
    IF saldo_origen >= p_monto THEN
        -- Restar al saldo de la meta origen
        UPDATE meta
        SET Importe_final_r = Importe_final_r - p_monto
        WHERE id = p_id_origen;

        -- Sumar al saldo de la meta destino
        UPDATE meta
        SET Importe_final_r = Importe_final_r + p_monto
        WHERE id = p_id_destino;

        -- Insertar el registro del traslado
        INSERT INTO meta_traslados (
            id_meta_origen, id_meta_destino, monto, fecha_traslado, id_usuario
        ) VALUES (
            p_id_origen, p_id_destino, p_monto, NOW(), p_id_usuario
        );

        -- Registrar aporte negativo (retiro) en meta origen
        INSERT INTO meta_aporte (
            id_meta, id_usuario, Aporte_Estimado, aporte_real, Fecha_Registro, Situacion
        ) VALUES (
            p_id_origen, p_id_usuario, 0, -p_monto, CURDATE(), 'T'
        );

        -- Registrar aporte positivo (ingreso) en meta destino
        INSERT INTO meta_aporte (
            id_meta, id_usuario, Aporte_Estimado, aporte_real, Fecha_Registro, Situacion
        ) VALUES (
            p_id_destino, p_id_usuario, 0, p_monto, CURDATE(), 'T'
        );
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Saldo insuficiente para realizar el traslado.';
    END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fuentes_ingreso`
--

CREATE TABLE `fuentes_ingreso` (
  `id_ingreso` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `descripcion` varchar(150) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `fecha_ingreso` date NOT NULL,
  `categoria` varchar(100) DEFAULT NULL,
  `observaciones` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `fuentes_ingreso`
--

INSERT INTO `fuentes_ingreso` (`id_ingreso`, `id_usuario`, `descripcion`, `monto`, `fecha_ingreso`, `categoria`, `observaciones`) VALUES
(18, 12, 'TRABAJO', '300.00', '2025-06-26', 'T5T', 'T5T5T'),
(19, 12, 'cts', '500.00', '2025-07-09', 'dede', 'deded');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gestion_contrasena`
--

CREATE TABLE `gestion_contrasena` (
  `id_gestion` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `contrasena_hash` varchar(255) NOT NULL,
  `fecha_cambio` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tipo_cambio` varchar(60) DEFAULT 'Creacion',
  `realizado_por` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `gestion_contrasena`
--

INSERT INTO `gestion_contrasena` (`id_gestion`, `id_usuario`, `contrasena_hash`, `fecha_cambio`, `tipo_cambio`, `realizado_por`) VALUES
(1, 2, '1234', '2025-05-11 05:00:00', 'Cambio Manual', 2),
(5, 2, '123456', '2025-05-11 05:00:00', 'Recuperacion', 2),
(6, 2, '1234', '2025-05-11 05:00:00', 'Cambio Manual', 2),
(7, 2, '123456789', '2025-05-12 05:00:00', 'Renovacion', 2),
(8, 2, '1234', '2025-05-12 05:00:00', 'Admin', 2),
(9, 2, '1234322a', '2025-05-12 00:00:00', 'Admin', 2),
(10, 2, '1234322a', '2025-05-12 00:00:00', 'Cambio Manual', 2),
(11, 2, '1234', '2025-05-12 00:00:00', 'Cambio Manual', 2),
(12, 2, '1234a', '2025-05-12 00:00:00', 'Cambio Manual', 2),
(13, 2, '1234a', '2025-05-12 00:00:00', 'Cambio Manual', 3),
(14, 2, '1234aaa', '2025-05-12 00:00:00', 'Cambio Manual', 3),
(15, 2, 'contrasena123', '2025-05-12 00:00:00', 'Cambio Manual', 3),
(16, 1, '123456', '2025-05-15 05:00:00', 'Admin', 1),
(17, 2, '123456', '2025-05-16 05:00:00', 'Cambio Manual', 2),
(18, 2, '', '2025-05-16 05:00:00', 'Cambio Manual', 2),
(19, 2, '', '2025-05-16 05:00:00', 'Cambio Manual', 2),
(20, 2, '123456', '2025-05-18 05:00:00', 'Cambio Manual', 2),
(21, 3, '12345678', '2025-05-18 05:00:00', 'Admin', 2),
(22, 2, '60fe74406e7f353ed979f350f2fbb6a2e8690a5fa7d1b0c32983d1d8b3f95f67', '2025-05-18 05:00:00', 'Cambio Manual', 2),
(23, 3, '3498be6f5b7e15bd81f0a5fc0d14527de0a1b3afaf440d5abf82dddcf0509cc8', '2025-05-18 05:00:00', 'Cambio Manual', 1),
(24, 3, '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2', '2025-05-20 05:00:00', 'Admin', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `meta`
--

CREATE TABLE `meta` (
  `id` int(11) NOT NULL,
  `nombre_meta` varchar(100) NOT NULL,
  `id_categoria` int(11) DEFAULT NULL,
  `Importe` decimal(18,7) DEFAULT NULL,
  `fecha_creacion` date DEFAULT NULL,
  `Fecha_fin` date DEFAULT NULL,
  `Estado` varchar(100) NOT NULL,
  `Importe_inicial_e` decimal(18,7) NOT NULL,
  `Importe_Final_e` decimal(18,7) NOT NULL,
  `Importe_inicial_r` decimal(18,7) NOT NULL,
  `Importe_final_r` decimal(18,7) NOT NULL,
  `Situacion` varchar(10) NOT NULL,
  `Comentario` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `meta`
--

INSERT INTO `meta` (`id`, `nombre_meta`, `id_categoria`, `Importe`, `fecha_creacion`, `Fecha_fin`, `Estado`, `Importe_inicial_e`, `Importe_Final_e`, `Importe_inicial_r`, `Importe_final_r`, `Situacion`, `Comentario`) VALUES
(12, 'COMPRA DE AUTO', 4, '12000.0000000', '2025-06-26', '2025-07-09', 'PENDIENTE', '0.0000000', '12000.0000000', '0.0000000', '800.0000000', 'A', 'ESTO ES DE PRUEBA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `meta_aporte`
--

CREATE TABLE `meta_aporte` (
  `id` int(11) NOT NULL,
  `id_meta` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `Aporte_Estimado` decimal(18,7) NOT NULL,
  `aporte_real` decimal(18,7) DEFAULT NULL,
  `Fecha_Registro` date NOT NULL,
  `Situacion` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `meta_aporte`
--

INSERT INTO `meta_aporte` (`id`, `id_meta`, `id_usuario`, `Aporte_Estimado`, `aporte_real`, `Fecha_Registro`, `Situacion`) VALUES
(85, 12, 12, '500.0000000', '300.0000000', '2025-06-26', 'A'),
(87, 12, 12, '500.0000000', '500.0000000', '2025-07-09', 'A');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `meta_categoria`
--

CREATE TABLE `meta_categoria` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `situacion` varchar(3) NOT NULL DEFAULT 'A'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `meta_categoria`
--

INSERT INTO `meta_categoria` (`id`, `nombre`, `descripcion`, `situacion`) VALUES
(1, 'Educación', 'Metas relacionadas con educación y formación', 'A'),
(2, 'Salud', 'Metas relacionadas con salud y bienestar', 'A'),
(3, 'Vivienda', 'Metas relacionadas con compra, mejora o mantenimiento de vivienda', 'A'),
(4, 'Transporte', 'Metas relacionadas con vehículos y transporte', 'A'),
(5, 'Ocio', 'Metas relacionadas con vacaciones y entretenimiento', 'A'),
(6, 'Emergencias', 'Fondos para emergencias y ahorro preventivo', 'A'),
(7, 'Vacaciones', 'Viajes y mas ....', 'I'),
(9, 'vacas', 'vacas y mas vacas', 'A');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `meta_prioridad`
--

CREATE TABLE `meta_prioridad` (
  `id` int(11) NOT NULL,
  `id_meta` int(11) NOT NULL,
  `prioridad` int(11) NOT NULL,
  `Situacion` varchar(10) NOT NULL,
  `porcentaje` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `meta_prioridad`
--

INSERT INTO `meta_prioridad` (`id`, `id_meta`, `prioridad`, `Situacion`, `porcentaje`) VALUES
(1, 12, 1, 'A', 100);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `meta_traslados`
--

CREATE TABLE `meta_traslados` (
  `id` int(11) NOT NULL,
  `id_meta_origen` int(11) NOT NULL,
  `id_meta_destino` int(11) NOT NULL,
  `monto` decimal(18,7) NOT NULL,
  `fecha_traslado` datetime NOT NULL,
  `id_usuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `meta_traslados`
--

INSERT INTO `meta_traslados` (`id`, `id_meta_origen`, `id_meta_destino`, `monto`, `fecha_traslado`, `id_usuario`) VALUES
(0, 12, 1, '200.0000000', '2025-06-26 13:21:54', 1),
(1, 1, 8, '1120.0000000', '2025-06-12 23:33:27', 1),
(2, 1, 9, '1120.0000000', '2025-06-13 00:12:56', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `Situacion` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`id`, `nombre`, `Situacion`) VALUES
(1, 'Administrador', 'A'),
(2, 'Integrante Familiar', 'A'),
(3, 'Coordinador de Finanzas', 'A');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_documento`
--

CREATE TABLE `tipo_documento` (
  `id` int(11) NOT NULL,
  `Nombre` varchar(100) NOT NULL,
  `Situacion` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `tipo_documento`
--

INSERT INTO `tipo_documento` (`id`, `Nombre`, `Situacion`) VALUES
(1, 'DNI', 'A'),
(2, 'CE', 'A');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `id_rol` int(11) DEFAULT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido` varchar(100) NOT NULL,
  `ID_Tipo_documento` int(11) NOT NULL,
  `Numero_doc` varchar(15) NOT NULL,
  `sexo` varchar(5) NOT NULL,
  `Situacion` varchar(3) NOT NULL,
  `Contrasenia` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `id_rol`, `nombre`, `apellido`, `ID_Tipo_documento`, `Numero_doc`, `sexo`, `Situacion`, `Contrasenia`) VALUES
(1, 1, 'ADMIH', 'ADMIN', 1, '1', 'F', 'A', '1'),
(2, 1, 'admin', 'admin', 1, '1234567910', 'M', 'A', '60fe74406e7f353ed979f350f2fbb6a2e8690a5fa7d1b0c32983d1d8b3f95f67'),
(3, 1, 'alondra', 'aguilar', 1, '12345636', 'F', 'I', '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2'),
(4, 1, 'misae', 'challco', 1, '71345668', 'M', 'I', NULL),
(5, 1, 'misae', 'challco', 2, '2', 'm', 'I', '121'),
(7, 1, 'Juan', 'Pérez', 1, '12345678', 'M', 'A', 'pass123'),
(8, 1, 'María', 'Gómez', 1, '87654321', 'F', 'ACT', 'pass456'),
(9, 2, 'Carlos', 'Ramírez', 1, '11223344', 'M', 'ACT', 'pass789'),
(10, 1, 'juanca', 'juanca', 1, '12345678', 'm', 'A', NULL),
(11, 2, 'Juan ', 'Perez', 1, '22446688', 'M', 'I', NULL),
(12, 2, 'CRISTINA', 'AGUILAR', 1, '12345678', 'F', 'A', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_ingreso`
--

CREATE TABLE `usuario_ingreso` (
  `id` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `Aporte` decimal(18,7) NOT NULL,
  `Situacion` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario_ingreso`
--

INSERT INTO `usuario_ingreso` (`id`, `id_usuario`, `Aporte`, `Situacion`) VALUES
(1, 3, '900.0000000', 'A'),
(2, 12, '500.0000000', 'A');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `fuentes_ingreso`
--
ALTER TABLE `fuentes_ingreso`
  ADD PRIMARY KEY (`id_ingreso`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `gestion_contrasena`
--
ALTER TABLE `gestion_contrasena`
  ADD PRIMARY KEY (`id_gestion`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `realizado_por` (`realizado_por`);

--
-- Indices de la tabla `meta`
--
ALTER TABLE `meta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_meta_categoria` (`id_categoria`);

--
-- Indices de la tabla `meta_aporte`
--
ALTER TABLE `meta_aporte`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `meta_categoria`
--
ALTER TABLE `meta_categoria`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `meta_prioridad`
--
ALTER TABLE `meta_prioridad`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `meta_traslados`
--
ALTER TABLE `meta_traslados`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_traslado_origen` (`id_meta_origen`),
  ADD KEY `fk_traslado_destino` (`id_meta_destino`),
  ADD KEY `fk_traslado_usuario` (`id_usuario`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `tipo_documento`
--
ALTER TABLE `tipo_documento`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario_ingreso`
--
ALTER TABLE `usuario_ingreso`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `fuentes_ingreso`
--
ALTER TABLE `fuentes_ingreso`
  MODIFY `id_ingreso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT de la tabla `gestion_contrasena`
--
ALTER TABLE `gestion_contrasena`
  MODIFY `id_gestion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de la tabla `meta`
--
ALTER TABLE `meta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `meta_aporte`
--
ALTER TABLE `meta_aporte`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=88;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `usuario_ingreso`
--
ALTER TABLE `usuario_ingreso`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

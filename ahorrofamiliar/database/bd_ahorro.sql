-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-04-2025 a las 19:43:28
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
-- Base de datos: `bd_ahorro`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `meta`
--

CREATE TABLE `meta` (
  `id` int(11) NOT NULL,
  `nombre_meta` varchar(100) NOT NULL,
  `fecha_creacion` date DEFAULT NULL,
  `Fecha_fin` date DEFAULT NULL,
  `Estado` varchar(100) NOT NULL,
  `Importe_inicial_e` decimal(18,7) NOT NULL,
  `Importe_Final_e` decimal(18,7) NOT NULL,
  `Importe_inicial_r` decimal(18,7) NOT NULL,
  `Importe_final_r` decimal(18,7) NOT NULL,
  `Situacion` decimal(18,7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `meta_aporte`
--

CREATE TABLE `meta_aporte` (
  `id` int(11) NOT NULL,
  `id_meta` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `Aporte_Estimado` decimal(18,7) NOT NULL,
  `Aporte Real` decimal(18,7) NOT NULL,
  `Fecha_Registro` date NOT NULL,
  `Situacion` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `id_rol` int(11) DEFAULT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido` varchar(100) NOT NULL,
  `Tipo_documento` varchar(30) NOT NULL,
  `Numero_doc` varchar(15) NOT NULL,
  `sexo` varchar(5) NOT NULL,
  `Situacion` varchar(3) NOT NULL,
  `Contrasenia` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `meta`
--
ALTER TABLE `meta`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `meta_aporte`
--
ALTER TABLE `meta_aporte`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `meta_prioridad`
--
ALTER TABLE `meta_prioridad`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
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
-- AUTO_INCREMENT de la tabla `meta`
--
ALTER TABLE `meta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `meta_aporte`
--
ALTER TABLE `meta_aporte`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `meta_prioridad`
--
ALTER TABLE `meta_prioridad`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuario_ingreso`
--
ALTER TABLE `usuario_ingreso`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-04-2024 a las 16:48:43
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `cloudatordb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `files`
--

CREATE TABLE `files` (
  `id` bigint(20) NOT NULL,
  `filename` varchar(128) NOT NULL,
  `filetype` varchar(128) NOT NULL,
  `fileroute` varchar(512) NOT NULL,
  `filedate` timestamp NOT NULL DEFAULT current_timestamp(),
  `filesize` bigint(20) NOT NULL,
  `owner` bigint(20) NOT NULL,
  `ispublic` tinyint(1) NOT NULL,
  `url` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `files`
--

INSERT INTO `files` (`id`, `filename`, `filetype`, `fileroute`, `filedate`, `filesize`, `owner`, `ispublic`, `url`) VALUES
(16, 'CRASHER.txt', 'text/plain', 'E:\\Eduardo\\pruebadescargas\\3\\', '2024-03-08 16:30:40', 57277, 3, 1, ''),
(18, 'CRASHER.txt', 'text/plain', 'E:\\Eduardo\\pruebadescargas\\2\\', '2024-03-08 16:40:19', 57277, 2, 1, ''),
(19, 'cloudatordb (11).sql', 'application/x-sql', 'E:\\Eduardo\\pruebadescargas\\2\\', '2024-03-08 16:40:31', 6426, 2, 1, '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_SUPERADMIN'),
(2, 'ROLE_ADMIN'),
(3, 'ROLE_PREMIUM'),
(4, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(64) NOT NULL,
  `email` varchar(64) NOT NULL,
  `creationDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `version` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `email`, `creationDate`, `version`) VALUES
(2, 'admingod', '$2a$10$pYv4bDY/B4uey5BfVM26JOLu2/BGqZ5xZGEEWuwb.UMZ0ld9vemJq', 'admingod@gmail.com', '2024-02-08 14:21:31', NULL),
(3, 'Baker', '$2a$10$H0H3pe2nzK2cMTaY4Eesxu7kccJvkjoP2KRtruIOMHwQg9xAIbaXW', 'hestrada498111@gmail.com', '2024-03-05 15:12:32', NULL),
(4, 'Buabua', '$2a$10$WFacN2vJ/Zgq7zxyA6CKxeOF7xi5CAqhvPe..JICV16.kIyVg3huO', 'buabua@gmail.com', '2024-03-05 15:17:57', NULL),
(5, 'marc', '$2a$10$yKiXX3vjE7aa00zRwNwzg.AktReb5rrQFgHUcNY5G/CWc3PWwjCk.', 'marc@gmail.com', '2024-03-07 14:37:15', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users_roles`
--

CREATE TABLE `users_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `users_roles`
--

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES
(2, 1),
(3, 4),
(4, 4),
(5, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_access`
--

CREATE TABLE `user_access` (
  `id` bigint(20) NOT NULL,
  `counter` int(11) DEFAULT NULL,
  `isblocked` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `user_access`
--

INSERT INTO `user_access` (`id`, `counter`, `isblocked`) VALUES
(2, 0, 0),
(3, 0, 0),
(4, 0, 0),
(5, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_info`
--

CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `surname` varchar(32) DEFAULT NULL,
  `birthday` varchar(255) DEFAULT NULL,
  `address` varchar(32) DEFAULT NULL,
  `foto` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `user_info`
--

INSERT INTO `user_info` (`id`, `name`, `surname`, `birthday`, `address`, `foto`) VALUES
(2, 'EDUARDO1', 'FORRO', '2024-03-02', 'Forro1', NULL),
(3, 'N/A', 'N/A', '2024-03-07', 'N/A', NULL),
(4, 'Buabua', 'Buababú', '2023-09-12', 'Río Alandor', NULL),
(5, NULL, NULL, NULL, NULL, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `files`
--
ALTER TABLE `files`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `existingfile` (`id`,`owner`),
  ADD UNIQUE KEY `filename_3` (`filename`,`owner`),
  ADD KEY `owner` (`owner`),
  ADD KEY `id` (`id`),
  ADD KEY `filename` (`filename`),
  ADD KEY `filename_2` (`filename`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `username` (`username`) USING BTREE,
  ADD KEY `username_2` (`username`);

--
-- Indices de la tabla `users_roles`
--
ALTER TABLE `users_roles`
  ADD KEY `user_id` (`user_id`),
  ADD KEY `role_id` (`role_id`);

--
-- Indices de la tabla `user_access`
--
ALTER TABLE `user_access`
  ADD UNIQUE KEY `id_2` (`id`),
  ADD KEY `id` (`id`);

--
-- Indices de la tabla `user_info`
--
ALTER TABLE `user_info`
  ADD KEY `id` (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `files`
--
ALTER TABLE `files`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `files`
--
ALTER TABLE `files`
  ADD CONSTRAINT `files_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `users_roles`
--
ALTER TABLE `users_roles`
  ADD CONSTRAINT `users_roles_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `users_roles_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `user_access`
--
ALTER TABLE `user_access`
  ADD CONSTRAINT `user_access_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `user_info`
--
ALTER TABLE `user_info`
  ADD CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

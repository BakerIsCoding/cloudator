-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-12-2023 a las 15:38:30
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4

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
  `filename` varchar(128) NOT NULL,
  `filetype` varchar(16) NOT NULL,
  `fileroute` varchar(512) NOT NULL,
  `filedate` timestamp NOT NULL DEFAULT current_timestamp(),
  `filesize` decimal(10,0) NOT NULL,
  `owner` varchar(32) NOT NULL,
  `ispublic` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` int(32) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(2, 'ROLE_ADMIN'),
(3, 'ROLE_PREMIUM'),
(1, 'ROLE_SUPERADMIN'),
(4, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` int(32) NOT NULL,
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
(1, 'Test', '$2a$10$xQm2wvJEMx4tW8kpqSlHD.xdj.tmG4bqEmfHv3swNBhUsPhGEcODa', 'test@gmail.com', '2023-11-20 16:04:50', NULL),
(2, 'TestNew', '$2a$10$T1kziX67THR31bU5FvIt1uLSDYZrNhH88VtffXrVM7eZSafbfp7Tu', 'testnew@gmail.com', '2023-11-28 15:40:21', NULL),
(3, 'TestUserAccess', '$2a$10$L57EurLcYEReskTlxVF5V.8PgWROPa7nOLAqpdACkdc09J7K/4qa.', 'testuseraccess@gmail.com', '2023-11-28 15:42:09', NULL),
(4, 'TestUserAccess1', '$2a$10$zfnVBkvoEuUA8UKjMcTQ4euH/X/bR27uf4m7f70EA5rhJx7L9WvFC', 'testuseraccess1@gmail.com', '2023-11-30 14:39:38', NULL),
(5, 'TestUserAccess2', '$2a$10$UlSB1rwb13OSELDL4.7aKug5cE50Zb5iiw7z4eFzj.4wB5R.1qhgG', 'testuseraccess2@gmail.com', '2023-12-01 15:28:57', NULL),
(6, 'TestUserAccess5', '$2a$10$rVQTOoLYpHJIeJpw9HQQFeMBZ2Yt7B6kx9fJBQCbp1Ja62W6o3zd.', 'hestradafdsafda498111@gmail.com', '2023-12-01 15:32:37', NULL),
(7, 'TestUserAccess3', '$2a$10$qUSbfK1SY3wmhEPlRM9NNOJEULIbWjkhGIhGFTxcUnGK7.eHzitd6', 'testuseraccess3@gmail.com', '2023-12-04 15:12:03', NULL),
(8, 'admingod', '$2a$10$E4HTzCyMUqNXbzCZQNM8becV21uE0Nci1997sG9VHpyWnAYNEyWmu', 'admingod@gmail.com', '2023-12-04 16:26:14', NULL),
(12, 'mar', '$2a$10$5etEV0E8/2X5x.R58nUVs.JlM07.4gNMI.v3DurLBbNzwEj8ZAwlG', 'mar@gmail.com', '2023-12-05 15:17:00', NULL),
(13, 'mar2', '$2a$10$Co59P47acTU5xugOEoxs2ufjdBaJD0OOlqbzOF9zOA1TdwtZKwCKa', 'mar2@gmail.com', '2023-12-05 15:46:18', NULL),
(14, 'fdsafa', '$2a$10$wppoDR1aEdGThe7Yt/Ir4uJrvGqUlXnOa3TOMr8pTifMw7GiBzsOy', 'fdsafa@gmail.comfdas', '2023-12-05 16:00:34', NULL),
(15, 'NuevoUser', '$2a$10$lnNo5rHj1PV5nAkf/OHiA.eKlD5BG6W7kzD39XrII1OAVJIMe4G6e', 'nuevouser@gmail.com', '2023-12-06 18:30:21', NULL),
(16, 'usuario1@gmail.com', '$2a$10$W2nQ7AfQJaY8Hg9STprHxO2m1gSBWJF.snJYDGuxxk3B7MLA0hpHO', 'usuario@gmail.com', '2023-12-07 09:36:02', NULL),
(17, 'buenastardes', '$2a$10$dBNQJNRkLkV1LW/cZ7xkTuOfyVCDYIm.urLEdM6lXEE7cr/Q9yKvW', 'buenastardes@gmail.com', '2023-12-07 12:28:26', NULL),
(18, 'nn3', '$2a$10$rjc0363YEJThTKQXdmDLOe5wWUmnA9yIQr0iRiBFGQRuJtU7o/3am', 'nn3@gmail.com', '2023-12-11 15:27:50', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users_roles`
--

CREATE TABLE `users_roles` (
  `user_id` int(32) NOT NULL,
  `role_id` int(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `users_roles`
--

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 4),
(3, 4),
(4, 4),
(5, 4),
(6, 4),
(7, 4),
(8, 1),
(12, 4),
(13, 4),
(14, 4),
(15, 4),
(16, 4),
(17, 4),
(18, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_access`
--

CREATE TABLE `user_access` (
  `id` int(32) NOT NULL,
  `counter` smallint(1) NOT NULL,
  `isblocked` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `user_access`
--

INSERT INTO `user_access` (`id`, `counter`, `isblocked`) VALUES
(3, 1, 0),
(4, 2, 1),
(5, 0, 0),
(6, 2, 1),
(7, 2, 1),
(8, 0, 0),
(12, 1, 0),
(13, 2, 1),
(14, 2, 1),
(15, 2, 0),
(16, 0, 1),
(17, 0, 0),
(18, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_seq`
--

CREATE TABLE `user_seq` (
  `next_val` int(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `files`
--
ALTER TABLE `files`
  ADD UNIQUE KEY `existingfile` (`filename`,`owner`),
  ADD KEY `owner` (`owner`),
  ADD KEY `filename` (`filename`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ofx66keruapi6vyqpv6f2or37` (`name`);

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
  ADD KEY `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indices de la tabla `user_access`
--
ALTER TABLE `user_access`
  ADD UNIQUE KEY `id_2` (`id`),
  ADD KEY `id` (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` int(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `files`
--
ALTER TABLE `files`
  ADD CONSTRAINT `files_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `files_ibfk_2` FOREIGN KEY (`filename`) REFERENCES `files` (`owner`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `users_roles`
--
ALTER TABLE `users_roles`
  ADD CONSTRAINT `FKgd3iendaoyh04b95ykqise6qh` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

--
-- Filtros para la tabla `user_access`
--
ALTER TABLE `user_access`
  ADD CONSTRAINT `id` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

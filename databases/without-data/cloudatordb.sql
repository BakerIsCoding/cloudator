-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-05-2024 a las 23:23:05
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
(7, 'FiddlerSetup.5.0.20242.10753-latest.exe', 'application/x-msdownload', 'E:\\Eduardo\\pruebadescargas\\4\\', '2024-05-09 13:30:51', 4632256, 4, 1, 'http://localhost:8080/download?owner=d2hCRFg3eG9uOS9RakF1RThiNmxYUy9OQUt3bGtTd2pNcnhKVWJWUk85SGsyV3RwaWVQWVZ1ZUxsSlZScUd1aA==&filename=dzgvWGZpSnZ2SHZDMERSUjFFTE9hUHhYWTZaQ1B2UnVXOTJaVFMyU0NxMVl5MWRwSGNhb2k2Vm0ycHJDUTJGb2FDUzd0OVZiV3lOaFJmSiszNjNFTUJFZCtENys4c2RDZEJvcWFkRFc0QXM9'),
(9, 'carnetgod.txt', 'text/plain', 'E:\\Eduardo\\pruebadescargas\\3\\', '2024-05-21 13:55:20', 8, 3, 1, 'http://management-pants.gl.at.ply.gg:27118/download?owner=WHpQMjVhMXRwUXNJN1NZdmU5VmtTTUcrMzl2SzRjNTMveGg5cHlUL0FwTzRBM3hrRDVtb2tpZ2hCbmI2WnNtWA==&filename=Y3AzbEc0b3M5cTk1dzM3VjdSVkpha25SQVJFMTBmMEpGaC9GL2RqTFdYbGI4TzIvNUxnYUpURTVuUkZMUjc5Ng==');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `max_storage` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `name`, `max_storage`) VALUES
(1, 'ROLE_SUPERADMIN', 1073741824000),
(2, 'ROLE_ADMIN', 1073741824000),
(3, 'ROLE_PREMIUM', 107374182400),
(4, 'ROLE_USER', 10737418240);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `password` varchar(64) NOT NULL,
  `email` varchar(64) NOT NULL,
  `creationDate` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `email`, `creationDate`) VALUES
(1, 'admin', '$2a$10$M.ZcKUWP4MNCsz8o/POdCOFJCHg8d8KzqIkITqpWqQiIYcKNO3zRS', 'admin@cloudator.live', '2024-04-19 15:02:13'),
(3, 'furrykiller24', '$2a$10$8HJPUWbdczOX4xahOfijJejjzmTC5uMsIKnbjJbHCRoM0wIcZYn/2', 'furrykiller24@gmail.com', '2024-04-26 15:03:55'),
(4, 'Marc Pedreño', '$2a$10$SkiO9C7/QUEOuw/q0anFM.AJIXnQmWNo8GpKZ0GtubIapoaoNeTcW', 'marc639@outlook.com', '2024-04-29 14:34:51'),
(5, 'Buabua', '$2a$10$ojY9I82vm5GFVqIyjkHmvOIe4WWxAbYGakKXpkLpZGNYns4xJn6Ii', 'buabua@gmail.com', '2024-05-02 14:11:22'),
(6, 'Baker', '$2a$10$GcxR9WtvaeRqU9if45CwhegohbijB5YqzOcEb8BrBLJHatV9nha5C', 'hestrada49811fadd1@gmail.com', '2024-05-10 13:23:57'),
(7, 'eliminacion', '$2a$10$cFnP2/pve5MXys6H.LfZ.ONkTGrpPWE3K38jBKEUugOFlUVRfs3mi', 'eliminacion@gmail.com', '2024-05-14 13:52:28');

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
(1, 1),
(3, 2),
(4, 4),
(5, 2),
(6, 4),
(7, 4);

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
(1, 0, 0),
(3, 0, 0),
(4, 0, 0),
(5, 0, 0),
(6, 0, 0),
(7, 0, 0);

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
(1, NULL, NULL, NULL, NULL, NULL),
(3, NULL, NULL, NULL, NULL, NULL),
(4, NULL, NULL, NULL, NULL, 'http://management-pants.gl.at.ply.gg:27118/upload/4/pfpic'),
(5, NULL, NULL, NULL, NULL, 'http://management-pants.gl.at.ply.gg:27118/upload/5/pfp/profile.jpg'),
(6, NULL, NULL, NULL, NULL, NULL),
(7, NULL, NULL, NULL, NULL, NULL);

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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

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

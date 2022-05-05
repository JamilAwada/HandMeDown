-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 05, 2022 at 07:08 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `handmedown_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `listing`
--

CREATE TABLE `listing` (
  `id` int(10) NOT NULL,
  `title` varchar(64) NOT NULL,
  `description` varchar(512) NOT NULL,
  `price` varchar(64) NOT NULL,
  `category` varchar(64) NOT NULL,
  `posted_on` date NOT NULL,
  `seller` varchar(64) NOT NULL,
  `seller_name` varchar(64) DEFAULT NULL,
  `picture` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `listing`
--

INSERT INTO `listing` (`id`, `title`, `description`, `price`, `category`, `posted_on`, `seller`, `seller_name`, `picture`) VALUES(248645, 'Teddy Bear', 'Gently used stuffed teddy bear. Very fluffy!', '20', 'Toys', '2022-05-05', '497583', 'Jamil Awada', 'DEMO: Bear');
INSERT INTO `listing` (`id`, `title`, `description`, `price`, `category`, `posted_on`, `seller`, `seller_name`, `picture`) VALUES(419094, 'Honey Formula', 'Rich in vitamins and minerals for your child to grow. About 1kg left', '50', 'Consumables', '2022-05-05', '291722', 'Abir Gabriel', 'DEMO: Formula');
INSERT INTO `listing` (`id`, `title`, `description`, `price`, `category`, `posted_on`, `seller`, `seller_name`, `picture`) VALUES(434050, 'Big Stroller', 'Heavy duty stroller to carry your fat kids', '200', 'Gear', '2022-05-05', '74512', 'Karim Sardouk', 'DEMO: Stroller');
INSERT INTO `listing` (`id`, `title`, `description`, `price`, `category`, `posted_on`, `seller`, `seller_name`, `picture`) VALUES(493536, 'White Onesie', 'White pyjama, small size, very clean.', '10', 'Clothing', '2022-05-05', '291722', 'Abir Gabriel', 'DEMO: Onesie');
INSERT INTO `listing` (`id`, `title`, `description`, `price`, `category`, `posted_on`, `seller`, `seller_name`, `picture`) VALUES(596047, 'Baby monitor', '', '5', 'Electronics', '2022-05-05', '497583', 'Jamil Awada', 'DEMO: Monitor');
INSERT INTO `listing` (`id`, `title`, `description`, `price`, `category`, `posted_on`, `seller`, `seller_name`, `picture`) VALUES(745596, 'Small Stroller', '', '50', 'Gear', '2022-05-05', '74512', 'Karim Sardouk', 'DEMO: Stroller');
INSERT INTO `listing` (`id`, `title`, `description`, `price`, `category`, `posted_on`, `seller`, `seller_name`, `picture`) VALUES(865075, 'Pampers', 'Never used before (Obviously lol)\nVery soft and absorbant. I happened to have extra.', '2', 'Disposables', '2022-05-05', '849531', 'Susan Smith', 'DEMO: Diapers');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `number` varchar(64) NOT NULL,
  `address` varchar(512) NOT NULL,
  `username` varchar(32) NOT NULL,
  `email` varchar(64) NOT NULL,
  `password` text NOT NULL,
  `picture` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `name`, `number`, `address`, `username`, `email`, `password`, `picture`) VALUES(74512, 'Karim Sardouk', '72839123', 'Khaldeh, Dawhet El Hoss', 'KookiePookie', 'karimtheking@hotmail.com', '$2y$10$XZrMeLvXCtfE6QAAezOEduwBe0On1EsxdJB/RLukzy3NzfBoNqezu', 'DEMO: Man 2');
INSERT INTO `user` (`id`, `name`, `number`, `address`, `username`, `email`, `password`, `picture`) VALUES(291722, 'Abir Gabriel', '03103932', 'Aramoun', 'Gabi33', 'abir.gabriel@yahoo.com', '$2y$10$rqiYsBZ4hn9SJDCe6isSFecYVNgWzbnLrox4ze87TTR0dJcgEi2xm', NULL);
INSERT INTO `user` (`id`, `name`, `number`, `address`, `username`, `email`, `password`, `picture`) VALUES(497583, 'Jamil Awada', '71202081', 'Beirut, Burj El Barajneh', 'JamilAwada123', 'jamil.awada@lau.edu', '$2y$10$OfnECaYrurbPqefXpx5zKuSUPQJH5jadignRGHi.2A9OnfvZngXnG', 'DEMO: Man 1');
INSERT INTO `user` (`id`, `name`, `number`, `address`, `username`, `email`, `password`, `picture`) VALUES(849531, 'Susan Smith', '03839231', 'Beirut, Hamra', 'SuperMom12', 'smith.susan@gmail.com', '$2y$10$/FuruLaCr09NujpBBYQA6Oy9gyt675OeNFFsouO57ERI0L2T43zhS', 'DEMO: Woman 2');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `listing`
--
ALTER TABLE `listing`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `phone_number` (`number`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `listing`
--
ALTER TABLE `listing`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2147483648;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(64) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2147483648;
COMMIT;

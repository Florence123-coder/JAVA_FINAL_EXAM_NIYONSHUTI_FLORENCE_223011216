-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3308
-- Generation Time: Dec 16, 2025 at 07:07 AM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `retailer_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE IF NOT EXISTS `customer` (
  `CustomerID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `PasswordHash` varchar(255) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `FullName` varchar(100) DEFAULT NULL,
  `Role` varchar(50) DEFAULT NULL,
  `CreatedAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `LastLogin` datetime DEFAULT NULL,
  PRIMARY KEY (`CustomerID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`CustomerID`, `Username`, `PasswordHash`, `Email`, `FullName`, `Role`, `CreatedAt`, `LastLogin`) VALUES
(1, 'florence', '123', 'florence@email.com', 'Niyonshuti Florence', 'Customer', '2025-10-15 16:26:05', NULL),
(3, 'belyse', '123', 'belyse@gmail.com', 'umutoni belyse', 'customer', '2025-10-21 11:31:05', NULL),
(4, 'ange', '234', 'ange@gmail.com', 'uwase ange', 'customer', '2025-10-21 17:10:05', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `demo`
--

DROP TABLE IF EXISTS `demo`;
CREATE TABLE IF NOT EXISTS `demo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `demo`
--

INSERT INTO `demo` (`id`, `name`, `price`) VALUES
(1, 'Rice', 1200),
(2, 'Pasta', 1500),
(3, 'Salt', 500),
(4, 'Maize Flour', 2000),
(5, 'Soap', 800),
(6, 'Rice', 1200),
(7, 'Pasta', 1500),
(8, 'Salt', 500),
(10, 'Soap', 800);

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
CREATE TABLE IF NOT EXISTS `inventory` (
  `InventoryID` int NOT NULL AUTO_INCREMENT,
  `ProductID` int DEFAULT NULL,
  `StoreID` int DEFAULT NULL,
  `Quantity` int DEFAULT '0',
  `CreatedAt` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`InventoryID`),
  KEY `ProductID` (`ProductID`),
  KEY `StoreID` (`StoreID`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`InventoryID`, `ProductID`, `StoreID`, `Quantity`, `CreatedAt`) VALUES
(1, 204, 30, 50, '2025-10-15 16:27:28'),
(2, 101, 102, 100, '2025-10-15 16:27:28');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE IF NOT EXISTS `orders` (
  `OrderID` int NOT NULL AUTO_INCREMENT,
  `CustomerID` int NOT NULL,
  `ProductID` int NOT NULL,
  `Quantity` int NOT NULL,
  `TotalPrice` decimal(10,2) NOT NULL,
  `OrderDate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`OrderID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`OrderID`, `CustomerID`, `ProductID`, `Quantity`, `TotalPrice`, `OrderDate`) VALUES
(1, 1, 1, 2, 5.00, '2025-11-01 09:30:00'),
(2, 1, 2, 1, 1.80, '2025-11-01 10:15:00'),
(3, 2, 3, 5, 6.00, '2025-11-02 11:20:00'),
(4, 2, 1, 3, 7.50, '2025-11-02 12:10:00'),
(5, 3, 2, 2, 3.60, '2025-11-03 14:45:00');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
CREATE TABLE IF NOT EXISTS `product` (
  `ProductID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Description` text,
  `Category` varchar(50) DEFAULT NULL,
  `PriceOrValue` decimal(10,2) NOT NULL,
  `Status` varchar(50) DEFAULT NULL,
  `CreatedAt` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ProductID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`ProductID`, `Name`, `Description`, `Category`, `PriceOrValue`, `Status`, `CreatedAt`) VALUES
(1, 'Soap', 'Body soap', 'Hygiene', 8000.00, 'Available', '2025-10-15 16:26:49'),
(2, 'rice', 'eating', 'tanzania', 12000.00, 'Available', '2025-10-15 16:26:49'),
(3, 'oil', 'eating oil', 'sunlight', 20000.00, 'Available', '2025-12-15 08:44:04');

-- --------------------------------------------------------

--
-- Table structure for table `productsupplier`
--

DROP TABLE IF EXISTS `productsupplier`;
CREATE TABLE IF NOT EXISTS `productsupplier` (
  `ProductID` int NOT NULL,
  `SupplierID` int NOT NULL,
  PRIMARY KEY (`ProductID`,`SupplierID`),
  KEY `SupplierID` (`SupplierID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `productsupplier`
--

INSERT INTO `productsupplier` (`ProductID`, `SupplierID`) VALUES
(1, 1),
(102, 103);

-- --------------------------------------------------------

--
-- Table structure for table `retailer_system_suppliers`
--

DROP TABLE IF EXISTS `retailer_system_suppliers`;
CREATE TABLE IF NOT EXISTS `retailer_system_suppliers` (
  `SupplierID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Contact` varchar(50) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`SupplierID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `retailer_system_suppliers`
--

INSERT INTO `retailer_system_suppliers` (`SupplierID`, `Name`, `Contact`, `Email`, `CreatedAt`) VALUES
(1, 'Supplier One', '0788123456', 'supplier1@example.com', '2025-11-05 08:46:27'),
(2, 'Supplier Two', '0788234567', 'supplier2@example.com', '2025-11-05 08:46:27'),
(3, 'Supplier Three', '0788345678', 'ange@2.com', '2025-11-05 08:46:27');

-- --------------------------------------------------------

--
-- Table structure for table `sale`
--

DROP TABLE IF EXISTS `sale`;
CREATE TABLE IF NOT EXISTS `sale` (
  `SaleID` int NOT NULL AUTO_INCREMENT,
  `InventoryID` int DEFAULT NULL,
  `CustomerID` int DEFAULT NULL,
  `OrderNumber` varchar(50) DEFAULT NULL,
  `Date` datetime DEFAULT CURRENT_TIMESTAMP,
  `Status` varchar(50) DEFAULT NULL,
  `TotalAmount` decimal(10,2) DEFAULT NULL,
  `PaymentMethod` varchar(50) DEFAULT NULL,
  `Notes` text,
  PRIMARY KEY (`SaleID`),
  KEY `InventoryID` (`InventoryID`),
  KEY `CustomerID` (`CustomerID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sale`
--

INSERT INTO `sale` (`SaleID`, `InventoryID`, `CustomerID`, `OrderNumber`, `Date`, `Status`, `TotalAmount`, `PaymentMethod`, `Notes`) VALUES
(1, 1, 1, 'ORD001', '2025-10-15 16:29:47', 'Completed', 5000.00, 'Cash', 'Successful sale');

-- --------------------------------------------------------

--
-- Table structure for table `salesreport`
--

DROP TABLE IF EXISTS `salesreport`;
CREATE TABLE IF NOT EXISTS `salesreport` (
  `SaleID` int NOT NULL AUTO_INCREMENT,
  `ProductID` int NOT NULL,
  `Quantity` int NOT NULL,
  `TotalPrice` decimal(10,2) NOT NULL,
  `SaleDate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`SaleID`),
  KEY `ProductID` (`ProductID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `salesreport`
--

INSERT INTO `salesreport` (`SaleID`, `ProductID`, `Quantity`, `TotalPrice`, `SaleDate`) VALUES
(1, 1, 5, 12.50, '2025-10-15 10:30:00'),
(2, 2, 3, 5.40, '2025-10-22 14:15:00'),
(3, 3, 10, 12.00, '2025-10-22 15:00:00'),
(4, 1, 2, 5.00, '2025-11-01 09:20:00'),
(5, 2, 4, 7.20, '2025-11-02 12:45:00');

-- --------------------------------------------------------

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
CREATE TABLE IF NOT EXISTS `store` (
  `StoreID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Address` varchar(150) DEFAULT NULL,
  `Capacity` int DEFAULT NULL,
  `Manager` varchar(100) DEFAULT NULL,
  `Contact` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`StoreID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `store`
--

INSERT INTO `store` (`StoreID`, `Name`, `Address`, `Capacity`, `Manager`, `Contact`) VALUES
(1, 'Huye Retail Shop', 'Huye City', 200, 'Alice', '078111222');

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
CREATE TABLE IF NOT EXISTS `supplier` (
  `SupplierID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) DEFAULT NULL,
  `Contact` varchar(50) DEFAULT NULL,
  `Address` varchar(150) DEFAULT NULL,
  `CreatedAt` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`SupplierID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`SupplierID`, `Name`, `Contact`, `Address`, `CreatedAt`) VALUES
(1, 'Kigali Supply Ltd', '0781234567', 'Kigali', '2025-10-15 16:27:50'),
(2, 'florence', '', NULL, '2025-10-22 09:54:33'),
(3, 'ange', 'o798567382', NULL, '2025-10-22 09:55:07'),
(4, 'betty', '07896543', NULL, '2025-10-22 10:09:40'),
(5, 'inyange company ltd', '07888956382', 'musanze', '2025-12-15 09:25:37');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `role` varchar(50) DEFAULT 'Customer',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `email`, `role`) VALUES
(1, 'admin', 'admin123', NULL, 'Customer'),
(2, 'florence', 'florence123', NULL, 'Customer'),
(3, 'ninah', 'ninah123', NULL, 'Customer'),
(4, 'abiella', 'abiella123', NULL, 'Customer');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

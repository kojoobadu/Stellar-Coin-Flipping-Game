-- phpMyAdmin SQL Dump
-- version 4.5.4.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 03, 2017 at 04:26 AM
-- Server version: 5.7.11
-- PHP Version: 5.6.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `stellar_data`
--

-- --------------------------------------------------------

--
-- Table structure for table `participants`
--

CREATE TABLE `participants` (
  `UserName` varchar(100) NOT NULL,
  `AccountID` varchar(100) NOT NULL,
  `SecretSeed` varchar(100) NOT NULL,
  `HashValue` varchar(40) DEFAULT NULL,
  `RandomNumber` varchar(100) DEFAULT NULL,
  `Bet` tinyint(1) NOT NULL DEFAULT '0',
  `IdentificationNumber` int(11) DEFAULT NULL,
  `ID` int(11) DEFAULT NULL,
  `Winner` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `participants`
--

INSERT INTO `participants` (`UserName`, `AccountID`, `SecretSeed`, `HashValue`, `RandomNumber`, `Bet`, `IdentificationNumber`, `ID`, `Winner`) VALUES
('Kojo', 'GBYQ27WESBQB2HEJCSSKXJ4OWOCW6CF3P4FN6DJV4EDVVIV7XY3B3R5W', 'SDRIDSMH3CEOWA76UJ6Q3BIX6PXKKCLOEH6YUGCXR6JKCXFDK2XFWGOD', NULL, NULL, 0, NULL, NULL, 0),
('Alice', 'GBATDKU7YYAEWA4OOPS2UYMNWM242NX4BESJ3V5GR3DGLTS2DYWUJWK5', 'SB74QYJLPTP5JGEREOXYUQQJULUQGPPAHCLX3SACSQY7W6TSM4MOBUGD', NULL, NULL, 0, NULL, NULL, 0),
('Bob', 'GDEP7OZBGGFM5DHMCDKJLFVJBRGJIZ4VWNFIGHCSAZ2K45KG2GHMLD75', 'SC54CLWKA5WYXDCPVWVFJ6DHHTO72R2CNGIHHYTID2APSDK6IOJB4HWL', NULL, NULL, 0, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `Sender` varchar(100) NOT NULL,
  `Receiver` varchar(100) NOT NULL,
  `Amount` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`Sender`, `Receiver`, `Amount`) VALUES
('GBYQ27WESBQB2HEJCSSKXJ4OWOCW6CF3P4FN6DJV4EDVVIV7XY3B3R5W', 'GDEP7OZBGGFM5DHMCDKJLFVJBRGJIZ4VWNFIGHCSAZ2K45KG2GHMLD75', 20),
('GBATDKU7YYAEWA4OOPS2UYMNWM242NX4BESJ3V5GR3DGLTS2DYWUJWK5', 'GDEP7OZBGGFM5DHMCDKJLFVJBRGJIZ4VWNFIGHCSAZ2K45KG2GHMLD75', 100);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `participants`
--
ALTER TABLE `participants`
  ADD PRIMARY KEY (`UserName`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

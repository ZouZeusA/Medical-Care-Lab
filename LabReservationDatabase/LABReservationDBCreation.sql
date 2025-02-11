CREATE DATABASE `labreservation` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `reservation` (
  `number` int NOT NULL,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `result` tinytext,
  PRIMARY KEY (`number`,`date`,`time`),
  KEY `firstName_idx` (`firstName`),
  KEY `lastName_idx` (`lastName`),
  CONSTRAINT `firstName_FK` FOREIGN KEY (`firstName`) REFERENCES `patient` (`firstName`),
  CONSTRAINT `lastName_FK` FOREIGN KEY (`lastName`) REFERENCES `patient` (`lastName`),
  CONSTRAINT `number_FK` FOREIGN KEY (`number`) REFERENCES `labotest` (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `patient` (
  `idPatient` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `telephone` varchar(45) NOT NULL,
  PRIMARY KEY (`idPatient`),
  UNIQUE KEY `idPatient_UNIQUE` (`idPatient`),
  UNIQUE KEY `address_UNIQUE` (`address`),
  KEY `firstName_idx` (`firstName`),
  KEY `lastName_idx` (`lastName`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `labotest` (
  `number` int NOT NULL,
  `type` varchar(45) NOT NULL,
  `price` double NOT NULL,
  `option1` varchar(45) NOT NULL,
  `option2` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`number`),
  UNIQUE KEY `number_UNIQUE` (`number`),
  UNIQUE KEY `type_UNIQUE` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

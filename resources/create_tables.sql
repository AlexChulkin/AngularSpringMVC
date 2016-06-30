DROP TABLE IF EXISTS `STATE`;
CREATE TABLE `STATE` (
  `STATE_ID` int(1) NOT NULL AUTO_INCREMENT,
  `LABEL` varchar(20) NOT NULL,
  PRIMARY KEY (`STATE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `PACKET`;
CREATE TABLE `PACKET` (
  `PACKET_ID` int(11) NOT NULL AUTO_INCREMENT,
  `STATE_ID_FK` int(1) NOT NULL,
  PRIMARY KEY (`PACKET_ID`),
  FOREIGN KEY (`STATE_ID_FK`) REFERENCES `STATE` (`STATE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `STATIC_DATA`;
CREATE TABLE `STATIC_DATA` (
  `STATIC_DATA_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LABEL` varchar(20) NOT NULL,
  PRIMARY KEY (`STATIC_DATA_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `COMPT`;
CREATE TABLE `COMPT` (
  `COMPT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LABEL` varchar(255) NOT NULL,
  `PACKET_ID_FK` int(11) NOT NULL,


  PRIMARY KEY (`COMPT_ID`),
  FOREIGN KEY (`PACKET_ID_FK`) REFERENCES `PACKET` (`PACKET_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `DATA_COMPT`;
CREATE TABLE `DATA_COMPT` (
  `DATA_COMPT_ID` int(21) NOT NULL AUTO_INCREMENT,
  `COMPT_ID_FK` int(11) NOT NULL,
  `STATE_ID_FK` int(1) NOT NULL,
  `STATIC_DATA_ID_FK` int(11) NOT NULL,
  `CHECKED` int(1) NOT NULL,

  PRIMARY KEY (`DATA_COMPT_ID`),
  FOREIGN KEY (`COMPT_ID_FK`) REFERENCES `COMPT` (`COMPT_ID`),
  FOREIGN KEY (`STATE_ID_FK`) REFERENCES `STATE` (`STATE_ID`),
  FOREIGN KEY (`STATIC_DATA_ID_FK`) REFERENCES `STATIC_DATA` (`STATIC_DATA_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;


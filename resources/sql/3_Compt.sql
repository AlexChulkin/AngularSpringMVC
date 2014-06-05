DROP TABLE IF EXISTS `COMPT`;
CREATE TABLE `COMPT` (
  `COMPT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LABEL` varchar(255) NOT NULL,
  `PACKET_ID_FK` int(11) NOT NULL,


  PRIMARY KEY (`COMPT_ID`),
  FOREIGN KEY (`PACKET_ID_FK`) REFERENCES `PACKET` (`PACKET_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

insert into COMPT (LABEL, PACKET_ID_FK) values ('Risk position with Extraordinary Support', '1');
insert into COMPT (LABEL, PACKET_ID_FK) values ('Growth and Changes in Exposure', '1');
insert into COMPT (LABEL, PACKET_ID_FK) values ('Risk Concentrations and Risk Diversification', '1');
insert into COMPT (LABEL, PACKET_ID_FK) values ('Complexity', '1');
insert into COMPT (LABEL, PACKET_ID_FK) values ('Risks not covered by RACF', '1');
insert into COMPT (LABEL, PACKET_ID_FK) values ('Evidence of stronger or weaker Loss Experience', '1');


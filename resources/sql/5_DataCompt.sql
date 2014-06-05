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



insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '1', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '1', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '1', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '1', '4', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '1', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '1', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '2', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '2', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '2', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '2', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '2', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '2', '6', '1');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '3', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '3', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '3', '3', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '3', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '3', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '1', '3', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '1', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '1', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '1', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '1', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '1', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '1', '6', '1');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '2', '1', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '2', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '2', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '2', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '2', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '2', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '3', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '3', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '3', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '3', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '3', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '2', '3', '6', '1');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '1', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '1', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '1', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '1', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '1', '5', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '1', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '2', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '2', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '2', '3', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '2', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '2', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '2', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '3', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '3', '1', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '3', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '3', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '3', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '3', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '3', '3', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '1', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '1', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '1', '3', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '1', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '1', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '1', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '2', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '2', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '2', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '2', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '2', '5', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '2', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '3', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '3', '2', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '3', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '3', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '3', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '4', '3', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '1', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '1', '2', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '1', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '1', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '1', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '1', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '2', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '2', '2', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '2', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '2', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '2', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '2', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '3', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '3', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '3', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '3', '4', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '3', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '5', '3', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '1', '1', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '1', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '1', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '1', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '1', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '1', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '2', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '2', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '2', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '2', '4', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '2', '5', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '2', '6', '0');

insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '3', '1', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '3', '2', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '3', '3', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '3', '4', '0');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '3', '5', '1');
insert into DATA_COMPT (COMPT_ID_FK, STATE_ID_FK, STATIC_DATA_ID_FK, CHECKED) values ( '6', '3', '6', '0');

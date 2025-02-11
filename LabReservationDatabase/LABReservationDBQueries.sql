SELECT * FROM labreservation.labotest;
SELECT number FROM labreservation.labotest ORDER BY number DESC LIMIT 1;
INSERT INTO `labreservation`.`labotest` (`number`, `type`, `price`, `option1`, `option2`) VALUES ('', '', '', '', '');
UPDATE `labreservation`.`labotest` SET `number` = '', `type` = '', `price` = '', `option1` = '', `option2` = '' WHERE `number` = '';
DELETE FROM `labreservation`.`labotest` WHERE `number` = '';
SELECT COUNT(1) FROM labreservation.labotest WHERE `type` = '';

INSERT INTO `labreservation`.`patient` (`firstName`, `lastName`, `password`, `address`, `telephone`) VALUES ('', '',SHA1(''), '', '');
SELECT `patient`.`idPatient`,
    `patient`.`firstName`,
    `patient`.`lastName`,
    `patient`.`password`,
    `patient`.`address`,
    `patient`.`telephone`
FROM `labreservation`.`patient` WHERE `address` = '' AND `password` = sha1('') ;
 
#Confirmation
SELECT `labotest`.*
    FROM `labreservation`.`labotest` 
    WHERE `labotest`.`number` NOT IN 
    (SELECT `reservation`.`number` FROM `labreservation`.`reservation` 
    WHERE `reservation`.`date` = '' AND `reservation`.`time` ='');
 INSERT INTO `labreservation`.`reservation`
(`number`,
`firstName`,
`lastName`,
`date`,
`time`,
`result`)
VALUES ('', '','','', '', '');
 
 #Cancellation
SELECT `labotest`.`number`,
    `labotest`.`type`,
    `labotest`.`price`,
    `reservation`.`date`,
    `reservation`.`time`
    FROM `labreservation`.`labotest`INNER JOIN `labreservation`.`reservation`
    ON `labotest`.`number` = `reservation`.`number` 
    WHERE `reservation`.`firstName` = '' AND `reservation`.`lastName` ='';
DELETE FROM `labreservation`.`reservation` WHERE `reservation`.`number` = '' 
AND `reservation`.`date` = '' AND `reservation`.`time` ='' ;
 
 #file  
 SELECT `labotest`.`type`,
    `labotest`.`price`,
    `reservation`.`date`,
    `reservation`.`time`,
    `reservation`.`result`
    FROM `labreservation`.`labotest`INNER JOIN `labreservation`.`reservation`
    ON `labotest`.`number` = `reservation`.`number` 
    WHERE `reservation`.`firstName` = 'j' AND `reservation`.`lastName` ='j'
    ORDER BY `labotest`.`price` ASC;  
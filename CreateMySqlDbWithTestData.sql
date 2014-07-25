CREATE DATABASE  IF NOT EXISTS `messages`;
USE `messages`;

--
-- Table structure for table `tbl_user_messages`
--

DROP TABLE IF EXISTS `tbl_user_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_user_messages` (
    `user_name` varchar(100) NOT NULL,
    `message_text` varchar(500) NOT NULL,
    `message_datetime` datetime NOT NULL
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addUserMessage`(
userName varchar(100)
,messageText varchar(500)
,messageDate datetime
 )
BEGIN
	insert into tbl_user_messages (user_name, message_text, message_datetime)
	values (userName, messageText, messageDate);
END ;;
DELIMITER ;

DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generateDefaultTestData`(
 )
BEGIN
	delete from tbl_user_messages;

	call addUserMessage ('Julia', 'Julia''s message', now());
	call addUserMessage ('Peter', 'Peter''s message',  '2014-06-23 08:49:55');
	call addUserMessage ('Peter', 'Peter''s message # 2',  '2014-07-24 08:49:55');
	call addUserMessage ('Peter', 'Peter''s message # 3',  '2014-08-22 08:49:55');
	call addUserMessage ('Greesha', 'Greesha''s message',  '2014-08-23 07:05:22');
	call addUserMessage ('Lusya', 'Lusya''s message',  '2014-09-15 00:00:01');

	select * from tbl_user_messages;
END ;;
DELIMITER ;


DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `searchMessages`(
userName varchar(100)
,fromDatetime datetime
,toDatetime datetime)
BEGIN

/*userName IS null*/
	IF userName IS null AND fromDatetime IS null AND toDatetime IS null
		THEN
			select * from tbl_user_messages;

	ELSEIF userName IS null AND fromDatetime IS NOT null AND toDatetime IS NOT null
		THEN
			select * from tbl_user_messages
			where message_datetime between fromDatetime and toDatetime;

    ELSEIF userName IS null AND fromDatetime IS NOT null AND toDatetime IS null
		THEN
			select * from tbl_user_messages
			where message_datetime >= fromDatetime;

    ELSEIF  userName IS null AND fromDatetime IS null AND toDatetime IS NOT null
		THEN
			select * from tbl_user_messages
			where message_datetime <= toDatetime;

/*user_name IS NOT null*/
    ELSEIF userName IS NOT null AND fromDatetime IS null AND toDatetime IS null
		THEN
			select * from tbl_user_messages
			where user_name=userName;

    ELSEIF userName IS NOT null AND fromDatetime IS NOT null AND toDatetime IS NOT null
		THEN
			select * from tbl_user_messages
			where user_name=userName
				and message_datetime between fromDatetime and toDatetime;

    ELSEIF userName IS NOT null AND fromDatetime IS NOT null AND toDatetime IS null
		THEN
			select * from tbl_user_messages
			where user_name=userName
				and message_datetime >= fromDatetime;

    ELSEIF userName IS NOT null AND fromDatetime IS null AND toDatetime IS NOT null
		THEN
			select * from tbl_user_messages
			where user_name=userName
			and message_datetime <= toDatetime;

    END IF;


END ;;
DELIMITER ;

			/*Submit test data*/
			call generateDefaultTestData();
DELIMITER ;;

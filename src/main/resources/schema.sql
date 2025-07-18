
CREATE TABLE IF NOT EXISTS `endpoint` (
  `created_at` datetime(6) DEFAULT NULL,
  `endpoint_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `endpoint_name` varchar(255) NOT NULL,
  PRIMARY KEY (`endpoint_id`),
  UNIQUE KEY `UK_ENDPOINT_NAME` (`endpoint_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `incoming_request` (
  `endpoint_id` bigint(20) DEFAULT NULL,
  `received_at` datetime(6) DEFAULT NULL,
  `request_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `body` longtext,
  `headers` longtext,
  `path` longtext,
  `query_params` longtext,
  PRIMARY KEY (`request_id`),
  KEY `FK_ENDPOINT_ID` (`endpoint_id`),
  CONSTRAINT `FK_ENDPOINT_ID` FOREIGN KEY (`endpoint_id`) REFERENCES `endpoint` (`endpoint_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `guid` varchar(100) DEFAULT NULL,
  `user_alias` varchar(200) DEFAULT NULL,
  `user_email` varchar(200) DEFAULT NULL,
  `user_password` varchar(200) DEFAULT NULL,
  `is_email_varified` tinyint(4) DEFAULT NULL,
  `created_by` varchar(200) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` varchar(200) DEFAULT NULL,
  `updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

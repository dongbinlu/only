CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
                        `username` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
                        `password` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
                        `permissions` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


INSERT INTO `is-user-api`.`user` (`id`, `name`, `username`, `password`, `permissions`) VALUES ('1', 'jojo', 'jojo', '$s0$f0801$a/kqAsSElTvfIz94a5GdwQ==$7ZGQP11gaVC4UnZBUuCE4sCqw0Y1E7ij1HMV6iGUkKU=', 'wr');


CREATE TABLE `audit_log` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `method` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
                             `path` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
                             `status` int(255) DEFAULT NULL,
                             `username` varchar(255) COLLATE utf8_bin DEFAULT '',
                             `created_time` datetime DEFAULT NULL,
                             `modify_time` datetime DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


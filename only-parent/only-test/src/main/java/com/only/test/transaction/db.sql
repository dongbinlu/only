CREATE TABLE `role` (
                        `role_id` int(11) NOT NULL AUTO_INCREMENT,
                        `role_name` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
                        PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



CREATE TABLE `user` (
                        `user_id` int(11) NOT NULL AUTO_INCREMENT,
                        `username` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
                        PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
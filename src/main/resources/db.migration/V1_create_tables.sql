CREATE TABLE `stocks` (
                          `id` varchar(50) NOT NULL,
                          `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                          `description` varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `id_UNIQUE` (`id`),
                          UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `syncs` (
                         `id` binary(16) NOT NULL,
                         `start_time` timestamp NOT NULL,
                         `end_time` timestamp NULL DEFAULT NULL,
                         `is_successful` tinyint(1) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='sync record';

CREATE TABLE `stock_prices` (
                                `id` binary(16) NOT NULL,
                                `stock_id` varchar(50) NOT NULL,
                                `price` decimal(7,2) NOT NULL,
                                `MA_20` decimal(7,2) DEFAULT NULL,
                                `MA_30` decimal(7,2) DEFAULT NULL,
                                `MA_60` decimal(7,2) DEFAULT NULL,
                                `MA_120` decimal(7,2) DEFAULT NULL,
                                `sync_id` binary(16) NOT NULL,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `id_UNIQUE` (`id`),
                                KEY `Stock_Price_Stock_Id_idx` (`stock_id`),
                                KEY `Stock_Price_Sync_Id_idx` (`sync_id`),
                                CONSTRAINT `Stock_Price_Stock_Id` FOREIGN KEY (`stock_id`) REFERENCES `stocks` (`id`) ON DELETE CASCADE,
                                CONSTRAINT `Stock_Price_Sync_Id` FOREIGN KEY (`sync_id`) REFERENCES `syncs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS t_users;
CREATE TABLE t_users(
    `id` BIGINT NOT NULL   COMMENT '' ,
    `username` VARCHAR(50) NOT NULL UNIQUE   COMMENT '' ,
    `password` VARCHAR(64) NOT NULL   COMMENT '' ,
    `email` VARCHAR(50) NOT NULL UNIQUE   COMMENT '' ,
    `is_ban` TINYINT(1) NOT NULL   COMMENT '' ,
    `register_date` TIMESTAMP NOT NULL   COMMENT '' ,
    `last_login_date` TIMESTAMP    COMMENT '' ,
    `user_role` TINYINT(1) NOT NULL   COMMENT '' ,
    PRIMARY KEY (id)
)  COMMENT = '';

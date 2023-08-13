DROP TABLE IF EXISTS t_users;
CREATE TABLE t_users(
    `id` BIGINT NOT NULL   COMMENT '' ,
    `username` VARCHAR(255) NOT NULL   COMMENT '' ,
    `password` VARCHAR(255) NOT NULL   COMMENT '' ,
    `email` VARCHAR(50) NOT NULL   COMMENT '' ,
    `confirmation_key` VARCHAR(16)    COMMENT '' ,
    `is_confirmation` TINYINT(1) NOT NULL   COMMENT '' ,
    `is_ban` TINYINT(1) NOT NULL   COMMENT '' ,
    `register_date` DATETIME NOT NULL   COMMENT '' ,
    `last_login_date` DATETIME    COMMENT '' ,
    PRIMARY KEY (id)
)  COMMENT = '';


DROP TABLE IF EXISTS t_users;
CREATE TABLE t_users
(
    `id`                 BIGINT      NOT NULL COMMENT '',
    `username`           VARCHAR(50) NOT NULL COMMENT '',
    `password`           VARCHAR(64) NOT NULL COMMENT '',
    `email`              VARCHAR(50) NOT NULL COMMENT '',
    `is_ban`             TINYINT(1)  NOT NULL COMMENT '',
    `register_date`      DateTime    NOT NULL COMMENT '',
    `last_login_date`    DateTime   NOT NULL COMMENT '',
    `user_role`          TINYINT(1)  NOT NULL COMMENT '',
    `service_permission` VARCHAR(50) NOT NULL DEFAULT 1 COMMENT '',
    PRIMARY KEY (id)
) COMMENT = '';

DROP TABLE IF EXISTS t_permissions;
CREATE TABLE t_permissions
(
    `id`           INT          NOT NULL AUTO_INCREMENT COMMENT '',
    `service_name` VARCHAR(255) NOT NULL COMMENT '',
    PRIMARY KEY (id)
) COMMENT = '';
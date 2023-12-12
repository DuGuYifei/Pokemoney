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
    `service_permission` VARCHAR(50) NOT NULL DEFAULT 1 COMMENT '',
    `role_id`            INT         NOT NULL COMMENT '' REFERENCES t_roles (id),
    PRIMARY KEY (id)
) COMMENT = '';

DROP TABLE IF EXISTS t_permissions;
CREATE TABLE t_permissions
(
    `id`                INT          NOT NULL COMMENT '',
    `service_name`      VARCHAR(255) NOT NULL COMMENT '',
    `permission_bit`    INT          NOT NULL COMMENT '',
    PRIMARY KEY (id)
) COMMENT = '';

DROP TABLE IF EXISTS t_roles;
CREATE TABLE t_roles
(
    `id`                INT          NOT NULL COMMENT '',
    `role_name`         VARCHAR(255) NOT NULL COMMENT '',
    `permission_level`  INT          NOT NULL COMMENT '',
    `description`       VARCHAR(255) NOT NULL COMMENT '',
    PRIMARY KEY (id)
) COMMENT = '';
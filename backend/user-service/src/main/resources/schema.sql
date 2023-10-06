--TODO: change this file for mysql when production environment is ready, prepare for all tables because of varchar(X)
CREATE TABLE IF NOT EXISTS t_permissions
(
    id           INT          NOT NULL AUTO_INCREMENT,
    service_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
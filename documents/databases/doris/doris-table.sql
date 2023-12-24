DROP TABLE IF EXISTS user_dim;
CREATE TABLE user_dim
(
    `user_id`            BIGINT      NOT NULL COMMENT '',
    `is_ban`             BOOLEAN     NOT NULL COMMENT '',
    `register_date`      DATETIME    NOT NULL COMMENT ''
)
    UNIQUE KEY (user_id)
    DISTRIBUTED BY HASH(user_id) BUCKETS 10
    PROPERTIES (
      "storage_type" = "COLUMN",
      "replication_num" = "1"
    );

DROP TABLE IF EXISTS user_analysis_dim;
CREATE TABLE user_analysis_dim
(
    `user_analysis_id`   VARCHAR(128)       NOT NULL COMMENT '',
    `is_ban`             BOOLEAN            NOT NULL COMMENT ''
)
    UNIQUE KEY (user_analysis_id)
    DISTRIBUTED BY HASH(user_analysis_id) BUCKETS 10
    PROPERTIES (
      "storage_type" = "COLUMN",
      "replication_num" = "1"
    );

DROP TABLE IF EXISTS category_dim;
CREATE TABLE category_dim
(
    `category_id`        INTEGER            NOT NULL COMMENT '',
    `name`               VARCHAR(50)        NOT NULL COMMENT ''
)
    UNIQUE KEY (category_id)
    DISTRIBUTED BY HASH(category_id) BUCKETS 10
    PROPERTIES (
      "storage_type" = "COLUMN",
      "replication_num" = "1"
    );

DROP TABLE IF EXISTS type_dim;
CREATE TABLE type_dim
(
    `type_id`            INTEGER            NOT NULL COMMENT '',
    `name`               VARCHAR(50)        NOT NULL COMMENT ''
)
    UNIQUE KEY (type_id)
    DISTRIBUTED BY HASH(type_id) BUCKETS 10
    PROPERTIES (
        "storage_type" = "COLUMN",
        "replication_num" = "1"
    );

DROP TABLE IF EXISTS ledger_dim;
CREATE TABLE ledger_dim
(
    `ledger_id`          BIGINT             NOT NULL COMMENT '',
    `owner_id`           BIGINT             NOT NULL COMMENT '',
    `name`               VARCHAR(50)        NOT NULL COMMENT '',
    `budget`             DECIMAL(18, 2)     NOT NULL COMMENT '',
    `create_at`          DATETIME           NOT NULL COMMENT '',
    `update_at`          DATETIME           NOT NULL COMMENT '',
    `del_flag`           INTEGER            NOT NULL COMMENT ''
)
    UNIQUE KEY (ledger_id)
    DISTRIBUTED BY HASH(ledger_id) BUCKETS 10
    PROPERTIES (
        "storage_type" = "COLUMN",
        "replication_num" = "1"
    );

DROP TABLE IF EXISTS fund_dim;
CREATE TABLE fund_dim
(
    `fund_id`            BIGINT             NOT NULL COMMENT '',
    `owner_id`           BIGINT             NOT NULL COMMENT '',
    `name`               VARCHAR(50)        NOT NULL COMMENT '',
    `balance`            DECIMAL(18, 2)     NOT NULL COMMENT '',
    `create_at`          DATETIME           NOT NULL COMMENT '',
    `update_at`          DATETIME           NOT NULL COMMENT '',
    `del_flag`           INTEGER            NOT NULL COMMENT ''
)
    UNIQUE KEY (fund_id)
    DISTRIBUTED BY HASH(fund_id) BUCKETS 10
    PROPERTIES (
        "storage_type" = "COLUMN",
        "replication_num" = "1"
    );

DROP TABLE IF EXISTS time_dim;
CREATE TABLE time_dim (
    `time_id`            INTEGER            NOT NULL COMMENT '',
    `date`               DATEV2             NOT NULL COMMENT '',
    `year`               INTEGER            NOT NULL COMMENT '',
    `month`              INTEGER            NOT NULL COMMENT '',
    `day`                INTEGER            NOT NULL COMMENT '',
    `quarter`            INTEGER            NOT NULL COMMENT '',
    `day_of_week`        INTEGER            NOT NULL COMMENT '',
    `is_weekday`         BOOLEAN            NOT NULL COMMENT '',
    `is_weekend`         BOOLEAN            NOT NULL COMMENT '',
    `is_holiday`         BOOLEAN            NOT NULL COMMENT '',
    `holiday_name`       VARCHAR(100)                COMMENT ''
)
    UNIQUE KEY (time_id)
    DISTRIBUTED BY HASH(time_id) BUCKETS 10
    PROPERTIES (
        "storage_type" = "COLUMN",
        "replication_num" = "1"
    );

DROP TABLE IF EXISTS time_analysis_dim;
CREATE TABLE time_analysis_dim (
    `time_analysis_id`   INTEGER            NOT NULL COMMENT '',
    `year`               INTEGER            NOT NULL COMMENT '',
    `month`              INTEGER            NOT NULL COMMENT '',
    `quarter`            INTEGER            NOT NULL COMMENT ''
)
    UNIQUE KEY (time_analysis_id)
    DISTRIBUTED BY HASH(time_analysis_id) BUCKETS 10
    PROPERTIES (
        "storage_type" = "COLUMN",
        "replication_num" = "1"
    );

DROP TABLE IF EXISTS transaction_fact;
CREATE TABLE transaction_fact
(
    `id`                        BIGINT             NOT NULL COMMENT '',
    `money`                     DECIMAL(18, 2)     NOT NULL COMMENT '',
    `fund_id`                   BIGINT             NOT NULL COMMENT '',
    `ledger_id`                 BIGINT             NOT NULL COMMENT '',
    `type_id`                   INTEGER            NOT NULL COMMENT '',
    `category_id`               INTEGER            NOT NULL COMMENT '',
    `happen_at`                 DATETIME           NOT NULL COMMENT '',
    `happen_time_id`            INTEGER            NOT NULL COMMENT '',
    `happen_time_analysis_id`   INTEGER            NOT NULL COMMENT '',
    `update_by`                 BIGINT             NOT NULL COMMENT '',
    `update_at`                 DATETIME           NOT NULL COMMENT '',
    `update_time_id`            INTEGER            NOT NULL COMMENT '',
    `update_time_analysis_id`   INTEGER            NOT NULL COMMENT '',
    `del_flag`                  INTEGER            NOT NULL COMMENT ''
)
    UNIQUE KEY (id)
    DISTRIBUTED BY HASH(id) BUCKETS 10
    PROPERTIES (
        "storage_type" = "COLUMN",
        "replication_num" = "1"
    );

DROP TABLE IF EXISTS transaction_example_fact;
CREATE TABLE transaction_example_fact
(
    `id`                 INTEGER                    NOT NULL                    COMMENT '',
    `sum_money`          DECIMAL(18, 2)  SUM        NOT NULL    DEFAULT "0.0"   COMMENT '' ,
    `category_id`        INTEGER         REPLACE    NOT NULL                    COMMENT '',
    `type_id`            INTEGER         REPLACE    NOT NULL                    COMMENT '' ,
    `time_analysis_id`   INTEGER         REPLACE    NOT NULL                    COMMENT ''
)
    AGGREGATE KEY (id)
    DISTRIBUTED BY HASH(id) BUCKETS 10
    PROPERTIES (
        "storage_type" = "COLUMN",
        "replication_num" = "1"
    );
CREATE TABLE t_funds (
    region_id               VARCHAR NOT NULL,
    user_id                 VARCHAR NOT NULL,
    fund_id                 VARCHAR NOT NULL,
    fund_info.id            UNSIGNED_LONG,
    fund_info.name          VARCHAR,
    fund_info.balance       DECIMAL(31, 2),
    fund_info.owner         VARCHAR,
    fund_info.editors       VARCHAR ARRAY,
    fund_info.create_at     TIMESTAMP,
    update_info.update_at   UNSIGNED_TIMESTAMP,
    update_info.del_flag    TINYINT,
    CONSTRAINT fund_pk PRIMARY KEY (region_id, user_id, fund_id)
);
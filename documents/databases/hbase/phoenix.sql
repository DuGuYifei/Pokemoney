CREATE TABLE t_funds (
    region_id               INTEGER NOT NULL,
    user_id                 BIGINT  NOT NULL,
    fund_id                 BIGINT  NOT NULL,
    fund_info.name          VARCHAR,
    fund_info.balance       DECIMAL(31, 2),
    fund_info.editors       BIGINT ARRAY,
    fund_info.owner         BIGINT,
    fund_info.create_at     BIGINT,
    update_info.update_at   BIGINT,
    update_info.del_flag    INTEGER,
    CONSTRAINT fund_pk PRIMARY KEY (region_id, user_id, fund_id)
);

CREATE TABLE t_ledgers (
    region_id               INTEGER NOT NULL,
    user_id                 BIGINT  NOT NULL,
    ledger_id               BIGINT  NOT NULL,
    ledger_info.name        VARCHAR,
    ledger_info.budget      DECIMAL(31, 2),
    ledger_info.owner       BIGINT,
    ledger_info.editors     BIGINT ARRAY,
    ledger_info.create_at   BIGINT,
    update_info.update_at   BIGINT,
    update_info.del_flag    INTEGER,
    CONSTRAINT ledger_pk PRIMARY KEY (region_id, user_id, ledger_id)
);

CREATE TABLE t_operations (
    region_id                       INTEGER NOT NULL,
    user_id                         BIGINT  NOT NULL,
    reverse_operation_id            BIGINT  NOT NULL,
    operation_info.operation_id     BIGINT,
    operation_info.target_table     VARCHAR,
    operation_info.target_row_key   VARCHAR,
    update_info.update_at           BIGINT,
    CONSTRAINT operation_pk PRIMARY KEY (region_id, user_id, reverse_operation_id)
);

CREATE TABLE t_users (
    region_id                   INTEGER NOT NULL,
    user_id                     BIGINT  NOT NULL,
    user_info.name              VARCHAR,
    user_info.email             VARCHAR,
    user_info.update_at         BIGINT,
    fund_info.funds             VARCHAR ARRAY,
    fund_info.del_funds         VARCHAR ARRAY,
    ledger_info.ledgers         VARCHAR ARRAY,
    ledger_info.del_ledgers     VARCHAR ARRAY,
    app_info.categories         VARCHAR,
    app_info.subcategories      VARCHAR,
    notifications.new_notify    VARCHAR,
    CONSTRAINT user_pk PRIMARY KEY (region_id, user_id)
);

CREATE TABLE t_transactions_202312 (
    region_id                           INTEGER NOT NULL,
    user_id                             BIGINT  NOT NULL,
    ledger_id_rk                        BIGINT  NOT NULL,
    reverse_transaction_id              BIGINT  NOT NULL,
    transaction_info.transaction_id     BIGINT,
    transaction_info.money              DECIMAL(31, 2),
    transaction_info.type_id            INTEGER,
    transaction_info.relevant_entity    VARCHAR,
    transaction_info.comment            VARCHAR,
    transaction_info.fund_id            BIGINT,
    transaction_info.category_id        INTEGER,
    transaction_info.sub_category       BIGINT,
    transaction_info.ledger_id          BIGINT,
    transaction_info.happen_at          BIGINT,
    update_info.update_by               BIGINT,
    update_info.update_at               BIGINT,
    update_info.del_flag                INTEGER,
    CONSTRAINT transaction_pk PRIMARY KEY (region_id, user_id, ledger_id_rk, reverse_transaction_id)
);

CREATE TABLE t_transactions_bf202312 (
    region_id                           INTEGER NOT NULL,
    user_id                             BIGINT  NOT NULL,
    ledger_id_rk                        BIGINT  NOT NULL,
    reverse_transaction_id              BIGINT  NOT NULL,
    transaction_info.transaction_id     BIGINT,
    transaction_info.money              DECIMAL(31, 2),
    transaction_info.type_id            INTEGER,
    transaction_info.relevant_entity    VARCHAR,
    transaction_info.comment            VARCHAR,
    transaction_info.fund_id            BIGINT,
    transaction_info.category_id        INTEGER,
    transaction_info.sub_category       BIGINT,
    transaction_info.ledger_id          BIGINT,
    transaction_info.happen_at          BIGINT,
    update_info.update_by               BIGINT,
    update_info.update_at               BIGINT,
    update_info.del_flag                INTEGER,
    CONSTRAINT transaction_pk PRIMARY KEY (region_id, user_id, ledger_id_rk, reverse_transaction_id)
);

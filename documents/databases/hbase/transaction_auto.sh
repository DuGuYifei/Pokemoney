#!/bin/bash

# get current date
current_date=$(date +%Y-%m-%d)

# get next month
next_month=$(date -d "$current_date +1 month" +%Y_%m)

sql_file="transaction_auto.sql"

#CREATE TABLE t_transactions_202312 (
#    region_id                           INTEGER NOT NULL,
#    user_id                             BIGINT  NOT NULL,
#    ledger_id_rk                        BIGINT  NOT NULL,
#    reverse_transaction_id              BIGINT  NOT NULL,
#    transaction_info.transaction_id     BIGINT  NOT NULL,
#    transaction_info.money              DECIMAL(31, 2),
#    transaction_info.type_id            INTEGER NOT NULL,
#    transaction_info.relevant_entity    VARCHAR,
#    transaction_info.comment            VARCHAR,
#    transaction_info.fund_id            BIGINT,
#    transaction_info.category_id        INTEGER NOT NULL,
#    transaction_info.sub_category       BIGINT,
#    transaction_info.ledger_id          BIGINT,
#    transaction_info.happen_at          BIGINT NOT NULL,
#    update_info.update_by               BIGINT NOT NULL,
#    update_info.update_at               BIGINT NOT NULL,
#    update_info.del_flag                INTEGER NOT NULL,
#    CONSTRAINT transaction_pk PRIMARY KEY (region_id, user_id, ledger_id_rk, reverse_transaction_id)
#);

echo "CREATE TABLE t_transaction_$next_month (" > $sql_file
echo "    region_id                           INTEGER NOT NULL," >> $sql_file
echo "    user_id                             BIGINT  NOT NULL," >> $sql_file
echo "    ledger_id_rk                        BIGINT  NOT NULL," >> $sql_file
echo "    reverse_transaction_id              BIGINT  NOT NULL," >> $sql_file
echo "    transaction_info.transaction_id     BIGINT  NOT NULL," >> $sql_file
echo "    transaction_info.money              DECIMAL(31, 2)," >> $sql_file
echo "    transaction_info.type_id            INTEGER NOT NULL," >> $sql_file
echo "    transaction_info.relevant_entity    VARCHAR," >> $sql_file
echo "    transaction_info.comment            VARCHAR," >> $sql_file
echo "    transaction_info.fund_id            BIGINT," >> $sql_file
echo "    transaction_info.category_id        INTEGER NOT NULL," >> $sql_file
echo "    transaction_info.sub_category       BIGINT," >> $sql_file
echo "    transaction_info.ledger_id          BIGINT," >> $sql_file
echo "    transaction_info.happen_at          BIGINT NOT NULL," >> $sql_file
echo "    update_info.update_by               BIGINT NOT NULL," >> $sql_file
echo "    update_info.update_at               BIGINT NOT NULL," >> $sql_file
echo "    update_info.del_flag                INTEGER NOT NULL," >> $sql_file
echo "    CONSTRAINT transaction_pk PRIMARY KEY (region_id, user_id, ledger_id_rk, reverse_transaction_id)" >> $sql_file
echo ");" >> $sql_file

zk_address="172.20.1.5:2181"

python3 sqlline.py $zk_address -f $sql_file

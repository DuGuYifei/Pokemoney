package com.pokemoney.hadoop.spark.service;

import com.pokemoney.hadoop.spark.dao.DorisFundMapper;
import com.pokemoney.hadoop.spark.dao.DorisLedgerMapper;
import com.pokemoney.hadoop.spark.dao.DorisTransactionAnalysisMapper;
import com.pokemoney.hadoop.spark.dao.DorisTransactionMapper;
import com.pokemoney.hadoop.spark.model.FundDimModel;
import com.pokemoney.hadoop.spark.model.LedgerDimModel;
import com.pokemoney.hadoop.spark.model.TransactionAnalysisModel;
import com.pokemoney.hadoop.spark.model.TransactionFactModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The service of the Spark.
 */
@Slf4j
@Service
public class SparkService {
    /**
     * The HBase configuration.
     */
    private final Configuration hbaseConfig;

    /**
     * The fund mapper
     */
    private final DorisFundMapper dorisFundMapper;

    /**
     * The ledger mapper.
     */
    private final DorisLedgerMapper dorisLedgerMapper;

    /**
     * The transaction mapper.
     */
    private final DorisTransactionMapper dorisTransactionMapper;

    /**
     * The transaction analysis mapper.
     */
    private final DorisTransactionAnalysisMapper dorisTransactionAnalysisMapper;

    /**
     * The simple date format of year and month and day.
     */
    private final DateFormat yearMonthDayFormat = new SimpleDateFormat("yyyyMMdd");


    /**
     * The constructor.
     *
     * @param hbaseConfig                    The HBase configuration.
     * @param dorisFundMapper                The fund mapper.
     * @param dorisLedgerMapper              The ledger mapper.
     * @param dorisTransactionMapper         The transaction mapper.
     * @param dorisTransactionAnalysisMapper The transaction analysis mapper.
     */
    public SparkService(Configuration hbaseConfig, DorisFundMapper dorisFundMapper, DorisLedgerMapper dorisLedgerMapper, DorisTransactionMapper dorisTransactionMapper, DorisTransactionAnalysisMapper dorisTransactionAnalysisMapper) {
        this.hbaseConfig = hbaseConfig;
        this.dorisFundMapper = dorisFundMapper;
        this.dorisLedgerMapper = dorisLedgerMapper;
        this.dorisTransactionMapper = dorisTransactionMapper;
        this.dorisTransactionAnalysisMapper = dorisTransactionAnalysisMapper;
    }

    /**
     * Upsert all funds.
     */
    public void upsertAllFunds() {
        try {
            Connection connection = ConnectionFactory.createConnection(hbaseConfig);
            Table table = connection.getTable(TableName.valueOf("t_funds"));

            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes("fund_info"));
            scan.addFamily(Bytes.toBytes("update_info"));

            ResultScanner scanner = table.getScanner(scan);

            for (Result result : scanner) {
                Long fundId = Bytes.toLong(result.getValue(Bytes.toBytes("fund_info"), Bytes.toBytes("id")));
                Long ownerId = Bytes.toLong(result.getValue(Bytes.toBytes("fund_info"), Bytes.toBytes("owner")));
                String name = Bytes.toString(result.getValue(Bytes.toBytes("fund_info"), Bytes.toBytes("name")));
                Double balance = Bytes.toDouble(result.getValue(Bytes.toBytes("fund_info"), Bytes.toBytes("balance")));
                Long createAt = Bytes.toLong(result.getValue(Bytes.toBytes("fund_info"), Bytes.toBytes("create_at")));
                Long updateAt = Bytes.toLong(result.getValue(Bytes.toBytes("update_info"), Bytes.toBytes("update_at")));
                Integer delFlag = Bytes.toInt(result.getValue(Bytes.toBytes("update_info"), Bytes.toBytes("del_flag")));

                FundDimModel fundDimModel = new FundDimModel(fundId, ownerId, name, balance, createAt, updateAt, delFlag);
                dorisFundMapper.insertFundDim(fundDimModel);
            }

            scanner.close();
            table.close();
            connection.close();
        } catch (Exception e) {
            log.error("Failed to handle fund.", e);
        }
    }

    /**
     * Upsert all ledgers.
     */
    public void upsertAllLedgers() {
        try {
            Connection connection = ConnectionFactory.createConnection(hbaseConfig);
            Table table = connection.getTable(TableName.valueOf("t_ledgers"));

            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes("ledger_info"));
            scan.addFamily(Bytes.toBytes("update_info"));

            ResultScanner scanner = table.getScanner(scan);

            for (Result result : scanner) {
                Long ledgerId = Bytes.toLong(result.getValue(Bytes.toBytes("ledger_info"), Bytes.toBytes("id")));
                Long ownerId = Bytes.toLong(result.getValue(Bytes.toBytes("ledger_info"), Bytes.toBytes("owner")));
                String name = Bytes.toString(result.getValue(Bytes.toBytes("ledger_info"), Bytes.toBytes("name")));
                Double budget = Bytes.toDouble(result.getValue(Bytes.toBytes("ledger_info"), Bytes.toBytes("budget")));
                Long createAt = Bytes.toLong(result.getValue(Bytes.toBytes("ledger_info"), Bytes.toBytes("create_at")));
                Long updateAt = Bytes.toLong(result.getValue(Bytes.toBytes("update_info"), Bytes.toBytes("update_at")));
                Integer delFlag = Bytes.toInt(result.getValue(Bytes.toBytes("update_info"), Bytes.toBytes("del_flag")));

                LedgerDimModel ledgerDimModel = new LedgerDimModel(ledgerId, ownerId, name, budget, createAt, updateAt, delFlag);
                dorisLedgerMapper.insertLedger(ledgerDimModel);
            }

            scanner.close();
            table.close();
            connection.close();
        } catch (Exception e) {
            log.error("Failed to handle ledger.", e);
        }
    }

    /**
     * Upsert all transactions in current month table.
     */
    public void upsertAllTransactionsCurrentMonth() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        String tableName = "t_transactions_" + dateFormat.format(currentDate);
        try {
            Connection connection = ConnectionFactory.createConnection(hbaseConfig);
            Table table = connection.getTable(TableName.valueOf(tableName));

            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes("transaction_info"));
            scan.addFamily(Bytes.toBytes("update_info"));

            ResultScanner scanner = table.getScanner(scan);

            for (Result result : scanner) {
                Long id = Bytes.toLong(result.getValue(Bytes.toBytes("transaction_info"), Bytes.toBytes("transaction_id")));
                Double money = Bytes.toDouble(result.getValue(Bytes.toBytes("transaction_info"), Bytes.toBytes("money")));
                Integer typeId = Bytes.toInt(result.getValue(Bytes.toBytes("transaction_info"), Bytes.toBytes("type_id")));
                Long fundId = Bytes.toLong(result.getValue(Bytes.toBytes("transaction_info"), Bytes.toBytes("fund_id")));
                Integer categoryId = Bytes.toInt(result.getValue(Bytes.toBytes("transaction_info"), Bytes.toBytes("category_id")));
                Long ledgerId = Bytes.toLong(result.getValue(Bytes.toBytes("transaction_info"), Bytes.toBytes("ledger_id")));
                long happenAt = Bytes.toLong(result.getValue(Bytes.toBytes("transaction_info"), Bytes.toBytes("happen_at")));
                Long updateBy = Bytes.toLong(result.getValue(Bytes.toBytes("update_info"), Bytes.toBytes("update_by")));
                long updateAt = Bytes.toLong(result.getValue(Bytes.toBytes("update_info"), Bytes.toBytes("update_at")));
                Integer delFlag = Bytes.toInt(result.getValue(Bytes.toBytes("update_info"), Bytes.toBytes("del_flag")));

                Date happenAtDate = new Date(happenAt);
                String happenAtYearMonthDay = yearMonthDayFormat.format(happenAtDate);
                Integer happenTimeId = Integer.parseInt(happenAtYearMonthDay);
                Integer happenTimeAnalysisId = Integer.parseInt(happenAtYearMonthDay.substring(0, 6));
                String updateAtYearMonthDay = yearMonthDayFormat.format(new Date(updateAt));
                Integer updateTimeId = Integer.parseInt(updateAtYearMonthDay);
                Integer updateTimeAnalysisId = Integer.parseInt(updateAtYearMonthDay.substring(0, 6));
                TransactionFactModel transactionFactModel = new TransactionFactModel(id, money, fundId, ledgerId, typeId, categoryId, happenAt, happenTimeId, happenTimeAnalysisId, updateBy, updateAt, updateTimeId, updateTimeAnalysisId, delFlag);
                dorisTransactionMapper.insertTransaction(transactionFactModel);

                TransactionAnalysisModel transactionAnalysisModel = new TransactionAnalysisModel(id, money, categoryId, typeId, happenTimeAnalysisId);
                dorisTransactionAnalysisMapper.insertTransactionAnalysis(transactionAnalysisModel);
            }

            scanner.close();
            table.close();
            connection.close();
        } catch (Exception e) {
            log.error("Failed to handle transaction.", e);
        }
    }
}

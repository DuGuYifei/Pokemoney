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

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The service of the Spark.
 */
@Slf4j
@Service
public class SparkService {
    /**
     * Connection string
     */
    private static final String connectionString = "jdbc:phoenix:43.131.33.18:2181";
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
     * @param dorisFundMapper                The fund mapper.
     * @param dorisLedgerMapper              The ledger mapper.
     * @param dorisTransactionMapper         The transaction mapper.
     * @param dorisTransactionAnalysisMapper The transaction analysis mapper.
     */
    public SparkService(DorisFundMapper dorisFundMapper, DorisLedgerMapper dorisLedgerMapper, DorisTransactionMapper dorisTransactionMapper, DorisTransactionAnalysisMapper dorisTransactionAnalysisMapper) {
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
            try(java.sql.Connection con = DriverManager.getConnection(SparkService.connectionString)) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM T_FUNDS");
                while (rs.next()) {
                    Long id = rs.getLong("FUND_ID");
                    String name = rs.getString("NAME");
                    Double balance = rs.getDouble("BALANCE");
                    Long ownerId = rs.getLong("OWNER");
                    Long createAt = rs.getLong("CREATE_AT");
                    Date createAtDate = new Date(createAt);
                    Long updateAt = rs.getLong("UPDATE_AT");
                    Date updateAtDate = new Date(updateAt);
                    Integer delFlag = rs.getInt("DEL_FLAG");
                    System.out.println("id" + id + " name" + name + " balance" + balance + " ownerId" + ownerId + " createAt" + createAt + " updateAt" + updateAt + " delFlag" + delFlag);
                    FundDimModel fundDimModel = new FundDimModel(id, ownerId, name, balance, createAtDate, updateAtDate, delFlag);
                    dorisFundMapper.insertFundDim(fundDimModel);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Upsert all ledgers.
     */
    public void upsertAllLedgers() {
        try {
            try(java.sql.Connection con = DriverManager.getConnection(SparkService.connectionString)) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM T_LEDGERS");
                while (rs.next()) {
                    Long id = rs.getLong("LEDGER_ID");
                    Long ownerId = rs.getLong("OWNER");
                    String name = rs.getString("NAME");
                    Double budget = rs.getDouble("BUDGET");
                    Long createAt = rs.getLong("CREATE_AT");
                    Date createAtDate = new Date(createAt);
                    Long updateAt = rs.getLong("UPDATE_AT");
                    Date updateAtDate = new Date(updateAt);
                    Integer delFlag = rs.getInt("DEL_FLAG");
                    System.out.println("id" + id + " ownerId" + ownerId + " name" + name + " budget" + budget + " createAt" + createAt + " updateAt" + updateAt + " delFlag" + delFlag);
                    LedgerDimModel ledgerDimModel = new LedgerDimModel(id, ownerId, name, budget, createAtDate, updateAtDate, delFlag);
                    dorisLedgerMapper.insertLedger(ledgerDimModel);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Upsert all transactions in current month table.
     */
    public void upsertAllTransactionsPrevMonth() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, -1);
        Date prevMonthDate = calendar.getTime();
        String tableName = "T_TRANSACTIONS_" + dateFormat.format(prevMonthDate);
        try {
            try(java.sql.Connection con = DriverManager.getConnection(SparkService.connectionString)) {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
                while (rs.next()) {
                    Long id = rs.getLong("TRANSACTION_ID");
                    BigDecimal money = rs.getBigDecimal("MONEY");
                    Integer typeId = rs.getInt("TYPE_ID");
                    Long fundId = rs.getLong("FUND_ID");
                    Integer categoryId = rs.getInt("CATEGORY_ID");
                    Long ledgerId = rs.getLong("LEDGER_ID");
                    long happenAt = rs.getLong("HAPPEN_AT");
                    Long updateBy = rs.getLong("UPDATE_BY");
                    long updateAt = rs.getLong("UPDATE_AT");
                    Integer delFlag = rs.getInt("DEL_FLAG");
                    System.out.println("id" + id + " money" + money + " typeId" + typeId + " fundId" + fundId + " categoryId" + categoryId + " ledgerId" + ledgerId + " happenAt" + happenAt + " updateBy" + updateBy + " updateAt" + updateAt + " delFlag" + delFlag);

                    Date happenAtDate = new Date(happenAt);
                    String happenAtYearMonthDay = yearMonthDayFormat.format(happenAtDate);
                    Integer happenTimeId = Integer.parseInt(happenAtYearMonthDay);
                    Integer happenTimeAnalysisId = Integer.parseInt(happenAtYearMonthDay.substring(0, 6));
                    Date updateAtDate = new Date(updateAt);
                    String updateAtYearMonthDay = yearMonthDayFormat.format(updateAtDate);
                    Integer updateTimeId = Integer.parseInt(updateAtYearMonthDay);
                    Integer updateTimeAnalysisId = Integer.parseInt(updateAtYearMonthDay.substring(0, 6));
                    System.out.println("happenAtYearMonthDay" + happenAtYearMonthDay + " happenTimeId" + happenTimeId + " happenTimeAnalysisId" + happenTimeAnalysisId + " updateAtYearMonthDay" + updateAtYearMonthDay + " updateTimeId" + updateTimeId + " updateTimeAnalysisId" + updateTimeAnalysisId);
                    System.out.println("insert into transaction_fact (id, money, fund_id, ledger_id, type_id, category_id, happen_at, happen_time_id, happen_time_analysis_id, update_by, update_at, update_time_id, update_time_analysis_id, del_flag) values (" + id + ", " + money + ", " + fundId + ", " + ledgerId + ", " + typeId + ", " + categoryId + ", " + happenAt + ", " + happenTimeId + ", " + happenTimeAnalysisId + ", " + updateBy + ", " + updateAt + ", " + updateTimeId + ", " + updateTimeAnalysisId + ", " + delFlag + ")");
                    TransactionFactModel transactionFactModel = new TransactionFactModel(id, money, fundId, ledgerId, typeId, categoryId, happenAtDate, happenTimeId, happenTimeAnalysisId, updateBy, updateAtDate, updateTimeId, updateTimeAnalysisId, delFlag);
                    dorisTransactionMapper.insertTransaction(transactionFactModel);

                    TransactionAnalysisModel transactionAnalysisModel = new TransactionAnalysisModel(id, money, categoryId, typeId, happenTimeAnalysisId);
                    dorisTransactionAnalysisMapper.insertTransactionAnalysis(transactionAnalysisModel);
                }
            }
        } catch (Exception e) {
            log.error("Failed to handle transaction.", e);
        }
    }
}

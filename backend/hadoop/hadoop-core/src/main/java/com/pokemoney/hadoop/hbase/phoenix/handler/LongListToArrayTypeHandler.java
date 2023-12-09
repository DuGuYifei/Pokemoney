package com.pokemoney.hadoop.hbase.phoenix.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Long array type handler.
 */
public class LongListToArrayTypeHandler extends BaseTypeHandler<List<Long>> {
    /**
     * @param ps            PreparedStatement
     * @param i             index
     * @param parameter     parameter
     * @param jdbcType      jdbcType
     * @throws SQLException SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = ps.getConnection();
        Long[] longArray = parameter.toArray(new Long[0]);
        Array sqlArray = conn.createArrayOf("bigint", longArray);
        ps.setArray(i, sqlArray);
    }

    /**
     * @param rs            ResultSet
     * @param columnName    columnName
     * @return              List<Long>
     * @throws SQLException SQLException
     */
    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toLongList(rs.getArray(columnName));
    }

    /**
     * @param rs            ResultSet
     * @param columnIndex   columnIndex
     * @return              List<Long>
     * @throws SQLException SQLException
     */
    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toLongList(rs.getArray(columnIndex));
    }

    /**
     * @param cs            CallableStatement
     * @param columnIndex   columnIndex
     * @return              List<Long>
     * @throws SQLException SQLException
     */
    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toLongList(cs.getArray(columnIndex));
    }

    /**
     * @param sqlArray      sqlArray
     * @return              List<Long>
     * @throws SQLException SQLException
     */
    private List<Long> toLongList(Array sqlArray) throws SQLException {
        if (sqlArray == null) {
            return null;
        }
        Long[] longArray = (Long[]) sqlArray.getArray();
        return new ArrayList<>(Arrays.asList(longArray));
    }
}

package com.pokemoney.hadoop.hbase.utils;

import org.apache.phoenix.schema.types.PDataType;
import org.apache.phoenix.schema.types.PLong;
import org.apache.phoenix.schema.types.PhoenixArray;

/**
 * Array utils
 */
public class ArrayUtils {
    /**
     * Convert java Long array to phoenix array
     *
     * @param array java Long array
     */
    public static PhoenixArray toPhoenixArray(Long[] array) {
        return new PhoenixArray(PDataType.fromTypeId(PDataType.ARRAY_TYPE_BASE + PLong.INSTANCE.getSqlType()), array);
    }
}

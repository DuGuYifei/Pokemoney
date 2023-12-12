package com.pokemoney.hadoop.client;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.dromara.dynamictp.core.spring.EnableDynamicTp;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The hadoop client application.
 */
@SpringBootApplication
@ComponentScan({
        "com.pokemoney.commons.http",
        "com.pokemoney.commons.config",
        "com.pokemoney.hadoop.hbase.phoenix.dao",
        "com.pokemoney.hadoop.client",
})
@MapperScan("com.pokemoney.hadoop.hbase.phoenix.dao")
@EnableDubbo
@EnableDynamicTp
public class HadoopClientApplication {
    /**
     * The main entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(HadoopClientApplication.class, args);
    }


//        public static void main(String[] args) {
//            // JDBC连接字符串
//            String jdbcUrl = "jdbc:phoenix:192.168.1.106:2181"; // 指定Phoenix服务器的ZooKeeper连接字符串
//
//            try {
//                // 连接到Phoenix数据库
//                Connection connection = DriverManager.getConnection(jdbcUrl);
//
//                // 创建Statement对象
//                Statement statement = connection.createStatement();
//
//                // 执行SQL查询
//                String sqlQuery = "SELECT * FROM t_funds"; // 替换为您的表名和查询
//                ResultSet resultSet = statement.executeQuery(sqlQuery);
//
//                // 处理查询结果
//                while (resultSet.next()) {
//                    // 从结果集中获取数据并进行处理
//                    String column1Value = resultSet.getString("fund_id"); // 替换为您的列名
//
//                    System.out.println("Column1: " + column1Value);
//                }
//
//                // 关闭资源
//                resultSet.close();
//                statement.close();
//                connection.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//    }

}
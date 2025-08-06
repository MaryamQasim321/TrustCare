package com.example.trustcare.config;

import java.sql.Connection;
import java.sql.SQLException;

import com.example.trustcare.Logging.LogUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnectorService {


    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectorService.class);

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {

         try {
             String jdbcUrl = System.getenv("DB_URL");
             String username = System.getenv("DB_USER");
             String password = System.getenv("DB_PASSWORD");
             if (jdbcUrl == null || username == null || password == null) {
                logger.error("Environment variables required: DB_URL, DB_USER, DB_PASSWORD");
                 throw new IllegalStateException("Environment variables are mssing");

             }
             config.setJdbcUrl(jdbcUrl);
             config.setUsername(username);
             config.setPassword(password);
             config.addDataSourceProperty("cachePrepStmts", "true");
             config.addDataSourceProperty("prepStmtCacheSize", "250");
             config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
             ds = new HikariDataSource(config);
             logger.info(LogUtils.success("DatabaseConnectorService connection established"));
         }catch(Exception e) {
             LogUtils.error("Error in HikariCP initialization.");
             e.printStackTrace();
         }
    }

    private DatabaseConnectorService() {


    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();

    }
}

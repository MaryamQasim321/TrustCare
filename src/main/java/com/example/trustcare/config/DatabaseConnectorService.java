package com.example.trustcare.config;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnectorService {



    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    static String jdbcUrl = System.getenv("DB_URL");
    static String username = System.getenv("DB_USER");
    static String password = System.getenv("DB_PASSWORD");
    static {
        config.setJdbcUrl( jdbcUrl);
        config.setUsername( username );
        config.setPassword( password );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }

    private DatabaseConnectorService() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();

    }
}

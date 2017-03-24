/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLCloseListener;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import java.sql.Connection;
import javax.inject.Provider;

/**
 * MySQl is an util class aimed to get a MySQL queryFactory
 */
public class MySQL {
    
    /**
     * @return a new MySQLQueryFactory to get database connections
     */
    public static MySQLQueryFactory getMySQLQueryFactory() {
        Provider<Connection> provider = MySQLConnectionProvider.getInstance();
        
        return new MySQLQueryFactory(getQueryDSLConfiguration(), provider);
    }
    
    /**
     * @return the configuration for the data source. Configured to 
     *         automatically close database connections. 
     */
    private static Configuration getQueryDSLConfiguration() {
        Configuration conf = new Configuration(SQLTemplates.DEFAULT);
        conf.addListener(SQLCloseListener.DEFAULT);
        
        return conf;
    }
    
}

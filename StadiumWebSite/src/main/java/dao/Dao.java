package dao;

import com.querydsl.sql.mysql.MySQLQueryFactory;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO is an abstract object aimed to create a base for DAO objects
 */
public abstract class Dao {
    
    protected MySQLQueryFactory queryFactory;
    
    /**
     * @post Makes this be a new DAO object
     */
    public Dao() {
        queryFactory = MySQL.getMySQLQueryFactory();
    }
    
    /**
     * @post Closes the connection of this object to the database
     */
    protected void closeConnection() {
        try {
            queryFactory.getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, 
                    "This connection cannot be closed", ex);
        }
    }
    
}

package by.draikon.model.db;

import by.draikon.model.exceptions.DaoException;
import by.draikon.model.exceptions.InitException;
import java.sql.*;
import static by.draikon.model.utils.Constants.*;

public class DBHelper implements AutoCloseable{

    private Connection connect;
    public DBHelper() throws DaoException {
        try{
            ConnectorDB.init();
            connect = ConnectorDB.getConnection();
        }catch ( InitException | SQLException e){
            throw new DaoException(ERROR_DB_CONNECTION + System.lineSeparator() + e.getMessage());
        }
    }
    public Statement getStatement() throws DaoException {
        Statement st = null;
        try{
            st = connect.createStatement();
        }catch (SQLException e){
            throw new DaoException(ERROR_DB_QUERY+System.lineSeparator()+e.getMessage());
        }
        return st;
    }
    public PreparedStatement getPreparedStatement(String sql) throws DaoException {
        PreparedStatement ps = null;
        try{
            ps = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        }catch (SQLException e){
            throw new DaoException(ERROR_DB_QUERY+System.lineSeparator()+e.getMessage());
        }
        return ps;
    }
    public static void closeStatement(Statement statement){
        if(statement != null){
            try{
                statement.close();
            }catch (SQLException e){
                System.err.println(ERROR_DB_CLOSE_RESOURCE+System.lineSeparator()+e.getMessage());
            }
        }
    }
    public static void closePreparedStatement(PreparedStatement statement){
        if(statement != null){
            try{
                statement.close();
            }catch (SQLException e){
                System.err.println(ERROR_DB_CLOSE_RESOURCE+System.lineSeparator()+e.getMessage());
            }
        }
    }
    public static void closeResultSet(ResultSet rs){
        try{
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }catch (SQLException e){
            System.err.println(ERROR_DB_CLOSE_RESOURCE+System.lineSeparator()+e.getMessage());
        }
    }
    public static void closeConnection(Connection cn){
        if(cn != null){
            try{
                cn.close();
            }catch(SQLException e){
                System.err.println(ERROR_DB_CLOSE_RESOURCE+System.lineSeparator()+e.getMessage());
            }
        }
    }
    public void closeConnection(){
        closeConnection(connect);
    }
    public void close(){
        closeConnection();
    }
}//end DBHelper

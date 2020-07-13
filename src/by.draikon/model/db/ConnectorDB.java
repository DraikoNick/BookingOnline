package by.draikon.model.db;

import by.draikon.model.exceptions.InitException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import static by.draikon.model.utils.Constants.*;

public class ConnectorDB {
    private static Properties properties;
    private String dbUrl;
    private String user;
    private String password;
    private static ConnectorDB connectorDB = null;

    private ConnectorDB(String dbUrl, String user, String password){
        this.dbUrl = dbUrl;
        this.user = user;
        this.password = password;
    }

    public static void setProperties(Properties properties) {
        ConnectorDB.properties = properties;
    }

    public static void init() throws InitException{
        if(properties == null){
            init(DB_PROPERTIES);    //default properties
        }else{
            init(properties);
        }
    }

    public static void init(Properties properties) throws InitException {
        String dbUrl = properties.getProperty(DB_URL);
        String user = properties.getProperty(DB_USER);
        String password = properties.getProperty(DB_PASS);
        init(dbUrl, user, password);
    }

    public static void init(String pathToProperties) throws InitException{
        ResourceBundle resource = ResourceBundle.getBundle(pathToProperties);
        String dbUrl = resource.getString(DB_URL);
        String user = resource.getString(DB_USER);
        String password = resource.getString(DB_PASS);
        init(dbUrl, user, password);
    }

    public static void init(String dbUrl, String user, String password) throws InitException{
        if(connectorDB != null){
            return;
        }
        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connectorDB = new ConnectorDB(dbUrl, user, password);
        }catch (SQLException e){
            throw new InitException(ERROR_DB_CONNECTION + System.lineSeparator() + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(connectorDB.dbUrl, connectorDB.user, connectorDB.password);
    }
}

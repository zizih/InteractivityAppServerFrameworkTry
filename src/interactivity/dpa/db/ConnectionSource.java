package interactivity.dpa.db;

import interactivity.util.IO;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * User: 无止(何梓)
 * Date: 3/22/14
 * Time: 8:19 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class ConnectionSource {

    private static BasicDataSource dataSource = null;

    public static void init() throws Exception {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (Exception e) {
//
            }
            dataSource = null;
        }
        Properties p = new Properties();
        p.load(IO.getPropertiesInputStream());
        p.getProperty("db.driverClassName", "com.mysql.jdbc.Driver");
        p.getProperty("db.url", "jdbc:mysql://localhost:3306/interactivity");
        p.getProperty("db.password", "rain");
        p.getProperty("db.username", "hk");
        p.getProperty("db.maxActive", "30");
        p.getProperty("db.maxIdle", "10");
        p.getProperty("db.maxWait", "1000");
        p.getProperty("db.removeAbandoned", "false");
        p.getProperty("db.removeAbandonedTimeout", "120");
        p.getProperty("db.testOnBorrow", "true");
        p.getProperty("db.logAbandoned", "true");
        dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
    }

    public static synchronized Connection getConnection() throws SQLException, Exception {
        if (dataSource == null) {
            init();
        }
        Connection conn = null;
        if (dataSource != null) {
            conn = dataSource.getConnection();
        }
        return conn;
    }
}

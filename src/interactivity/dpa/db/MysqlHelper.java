package interactivity.dpa.db;

import java.sql.*;

/**
 * User: 无止(何梓)
 * Date: 3/6/14
 * Time: 2:14 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class MysqlHelper {

    private static class SingletonContainer {

        private static MysqlHelper instance = new MysqlHelper();

    }

    public static MysqlHelper getInstance() {
        return SingletonContainer.instance;
    }

    public long exec(Connection conn, String sql, Object... params) {
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Long result = rs.getLong(1);
                ps.close();
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean execUpdate(Connection conn,  String sql) {
        try {
            Statement statement = conn.createStatement();
            int state = statement.executeUpdate(sql);
            statement.close();
            if (state > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exec(Connection conn,  String sql) {
        try {
            Statement statment = conn.createStatement();
            statment.execute(sql);
            statment.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet execQuery(Connection conn,String sql) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConn() {
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();    //todo
        closeConn();
    }

}

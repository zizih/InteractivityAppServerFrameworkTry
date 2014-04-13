package interactivity.dpa.db;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * User: 无止(何梓)
 * Date: 3/22/14
 * Time: 7:53 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public interface IDBDao<T> {

    T fetchOne(Connection conn, long id) throws SQLException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException;

    List<T> fetch(Connection conn) throws SQLException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException;

    boolean insert(Connection conn, T t) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    boolean update(Connection conn, T t);

    boolean delete(Connection conn, long id);
}

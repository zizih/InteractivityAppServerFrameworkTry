package interactivity.dpa.db;

import java.sql.Connection;
import java.util.List;

/**
 * User: 无止(何梓)
 * Date: 3/22/14
 * Time: 7:53 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public interface IDBDao<T> {

    T fetchOne(Connection conn, long id);

    List<T> fetch(Connection conn);

    boolean insert(Connection conn, T t);

    boolean update(Connection conn, T t);

    boolean delete(Connection conn, long id);
}

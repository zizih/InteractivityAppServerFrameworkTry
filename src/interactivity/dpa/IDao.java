package interactivity.dpa;

import java.util.List;

/**
 * User: 无止(何梓)
 * Date: 3/22/14
 * Time: 7:53 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public interface IDao<T> {

    T fetchOne(long id);

    List<T> fetch();

    boolean insert(T t);

    boolean update(T t);

    boolean delete(long id);
}

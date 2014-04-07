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


    List<T> fetch() throws Exception;

    T fetchOne(long id) throws Exception;

    boolean insert(T t) throws Exception;

    boolean update(T t) throws Exception;

    boolean delete(long id) throws Exception;
}

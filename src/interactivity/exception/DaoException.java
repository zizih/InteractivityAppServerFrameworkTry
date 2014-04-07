package interactivity.exception;

/**
 * User: 无止(何梓)
 * Date: 4/6/14
 * Time: 1:35 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class DaoException extends Exception {

    public DaoException() {
        super("数据操作错误");
    }

    public DaoException(String message) {
        super(message);
    }
}

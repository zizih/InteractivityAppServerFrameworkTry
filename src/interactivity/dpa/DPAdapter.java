package interactivity.dpa;

import interactivity.util.IO;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

/**
 * User: 无止(何梓)
 * Date: 3/22/14
 * Time: 10:01 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class DPAdapter<T> implements IDao<T> {

    private Class<T> tClzz;
    //0:mem
    //1:log
    //2:mysql
    private int STOREAGE;

    public DPAdapter() throws IOException {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        tClzz = (Class) params[0];
        Properties p = new Properties();
        p.load(IO.getPropertiesInputStream());
        String storage = p.getProperty("storage", "mem");
        if (storage.equals("log")) {
            STOREAGE = 1;
        } else if (storage.equals("mysql")) {
            STOREAGE = 2;
        } else {
            STOREAGE = 0;
        }
    }


    @Override
    public T fetchOne(long id) {
        switch (STOREAGE) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        return null;  //todo
    }

    @Override
    public List<T> fetch() {
        switch (STOREAGE) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        return null;  //todo
    }

    @Override
    public boolean insert(T t) {
        switch (STOREAGE) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        return false;  //todo
    }

    @Override
    public boolean update(T t) {
        switch (STOREAGE) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        return false;  //todo
    }

    @Override
    public boolean delete(long id) {
        switch (STOREAGE) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        return false;  //todo
    }
}

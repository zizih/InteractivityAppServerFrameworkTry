package interactivity.dpa;

import interactivity.dpa.db.ConnectionSource;
import interactivity.dpa.db.DBBaseDao;
import interactivity.util.IO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * User: 无止(何梓)
 * Date: 3/22/14
 * Time: 10:01 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class DaoAdapter<T extends Model> implements IDao<T> {

    protected Class<T> tClzz;
    //0:mem
    //1:txt
    //2:db
    private int STOREAGE;
    private IDao dao;

    public DaoAdapter() throws Exception {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        tClzz = (Class) params[0];

        Properties p = new Properties();
        p.load(IO.getPropertiesInputStream());
        String storage = p.getProperty("storage", "mem");
        if (storage.equals("txt")) {
            STOREAGE = 1;
            dao = new TxtDao(tClzz);
        } else if (storage.equals("db")) {
            STOREAGE = 2;
            dao = new DBDao(tClzz);
        } else {
            STOREAGE = 0;
            dao = new MemDao();
        }
    }


    @Override
    public T fetchOne(long id) throws Exception {
        return (T) dao.fetchOne(id);
    }

    @Override
    public List<T> fetch() throws Exception {
        return dao.fetch();
    }

    @Override
    public boolean insert(T t) throws Exception {
        return dao.insert(t);
    }

    @Override
    public boolean update(T t) throws Exception {
        return dao.update(t);
    }

    @Override
    public boolean delete(long id) throws Exception {
        return dao.delete(id);
    }

    class TxtDao extends interactivity.dpa.TxtDao<T> {

        public TxtDao(Class<T> tClzz) throws IOException {
            super(tClzz);
        }
    }

    class MemDao extends interactivity.dpa.MemDao<T> {

    }

    class DBDao extends DBBaseDao<T> implements IDao<T> {

        public DBDao(Class<T> tClzz) throws Exception {
            super(tClzz);
            ConnectionSource.init();
        }

        @Override
        public List<T> fetch() throws Exception {
            Connection conn = ConnectionSource.getConnection();
            List<T> list = fetch(conn);
            conn.close();
            return list;
        }

        @Override
        public T fetchOne(long id) throws Exception {
            Connection conn = ConnectionSource.getConnection();
            T t = fetchOne(conn, id);
            conn.close();
            return t;
        }

        @Override
        public boolean insert(T t) throws Exception {
            Connection conn = ConnectionSource.getConnection();
            boolean state = insert(conn, t);
            conn.close();
            return state;
        }

        @Override
        public boolean update(T t) throws Exception {
            Connection conn = ConnectionSource.getConnection();
            boolean state = update(conn, t);
            conn.close();
            return state;
        }

        @Override
        public boolean delete(long id) throws Exception {
            Connection conn = ConnectionSource.getConnection();
            boolean state = delete(conn, id);
            conn.close();
            return state;
        }
    }
}

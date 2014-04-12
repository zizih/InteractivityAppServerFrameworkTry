package test.tmp;

import app.model.Client;
import interactivity.Person;
import interactivity.dpa.DaoAdapter;
import interactivity.dpa.db.Colum;
import interactivity.dpa.db.Key;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Properties;

/**
 * User: 无止(何梓)
 * Date: 3/19/14
 * Time: 8:19 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class Test {


    public static void main(String[] args) {
        Client c = new Client();
        c.setDesc(Client.Desc.dios);
        c.setId(123);
        c.setIp("10.50.9.186");
        c.setNick("hezi");
        c.setPort("9080");

        try {
            ClientDao dao = new ClientDao();
            dao.insert(c);
            dao.insert(c);
        System.out.println(dao.fetch().size());
        } catch (Exception e) {
            e.printStackTrace();  //deal with ex
        }
    }

    static class ClientDao extends DaoAdapter<Client> {

        public ClientDao() throws Exception {
        }
    }


    public static void main2123(String[] args) {
        ModelTest m = new ModelTest();
        m.setName("Hezi");
        Field[] fields = m.getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.println("..: " + field.getName());
            Annotation anno = field.getAnnotation(Key.class);
            System.out.println(anno == null);
            System.out.println(((Key) anno).name());
        }
    }

    static class ModelTest {

        @Key(auto = true, name = "name")
        private String id;

        @Colum(name = "", format = "", type = "")
        private long idd;

        public String getName() {
            return id;
        }

        public void setName(String name) {
            this.id = name;
        }
    }

    public static void main2(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        String rootPath = System.getProperty("user.dir");
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(rootPath + "/conf/application.conf"));
            System.out.println(p.getProperty("dbi", "mem"));
        } catch (IOException e) {
            e.printStackTrace();  //deal with ex
        }
    }

    public static void maion(String[] args) {
//        new Test().test();
        try {
            Person p = Person.class.newInstance();
            p.setName("hezi");
            System.out.println(p.getName());
        } catch (InstantiationException e) {
            e.printStackTrace();  //deal with ex
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //deal with ex
        }
    }

    public void test() {
        EntityDao testDao = new EntityDao();
        Entity e = testDao.get();
    }

    public void test2() {
        HBaseDao<Entity> en = new HBaseDao<Entity>();
        en.get();
    }

    interface BaseDao<T> {
        T get();
    }

    class HBaseDao<T> implements BaseDao<T> {
        Class<T> entityClass;

        public HBaseDao() {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            entityClass = (Class) params[0];
            System.out.println(entityClass.getName());
        }

        public T get() {
            try {
                System.out.println(entityClass.getName());
                return entityClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();  //deal with ex
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //deal with ex
            }
            return null;
        }
    }

    class Entity {
        String name;

        public Entity() {
            super();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class EntityDao extends HBaseDao<Entity> {

    }


}

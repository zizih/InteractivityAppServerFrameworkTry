package interactivity.dpa.db;

import interactivity.dpa.Model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: 无止(何梓)
 * Date: 3/11/14
 * Time: 10:17 AM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class DBBaseDao<T extends Model> implements IDBDao<T> {

    private Class<T> tClzz;

    public DBBaseDao(Class<T> tClzz) {
        this.tClzz = tClzz;
//        Type genType = getClass().getGenericSuperclass();
//        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
//        tClzz = (Class) params[0];


    }

    @Override
    public T fetchOne(Connection conn, long id) {
        MysqlHelper mysqlHelper = MysqlHelper.getInstance();
        Table anno = (Table) tClzz.getAnnotation(Table.class);
        ResultSet rs = mysqlHelper.execQuery(conn, "select * from " + anno.name() + " where id=" + id);
        try {
            if (rs.next()) {
                return toModel(tClzz, rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //deal with ex
        }
        return null;
    }

    @Override
    public List<T> fetch(Connection conn) {
        MysqlHelper mysqlHelper = MysqlHelper.getInstance();
        Table anno = (Table) tClzz.getAnnotation(Table.class);
        ResultSet rs = mysqlHelper.execQuery(conn, "select * from " + anno.name());
        try {
            List<T> list = new ArrayList<T>();
            while (rs.next()) {
                list.add(toModel(tClzz, rs));
            }
            rs.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();  //deal with ex
        }

        return null;
    }

    @Override
    public boolean insert(Connection conn, T t) {
        if (!t.getClass().isAnnotationPresent(Table.class)) {
            return false;
        }
        String tableName = t.getClass().getAnnotation(Table.class).name();
        StringBuilder colums = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        int kvSize = 0;
        String[] columsStr = getColums(t.getClass());
        Object[] valuesObj = getValues(t);
        if (columsStr.length != valuesObj.length) {
            return false;
        }
        for (int i = 0; i < columsStr.length; i++) {
            if (columsStr[i] == null || columsStr[i] == "") {
                continue;
            }
            if (!(kvSize == 0)) {
                colums.append(",");
                values.append(",");
            } else {
                kvSize++;
            }
            colums.append("`").append(columsStr[i]).append("`");
            //这里要对值做不同的append。
            Object tmp = valuesObj[i];
            if (tmp.equals(Integer.class) || tmp.equals(int.class) || tmp.equals(long.class)
                    || tmp.equals(float.class) || tmp.equals(double.class)) {
                values.append(valuesObj[i]);
            } else {
                values.append("'").append(valuesObj[i]).append("'");
            }

        }
        colums.append(") ");
        values.append(")");
        String sql = "insert into " + tableName + colums + "values" + values;

        //发送sql语句并执行
        MysqlHelper dbHelper = MysqlHelper.getInstance();
        return dbHelper.exec(conn, sql);
    }

    @Override
    public boolean delete(Connection conn, long id) {
        MysqlHelper mysqlHelper = MysqlHelper.getInstance();
        Table anno = (Table) tClzz.getAnnotation(Table.class);
        return mysqlHelper.exec(conn, "delete from " + anno.name() + " where id=" + id);
    }

    @Override
    public boolean update(Connection conn, T t) {
        return false;
    }

    private Object[] getValues(T t) {
        if (!t.getClass().isAnnotationPresent(Table.class)) {
            return null;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        Object[] values = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            values[i] = callGetter(t, f.getName());
        }
        return values;
    }

    private T toModel(Class clzz, ResultSet rs) {
        try {
            T t = (T) clzz.newInstance();
            String[] colums = getColums(clzz);
            for (String colum : colums) {
                callSetter(t, colum, rs.getObject(rs.findColumn(colum)));
            }
            rs.close();
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();  //deal with ex
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //deal with ex
        } catch (SQLException e) {
            e.printStackTrace();  //deal with ex
        }
        return null;
    }

    private Object callGetter(T t, String fieldName) {
        String setterName = "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1, fieldName.length());
        try {
            Class clzz = t.getClass();
            Method method = clzz.getDeclaredMethod(setterName);
            return method.invoke(t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void callSetter(T t, String fieldName, Object value) {
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1, fieldName.length());
        try {
            Class clzz = t.getClass();
            Field field = clzz.getDeclaredField(fieldName);
            Class type = field.getType();
            Method method = clzz.getDeclaredMethod(setterName, type);

            if (type == long.class) {
                method.invoke(t, Long.valueOf(value.toString()));
                return;
            } else if (type == int.class) {
                method.invoke(t, Integer.valueOf(value.toString()));
                return;
            }
            method.invoke(t, value);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private String[] getColums(Class clzz) {
        Field[] fields = clzz.getDeclaredFields();
        if (fields != null && fields.length != 0) {
            String[] colums = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                colums[i] = fields[i].getName();
            }
            return colums;
        }
        return null;
    }

}

package interactivity.dpa;

import interactivity.dpa.db.Key;
import interactivity.exception.DaoException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: 无止(何梓)
 * Date: 3/27/14
 * Time: 10:50 AM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class Mem<T extends Model> implements IDao<T> {

    private Map<Object, T> cache;
    private AtomicLong idCounter;

    public Mem() {
        cache = new HashMap<Object, T>();
        idCounter = new AtomicLong(cache.size());
    }

    @Override
    public T fetchOne(long id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        return null;  //todo
    }

    @Override
    public List<T> fetch() {
        List<T> list = new ArrayList<T>();
        if (cache.size() == 0) {
            return null;
        }
        for (Map.Entry<Object, T> entry : cache.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    @Override
    public boolean insert(T t) {
        long id = Long.parseLong(callGetter(t, "id").toString());
        if (id > idCounter.get()) {
            idCounter.getAndSet(id);
        }
        idCounter.getAndIncrement();
        callSetter(t, "id", idCounter.get());
        cache.put(idCounter.get(), t);
        return true;  //todo
    }

    @Override
    public boolean update(T t) throws DaoException {
        if (!cache.containsKey(getkeyField(t))) {
            return false;
        }
        cache.put(callGetter(t, getkeyField(t)), t);
        return true;
    }

    @Override
    public boolean delete(long id) throws DaoException {
        if (!cache.containsKey(id)) {
            throw new DaoException("缓存中不存在该key所在的对象");
        }
        cache.remove(id);
        return true;  //todo
    }

    private String getkeyField(T t) throws DaoException {
        Field[] fields = t.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            throw new DaoException("模型没有字段");
        }
        for (Field field : fields) {
            Annotation anno = t.getClass().getAnnotation(Key.class);
            if (anno != null) {
                String keyName = ((Key) anno).name();
                if (keyName == null) {
                    return keyName;
                }
                return field.getName();
            }
        }
        return fields[0].getName();
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
}

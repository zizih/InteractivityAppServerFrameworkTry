package interactivity.dpa;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: 无止(何梓)
 * Date: 4/11/14
 * Time: 5:37 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class BaseDao<T> {


    protected void callSetter(T t, String fieldName, Object value) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1, fieldName.length());
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
    }

    protected Object callGetter(T t, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String setterName = "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1, fieldName.length());
        Class clzz = t.getClass();
        Method method = clzz.getDeclaredMethod(setterName);
        return method.invoke(t);
    }
}

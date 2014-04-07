package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: 无止(何梓)
 * Date: 4/7/14
 * Time: 8:46 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class C<K> implements A<K> {

    private Class<K> tClzz;

    @Override
    public void say() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        tClzz = (Class) params[0];
        System.out.println("C: " + tClzz.getName());
    }
}
package test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: 无止(何梓)
 * Date: 4/7/14
 * Time: 8:45 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class B<T> implements A<T> {

    private Class<T> tClzz;

    @Override
    public void say() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        tClzz = (Class) params[0];
        System.out.println("B: " + tClzz.getName());

        new BB().f();
    }

    class BB extends C<T> {
        public void f() {
            super.say();
        }
    }
}
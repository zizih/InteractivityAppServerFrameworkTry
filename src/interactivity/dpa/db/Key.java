package interactivity.dpa.db;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 11/10/13
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {

    boolean auto();

    String name();

}

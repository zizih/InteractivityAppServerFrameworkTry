package interactivity.net;

import interactivity.mvc.model.Application;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * User: 无止(何梓)
 * Date: 4/8/14
 * Time: 2:51 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class Reactor {

    void execute(SelectionKey key, Application app) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException {

    }

    protected ByteBuffer toByteBuffer(String str) {
        return ByteBuffer.wrap(new String(str).getBytes());
    }

}

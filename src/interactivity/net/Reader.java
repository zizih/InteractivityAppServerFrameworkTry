package interactivity.net;

import interactivity.mvc.model.Application;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * User: 无止(何梓)
 * Date: 4/8/14
 * Time: 2:52 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class Reader extends Reactor {

    @Override
    public void execute(SelectionKey key, Application app) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        System.out.println("Received:  " + new String(data).trim());

        String resultStr = null;
        if (data.length <= 0) {
            resultStr = "空命令";
        } else {
            String[] datas = new String(data).trim().split(":");
            int len = datas.length;
            if (len < 0) {
                resultStr = "空命令";
            } else if (len >= 2) {
                if (datas[1].equals("exit")) {
                    byteBuffer.clear();
                    key.cancel();
                    channel.close();
                    return;
                } else {
                    resultStr = app.invoke(datas) +"\n"+ app.nextCommond(datas[0]).getPrompt();
                }
            }
        }
        channel.write(toByteBuffer(resultStr));
        byteBuffer.clear();

//        channel.register(key.selector(), SelectionKey.OP_WRITE, new Writer(byteBuffer));
    }

}

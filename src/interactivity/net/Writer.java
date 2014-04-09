package interactivity.net;

import interactivity.mvc.model.Application;

import java.io.IOException;
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
public class Writer extends Reactor {

    private ByteBuffer output;

    public Writer(ByteBuffer output) {
        this.output = output;
    }

    @Override
    public void execute(SelectionKey key, Application app) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.read(output);
        byte[] data = output.array();

        String resultStr = null;
        if (data.length <= 0) {
            resultStr = "空命令";
        } else {
            String[] datas = new String(data).trim().split(":");
            int len = datas.length;
            if (len < 0) {
                resultStr = "空命令";
            } else {
                resultStr = app.invoke(datas) + app.nextCommond(datas[0]).getPrompt();
            }
        }
        channel.write(toByteBuffer(resultStr));
        output.clear();
    }
}

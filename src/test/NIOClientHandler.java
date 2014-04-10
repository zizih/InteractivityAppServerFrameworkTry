package test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * User: 无止(何梓)
 * Date: 3/10/14
 * Time: 4:27 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class NIOClientHandler implements NIOClient.IHandler {
    @Override
    public void handleConnect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()) {
            channel.finishConnect();
        }
        channel.configureBlocking(false);
        channel.write(ByteBuffer.wrap("initComm:show".getBytes()));
        channel.register(key.selector(), SelectionKey.OP_READ);
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        //todo
    }

    int i = 0;

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        //读取服务器返回的数据
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        channel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        System.out.println(new String(data).trim());

        //客户端输入
        String cmd = null;
        switch (i) {
            case 0:
                cmd = "initComm:demo";
                i++;
                break;
            case 1:
                cmd = "oneComm:todo:" + i + " hate " + i;
                i++;
                break;
            case 2:
                cmd = "twoComm:todo:" + i * 2 + " hate " + i * 3;
                i++;
                break;
            case 3:
                cmd = "thrComm:wish";
                i++;
                break;
            case 4:
                cmd = "initComm:show";
                i++;
                break;
            case 5:
                cmd = "oneComm:exit";
                break;
        }
        channel.write(ByteBuffer.wrap(cmd.getBytes()));
        channel.register(key.selector(), SelectionKey.OP_READ);
        if (cmd.contains("exit")) {
            System.exit(0);
        }
    }
}

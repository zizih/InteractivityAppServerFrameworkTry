package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * User: 无止(何梓)
 * Date: 3/5/14
 * Time: 5:52 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class NIOClient {

    private Selector selector;
    private IHandler hanlder;

    public NIOClient(IHandler handler) {
        this.hanlder = handler;
    }

    public NIOClient init(String ip, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        this.selector = Selector.open();
        channel.connect(new InetSocketAddress(ip, port));
        channel.register(this.selector, SelectionKey.OP_CONNECT);
        return this;
    }

    public void listen() throws IOException {
        while (true) {
            selector.select();
            Iterator iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (key.readyOps() == SelectionKey.OP_CONNECT) {
                    hanlder.handleConnect(key);
                } else if (key.readyOps() == SelectionKey.OP_READ) {
                    hanlder.handleRead(key);
                }

            }

        }
    }

    public static void main(String[] args) {
        try {
            new NIOClient(new NIOClientHandler()).init("localhost", 9000).listen();
        } catch (IOException e) {
            e.printStackTrace();  //deal with ex
        }
    }

    public interface IHandler {

        void handleConnect(SelectionKey key) throws IOException;

        void handleWrite(SelectionKey key) throws IOException;

        void handleRead(SelectionKey key) throws IOException;

    }

}

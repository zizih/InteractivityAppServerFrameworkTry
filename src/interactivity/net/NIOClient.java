package interactivity.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
    private ClientHandler hanlder;

    public NIOClient() {
        this.hanlder = new ClientHandler();
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
                } else if (key.readyOps() == SelectionKey.OP_WRITE) {
                    hanlder.handleWrite(key);
                }

            }

        }
    }

    class ClientHandler {

        private void handleConnect(SelectionKey key) throws IOException {
            SocketChannel channel = (SocketChannel) key.channel();
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            }
            channel.configureBlocking(false);
            channel.write(ByteBuffer.wrap("Client hased received Server connection reponse!".getBytes()));
            channel.register(key.selector(), SelectionKey.OP_READ);
        }

        private void handleWrite(SelectionKey key) throws IOException {
            //todo
        }

        private void handleRead(SelectionKey key) throws IOException {
            //读取服务器返回的数据
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            channel.read(byteBuffer);
            byte[] data = byteBuffer.array();
            System.out.println(new String(data).trim());

            //客户端输入
            StringBuffer sb = new StringBuffer();
            System.out.println("Please input somthing to server: ");
            while (true) {
                char c = (char) System.in.read();
                sb.append(c);
                if (c == '\n') {
                    channel.write(ByteBuffer.wrap(sb.toString().getBytes()));
                    channel.register(key.selector(), SelectionKey.OP_READ);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new NIOClient().init("localhost", 9000).listen();
        } catch (IOException e) {
            e.printStackTrace();  //deal with ex
        }
    }

}

package test.tmp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: 无止(何梓)
 * Date: 3/5/14
 * Time: 5:52 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class NIOClient {

    private Selector selector;

    public NIOClient() {
    }

    public NIOClient init(String ip, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        this.selector = Selector.open();
        channel.connect(new InetSocketAddress(ip, port));
        channel.register(this.selector, SelectionKey.OP_CONNECT);
        return this;
    }

    public void listen() throws Exception {
        while (selector.isOpen()) {
            selector.select();
            Iterator iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (key.readyOps() == SelectionKey.OP_CONNECT) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }
                    channel.configureBlocking(false);
                    channel.register(key.selector(), SelectionKey.OP_READ, new Reader());
                } else if (key.readyOps() == SelectionKey.OP_READ) {
                    Reader reader = (Reader) key.attachment();
                    reader.excute(key);
                }

            }
        }
    }

    public static void main(String[] args) {
        try {
            new NIOClient().init("localhost", 9000).listen();
        } catch (IOException e) {
            e.printStackTrace();  //deal with ex
        } catch (Exception e) {
            e.printStackTrace();  //deal with ex
        }
    }

    class Reader {

        private AtomicInteger i = new AtomicInteger(0);

        public void excute(SelectionKey key) throws Exception {
            //读取服务器返回的数据
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
            channel.read(byteBuffer);
            byte[] data = byteBuffer.array();
            System.out.println(new String(data).trim());

            //客户端输入
            String cmd = null;
            switch (((int) (Math.random() * 4)) % 4) {
                case 0:
                    cmd = "myComm:insert:" + System.currentTimeMillis() + "name:" + System.currentTimeMillis();
                    break;
                case 1:
                    cmd = "myComm:search:" + System.currentTimeMillis();
                    break;
                default:
                    cmd = "myComm:list";
                    break;
            }
            channel.write(ByteBuffer.wrap(cmd.getBytes()));
        }
    }

}

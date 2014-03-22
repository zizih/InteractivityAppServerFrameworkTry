package interactivity.net;

import interactivity.mvc.model.Application;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


/**
 * User: 无止
 * Date: 2/28/14
 * Time: 4:21 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class NIOServer<T> implements IServer<T> {

    //通道管理器
    private Selector selector;
    private ServerSocket socket;
    private ServerHandler handler;

    private Class<T> entityClass;

    public NIOServer() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
        System.out.println(entityClass.getName());
        handler = new ServerHandler(entityClass);
    }

    public NIOServer init(int port) throws IOException {

        //打开ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        this.socket = serverChannel.socket();
        socket.bind(new InetSocketAddress(port));

        //打开管理器
        this.selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        return this;
    }

    @Override
    public void listen() {
        System.out.println("Server started...");
        try {
            while (true) {
                selector.select();
                Iterator iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();
                    iterator.remove();
                    if (key.readyOps() == SelectionKey.OP_ACCEPT) {
                        handler.handleAccept(key);
                    } else if (key.readyOps() == SelectionKey.OP_READ) {
                        handler.handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.socket.close();
    }

    class ServerHandler {

        private Class handlerClzz;

        ServerHandler(Class clzz) {
            this.handlerClzz = clzz;
        }

        private void handleAccept(SelectionKey key) throws IOException {
            //todo
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            channel.write(ByteBuffer.wrap(
                    ("Server haved received your connection,and your address: "
                            + channel.socket().getInetAddress() + "")
                            .getBytes()));
            channel.register(key.selector(), SelectionKey.OP_READ);
        }

        private void handleRead(SelectionKey key) throws IOException {
            //todo
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            channel.read(byteBuffer);
            byte[] data = byteBuffer.array();
            System.out.println(new String(data).trim());

            String resultStr = null;
            if (data.length <= 0) {
                resultStr = "空命令";
            } else {
                String[] datas = new String(data).trim().split(":");
                int len = datas.length;
                if (len < 0) {
                    resultStr = "空命令";
                } else {
                    try {
                        Application baseApp = (Application) handlerClzz.newInstance();
                        Application app = baseApp.newApp();
                        resultStr = app.invoke(datas[0], datas[1], cutStrArr(datas, 2, len));
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultStr = "出错，请给正确的命令！";
                    }
                }
            }
            channel.write(toByteBuffer(resultStr));
            channel.register(key.selector(), SelectionKey.OP_READ);
        }

        private String[] cutStrArr(String[] rcArr, int start, int end) {
            if (rcArr == null) return null;
            int len = rcArr.length;
            if (start < 0) start = 0;
            if (end < len) end = len - 1;
            String[] resultArr = new String[end - start];
            for (int i = start; i < end; i++) {
                resultArr[i - start] = rcArr[i];
            }
            return resultArr;
        }

        private ByteBuffer toByteBuffer(String str) {
            return ByteBuffer.wrap(new String(str).getBytes());
        }

        private void handleWrite(SelectionKey key) throws IOException {
            //todo
        }
    }
}

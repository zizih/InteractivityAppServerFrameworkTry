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
public class NIOServer<T extends Application> implements IServer<T> {

    //通道管理器
    private Selector selector;
    private ServerSocket socket;
    private ServerHandler handler;

    private Class<T> tClzz;

    public NIOServer() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        tClzz = (Class) params[0];
        System.out.println(tClzz.getName());
        handler = new ServerHandler(tClzz);
    }

    @Override
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
        private Application app;

        ServerHandler(Class clzz) {
            this.handlerClzz = clzz;
        }

        private void handleAccept(SelectionKey key) throws IOException {
            // 获得和客户端连接的通道
            ServerSocketChannel server = (ServerSocketChannel) key
                    .channel();
            SocketChannel channel = server.accept();
            // 设置成非阻塞
            channel.configureBlocking(false);

            //给客户端发送信息
            if (app == null) {
                Application baseApp = null;
                try {
                    baseApp = (Application) handlerClzz.newInstance();
                    app = baseApp.newApp();
                    channel.write(toByteBuffer(app.getCommand("initComm").getPrompt()));
                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                    channel.register(key.selector(), SelectionKey.OP_READ);
                } catch (Exception e) {
                    e.printStackTrace();  //deal with ex
                }
            }
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
                    resultStr = app.invoke(datas) + app.nextCommond(datas[0]).getPrompt();
                }
            }
            channel.write(toByteBuffer(resultStr));
            channel.register(key.selector(), SelectionKey.OP_READ);
        }

        private ByteBuffer toByteBuffer(String str) {
            return ByteBuffer.wrap(new String(str).getBytes());
        }

        private void handleWrite(SelectionKey key) throws IOException {
            //todo
        }
    }
}

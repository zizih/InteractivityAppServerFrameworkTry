package interactivity.net;

import interactivity.mvc.model.Application;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private Application app;

    private Class<T> tClzz;

    public NIOServer() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        tClzz = (Class) params[0];
        System.out.println(tClzz.getName());
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
    public void listen() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException, NoSuchFieldException {
        System.out.println("Server started...");
        while (true) {
            selector.select();
            Iterator iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key
                            .channel();
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);

                    if (app == null) {
                        Application baseApp = (Application) tClzz.newInstance();
                        app = baseApp.newApp();
                    }
                    boolean state = Boolean.parseBoolean(app.invoke("initComm:init".split(":")));
                    if (!state) {
                        channel.write(toByteBuffer("Oop,Something wrong on server,Sorry!"));
                        break;
                    }
                    channel.write(toByteBuffer(app.getFirstCommand().getPrompt()));
                    //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                    channel.register(key.selector(), SelectionKey.OP_READ, new Reader());
                } else if (key.isReadable() || key.isWritable()) {
                    Reactor reactor = (Reactor) key.attachment();
                    reactor.execute(key, app);
                }
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.socket.close();
    }

    private ByteBuffer toByteBuffer(String str) {
        return ByteBuffer.wrap(new String(str).getBytes());
    }

}

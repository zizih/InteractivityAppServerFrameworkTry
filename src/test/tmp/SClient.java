package test.tmp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * User: 无止
 * Date: 2/27/14
 * Time: 5:04 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class SClient extends Thread {

    private Socket server;
    private OutputStream os;
    private static String[] cmds = {
            "myComm:exit",
            "myComm:insert:" + System.currentTimeMillis() + ":" + (Math.random() * 100),
            "myComm:list",
            "myComm:search:" + System.currentTimeMillis()
    };
    ;

    public SClient(String ip, int port) throws IOException {
        this.server = new Socket(ip, port);
        this.os = server.getOutputStream();
    }

    @Override
    public void run() {
        try {
            os.write(cmds[1].getBytes());
            os.flush();
//            os.write(cmds[2].getBytes());
//            os.flush();
            os.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();  //deal with ex
        }
    }

    private static int concurrencyNum = 1;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(concurrencyNum + " thead");
        for (int i = 0; i < concurrencyNum; i++) {
            try {
                new SClient("localhost", 9000).start();
            } catch (IOException e) {
                e.printStackTrace();  //deal with ex
            }
        }
        System.out.println("客户端输入的命令：" + cmds[1]);
//        try {
//            Socket socket = new Socket("localhost", 9000);
//            while (!socket.isConnected()) {
//                socket.close();
//                break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();  //deal with ex
//        }
        System.out.println("excuted period:  " + (System.currentTimeMillis() - start));
    }

}

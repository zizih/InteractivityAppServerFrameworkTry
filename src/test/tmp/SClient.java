package test.tmp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * User: 无止
 * Date: 2/27/14
 * Time: 5:04 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class SClient {

    private Socket server;
    private OutputStream os;
    private InputStream is;
    private int i = 0;

    public SClient(String ip, int port) throws IOException {
        this.server = new Socket(ip, port);
        this.is = server.getInputStream();
        this.os = server.getOutputStream();

        new Thread() {
            @Override
            public void run() {
                try {
                    os.write("myComm:exit".getBytes());
                    os.flush();
                    os.close();
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();  //deal with ex
                }
            }
        }.start();

    }

    private static int concurrencyNum = 100000;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(start);
        for (int i = 0; i < concurrencyNum; i++) {
            try {
                new SClient("localhost", 9000);
            } catch (IOException e) {
                e.printStackTrace();  //deal with ex
            }
        }
        System.out.println(" period:  " + (System.currentTimeMillis() - start));
    }

}

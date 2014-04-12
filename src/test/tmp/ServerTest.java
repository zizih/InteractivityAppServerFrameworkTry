package test.tmp;

/**
 * User: 无止(何梓)
 * Date: 4/10/14
 * Time: 10:37 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class ServerTest {

    private static int concurrencyNum = 10;

    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
//        System.out.println(start);
        for (int i = 0; i < concurrencyNum; i++) {
            new Thread() {
                @Override
                public void run() {
                    NIOClient client = null;
                    try {
                        client = new NIOClient().init("localhost", 9000);
                        client.listen();
                    } catch (Exception e) {
                        e.printStackTrace();  //deal with ex
                    }
                }
            }.start();
        }
//        System.out.println(" period:  " + (System.currentTimeMillis() - start));
    }

}

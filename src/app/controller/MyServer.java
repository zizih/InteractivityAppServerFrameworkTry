package app.controller;

import interactivity.net.NIOServer;

/**
 * User: 无止(何梓)
 * Date: 3/20/14
 * Time: 7:25 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class MyServer extends NIOServer<MyApp> {

    public static void main(String[] args) {
        try {
            new MyServer().init(9000).listen();
        } catch (Exception e) {
            e.printStackTrace();  //deal with ex
        }
    }

}

package app.controller;

/**
 * User: 无止(何梓)
 * Date: 3/21/14
 * Time: 9:53 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class MyHandler {


    enum From {rain, jack, rose, aaron}

    enum To {dios, lucy, ruby, anson}

    enum Action {like, ignore, hate, love}

    //默认callbak方法的参数都是第一个为ip:port 第二个之后为客户端传入的第三个参数
    public String demo(String addr) {

        StringBuffer sb = new StringBuffer("♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥\n");

        //默认都一样长
        int len = From.values().length;
        int i = 0;
        for (From from : From.values()) {
            int[] indexs = {(int) (Math.random() * len), (int) (Math.random() * len)};
            sb.append(from + " " + Action.values()[indexs[0]] + " " + To.values()[indexs[1]] + "\n");
            i++;
        }
        return sb.append("♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥\n").toString();
    }

    public String todo(String addr, String todo) {
        return null;
    }


}

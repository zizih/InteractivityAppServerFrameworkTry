package test;

import app.model.Event;

/**
 * User: 无止(何梓)
 * Date: 4/7/14
 * Time: 8:34 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class Main {


    public static void main(String[] args) {
        new Mod().say();
    }


    static class Mod extends B<Event> {
    }

}

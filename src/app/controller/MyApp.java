package app.controller;

import interactivity.mvc.model.Application;
import interactivity.mvc.model.Command;
import interactivity.mvc.model.Option;

/**
 * User: 无止(何梓)
 * Date: 3/21/14
 * Time: 8:55 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class MyApp extends Application<MyHandler> {

    @Override
    public Application newApp() {
        Application app = new MyApp();
        Command initComm, oneComm, twoComm, thrComm;      //有这些命令
        Option demo, todo, exit, regist, show, wish; //有这些开关

        //第一个option
        initComm = app.newCommand("initComm", "oneComm", "Successfully!");
        initComm.setId("initComm");
        initComm.setNextId("oneComm");
        initComm.setPrompt("Successfully!");
        demo = app.newOption();
        demo.setId("demo");
        demo.setPrompt("查看演示");
        show = app.newOption();
        show.setId("show");
        show.setPrompt("去看看这里都有谁在");
        wish = app.newOption();
        wish.setId("wish");
        wish.setPrompt("查看对对方使用相同动词的人");
        exit = app.newOption();
        exit.setId("exit");
        exit.setPrompt("退出");
        app.addCommand(initComm, demo, show, wish, exit);

        //第二个option  //有复用，按需设计
        oneComm = app.newCommand();
        oneComm.setId("oneComm");
        oneComm.setNextId("twoComm");
        oneComm.setPrompt("你知道怎么做的～");
        demo.setPrompt("再看一次演示，会有不同结果哦");
        todo = app.newOption();
        todo.setId("todo");
        todo.setPrompt("去表达，格式是 todo: you [ignore|like|hate|unlike|love] other");
        app.addCommand(oneComm, demo, todo, show, wish, exit);    //第二个开关拥有的命令

        //第三个option  //有复用，按需设计
        twoComm = app.newCommand();
        twoComm.setId("twoComm");
        twoComm.setNextId("thrComm");
        twoComm.setPrompt("不紧张，不是刚好互相讨厌或互相其他.不会有人知道你说了什么");
        regist = app.newOption();
        regist.setId("regist");
        regist.setPrompt("留下自己的名字,可以等待被表白OR表黑，输入格式是regist:yourname");
        app.addCommand(twoComm, demo, show, todo, regist, wish, exit);

        //第三个option  //有复用，按需设计
        thrComm = app.newCommand();
        thrComm.setId("thrComm");
        thrComm.setNextId("oneComm");   //循环复用
        thrComm.setPrompt("说了就不会遗憾");
        app.addCommand(thrComm, show, todo, regist, wish, exit);
        return app;
    }

}

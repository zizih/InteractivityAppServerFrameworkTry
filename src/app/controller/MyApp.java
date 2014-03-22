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
        Command initOpt, oneOpt, twoOpt, thrOpt;      //有这些步骤
        Option demo, todo, exit, regist, show, wish; //有这些命令

        //第一个option
        initOpt = app.newOption("init", "oneOpt", "Successfully!");
        initOpt.setId("init");
        initOpt.setNextId("oneOpt");
        initOpt.setPrompt("Successfully!");
        demo = app.newCommand();
        demo.setId("demo");
        demo.setPrompt("查看演示");
        show = app.newCommand();
        show.setId("show");
        show.setPrompt("去看看这里都有谁在");
        wish = app.newCommand();
        wish.setId("wish");
        wish.setPrompt("查看对对方使用相同喜欢词语的快乐的人");
        exit = app.newCommand();
        exit.setId("exit");
        exit.setPrompt("退出不需要代价哦～");
        app.addCommand(initOpt, demo, show, wish, exit);

        //第二个option  //有复用，按需设计
        oneOpt = app.newOption();
        oneOpt.setId("one");
        oneOpt.setNextId("twoOpt");
        oneOpt.setPrompt("你知道怎么做的～");
        demo.setPrompt("再看一次演示，会有不同结果哦");
        todo = app.newCommand();
        todo.setId("todo");
        todo.setPrompt("去表白,格式是 todo: you [ignore|like|hate|unlike|love] other");
        app.addCommand(oneOpt, demo, todo, show, wish, exit);    //第二个开关拥有的命令

        //第三个option  //有复用，按需设计
        twoOpt = app.newOption();
        twoOpt.setId("twoOpt");
        twoOpt.setNextId("thrOpt");
        twoOpt.setPrompt("不紧张，除非喜欢你的人刚好你也喜欢，ta才会知道你在这里说的一切");
        regist = app.newCommand();
        regist.setId("regist");
        regist.setPrompt("留下自己的名字,可以等待被表白哦～ 输入格式是 regist:yourname");
        app.addCommand(twoOpt, demo, show, todo, regist, wish, exit);

        //第三个option  //有复用，按需设计
        thrOpt = app.newOption();
        thrOpt.setId("thrOpt");
        thrOpt.setNextId("oneOpt");   //循环复用
        thrOpt.setPrompt("加油啦，有勇气就不会遗憾");
        app.addCommand(thrOpt, show, todo, regist, wish, exit);
        return app;
    }

}

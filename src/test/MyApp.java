package test;

import interactivity.mvc.model.Application;
import interactivity.mvc.model.Command;
import interactivity.mvc.model.Option;

/**
 * User: 无止(何梓)
 * Date: 4/11/14
 * Time: 2:09 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class MyApp extends Application<MyHandler> {


    @Override
    public Application newApp() {
        Application app = new MyApp();

        Command comm0 = app.newCommand();
        comm0.setId("myComm");
        comm0.setNextId("myComm");
        comm0.setPrompt("MyComm Show");

        Option insert = app.newOption();
        insert.setId("insert");
        insert.setPrompt("插入一条数据");

        Option search = app.newOption();
        search.setId("search");
        search.setPrompt("根据查询商品价格");

        Option update = app.newOption();
        update.setId("update");
        update.setPrompt("设置商品的价格");

        Option delete = app.newOption();
        delete.setId("delete");
        delete.setPrompt("根据id删除商品");

        Option list = app.newOption();
        list.setId("list");
        list.setPrompt("列出所有商品");

        Option exit = app.newOption();
        exit.setId("exit");
        exit.setPrompt("退出");

        app.addCommand(comm0, insert,search, update,delete,list, exit);
        return app;
    }
}

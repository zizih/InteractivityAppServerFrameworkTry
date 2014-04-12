package interactivity.mvc;

import interactivity.dpa.DaoAdapter;

/**
 * User: 无止(何梓)
 * Date: 3/22/14
 * Time: 7:08 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public abstract class AppHandler {

    protected DaoAdapter dao;

    public AppHandler instance;

    public boolean init(){
        return true;
    }

}

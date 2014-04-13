package interactivity.mvc.model;

import interactivity.mvc.AppHandler;

/**
 * User: 无止(何梓)
 * Date: 3/21/14
 * Time: 7:31 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public interface IApplication<T extends AppHandler> {

    public abstract IApplication newApp();

}

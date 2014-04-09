package interactivity.net;

import interactivity.mvc.model.IApplication;

import java.io.IOException;

/**
 * User: 无止(何梓)
 * Date: 3/21/14
 * Time: 6:16 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
interface IServer<T extends IApplication> {

    public NIOServer init(int port) throws IOException;

    public void listen() throws IllegalAccessException, InstantiationException;
}

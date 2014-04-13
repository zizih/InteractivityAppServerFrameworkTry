package interactivity.mvc.model;

import interactivity.mvc.AppHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 11/16/13
 * Time: 7:36 PM
 * email: zizihjk@gmail.com，作者是个好人
 */
public abstract class Application<T extends AppHandler> implements IApplication<T> {

    private Map<String, Command> cmds;

    private Class<T> tClzz;

    public Application() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        tClzz = (Class) params[0];
        cmds = new ConcurrentHashMap<String, Command>();

        //init..
        Command initComm = newCommand();
        initComm.setId("initComm");
        initComm.setPrompt("服务器准备完毕");
        Option init = newOption();
        init.setId("init");
        init.setPrompt("处理器状态");
        initComm.addOption(init);
        addCommand(initComm);
    }

    @Override
    public abstract Application newApp() ;


    public Option newOption() {
        return new Option(tClzz);
    }

    public Option newOption(String optId, String optPrompt) {
        Option opt = new Option(tClzz);
        opt.setId(optId);
        opt.setPrompt(optPrompt);
        return opt;
    }

    public Command newCommand() {
        return new Command();
    }

    public Command newCommand(String optionId, String nextId, String optionPrompt) {
        return new Command(optionId, nextId, optionPrompt);
    }

    public void addCommand(Command comm, Option... opts) {
        if (!cmds.containsKey(comm.getId())) {
            cmds.put(comm.getId(), comm);
        }
        comm.addOptions(opts);
    }

    public Command getFirstCommand() {
        if (cmds.size() == 1) return null;
        for (Map.Entry<String, Command> entry : cmds.entrySet()) {
            return entry.getValue();
        }
        return null;
    }

    public Command getCommand(String commandId) {
        return cmds.get(commandId);
    }

    public Command nextCommond(String commandId) {
        Command cm = getCommand(commandId);
        String tmp = cm.getNextId();
        Command cmm = getCommand(tmp);
        return getCommand(getCommand(commandId).getNextId());
    }

    //默认第0个为OptionId
    public String invoke(String... params) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return cmds.get(params[0]).invoke(cutStrArr(params, 1, params.length));
    }

    private String[] cutStrArr(String[] rcArr, int start, int end) {
        if (rcArr == null) return null;
        int len = rcArr.length;
        if (start < 0) start = 0;
        if (end < len) end = len;
        String[] resultArr = new String[end - start];
        for (int i = start; i < end; i++) {
            resultArr[i - start] = rcArr[i];
        }
        return resultArr;
    }
}

package interactivity.mvc.model;

import interactivity.mvc.AppHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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
        cmds = new HashMap<String, Command>();
    }

    @Override
    public Application newApp() {
        return null;  //todo
    }

    public Option newCommand() {
        return new Option(tClzz);
    }

    public Option newCommand(String commId, String commPrompt) {
        Option comm = new Option(tClzz);
        comm.setId(commId);
        comm.setPrompt(commPrompt);
        return comm;
    }

    public Command newOption() {
        return new Command();
    }

    public Command newOption(String optionId, String nextId, String optionPrompt) {
        return new Command(optionId, nextId, optionPrompt);
    }

    public void addCommand(Command comm, Option... opts) {
        if (!cmds.containsKey(comm.getId())) {
            cmds.put(comm.getId(), comm);
        }
        comm.addOptions(opts);
    }


    public Command getCommand(String commandId) {
        return cmds.get(commandId);
    }

    public  Command nextCommond(String commandId) {
        Command cm = getCommand(commandId);
        String tmp = cm.getNextId();
        Command cmm = getCommand(tmp);
        return getCommand(getCommand(commandId).getNextId());
    }

    //默认第0个为OptionId
    public String invoke(String... params) {
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

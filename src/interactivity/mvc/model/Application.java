package interactivity.mvc.model;

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
public abstract class Application<T> implements IApplication<T>{

    public Map<String, Command> cmds;

    private Class<T> entityClass;

    public Application() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

    public Option newCommand() {
        return new Option(entityClass);
    }

    public Option newCommand(String commId, String commPrompt) {
        Option comm = new Option(entityClass);
        comm.setId(commId);
        comm.setPrompt(commPrompt);
        return comm;
    }

    public void addCommand(Command option, Option... commands) {
        if (!cmds.containsKey(option.getId())) {
            cmds.put(option.getId(), option);
        }
        option.addCommands(commands);
    }

    public Command newOption() {
        return new Command();
    }

    public Command newOption(String optionId, String nextId, String optionPrompt) {
        return new Command(optionId, nextId, optionPrompt);
    }

    private Command addOption(Command option) {
        if (cmds == null) {
            cmds = new HashMap<String, Command>();
        }
        return cmds.put(option.getId(), option);
    }

    private Command getOption(String optionId) {
        return cmds.get(optionId);
    }

    public Command nextOption(String optionId) {
        return getOption(getOption(optionId).getNextId());
    }

    public String invoke(String optionId, String commandId, String... params) {
        return cmds.get(optionId).invoke(commandId, params);
    }

}

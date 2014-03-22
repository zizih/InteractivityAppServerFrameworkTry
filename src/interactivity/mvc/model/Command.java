package interactivity.mvc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 11/16/13
 * Time: 6:58 PM
 * email: zizihjk@gmail.com，作者是个好人
 */
public class Command {

    private String id;

    private String prompt;

    private Map<String, Option> opts;

    private String nextId;

    protected Command() {
    }

    protected Command(String id, String nextId, String prompt) {
        this.opts = new HashMap<String, Option>();
        this.id = id;
        this.prompt = prompt;
    }

    protected void addCommands(Option... commands) {
        StringBuilder pattern = new StringBuilder();
        for (Option comm : commands) {
            this.opts.put(comm.getId(), comm);
            pattern.append("\n" + comm.getPrompt());
        }
        this.prompt += "\n\n"
                + "<<<<Step: " + id + ">>>>"
                + pattern.toString();
    }

    protected Option addCommand(Option comm) {
        this.opts.put(comm.getId(), comm);
        this.prompt += "\n\n"
                + "<<<<Step: " + id + ">>>>"
                + comm.getPrompt();
        return comm;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    protected String getId() {
        return id;
    }

    protected String getPrompt() {
        return prompt;
    }

    protected String getNextId() {
        return nextId;
    }

    protected String invoke(String commId, String... params) {
        Option comm = opts.get(commId);
        if (comm == null) return "客户端输入命令有错\n";
        return comm.invoke(params);
    }

}

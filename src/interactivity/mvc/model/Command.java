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
        this.opts = new HashMap<String, Option>();
    }

    protected Command(String id, String nextId, String prompt) {
        this.opts = new HashMap<String, Option>();
        this.id = id;
        this.nextId = nextId;
        this.prompt = prompt;
    }

    protected void addOptions(Option... opts) {
        StringBuilder pattern = new StringBuilder();
        for (Option comm : opts) {
            this.opts.put(comm.getId(), comm);
            pattern.append("\n" + comm.getPrompt());
        }
        this.prompt += "\n\n"
                + "<<<<Step: " + id + ">>>>"
                + pattern.toString();
    }

    protected Option addOption(Option opt) {
        this.opts.put(opt.getId(), opt);
        this.prompt += "\n\n"
                + "<<<<Step: " + id + ">>>>"
                + opt.getPrompt();
        return opt;
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

    public String getId() {
        return id;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getNextId() {
        return nextId;
    }

    //默认第0个为CommandId
    protected String invoke(String... params) {
        if (params == null || params.equals("") || params.length == 0) return "没有收到命令";
        Option opt = opts.get(params[0]);
        if (opt == null) return "客户端输入命令有错\n";
        return opt.invoke(cutStrArr(params, 1, params.length));
    }

    private String[] cutStrArr(String[] rcArr, int start, int end) {
        if (rcArr == null || start == end) return null;
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

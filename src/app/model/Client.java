package app.model;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 11/15/13
 * Time: 4:03 PM
 * email: zizihjk@gmail.com，作者是个好人
 */
public class Client {

    private int id;
    private String ip;
    private String port;
    private String nick;
    private Desc desc;

    public Client() {
    }

    public Client(String nick) {
        this.nick = nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDesc(Desc desc) {
        this.desc = desc;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public Desc getDesc() {
        return desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public enum Desc {

        self("自私的"),

        famouse("高调的"),

        literary("文艺的"),

        dios("屌丝的");

        private String desc;

        private Desc(String desc) {
            this.desc = desc;
        }

        public String getOne() {
            return this.desc;
        }

    }
}

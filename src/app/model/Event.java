package app.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 11/15/13
 * Time: 7:38 PM
 * email: zizihjk@gmail.com，作者是个好人
 */
public class Event {

    private Client from;

    private Client to;

    private Action action;

    private long time;

    public Event(Action action, Client from, Client to) {
        this.action = action;
        this.from = from;
        this.to = to;
        this.time = System.currentTimeMillis();
    }

    public Event(Action action, String fromNick, String toNick) {
        this(action, new Client(fromNick), new Client(toNick));
    }

    public boolean match(List<Event> others) {
        if (others.size() != 0) {
            for (Event other : others) {
                Action action = revertAction(other);
                //返回给客户端信息的过滤条件
//                if (action.toString().equals("love")
//                        || action.toString().equals("like") && action.equals(this.action)) {
//                    return true;
//                }
                if (action.equals(this.action)) return true;
            }
        }
        return false;
    }

    public Action revertAction(Event other) {
        if (other.getTo().getNick().equals(this.from.getNick())) {
            return other.getAction();
        }
        return Action.norelation;
    }

    public Action getAction() {
        return action;
    }

    public Client getFrom() {
        return from;
    }

    public Client getTo() {
        return to;
    }

    public long getTime() {
        return time;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setFrom(Client from) {
        this.from = from;
    }

    public void setTo(Client to) {
        this.to = to;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public enum Action {

        miss,

        hate,

        unlike,

        ignore,

        like,

        love,

        norelation;
    }
}

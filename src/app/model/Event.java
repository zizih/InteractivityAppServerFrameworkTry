package app.model;

import com.google.gson.Gson;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 11/15/13
 * Time: 7:38 PM
 * email: zizihjk@gmail.com，作者是个好人
 */
public class Event {

    private int id;

    private int fromId;

    private int toId;

    private Action action;

    private long time;

    public Event(){

    }

    public Event(Action action, int fromId, int toId) {
        this.action = action;
        this.fromId = fromId;
        this.toId = toId;
        this.time = System.currentTimeMillis();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public Action getAction() {
        return action;
    }

    public long getTime() {
        return time;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public enum Action {

        hate,

        unlike,

        ignore,

        like,

        love,

        norelation;
    }
}

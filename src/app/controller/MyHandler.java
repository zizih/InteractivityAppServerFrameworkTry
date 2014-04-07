package app.controller;

import app.model.Client;
import app.model.Event;
import interactivity.dpa.Log;
import interactivity.mvc.AppHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 无止(何梓)
 * Date: 3/21/14
 * Time: 9:53 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class MyHandler extends BaseHandler {


    enum From {rain, jack, rose, aaron}

    enum To {dios, lucy, ruby, anson}

    enum Action {like, ignore, hate, love}

    //默认callbak方法的参数都是第一个为ip:port 第二个之后为客户端传入的第三个参数
    public String demo() {

        StringBuffer sb = new StringBuffer("♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥\n");

        //默认都一样长
        int len = From.values().length;
        int i = 0;
        for (From from : From.values()) {
            int[] indexs = {(int) (Math.random() * len), (int) (Math.random() * len)};
            sb.append(from + " " + Action.values()[indexs[0]] + " " + To.values()[indexs[1]] + "\n");
            i++;
        }
        return sb.append("♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥\n").toString();
    }

    public String todo(String todo) {
        //对客户端返回的约定格式数据进行解析
        String[] todos = todo.trim().split(" ");
        String fromNick = todos[0];
        String act = todos[1];
        String toNick = todos[2];

        //先检查action行为是否冲突
        Event.Action action = Event.Action.valueOf(act);
        String result = verify(fromNick, action, toNick);
        if (!result.equals("OK")) {
            return result + "\n";
        }

        //封装对象
        if (todos.length != 3 || !(action instanceof Event.Action)) {
            return "⊙ ω ⊙ 表白句子不对～\n";
        }

        //构造一个event对象，即发生了一个事件的数据传输对象
        Client from = new Client();
        from.setNick(fromNick);
        Client to = new Client(toNick);

        //记录一个event到日志
        Event event = new Event(action, from, to);
        //检查相似event是否已经记录过
        if (contains(event)) return "● ω ●: 你已经说过相同的话了。。\n";
        add(event);//添加到当前运行时的内存中

        if (event.match(getFromEvent(to.getNick()))) {
            log.iwish(toNick + " and " + fromNick + " " + act + " each other!");
            return "＠^^＠\n"
                    + "♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥\n"
                    + "Congratulation! " + toNick + " also "
                    + action.toString() + " you!\n"
                    + "♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥";
        }
        return "● ω ●！你们没有任何关系，"
                + toNick
                + "没有对你表示相同的感觉,或者尝试换个词。";
    }

    //反射的时候一起都定位有第一个参数，所以就放这里先，以后日志也用得上的
    public String show() {
        if (nicksCache.size() == 0) return "^ ^ ~ 还没有替他人，你是第一个到达这里的人\n";
        StringBuilder sb = new StringBuilder("这些人在这里出现过或者故意留下名字暗示你对ta说些什么：\n");
        int contr = 1;
        for (String nick : nicksCache) {
            if (contr % 5 == 0) sb.append("\n");
            sb.append(nick + " ");
            contr++;
        }
        return sb.toString() + "\n";
    }

    public String regist(String nick) {
        add(nick);
        return "☺ ☺ 名字添加成功\n";
    }

    //和命令一一对应的回调
    public String wish() {
        if (wishesCache.size() == 0) return "@ @ 還沒有任何值得慶祝的一對哦～\n";
        StringBuilder sb = new StringBuilder("恭喜这些人对对方用了相同的词\n");
        int count = -11;
        for (String wish : wishesCache) {
            sb.append("  " + count + ".  " + wish + "\n");
            count++;
        }
        return sb.toString() + "\n";
    }


}

class BaseHandler extends AppHandler{

    protected static List<Event> eventsCache;
    protected static List<String> nicksCache;
    protected static List<String> wishesCache;
    protected Log log;

    public BaseHandler() {
        log = Log.ini();
        eventsCache = log.events();
        nicksCache = log.nicks();
        wishesCache = log.wishes();
    }

    public void add(Event event) {
        this.eventsCache.add(event);
        log.ievent(event.toJson());
        //更新内存中的nick
        add(event.getFrom().getNick());
    }

    protected void add(String nick) {
        if (!nicksCache.contains(nick)) {
            nicksCache.add(nick);
            log.inick(nick);
        }
    }

    protected void addAWish(String wish) {
        if (!wishesCache.contains(wish)) {
            wishesCache.add(wish);
            log.iwish(wish);
        }
    }

    /**
     * 判断条件是fromNick、action、toNick相同
     *
     * @param event
     * @return
     */
    public boolean contains(Event event) {
        if (this.eventsCache == null) return false;
        for (Event e : this.eventsCache) {
            if (e.getFrom().getNick().equals(event.getFrom().getNick())
                    && e.getTo().getNick().equals(event.getTo().getNick())
                    && e.getAction().equals(event.getAction())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回nick作为event的发起者的所有event
     *
     * @param nick
     * @return
     */
    protected List<Event> getFromEvent(String nick) {
        if (this.eventsCache == null) return null;
        List<Event> froms = new ArrayList<Event>();
        for (Event from : this.eventsCache) {
            if (from.getFrom().getNick().equals(nick)) {
                froms.add(from);
            }
        }
        return froms;
    }

    /**
     * 返回nikc作为event接受者的所有event
     *
     * @param nick
     * @return
     */
    protected List<Event> getToEvent(String nick) {
        if (this.eventsCache == null) return null;
        List<Event> tos = new ArrayList<Event>();
        for (Event to : this.eventsCache) {
            if (to.getTo().getNick().equals(nick)) {
                tos.add(to);
            }
        }
        return tos;
    }

    /**
     * 返回不重复的event发起者的名字列表
     *
     * @return
     */
    protected List<String> getFromNicks() {
        if (this.eventsCache == null) return null;
        List<String> nicks = new ArrayList<String>();
        for (Event e : this.eventsCache) {
            if (!nicks.contains(e.getFrom().getNick())) {
                nicks.add(e.getFrom().getNick());
            }
        }
        return nicks;
    }

    /**
     * 返回fromNick和toNick相同的event列表
     *
     * @param fromNick
     * @param toNick
     * @return
     */
    protected List<Event> getCommonEvents(String fromNick, String toNick) {
        if (this.eventsCache == null) return null;
        List<Event> es = new ArrayList<Event>();
        for (Event to : this.eventsCache) {
            if (to.getFrom().getNick().equals(fromNick)
                    && to.getTo().getNick().equals(toNick)) {
                es.add(to);
            }
        }
        return es;
    }

    /**
     * return OK 表示通过
     * 否则表示action冲突
     *
     * @param fromNick
     * @param toNick
     * @return
     */
    protected String verify(String fromNick, Event.Action action, String toNick) {
        if (this.eventsCache == null) return "OK";
        List<Event.Action> as = new ArrayList<Event.Action>();
        as.add(action);
        for (Event e : this.eventsCache) {
            if (e.getFrom().getNick().equals(fromNick)
                    && e.getTo().getNick().equals(toNick)) {
                if (!as.contains(e.getAction())) {
                    as.add(e.getAction());
                }
            }
        }
        if ((as.contains(Event.Action.like) || as.contains(Event.Action.love))
                && as.contains(Event.Action.hate)) {
            return "⊙ω⊙: 你不能"
                    + "对"
                    + toNick
                    + "既"
                    + (as.contains(Event.Action.like) ? Event.Action.like.toString() : Event.Action.love.toString())
                    + "又"
                    + Event.Action.hate.toString();
        }
        return "OK";
    }

}

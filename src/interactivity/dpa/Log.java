package interactivity.dpa;

import app.model.Event;
import com.google.gson.Gson;
import interactivity.dpa.IDao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 11/6/13
 * Time: 6:24 PM
 * email: zizihjk@gmail.com，作者是个好人
 */
public class Log<T extends Model> implements IDao<T>{

    @Override
    public List<T> fetch() {
        return null;  //todo
    }

    @Override
    public T fetchOne(long id) {
        return null;  //todo
    }

    @Override
    public boolean insert(T t) {
        return false;  //todo
    }

    @Override
    public boolean update(T t) {
        return false;  //todo
    }

    @Override
    public boolean delete(long id) {
        return false;  //todo
    }

    //第一次把private命名加上_
    private static Log _instance;
    private Formatter _like_formatter;
    private Handler _like_handler;
    private Gson _gson;
    private Logger _like_logger;

    public static Log ini() {
        if (_instance == null) {
            _instance = new Log();
        }
        return _instance;
    }

    private Log() {
        //Gson util
        _gson = new Gson();
        ilikeIni();
    }

    //info 为event的Json字符串
    public void ievent(String info) {
        _like_logger.log(Level.INFO, "[todo]" + info);
    }

    //info 为nick字符串
    public void inick(String info) {
        _like_logger.log(Level.INFO, "[regist]" + info);
    }

    public void iwish(String info) {
        _like_logger.log(Level.INFO, "[wish]" + info);
    }

    public List<Event> events() {
        List<Event> _events = new ArrayList<Event>();
        try {
            File file = new File("like.log");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.equals("") || line == "") break;
                    if (line.startsWith("[todo]")) {
                        int index = line.indexOf("detail:");
                        if (index == -1) continue;
                        String tmp = line.substring(index + "detail:".length());
                        Event event = _gson.fromJson(tmp, Event.class);
                        _events.add(event);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _events;
    }

    public List<String> nicks() {
        List<String> _nicks = new ArrayList<String>();
        try {
            File file = new File("like.log");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.equals("") || line == "") break;
                    if (line.startsWith("[regist]")) {
                        _nicks.add(line.substring("[regist]".length()).trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _nicks;
    }

    public List<String> wishes() {
        List<String> _nicks = new ArrayList<String>();
        try {
            File file = new File("like.log");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.equals("") || line == "") break;
                    if (line.startsWith("[wish]")) {
                        _nicks.add(line.substring("[wish]".length()).trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _nicks;
    }

    private void ilikeIni() {
        _like_formatter = new LikeFormatter();
        try {
            //设置like logger的参数
            _like_handler = new FileHandler("like.log", true);
            _like_handler.setFormatter(_like_formatter);
            _like_logger = Logger.getLogger(Thread.currentThread().getName());
            _like_logger.addHandler(_like_handler);
            _like_logger.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class LikeFormatter extends Formatter {

        @Override
        public String format(LogRecord record) {
            String msg = record.getMessage();
            if (msg.startsWith("[todo]")) {
                try {
                    Event event = _gson.fromJson(msg.substring("[todo]".length()), Event.class);
                    //return like such: [1384519003991] rain:like:dad detail:{}
                    return String.format("[todo]%s:%s:%s  detail:%s\n",
                            event.getFrom().getNick(),
                            event.getAction(),
                            event.getTo().getNick(),
                            msg.substring("[todo]".length()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg.startsWith("[regist]") || msg.startsWith("[wish]")) {
                return msg + "\n";
            }
            return "";
        }
    }

}

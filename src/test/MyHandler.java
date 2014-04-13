package test;

import com.google.gson.Gson;
import interactivity.mvc.AppHandler;

import java.util.List;

/**
 * User: 无止(何梓)
 * Date: 4/11/14
 * Time: 2:10 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class MyHandler extends AppHandler {

    private boolean state = true;

    private Gson gson;

    public MyHandler() {
        try {
            dao = new GoodDao();
        } catch (Exception e) {
            state = false;
        }
        gson = new Gson();
    }

    @Override
    public boolean init() {
        return state;
    }

    public String insert(String name, String price) {
        Good good = new Good();
        good.setName(name);
        good.setPrice(price);
        try {
            dao.insert(good);
        } catch (Exception e) {
            return "商品插入错误";
        }
        return "插入的商品：" + gson.toJson(good);
    }

    public String search(String id) {
        Good good = null;
        try {
            good = (Good) dao.fetchOne(Long.parseLong(id));
            if (good != null) {
                return "商品名字为：" + good.getName() + "\n"
                        + "商品价格为：" + good.getPrice();
            }
        } catch (Exception e) {
        }
        return "商品查询错误";
    }

    public String update(String id, String price) {
        Good good = null;
        try {
            good = (Good) dao.fetchOne(Long.parseLong(id));
        } catch (Exception e) {
            return "该id不存在";
        }
        good.setPrice(price);
        try {
            dao.update(good);
        } catch (Exception e) {
            e.printStackTrace();  //deal with ex
        }
        return "修改为：" + new Gson().toJson(good);
    }

    public String delete(String idStr) {
        long id = Long.parseLong(idStr);
        try {
            dao.delete(id);
        } catch (Exception e) {
            return "删除出错";
        }
        return "删除id为" + id + "的商品";
    }

    public String list() {
        List list = null;
        try {
            list = dao.fetch();
        } catch (Exception e) {
            return "商品清单出错";
        }
        return gson.toJson(list);
    }

}

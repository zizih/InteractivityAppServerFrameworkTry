package test;

import interactivity.dpa.Model;

/**
 * User: 无止(何梓)
 * Date: 4/11/14
 * Time: 2:11 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class Good extends Model {

    long id;

    String name;

    String price;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}

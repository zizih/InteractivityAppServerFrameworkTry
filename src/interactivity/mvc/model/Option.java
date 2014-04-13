package interactivity.mvc.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 11/16/13
 * Time: 7:13 PM
 * email: zizihjk@gmail.com，作者是个好人
 */
public class Option {

    private String id;

    private String prompt;

    private Method method;

    private Class callbackClzz;

    protected Option(Class callbackClzz) {
        this.callbackClzz = callbackClzz;
    }

    public void setId(String id) {
        this.id = id;
        //通过id获得method，类型和参数是不安全的，Sorry
        Method[] methods = callbackClzz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(id)) {
                this.method = method;
            }

        }
    }

    public void setPrompt(String prompt) {
        this.prompt = String.format("[%s]: %s", id, prompt);
    }

    protected String invoke(String... params) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        try {
            return method.invoke(callbackClzz.newInstance(), params).toString();
        } catch (IllegalArgumentException e) {
            return "参数不对";
        }
    }

    protected String getId() {
        return id;
    }

    protected String getPrompt() {
        return prompt;
    }
}

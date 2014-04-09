package interactivity.dpa;

import com.google.gson.Gson;
import interactivity.exception.DaoException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: 无止(何梓)
 * Date: 4/5/14
 * Time: 10:43 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class TxtDao<T extends Model> implements IDao<T> {

    private Class<T> tClzz;
    private Gson gson;
    private File txt;
    private AtomicLong idCounter;

    public TxtDao(Class<T> tClzz) throws IOException {
        this.tClzz = tClzz;
//        Type genType = getClass().getGenericSuperclass();
//        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
//        tClzz = (Class) params[0];

        String rootPath = System.getProperty("user.dir");
        txt = new File(rootPath + File.separator + tClzz.getName() + ".db");
        if (!txt.exists()) {
            txt.createNewFile();
        }
        //txt = new File(rootPath + "/like.log");

        gson = new Gson();
        idCounter = new AtomicLong(getKeyByLine(getLastLine(txt)));
    }

    @Override
    public List<T> fetch() throws IOException, IllegalAccessException, InstantiationException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(txt)));
        String line = null;
        List<T> list = new ArrayList<T>();
        while ((line = br.readLine()) != null) {
            int start = line.indexOf("]");
            int length = line.length();
            T t = gson.fromJson(line.substring(start + 1, length), tClzz);
            list.add(t);
        }
        return list;
    }

    @Override
    public T fetchOne(long id) throws IOException {
        String line = null;
        //根据id定位到行
        try {
            if ((line = readAppointedLineNumber(txt, (int) (id + 1))) != null) {
                long key = getKeyByLine(line);
                if (key == id) {
                    int start = line.indexOf("]");
                    int length = line.length();
                    T t = gson.fromJson(line.substring(start + 1, length), tClzz);
                    return t;
                }
            }
        } catch (DaoException e) {
            //id作为行数，超出范围,容许存在的异常
        }
        //按行搜索
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(txt)));
        while ((line = br.readLine()) != null) {
            long key = getKeyByLine(line);
            if (key == id) {
                int start = line.indexOf("]");
                int length = line.length();
                T t = gson.fromJson(line.substring(start + 1, length), tClzz);
                return t;
            }
        }
        return null;
    }

    @Override
    public boolean insert(T t) throws IOException {
        long id = Long.parseLong(callGetter(t, "id").toString());
        if (id > idCounter.get()) {
            idCounter.getAndSet(id);
        }
        System.out.println(idCounter.get());
        idCounter.getAndIncrement();
        id = idCounter.get();
        appendMethod(txt.getAbsolutePath(), "[" + id + "]" + gson.toJson(t));
        return true;
    }

    @Override
    public boolean update(T t) throws IOException, DaoException {
        long id = Long.parseLong(callGetter(t, "id").toString());
        if (readAppointedLineNumber(txt, id) != null) {
            //do not finished
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        //do not finished
        return false;
    }

    private Object callGetter(T t, String fieldName) {
        String setterName = "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1, fieldName.length());
        try {
            Class clzz = t.getClass();
            Method method = clzz.getDeclaredMethod(setterName);
            return method.invoke(t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private long getKeyByLine(String line) {
        if (line == null) {
            return 0;
        }
        return Long.parseLong(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
    }

    public void appendMethod(String fileName, String content) throws IOException {
        RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
        long fileLength = randomFile.length();
        randomFile.seek(fileLength);
        randomFile.writeBytes(content + "\n");
        randomFile.close();
    }

    private String readAppointedLineNumber(File sourceFile, long lineNumber)
            throws IOException, DaoException {
        FileReader in = new FileReader(sourceFile);
        LineNumberReader reader = new LineNumberReader(in);
        if (lineNumber <= 0 || lineNumber > getTotalLines(sourceFile)) {
            throw new DaoException("不在文件的行数范围(1至总行数)之内。");
        }
        int lines = 1;
        while (reader.readLine() != null) {
            if (lines + 1 == lineNumber) {
                return reader.readLine();
            }
            lines++;
        }
        reader.close();
        in.close();
        return null;
    }

    // 文件内容的总行数。
    private int getTotalLines(File file) throws IOException {
        FileReader in = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(in);
        int lines = 0;
        while (reader.readLine() != null) {
            lines++;
        }
        reader.close();
        in.close();
        return lines;
    }


    // 文件内容的总行数。
    private String getLastLine(File file) throws IOException {
        FileReader in = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(in);
        String line = null, result = null;
        while ((line = reader.readLine()) != null) {
            result = line;
            continue;
        }
        reader.close();
        in.close();
        return result;
    }

}

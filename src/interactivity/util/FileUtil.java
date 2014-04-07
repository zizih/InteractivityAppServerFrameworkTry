package interactivity.util;

import interactivity.exception.DaoException;

import java.io.*;

/**
 * User: 无止(何梓)
 * Date: 4/7/14
 * Time: 3:18 PM
 * EMail: hezi.hz@alibaba-inc.com
 * Comment: ~ ~
 */
public class FileUtil {

    private static class SingletonContainer {
        public static FileUtil instance = new FileUtil();
    }

    public static FileUtil getInstance() {
        return SingletonContainer.instance;
    }

    public void readLine(InputStream is) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String data = null;
        while ((data = br.readLine()) != null) {
            System.out.println(data);
        }
    }

    public FileUtil appendMethod(String fileName, String content) throws IOException {
        RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
        long fileLength = randomFile.length();
        randomFile.seek(fileLength);
        randomFile.writeBytes(content + "\n");
        randomFile.close();
        return this;
    }

    public String readAppointedLineNumber(File sourceFile, long lineNumber)
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
    public int getTotalLines(File file) throws IOException {
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
    public String getLastLine(File file) throws IOException {
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

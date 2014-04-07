package interactivity.util;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: rain
 * Date: 11/15/13
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class IO {

    private static int count = 0;

    public static String inputStream2Str(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.toUpperCase().equals("EOF")
                        || line.equals("") || line == "") break;
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        count++;
        return sb.toString();
    }

    public static InputStream getPropertiesInputStream() {
        String rootPath = System.getProperty("user.dir");
        try {
            return new FileInputStream(rootPath + "/conf/application.conf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //deal with ex
        }
        return null;
    }

     public static File byteArrayToFile(byte[] fileBuff, String filePath) {
        File target = null;
        OutputStream os = null;

        try {
            target = new File(filePath);
            os = new BufferedOutputStream(new FileOutputStream(target));
            os.write(fileBuff);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (Exception e) {
                }
            }
        }

        return target;
    }


    public static byte[] fileToByteArray(File file) {
        if (file != null) {
            try {
                FileInputStream fileInput = new FileInputStream(file);
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1000];
                for (int i; (i = fileInput.read(b)) != -1; ) {
                    byteArrayOut.write(b, 0, i);
                }

                fileInput.close();
                byteArrayOut.close();
                return byteArrayOut.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}

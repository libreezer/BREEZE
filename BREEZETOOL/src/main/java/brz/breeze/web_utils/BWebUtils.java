package brz.breeze.web_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BWebUtils {

    public static final String TAG = "BWebUtils";

    /**
     * @param picUri 图片直链
     * @author BREEZE
     */
    public static Bitmap getWebPicture(String picUri) throws IOException {
        URL u = new URL(picUri);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod("GET");
        InputStream input = con.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();
        return bitmap;
    }

    /**
     * @param uri 网页链接
     * @author BREEZE
     */
    public static String getWebContent(String uri) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        InputStream input = con.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while ((len = input.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.close();
        input.close();
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * @param uri  网页链接
     * @param post POST内容
     * @author BREEZE
     */
    public static String postWebData(String uri, String post) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        OutputStream outputStream = con.getOutputStream();
        outputStream.write(post.getBytes());
        InputStream input = con.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while ((len = input.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.close();
        input.close();
        outputStream.close();
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}

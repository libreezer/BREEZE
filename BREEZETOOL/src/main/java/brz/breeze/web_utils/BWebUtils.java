package brz.breeze.web_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

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


    private static String getWebData(String url, String post, HashMap<String, String> headers) throws Exception {
        URL url1 = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        if (headers != null) {
            Set<String> strings = headers.keySet();
            for (String item : strings) {
                httpURLConnection.setRequestProperty(item, headers.get(item));
            }
        }
        if (post == null) {
            httpURLConnection.setRequestMethod("GET");
        } else {
            httpURLConnection.setRequestMethod("POST");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter printWriter = new PrintWriter(outputStreamWriter);
            printWriter.print(post);
            printWriter.flush();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}

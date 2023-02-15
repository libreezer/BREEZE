package brz.breeze.web_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

import brz.breeze.app_utils.BAppUtils;

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
     * @param url     网址
     * @param post    post数据（可选）
     * @param headers 头部标识（可选）
     * @throws Exception 网络请求错误
     */
    public static String getWebData(final String url, final String post, final HashMap<String, String> headers) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openWebConnection(url, post, headers).getInputStream()));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public static HttpURLConnection openWebConnection(String url, String post, HashMap<String, String> headers) throws Exception {
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
        return httpURLConnection;
    }

    public interface WebDownloadListener {
        void onProgress(int progress);

        void onSuccess(File targetPath);

        void onError(Exception exception);

    }

    public static void downloadFile(final String link, final File targetPath, final WebDownloadListener listener) {
        BAppUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = openWebConnection(link, null, null);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(targetPath);
                    byte[] bytes = new byte[2048];
                    int length, recorded = 0;
                    long totalLength = Build.VERSION.SDK_INT >= 24 ? httpURLConnection.getContentLengthLong() : httpURLConnection.getContentLength();
                    while ((length = bufferedInputStream.read(bytes)) != -1) {
                        recorded += length;
                        if (totalLength > -1) {
                            listener.onProgress((int) (((float) recorded / (float) totalLength) * 100));
                        } else {
                            listener.onProgress(-1);
                        }
                        fileOutputStream.write(bytes);
                    }
                    fileOutputStream.close();
                    bufferedInputStream.close();
                    listener.onSuccess(targetPath);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    listener.onError(exception);
                }
            }
        });
    }

}

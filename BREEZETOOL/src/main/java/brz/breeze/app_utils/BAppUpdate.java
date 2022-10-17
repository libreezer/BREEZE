package brz.breeze.app_utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import brz.breeze.web_utils.BWebUtils;

public class BAppUpdate {

    /*
     *@author BREEZE
     *@date 2021-08-22 12:00:06
     */
    private static String appLink = "";
    private static Context appContext = null;
    private static BUpdateMode data;
    private static BAppUpdateListener mListener;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * @param context 上下文
     * @param link 更新请求连接
     * @param listener 更新监听器
     */
    public static void setOnUpdateListener(Context context, String link, BAppUpdateListener listener) {
        appContext = context;
        appLink = link;
        mListener = listener;
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = BWebUtils.getWebContent(appLink);
                    if (result.equals("") || result == null) {
                        threadFinished(null);
                    } else {
                        JSONObject obj = new JSONObject(result);
                        threadFinished(obj);
                    }
                } catch (Exception e) {
                    mListener.onError(e);
                    threadFinished(null);
                }
            }
        });
    }

    private static void threadFinished(final JSONObject result) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (result != null) {
                    data = new BUpdateMode();
                    try {
                        data.setAppName(result.getString("appName"));
                        data.setAppVersion(result.getInt("appVersion"));
                        data.setAppVersionName(result.getString("appVersionName"));
                        data.setDownLink(result.getString("downLink"));
                        data.setUpdateContent(result.getString("updateContent"));
                        int appVersion = BAppUtils.getVersionCode(appContext);
                        if (appVersion >= data.getAppVersion()) {
                            //没有更新
                            mListener.noNewVersion();
                        } else {
                            //发现更新
                            mListener.haveNewVersion(data);
                        }
                    } catch (Exception e) {
                        mListener.onError(e);
                    }
                }
            }
        });
    }

    public static class BUpdateMode {
        String appName, appVersionName, updateContent, downLink;
        int appVersion;

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppVersionName(String appVersionName) {
            this.appVersionName = appVersionName;
        }

        public String getAppVersionName() {
            return appVersionName;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }

        public String getUpdateContent() {
            return updateContent;
        }

        public void setDownLink(String downLink) {
            this.downLink = downLink;
        }

        public String getDownLink() {
            return downLink;
        }

        public void setAppVersion(int appVersion) {
            this.appVersion = appVersion;
        }

        public int getAppVersion() {
            return appVersion;
        }
    }

    public interface BAppUpdateListener {
        void haveNewVersion(BUpdateMode info);

        void noNewVersion();

        void onError(Exception exception);
    }

}

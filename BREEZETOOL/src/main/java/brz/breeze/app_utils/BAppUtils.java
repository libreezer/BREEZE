package brz.breeze.app_utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import brz.breeze.tool_utils.Blog;

public class BAppUtils {

    public static final String TAG = "BAppUtils";

    /**
     * @param context 上下文，获取版本号
     * @author BREEZE
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @author BREEZE
     * * @return 获取安卓版本
     */
    public static String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * @param Context 上下文
     * @author BREEZE
     */
    public static String getAppVersionName(Context Context) {
        String strVersion = null;
        try {
            PackageInfo pi;
            pi = Context.getPackageManager().getPackageInfo(Context.getPackageName(), 0);
            if (pi != null) {
                strVersion = pi.versionName;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return strVersion;
    }

    /**
     * @param Context 上下文
     * @author BREEZE
     */
    public static String getPackageName(Context Context) {
        return Context.getPackageName();
    }

    /**
     * @author BREEZE
     */
    public static String PhoneName() {
        return android.os.Build.BRAND;
    }

    /**
     * @author BREEZE
     */
    public static boolean getIsRoot() {
        boolean isRoot;
        try {
            isRoot = (new File("/system/bin/su").exists())
                    || (new File("/system/xbin/su").exists());
        } catch (Exception e) {
            return false;
        }
        return isRoot;
    }

    public static InputStream getResFromAssets(Context context, String path) {
        try {
            AssetManager as = context.getAssets();
            return as.open(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static Typeface getTypefaceFromAssets(Context context, String path) {
        try {
            return Typeface.createFromAsset(context.getAssets(), path);
        } catch (Exception e) {
            return null;
        }
    }

    private static ExecutorService executorService;
    private static Handler uiHandler;

    public static void execute(Runnable runnable) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(5);
        }
        executorService.execute(runnable);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        return executorService.submit(callable);
    }

    /**
     * @param runnable 主线程运行
     */
    public static void uiExecute(Runnable runnable) {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
        uiHandler.post(runnable);
    }

    public static String md5(String content) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("md5");
        byte[] bytes = digest.digest(content.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            int var = aByte & 0xff;
            if (var < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(var));
        }
        return sb.toString();
    }

    /**
     * @param format 类型
     * @author BREEZE
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTime(String format) {
        return new SimpleDateFormat(format).format(System.currentTimeMillis());
    }

    private static final String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String getDayInWeek() {
        Calendar c = Calendar.getInstance();
        int week_day = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_day < 0) {
            week_day = 0;
        }
        return weeks[week_day];
    }

    public static String execShellCommand(String root, String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(root);
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(command + "\n");
            outputStream.close();
            process.waitFor();

            BufferedInputStream inputStream = new BufferedInputStream(process.getInputStream());
            byte[] bytes = new byte[inputStream.available()];
            int read = inputStream.read(bytes);
            inputStream.close();
            return new String(bytes);
        } catch (Exception e) {
            Blog.e(e);
            return null;
        }
    }
}

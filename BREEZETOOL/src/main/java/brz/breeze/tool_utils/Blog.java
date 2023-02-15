package brz.breeze.tool_utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import brz.breeze.app_utils.BAppUtils;
import brz.breeze.file_utils.BFileUtils;

public class Blog {

    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    public static final int NOTHING = 6;

    public static int level = VERBOSE;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public Blog(Context context2) {
        context = context2;
    }

    public static void init(Context mContext) {
        context = mContext;
    }

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            write(tag, "V", msg);
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            write(tag, "D", msg);
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (level <= INFO) {
            write(tag, "I", msg);
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (level <= WARN) {
            write(tag, "W", msg);
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            write(tag, "E", msg);
            Log.e(tag, msg);
        }
    }

    /**
     * @param content 控制台输出内容
     */
    public static void log(String content) {
        write("BREEZE", "I", content);
        Log.i("BREEZE", content);
    }

    /**
     * @param tag   标签
     * @param level 日志等级
     * @param msg   日志信息
     */
    public static void write(String tag, String level, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(level).append("/").append(tag).append(BAppUtils.getTime("[yyyy-MM-dd HH:mm:ss]"))
                .append("	").append(msg).append("\n");
        String filePath = BFileUtils.getExternalCacheFile(context, "logcat") + BAppUtils.getTime("yyyy-MM-dd") + ".txt";
        try {
            BFileUtils.createMultiFilePath(filePath);
            BFileUtils.addContentToFile(filePath, sb.toString());
        } catch (IOException e) {
            e(e);
        }
    }

    public static void e(Throwable p1) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        p1.printStackTrace(printWriter);
        Throwable throwable = p1.getCause();
        while (throwable != null) {
            throwable.printStackTrace(printWriter);
            throwable = throwable.getCause();
        }
        String Exception_content = writer.toString();
        Log.e("Blog", Exception_content);
        write("Blog", "E", Exception_content);
    }

}

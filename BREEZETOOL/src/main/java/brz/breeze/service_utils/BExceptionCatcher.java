package brz.breeze.service_utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import brz.breeze.file_utils.BFileUtils;

public class BExceptionCatcher implements Thread.UncaughtExceptionHandler {

    @SuppressLint("StaticFieldLeak")
    private static BExceptionCatcher mBExceptionCatcher;

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    public static BExceptionCatcher getInstance(Context context) {
        if (mBExceptionCatcher == null) {
            mBExceptionCatcher = new BExceptionCatcher(context);
        }
        return mBExceptionCatcher;
    }

    public BExceptionCatcher(Context context) {
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread p1, Throwable p2) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        p2.printStackTrace(printWriter);
        Throwable throwable = p2.getCause();
        while (throwable != null) {
            throwable.printStackTrace(printWriter);
            throwable = throwable.getCause();
        }
        String Exception_content = writer.toString();
        try {
            String path = BFileUtils.getExternalCacheFile(mContext, "Exception") + "ErrorLog.txt";
            BFileUtils.createMultiFilePath(path);
            BFileUtils.writeFileContent(path, Exception_content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

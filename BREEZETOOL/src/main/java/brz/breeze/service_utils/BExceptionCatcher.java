package brz.breeze.service_utils;
import android.content.Context;
import java.io.Writer;
import java.io.StringWriter;
import java.io.PrintWriter;
import brz.breeze.file_utils.BFileUtils;
import java.io.IOException;
import android.content.Intent;
import brz.breeze.tool_utils.BToolUtils;

public class BExceptionCatcher implements Thread.UncaughtExceptionHandler {
	
	private static BExceptionCatcher mBExceptionCatcher;
	
	public static Context mContext;
	
	public static BExceptionCatcher getInstance(Context context){
		if(mBExceptionCatcher == null){
			mBExceptionCatcher = new BExceptionCatcher(context);
		}
		return mBExceptionCatcher;
	}
	
	public BExceptionCatcher(Context context){
		mContext = context;
	}
	
	@Override
	public void uncaughtException(Thread p1, Throwable p2) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		p2.printStackTrace(printWriter);
		Throwable throwable = p2.getCause();
		while(throwable != null){
			throwable.printStackTrace(printWriter);
			throwable = throwable.getCause();
		}
		String Exception_content = writer.toString();
		try {
			String path = BFileUtils.getExternalCacheFile(mContext, "Exception") + "错误日志.txt";
			BFileUtils.createFile(path);
			BFileUtils.writeFileContent(path,Exception_content);
		} catch (IOException e) {}
	}
	
	public static void e(Context context,Exception p2){
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		p2.printStackTrace(printWriter);
		Throwable throwable = p2.getCause();
		while(throwable != null){
			throwable.printStackTrace(printWriter);
			throwable = throwable.getCause();
		}
		final String Exception_content = writer.toString() + "\n\n\n";
		String log_path = BFileUtils.getExternalCacheFile(context,"logcat")+"Error.txt";
		try {
			BFileUtils.createFile(log_path);
			BFileUtils.addContentToFile(log_path,Exception_content);
		} catch (IOException e) {}
	}
}

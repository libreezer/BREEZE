package brz.breeze.app_utils;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.io.File;
import android.app.Application;
import android.graphics.Typeface;
import android.content.res.AssetManager;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import android.os.Handler;
import android.os.Looper;

public class BAppUtils {
    
    public static final String TAG = "BAppUtils";

	/**
	*@author BREEZE
	*@param context 上下文，获取版本号
	*/
    public static int getVersionCode(Context context){
		try{
			PackageManager pm=context.getPackageManager();
			PackageInfo info=pm.getPackageInfo(context.getPackageName(),0);
			return info.versionCode;
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	*@author BREEZE
	*/
	public static String getAndroidVersion()
    {
        return android.os.Build.VERSION.RELEASE;
    }
	
	/**
	*@author BREEZE
	*@param Context 上下文
	*/
	public static String getAppVersionName(Context Context) {
        String strVersion = null;
        try {
            PackageInfo pi = null;
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
	*@author BREEZE
	*@param Context 上下文
	*/
	public static String getPackageName(Context Context) {
        return Context.getPackageName();
    }

	/**
	*@author BREEZE
	*/
    public static String PhoneName() {
        return android.os.Build.BRAND;
    }

	/**
	*@author BREEZE
	*/
    public static boolean getIsRoot() {
        boolean isRoot = false;
        try {
            isRoot = (new File("/system/bin/su").exists())
                || (new File("/system/xbin/su").exists());
        } catch (Exception e) {
            return false;
        }
        return isRoot;
	}
	
	public static Application mApplication;

	public static InputStream getResFromAssets(Context context,String path){
		try{
			AssetManager as = context.getAssets();
			return as.open(path);
		}catch(Exception e){
			return null;
		}
	}
	
	public static Typeface getTypefaceFromAssets(Context context,String path){
		try{
			return Typeface.createFromAsset(context.getAssets(),path);
		}catch(Exception e){
			return null;
		}
	}

	private static ExecutorService executorService;
	private static Handler uiHandler;
	public static void execute(Runnable runnable){
		if(executorService==null){
			executorService = Executors.newFixedThreadPool(5);
		}
		executorService.execute(runnable);
	}
	
	public static void uiExecute(Runnable runnable){
		if(uiHandler == null){
			uiHandler = new Handler(Looper.getMainLooper());
		}
		uiHandler.post(runnable);
	}
    
}

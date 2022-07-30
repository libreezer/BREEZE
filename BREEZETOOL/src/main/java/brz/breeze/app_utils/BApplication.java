package brz.breeze.app_utils;
import android.app.Application;
import android.content.Context;
import brz.breeze.service_utils.BExceptionCatcher;
import brz.breeze.tool_utils.Blog;

public class BApplication extends Application{
    
    /*
	*@author BREEZE
	*@date 2022-07-02 20:11:12
    */
    public static final String TAG = "BApplication";

	private static Context mContext;
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		Thread.setDefaultUncaughtExceptionHandler(BExceptionCatcher.getInstance(this));
		Blog.init(this);
	}
	
	public static Context getContext(){
		return mContext;
	}
    
}

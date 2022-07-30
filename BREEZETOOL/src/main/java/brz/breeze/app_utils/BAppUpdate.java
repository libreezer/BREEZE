package brz.breeze.app_utils;
import android.content.Context;
import android.os.AsyncTask;
import brz.breeze.app_utils.BAppUtils;
import brz.breeze.web_utils.BWebUtils;
import org.json.JSONObject;
import brz.breeze.app_utils.BToast;
import brz.breeze.tool_utils.Blog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.FileOutputStream;

public class BAppUpdate {
    
    /*
	*@author BREEZE
	*@date 2021-08-22 12:00:06
    */
	private static String appLink = "";
	private static Context appContext = null;
	private static BUpdateMode data;
	private static BAppUpdateListener mListener;
	
	public static void setOnUpdateListener(Context context,String link,BAppUpdateListener listener){
		appContext = context;
		appLink = link;
		mListener = listener;
		new GetAppVersionData().execute(appLink);
	}
	
	private static class GetAppVersionData extends AsyncTask<String,Void,JSONObject> {

		@Override
		protected JSONObject doInBackground(String[] p1) {
			String link = p1[0];
			try{
				String result = BWebUtils.getWebContent(link);
				if(result.equals("")||result == null){
					return null;
				}else{
					JSONObject obj = new JSONObject(result);
					return obj;
				}
			}catch(Exception e){
				mListener.onError(e);
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			if(result!=null){
				data = new BUpdateMode();
				try{
					data.setAppName(result.getString("appName"));
					data.setAppVersion(result.getInt("appVersion"));
					data.setAppVersionName(result.getString("appVersionName"));
					data.setDownLink(result.getString("downLink"));
					data.setUpdateContent(result.getString("updateContent"));
					int appVersion = BAppUtils.getVersionCode(appContext);
					if(appVersion >= data.getAppVersion()){
						//没有更新
						mListener.noNewVersion();
					}else{
						//发现更新
						mListener.haveNewVersion(data);
					}
				}catch(Exception e){
					mListener.onError(e);
				}
			}
		}
		
	}
	
	public static class BUpdateMode{
		String appName,appVersionName,updateContent,downLink;
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
	
	public interface BAppUpdateListener{
		void haveNewVersion(BUpdateMode info);
		void noNewVersion();
		void onError(Exception exception);
	}

    
}

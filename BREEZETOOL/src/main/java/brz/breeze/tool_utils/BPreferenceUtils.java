package brz.breeze.tool_utils;
import android.content.Context;
import android.preference.PreferenceManager;
import java.util.Map;

public class BPreferenceUtils {
	
	public static String getStringValue(Context context,String key,String def){
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key,def);
	}
	
	public static int getIntValue(Context context,String key,int def){
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(key,def);
	}
	
	public static boolean getBooleanValue(Context context,String key,boolean def){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key,def);
	}
	
	public static float getFloatValue(Context context,String key,float def){
		return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key,def);
	}
	
	public static Map<String,?> getValue(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getAll();
	}
	
	public static void setStringValue(Context context,String key,String def){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key,def).apply();
	}

	public static void setIntValue(Context context,String key,int def){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key,def).apply();
	}

	public static void setBooleanValue(Context context,String key,boolean def){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key,def).apply();
	}

	public static void setFloatValue(Context context,String key,float def){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat(key,def).apply();
	}
    
    /*
	*@author BREEZE
	*@date 2021-06-08 10:08:06
    */

    
}

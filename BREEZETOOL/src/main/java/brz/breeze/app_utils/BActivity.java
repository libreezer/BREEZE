package brz.breeze.app_utils;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Map;

import android.content.SharedPreferences;

public abstract class BActivity extends Activity {
	
	public SharedPreferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preference = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public void toast(final String message){
		runOnUiThread(new Runnable(){
			public void run(){
				BToast.toast(getApplicationContext(),message,BToast.LENGTH_SHORT).show();
			}
		});
	}
	
	public abstract void init();
	public abstract void initData();

	public String getStringValue(String key,String def){
		return preference.getString(key,def);
	}

	public int getIntValue(String key,int def){
		return preference.getInt(key,def);
	}

	public boolean getBooleanValue(String key,boolean def){
		return preference.getBoolean(key,def);
	}

	public float getFloatValue(String key,float def){
		return preference.getFloat(key,def);
	}

	public Map<String,?> getValue(){
		return preference.getAll();
	}

	public void setStringValue(String key,String def){
		preference.edit().putString(key,def).apply();
	}

	public void setIntValue(String key,int def){
		preference.edit().putInt(key,def).apply();
	}

	public void setBooleanValue(String key,boolean def){
		preference.edit().putBoolean(key,def).apply();
	}

	public void setFloatValue(String key,float def){
		preference.edit().putFloat(key,def).apply();
	}

	public void hideActionBar(){
		if(getActionBar()!=null){
			getActionBar().hide();
		}
	}

	public <T> T find(int id) {
		return (T)findViewById(id);
	}
	
	public <T> T find(View baseView, int id){
		return (T)baseView.findViewById(id);
	}

    
}

package brz.breeze.app_utils;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BAppCompatActivity extends AppCompatActivity {

    public SharedPreferences preference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void toast(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public abstract void init();

    public abstract void initData();

    public void hideActionBar() {
        if (getActionBar() != null) {
            getActionBar().hide();
        }
    }

    protected void alert(String title, String content){
        new AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton("确定", null)
        .show();
    }

    /**
     * @param id  控件id
     * @param <T> 返回控件
     * @return 返回控件
     */
    public <T> T find(int id) {
        return (T) findViewById(id);
    }

    /**
     * @param baseView 基础view
     * @param id       控件id
     * @param <T>      返回控件类型
     * @return 返回T
     */
    public <T> T find(View baseView, int id) {
        return (T) baseView.findViewById(id);
    }
}

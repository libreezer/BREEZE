package breeze.jar.breeze;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.IOException;

import brz.breeze.app_utils.BAppCompatActivity;
import brz.breeze.app_utils.BAppUpdate;

public class MainActivity extends BAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void init() {
        //权限申请
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(permissions,1001);
            }
        }
    }

    @Override
    public void initData() {

    }

    public void multiThreadDownload(View view) {
        File externalStoragePublicDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/bbb");
        if (!externalStoragePublicDirectory.exists()){
            try {
                boolean newFile = externalStoragePublicDirectory.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
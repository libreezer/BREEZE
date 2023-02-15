package breeze.jar.breeze;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import brz.breeze.app_utils.BAppCompatActivity;
import brz.breeze.app_utils.BHotFixUtils;
import brz.breeze.web_utils.BWebUtils;

public class MainActivity extends BAppCompatActivity {

    private static final String TAG = "MainActivity";

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
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 1001);
            }
        }
    }

    @Override
    public void initData() {

    }

    public void multiThreadDownload(View view) {
        final String _API_HOT_FIX = "http://192.168.0.103:2199/HotFix.php?version=20220506";
        BHotFixUtils.init(this, _API_HOT_FIX, null);
        BHotFixUtils.checkPatch(new BHotFixUtils.HotFixListener() {
            @Override
            public void result(int status, String file) {
                Log.d(TAG, "result: " + status + "|" + file);
                String file2 = "http://192.168.0.103:2199/hotFix/"+file;
                BWebUtils.downloadFile(file2, new File(BHotFixUtils.patchStoragePath + "/" + file), new BWebUtils.WebDownloadListener() {
                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onSuccess(File targetPath) {
                        toast("成功");
                    }

                    @Override
                    public void onError(Exception exception) {
                        toast(exception.toString());
                    }
                });
            }
        });
    }
}
package brz.breeze.app_utils;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import brz.breeze.web_utils.BWebUtils;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class BHotFixUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static String API;
    public static File patchStoragePath;

    public static void init(Context context, String api, File savePatchDir) {
        mContext = context;
        API = api;
        if (savePatchDir == null) {
            patchStoragePath = context.getExternalFilesDir("patch");
        } else {
            patchStoragePath = savePatchDir;
        }
    }

    public interface HotFixListener {
        void result(int status, String file);
    }

    public static void checkPatch(final HotFixListener listener) {
        String NEWAPI = API.replace("{version}", String.valueOf(BAppUtils.getVersionCode(mContext)));
        BWebUtils.getWebData(NEWAPI, null, null, new BWebUtils.WebRequestCallBack() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int status = jsonObject.getInt("status");
                    String file = jsonObject.getString("file");
                    listener.result(status, file);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.result(0, e.toString());
                }
            }

            @Override
            public void onFailure(Exception exception) {
                listener.result(0, exception.toString());
            }
        });
    }

    public static void fixApp() throws Exception {
        File[] files = patchStoragePath.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".dex")) {
                patch(file.getAbsolutePath());
            }
        }
    }

    private static Object getDexElements(ClassLoader classLoader) throws Exception {
        //先获取pathList
        Field pathList = Class.forName("dalvik.system.BaseDexClassLoader").getDeclaredField("pathList");
        pathList.setAccessible(true);
        Object pathListValue = pathList.get(classLoader);

        //再获取dexElements
        Field dexElements = pathListValue.getClass().getDeclaredField("dexElements");
        dexElements.setAccessible(true);
        return dexElements.get(pathListValue);
    }

    private static Object combineArray(Object obj, Object obj2) {
        Class<?> componentType = obj.getClass().getComponentType();
        int length = Array.getLength(obj);
        int length1 = Array.getLength(obj2);
        Object newInstance = Array.newInstance(componentType, length + length1);
        for (int i = 0; i < length + length1; i++) {
            if (i < length1) {
                Array.set(newInstance, i, Array.get(obj2, i));
            } else {
                Array.set(newInstance, i, Array.get(obj, i - length1));
            }
        }
        return newInstance;
    }

    private static void setDexElements(ClassLoader classLoader, Object obj) throws Exception {
        Field pathList = Class.forName("dalvik.system.BaseDexClassLoader").getDeclaredField("pathList");
        pathList.setAccessible(true);
        Object pathListValue = pathList.get(classLoader);

        //再获取dexElements
        Field dexElements = pathListValue.getClass().getDeclaredField("dexElements");
        dexElements.setAccessible(true);
        dexElements.set(pathListValue, obj);
    }

    private static void patch(String dexFile) throws Exception {
        //获取dexElements值
        PathClassLoader classLoader = (PathClassLoader) mContext.getClassLoader();
        Object dexElements = getDexElements(classLoader);

        //存储补丁的文件夹
        String patchPath = mContext.getDir("patch", Context.MODE_PRIVATE).getAbsolutePath();
        //新建一个DexLoader并获取dexElements值
        DexClassLoader dexClassLoader = new DexClassLoader(dexFile, patchPath, dexFile, mContext.getClassLoader());
        Object dexElements1 = getDexElements(dexClassLoader);

        //结合
        Object combineArray = combineArray(dexElements, dexElements1);

        //设置到原来的那个加载器
        setDexElements(classLoader, combineArray);

        //重新加载类
        //classLoader.loadClass()
    }

}

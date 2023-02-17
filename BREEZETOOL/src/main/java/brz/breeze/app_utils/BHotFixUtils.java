package brz.breeze.app_utils;

import android.annotation.SuppressLint;
import android.content.Context;

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
        void result(int status, String file, String version);
    }

    public static void checkPatch(final HotFixListener listener) {
        String NEWAPI = API.replace("{version}", String.valueOf(BAppUtils.getVersionCode(mContext)));
        try {
            String data = BWebUtils.getWebData(NEWAPI, null, null);
            JSONObject jsonObject = new JSONObject(data);
            int status = jsonObject.getInt("status");
            String file = jsonObject.getString("file");
            String version = jsonObject.getString("version");
            listener.result(status, file, version);
        } catch (Exception exception) {
            exception.printStackTrace();
            listener.result(0, exception.toString(), "-1");
        }
    }

    public static void fixApp() throws Exception {
        File[] files = patchStoragePath.listFiles();
        if (files!=null){
            for (File file : files) {
                if (file.getName().endsWith(".dex")) {
                    patch(file.getAbsolutePath());
                }
            }
        }
    }

    private static Object getDexElements(ClassLoader classLoader) throws Exception {
        //先获取pathList
        Field pathList = Class.forName("dalvik.system.BaseDexClassLoader")
                .getDeclaredField("pathList");
        pathList.setAccessible(true);
        Object pathListValue = pathList.get(classLoader);

        //再获取dexElements
        Field dexElements = pathListValue.getClass().
                getDeclaredField("dexElements");
        dexElements.setAccessible(true);
        return dexElements.get(pathListValue);
    }


    private static Object combineArray(Object obj, Object obj2) {
        //获取数据类型
        Class<?> componentType = obj.getClass().getComponentType();
        if (componentType != null) {
            //获取两个数组的长度
            int length = Array.getLength(obj);
            int length1 = Array.getLength(obj2);
            //创建新的数组，长度为前两个数组长度之和
            Object newInstance = Array.newInstance(componentType, length + length1);
            //添加数据，把第二个数组的数据放在第一个之前
            for (int i = 0; i < length + length1; i++) {
                if (i < length1) {
                    Array.set(newInstance, i, Array.get(obj2, i));
                } else {
                    //i- length1是为了把指针归0来读取数组1的数据
                    Array.set(newInstance, i, Array.get(obj, i - length1));
                }
            }
            return newInstance;
        }
        return null;
    }

    private static void setDexElements(ClassLoader classLoader, Object obj) throws Exception {
        Field pathList = Class.forName("dalvik.system.BaseDexClassLoader")
                .getDeclaredField("pathList");
        pathList.setAccessible(true);
        Object pathListValue = pathList.get(classLoader);

        //再获取dexElements
        Field dexElements = pathListValue.getClass()
                .getDeclaredField("dexElements");
        dexElements.setAccessible(true);
        //把返回改成设置
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

        //重新加载指定类
        //classLoader.loadClass(类路径)
    }

}

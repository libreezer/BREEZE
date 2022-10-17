package brz.breeze.tool_utils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BToolUtils {

    public static final String TAG = "BToolUtils";


    public static String md5(String content) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("md5");
        byte[] bytes = digest.digest(content.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int var = bytes[i] & 0xff;
            if (var < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(var));
        }
        return sb.toString();
    }

    /**
     * @param format 类型
     * @author BREEZE
     */
    public static String getTime(String format) {
        return new SimpleDateFormat(format).format(System.currentTimeMillis());
    }

    private static final String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String getDayInWeek() {
        Calendar c = Calendar.getInstance();
        int week_day = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_day < 0) {
            week_day = 0;
        }
        return weeks[week_day];
    }

    /**
     * @author BREEZE
     * @description 获取线程
     */
    static Executors executors;
    static ExecutorService executorService;

    private static ExecutorService exe() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(5);
        }
        return executorService;
    }

    public static void execute(Runnable runnable) {
        exe().execute(runnable);
    }

	/**
	 * @param root 根命令 sh/su
	 * @param command 可执行命令
	 * @return
	 */
    public static String execShellCommand(String root, String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(root);
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(command + "\n");
            outputStream.close();
            process.waitFor();

            BufferedInputStream inputStream = new BufferedInputStream(process.getInputStream());
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            return new String(bytes);
        } catch (Exception e) {
            Blog.e(e);
            return null;
        }
    }

}

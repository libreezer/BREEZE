package brz.breeze.file_utils;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.content.Intent;

public class BFileUtils {

    public static final String TAG = "BFileUtils";
	
	public static final int CHOOSE_FILE_REQUEST = 100;

	/**
	 *@author BREEZE
	 *@param filePath 文件路径
	 *@param content 文件内容
	 */
    public static void writeFileContent(String filePath, String content) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(filePath);
		outputStream.write(content.getBytes());
		outputStream.close();
	}

	/**
	 *@author BREEZE
	 *@param filePath 文件路径
	 *@param content 追加内容
	 */
	public static void addContentToFile(String filePath, String content) throws IOException {
		FileWriter writer = new FileWriter(filePath, true);
		writer.write(content);
		writer.close();
	}

	/**
	 *@author BREEZE
	 *@param filePath 文件路径
	 */
	public static String readFileContent(String filePath) throws IOException {
		FileInputStream inputStream = new FileInputStream(filePath);
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		inputStream.close();
		return new String(bytes);
	}

	/**
	 *@author BREEZE
	 *@param path 文件路径
	 */
	public static void createFile(String path) throws IOException {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		String[] dir_path = path.split("/");
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		for (int i = 0;i < dir_path.length;i++) {
			if (i != dir_path.length - 1) {
				String child_path = dir_path[i] + "/";
				String child_dir_path = sb.toString() + child_path;
				System.out.println(child_dir_path);
				File file = new File(child_dir_path);
				if (!file.exists()) {
					file.mkdir();
				}
				sb.append(child_path);
			} else {
				sb.append(dir_path[i]);
			}
		}
        File file = new File(sb.toString());
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	/**
	 *@author BREEZE
	 *@param old 旧文件路径
	 *@param newFile 新文件路径
	 */
	public static void copyFile(final String old, final String newfile) throws IOException {
		int bytesum = 0;   
		int byteread = 0;   
		File oldfile = new File(old);   
		if (oldfile.exists()) { //文件存在时   
			InputStream inStream = new FileInputStream(old); //读入原文件   
			FileOutputStream fs = new FileOutputStream(newfile);   
			byte[] buffer = new byte[1444];   
			while ((byteread = inStream.read(buffer)) != -1) {   
				bytesum += byteread; //字节数 文件大小   
				fs.write(buffer, 0, byteread);   
			}   
			inStream.close();   
		}   
    }

	/**
	 *@author BREEZE
	 *@param context 上下文
	 *@param fileName 文件夹名称
	 */
	public static String getExternalCacheFile(Context context, String fileName) {
		String file = context.getExternalCacheDir() + "/" + fileName + "/";
		File realFile = new File(file);
		if (!realFile.exists()) {realFile.mkdir();}
		return file;
	}

	/**
	 *@author BREEZE
	 *@param context 上下文
	 *@param fileName 文件名称
	 */
	public static String getExternalFile(Context context, String fileName) {
		return context.getExternalFilesDir(fileName).getAbsolutePath();
	}

	public static String uriToFileApiQ(Uri uri, Context context) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
                    split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}
	

	private static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {
	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
			column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
														null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	private static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	
	public static void chooseFile(Activity activity,String mimeType){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(mimeType);
		activity.startActivityForResult(intent,CHOOSE_FILE_REQUEST);
	}
}

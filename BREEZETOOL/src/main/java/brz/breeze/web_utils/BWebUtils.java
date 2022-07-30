package brz.breeze.web_utils;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import brz.breeze.tool_utils.Blog;

public class BWebUtils {
    
    public static final String TAG = "BWebUtils";
	
    /**
	*@author BREEZE
	*@param picUri 图片直链
	*/
	public static Bitmap getWebPicture(String picUri) throws IOException{
		URL u = new URL(picUri);
		HttpURLConnection con = (HttpURLConnection)u.openConnection();
		con.setRequestMethod("GET");
		InputStream input = con.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		input.close();
		return bitmap;
	}
	
	/**
	*@author BREEZE
	*@param uri 网页链接
	*/
	public static String getWebContent(String uri) throws IOException{
		URL url = new URL(uri);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		InputStream input = con.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int len;
		while ((len = input.read(bytes)) != -1) {
			out.write(bytes, 0, len);
		}
		out.close();
		input.close();
		return new String(out.toByteArray(), StandardCharsets.UTF_8);
	}
	
	/**
	*@author BREEZE
	*@param uri 网页链接
	*@param post POST内容
	*/
	public static String postWebData(String uri,String post) throws IOException{
		URL url = new URL(uri);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		OutputStream outputStream = con.getOutputStream();
		outputStream.write(post.getBytes());
		InputStream input = con.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int len;
		while ((len = input.read(bytes)) != -1) {
			out.write(bytes, 0, len);
		}
		out.close();
		input.close();
		outputStream.close();
		return new String(out.toByteArray(), StandardCharsets.UTF_8);
	}
	
	public static WebView getWebView(final Context context){
		WebView webview = new WebView(context);
		WebSettings mWebSettings;
		mWebSettings = webview.getSettings();
		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setLoadsImagesAutomatically(true);
		mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webview.setWebViewClient(new WebViewClient(){
				public boolean shouldOverrideUrlLoading(WebView webvidw,String url){
					if(!url.startsWith("http")){
						try{
							Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
							context.startActivity(intent);
						}catch(Exception e){
						}
						if(url.startsWith("breeze")){
							webvidw.destroy();
						}
						return true;
					}else{
						webvidw.loadUrl(url);
						return false;
					}
				}
			});
		return webview;
	}
	
}

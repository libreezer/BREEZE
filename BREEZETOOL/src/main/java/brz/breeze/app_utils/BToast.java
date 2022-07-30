package brz.breeze.app_utils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.res.AssetManager;

public class BToast extends Toast {
	
	public static int FONT_ENGLISH = 1,FONT_CHINESE = 0;

	private final LinearLayout base_linearlayout;

	private final TextView textView;
	
	private final Context mContext;

    /*
	 *@author BREEZE
	 *@date 2021-05-11 08:38:28
	 */
	public BToast(Context context){
		super(context);
		this.mContext = context;
		base_linearlayout = new LinearLayout(context);
		LinearLayout.LayoutParams line_parame = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		textView = new TextView(context);
		textView.setTextColor(Color.BLACK);
		textView.setLayoutParams(line_parame);
		base_linearlayout.setPadding(25,15,25,15);
		base_linearlayout.setGravity(Gravity.CENTER);
		base_linearlayout.setLayoutParams(line_parame);
		base_linearlayout.setBackground(getRound());
		this.setView(base_linearlayout);
	}

	@Override
	public void setText(CharSequence s) {
		textView.setText(s);
	}

	@Override
	public void setText(int resId) {
		textView.setText(mContext.getString(resId));
	}
	
	private GradientDrawable getRound(){
		GradientDrawable g = new GradientDrawable();
		g.setColor(Color.WHITE);
		g.setStroke(2,Color.BLACK);
		g.setCornerRadius(30);
		return g;
	}
	
	public BToast setTypeface(Typeface typeface){
		textView.setTypeface(typeface);
		return this;
	}

	@Override
	public void show() {
		base_linearlayout.addView(textView);
		super.show();
	}
	
	public static BToast toast(Context context,String content,int duration){
		BToast mToast = new BToast(context);
		mToast.setText(content);
		mToast.setDuration(duration);
		return mToast;
	}
}


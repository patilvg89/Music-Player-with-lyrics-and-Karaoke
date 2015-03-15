package com.mukavi.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.mukavi.app.R;

public class BottomBar extends RelativeLayout {
	public static final String TAG = BottomBar.class.getSimpleName();
	//private TextView tvTitle;
	private LayoutInflater inflater;

	public BottomBar(Context context) {
		super(context);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public BottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		inflater.inflate(R.layout.bottombar_layout, this);
	}

}

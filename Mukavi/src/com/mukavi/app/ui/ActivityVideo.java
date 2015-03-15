package com.mukavi.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MediaController;
import android.widget.VideoView;
import com.mukavi.app.R;

public class ActivityVideo extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("Video");
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(
						R.color.color_actionbar)));
		
		BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBarMain);
		bottomBar.findViewById(R.id.btnBottomHome).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomKaraoke).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomMusic).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomVideo).setVisibility(View.GONE);
		bottomBar.findViewById(R.id.btnBottomLyric).setOnClickListener(this);
		
		VideoView videoView = (VideoView) findViewById(R.id.videoView1);
		// Creating MediaController
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		String UrlPath="android.resource://"+getPackageName()+"/"+R.raw.boyfriend;
		videoView.setVideoURI(Uri.parse(UrlPath));
		videoView.requestFocus();
		videoView.start();

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnBottomHome:
			this.finish();
			break;
		case R.id.btnBottomKaraoke:
			this.finish();
			startActivity(new Intent(this, ActivityKaraoke.class));
			break;
		case R.id.btnBottomMusic:
			this.finish();
			startActivity(new Intent(this, ActivityPlayList.class));
			break;
		case R.id.btnBottomLyric:
			this.finish();
			startActivity(new Intent(this, ActivityLyrics.class));
			break;
		default:
			break;
		}
	
	}

}

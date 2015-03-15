package com.mukavi.app.ui;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mukavi.app.R;

public class ActivityPlayAudio extends Activity implements OnClickListener{
	String outputFile;
	Button play;
	private MediaPlayer mediaplayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_audio);
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("My Records");
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(
						R.color.color_actionbar)));
		
		
		BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBarMain);
		bottomBar.findViewById(R.id.btnBottomHome).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomKaraoke).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomMusic).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomVideo).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomLyric).setOnClickListener(this);
		
		play = (Button) findViewById(R.id.buttonPlay);
		
		mediaplayer = new MediaPlayer();
		
		
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String outputFile = getIntent().getStringExtra("audio");
				
				try {
					mediaplayer.setDataSource(outputFile);
					mediaplayer.prepare();
				} catch (IllegalArgumentException illegalargumentexception) {
					illegalargumentexception.printStackTrace();
				} catch (SecurityException securityexception) {
					securityexception.printStackTrace();
				} catch (IllegalStateException illegalstateexception) {
					illegalstateexception.printStackTrace();
				} catch (IOException ioexception) {
					ioexception.printStackTrace();
				}
				mediaplayer.start();
				Toast.makeText(getApplicationContext(), "Playing audio", 1)
						.show();
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (mediaplayer != null) {
			mediaplayer.stop();
			mediaplayer.release();
		}
		
	}
	@Override
	public void onClick(View v) {
		if (mediaplayer != null) {
			mediaplayer.stop();
			mediaplayer.release();
		}

		switch (v.getId()) {
		case R.id.btnBottomHome:
			ActivityKaraoke.getInstance().finish();
			this.finish();
			break;
		case R.id.btnBottomKaraoke:
			this.finish();
			ActivityKaraoke.getInstance().finish();
			startActivity(new Intent(this, ActivityKaraoke.class));
			break;
		case R.id.btnBottomMusic:
			this.finish();
			ActivityKaraoke.getInstance().finish();
			startActivity(new Intent(this, ActivityPlayList.class));
			break;
		case R.id.btnBottomLyric:
			this.finish();
			ActivityKaraoke.getInstance().finish();
			startActivity(new Intent(this, ActivityLyrics.class));
			break;
		case R.id.btnBottomVideo:
			this.finish();
			ActivityKaraoke.getInstance().finish();
			startActivity(new Intent(this, ActivityVideo.class));
			break;
		default:
			break;
		}
	
	
	}
}

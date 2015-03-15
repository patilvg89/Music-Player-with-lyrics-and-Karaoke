package com.mukavi.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.mukavi.app.R;
import com.mukavi.lrcView.DefaultLrcBuilder;
import com.mukavi.lrcView.ILrcBuilder;
import com.mukavi.lrcView.ILrcView;
import com.mukavi.lrcView.LrcRow;
import com.mukavi.lrcView.LrcView;
import com.mukavi.lrcView.ILrcView.LrcViewListener;
import com.mukavi.utils.Constant;
import com.mukavi.utils.SongsManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ActivityKaraoke extends Activity implements OnCompletionListener,
		OnClickListener {
	public final static String TAG = "ActivityKaraoke";
	ILrcView mLrcView;
	private int mPalyTimerDuration = 1000;
	private Timer mTimer;
	private TimerTask mTask;
	private MediaRecorder myAudioRecorder;
	private String outputFile = null;
	ImageButton start, stop, play;

	public String getFromAssets(String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null) {
				if (line.trim().equals(""))
					continue;
				Result += line + "\r\n";
			}
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	static ActivityKaraoke activityKaraoke;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLrcView = new LrcView(this, null);

		setContentView(R.layout.activity_karaoke);

		((LinearLayout) findViewById(R.id.lyric_parent))
				.addView((View) mLrcView);

		activityKaraoke = this;

		start = (ImageButton) findViewById(R.id.btnStart);
		start.setOnClickListener(this);
		stop = (ImageButton) findViewById(R.id.btnStop);
		stop.setOnClickListener(this);
		play = (ImageButton) findViewById(R.id.btnPlay);
		play.setOnClickListener(this);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("Karaoke");
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(
						R.color.color_actionbar)));

		BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBarMain);
		bottomBar.findViewById(R.id.btnBottomHome).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomLyric).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomMusic).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomKaraoke).setVisibility(View.GONE);
		bottomBar.findViewById(R.id.btnBottomVideo).setOnClickListener(this);

		stop.setEnabled(false);
		play.setEnabled(false);
		outputFile = (new StringBuilder(String.valueOf(Environment
				.getExternalStorageDirectory().getAbsolutePath()))).append(
				"/myrecording.3gp").toString();
		myAudioRecorder = new MediaRecorder();
		myAudioRecorder.setAudioSource(1);
		myAudioRecorder.setOutputFormat(1);
		myAudioRecorder.setAudioEncoder(3);
		myAudioRecorder.setOutputFile(outputFile);

		String lrc = getFromAssets(Constant.KaraokeName);
		Log.d(TAG, "lrc:" + lrc);

		ILrcBuilder builder = new DefaultLrcBuilder();
		List<LrcRow> rows = builder.getLrcRows(lrc);

		mLrcView.setLrc(rows);
		beginLrcPlay();

		mLrcView.setListener(new LrcViewListener() {

			public void onLrcSeeked(int newPosition, LrcRow row) {
				if (mPlayer != null) {
					Log.d(TAG, "onLrcSeeked:" + row.time);
					mPlayer.seekTo((int) row.time);
				}
			}
		});
	}

	// create to get this activity context within an app
	public static ActivityKaraoke getInstance() {
		return activityKaraoke;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopLrcPlay();
		if (mPlayer != null) {
			mPlayer.stop();
		}
		mPlayer.release();
	}

	MediaPlayer mPlayer;

	public void beginLrcPlay() {
		mPlayer = new MediaPlayer();

		try {
			mPlayer=MediaPlayer.create(ActivityKaraoke.this.getBaseContext(), R.raw.justein_biber_boyfriend_instrumental);
			mPlayer.setOnPreparedListener(new OnPreparedListener() {

				public void onPrepared(MediaPlayer mp) {
					Log.d(TAG, "onPrepared");
					mp.start();
					if (mTimer == null) {
						mTimer = new Timer();
						mTask = new LrcTask();
						mTimer.scheduleAtFixedRate(mTask, 0, mPalyTimerDuration);
					}
				}
			});
			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				public void onCompletion(MediaPlayer mp) {
					stopLrcPlay();
					mp.stop();
					mp.release();
				}
			});
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void stopLrcPlay() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	class LrcTask extends TimerTask {

		long beginTime = -1;

		@Override
		public void run() {
			if (beginTime == -1) {
				beginTime = System.currentTimeMillis();
			}

			final long timePassed = mPlayer.getCurrentPosition();
			runOnUiThread(new Runnable() {

				public void run() {
					mLrcView.seekLrcToTime(timePassed);
				}
			});
		}
	};

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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBottomHome:
			this.finish();
			break;
		case R.id.btnBottomLyric:
			this.finish();
			startActivity(new Intent(this, ActivityLyrics.class));
			break;
		case R.id.btnBottomMusic:
			this.finish();
			startActivity(new Intent(this, ActivityPlayList.class));
			break;
		case R.id.btnBottomVideo:
			this.finish();
			startActivity(new Intent(this, ActivityVideo.class));
			break;
		case R.id.btnStart:

			try {
				myAudioRecorder.prepare();
				myAudioRecorder.start();
			} catch (IllegalStateException illegalstateexception) {
				illegalstateexception.printStackTrace();
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
			}
			start.setEnabled(false);
			start.setVisibility(View.INVISIBLE);
			stop.setVisibility(View.VISIBLE);
			stop.setEnabled(true);
			getActionBar().setTitle("Karaoke Recording");
			Toast.makeText(getApplicationContext(), "Recording started", 1)
					.show();
			break;
		case R.id.btnStop:
			myAudioRecorder.stop();
			myAudioRecorder.release();
			myAudioRecorder = null;

			start.setEnabled(true);
			start.setVisibility(View.INVISIBLE);
			stop.setEnabled(false);
			stop.setVisibility(View.INVISIBLE);
			play.setVisibility(View.VISIBLE);
			play.setEnabled(true);
			mPlayer.pause();
			getActionBar().setTitle("Karaoke Recorded");
			Toast.makeText(getApplicationContext(),
					"Audio recorded successfully", 1).show();
			break;
		case R.id.btnPlay:
			if (mPlayer != null) {
				mPlayer.stop();
			}
			Intent intent = new Intent(this, ActivityPlayAudio.class);
			intent.putExtra("audio", outputFile);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		stopLrcPlay();
		mp.release();
	}

}

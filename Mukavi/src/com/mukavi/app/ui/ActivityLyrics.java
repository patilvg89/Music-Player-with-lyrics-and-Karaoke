package com.mukavi.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mukavi.app.R;
import com.mukavi.lrcView.DefaultLrcBuilder;
import com.mukavi.lrcView.ILrcBuilder;
import com.mukavi.lrcView.ILrcView;
import com.mukavi.lrcView.ILrcView.LrcViewListener;
import com.mukavi.lrcView.LrcRow;
import com.mukavi.lrcView.LrcView;
import com.mukavi.utils.Constant;

public class ActivityLyrics extends Activity implements OnClickListener {
	public final static String TAG = "ActivityLyrics";
	ILrcView mLrcView;
	private int mPalyTimerDuration = 1000;
	private Timer mTimer;
	private TimerTask mTask;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLrcView = new LrcView(this, null);

		setContentView(R.layout.activity_lyrics);

		((LinearLayout) findViewById(R.id.lyrics_parent))
				.addView((View) mLrcView);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("Lyrics");
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(
						R.color.color_actionbar)));

		BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBarMain);
		bottomBar.findViewById(R.id.btnBottomHome).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomKaraoke).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomMusic).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomLyric).setVisibility(View.GONE);
		bottomBar.findViewById(R.id.btnBottomVideo).setOnClickListener(this);

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
			mPlayer.setDataSource(getAssets().openFd(Constant.SongNameHavingKaraoke)
					.getFileDescriptor());
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
				}
			});
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setVolume(0, 0);
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
		case R.id.btnBottomKaraoke:
			this.finish();
			startActivity(new Intent(this, ActivityKaraoke.class));
			break;
		case R.id.btnBottomMusic:
			this.finish();
			startActivity(new Intent(this, ActivityPlayList.class));
			break;
		case R.id.btnBottomVideo:
			this.finish();
			startActivity(new Intent(this, ActivityVideo.class));
			break;
		default:
			break;
		}
	}
}

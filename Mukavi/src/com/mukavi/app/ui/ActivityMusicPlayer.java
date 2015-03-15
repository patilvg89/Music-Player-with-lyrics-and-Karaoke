package com.mukavi.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mukavi.app.R;
import com.mukavi.lrcView.DefaultLrcBuilder;
import com.mukavi.lrcView.ILrcBuilder;
import com.mukavi.lrcView.ILrcView;
import com.mukavi.lrcView.ILrcView.LrcViewListener;
import com.mukavi.lrcView.LrcRow;
import com.mukavi.lrcView.LrcView;
import com.mukavi.utils.SongsManager;

public class ActivityMusicPlayer extends Activity implements
		OnCompletionListener, OnClickListener {

	private ImageButton btnPlay;
	private TextView songTitleLabel;
	// LRC
	ILrcView mLrcView;
	private int mPalyTimerDuration = 1000;
	private Timer mTimer;
	private TimerTask mTask;
	// Media Player
	private MediaPlayer mp;
	// Handler to update UI timer, progress bar etc,.
	private SongsManager songManager;
	private int currentSongIndex = 0;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_player);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("Music");
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(
						R.color.color_actionbar)));

		BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBarMain);
		bottomBar.findViewById(R.id.btnBottomHome).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomKaraoke).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomLyric).setOnClickListener(this);
		bottomBar.findViewById(R.id.btnBottomMusic).setVisibility(View.GONE);
		bottomBar.findViewById(R.id.btnBottomVideo).setOnClickListener(this);

		mLrcView = new LrcView(this, null);
		// All player buttons
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		((LinearLayout) findViewById(R.id.lyric_parent))
				.addView((View) mLrcView);
		// Mediaplayer
		mp = new MediaPlayer();
		String targetFolder = getApplicationContext().getExternalFilesDir(null)+"";
		songManager = new SongsManager(targetFolder);

		// Listeners
		mp.setOnCompletionListener(this); // Important

		// Getting all songs list
		songsList = songManager.getPlayList();

		// By default play  song
		int songIndex=getIntent().getIntExtra("songIndex", 0);
		playSong(songIndex);

		/**
		 * Play button click event plays a song and changes button to pause
		 * image pauses a song and changes button to play image
		 * */
		btnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// check for already playing
				if (mp.isPlaying()) {
					if (mp != null) {
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				} else {
					// Resume song
					if (mp != null) {
						mp.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}

			}
		});

		mLrcView.setListener(new LrcViewListener() {
			public void onLrcSeeked(int newPosition, LrcRow row) {
				if (mp != null) {
					Log.d("TAG", "onLrcSeeked:" + row.time);
					mp.seekTo((int) row.time);
				}
			}
		});
	}

	/**
	 * Receiving song index from playlist view and play the song
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			currentSongIndex = data.getExtras().getInt("songIndex");
			// play selected song
			playSong(currentSongIndex);
		}

	}

	/**
	 * Function to play a song
	 * 
	 * @param songIndex
	 *            - index of song
	 * */
	public void playSong(int songIndex) {
		// Play song
		try {
			mp.reset();
			mp.setDataSource(songsList.get(songIndex).get("songPath"));
			mp.prepare();
			mp.start();
			// Displaying Song title
			String songTitle = songsList.get(songIndex).get("songTitle");
			songTitleLabel.setText(songTitle);

			// Changing Button Image to pause image
			btnPlay.setImageResource(R.drawable.btn_pause);

			if (mTimer == null) {
				mTimer = new Timer();
				mTask = new LrcTask();
				mTimer.scheduleAtFixedRate(mTask, 0, mPalyTimerDuration);
			}

			String lrc = getFromAssets("Boyfriend.lrc");
			Log.d("TAG", "lrc:" + lrc);
			ILrcBuilder builder = new DefaultLrcBuilder();
			List<LrcRow> rows = builder.getLrcRows(lrc);
			mLrcView.setLrc(rows);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * On Song Playing completed if repeat is ON play same song again if shuffle
	 * is ON play random song
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {

		// no repeat or shuffle ON - play next song
		if (currentSongIndex < (songsList.size() - 1)) {
			playSong(currentSongIndex + 1);
			currentSongIndex = currentSongIndex + 1;
		} else {
			// play first song
			playSong(0);
			currentSongIndex = 0;
		}
		stopLrcPlay();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.release();
	}

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

			final long timePassed = mp.getCurrentPosition();
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
			mp.release();
			stopLrcPlay();
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		mp.release();
		stopLrcPlay();
		switch (v.getId()) {
		case R.id.btnBottomHome:
			this.finish();
			break;
		case R.id.btnBottomKaraoke:
			this.finish();
			startActivity(new Intent(this, ActivityKaraoke.class));
			break;
		case R.id.btnBottomLyric:
			this.finish();
			startActivity(new Intent(this, ActivityLyrics.class));
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
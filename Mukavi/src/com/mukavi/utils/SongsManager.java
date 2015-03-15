package com.mukavi.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;

public class SongsManager {
	Context con;
	// SDCard Path
	final String MEDIA_PATH;

	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	// Constructor
	public SongsManager(String path) {
		this.MEDIA_PATH = path;
	}

	/**
	 * Function to read all mp3 files from sdcard and store the details in
	 * ArrayList
	 * */
	public ArrayList<HashMap<String, String>> getPlayList() {
		System.out.println(MEDIA_PATH);
		File home = new File(MEDIA_PATH);

		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put(
						"songTitle",
						file.getName().substring(0,
								(file.getName().length() - 4)));
				song.put("songPath", file.getPath());

				// Adding each song to SongList
				songsList.add(song);
			}
		}
		// return songs list array
		return songsList;
	}

	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
}
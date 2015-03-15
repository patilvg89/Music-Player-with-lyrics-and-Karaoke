package com.mukavi.app.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.mukavi.app.R;

public class ActivityLogin extends SherlockActivity implements OnClickListener {
	private EditText editPassword, editUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity__login);
		getSupportActionBar().hide();

		new Thread(new Runnable() {
			@Override
			public void run() {
				copyAssets();
			}
		}).start();

		// find edit test ids
		editUsername = (EditText) findViewById(R.id.editTextUsername);
		editPassword = (EditText) findViewById(R.id.editTextPassword);
		findViewById(R.id.btnLogin).setOnClickListener(this);
	}

	private void copyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		for (String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				File outFile = new File(getExternalFilesDir(null), filename);
				Log.e("tag", "outFile: " + outFile);
				out = new FileOutputStream(outFile);
				copyFile(in, out);
			} catch (IOException e) {
				Log.e("tag", "Failed to copy asset file: " + filename, e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// NOOP
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// NOOP
					}
				}
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			String user = editUsername.getText().toString().trim();
			String pass = editPassword.getText().toString().trim();

			if (user.equals("admin") && pass.equals("admin")) {

				startActivity(new Intent(ActivityLogin.this,
						Activity_Dashboard.class)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				editUsername.clearComposingText();
				editPassword.clearComposingText();
				Toast.makeText(this, "Enter valid username or password !",
						Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}
	}
}

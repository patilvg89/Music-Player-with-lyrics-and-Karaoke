package com.mukavi.app.ui;

import com.mukavi.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class FragmentDashboard extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_dashboard, container, false);

		rootView.findViewById(R.id.btn_lyric).setOnClickListener(this);
		rootView.findViewById(R.id.btn_karaoke).setOnClickListener(this);
		rootView.findViewById(R.id.btn_music).setOnClickListener(this);
		rootView.findViewById(R.id.btn_video).setOnClickListener(this);

		return rootView;

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_lyric:
			getActivity().startActivity(new Intent(getActivity(), ActivityLyrics.class));
			break;
		case R.id.btn_karaoke:
			getActivity().startActivity(new Intent(getActivity(), ActivityKaraoke.class));
			break;
		case R.id.btn_music:
			getActivity().startActivity(new Intent(getActivity(), ActivityPlayList.class));
			break;
		case R.id.btn_video:
			getActivity().startActivity(new Intent(getActivity(), ActivityVideo.class));
			break;
		default:
			break;
		}
	}
}

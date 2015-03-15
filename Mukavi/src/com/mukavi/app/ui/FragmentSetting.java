package com.mukavi.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.mukavi.app.R;

public class FragmentSetting extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_setting, container, false);

		rootView.findViewById(R.id.imageView1).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Fragment newContent = new FragmentDashboard();
						getActivity().getActionBar().setTitle("Mukavi");
						String FragmentTag = "FragmentDashboard";
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager
								.beginTransaction()
								.replace(R.id.content_frame, newContent,
										FragmentTag)
								.addToBackStack(FragmentTag).commit();
					}
				});

		return rootView;

	}
}
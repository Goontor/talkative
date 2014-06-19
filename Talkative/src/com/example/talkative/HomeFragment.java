package com.example.talkative;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class HomeFragment extends Fragment {

	public HomeFragment() {
	}

	TextView Grettings;
	Spinner talkingLang;
	ImageButton matchingButton;
	Spinner knownLang;
	VCard userCard;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.chat_matcher, container,
				false);

		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userCard = new VCard();
		matchingButton = (ImageButton) getView().findViewById(
				R.id.matchingImage);
		Grettings = (TextView) getView().findViewById(R.id.grettingCM);
		knownLang = (Spinner) getView().findViewById(R.id.spinLangNatMatch);
		talkingLang = (Spinner) getView().findViewById(R.id.spinLangMatch);
		try {

			userCard.load(ConnexionService.con);
			if (userCard.getLastName() == null) {
				userCard.setField("knownLanguage", "French");
				userCard.setField("talkingLanguage", "English");
				try {
					userCard.save(ConnexionService.con);
				} catch (XMPPException e2) {
					Log.d("New card failed", "Error: " + e2);
				}
			}
			Log.d("Card Loading", "By the try" + userCard);

		} catch (Exception e) {
			Log.d("Card Loading",
					"By the catch: "
							+ ConnexionService.conAcc
									.getAccountAttribute("username")
							+ "   ERROR:   " + e);

			userCard.setField("knownLanguage", "French");
			userCard.setField("talkingLanguage", "English");
			try {
				userCard.save(ConnexionService.con);
			} catch (XMPPException e2) {
				Log.d("New card failed", "Error: " + e2);
			}
		}
		if (ConnexionService.conAcc != null) {
			Grettings.setText(ConnexionService.conAcc
					.getAccountAttribute("name"));
		}

		matchingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userCard.setField("knownLanguage", knownLang.getSelectedItem()
						.toString());
				userCard.setField("talkingLanguage", talkingLang
						.getSelectedItem().toString());
				ConnexionService.countryMatcher = new ArrayList<String>();
				ConnexionService.countryMatcher.add(knownLang
						.getSelectedItem().toString());
				ConnexionService.countryMatcher.add(talkingLang
						.getSelectedItem().toString());
				ConnexionService.matcher = true;
				try {
					Log.d("Save ok", "Save ok" + userCard);
					userCard.save(ConnexionService.con);
				} catch (XMPPException e) {
					Log.d("Save error", "" + e);
				}
				getView().invalidate();
				Intent i = new Intent(getActivity(), MatcherActivity.class);
				startActivity(i);
			}
		});

		// Setting Spinner Values
		ArrayAdapter talkingAdap = (ArrayAdapter) talkingLang.getAdapter();
		Log.d("talkingValeur", "" + userCard.getField("talkingLanguage"));
		Integer talkingPos = talkingAdap.getPosition(userCard
				.getField("talkingLanguage"));
		Log.d("takingPos", "" + talkingPos);
		talkingLang.setSelection(talkingPos);
		ArrayAdapter knownAdap = (ArrayAdapter) knownLang.getAdapter();
		Log.d("knownValeur", "" + userCard.getField("knownLanguage"));
		Integer knownPos = talkingAdap.getPosition(userCard
				.getField("knownLanguage"));
		Log.d("knownPos", "" + knownPos);
		knownLang.setSelection(knownPos);
		// /////Setting Spinner Values (END)

	}

	private void stackAFragment() {
        Fragment f;
        f = new ChatFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, f);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}

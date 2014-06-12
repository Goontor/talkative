package com.example.talkative;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ChatMatcher extends Activity {
	TextView Grettings;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_matcher);
		Grettings = (TextView) findViewById(R.id.grettingCM);
			if(ConnexionService.conAcc!=null){
				Grettings.setText(ConnexionService.conAcc.getAccountAttribute("name"));
			}
		
	}
}

package com.example.talkative;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Login extends Activity {
	TextView SignUp;
	Intent SignUpInt;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		SignUpInt = new Intent(this, SignIn.class);
		SignUp = (TextView) findViewById(R.id.signInLog);
		SignUp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				startActivity(SignUpInt);
				
			}
			
		});
	}

}

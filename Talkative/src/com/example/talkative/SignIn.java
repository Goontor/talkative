package com.example.talkative;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignIn extends Activity {
	Button submitButton;
	EditText pseudo;
	EditText password;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		pseudo = (EditText) findViewById(R.id.pseudosi);
		password = (EditText) findViewById(R.id.passwordsi);
		submitButton = (Button) findViewById(R.id.submitsi);
	}
}

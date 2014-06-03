package com.example.talkative;

import org.jivesoftware.smack.XMPPException;

import com.example.talkative.ConnexionService.LocalBinder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	TextView SignUp;
	EditText Pseudo;
	EditText Password;
	Intent SignUpInt;
	Intent LogInInt;
	Button SubmitButton;
	boolean mBounded;
	ConnexionService mCService;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		SignUpInt = new Intent(this, SignIn.class);
		LogInInt = new Intent(this, MainActivity.class);
		SignUp = (TextView) findViewById(R.id.signInLog);
		Pseudo = (EditText) findViewById(R.id.usernamLog);
		Password = (EditText) findViewById(R.id.passwordLog);
		SubmitButton = (Button) findViewById(R.id.submitButtonLog);
		Intent i = new Intent(Login.this, ConnexionService.class);
		Login.this.startService(i);
		SubmitButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ConnectMe task = new ConnectMe();
			    task.execute(new String[] { "super string" });
			}

		});

		SignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(SignUpInt);

			}

		});
	}

	protected void onStart() {
		Log.d("onstart", "onstart1");
		super.onStart();
		Log.d("onstart", "onstart1");
		Intent mIntent = new Intent(this, ConnexionService.class);
		Log.d("onstart", "onstart1");
		bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
		Log.d("onstart", "onstart1");
	};

	ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
			Log.d("onstart", "onstartDic1");
			Toast.makeText(Login.this, "Service is disconnected", 1000).show();
			mBounded = false;
			mCService = null;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d("onstart", "onstartCon1");
			Toast.makeText(Login.this, "Service is connected", 1000).show();
			mBounded = true;
			LocalBinder mLocalBinder = (LocalBinder) service;
			mCService = mLocalBinder.getConnexionInstance();
		}
	};
	
	
	private class ConnectMe extends AsyncTask<String, Void, String> {
		final ProgressDialog dialog = ProgressDialog.show(
				Login.this, "Connecting...", "Please wait...",
				false);
		protected void onPreExecute() {
			
			dialog.show();
		}

		protected String doInBackground(String... arg0) {
			try {
				mCService.connect(Pseudo.getText().toString(),
						Password.getText().toString());
				Log.d("toto", "onclick2");
			} catch (Exception e) {
				Log.d("connexion error", " is " + e);
			}
			String response="malin";
			return response;
		}

		protected void onPostExecute(String result) {
			dialog.dismiss(); 
			startActivity(LogInInt);
		}

	}
}

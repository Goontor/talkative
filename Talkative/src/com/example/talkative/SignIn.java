package com.example.talkative;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignIn extends Activity  {
	Button submitButton;
	EditText pseudo;
	EditText password;
	EditText email;

	// Progress Dialog
    private ProgressDialog pDialog;

 

    private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/register.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String HOST = "188.226.205.160";
	public static final int PORT = 5222;
	public static final String SERVICE = "talkative.com";
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		pseudo = (EditText) findViewById(R.id.pseudosi);
		password = (EditText) findViewById(R.id.passwordsi);
		email = (EditText) findViewById(R.id.emailsi);
		submitButton = (Button) findViewById(R.id.submitsi);
		submitButton.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				Log.d("eeee", "click");
				CreateUser();}
		});

	}
	public void CreateUser() {

		final ProgressDialog dialog = ProgressDialog.show(this,
				"Creating...", "Please wait...", false);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
	           int success;
	           String pseudoS = pseudo.getText().toString();
	           String passwordS = password.getText().toString();
	           String emailS = email.getText().toString();
           
           
           try {
               // Building Parameters
               Map<String,String> params = new HashMap<String,String>();
               params.put("username", pseudoS);
               params.put("password", passwordS);
               params.put("email", emailS);
               params.put("name", pseudoS);
               Log.d("request!", "starting");
               ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT,SERVICE);
               XMPPConnection con = new XMPPConnection (connConfig);
               
               Log.d("request!", "starting2"+con.getRoster().toString());
               try{
            	   con.connect();
            	   Log.d("request!", "starting3");
            	   Log.d("request!", "starting4");
               }
               catch(XMPPException e){
            	   Toast.makeText(SignIn.this, "connexion eror: "+e,3000).show();
               }
               AccountManager am = new AccountManager(con);
               if(am.supportsAccountCreation()){
	               try{
	            	   Log.d("request!", "starting5");
	            	   
	            	 am.createAccount(pseudoS, passwordS,params);
	            	 Log.d("request!", "starting6");
	               }
	               catch(XMPPException e){
	            	   Log.d("request!", "starting7 "+e);
	            	   Toast.makeText(SignIn.this, "Adding account Error: "+e,3000).show();
	               }
               }
               else{
            	   Log.d("request!", "starting8: Not supported");
               }
               con.disconnect();
           } catch (Exception e) {
               
           }
           dialog.dismiss();

		}
		/**
        * After completing background task Dismiss the progress dialog
        * **/
           
			
		});
		
		t.start();
		dialog.show();

	}
}

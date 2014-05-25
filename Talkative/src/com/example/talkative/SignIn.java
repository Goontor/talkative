package com.example.talkative;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script

    //localhost :
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
   // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/register.php";

    //testing on Emulator:
    private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/register.php";

  //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/register.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String HOST = "188.226.205.160";
	public static final int PORT = 5222;
	public static final String SERVICE = "localhost";
	

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
	/*class CreateUser extends AsyncTask<String, String, String> {

		 /**
        * Before starting background thread Show Progress Dialog
        * 
		boolean failure = false;

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           Log.d("eeee", "pre");
           
           pDialog = new ProgressDialog(SignIn.this);
           pDialog.setMessage("Creating User...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }

		protected String doInBackground(String... args) {*/
			// TODO Auto-generated method stub
			 // Check for success tag
	public void CreateUser() {

		final ProgressDialog dialog = ProgressDialog.show(this,
				"Connecting...", "Please wait...", false);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
           int success;
           String pseudoS = pseudo.toString();
           String passwordS = password.toString();
           String emailS = email.toString();
           
           try {
               // Building Parameters
               Map<String,String> params = new HashMap<String,String>();
               params.put("username", pseudoS);
               params.put("password", passwordS);
               params.put("email", emailS);
               params.put("name", pseudoS);
               Log.d("request!", "starting");
               ConnectionConfiguration connConfig = new ConnectionConfiguration(
						HOST, PORT,SERVICE);
               XMPPConnection con = new XMPPConnection (connConfig);
               Log.d("request!", "starting2");
               try{
            	   con.connect();
            	   Log.d("request!", "starting3");
            	   con.login("admin", "dratar1er");
            	   Log.d("request!", "starting4");
               }
               catch(XMPPException e){
            	   Toast.makeText(SignIn.this, "connexion eror: "+e,3000).show();
               }
               AccountManager am = new AccountManager(con);
               try{
            	   Log.d("request!", "starting5");
            	 am.createAccount(pseudoS, passwordS, params);
            	 Log.d("request!", "starting6");
               }
               catch(XMPPException e){
            	   Toast.makeText(SignIn.this, "Adding account Error: "+e,3000).show();
               }
               con.disconnect();
               //Posting user data to script
               
               //JSONObject json = jsonParser.makeHttpRequest(
               //     "http://188.226.205.160/:9090/plugins/userService/userservice", "GET", params);

               // full json response
               //Log.d("Login attempt", json.toString());

               // json success element
               //success = json.getInt("Succes");
               //if (success == 1) {
               	//Log.d("User Created!", json.toString());
               	//finish();
               	//return json.getString("Tag message");
               //}else{
               	//Log.d("Login Failure!", json.getString("Tag message"));
               	//return json.getString("Tag message");

               //}
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

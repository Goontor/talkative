package com.example.talkative;

import java.util.ArrayList;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.packet.VCard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class MatcherActivity extends Activity{

	
public static final String HOST = "188.226.205.160";

public static final int PORT = 5222;
public static final String SERVICE = "localhost";
public static final String USERNAME = "tomy";
public static final String PASSWORD = "tomy";

private XMPPConnection connection;
private ArrayList<String> messages = new ArrayList<String>();
private Handler mHandler = new Handler();
public VCard contact;
private String recipient;
public ProgressDialog dialog;
private DiscussionHistory history;
private Button quitRequest;
private Button quitReject;
private TextView headerRecipent;
private EditText textMessage;
private ListView listview;
String toastText;
private String text;

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.matcherlayout);
	SmackAndroid.init(this);
	recipient="";
	textMessage = (EditText) findViewById(R.id.chatETMat);
	quitRequest = (Button) findViewById(R.id.buttonAdd);
	quitReject = (Button) findViewById(R.id.buttonReject);
	listview = (ListView) findViewById(R.id.listMessagesMat);
	ConnexionService.configure(ProviderManager.getInstance());
	connection = ConnexionService.con;
	setConnection(connection);
	ConnexionService.matcher = false;
	
	Thread t = new Thread(new Runnable() {

		@Override
		public void run() {
			Log.d("COMPARE", "COMPARE"+WebServiceInter.requetteExpectMatch(ConnexionService.countryMatcher.get(0)
					+ ConnexionService.countryMatcher.get(1)));
			runOnUiThread(new Runnable()
	        {
	            public void run()
	            {
	            	 dialog = ProgressDialog.show(MatcherActivity.this,
					 "Finding a Match", "Please wait...", false);
	            }
	        });
			if(WebServiceInter.requetteExpectMatch(ConnexionService.countryMatcher.get(0)
					+ ConnexionService.countryMatcher.get(1))=="")
			{
				WebServiceInter.createMatch(ConnexionService.con.getUser(),ConnexionService.countryMatcher.get(0)
						+ ConnexionService.countryMatcher.get(1));
				Log.d("reciepient","reicepient:::::"+recipient+"/////");
				while(recipient==""){
					try{
					
					recipient=WebServiceInter.requetteExpectAnswer(ConnexionService.countryMatcher.get(0)
							+ ConnexionService.countryMatcher.get(1),ConnexionService.con.getUser());
					Log.d("reciepientWhile","reicepientWhile:::::"+recipient+"/////");
					
					}
					catch(Exception e){
						Log.d("ERREURWHile","ERREURWHile"+e);
					}
					
				}
				runOnUiThread(new Runnable()
		        {
		            public void run()
		            {
				
		            
		            dialog.dismiss();}
		            
		        });
				WebServiceInter.deleteMatch(ConnexionService.con.getUser());
				WebServiceInter.deleteAnswer(recipient);
			
			}
			else{
				recipient = WebServiceInter.requetteExpectMatch(ConnexionService.countryMatcher.get(0)
						+ ConnexionService.countryMatcher.get(1));
				WebServiceInter.createAnswer(recipient,ConnexionService.con.getUser(),ConnexionService.countryMatcher.get(0)
						+ ConnexionService.countryMatcher.get(1));
				runOnUiThread(new Runnable()
		        {
		            public void run()
		            {
				
		            dialog.dismiss();
				
		            }
		        });
			}
		}
	});
	t.start();
	setListAdapter();
	
	
	// Set a listener to send a chat text message
	Button send = (Button) findViewById(R.id.sendBtnMat);
	send.setOnClickListener(new View.OnClickListener() {
		public void onClick(View view) {
			String to = recipient;
			text = textMessage.getText().toString();

			Log.i("XMPPChatDemoActivity", "Sending text " + text + " to "
					+ to);
			Message msg = new Message(to, Message.Type.chat);
			msg.setBody(text);
			if (connection != null) {
				connection.sendPacket(msg);
				messages.add(connection.getUser() + ":");
				messages.add(text);
				setListAdapter();
			}
		}
	});
	//Set Listener to add friend button
	quitRequest.setOnClickListener(new View.OnClickListener() {
		public void onClick(View view) {
			String to = recipient;
			Log.d("DEBUG1","DEBUG1");
			text = textMessage.getText().toString();
			Log.d("DEBUG2","DEBUG2");
			Log.i("XMPPChatDemoActivity", "Sending text " + text + " to "
					+ to);
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					String to = recipient;
					text = textMessage.getText().toString();
					if(WebServiceInter.requetteRequest(ConnexionService.con.getUser().toString(),recipient)==""){
						Message msg = new Message(to, Message.Type.chat);
						msg.setBody(ConnexionService.con.getUser().toString()+"has quit the conversation and asked to be friend!");
						if (connection != null) {
							connection.sendPacket(msg);
						}
						toastText=" A request has been created for ";
						
						WebServiceInter.createRequest(recipient, ConnexionService.con.getUser().toString());
					}
					else{
						toastText=" has been added to your friends :D";
						WebServiceInter.deleteRequest(ConnexionService.con.getUser().toString(),recipient);
					}
				}
			});
			
			
			Toast.makeText(getApplicationContext(), recipient+toastText,
					   3000).show();
			messages.add(connection.getUser() + ":");
			messages.add(text);
			setListAdapter();
			Intent returnToMenu = new Intent(MatcherActivity.this, MainActivity.class);
			startActivity(returnToMenu);
		}
	});
	///////
	//Set Listener to add friend button
		quitReject.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String to = recipient;
				String text = textMessage.getText().toString();

				Log.i("XMPPChatDemoActivity", "Sending text " + text + " to "
						+ to);
					Message msg = new Message(to, Message.Type.chat);
					msg.setBody(ConnexionService.con.getUser().toString()+"has quit the conversation without asking to be friend!");
					if (connection != null) {
						connection.sendPacket(msg);
						messages.add(connection.getUser() + ":");
						messages.add(text);
						setListAdapter();
					}
					WebServiceInter.deleteRequest(ConnexionService.con.getUser().toString(),recipient);
					Intent returnToMenu = new Intent(MatcherActivity.this, MainActivity.class);
					startActivity(returnToMenu);
			}
		});
	
}

public void setConnection(XMPPConnection connection) {
	this.connection = connection;
	if (connection != null) {
		// Add a packet listener to get messages sent to us
		PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		connection.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				if (message.getBody() != null) {
					String fromName = StringUtils.parseBareAddress(message
							.getFrom());
					Log.i("XMPPChatDemoActivity", "Text Recieved "
							+ message.getBody() + " from " + fromName);
					messages.add(fromName + ":");
					messages.add(message.getBody());
					// Add the incoming message to the list view
					mHandler.post(new Runnable() {
						public void run() {
							setListAdapter();
						}
					});
				}
			}
		}, filter);
	}
}

private void setListAdapter() {
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			R.layout.chatlayout, messages);
	listview.setAdapter(adapter);
}
}

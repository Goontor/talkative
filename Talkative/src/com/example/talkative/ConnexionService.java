package com.example.talkative;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ConnexionService extends Service {

	IBinder mBinder = new LocalBinder();
	public static   AccountManager conAcc;
	private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/register.php";
	public static final String HOST = "188.226.205.160";
	public static final int PORT = 5222;
	public static final String SERVICE = "talkative.com";
	final ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT,SERVICE);
	public static XMPPConnection con ;
	private ArrayList<String> messages = new ArrayList<String>();
	private Handler mHandler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public ConnexionService getConnexionInstance() {
			return ConnexionService.this;
		}
	}
	
	
	public void deleteAccount(){
	try{
		conAcc.deleteAccount();
	}
	catch(XMPPException e){
		Log.d("Erreur de delete", ""+e);
	}
	}
	
	
	public void setConnection(XMPPConnection connection) {
		this.con = connection;
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
						Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
								+ " from " + fromName );
						messages.add(fromName + ":");
						messages.add(message.getBody());
						// Add the incoming message to the list view
						mHandler.post(new Runnable() {
							public void run() {
								// do what you should do if you recieve a message
							}
						});
					}
				}
			}, filter);
		}
	}
	
	
	public void connect(final String uname, final String pwd) {
		Log.d("toto", "good0");
		if(conAcc==null){
			Log.d("toto", "good1");
			
			Log.d("toto", "good3");
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
			        try{
			        	con = new XMPPConnection (connConfig);
				        con.connect();
				        Log.d("toto", "good4");
				        con.login(uname, pwd);
				        Log.d("toto", "good5");
				        conAcc = new AccountManager(con);
				        Presence presence = new Presence(Presence.Type.available);
						con.sendPacket(presence);
						setConnection(con);

						Roster roster = con.getRoster();
						Collection<RosterEntry> entries = roster.getEntries();
			        }
			        catch(XMPPException e){
			        	Log.d("connexion error", " is "+e);
			        }
				}
			});
			t.start();
			try
			{ 
				t.join(); 
			}
			catch(InterruptedException e)
			{
				
			} 
		}
		else{
			Log.d("toto", "totalFail");
			Toast.makeText(this, "Already Connected Refresh",3000).show();
    	}
	}

}

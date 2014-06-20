package com.example.talkative;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

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
	public static  ArrayList<HashMap<String, String>> usersList=new ArrayList<HashMap<String, String>>();
	final ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT,SERVICE);
	public static XMPPConnection con ;
	public static Roster roster;
	public static ChatManager CMan;
	public static Boolean matcher;
	public static List<String> countryMatcher;
	public static String selectedFriend;
	public static String connexionResult;
	public static OfflineMessageManager OFMMan;
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
		configure(ProviderManager.getInstance());
		matcher = false;
		if(conAcc==null){
			Log.d("toto", "good1");
			
			Log.d("toto", "good3");
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
			        try{
			        	connexionResult="";
			        	connConfig.setDebuggerEnabled(true);
			        	con = new XMPPConnection (connConfig);
				        con.connect();
				        Log.d("toto", "good4");
				        con.login(uname, pwd);
				        OFMMan = new OfflineMessageManager(con);
				        CMan = con.getChatManager();
				        Presence presence = new Presence(Presence.Type.available);
						con.sendPacket(presence);
						setConnection(con);
				        Log.d("toto", "good5");
				        conAcc = new AccountManager(con);
				        roster  = con.getRoster();
				        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
				        roster.addRosterListener(new RosterListener() {
				            // Ignored events public void entriesAdded(Collection<String> addresses) {}
				            public void entriesDeleted(Collection<String> addresses) {
				            }
				            public void entriesUpdated(Collection<String> addresses) {
				            }
				            public void presenceChanged(Presence presence) {
				                Log.d("Presence changed: " + presence.getFrom() + " " + presence,"");
				            }
							@Override
							public void entriesAdded(Collection<String> arg0) {
								
							}
				        });
				        Collection<RosterEntry> entries = roster.getEntries();

			            for (RosterEntry entry : entries) {

			                    HashMap<String, String> map = new HashMap<String, String>();
			                    Presence entryPresence = roster.getPresence(entry.getUser());

			                    Presence.Type type = entryPresence.getType();       
			                    try{
			                    	map.put("USER", entry.getName().toString());
			                    	Log.e("USER", entry.getName().toString());
			                    }
			                    catch(Exception e){
			                    	map.put("USER", "default");
			                    	Log.e("USER", "default");
			                    }
			                    map.put("STATUS", type.toString());
			                    

			                    usersList.add(map);

			            }
						con.sendPacket(presence);
						setConnection(con);
						
			        }
			        catch(XMPPException e){
			        	connexionResult="Failed";
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
	
	public static void configure(ProviderManager pm) {
		//SmackConfiguration.setPacketReplyTimeout(4000);
	//  Private Data Storage
	pm.addIQProvider("query","jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());

	//  Time
	try {
	    pm.addIQProvider("query","jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
	} catch (ClassNotFoundException e) {
	    Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
	}

	//  Roster Exchange
	pm.addExtensionProvider("x","jabber:x:roster", new RosterExchangeProvider());

	//  Message Events
	pm.addExtensionProvider("x","jabber:x:event", new MessageEventProvider());
	//  Chat State
	pm.addExtensionProvider("active","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
	pm.addExtensionProvider("composing","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider()); 
	pm.addExtensionProvider("paused","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
	pm.addExtensionProvider("inactive","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
	pm.addExtensionProvider("gone","http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());

	//  XHTML
	pm.addExtensionProvider("html","http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());

	//  Group Chat Invitations
	pm.addExtensionProvider("x","jabber:x:conference", new GroupChatInvitation.Provider());

	//  Service Discovery # Items    
	pm.addIQProvider("query","http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());

	//  Service Discovery # Info
	pm.addIQProvider("query","http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

	//  Data Forms
	pm.addExtensionProvider("x","jabber:x:data", new DataFormProvider());

	//  MUC User
	pm.addExtensionProvider("x","http://jabber.org/protocol/muc#user", new MUCUserProvider());

	//  MUC Admin    
	pm.addIQProvider("query","http://jabber.org/protocol/muc#admin", new MUCAdminProvider());

	//  MUC Owner    
	pm.addIQProvider("query","http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());

	//  Delayed Delivery
	pm.addExtensionProvider("x","jabber:x:delay", new DelayInformationProvider());

	//  Version
	try {
	    pm.addIQProvider("query","jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
	} catch (ClassNotFoundException e) {
	    //  Not sure what's happening here.
	}

	//  VCard
	pm.addIQProvider("vCard","vcard-temp", new VCardProvider());

	//  Offline Message Requests
	pm.addIQProvider("offline","http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());

	//  Offline Message Indicator
	pm.addExtensionProvider("offline","http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());

	//  Last Activity
	pm.addIQProvider("query","jabber:iq:last", new LastActivity.Provider());

	//  User Search
	pm.addIQProvider("query","jabber:iq:search", new UserSearch.Provider());

	//  SharedGroupsInfo
	pm.addIQProvider("sharedgroup","http://www.jivesoftware.org/protocol/sharedgroup", new SharedGroupsInfo.Provider());

	//  JEP-33: Extended Stanza Addressing
	pm.addExtensionProvider("addresses","http://jabber.org/protocol/address", new MultipleAddressesProvider());

	//   FileTransfer
	pm.addIQProvider("si","http://jabber.org/protocol/si", new StreamInitiationProvider());

	pm.addIQProvider("query","http://jabber.org/protocol/bytestreams", new BytestreamsProvider());

	//  Privacy
	pm.addIQProvider("query","jabber:iq:privacy", new PrivacyProvider());
	pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
	pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.MalformedActionError());
	pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadLocaleError());
	pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadPayloadError());
	pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadSessionIDError());
	pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.SessionExpiredError());
	}

}
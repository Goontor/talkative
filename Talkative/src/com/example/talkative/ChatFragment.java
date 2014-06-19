package com.example.talkative;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.packet.VCard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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

public class ChatFragment extends Fragment {

	public ChatFragment() {
	}

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
	private TextView headerRecipent;
	private EditText textMessage;
	private ListView listview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.conversation_layout,
				container, false);

		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SmackAndroid.init(getActivity());
		recipient="";
		textMessage = (EditText) getView().findViewById(R.id.chatET);
		listview = (ListView) getView().findViewById(R.id.listMessages);
		ConnexionService.configure(ProviderManager.getInstance());
		connection = ConnexionService.con;
		setConnection(connection);
		recipient = ConnexionService.selectedFriend + "@talkative";
		
		setListAdapter();
		// Set a listener to send a chat text message
		Button send = (Button) getView().findViewById(R.id.sendBtn);
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String to = recipient;
				String text = textMessage.getText().toString();

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

	/*private class Matcher extends AsyncTask<String, Void, MultiUserChat> {

		 final ProgressDialog dialog = ProgressDialog.show(getActivity(),
		 "Finding a Match", "Please wait...", false);

		protected void onPreExecute() {

		}

		protected MultiUserChat doInBackground(String... table) {

			return muc;
		}

		protected void onPostExecute(MultiUserChat result) {
			Collection<Occupant> mycoll = null;
			try {
				mycoll = result.getParticipants();
			} catch (XMPPException e) {
			}
			for (Occupant occup : mycoll) {
				if (occup.getJid() != ConnexionService.con.getUser()) {
					recipient = occup.getJid();
				}
			}
			// dialog.dismiss();

		}

	}*/

	private void setListAdapter() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.chatlayout, messages);
		listview.setAdapter(adapter);
	}

	
	
	
}

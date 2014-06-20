package com.example.talkative;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendFragment extends Fragment {
	
	public FriendFragment(){}
	public ListView friendList;
	public Collection<RosterEntry> rostEventList;
	public List<VCard> userList;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
           
    		Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_layout, container, false);
        return rootView;
    }
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		friendList = (ListView) getView().findViewById(R.id.friendLV);
		
		friendList.setOnItemClickListener(new OnItemClickListener()
				   {
			
		      public void onItemClick(AdapterView<?> adapter, View v, int position,
		            long arg3) 
		      {
		    	  
		    	  TextView fullNameF = (TextView) v.findViewById(R.id.friendName);
		    	  String fullNameS = fullNameF.getText().toString();
		    	  Log.d("LastName", "LastName : "+fullNameS.split(" ")[1]);
		    	  String lastName = fullNameS.split(" ")[1];
		    	  ConnexionService.selectedFriend = lastName;
		    	  stackAFragment();
		      }
		   });
		
		
		
		userList = new ArrayList<VCard>();
		rostEventList = ConnexionService.roster.getEntries();
		for(RosterEntry myent : rostEventList){
			Log.d("ajout amis except","leRoster"+myent.getUser());
			ConnexionService.configure(ProviderManager.getInstance());
			VCard thisUserVCard = new VCard();
			try{
				Log.d("VCard", "VCardpre"+thisUserVCard);
			thisUserVCard.load(ConnexionService.con, myent.getUser());
				Log.d("VCard", "VCardafter"+thisUserVCard);
			}
			catch(XMPPException e){
				Log.d("VCardPROB", "VCardPROB"+e);
			}
			Log.d("VCardList","VCardListLoop"+userList);
			userList.add(thisUserVCard);
		}
		Log.d("VCardList","VCardList"+userList);
		
		final StableArrayAdapter myAdapter = new StableArrayAdapter(getActivity(),userList);
		friendList.setAdapter(myAdapter);
		
	}
	
	private class StableArrayAdapter extends ArrayAdapter<VCard> {
		
		private Context context;
		private List<VCard> mylist;
		
		public StableArrayAdapter(Context context, List<VCard> mylist) {
			super(context, R.layout.firnd_list_view, mylist);
			this.context = context;
			this.mylist = mylist;
		}
		
		public class ViewHolder {
			public TextView friendName;
			public ImageView friendStatus;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("DeBuG","DeBuG1");
			View vi = convertView;
				Log.d("DeBuG","DeBuG2");
			if (vi == null) {
					Log.d("DeBuG","DeBuG3");
				final ViewHolder holder = new ViewHolder();
					Log.d("DeBuG","DeBuG4");
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					Log.d("DeBuG","DeBuG5");
				vi = inflater.inflate(R.layout.firnd_list_view, null);
					Log.d("DeBuG9","DeBuG6");
				holder.friendName = (TextView) vi.findViewById(R.id.friendName);
					Log.d("DeBuG","DeBuG7");
				//holder.friendPic = (ImageView) vi.findViewById(R.id.friendPic);
					Log.d("DeBuG","DeBuG8");
				holder.friendStatus = (ImageView) vi.findViewById(R.id.friendStat);
					Log.d("DeBuG","DeBuG9");
				vi.setTag(holder);
					Log.d("DeBuG","DeBuG10");
			}
				Log.d("DeBuG","DeBuG11");
			ViewHolder holder = (ViewHolder) vi.getTag();
				Log.d("DeBuG","DeBuG12");
			holder.friendName.setText(mylist.get(position).getFirstName()+" "+mylist.get(position).getLastName());
				Log.d("DeBuG","DeBuG13");
			VCard thisUserVCard = new VCard();
				Log.d("DeBuG","DeBuG14");
			try{
					Log.d("DeBuG","DeBuG16");
				//holder.friendPic.setImageBitmap(ProfileFragment.bytesToBitmap(thisUserVCard.getAvatar()));
					Log.d("DeBuG","DeBuG17"+ConnexionService.roster.getPresence(mylist.get(position).getFirstName()+"@talkative"));
				if(ConnexionService.roster.getPresence(mylist.get(position).getFirstName()+"@talkative").isAvailable()){
					Log.d("DeBuG","DeBuG18");
					holder.friendStatus.setImageDrawable(getResources().getDrawable(R.drawable.online));
				}
				else{
					Log.d("DeBuG","DeBuG19");
					holder.friendStatus.setImageDrawable(getResources().getDrawable(R.drawable.online));
				}
			}
			
			catch(Exception e2){
				Log.d("eee", "detupo"+e2);
			}
			Log.d("DeBuG","DeBuG20");
			return vi;
		}
	}
	
	private void stackAFragment() {
        Fragment f;
        f = new ChatFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, f);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
	
}

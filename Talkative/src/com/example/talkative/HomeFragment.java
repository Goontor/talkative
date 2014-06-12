package com.example.talkative;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
	TextView Grettings;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.chat_matcher, container, false);
         
        return rootView;
    }
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Grettings = (TextView) getView().findViewById(R.id.grettingCM);
			if(ConnexionService.conAcc!=null){
				Grettings.setText(ConnexionService.conAcc.getAccountAttribute("name"));
			}
		
	}
	
	
}

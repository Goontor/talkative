package com.example.talkative;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;

import com.example.talkative.FilePickerActivity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	EditText lastName;
	EditText firstName;
	private File selectedFile;
	private TextView mFilePathTextView;
	EditText phoneNumber;
	ImageButton profilPic;
	Spinner knownLang;
	VCard mVCard;
	Button saveButton;
	Button deleteButton;
	Spinner talkingLang;
	private static final int REQUEST_PICK_FILE = 1;
	private static final int RESULT_OK = -1;
	public ProfileFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.profil, container, false);
         
        return rootView;
    }
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Loading the VCard
		
		ConnexionService.configure(ProviderManager.getInstance());
		mVCard =new VCard();
		Log.d("Card Loading","CONACC"+ConnexionService.conAcc);
		try{
			
			mVCard.load(ConnexionService.con);
			Log.d("Card Loading","By the try"+mVCard);
			
		}
		catch(Exception e){
			Log.d("Card Loading","By the catch: "+ConnexionService.conAcc.getAccountAttribute("username")+"   ERROR:   "+e);
			
			mVCard.setField("knownLanguage", "French");
			mVCard.setField("talkingLanguage", "English");
			try{
				mVCard.save(ConnexionService.con);
			}
			catch(XMPPException e2){
				Log.d("New card failed","Error: "+e2);
			}
		}
		
		lastName = (EditText) getView().findViewById(R.id.lastNamePro);
		firstName = (EditText) getView().findViewById(R.id.firstNamePro);
		phoneNumber = (EditText) getView().findViewById(R.id.phonePro);	
		knownLang = (Spinner) getView().findViewById(R.id.knownLanguage);
		talkingLang = (Spinner) getView().findViewById(R.id.talkingLanguage);
		lastName.setText(ConnexionService.conAcc.getAccountAttribute("name"));
		profilPic = (ImageButton) getView().findViewById(R.id.profilePicPro);
		saveButton = (Button) getView().findViewById(R.id.submitPro);
		deleteButton = (Button) getView().findViewById(R.id.deletePro);
		
		
		//Pick image ImageButton onclicklist
		profilPic.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent browsePic = new Intent(getActivity(),FilePickerActivity.class);
				startActivityForResult(browsePic, REQUEST_PICK_FILE);
			}
		});
		
		
		
		
		
		///////Pick image ImageButton onclicklist (END)
		
		//Save Button onclicklist
		
		saveButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mVCard.setFirstName(firstName.getText().toString());
				mVCard.setPhoneHome("Voice", phoneNumber.getText().toString());
				mVCard.setLastName(lastName.getText().toString());
				profilPic.buildDrawingCache();
				Bitmap myImage = profilPic.getDrawingCache();
				int bytes = myImage.getByteCount();
                ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                myImage.copyPixelsToBuffer(buffer);
                byte[] array = buffer.array(); 
				mVCard.setAvatar(array);
				mVCard.setField("knownLanguage",knownLang.getSelectedItem().toString());
				mVCard.setField("talkingLanguage", talkingLang.getSelectedItem().toString());
				try{
					Log.d("Save ok","Save ok"+mVCard);
					mVCard.save(ConnexionService.con);
				}
				catch(XMPPException e){
					Log.d("Save error",""+e);
				}
				getView().invalidate();
			}
		});
		
		///////Save Button onclicklist (END)
		
		//Delete Button onclicklist
		
		//TODO
				
		///////Delete Button onclicklist (END)
		
		//Setting fields value
		
		if(mVCard.getFirstName()==null){
			firstName.setText("Still secret");
		}
		else{
			firstName.setText(mVCard.getFirstName());
		}
		if(mVCard.getPhoneHome("VOICE")==null){
			phoneNumber.setText("Still secret");
		}
		else{
			phoneNumber.setText(mVCard.getPhoneHome("VOICE"));
		}
		
		///////Setting fields value (END)
		
		//Setting Spinner Values
		ArrayAdapter talkingAdap = (ArrayAdapter) talkingLang.getAdapter();
		Log.d("talkingValeur", ""+mVCard.getField("talkingLanguage"));
		Integer talkingPos = talkingAdap.getPosition(mVCard.getField("talkingLanguage"));
		Log.d("takingPos",""+talkingPos);
		talkingLang.setSelection(talkingPos);
		ArrayAdapter knownAdap = (ArrayAdapter) knownLang.getAdapter();
		Log.d("knownValeur", ""+mVCard.getField("knownLanguage"));
		Integer knownPos = talkingAdap.getPosition(mVCard.getField("knownLanguage"));
		Log.d("knownPos", ""+knownPos);
		knownLang.setSelection(knownPos);
		///////Setting Spinner Values (END)
		//Setting Image
		if(mVCard.getAvatar()!=null){
		Log.d("NoProblem", "Get avatar notnull"+mVCard.getAvatar());
		profilPic.setImageBitmap(bytesToBitmap(mVCard.getAvatar()));
		}
		else{
			Log.d("Problem", "Get avatar null");
		}
		///////Setting Image(END)
		
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("GAGNé", "IL APPEL!!!!");
		if(resultCode == RESULT_OK) {
			Log.d("GAGNé", "IL APPEL!!!!3");
            switch(requestCode) {
            case REQUEST_PICK_FILE:
            	Log.d("GAGNé", "IL APPEL!!!!4");
                if(data.hasExtra(FilePickerActivity.EXTRA_FILE_PATH)) {
                	Log.d("GAGNé", "IL APPEL!!!!5");
                    // Get the file path
                    selectedFile = new File(data.getStringExtra(FilePickerActivity.EXTRA_FILE_PATH));
                    // Set the file path text view
                    Bitmap myImage = BitmapFactory.decodeFile(selectedFile.getPath());
                    //Now I have my selected file, You can do your additional requirement with file.
                    profilPic.setImageBitmap(myImage);
                    profilPic.setBackgroundResource(0);
                    int bytes = myImage.getByteCount();
                    ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                    myImage.copyPixelsToBuffer(buffer);
                    byte[] array = buffer.array(); 
                    /*mVCard.setAvatar(array);
                    try{
                    	mVCard.save(ConnexionService.con);
                    	Log.d("GAGNé", "IL APPEL!!!!15");
                    }
                    catch(XMPPException e){
                    	Log.d("GAGNé", "IL APPEL!!!!16  "+e);
                    	
                    }*/
                }
            }
        }
    }
	
	public static Bitmap bytesToBitmap(byte[] bytes)
	{
	    ByteArrayInputStream imageStream = null;

	    try
	    {
	    	
	        imageStream = new ByteArrayInputStream(bytes);
	        return BitmapFactory.decodeStream(imageStream);
	    }
	    catch (Exception ex)
	    {
	        Log.d("My Activity", "Unable to generate a bitmap: " + ex.getMessage());
	        return null;
	    }
	    finally
	    {
	        if (imageStream != null)
	        {
	            try
	            {
	                imageStream.close();
	            }
	            catch (Exception ex) {}
	        }
	    }
	}
	
}

package com.example.talkative;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebServiceInter {
//////HTTPREQUEST/////////
	
	
	
	
	//////ASKING FOR A MATCH////////
	
	
	public static String requetteExpectMatch(String expectation){
	String result = "";
	String result2 = "";
	InputStream is=null;
	String line=null;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("expectation",expectation));
	 
	// Envoi de la requête avec HTTPPost
	try{
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost("http://188.226.205.160/expectationReader.php");
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent();
	}catch(Exception e){
	        Log.e("log_tag", "Error in http connection "+e.toString());
	}
	//
	 try
   {
    	BufferedReader reader = new BufferedReader
				(new InputStreamReader(is,"iso-8859-1"),8);
       	StringBuilder sb = new StringBuilder();
       	while ((line = reader.readLine()) != null)
		{
  		    sb.append(line + "\n");
      	}
       	is.close();
       	result = sb.toString();
	        Log.e("pass 2", "connection success "+result);
	}
   catch(Exception e)
	{
		Log.e("Fail 2", e.toString());
	}     
	// Parsing des données JSON
	try{
			Log.d("DEBUGREQ7", "DEBUGREQ8");
	        JSONArray jArray = new JSONArray(result);
	        Log.d("DEBUGREQ7", "DEBUGREQ9"+result);
	        for(int i=0;i<jArray.length();i++){
	                JSONObject json_data = jArray.getJSONObject(i);
	                Log.i("log_tag","Jid: "+json_data.get("jid").toString()+
	                        ", expectation: "+json_data.get("expectation").toString()
	                );
	                result2 = json_data.get("jid").toString();
	        }
	}
	catch(JSONException e){
	        Log.e("log_tag", "Error parsing data :::::"+result2+"/////"+e.toString());
	}
	
	return result2;	
	}

	//////////(ASKING FOR MATCH END)
	
	//////////(ASKING FOR ANSWER)
	
	public static String requetteExpectAnswer(String expectation,String recieverjid){
		String result = "";
		String result2 = "";
		InputStream is=null;
		String line=null;
		// L'année à envoyer
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("expectation",expectation));
		nameValuePairs.add(new BasicNameValuePair("recieverjid",recieverjid));
		nameValuePairs.add(new BasicNameValuePair("Type","Answer"));
		 
		// Envoi de la requête avec HTTPPost
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://188.226.205.160/answerReader.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//
		 try
	     {
	      	BufferedReader reader = new BufferedReader
					(new InputStreamReader(is,"iso-8859-1"),8);
	         	StringBuilder sb = new StringBuilder();
	         	while ((line = reader.readLine()) != null)
			{
	    		    sb.append(line + "\n");
	        	}
	         	is.close();
	         	result = sb.toString();
		        Log.e("pass 2", "connection success "+result);
		}
	     catch(Exception e)
	 	{
			Log.e("Fail 2", e.toString());
		}     
		// Parsing des données JSON
		try{
				Log.d("DEBUGREQ7", "DEBUGREQ8");
		        JSONArray jArray = new JSONArray(result);
		        Log.d("DEBUGREQ7", "DEBUGREQ9"+result);
		        for(int i=0;i<jArray.length();i++){
		                JSONObject json_data = jArray.getJSONObject(i);
		                Log.i("log_tag","Jid: "+json_data.get("senderjid").toString()+
		                        ", expectation: "+json_data.get("expectation").toString()
		                );
		                result2 = json_data.get("senderjid").toString();
		        }
		}
		catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		
		return result2;	}
	
//////////ASKING FOR ANSWER(END)
	
//////////CREATE ANSWER	
	
	public static void createAnswer(String recieverjid,String senderjid,String expectation){
		String result="";
		InputStream is=null;
		String line=null;
		int code;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("recieverjid",recieverjid));
		nameValuePairs.add(new BasicNameValuePair("senderjid",senderjid));
		nameValuePairs.add(new BasicNameValuePair("expectation",expectation));
		nameValuePairs.add(new BasicNameValuePair("Type","Answer"));
		 
		// Envoi de la requête avec HTTPPost
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://188.226.205.160/answerCreator.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//
		 try
	     {
	      	BufferedReader reader = new BufferedReader
					(new InputStreamReader(is,"iso-8859-1"),8);
	         	StringBuilder sb = new StringBuilder();
	         	while ((line = reader.readLine()) != null)
			{
	    		    sb.append(line + "\n");
	        	}
	         	is.close();
	         	result = sb.toString();
		        Log.e("pass 2", "connection success "+result);
		}
	     catch(Exception e)
	 	{
			Log.e("Fail 2", e.toString());
		}     
		// Parsing des données JSON
		 try
			{
		            JSONObject json_data = new JSONObject(result);
		            code=(json_data.getInt("code"));
					
		            if(code==1)
		            {
		            	Log.e("INSERT", "INSERT SUCCEED");
		            }
		            else
		            {
		            	Log.e("INSERT", "INSERT FAILED");
		            }
			}
			catch(Exception e)
			{
		            Log.e("Fail 3", e.toString());
			}
	}
//////////CREATE ANSWER(END)
	
//////////CREATE MATCH
	public static void createMatch(String jid,String expectation){
		String result="";
		InputStream is=null;
		String line=null;
		int code;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("jid",jid));
		nameValuePairs.add(new BasicNameValuePair("expectation",expectation));
		nameValuePairs.add(new BasicNameValuePair("Type","Match"));
		 
		// Envoi de la requête avec HTTPPost
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://188.226.205.160/answerCreator.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//
		 try
	     {
	      	BufferedReader reader = new BufferedReader
					(new InputStreamReader(is,"iso-8859-1"),8);
	         	StringBuilder sb = new StringBuilder();
	         	while ((line = reader.readLine()) != null)
			{
	    		    sb.append(line + "\n");
	        	}
	         	is.close();
	         	result = sb.toString();
		        Log.e("pass 2", "connection success "+result);
		}
	     catch(Exception e)
	 	{
			Log.e("Fail 2", e.toString());
		}     
		// Parsing des données JSON
		 try
			{
		            JSONObject json_data = new JSONObject(result);
		            code=(json_data.getInt("code"));
					
		            if(code==1)
		            {
		            	Log.e("INSERT", "INSERT SUCCEED");
		            }
		            else
		            {
		            	Log.e("INSERT", "INSERT FAILED");
		            }
			}
			catch(Exception e)
			{
		            Log.e("Fail 3", e.toString());
			}
	}
	
///////CREAT MATCH(END)
	
///////DELETE MATCH
	public static void deleteMatch(String deleteid){
		String result="";
		InputStream is=null;
		String line=null;
		int code;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("deleteid",deleteid));
		 
		// Envoi de la requête avec HTTPPost
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://188.226.205.160/matchDelete.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}
		catch(Exception e){
			
		}
	}
//////DELETE MATCH (END)
	
//////DELETE ANSWER
	public static void deleteAnswer(String deleteid){
		String result="";
		InputStream is=null;
		String line=null;
		int code;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("deleteid",deleteid));
		 
		// Envoi de la requête avec HTTPPost
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://188.226.205.160/answerDelete.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}
		catch(Exception e){
			
		}
	}
////////FIND REQUEST
	public static String requetteRequest(String recieverjid,String senderjid){
		String result = "";
		String result2 = "";
		InputStream is=null;
		String line=null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("recieverjid",recieverjid));
		nameValuePairs.add(new BasicNameValuePair("senderjid",senderjid));
		 
		// Envoi de la requête avec HTTPPost
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://188.226.205.160/requestFinder.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//
		 try
	   {
	    	BufferedReader reader = new BufferedReader
					(new InputStreamReader(is,"iso-8859-1"),8);
	       	StringBuilder sb = new StringBuilder();
	       	while ((line = reader.readLine()) != null)
			{
	  		    sb.append(line + "\n");
	      	}
	       	is.close();
	       	result = sb.toString();
		        Log.e("pass 2", "connection success "+result);
		}
	   catch(Exception e)
		{
			Log.e("Fail 2", e.toString());
		}     
		// Parsing des données JSON
		try{
				Log.d("DEBUGREQ7", "DEBUGREQ8");
		        JSONArray jArray = new JSONArray(result);
		        Log.d("DEBUGREQ7", "DEBUGREQ9"+result);
		        for(int i=0;i<jArray.length();i++){
		                JSONObject json_data = jArray.getJSONObject(i);
		                Log.i("log_tag","Jid: "+json_data.get("senderjid").toString()+
		                        ", expectation: "+json_data.get("recieverjid").toString()
		                );
		                result2 = json_data.get("recieverjid").toString();
		        }
		}
		catch(JSONException e){
		        Log.e("log_tag", "Error parsing data :::::"+result2+"/////"+e.toString());
		}
		
		return result2;	
		}
////////FIND REQUEST(END)
////////CREATE REQUEST
	public static void createRequest(String recieverjid,String senderjid){
		String result="";
		InputStream is=null;
		String line=null;
		int code;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("recieverjid",recieverjid));
		nameValuePairs.add(new BasicNameValuePair("senderjid",senderjid));
		 
		// Envoi de la requête avec HTTPPost
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://188.226.205.160/requestCreator.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//
		 try
	     {
	      	BufferedReader reader = new BufferedReader
					(new InputStreamReader(is,"iso-8859-1"),8);
	         	StringBuilder sb = new StringBuilder();
	         	while ((line = reader.readLine()) != null)
			{
	    		    sb.append(line + "\n");
	        	}
	         	is.close();
	         	result = sb.toString();
		        Log.e("pass 2", "connection success "+result);
		}
	     catch(Exception e)
	 	{
			Log.e("Fail 2", e.toString());
		}     
		// Parsing des données JSON
		 try
			{
		            JSONObject json_data = new JSONObject(result);
		            code=(json_data.getInt("code"));
					
		            if(code==1)
		            {
		            	Log.e("INSERT", "INSERT SUCCEED");
		            }
		            else
		            {
		            	Log.e("INSERT", "INSERT FAILED");
		            }
			}
			catch(Exception e)
			{
		            Log.e("Fail 3", e.toString());
			}
	}
////////CREATE REQUEST(END)
///////DELETE MATCH
	public static void deleteRequest(String recieverjid,String senderjid){
		String result="";
		InputStream is=null;
		String line=null;
		int code;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("senderjid",senderjid));
		nameValuePairs.add(new BasicNameValuePair("recieverjid",recieverjid));
		 
		// Envoi de la requête avec HTTPPost
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://188.226.205.160/requestDelete.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}
		catch(Exception e){
			
		}
	}
//////DELETE MATCH (END)
}

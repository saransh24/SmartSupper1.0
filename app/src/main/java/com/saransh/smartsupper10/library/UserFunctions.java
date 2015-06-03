package com.saransh.smartsupper10.library;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class UserFunctions {

	private JSONParser jsonParser;
    
    /*private static String loginURL = "http://10.0.2.2/vnb/android/user_activity.php";
    private static String registerURL = "http://10.0.2.2/vnb/android/user_activity.php";
    private static String noticesURL = "http://10.0.2.2/vnb/android/get_data.php";*/
    private static String insertURL = "http://10.0.2.2/smartsupper/insert_gcm.php";
    private static String loginURL = "http://dcetech.com/sagnik/vnb/android/user_activity.php";
    private static String registerURL = "http://dcetech.com/sagnik/vnb/android/user_activity.php";
    private static String noticesURL = "http://dcetech.com/sagnik/vnb/android/get_data.php";
     
    private static String login_tag = "login";
    private static String register_tag = "register";
	
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

    public void insertGCM(String regId){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gcm_id", regId));

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(insertURL);
        if(params != null)
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        try {
            httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	public JSONObject loginUser(String email, String password){
        
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        
        return json;
    }
	
	public JSONObject registerUser(String name, String email, String password, String regId, String year){
        
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("regId", regId));
        params.add(new BasicNameValuePair("year", year));
         
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }
	
	public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            return true;
        }
        return false;
    }
	

}

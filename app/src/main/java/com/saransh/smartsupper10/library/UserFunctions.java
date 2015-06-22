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
    private static String foodDetailsURL = "http://ec2-52-26-3-72.us-west-2.compute.amazonaws.com/smartsupper/food_detail.php";
    private static String registerURL = "http://ec2-52-26-3-72.us-west-2.compute.amazonaws.com/smartsupper/register.php";
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

	public String registerUser(String name, String contact, String address){

		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Name", name));
        params.add(new BasicNameValuePair("Contact", contact));
        params.add(new BasicNameValuePair("Address", address));
        String json = String.valueOf(jsonParser.getJSONFromUrl(registerURL, params));
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
    public JSONObject getfoodDetails(Context context) {

        JSONObject json = jsonParser.getJSONFromUrl(foodDetailsURL, null);
        return json;
    }
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTable_Login();
        return true;
    }


}

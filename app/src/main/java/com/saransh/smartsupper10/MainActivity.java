package com.saransh.smartsupper10;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.saransh.smartsupper10.library.config;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    String msg="";
    //GoogleCloudMessaging gcm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regId = gcm.register(config.GOOGLE_PROJECT_ID);
            Log.d("RegisterActivity", "registerInBackground - regId: "
                    + regId);
            msg = "Device registered, registration ID=" + regId;

            //storeRegistrationId(context, regId);
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            Log.d("RegisterActivity", "Error: " + msg);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

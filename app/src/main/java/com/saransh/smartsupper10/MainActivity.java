package com.saransh.smartsupper10;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.saransh.smartsupper10.library.UserFunctions;
import com.saransh.smartsupper10.library.config;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    String msg="";
    public final static String EXTRA_MESSAGE = "com.saransh.myapplication.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AttemptRegister().execute();
    }
    class AttemptRegister extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                regId = gcm.register(config.GOOGLE_PROJECT_ID);
                Log.d("RegisterActivity", "registerInBackground - regId: "
                        + regId);
                msg = "Device registered, registration ID=" + regId;
                new UserFunctions().insertGCM(regId);
                return regId;
                //storeRegistrationId(context, regId);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                Log.d("RegisterActivity", "Error: " + msg);
            }

            return regId;

        }

        @Override
        protected void onPostExecute(String params) {
            Toast.makeText(getApplicationContext(),
                    params,
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void b_add1(View view)
    {
        TextView textView = (TextView)findViewById(R.id.count_dish1);
        int n = Integer.parseInt(String.valueOf(textView.getText()));
        textView.setText(String.valueOf(n+1));
    }
    public void b_add2(View view)
    {
        TextView textView = (TextView)findViewById(R.id.count_dish2);
        int n = Integer.parseInt(String.valueOf(textView.getText()));
        textView.setText(String.valueOf(n+1));
    }
    public void b_sub1(View view)
    {
        TextView textView = (TextView)findViewById(R.id.count_dish1);
        int n = Integer.parseInt(String.valueOf(textView.getText()));
        if(n>0)
        textView.setText(String.valueOf(n-1));
    }
    public void b_sub2(View view)
    {
        TextView textView = (TextView)findViewById(R.id.count_dish2);
        int n = Integer.parseInt(String.valueOf(textView.getText()));
        if(n>0)
        textView.setText(String.valueOf(n-1));
    }
    public void b_checkout(View view)
    {
        TextView textView = (TextView)findViewById(R.id.count_dish1);
        int n1 = Integer.parseInt(String.valueOf(textView.getText()));
        textView = (TextView)findViewById(R.id.count_dish2);
        int n2 = Integer.parseInt(String.valueOf(textView.getText()));
        textView = (TextView)findViewById(R.id.rate_dish1);
        int rate1 = Integer.parseInt(String.valueOf(textView.getText()));
        textView = (TextView)findViewById(R.id.rate_dish2);
        int rate2 = Integer.parseInt(String.valueOf(textView.getText()));
        int cost = n1*rate1 + n2*rate2;
        Intent intent =new Intent(getApplicationContext(),checkout.class);
        intent.putExtra(EXTRA_MESSAGE,String.valueOf(cost));
        startActivity(intent);
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

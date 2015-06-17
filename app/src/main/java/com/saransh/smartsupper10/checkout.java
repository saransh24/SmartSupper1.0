package com.saransh.smartsupper10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.saransh.smartsupper10.library.DatabaseHandler;
import com.saransh.smartsupper10.library.UserFunctions;

import java.util.HashMap;


public class checkout extends Activity {

    TextView name;
    TextView contact;
    TextView address;

    Context context;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Intent intent = getIntent();
        intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView)findViewById(R.id.total);
        textView.setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));
        name = (TextView) findViewById(R.id.Name);
        contact = (TextView) findViewById(R.id.contactno);
        address = (TextView) findViewById(R.id.address);
        db = new DatabaseHandler(getApplicationContext());
        int count = db.getRowCount();

        if(count > 0) {
            HashMap<String, String> details= db.getUserDetails();
            name.setText(details.get("name"));
            contact.setText(details.get("contact"));
            address.setText(details.get("address"));
        }
        db.close();
    }

    public void b_order(View view)
    {
        int count = db.getRowCount();
        if(count>0)
        {
            db.resetTable_Register();
        }
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        if(ni == null) {

            Toast.makeText(getApplicationContext(), "Can't Connect to the Internet", Toast.LENGTH_LONG).show();

        } else {
            new AttemptRegister().execute();
        }
        context = getApplicationContext();
        Intent intent = new Intent(context,Thankyou.class);
        startActivity(intent);

    }
    class AttemptRegister extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {

            String Name = name.getText().toString();
            String Contact = contact.getText().toString();
            String Address = address.getText().toString();

            UserFunctions userFunction = new UserFunctions();

            String msg = "";

            String k = userFunction.registerUser(Name,Contact ,Address);
            Log.d("Pagal",k);
            try {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                userFunction.logoutUser(getApplicationContext());
                db.addUser(Name,Contact,Address);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_chechkout, menu);
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

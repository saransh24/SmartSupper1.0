package com.saransh.smartsupper10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.saransh.smartsupper10.library.DatabaseHandler;
import com.saransh.smartsupper10.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class checkout extends Activity {

    TextView name = (TextView) findViewById(R.id.name);
    TextView contact = (TextView) findViewById(R.id.contactno);
    TextView address = (TextView) findViewById(R.id.address);

    Context context;
    String regId;
    public static final String REG_ID = "regId";
    //private static final String APP_VERSION = "appVersion";

    static final String TAG = "Register Activity";

    private static String KEY_SUCCESS = "success";
    private static String KEY_NAME = "name";
    private static String KEY_Contact = "email";
    private static String KEY_Address = "year";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Intent intent = getIntent();
        intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView)findViewById(R.id.total);
        textView.setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));
        name = (TextView) findViewById(R.id.name);
        contact = (TextView) findViewById(R.id.contactno);
        address = (TextView) findViewById(R.id.address);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
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
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
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

    }
    class AttemptRegister extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {

            String Name = name.getText().toString();
            String Contact = contact.getText().toString();
            String Address = address.getText().toString();

            UserFunctions userFunction = new UserFunctions();

            String msg = "";
            context = getApplicationContext();
            JSONObject json = userFunction.registerUser(Name,Contact ,Address);

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    //registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        userFunction.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_Contact), json_user.getString(KEY_Address));
                        db.close();
                        Intent intent = new Intent(context,Thankyou.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //registerErrorMsg.setText("Error Occurred During Registration");
                    }
                }
            } catch (JSONException e) {
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

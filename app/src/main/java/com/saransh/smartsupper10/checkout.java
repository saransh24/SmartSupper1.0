package com.saransh.smartsupper10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.saransh.smartsupper10.library.DatabaseHandler;

import java.util.HashMap;


public class checkout extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Intent intent = getIntent();
        intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView)findViewById(R.id.total);
        textView.setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));
        TextView t1 = (TextView) findViewById(R.id.contactno);
        TextView t2 = (TextView) findViewById(R.id.address);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        int count = db.getRowCount();

        if(count > 0) {

            HashMap<String, String> details= db.getUserDetails();
            t1.setText(details.get("contact"));
            t2.setText(details.get("address"));
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
        TextView t1 = (TextView) findViewById(R.id.contactno);
        TextView t2 = (TextView) findViewById(R.id.address);
        String contact = String.valueOf(t1.getText());
        String address = String.valueOf(t2.getText());
        db.addUser(contact,address);
        Intent intent = new Intent(this,Thankyou.class);
        startActivity(intent);
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

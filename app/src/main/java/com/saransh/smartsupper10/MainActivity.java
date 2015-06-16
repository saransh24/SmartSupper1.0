package com.saransh.smartsupper10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.saransh.smartsupper10.library.UserFunctions;
import com.saransh.smartsupper10.library.config;

import java.io.IOException;


public class MainActivity extends Activity {
    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    String msg="";
    public final static String EXTRA_MESSAGE = "com.saransh.myapplication.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


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
        TextView textView1 = (TextView)findViewById(R.id.count_dish1);
        TextView textView2 = (TextView)findViewById(R.id.rate_dish1);
        TextView textView3 = (TextView)findViewById(R.id.total);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.total_panel);
        int visiblity = linearLayout.getVisibility();

        if(visiblity==View.GONE)
        {
            linearLayout.setVisibility(View.VISIBLE);
        }

        int n = Integer.parseInt(String.valueOf(textView1.getText()));
        int rate = Integer.parseInt(String.valueOf(textView2.getText()));
        int total = rate+Integer.parseInt(String.valueOf(textView3.getText()));

        textView1.setText(String.valueOf(n+1));
        textView3.setText(String.valueOf(total));

    }
    public void b_add2(View view)
    {

        TextView textView1 = (TextView)findViewById(R.id.count_dish2);
        TextView textView2 = (TextView)findViewById(R.id.rate_dish2);
        TextView textView3 = (TextView)findViewById(R.id.total);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.total_panel);
        int visiblity = linearLayout.getVisibility();

        if(visiblity==View.GONE)
        {
            linearLayout.setVisibility(View.VISIBLE);
        }
        int n = Integer.parseInt(String.valueOf(textView1.getText()));
        int rate = Integer.parseInt(String.valueOf(textView2.getText()));
        int total = rate+Integer.parseInt(String.valueOf(textView3.getText()));

        textView1.setText(String.valueOf(n+1));
        textView3.setText(String.valueOf(total));
    }
    public void b_sub1(View view)
    {
        TextView textView1 = (TextView)findViewById(R.id.count_dish1);
        TextView textView2 = (TextView)findViewById(R.id.rate_dish1);
        TextView textView3 = (TextView)findViewById(R.id.total);

        int rate = Integer.parseInt(String.valueOf(textView2.getText()));
        int total = Integer.parseInt(String.valueOf(textView3.getText()));

        int n = Integer.parseInt(String.valueOf(textView1.getText()));
        if(n>0) {
            textView1.setText(String.valueOf(n - 1));
            if (total > 0)
                textView3.setText(String.valueOf(total - rate));
        }

    }
    public void b_sub2(View view)
    {

        TextView textView1 = (TextView)findViewById(R.id.count_dish2);
        TextView textView2 = (TextView)findViewById(R.id.rate_dish2);
        TextView textView3 = (TextView)findViewById(R.id.total);

        int rate = Integer.parseInt(String.valueOf(textView2.getText()));
        int total = Integer.parseInt(String.valueOf(textView3.getText()));

        int n = Integer.parseInt(String.valueOf(textView1.getText()));
        if(n>0) {
            textView1.setText(String.valueOf(n - 1));
            if(total>0)
                textView3.setText(String.valueOf(total-rate));
        }

    }
    public void b_order(View view)
    {
        TextView textView3 = (TextView)findViewById(R.id.total);
        int total = Integer.parseInt(String.valueOf(textView3.getText()));
        Intent intent =new Intent(getApplicationContext(),checkout.class);
        intent.putExtra(EXTRA_MESSAGE,String.valueOf(total));
        startActivity(intent);
    }
    public void picClick1(View view)
    {
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        final ImageView image = (ImageView)findViewById(R.id.pic_dish1);
        TextView textView = (TextView)findViewById(R.id.food_desc1);

        int visiblity = textView.getVisibility();
        if(visiblity == view.VISIBLE) {
            textView.setVisibility(View.GONE);
            image.startAnimation(animationFadeOut);
            animationFadeOut.setFillAfter(true);

        }
        else if(visiblity == view.GONE) {
            textView.setVisibility(View.VISIBLE);
            image.startAnimation(animationFadeIn);
            animationFadeIn.setFillAfter(true);
        }
    }
    public void picClick2(View view)
    { final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        final ImageView image = (ImageView)findViewById(R.id.pic_dish2);
        TextView textView = (TextView)findViewById(R.id.food_desc2);
        int visiblity = textView.getVisibility();
        if(visiblity == view.VISIBLE) {
            textView.setVisibility(View.GONE);
            image.startAnimation(animationFadeOut);
            animationFadeOut.setFillAfter(true);
        }
        else if(visiblity == view.GONE) {
            textView.setVisibility(View.VISIBLE);
            image.startAnimation(animationFadeIn);
            animationFadeIn.setFillAfter(true);
        }
    }
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

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

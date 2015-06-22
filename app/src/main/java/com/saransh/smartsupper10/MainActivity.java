package com.saransh.smartsupper10;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.saransh.smartsupper10.library.UserFunctions;
import com.saransh.smartsupper10.library.config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {
    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    String msg="";

    String [][]foodDetails;

    TextView rate1;
    TextView rate2;

    TextView count_dish1;
    TextView count_dish2;
    TextView total;

    TextView food_desc1;
    TextView food_desc2;

    TextView foodName1;
    TextView foodName2;

    public final static String EXTRA_MESSAGE = "com.saransh.myapplication.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        food_desc1 = (TextView)findViewById(R.id.food_desc1);
        food_desc2 = (TextView)findViewById(R.id.food_desc2);

        rate1 = (TextView)findViewById(R.id.rate_dish1);
        rate2 = (TextView)findViewById(R.id.rate_dish2);

        foodName1 = (TextView)findViewById(R.id.name_dish1);
        foodName2 = (TextView)findViewById(R.id.name_dish2);

        count_dish1 = (TextView)findViewById(R.id.count_dish1);
        count_dish2 = (TextView)findViewById(R.id.count_dish2);
        total = (TextView)findViewById(R.id.total);

        foodDetails = new String[2][4];

        new SetData().execute();
        new AttemptRegister().execute();

    }
    class SetData extends AsyncTask<String, String, String> {

        JSONObject jsonObject;
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this,R.style.ActivityDialog);
            pDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            pDialog.setMessage("Loading Engine ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            UserFunctions user = new UserFunctions();
            jsonObject = user.getfoodDetails(getApplicationContext());

            return null;
        }

        @Override
        protected void onPostExecute(String params) {

            pDialog.dismiss();

            try {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                JSONObject jsonObject1;

                for(int i=0;i<2;i++)
                {
                    jsonObject1 = jsonArray.getJSONObject(i);
                    //Log.e("jsonMessage", jsonArray.getJSONObject(i).getString("foodName"));
                    foodDetails[i][0] = jsonObject1.getString("foodName");
                    foodDetails[i][1] = jsonObject1.getString("picLink");
                    foodDetails[i][2] = jsonObject1.getString("Rate");
                    foodDetails[i][3] = jsonObject1.getString("foodDesc");
                }
                Log.e("try", foodDetails[0][0]);
            }
            catch (Exception e)
            {
                Log.e("catch", e.toString());
                e.printStackTrace();
            }

            food_desc1.setText(foodDetails[0][3]);
            food_desc2.setText(foodDetails[1][3]);

            rate1.setText(foodDetails[0][2]);
            rate2.setText(foodDetails[1][2]);

            foodName1.setText(foodDetails[0][0]);
            foodName2.setText(foodDetails[1][0]);


            new DownloadImageTask((ImageView) findViewById(R.id.pic_dish1))
                    .execute(foodDetails[0][1]);
            new DownloadImageTask((ImageView) findViewById(R.id.pic_dish2))
                    .execute(foodDetails[1][1]);
            //Toast.makeText(getApplicationContext(),
                    //params,
                    //Toast.LENGTH_SHORT).show();
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        ProgressDialog pDialog;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this,R.style.ActivityDialog);
            pDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            pDialog.setTitle("SmartSupper");
            pDialog.setMessage("Locating Tastiest Dishes Around You ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            pDialog.dismiss();
            bmImage.setImageBitmap(result);

        }
    }
    class AttemptRegister extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this,R.style.ActivityDialog);
            pDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            pDialog.setTitle("SmartSupper");
            pDialog.setMessage("Finishing Up ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


        }

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
            pDialog.dismiss();
            /*Toast.makeText(getApplicationContext(),
                    params,
                    Toast.LENGTH_SHORT).show();*/
        }
    }  
    public void b_add1(View view)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.total_panel);

        int visiblity = linearLayout.getVisibility();

        if(visiblity==View.GONE)
        {
            linearLayout.setVisibility(View.VISIBLE);
        }
        int n = Integer.parseInt(String.valueOf(count_dish1.getText()));
        int rate = Integer.parseInt(String.valueOf(rate1.getText()));
        int Total;
        Total = rate+Integer.parseInt(String.valueOf(total.getText()));
        count_dish1.setText(String.valueOf(n+1));
        total.setText(String.valueOf(Total));

    }
    public void b_add2(View view)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.total_panel);

        int visiblity = linearLayout.getVisibility();

        if(visiblity==View.GONE)
        {
            linearLayout.setVisibility(View.VISIBLE);
        }
        int n = Integer.parseInt(String.valueOf(count_dish2.getText()));
        int rate = Integer.parseInt(String.valueOf(rate2.getText()));
        int Total;
        Total = rate+Integer.parseInt(String.valueOf(total.getText()));
        count_dish2.setText(String.valueOf(n+1));
        total.setText(String.valueOf(Total));
    }
    public void b_sub1(View view)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.total_panel);


        int rate = Integer.parseInt(String.valueOf(rate1.getText()));
        int Total = Integer.parseInt(String.valueOf(total.getText()));
        int n = Integer.parseInt(String.valueOf(count_dish1.getText()));
        if(n>0) {
            count_dish1.setText(String.valueOf(n - 1));
            if (Total > 0) {
                Total=Total-rate;
                total.setText(String.valueOf(Total));
            }
        }

        if(Total<=0)
        {
            linearLayout.setVisibility(View.GONE);
        }

    }
    public void b_sub2(View view)
    {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.total_panel);


        int rate = Integer.parseInt(String.valueOf(rate2.getText()));
        int Total = Integer.parseInt(String.valueOf(total.getText()));
        int n = Integer.parseInt(String.valueOf(count_dish2.getText()));
        if(n>0) {
            count_dish2.setText(String.valueOf(n - 1));
            if (Total > 0)
            {
                Total=Total-rate;
                total.setText(String.valueOf(Total));
            }

        }
        if(Total<=0)
        {
            linearLayout.setVisibility(View.GONE);
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
            image.startAnimation(animationFadeIn);
            animationFadeIn.setFillAfter(true);

        }
        else if(visiblity == view.GONE) {
            textView.setVisibility(View.VISIBLE);
            image.startAnimation(animationFadeOut);
            animationFadeOut.setFillAfter(true);
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
            image.startAnimation(animationFadeIn);
            animationFadeOut.setFillAfter(true);
        }
        else if(visiblity == view.GONE) {
            textView.setVisibility(View.VISIBLE);
            image.startAnimation(animationFadeOut);
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

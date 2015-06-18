package com.saransh.smartsupper10;

import android.app.Activity;
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
import android.widget.Toast;

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
    TextView food_desc1;
    TextView food_desc2;

    TextView rate1;
    TextView rate2;

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
        new SetData().execute();
        new AttemptRegister().execute();

    }
    class SetData extends AsyncTask<String, String, String> {

        JSONObject jsonObject;
        String [][]foodDetails;
        @Override
        protected String doInBackground(String... params) {

            UserFunctions user = new UserFunctions();
            jsonObject = user.getfoodDetails(getApplicationContext());

            return null;
        }

        @Override
        protected void onPostExecute(String params) {
            foodDetails=getdata(jsonObject);
            food_desc1.setText(foodDetails[0][3]);
            food_desc2.setText(foodDetails[1][3]);

            rate1.setText(foodDetails[0][2]);
            rate2.setText(foodDetails[1][2]);

            foodName1.setText(foodDetails[0][0]);
            foodName2.setText(foodDetails[1][0]);


            new DownloadImageTask((ImageView) findViewById(R.id.pic_dish1))
                    .execute(foodDetails[0][0]);
            new DownloadImageTask((ImageView) findViewById(R.id.pic_dish1))
                    .execute(foodDetails[1][1]);
            Toast.makeText(getApplicationContext(),
                    params,
                    Toast.LENGTH_SHORT).show();
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
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
    String [][] getdata(JSONObject jsonObject)
    {
        try {
            String Data[][] = new String[2][4];
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=0;i<2;i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Data[i][0] = jsonObject1.getString("foodName");
                Data[i][1] = jsonObject1.getString("picLink'");
                Data[i][2] = jsonObject1.getString("Rate");
                Data[i][3] = jsonObject1.getString("foodDesc");
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
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

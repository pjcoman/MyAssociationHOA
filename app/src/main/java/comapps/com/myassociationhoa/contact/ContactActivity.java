package comapps.com.myassociationhoa.contact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by me on 6/22/2016.
 */
public class ContactActivity extends AppCompatActivity {

    public static final String TAG = "CONTACTACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    private String phone_number_b1;
    private String phone_number_b2;
    private String phone_number_b3;
    private String phone_number_b4;
    private String phone_number_b5;
    private String phone_number_b6;
    private String phone_number_b7;
    private String phone_number_b8;

    final String phone_number_EwingServices = "tel:9725232357";

    GestureDetectorCompat mDetector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWindowAnimations();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_contacts);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {


            bar.setTitle("Contacts");

        }

        SharedPreferences sharedpreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        String assocNameAddress = sharedpreferences.getString("defaultRecord(1)", "");
        String assocNameAddress2 = sharedpreferences.getString("defaultRecord(18)", "");
        String assocNameAddress3 = sharedpreferences.getString("defaultRecord(19)", "");

        Button b1 = (Button) findViewById(R.id.button);
        Button b2 = (Button) findViewById(R.id.button2);
        Button b3 = (Button) findViewById(R.id.button3);
        Button b4 = (Button) findViewById(R.id.button4);
        Button b5 = (Button) findViewById(R.id.button5);
        Button b6 = (Button) findViewById(R.id.button6);
        Button b7 = (Button) findViewById(R.id.button7);
        Button b8 = (Button) findViewById(R.id.button8);

        b1.setText(abbreviateText(sharedpreferences.getString("defaultRecord(6)", "").toLowerCase()));
        b2.setText(abbreviateText(sharedpreferences.getString("defaultRecord(7)", "").toLowerCase()));
        b3.setText(abbreviateText(sharedpreferences.getString("defaultRecord(8)", "").toLowerCase()));
        b4.setText(abbreviateText(sharedpreferences.getString("defaultRecord(9)", "").toLowerCase()));
        b5.setText(abbreviateText(sharedpreferences.getString("defaultRecord(10)", "").toLowerCase()));
        b6.setText(abbreviateText(sharedpreferences.getString("defaultRecord(11)", "").toLowerCase()));
        b7.setText(sharedpreferences.getString("defaultRecord(26)", ""));
        b8.setText(sharedpreferences.getString("defaultRecord(28)", ""));

        TextView assocationNameAddressTV = (TextView) findViewById(R.id.textViewAssociationName);
        TextView assocationNameAddressTV2 = (TextView) findViewById(R.id.textViewAssociationName2);
        TextView assocationNameAddressTV3 = (TextView) findViewById(R.id.textViewAssociationName3);
        assocationNameAddressTV.setText(assocNameAddress);
        assocationNameAddressTV2.setText(assocNameAddress2);
        assocationNameAddressTV3.setText(assocNameAddress3);


        phone_number_b1 = "tel:" + sharedpreferences.getString("defaultRecord(12)", "");
        phone_number_b2 = "tel:" + sharedpreferences.getString("defaultRecord(13)", "");
        phone_number_b3 = "tel:" + sharedpreferences.getString("defaultRecord(14)", "");
        phone_number_b4 = "tel:" + sharedpreferences.getString("defaultRecord(15)", "");
        phone_number_b5 = "tel:" + sharedpreferences.getString("defaultRecord(16)", "");
        phone_number_b6 = "tel:" + sharedpreferences.getString("defaultRecord(17)", "");
        phone_number_b7 = "tel:" + sharedpreferences.getString("defaultRecord(27)", "");
        phone_number_b8 = "tel:" + sharedpreferences.getString("defaultRecord(29)", "");




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
            Guide();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Guide() {

        Intent loadGuide = new Intent();
        loadGuide.setClass(this, GuideActivity.class);
        startActivity(loadGuide);
        //     overridePendingTransition(R.anim.fadeinanimationgallery,R.anim.fadeoutanimationgallery);


    }

    public void button1Dial() {

        // TODO Auto-generated method stub

        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phone_number_b1));
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
        }


    }

    public void button2Dial() {

        // TODO Auto-generated method stub

        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phone_number_b2));
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
        }


    }

    public void button3Dial() {

        // TODO Auto-generated method stub

        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phone_number_b3));
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
        }


    }

    public void button4Dial() {

        // TODO Auto-generated method stub

        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phone_number_b4));
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
        }


    }

    public void button5Dial() {

        // TODO Auto-generated method stub

        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phone_number_b5));
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
        }


    }

    public void button6Dial() {

        // TODO Auto-generated method stub

        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phone_number_b6));
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
        }


    }

    public void button7Dial() {

        // TODO Auto-generated method stub

        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phone_number_b7));
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
        }


    }

    public void button8Dial() {

        // TODO Auto-generated method stub

        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phone_number_b8));
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
        }


    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));



        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.RIGHT);
        getWindow().setEnterTransition(slideTransition);


        Slide slideTransitionExit = new Slide();
        slideTransitionExit.setSlideEdge(Gravity.RIGHT);
        getWindow().setExitTransition(slideTransitionExit);



    }


    public String abbreviateText(String buttonText) {

        switch (buttonText) {
            case "administration":
                buttonText = "ADMIN";
                break;
            case "fire department":
                buttonText = "FIRE";
                break;
            case "police department":
                buttonText = "POLICE";
                break;
            case "maintenance":
                buttonText = "MAINT.";
                break;
            case "association":
                buttonText = "ASSOC.";
                break;
        }


        return buttonText;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }





    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }




}



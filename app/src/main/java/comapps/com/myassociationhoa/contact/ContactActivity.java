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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
@SuppressWarnings("ALL")
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

    private String b1Text;
    private String b2Text;
    private String b3Text;
    private String b4Text;
    private String b5Text;
    private String b6Text;
    private String b7Text;
    private String b8Text;

    final String phone_number_EwingServices = "tel:9725232357";

    private GestureDetectorCompat mDetector;


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

        b1Text = sharedpreferences.getString("defaultRecord(6)", "");
        b1Text = b1Text.toLowerCase().replace("administration", "admin.");
        b1Text = b1Text.toLowerCase().replace("association", "assoc.");
        b1Text = b1Text.toLowerCase().replace("department", "dept.");
        b2Text = sharedpreferences.getString("defaultRecord(7)", "");
        b2Text = b2Text.toLowerCase().replace("administration", "admin.");
        b2Text = b2Text.toLowerCase().replace("association", "assoc.");
        b2Text = b2Text.toLowerCase().replace("department", "dept.");
        b3Text = sharedpreferences.getString("defaultRecord(8)", "");
        b3Text = b3Text.toLowerCase().replace("administration", "admin.");
        b3Text = b3Text.toLowerCase().replace("association", "assoc.");
        b3Text = b3Text.toLowerCase().replace("department", "dept.");
        b4Text = sharedpreferences.getString("defaultRecord(9)", "");
        b4Text = b4Text.toLowerCase().replace("administration", "admin.");
        b4Text = b4Text.toLowerCase().replace("association", "assoc.");
        b4Text = b4Text.toLowerCase().replace("department", "dept.");
        b5Text = sharedpreferences.getString("defaultRecord(10)", "");
        b5Text = b5Text.toLowerCase().replace("administration", "admin.");
        b5Text = b5Text.toLowerCase().replace("association", "assoc.");
        b5Text = b5Text.toLowerCase().replace("department", "dept.");
        b6Text = sharedpreferences.getString("defaultRecord(11)", "");
        b6Text = b6Text.toLowerCase().replace("administration", "admin.");
        b6Text = b6Text.toLowerCase().replace("association", "assoc.");
        b6Text = b6Text.toLowerCase().replace("department", "dept.");
        b7Text = sharedpreferences.getString("defaultRecord(26)", "");
        b7Text = b7Text.toLowerCase().replace("administration", "admin.");
        b7Text = b7Text.toLowerCase().replace("association", "assoc.");
        b7Text = b7Text.toLowerCase().replace("department", "dept.");
        b8Text = sharedpreferences.getString("defaultRecord(28)", "");
        b8Text = b8Text.toLowerCase().replace("administration", "admin.");
        b8Text = b8Text.toLowerCase().replace("association", "assoc.");
        b8Text = b8Text.toLowerCase().replace("department", "dept.");

        Log.d(TAG, "contact name b1 " + sharedpreferences.getString("defaultRecord(6)", "null"));
        Log.d(TAG, "contact name b2 " + sharedpreferences.getString("defaultRecord(7)", "null"));
        Log.d(TAG, "contact name b3 " + sharedpreferences.getString("defaultRecord(8)", "null"));
        Log.d(TAG, "contact name b4 " + sharedpreferences.getString("defaultRecord(9)", "null"));
        Log.d(TAG, "contact name b5 " + sharedpreferences.getString("defaultRecord(10)", "null"));
        Log.d(TAG, "contact name b6 " + sharedpreferences.getString("defaultRecord(11)", "null"));
        Log.d(TAG, "contact name b7 " + sharedpreferences.getString("defaultRecord(26)", "null"));
        Log.d(TAG, "contact name b8 " + sharedpreferences.getString("defaultRecord(28)", "null"));

        b1.setText(b1Text);
        b2.setText(b2Text);
        b3.setText(b3Text);
        b4.setText(b4Text);
        b5.setText(b5Text);
        b6.setText(b6Text);
        b7.setText(b7Text);
        b8.setText(b8Text);

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

        if ( phone_number_b1.length() == 0 ) {
            b1.setEnabled(false);
            b1.getBackground().setAlpha(125);
        }
        if ( phone_number_b2.length() == 0 ) {
            b2.setEnabled(false);
            b2.getBackground().setAlpha(125);
        }
        if ( phone_number_b3.length() == 0 ) {
            b3.setEnabled(false);
            b3.getBackground().setAlpha(125);
        }
        if ( phone_number_b4.length() == 0 ) {
            b4.setEnabled(false);
            b4.getBackground().setAlpha(125);
        }
        if ( phone_number_b5.length() == 0 ) {
            b5.setEnabled(false);
            b5.getBackground().setAlpha(125);
        }
        if ( phone_number_b6.length() == 0 ) {
            b6.setEnabled(false);
            b6.getBackground().setAlpha(125);
        }
        if ( phone_number_b7.length() == 0 ) {
            b7.setEnabled(false);
            b7.getBackground().setAlpha(125);
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

    public void button1Dial(View v) {

        // TODO Auto-generated method stub

        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phone_number_b1));
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
        }


    }

    public void button2Dial(View v) {

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

    public void button3Dial(View v) {

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

    public void button4Dial(View v) {

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

    public void button5Dial(View v) {

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

    public void button6Dial(View v) {

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

    public void button7Dial(View v) {

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

    public void button8Dial(View v) {

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


    private String abbreviateText(String buttonText) {

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



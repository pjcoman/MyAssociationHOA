package comapps.com.myassociationhoa.contact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        b1.setText(sharedpreferences.getString("defaultRecord(6)", ""));
        b2.setText(sharedpreferences.getString("defaultRecord(7)", ""));
        b3.setText(sharedpreferences.getString("defaultRecord(8)", ""));
        b4.setText(sharedpreferences.getString("defaultRecord(9)", ""));
        b5.setText(sharedpreferences.getString("defaultRecord(10)", ""));
        b6.setText(sharedpreferences.getString("defaultRecord(11)", ""));
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

    public void button1Dial(View view) {

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

    public void button2Dial(View view) {

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

    public void button3Dial(View view) {

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

    public void button4Dial(View view) {

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

    public void button5Dial(View view) {

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

    public void button6Dial(View view) {

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

    public void button7Dial(View view) {

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

    public void button8Dial(View view) {

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

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }







}



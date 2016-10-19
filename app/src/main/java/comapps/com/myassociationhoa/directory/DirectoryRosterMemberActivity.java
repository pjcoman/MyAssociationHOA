package comapps.com.myassociationhoa.directory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.ParseInstallation;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.autos.AutosActivity;
import comapps.com.myassociationhoa.guests.GuestsActivity;
import comapps.com.myassociationhoa.objects.RosterObject;
import comapps.com.myassociationhoa.pets.PetsActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class DirectoryRosterMemberActivity extends AppCompatActivity {

    private static final String TAG = "DIRECTORYROSTERMEMBERACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    private ScrollView memberInfo;

    private TextView textViewLastName;
    private TextView textViewFirstName;
    private TextView textViewMiddleName;
    private TextView textViewAddress1;
    private TextView textViewAddress2;
    private TextView textViewCity;
    private TextView textViewState;
    private TextView textViewZip;
    private TextView textViewHomePhone;
    private TextView textViewMobilePhone;
    private TextView textViewEmail;
    private TextView textViewEmergencyLabel;
    private TextView textViewEmergencyContact;
    private TextView textViewEmergencyContactPhone;
    private TextView textViewWinterAddress;
    private TextView textViewWinterAddress2;
    private TextView textViewWinterCity;
    private TextView textViewWinterState;
    private TextView textViewWinterZip;
    private TextView textViewWinterPhone;
    private TextView textViewWinterEmail;
    private TextView mobileLabel;

    private Button backButton;

    private ImageButton buttonCall;
    private ImageButton buttonCallMobile;
    private ImageButton buttonEmail;
    private ImageButton buttonEmailWinter;
    private ImageButton buttonCallEmergency;
    private ImageButton buttonCallWinter;

    private Button petsButton;
    private Button autosButton;
    private Button guestsButton;
    private Button otherButton;
    private LinearLayout otherInfoLayout;
    private LinearLayout other1InfoLayout;
    private LinearLayout other2InfoLayout;

    private TextView summerAddress;
    private TextView winterAddress;

    private ParseInstallation installation;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_directory_full_listing);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Directory");
        }

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        otherInfoLayout = (LinearLayout) findViewById(R.id.otherInfoLayout);
        other1InfoLayout = (LinearLayout) findViewById(R.id.other);
        other2InfoLayout = (LinearLayout) findViewById(R.id.other2);
        petsButton = (Button) findViewById(R.id.petsButton);
        autosButton = (Button) findViewById(R.id.autosButton);
        guestsButton = (Button) findViewById(R.id.guestsButton);
        otherButton = (Button) findViewById(R.id.otherButton);



        installation = ParseInstallation.getCurrentInstallation();

        if ( installation.getString("MemberType").equals("Member")) {
            if (guestsButton != null) {
                guestsButton.setVisibility(View.GONE);
            } else {
                assert guestsButton != null;
                guestsButton.setVisibility(View.VISIBLE);
            }
        }

        if (sharedPreferences.getString("defaultRecord(35)", "No").equals("No")) {
            if (petsButton != null) {
                petsButton.setVisibility(View.GONE);
            } else {
                assert petsButton != null;
                petsButton.setVisibility(View.VISIBLE);
            }
        }

        if (sharedPreferences.getString("defaultRecord(37)", "No").equals("No")) {
            if (autosButton != null) {
                autosButton.setVisibility(View.GONE);
            } else {
                assert autosButton != null;
                autosButton.setVisibility(View.VISIBLE);
            }
        }

        if ( guestsButton.getVisibility() == View.GONE && otherButton.getVisibility() == View.GONE) {
            other2InfoLayout.setVisibility(View.GONE);

        }

        if ( autosButton.getVisibility() == View.GONE && petsButton.getVisibility() == View.GONE) {
            other1InfoLayout.setVisibility(View.GONE);
        }

        if (sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {

          //  otherInfoLayout.setVisibility(View.GONE);

        }

        textViewLastName = (TextView) findViewById(R.id.lastNameTextView);
        textViewFirstName = (TextView) findViewById(R.id.firstNameTextView);
        textViewMiddleName = (TextView) findViewById(R.id.middleNameTextView);
        textViewAddress1 = (TextView) findViewById(R.id.address1TextView);
        textViewAddress2 = (TextView) findViewById(R.id.address2TextView);
        textViewCity = (TextView) findViewById(R.id.cityTextView);
        textViewState = (TextView) findViewById(R.id.stateTextView);
        textViewZip = (TextView) findViewById(R.id.zipTextView);
        textViewHomePhone = (TextView) findViewById(R.id.homePhoneTextView);
        textViewMobilePhone = (TextView) findViewById(R.id.mobilePhoneTextView);
        textViewEmail = (TextView) findViewById(R.id.emailTextView);
        textViewEmergencyLabel = (TextView)  findViewById(R.id.textViewEC);
        textViewEmergencyContact = (TextView) findViewById(R.id.emergencyContactTextView);
        textViewEmergencyContactPhone = (TextView) findViewById(R.id.emergencyContactPhoneTextView);
        textViewWinterAddress = (TextView) findViewById(R.id.winter_address1TextView);
        textViewWinterAddress2 = (TextView) findViewById(R.id.winter_address2TextView);
        textViewWinterCity = (TextView) findViewById(R.id.winter_cityTextView);
        textViewWinterState = (TextView) findViewById(R.id.winter_stateTextView);
        textViewWinterZip = (TextView) findViewById(R.id.winter_zipTextView);
        textViewWinterPhone = (TextView) findViewById(R.id.winter_homePhoneTextView);
        textViewWinterEmail = (TextView) findViewById(R.id.winter_emailTextView);
        summerAddress = (TextView) findViewById(R.id.summerAddressLabel);
        winterAddress = (TextView) findViewById(R.id.winterAddressLabel);
        mobileLabel = (TextView) findViewById(R.id.textViewMobileLabel);



        backButton = (Button) findViewById(R.id.backButton);
        buttonCall = (ImageButton) findViewById(R.id.imageButtonPhone);
        buttonCallMobile = (ImageButton) findViewById(R.id.imageButtonMobile);
        buttonEmail = (ImageButton) findViewById(R.id.imageButtonEmail);
        buttonCallWinter = (ImageButton) findViewById(R.id.imageButtonPhoneWinter);
        buttonCallEmergency = (ImageButton) findViewById(R.id.imageButtonEC);
        buttonEmailWinter = (ImageButton) findViewById(R.id.imageButtonEmailWinter);

        Intent mIntent = getIntent();
        int i = mIntent.getIntExtra("object_number", 0);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        String jsonRosterObject = sharedPreferences.getString("rosterObject" + "[" + i + "]", "");
        Gson gson = new Gson();
        final RosterObject rosterObject = gson.fromJson(jsonRosterObject, RosterObject.class);




        String pn;

        textViewLastName.setText(rosterObject.getLastName());
        textViewFirstName.setText(rosterObject.getFirstName());
        textViewMiddleName.setText(rosterObject.getMiddleName());
        textViewAddress1.setText(rosterObject.getHomeAddress1());
        textViewAddress2.setText(rosterObject.getHomeAddress2());
        textViewCity.setText(rosterObject.getHomeCity());
        textViewState.setText(rosterObject.getHomeState());
        textViewZip.setText(rosterObject.getHomeZip());



        if ( rosterObject.getHomePhone().length() == 10) {
            pn = "(" + rosterObject.getHomePhone().substring(0,3) + ") " + rosterObject.getHomePhone().substring(3,6) + "-" +
                    rosterObject.getHomePhone().substring(6);
            textViewHomePhone.setText(pn);
        } else {
            textViewHomePhone.setText(rosterObject.getHomePhone());
        }

        if ( rosterObject.getMobilePhone().length() == 10) {
            pn = "(" + rosterObject.getMobilePhone().substring(0,3) + ") " + rosterObject.getMobilePhone().substring(3,6) + "-" +
                    rosterObject.getMobilePhone().substring(6);
            textViewMobilePhone.setText(pn);
        } else {
            textViewMobilePhone.setText(rosterObject.getMobilePhone());
        }

        mobileLabel.setText("mobile");




        textViewEmail.setText(rosterObject.getEmail());
        textViewEmergencyContact.setText(rosterObject.getEmergencyName());

        Log.d(TAG, "emer length ----> " + rosterObject.getEmergencyPhoneNumber().length());

        if ( rosterObject.getEmergencyPhoneNumber().length() == 10) {
            pn = "(" + rosterObject.getEmergencyPhoneNumber().substring(0,3) + ") " + rosterObject.getEmergencyPhoneNumber().substring(3,6) + "-" +
                    rosterObject.getEmergencyPhoneNumber().substring(6);
            textViewEmergencyContactPhone.setText(pn);
        } else {
            textViewEmergencyContactPhone.setText(rosterObject.getEmergencyPhoneNumber());
        }

        textViewWinterAddress.setText(rosterObject.getWinterAddress1());
        textViewWinterAddress2.setText(rosterObject.getWinterAddress2());
        textViewWinterCity.setText(rosterObject.getWinterCity());
        textViewWinterState.setText(rosterObject.getWinterState());
        textViewWinterZip.setText(rosterObject.getWinterZip());

        if ( rosterObject.getWinterPhone().length() == 10) {
            pn = "(" + rosterObject.getWinterPhone().substring(0,3) + ") " + rosterObject.getWinterPhone().substring(3,6) + "-" +
                    rosterObject.getWinterPhone().substring(6);
            textViewWinterPhone.setText(pn);
        } else {
            textViewWinterPhone.setText(rosterObject.getWinterPhone());
        }

        textViewWinterEmail.setText(rosterObject.getWinterEmail());

        if (textViewEmergencyContactPhone.getText().length() == 0) {
            textViewEmergencyContact.setVisibility(View.GONE);
            textViewEmergencyContactPhone.setVisibility(View.GONE);
         //   textViewEmergencyLabel.setVisibility(View.GONE);
            buttonCallEmergency.setVisibility(View.GONE);
        }

        if (textViewHomePhone.getText().length() == 0) {
            buttonCall.setVisibility(View.GONE);
            textViewHomePhone.setVisibility(View.GONE);
        }

        if (textViewAddress2.getText().length() == 0) {
            textViewAddress2.setVisibility(View.GONE);

        }

        if (textViewWinterAddress2.getText().length() == 0) {
            textViewWinterAddress2.setVisibility(View.GONE);

        }

        if (textViewMobilePhone.getText().length() == 0) {
            buttonCallMobile.setVisibility(View.GONE);
            textViewMobilePhone.setVisibility(View.GONE);
        }

        if (textViewWinterPhone.getText().length() == 0) {
            buttonCallWinter.setVisibility(View.GONE);
            textViewWinterPhone.setVisibility(View.GONE);
        }

        if (textViewAddress2.getText().length() == 0) {
            textViewAddress2.setVisibility(View.GONE);

        }

        if (textViewEmail.getText().length() == 0) {
            textViewAddress2.setVisibility(View.GONE);
            buttonEmail.setVisibility(View.GONE);
        }

        if (textViewWinterEmail.getText().length() == 0) {
            textViewWinterEmail.setVisibility(View.GONE);
            buttonEmailWinter.setVisibility(View.GONE);
        }

        summerAddress.setText(sharedPreferences.getString("defaultRecord(24)", ""));
        winterAddress.setText(sharedPreferences.getString("defaultRecord(25)", ""));



        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Log.d(TAG, "to dial ----> " + String.valueOf(textViewHomePhone.getText()));

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", textViewHomePhone.getText().toString(), null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        petsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(DirectoryRosterMemberActivity.this, PetsActivity.class);
                intent.putExtra("memberNumber", rosterObject.getMemberNumber());
                intent.putExtra("fromPopInfo", false);
                startActivity(intent);

            }
        });

        guestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(DirectoryRosterMemberActivity.this, GuestsActivity.class);
                intent.putExtra("memberNumber", rosterObject.getMemberNumber());
                intent.putExtra("fromPopInfo", false);
                startActivity(intent);

            }
        });

        autosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(DirectoryRosterMemberActivity.this, AutosActivity.class);
                intent.putExtra("memberNumber", rosterObject.getMemberNumber());
                intent.putExtra("fromPopInfo", false);
                startActivity(intent);

            }
        });

        buttonCallMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "to dial ----> " + String.valueOf(textViewMobilePhone.getText()));
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", textViewMobilePhone.getText().toString(), null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendEmail = new Intent(Intent.ACTION_SEND);
                intentSendEmail.setType("text/plain");
                String[] address = {String.valueOf(textViewEmail.getText())};

                intentSendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, address);

                startActivity(intentSendEmail);

            }
        });

        buttonCallEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "to dial ----> " + String.valueOf(textViewEmergencyContactPhone.getText()));
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", textViewEmergencyContactPhone.getText().toString(), null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        buttonCallWinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, "to dial ----> " + String.valueOf(textViewWinterPhone.getText()));
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", textViewWinterPhone.getText().toString(), null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "No phone service.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        buttonEmailWinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSendEmail = new Intent(Intent.ACTION_SEND);
                intentSendEmail.setType("text/plain");
                String[] address = {String.valueOf(textViewWinterEmail.getText())};

                intentSendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, address);


                startActivity(intentSendEmail);
            }
        });






       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               finish();

           }
       });

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


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {

       /* Intent intentMain = new Intent();
        intentMain.setClass(MBActivity.this, MainActivity.class);
        MBActivity.this.finish();
        startActivity(intentMain);*/

        finish();

    }




}

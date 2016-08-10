package comapps.com.myassociationhoa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "REGISTRATIONACTIVITY";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    SharedPreferences sharedPreferencesVisited;

    Context context;

    EditText firstNameEditText;
    EditText middleNameEditText;
    EditText lastNameEditText;
    EditText address1EditText;
    EditText address2EditText;
    EditText cityEditText;
    EditText stateEditText;
    EditText zipEditText;
    EditText homePhoneEditText;
    EditText mobilePhoneEditText;
    EditText emailEditText;
    EditText winterNameEditText;
    EditText winterAddress1EditText;
    EditText winterAddress2EditText;
    EditText winterCityEditText;
    EditText winterStateEditText;
    EditText winterZipEditText;
    EditText winterPhoneEditText;
    EditText emerContactEditText;
    EditText emerContactPhoneEditText;
    EditText winterEmailEditText;

    TextView summerAddress;
    TextView winterAddress;


    String rosterString;


    Button saveButton;
    String[] rosterFileArray;
    int numberOfMembers;

    private Boolean uniqueMemberNumber = false;

    ProgressDialog progressDialog;

    String numbersFileString;
    String memberNumberRandom;
    String memberName;
    String assocCode;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.register_info_layout);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Registration");
        }



        firstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
        middleNameEditText = (EditText) findViewById(R.id.editTextMiddleName);
        lastNameEditText = (EditText) findViewById(R.id.editTextLastName);
        address1EditText = (EditText) findViewById(R.id.editTextAddress1);
        address2EditText = (EditText) findViewById(R.id.editTextAddress2);
        cityEditText = (EditText) findViewById(R.id.editTextCity);
        stateEditText = (EditText) findViewById(R.id.editTextState);
        zipEditText = (EditText) findViewById(R.id.editTextZip);
        homePhoneEditText = (EditText) findViewById(R.id.editTextPhone);
        mobilePhoneEditText = (EditText) findViewById(R.id.editTextMobilePhone);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        emerContactEditText = (EditText) findViewById(R.id.editTextEmergencyContact);
        emerContactPhoneEditText = (EditText) findViewById(R.id.editTextEmergencyPhone);
        winterNameEditText = (EditText) findViewById(R.id.editTextNameW);
        winterAddress1EditText = (EditText) findViewById(R.id.editTextAddress1W);
        winterAddress2EditText = (EditText) findViewById(R.id.editTextAddress2W);
        winterCityEditText = (EditText) findViewById(R.id.editTextCityW);
        winterStateEditText = (EditText) findViewById(R.id.editTextStateW);
        winterZipEditText = (EditText) findViewById(R.id.editTextZipW);
        winterPhoneEditText = (EditText) findViewById(R.id.editTextPhoneW);
        winterEmailEditText = (EditText) findViewById(R.id.editTextemailW);

        summerAddress = (TextView) findViewById(R.id.summerAddressLabel);
        winterAddress = (TextView) findViewById(R.id.winterAddressLabel);

        saveButton = (Button) findViewById(R.id.buttonSave);

        homePhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mobilePhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        emerContactPhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        winterPhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);

        summerAddress.setText(sharedPreferencesVisited.getString("defaultRecord(24)", "Summer Address"));
        winterAddress.setText(sharedPreferencesVisited.getString("defaultRecord(25)", "Winter Address"));


        final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        memberName = installation.getString("memberName");
        assocCode = installation.getString("AssociationCode");
        Log.d(TAG, "memberName is " + memberName);

        //*************************************************************************************************************************************

        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseInstallation.getCurrentInstallation().getString("AssociationCode"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> assoc, ParseException e) {


                ParseFile rosterFile = assoc.get(0).getParseFile("RosterFile");


                try {
                    byte[] file = rosterFile.getData();
                    try {
                        rosterString = new String(file, "UTF-8");
                        Log.d(TAG, "rosterString is " + rosterString);
                        rosterFileArray = rosterString.split("\\|");
                        numberOfMembers = rosterFileArray.length;
                        Log.d(TAG, "no. of members = " + numberOfMembers);

                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                //****************************************************************************************************************************************************************************************************************


                while (!uniqueMemberNumber) {
                    ParseFile numbersFile = assoc.get(0).getParseFile("NumberFile");
                    String[] numbersFileArray = null;

                    memberNumberRandom = String.valueOf(generateRandom(10));


                    Log.d(TAG, "unique member number is " + memberNumberRandom);

                    try {

                        byte[] file = numbersFile.getData();


                        try {
                            numbersFileString = new String(file, "UTF-8");


                            Log.d(TAG, "numbersFileString is " + numbersFileString);
                            numbersFileArray = numbersFileString.split("\\|");

                            Log.d(TAG, "numbersFileArray length is " + numbersFileArray.length);

                            for (int i = 0; i < numbersFileArray.length; i++) {

                                uniqueMemberNumber = memberNumberRandom != numbersFileArray[i];

                                Log.d(TAG, "numbersFileString member is " + Integer.toString(i) + " " + numbersFileArray[i]);

                            }

                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }


                }

                Log.d(TAG, "unique member number is true/false " + uniqueMemberNumber);
                installation.put("memberNumber", memberNumberRandom);
                installation.saveInBackground();

                numbersFileString = numbersFileString + "|" + memberNumberRandom;

                Log.d(TAG, "numbersFileString is " + numbersFileString);

                byte[] data = numbersFileString.getBytes();
                ParseFile NumberFile = new ParseFile("Number.txt", data);


                try {
                    NumberFile.save();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }


                assoc.get(0).put("NumberFile", NumberFile);
                try {
                    assoc.get(0).save();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }


            }
        });


//********************************************************************************************************************************************************************************************************************


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                installation.put("memberName", firstNameEditText.getText() + " " + lastNameEditText.getText());
                installation.put("memberDeviceName", getDeviceName());
                installation.saveInBackground();

                SharedPreferences.Editor editor;
                editor = sharedPreferencesVisited.edit();

                String memberInfo = String.valueOf(numberOfMembers) + "^" +
                        lastNameEditText.getText() + "^" +
                        firstNameEditText.getText() + "^" +
                        middleNameEditText.getText() + "^" +
                        address1EditText.getText() + "^" +
                        address2EditText.getText() + "^" +
                        cityEditText.getText() + "^" +
                        stateEditText.getText() + "^" +
                        zipEditText.getText() + "^" +
                        homePhoneEditText.getText() + "^" +
                        mobilePhoneEditText.getText() + "^" +
                        emailEditText.getText() + "^" +
                        winterNameEditText.getText() + "^" +
                        winterAddress1EditText.getText() + "^" +
                        winterAddress2EditText.getText() + "^" +
                        winterCityEditText.getText() + "^" +
                        winterStateEditText.getText() + "^" +
                        winterZipEditText.getText() + "^" +
                        winterPhoneEditText.getText() + "^" +
                        winterEmailEditText.getText() + "^" +
                        memberNumberRandom + "^" +
                        installation.getString("MemberType") + "^" +
                        emerContactEditText.getText() + "^" +
                        emerContactPhoneEditText.getText() + "^" +
                        installation.getCreatedAt().toString() + "^";

                editor.putString("MEMBER_INFO", memberInfo);
                editor.putString(assocCode + "MemberNumber", memberNumberRandom);
                editor.apply();


                String homePhone = homePhoneEditText.getText().toString();
                String mobilePhone = mobilePhoneEditText.getText().toString();
                String winterPhone = winterPhoneEditText.getText().toString();
                String emerPhone = emerContactPhoneEditText.getText().toString();

                String p = "\\(";
                String p2 = "\\)";
                String d = "-";
                String e = "";

                String homePhoneForUpdate = homePhone.replaceAll(p,e).replaceAll(p2,e).replaceAll(d,e).replaceAll(" ", "").trim();
                String mobilePhoneForUpdate = mobilePhone.replaceAll(p,e).replaceAll(p2,e).replaceAll(d,e).replaceAll(" ", "").trim();
                String winterPhoneForUpdate = winterPhone.replaceAll(p,e).replaceAll(p2,e).replaceAll(d,e).replaceAll(" ", "").trim();
                String emerPhoneForUpdate = emerPhone.replaceAll(p,e).replaceAll(p2,e).replaceAll(d,e).replaceAll(" ", "").trim();


                rosterString = rosterString + "|" +
                        String.valueOf(numberOfMembers) + "^" +
                        lastNameEditText.getText() + "^" +
                        firstNameEditText.getText() + "^" +
                        middleNameEditText.getText() + "^" +
                        address1EditText.getText() + "^" +
                        address2EditText.getText() + "^" +
                        cityEditText.getText() + "^" +
                        stateEditText.getText() + "^" +
                        zipEditText.getText() + "^" +
                        homePhoneForUpdate + "^" +
                        mobilePhoneForUpdate + "^" +
                        emailEditText.getText() + "^" +
                        winterNameEditText.getText() + "^" +
                        winterAddress1EditText.getText() + "^" +
                        winterAddress2EditText.getText() + "^" +
                        winterCityEditText.getText() + "^" +
                        winterStateEditText.getText() + "^" +
                        winterZipEditText.getText() + "^" +
                        winterPhoneForUpdate + "^" +
                        winterEmailEditText.getText() + "^" +
                        memberNumberRandom + "^" +
                        installation.getString("MemberType") + "^" +
                        emerContactEditText.getText() + "^" +
                        emerPhoneForUpdate + "^" +
                        installation.getCreatedAt().toString() + "^";


                Log.d(TAG, "rosterString is " + rosterString);


                query.findInBackground(new FindCallback<ParseObject>() {


                    @Override
                    public void done(List<ParseObject> assoc, ParseException e) {

                        byte[] data = rosterString.getBytes();
                        ParseFile RosterFile = new ParseFile("Roster.txt", data);
                        try {
                            RosterFile.save();
                        } catch (ParseException e2) {
                            e2.printStackTrace();
                        }

                        assoc.get(0).put("RosterFile", RosterFile);
                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }

                });


                Intent mainActivity = new Intent();
                mainActivity.setClass(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
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

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);

        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent();
        mainActivity.setClass(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish();
    }


}

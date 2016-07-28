package comapps.com.myassociationhoa.myinfo;

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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.MainActivity;
import comapps.com.myassociationhoa.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class PersonalInfoActivity extends AppCompatActivity {

    private static final String TAG = "PERSONALINFOACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    Context context;

    ParseQuery<ParseObject> query;

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText middleNameEditText;
    EditText address1EditText;
    EditText address2EditText;
    EditText cityEditText;
    EditText stateEditText;
    EditText zipEditText;
    EditText homePhoneEditText;
    EditText mobilePhoneEditText;
    EditText emailEditText;
    EditText emerContactEditText;
    EditText emerContactPhoneEditText;
    EditText winterAddress1EditText;
    EditText winterAddress2EditText;
    EditText winterCityEditText;
    EditText winterStateEditText;
    EditText winterZipEditText;
    EditText winterPhoneEditText;
    EditText winterEmailEditText;

    TextView summerAddress;
    TextView winterAddress;


    String memberNumber;
    String[] memberInfoArray;
    String[] rosterFileArray;
    String rosterFileUpdate = "";
    String rosterFileString;

    Button updateButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ScrollView scrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.personal_info_layout);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Directory Update");
        }

        firstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
        lastNameEditText = (EditText) findViewById(R.id.editTextLastName);
        middleNameEditText = (EditText) findViewById(R.id.editTextMiddleName);
        address1EditText = (EditText) findViewById(R.id.editTextAddress1);
        address2EditText = (EditText) findViewById(R.id.editTextAddress2);
        cityEditText = (EditText) findViewById(R.id.cityEditText);
        stateEditText = (EditText) findViewById(R.id.stateEditText);
        zipEditText = (EditText) findViewById(R.id.zipEditText);
        homePhoneEditText = (EditText) findViewById(R.id.homePhoneEditText);
        mobilePhoneEditText = (EditText) findViewById(R.id.mobilePhoneEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        emerContactEditText = (EditText) findViewById(R.id.emergencyContactEditText);
        emerContactPhoneEditText = (EditText) findViewById(R.id.emergencyContactPhoneEditText);
        winterAddress1EditText = (EditText) findViewById(R.id.winter_address1EditText);
        winterAddress2EditText = (EditText) findViewById(R.id.winter_address2EditText);
        winterCityEditText = (EditText) findViewById(R.id.winter_cityEditText);
        winterStateEditText = (EditText) findViewById(R.id.winter_stateEditText);
        winterZipEditText = (EditText) findViewById(R.id.winter_zipEditText);
        winterPhoneEditText = (EditText) findViewById(R.id.winter_homePhoneEditText);
        winterEmailEditText = (EditText) findViewById(R.id.winter_emailEditText);

        summerAddress = (TextView) findViewById(R.id.summerAddressLabel);
        winterAddress = (TextView) findViewById(R.id.winterAddressLabel);

        homePhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mobilePhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        emerContactPhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        winterPhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        updateButton = (Button) findViewById(R.id.buttonSave);


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        String memberInfo = sharedPreferences.getString("MEMBER_INFO", "");


        memberInfoArray = memberInfo.split("\\^");

        int i = 0;
        for (String info : memberInfoArray) {

            info.trim();


            Log.v(TAG, "info(" + i + ")" + " " + info);

            switch (i) {
                case 0:
                    Log.v(TAG, "member number of Assoc ---> " + memberInfoArray[0]);
                    break;
                case 1:
                    lastNameEditText.setText(info);
                    break;
                case 2:
                    firstNameEditText.setText(info);
                    break;
                case 3:
                    middleNameEditText.setText(info);
                    break;
                case 4:
                    address1EditText.setText(info);
                    break;
                case 5:
                    address2EditText.setText(info);
                    break;
                case 6:
                    cityEditText.setText(info);
                    break;
                case 7:
                    stateEditText.setText(info);
                    break;
                case 8:
                    zipEditText.setText(info);
                    break;
                case 9:
                    homePhoneEditText.setText(info);
                    break;
                case 10:
                    mobilePhoneEditText.setText(info);
                    break;
                case 11:
                    emailEditText.setText(info);
                    break;
                case 12:
                    break;
                case 13:
                    winterAddress1EditText.setText(info);
                    break;
                case 14:
                    winterAddress2EditText.setText(info);
                    break;
                case 15:
                    winterCityEditText.setText(info);
                    break;
                case 16:
                    winterStateEditText.setText(info);
                    break;
                case 17:
                    winterZipEditText.setText(info);
                    break;
                case 18:
                    winterPhoneEditText.setText(info);
                    break;
                case 19:
                    winterEmailEditText.setText(info);
                    break;
                case 20:
                    break;
                case 21:
                    break;
                case 22:
                    emerContactEditText.setText(info);
                    break;
                case 23:
                    emerContactPhoneEditText.setText(info);


                    scrollView = (ScrollView) findViewById(R.id.scrollViewInfo);
                    scrollView.setFocusableInTouchMode(true);
                    scrollView.scrollTo(0, 0);

                    break;

            }


            i = i + 1;
        }


        summerAddress.setText(sharedPreferences.getString("defaultRecord(24)", ""));
        winterAddress.setText(sharedPreferences.getString("defaultRecord(25)", ""));

        final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        String memberName = ParseInstallation.getCurrentInstallation().getString("memberName");
        Log.d(TAG, "memberName is " + memberName);

        query = new ParseQuery<ParseObject>(ParseInstallation.getCurrentInstallation().getString("AssociationCode"));


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> assoc, ParseException e) {


                ParseFile rosterFile = assoc.get(0).getParseFile("RosterFile");
                rosterFileArray = null;

                try {
                    byte[] file = rosterFile.getData();
                    try {
                        rosterFileString = new String(file, "UTF-8");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }


            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updatedMemberInfo = memberInfoArray[0] + "^" +
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
                        memberInfoArray[12] + "^" +
                        winterAddress1EditText.getText() + "^" +
                        winterAddress2EditText.getText() + "^" +
                        winterCityEditText.getText() + "^" +
                        winterStateEditText.getText() + "^" +
                        winterZipEditText.getText() + "^" +
                        winterPhoneEditText.getText() + "^" +
                        winterEmailEditText.getText() + "^" +
                        memberInfoArray[20] + "^" +
                        memberInfoArray[21] + "^" +
                        emerContactEditText.getText() + "^" +
                        emerContactPhoneEditText.getText() + "^" +
                        memberInfoArray[24];


                editor = sharedPreferences.edit();
                editor.putString("MEMBER_INFO", updatedMemberInfo);
                editor.apply();


                memberNumber = ParseInstallation.getCurrentInstallation().getString("memberNumber");
                Log.d(TAG, "installation memberNumber -->" + memberNumber + "<--");

                rosterFileArray = rosterFileString.split("\\|");

                ArrayList<String> rosterArrayForSort = new ArrayList<String>();



                for (String member : rosterFileArray) {

                    String memberRemovedIndex = member.substring(member.indexOf("^") + 1);

                    if ( !member.contains(memberNumber)) {
                            rosterArrayForSort.add(memberRemovedIndex);
                    }



                }

                updatedMemberInfo = updatedMemberInfo.substring(updatedMemberInfo.indexOf("^") + 1);
                rosterArrayForSort.add(updatedMemberInfo);
                Collections.sort(rosterArrayForSort, String.CASE_INSENSITIVE_ORDER);

                int i = 0;

                for (String member : rosterArrayForSort) {

                    member = String.valueOf(i) + "^" + member;

                    Log.d(TAG, "rosterArrayForSort member -->" + member + "<--");

                    rosterFileUpdate = rosterFileUpdate + member + "|";

                    i++;

                }




                rosterFileUpdate = rosterFileUpdate.substring(0, rosterFileUpdate.length() - 1);

                Log.d(TAG, "rosterFileUpdate for load -->" + rosterFileUpdate + "<--");

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy, hh:mm a");
                final String formattedDate = df.format(c.getTime());


                query = new ParseQuery<ParseObject>(ParseInstallation.getCurrentInstallation().getString("AssociationCode"));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, ParseException e) {

                        byte[] data = rosterFileUpdate.getBytes();
                        ParseFile RosterFile = new ParseFile("Roster.txt", data);


                        try {
                            RosterFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("RosterFile", RosterFile);
                        assoc.get(0).put("RosterDate", formattedDate);
                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                    }
                });


                Toast.makeText(getBaseContext(), "MEMBER INFO UPDATED.", Toast.LENGTH_LONG).show();


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

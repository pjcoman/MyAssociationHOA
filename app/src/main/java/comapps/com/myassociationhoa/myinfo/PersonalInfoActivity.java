package comapps.com.myassociationhoa.myinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.objects.RosterObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class PersonalInfoActivity extends AppCompatActivity {

    private static final String TAG = "PERSONALINFOACTIVITY";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";
    private static final String MYPREFERENCES = "MyPrefs";

    private ParseQuery<ParseObject> query;
    private ParseInstallation installation;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText middleNameEditText;
    private EditText address1EditText;
    private EditText address2EditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText zipEditText;
    private EditText homePhoneEditText;
    private EditText mobilePhoneEditText;
    private EditText emailEditText;
    private EditText emerContactEditText;
    private EditText emerContactPhoneEditText;
    private EditText winterAddress1EditText;
    private EditText winterAddress2EditText;
    private EditText winterCityEditText;
    private EditText winterStateEditText;
    private EditText winterZipEditText;
    private EditText winterPhoneEditText;
    private EditText winterEmailEditText;
    private TextView textViewStatus;
    private EditText editTextGroup1;
    private EditText editTextGroup2;
    private EditText editTextGroup3;
    private EditText editTextGroup4;
    private EditText editTextGroup5;
    private EditText editTextGroup6;
    private EditText editTextGroup7;
    private EditText editTextGroup8;


    private TextView summerAddress;
    private TextView winterAddress;

    private RosterObject rosterObject;

    private String memberNumber;
    private String memberType;
    private String[] rosterFileArray;
    private String rosterFileUpdate = "";
    private String rosterFileString;

    private Bundle bundle;
    private Boolean fromAddGuest;
    private Boolean fromAdminUpdate;
    private Boolean fromMyInfo;

    private String homePhone;
    private String mobilePhone;
    private String winterPhone;
    private String emerPhone;

    private String p;
    private String p2;
    private String d;
    private String ep;

    private String homePhoneForUpdate;
    private String mobilePhoneForUpdate;
    private String winterPhoneForUpdate;
    private String emerPhoneForUpdate;

    private String rosterPosition;

    private Button updateButton;

    private LinearLayout myGroups;

    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editorVisited;
    SharedPreferences.Editor editor;
    private ScrollView scrollView;

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

        hideSoftKeyboard();

        myGroups = (LinearLayout) findViewById(R.id.llGroups);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);

        bundle = getIntent().getExtras();

        fromAddGuest = false;
        fromAdminUpdate = false;
        fromMyInfo = false;

        installation = ParseInstallation.getCurrentInstallation();

        if ( bundle != null ) {

            fromAddGuest = bundle.getBoolean("FROMADDGUEST", false);
            fromAdminUpdate = bundle.getBoolean("FROMADMINUPDATE", false);
            fromMyInfo = bundle.getBoolean("FROMMYINFO", false);

            if ( fromMyInfo ) {

                assert bar != null;
                bar.setTitle("Update MyInfo");
                myGroups.setVisibility(View.GONE);
                textViewStatus.setVisibility(View.GONE);

            }

            if ( fromAdminUpdate && !fromMyInfo) {

                Gson gson = new Gson();
                rosterObject = gson.fromJson(bundle.getString("ROSTEROBJECTTOUPDATE"), RosterObject.class);
                rosterPosition = String.valueOf(bundle.getInt("ROSTERPOSITION"));
                assert bar != null;
                bar.setTitle("Android Admin Update");
                myGroups.setVisibility(View.VISIBLE);
                textViewStatus.setVisibility(View.VISIBLE);
            }

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



        editTextGroup1 = (EditText) findViewById(R.id.editTextGroup1);
        editTextGroup2 = (EditText) findViewById(R.id.editTextGroup2);
        editTextGroup3 = (EditText) findViewById(R.id.editTextGroup3);
        editTextGroup4 = (EditText) findViewById(R.id.editTextGroup4);
        editTextGroup5 = (EditText) findViewById(R.id.editTextGroup5);
        editTextGroup6 = (EditText) findViewById(R.id.editTextGroup6);
        editTextGroup7 = (EditText) findViewById(R.id.editTextGroup7);
        editTextGroup8 = (EditText) findViewById(R.id.editTextGroup8);


        updateButton = (Button) findViewById(R.id.buttonSave);


        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);



        if ( fromAdminUpdate ) {

            lastNameEditText.setText(rosterObject.getLastName());
            firstNameEditText.setText(rosterObject.getFirstName());
            middleNameEditText.setText(rosterObject.getMiddleName());
            address1EditText.setText(rosterObject.getHomeAddress1());
            address2EditText.setText(rosterObject.getHomeAddress2());
            cityEditText.setText(rosterObject.getHomeCity());
            stateEditText.setText(rosterObject.getHomeState());
            zipEditText.setText(rosterObject.getHomeZip());
            homePhoneEditText.setText(rosterObject.getHomePhone());
            mobilePhoneEditText.setText(rosterObject.getMobilePhone());
            emailEditText.setText(rosterObject.getEmail());
            winterAddress1EditText.setText(rosterObject.getWinterAddress1());
            winterAddress2EditText.setText(rosterObject.getWinterAddress2());
            winterCityEditText.setText(rosterObject.getWinterCity());
            winterStateEditText.setText(rosterObject.getWinterState());
            winterZipEditText.setText(rosterObject.getWinterZip());
            winterPhoneEditText.setText(rosterObject.getWinterPhone());
            winterEmailEditText.setText(rosterObject.getWinterEmail());
            emerContactEditText.setText(rosterObject.getEmergencyName());
            emerContactPhoneEditText.setText(rosterObject.getEmergencyPhoneNumber());
            textViewStatus.setText(rosterObject.getStatus());

            textViewStatus.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      if (textViewStatus.getText().toString().equals("Resigned")) {
                                                          textViewStatus.setText("Administrator");
                                                      } else if (textViewStatus.getText().toString().equals("Administrator")) {
                                                          textViewStatus.setText("Master");
                                                      } else if (textViewStatus.getText().toString().equals("Master")) {
                                                          textViewStatus.setText("Security");
                                                      } else if (textViewStatus.getText().toString().equals("Security")) {
                                                          textViewStatus.setText("Guest");
                                                      } else if (textViewStatus.getText().toString().equals("Guest")) {
                                                          textViewStatus.setText("Member");
                                                      } else if (textViewStatus.getText().toString().equals("Member")) {
                                                          textViewStatus.setText("Resigned");
                                                      }
                                              }

});


            String[] memberGroups = new String[0];
            try {
                memberGroups = rosterObject.getGroups().split(",", -1);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "not a member of any groups");
            }

            int i = 0;
            for (String group: memberGroups) {

                switch (i) {
                    case 0:
                        editTextGroup1.setText(group);
                        break;
                    case 1:
                        editTextGroup2.setText(group);
                        break;
                    case 2:
                        editTextGroup3.setText(group);
                        break;
                    case 3:
                        editTextGroup4.setText(group);
                        break;
                    case 4:
                        editTextGroup5.setText(group);
                        break;
                    case 5:
                        editTextGroup6.setText(group);
                        break;
                    case 6:
                        editTextGroup7.setText(group);
                        break;
                    case 7:
                        editTextGroup7.setText(group);
                        break;

                }


                i++;
            }




        } else {

            Gson gson = new Gson();
            rosterObject = gson.fromJson(sharedPreferences.getString("ROSTEROBJECTMYINFO",""), RosterObject.class);



            Log.v(TAG, "member number of Assoc ---> " + rosterObject.getNumber());
            lastNameEditText.setText(rosterObject.getLastName());
            firstNameEditText.setText(rosterObject.getFirstName());
            middleNameEditText.setText(rosterObject.getMiddleName());
            address1EditText.setText(rosterObject.getHomeAddress1());
            address2EditText.setText(rosterObject.getHomeAddress2());
            cityEditText.setText(rosterObject.getHomeCity());
            stateEditText.setText(rosterObject.getHomeState());
            zipEditText.setText(rosterObject.getHomeZip());
            homePhoneEditText.setText(rosterObject.getHomePhone());
            mobilePhoneEditText.setText(rosterObject.getMobilePhone());
            emailEditText.setText(rosterObject.getEmail());
            winterAddress1EditText.setText(rosterObject.getWinterAddress1());
            winterAddress2EditText.setText(rosterObject.getWinterAddress2());
            winterCityEditText.setText(rosterObject.getWinterCity());
            winterStateEditText.setText(rosterObject.getWinterState());
            winterZipEditText.setText(rosterObject.getWinterZip());
            winterPhoneEditText.setText(rosterObject.getWinterPhone());
            winterEmailEditText.setText(rosterObject.getWinterEmail());
            emerContactEditText.setText(rosterObject.getEmergencyName());
            emerContactPhoneEditText.setText(rosterObject.getEmergencyPhoneNumber());
            textViewStatus.setText(rosterObject.getStatus());

            String[] memberGroups = new String[0];
            try {
                memberGroups = rosterObject.getGroups().split(",", -1);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "not a member of any groups");
            }

            int i = 0;
            for (String group: memberGroups) {

                switch (i) {
                    case 0:
                        editTextGroup1.setText(group);
                        break;
                    case 1:
                        editTextGroup2.setText(group);
                        break;
                    case 2:
                        editTextGroup3.setText(group);
                        break;
                    case 3:
                        editTextGroup4.setText(group);
                        break;
                    case 4:
                        editTextGroup5.setText(group);
                        break;
                    case 5:
                        editTextGroup6.setText(group);
                        break;
                    case 6:
                        editTextGroup7.setText(group);
                        break;
                    case 7:
                        editTextGroup7.setText(group);
                        break;

                }


                i++;
            }




            scrollView = (ScrollView) findViewById(R.id.scrollViewInfo);
            scrollView.setFocusableInTouchMode(true);
            scrollView.scrollTo(0, 0);




            String memberName = installation.getString("memberName");
            Log.d(TAG, "memberName is " + memberName);

        }



        query = new ParseQuery<>(installation.getString("AssociationCode")).fromLocalDatastore();


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> assoc, ParseException e) {


                ParseFile rosterFile = assoc.get(0).getParseFile("RosterFile");


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

                Log.d(TAG, "rosterFileString --> " + rosterFileString);
                rosterFileArray = rosterFileString.split("\\|", -1);

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        homePhone = homePhoneEditText.getText().toString();
                        mobilePhone = mobilePhoneEditText.getText().toString();
                        winterPhone = winterPhoneEditText.getText().toString();
                        emerPhone = emerContactPhoneEditText.getText().toString();

                        p = "\\(";
                        p2 = "\\)";
                        d = "-";
                        ep = "";

                        homePhoneForUpdate = homePhone.replaceAll(p, ep).replaceAll(p2, ep).replaceAll(d, ep).replaceAll(" ", "").trim();
                        mobilePhoneForUpdate = mobilePhone.replaceAll(p, ep).replaceAll(p2, ep).replaceAll(d, ep).replaceAll(" ", "").trim();
                        winterPhoneForUpdate = winterPhone.replaceAll(p, ep).replaceAll(p2, ep).replaceAll(d, ep).replaceAll(" ", "").trim();
                        emerPhoneForUpdate = emerPhone.replaceAll(p, ep).replaceAll(p2, ep).replaceAll(d, ep).replaceAll(" ", "").trim();





                        if ( !fromAdminUpdate ) {

                            memberNumber = ParseInstallation.getCurrentInstallation().getString("memberNumber");
                            memberType = ParseInstallation.getCurrentInstallation().getString("MemberType");




                            String updatedMemberInfo = rosterObject.getNumber() + "^" +
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
                                    rosterObject.getBusinessName() + "^" +
                                    winterAddress1EditText.getText() + "^" +
                                    winterAddress2EditText.getText() + "^" +
                                    winterCityEditText.getText() + "^" +
                                    winterStateEditText.getText() + "^" +
                                    winterZipEditText.getText() + "^" +
                                    winterPhoneForUpdate + "^" +
                                    winterEmailEditText.getText() + "^" +
                                    memberNumber + "^" +
                                    memberType + "^" +
                                    emerContactEditText.getText() + "^" +
                                    emerPhoneForUpdate + "^" +
                                    rosterObject.getActivationDate() + "^" + rosterObject.getGroups();


                            editorVisited = sharedPreferencesVisited.edit();

                            editorVisited.putString("MEMBER_INFO", updatedMemberInfo);
                            editorVisited.putString("SUMMER_EMAIL", emailEditText.getText().toString());
                            editorVisited.putString("FULL_NAME", firstNameEditText.getText().toString() + " " + middleNameEditText.getText().toString() + " " +
                                    lastNameEditText.getText().toString());
                            editorVisited.apply();


                            Log.d(TAG, "installation memberNumber -->" + memberNumber + "<--");

                            Log.d(TAG, "rosterFileString --> " + rosterFileString);




                            ArrayList<String> rosterArrayForSort = new ArrayList<>();


                            for (String member : rosterFileArray) {


                                String memberRemovedIndex = member.substring(member.indexOf("^") + 1);


                                if (!member.contains(memberNumber)) {
                                    rosterArrayForSort.add(memberRemovedIndex);


                                }


                            }


                            updatedMemberInfo = updatedMemberInfo.substring(updatedMemberInfo.indexOf("^") + 1);


                            rosterArrayForSort.add(updatedMemberInfo);


                            Collections.sort(rosterArrayForSort, String.CASE_INSENSITIVE_ORDER);


                            int i = 0;

                            for (String member : rosterArrayForSort) {

                                Log.d(TAG, "member before index -->" + member + "<--");

                                String memberIndexed = String.valueOf(i) + "^" + member.substring(0, member.length());

                                Log.d(TAG, "member after index -->" + memberIndexed + "<--");

                                rosterFileUpdate = rosterFileUpdate + memberIndexed + "|";

                                i++;

                            }


                            final String rosterFileUpdateForLoad = rosterFileUpdate.substring(0, rosterFileUpdate.length() - 1);

                            Log.d(TAG, "rosterFileUpdate for load -->" + rosterFileUpdateForLoad + "<--");

                            Calendar c = Calendar.getInstance();
                            System.out.println("Current time => " + c.getTime());

                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy, h:mm a");
                            final String formattedDate = df.format(c.getTime());


                            query = new ParseQuery<>(ParseInstallation.getCurrentInstallation().getString("AssociationCode"));

                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> assoc, ParseException e) {

                                    byte[] data = rosterFileUpdateForLoad.getBytes();
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



                        } else {


                            int i = 0;
                            for ( String rosterItem: rosterFileArray) {
                                Log.d(TAG, "rosterItem ---->" + rosterItem + "<----" + " " + i);
                            }

                            Log.d(TAG, "substring to update ---->" + rosterFileArray[Integer.valueOf(rosterPosition)]);


                            homePhone = homePhoneEditText.getText().toString();
                            mobilePhone = mobilePhoneEditText.getText().toString();
                            winterPhone = winterPhoneEditText.getText().toString();
                            emerPhone = emerContactPhoneEditText.getText().toString();

                            p = "\\(";
                            p2 = "\\)";
                            d = "-";
                            ep = "";

                            homePhoneForUpdate = homePhone.replaceAll(p, ep).replaceAll(p2, ep).replaceAll(d, ep).replaceAll(" ", "").trim();
                            mobilePhoneForUpdate = mobilePhone.replaceAll(p, ep).replaceAll(p2, ep).replaceAll(d, ep).replaceAll(" ", "").trim();
                            winterPhoneForUpdate = winterPhone.replaceAll(p, ep).replaceAll(p2, ep).replaceAll(d, ep).replaceAll(" ", "").trim();
                            emerPhoneForUpdate = emerPhone.replaceAll(p, ep).replaceAll(p2, ep).replaceAll(d, ep).replaceAll(" ", "").trim();

                            Log.d(TAG, "rosterObject to update ---->" + rosterObject.toString());


                            String updatedMemberInfoFromAdmin = Integer.valueOf(rosterPosition) + "^" +
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
                                    rosterObject.getBusinessName() + "^" +
                                    winterAddress1EditText.getText() + "^" +
                                    winterAddress2EditText.getText() + "^" +
                                    winterCityEditText.getText() + "^" +
                                    winterStateEditText.getText() + "^" +
                                    winterZipEditText.getText() + "^" +
                                    winterPhoneForUpdate + "^" +
                                    winterEmailEditText.getText() + "^" +
                                    rosterObject.getMemberNumber() + "^" +
                                    textViewStatus.getText() + "^" +
                                    emerContactEditText.getText() + "^" +
                                    emerPhoneForUpdate + "^" +
                                    rosterObject.getActivationDate() + "^";


                            Log.d(TAG, "updateMemberInfoFromAdmin ---->" + updatedMemberInfoFromAdmin);

                            String memberGroupsToAdd = "";

                            if ( editTextGroup1.getText() != null && editTextGroup1.getText().toString().length() > 0 ) {
                                memberGroupsToAdd = memberGroupsToAdd + editTextGroup1.getText().toString();
                            }
                            if ( editTextGroup2.getText() != null && editTextGroup2.getText().toString().length() > 0 ) {
                                memberGroupsToAdd = memberGroupsToAdd + "," + editTextGroup2.getText().toString();
                            }
                            if ( editTextGroup3.getText() != null && editTextGroup3.getText().toString().length() > 0 ) {
                                memberGroupsToAdd = memberGroupsToAdd + "," + editTextGroup3.getText().toString();
                            }
                            if ( editTextGroup4.getText() != null && editTextGroup4.getText().toString().length() > 0 ) {
                                memberGroupsToAdd = memberGroupsToAdd + "," + editTextGroup4.getText().toString();
                            }
                            if ( editTextGroup5.getText() != null && editTextGroup5.getText().toString().length() > 0 ) {
                                memberGroupsToAdd = memberGroupsToAdd + "," + editTextGroup5.getText().toString();
                            }
                            if ( editTextGroup6.getText() != null && editTextGroup6.getText().toString().length() > 0 ) {
                                memberGroupsToAdd = memberGroupsToAdd + "," + editTextGroup6.getText().toString();
                            }
                            if ( editTextGroup7.getText() != null && editTextGroup7.getText().toString().length() > 0 ) {
                                memberGroupsToAdd = memberGroupsToAdd + "," + editTextGroup7.getText().toString();
                            }
                            if ( editTextGroup8.getText() != null && editTextGroup8.getText().toString().length() > 0 ) {
                                memberGroupsToAdd = memberGroupsToAdd + "," + editTextGroup8.getText().toString();
                            }


                            try {
                                if ( memberGroupsToAdd.substring(0,1).contains(",")) {
                                    memberGroupsToAdd = memberGroupsToAdd.substring(1);
                                }
                                Log.d(TAG, "memberGroupsToAdd ---->" + memberGroupsToAdd);
                                updatedMemberInfoFromAdmin = updatedMemberInfoFromAdmin + memberGroupsToAdd;
                                Log.d(TAG, "updatedMemberAfterGroupsAdd  ---->" + updatedMemberInfoFromAdmin);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            rosterFileUpdate = rosterFileString.replace(rosterFileArray[Integer.valueOf(rosterPosition)], updatedMemberInfoFromAdmin);


                            Log.d(TAG, "rosterFileUpdate ---->" + rosterFileUpdate);

                            Calendar c = Calendar.getInstance();
                            System.out.println("Current time => " + c.getTime());
                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy, h:mm a");
                            final String formattedDate = df.format(c.getTime());

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


                 /*       ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
                        query.whereEqualTo("memberNumber", rosterObject.getMemberNumber());
                        query.getFirstInBackground(new GetCallback<ParseInstallation>() {
                            @Override
                            public void done(ParseInstallation parseInstallation, ParseException e) {
                                parseInstallation.put("MemberType", textViewStatus.getText().toString());
                                parseInstallation.saveInBackground();
                            }
                        });

*/





                        AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                        remoteDataTaskClass.execute();




                        Toast toast = Toast.makeText(getBaseContext(), "MEMBER INFO UPDATED.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();





                        finish();






                    }






                });



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

    private void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }



    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {

        finish();
    }


}

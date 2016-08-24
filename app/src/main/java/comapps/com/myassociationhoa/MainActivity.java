package comapps.com.myassociationhoa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import comapps.com.myassociationhoa.autos.AutosActivity;
import comapps.com.myassociationhoa.budget.BudgetActivity;
import comapps.com.myassociationhoa.calendar.CalendarActivity;
import comapps.com.myassociationhoa.contact.ContactActivity;
import comapps.com.myassociationhoa.directory.DirectoryActivity;
import comapps.com.myassociationhoa.documents.DocumentsActivity;
import comapps.com.myassociationhoa.guests.GuestsActivity;
import comapps.com.myassociationhoa.maintenance.MaintenanceActivity;
import comapps.com.myassociationhoa.messageboard.MBActivity;
import comapps.com.myassociationhoa.myinfo.MyInfoActivity;
import comapps.com.myassociationhoa.objects.AutoObject;
import comapps.com.myassociationhoa.objects.CalendarObject;
import comapps.com.myassociationhoa.objects.GuestObject;
import comapps.com.myassociationhoa.objects.MBObject;
import comapps.com.myassociationhoa.objects.MaintenanceCategoryObject;
import comapps.com.myassociationhoa.objects.MaintenanceObject;
import comapps.com.myassociationhoa.objects.PetObject;
import comapps.com.myassociationhoa.objects.ProviderObject;
import comapps.com.myassociationhoa.objects.PushObject;
import comapps.com.myassociationhoa.objects.RosterObject;
import comapps.com.myassociationhoa.pets.PetsActivity;
import comapps.com.myassociationhoa.push_history.PushActivity;
import comapps.com.myassociationhoa.service_providers.ServiceProviderActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MAINACTIVITY";

    private static final String MYPREFERENCES = "MyPrefs";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    CustomVolleyRequest customVolleyRequest;

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesVisited;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorVisited;



    private ParseInstallation installation;
    ParseQuery<ParseObject> queryAssociations;



    private RosterObject rosterObject;
    private ArrayList<RosterObject> rosterObjects;

    private CalendarObject calendarObject;
    private ArrayList<CalendarObject> calendarObjects;

    MaintenanceObject maintenanceObject;
    private ArrayList<MaintenanceObject> maintenanceObjects;

    MaintenanceCategoryObject maintenanceCategoryObject;
    private ArrayList<MaintenanceCategoryObject> maintenanceCategoryObjects;

    ProviderObject providerObject;
    private ArrayList<ProviderObject> providerObjects;

    MBObject mbObject;
    private ArrayList<MBObject> mbObjects;

    PushObject pushObject;
    private ArrayList<PushObject> pushObjectsMember;
    private ArrayList<PushObject> pushObjects;

    PetObject petObject;
    private ArrayList<PetObject> petObjects;

    AutoObject autoObject;
    private ArrayList<AutoObject> autoObjects;

    GuestObject guestObject;
    private ArrayList<GuestObject> guestObjects;



    String url;


    ImageLoader imageLoader;



    private String memberName;
    private String memberType;
    private String memberDeviceName;
    private String memberNumber;
    private String associationCode;
    private TextView associationName;

    Button b1; //email button
    Button b2; //contact button
    Button b3; //directory button
    Button b4; //weather button
    Button b5; //calendar button
    Button b6; //budget button
    Button b7; //documents button
    Button b8; //message board button
    Button b9; //my info button
    Button b10; //push history button
    Button b11; //service providers button
    Button b12; //change add assoc button
    Button b13; //pets directory button
    Button b14; //guests directory button
    Button b15; //auto directory button
    Button b16; //tools button
    Button b17; //push email button
    Button b18; //maintenance items button
    Button b18b;

    LinearLayout contentMain;

    LinearLayout ll1;
    LinearLayout ll2;
    LinearLayout ll3;

    ImageView redDot;

    int i = 0;
    int j = 0;
    int k = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     /*   ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
*/


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {


            bar.setTitle("Main Menu Ver " + getResources().getString(R.string.app_version));

        }


        contentMain = (LinearLayout) findViewById(R.id.llContentMainVertical);









        associationName = (TextView) findViewById(R.id.textViewAssociationName);

        redDot = (ImageView) findViewById(R.id.imageViewRedDot);

        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        b6 = (Button) findViewById(R.id.button6);
        b7 = (Button) findViewById(R.id.button7);
        b8 = (Button) findViewById(R.id.button8);
        b9 = (Button) findViewById(R.id.button9);
        b10 = (Button) findViewById(R.id.button10);
        b11 = (Button) findViewById(R.id.button11);
        b12 = (Button) findViewById(R.id.button12);
        b13 = (Button) findViewById(R.id.button13);
        b14 = (Button) findViewById(R.id.button14);
        b15 = (Button) findViewById(R.id.button15);
        b16 = (Button) findViewById(R.id.button16);
        b17 = (Button) findViewById(R.id.button17);
        b18 = (Button) findViewById(R.id.button18);
        b18b = (Button) findViewById(R.id.button18_b);


        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);



        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

     /*   editorVisited = sharedPreferencesVisited.edit();
        editorVisited.putString("ASSOCIATIONS_JOINED", "Android Test HOA^Administrator^Android|Android Test HOA^Member^Android");
        editorVisited.putString("ASSOCIATIONS", "AndroidAdminAndroidMember");
        editorVisited.apply();*/

         Log.d(TAG, "visited before ------> " + sharedPreferences.getBoolean("visitedBefore", false));


        if (!sharedPreferencesVisited.getBoolean("visitedBefore", false)) {



            // Log.d(TAG, "visited before ------> " + sharedPreferences.getBoolean("visitedBefore", false));
            startActivity(new Intent(MainActivity.this, PopEnterPasscode.class));


        } else {

            // Log.d(TAG, "NOT FIRST VISIT");



            installation = ParseInstallation.getCurrentInstallation();
            memberType = installation.getString("MemberType");
            associationCode = installation.getString("AssociationCode");
            memberName = installation.getString("memberName");
            memberNumber = installation.getString("memberNumber");

            editor = sharedPreferences.edit();
            editor.putString("MEMBERNUMBER", memberNumber);
            editor.putString("MEMBERTYPE", memberType);
            editor.putString("MEMBERNAME", memberName);


            editor.apply();


            new RemoteDataTask().execute();



        }

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b10.setOnClickListener(this);
        b11.setOnClickListener(this);
        b12.setOnClickListener(this);
        b13.setOnClickListener(this);
        b14.setOnClickListener(this);
        b15.setOnClickListener(this);
        b16.setOnClickListener(this);
        b17.setOnClickListener(this);
        b18.setOnClickListener(this);
        b18b.setOnClickListener(this);



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

    private void sendEmail() {
        // TODO Auto-generated method stub

        // The following code is the implementation of Email client
        Intent intentSendEmail = new Intent(android.content.Intent.ACTION_SEND);
        intentSendEmail.setType("text/plain");
        String[] address = {sharedPreferences.getString("defaultRecord(2)", "")};

        intentSendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, address);
        intentSendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Question from " + memberName);

        startActivityForResult((Intent.createChooser(intentSendEmail, "Email")), 1);

    }


    private void Guide() {

        Intent loadGuide = new Intent();
        loadGuide.setClass(this, GuideActivity.class);
        startActivity(loadGuide);
   //     overridePendingTransition(R.anim.fadeinanimationgallery,R.anim.fadeoutanimationgallery);


    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();

        switch (v.getId()) {
            //handle multiple view click events
            case R.id.button:
                sendEmail();
                break;
            case R.id.button2:
                intent.setClass(this, ContactActivity.class);
                break;
            case R.id.button3:
                intent.setClass(this, DirectoryActivity.class);
                break;
            case R.id.button4:
                intent.setClass(this, WeatherActivity.class);
                break;
            case R.id.button5:
                intent.setClass(this, CalendarActivity.class);
                break;
            case R.id.button6:
                intent.setClass(this, BudgetActivity.class);
                break;
            case R.id.button7:
                intent.setClass(this, DocumentsActivity.class);
                break;
            case R.id.button8:
                intent.setClass(this, MBActivity.class);
                break;
            case R.id.button9:
                intent.setClass(this, MyInfoActivity.class);
                break;
            case R.id.button10:
                intent.setClass(this, PushActivity.class);
                break;
            case R.id.button11:
                intent.setClass(this, ServiceProviderActivity.class);
                break;
            case R.id.button12:
                intent.setClass(this, Change_Add_Associations.class);
                break;
            case R.id.button13:
                intent.setClass(this, PetsActivity.class);
                break;
            case R.id.button14:
                intent.setClass(this, GuestsActivity.class);
                break;
            case R.id.button15:
                intent.setClass(this, AutosActivity.class);
                break;
            case R.id.button16:
                intent.setClass(this, ToolsActivity.class);
                break;
            case R.id.button17:
                intent.setClass(this, AdminPushActivity.class);
                break;
            case R.id.button18:
                intent.setClass(this, MaintenanceActivity.class);
                break;
            case R.id.button18_b:
                intent.setClass(this, MaintenanceActivity.class);
                break;
        }

        startActivity(intent);
    }


    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {



            try {
                queryAssociations = new ParseQuery<>(installation.getString("AssociationCode")).fromLocalDatastore();
            } catch (Exception e) {
                queryAssociations = new ParseQuery<>(installation.getString("AssociationCode"));
                e.printStackTrace();
            }


            queryAssociations.findInBackground(new FindCallback<ParseObject>() {
                        public void done(final List<ParseObject> associationObject, ParseException e) {

                            if (e == null) {




                                ParseObject.unpinAllInBackground(associationObject, new DeleteCallback() {
                                    public void done(ParseException e) {
                                        // Cache the new results.
                                        ParseObject.pinAllInBackground(associationCode, associationObject);

                                        Log.d(TAG, "association object pinned");


                                    }
                                });


                                contentMain.setVisibility(View.VISIBLE);



                                ParseFile backgroundImage = null;
                                try {
                                    backgroundImage = (ParseFile) associationObject.get(0).get("Image1File");

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                ParseFile backgroundImage2 = null;
                                try {
                                    backgroundImage2 = (ParseFile) associationObject.get(0).get("Image2File");
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                                url = backgroundImage.getUrl();


                                imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();



                                    NetworkImageView niv = (NetworkImageView) findViewById(R.id.networkImageView);
                                    if(url.length() > 0)
                                        niv.setImageUrl(url, imageLoader);
                           /*     niv.setDefaultImageResId(R.drawable.button_rounded_corners_black);
                                niv.setErrorImageResId(R.drawable.blonde_engraved);*/





                                editorVisited = sharedPreferencesVisited.edit();
                                editorVisited.putString("backgroundImageUrl", backgroundImage.getUrl());
                                editorVisited.putString("backgroundImage2Url", backgroundImage2.getUrl());

                                editorVisited.apply();


                                Log.d(TAG, "bg image 1 " + backgroundImage.getUrl());
                                Log.d(TAG, "bg image 2 " + backgroundImage2.getUrl());

                                Log.d(TAG, "bg image 1 " + sharedPreferencesVisited.getString("backgroundImageUrl",""));
                                Log.d(TAG, "bg image 2 " + sharedPreferencesVisited.getString("backgroundImage2Url",""));



                                ParseFile defaultsFile = associationObject.get(0).getParseFile("DefaultsFile");
                                String[] defaultsFileArray = null;


                                byte[] file = new byte[0];
                                try {
                                    file = defaultsFile.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }


                                String defaultsFileString = null;
                                try {
                                    defaultsFileString = new String(file, "UTF-8");
                                } catch (UnsupportedEncodingException e3) {
                                    e3.printStackTrace();
                                }
                                // Log.d(TAG, "defaultsFileString is " + defaultsFileString);
                                defaultsFileArray = defaultsFileString.split("\\|");


                                for (int i = 0; i < defaultsFileArray.length; i++) {
                                    // Log.d(TAG, "defaultRecord(" + Integer.toString(i) + ") " + defaultsFileArray[i]);
                                    editor = sharedPreferences.edit();
                                    editor.putString("defaultRecord(" + Integer.toString(i) + ")", defaultsFileArray[i]);
                                    editor.apply();
                                }

                                associationName.setText(sharedPreferences.getString("defaultRecord(30)", ""));

                                if (sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {
                                    if (b13 != null) {
                                        b13.setVisibility(View.GONE);
                                    } else {
                                        b13.setVisibility(View.VISIBLE);
                                    }
                                    }


                                // check defaults for auto

                                if (sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {
                                    if (b15 != null) {
                                        b15.setVisibility(View.GONE);
                                    } else {
                                        b15.setVisibility(View.VISIBLE);
                                    }
                                    }


                                // check defaults for guests

                                if (sharedPreferences.getString("defaultRecord(39)", "No").equals("No")) {
                                    if (b14 != null) {
                                        b14.setVisibility(View.GONE);
                                    } else {
                                        b14.setVisibility(View.VISIBLE);
                                    }
                                    }


                                // check defaults for maintenance items

                                if (sharedPreferences.getString("defaultRecord(46)", "No").equals("No")) {
                                    if (b18 != null) {
                                        b18b.setVisibility(View.GONE);
                                    } else {
                                        b18b.setVisibility(View.VISIBLE);
                                    }
                                }

                                // check defaults for service providers

                                if (sharedPreferences.getString("defaultRecord(47)", "No").equals("No")) {
                                    if (b11 != null) {
                                        b11.setVisibility(View.GONE);
                                    } else {
                                        b11.setVisibility(View.VISIBLE);
                                    }
                                    }

                                switch (memberType) {


                                    case "Member":
                                        b14.setVisibility(View.GONE);
                                        b16.setVisibility(View.GONE);
                                        b17.setVisibility(View.GONE);
                                        b18.setVisibility(View.GONE);
                                        b18b.setVisibility(View.VISIBLE);
                                        ll1.setWeightSum(5);
                                        ll2.setWeightSum(5);
                                        ll3.setWeightSum(5);


                                        break;
                                    case "Administrator":


                                        b13.setVisibility(View.VISIBLE);
                                        b14.setVisibility(View.VISIBLE);
                                        b15.setVisibility(View.VISIBLE);
                                        b16.setVisibility(View.VISIBLE);
                                        b17.setVisibility(View.VISIBLE);
                                        b18.setVisibility(View.VISIBLE);
                                        b18b.setVisibility(View.GONE);

                                        break;

                                    case "Master":


                                        b13.setVisibility(View.VISIBLE);
                                        b14.setVisibility(View.VISIBLE);
                                        b15.setVisibility(View.VISIBLE);
                                        b16.setVisibility(View.VISIBLE);
                                        b17.setVisibility(View.VISIBLE);
                                        b18.setVisibility(View.VISIBLE);
                                        b18b.setVisibility(View.GONE);

                                        break;


                                }



                                TextView associationName = (TextView) findViewById(R.id.textViewAssociationName);
                                associationName.setText(defaultsFileArray[1]);





//****************************************************************************ROSTER**********************************************************************************


                                try {
                                    ParseFile rosterFile = associationObject.get(0).getParseFile("RosterFile");

                                    String[] rosterFileArray = null;


                                    byte[] rosterFileData = new byte[0];
                                    try {
                                        rosterFileData = rosterFile.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }


                                    String rosterFileString = null;
                                    try {
                                        rosterFileString = new String(rosterFileData, "UTF-8");
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }

                                    rosterFileArray = rosterFileString.split("\\|");


                                    for (String member : rosterFileArray) {

                                        member.trim();

                                    }


                                    rosterObjects = new ArrayList<RosterObject>();

                                    for (i = 0; i < rosterFileArray.length; i++) {


                                        String[] rosterFields = rosterFileArray[i].split("\\^");

                                        //    // Log.d(TAG, "rosterFileArray[" + i + "] is " + rosterFileArray[i].substring(0, 40));
                                        //     // Log.d(TAG, "rosterFileArray[" + i + "] number of fields is " + count(rosterFileArray[i], '^'));
                                        //    // Log.d(TAG, "rosterFileArray member is " + rosterFileArray[i]);

                                        rosterObject = new RosterObject();

                                        for (j = 0; j < rosterFields.length; j++) {

                                            // Log.d(TAG, "rosterFileArray[" + i + "] field" + j + " is " + rosterFields[j]);

                                            switch (j) {
                                                case 0:
                                                    rosterObject.setNumber(rosterFields[j]);
                                                    break;
                                                case 1:
                                                    rosterObject.setLastName(rosterFields[j]);
                                                    break;
                                                case 2:
                                                    rosterObject.setFirstName(rosterFields[j]);
                                                    break;
                                                case 3:
                                                    rosterObject.setMiddleName(rosterFields[j]);
                                                    break;
                                                case 4:
                                                    rosterObject.setHomeAddress1(rosterFields[j]);
                                                    break;
                                                case 5:
                                                    rosterObject.setHomeAddress2(rosterFields[j]);
                                                    break;
                                                case 6:
                                                    rosterObject.setHomeCity(rosterFields[j]);
                                                    break;
                                                case 7:
                                                    rosterObject.setHomeState(rosterFields[j]);
                                                    break;
                                                case 8:
                                                    rosterObject.setHomeZip(rosterFields[j]);
                                                    break;
                                                case 9:
                                                    rosterObject.setHomePhone(rosterFields[j]);
                                                    break;
                                                case 10:
                                                    rosterObject.setMobilePhone(rosterFields[j]);
                                                    break;
                                                case 11:
                                                    rosterObject.setEmail(rosterFields[j]);
                                                    break;
                                                case 12:
                                                    rosterObject.setWinterName(rosterFields[j]);
                                                    break;
                                                case 13:
                                                    rosterObject.setWinterAddress1(rosterFields[j]);
                                                    break;
                                                case 14:
                                                    rosterObject.setWinterAddress2(rosterFields[j]);
                                                    break;
                                                case 15:
                                                    rosterObject.setWinterCity(rosterFields[j]);
                                                    break;
                                                case 16:
                                                    rosterObject.setWinterState(rosterFields[j]);
                                                    break;
                                                case 17:
                                                    rosterObject.setWinterZip(rosterFields[j]);
                                                    break;
                                                case 18:
                                                    rosterObject.setWinterPhone(rosterFields[j]);
                                                    break;
                                                case 19:
                                                    rosterObject.setWinterEmail(rosterFields[j]);
                                                    break;
                                                case 20:
                                                    rosterObject.setMemberNumber(rosterFields[j]);
                                                    break;
                                                case 21:
                                                    rosterObject.setStatus(rosterFields[j]);
                                                    break;
                                                case 22:
                                                    rosterObject.setEmergencyName(rosterFields[j]);
                                                    break;
                                                case 23:
                                                    rosterObject.setEmergencyPhoneNumber(rosterFields[j]);
                                                    break;
                                                case 24:
                                                    rosterObject.setActivationDate(rosterFields[j]);
                                                    break;
                                                case 25:
                                                    rosterObject.setGroups(rosterFields[j]);
                                                    break;
                                            }


                                        }


                                        rosterObjects.add(rosterObject);

                                        Log.d(TAG, "rosterObject added ----> " + rosterObject.toString() + "<----");

                                        editor = sharedPreferences.edit();
                                        Gson gson = new Gson();
                                        String jsonRosterObject = gson.toJson(rosterObject); // myObject - instance of MyObject
                                        editor.putString("rosterObject" + "[" + i + "]", jsonRosterObject);
                                        editor.putInt("rosterSize", rosterObjects.size());

                                        editor.apply();


                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }


//*************************************************************************CALENDAR************************************************************************************


                                try {
                                    ParseFile eventFile = associationObject.get(0).getParseFile("EventFile");

                                    String[] eventFileArray = null;


                                    byte[] eventFileData = new byte[0];
                                    try {
                                        eventFileData = eventFile.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                    String eventFileString = null;
                                    try {
                                        eventFileString = new String(eventFileData, "UTF-8");
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }

                                    eventFileArray = eventFileString.split("\\|", -1);


                                    for (String event : eventFileArray) {

                                        event.trim();


                                        Log.v(TAG, "EVENT: " + event);

                                    }

                                    Log.v(TAG, "eventFileArray length: " + eventFileArray.length);


                                    calendarObjects = new ArrayList<CalendarObject>();

                                    for (i = 1, j = 0, k = 0; i < eventFileArray.length; i++) {

                                        switch (j) {
                                            case 0:
                                                calendarObject = new CalendarObject();
                                                calendarObject.setCalendarText(eventFileArray[i]);
                                                j = j + 1;
                                                break;
                                            case 1:
                                                calendarObject.setCalendarDetailText(eventFileArray[i]);
                                                j = j + 1;
                                                break;
                                            case 2:
                                                calendarObject.setCalendarStartDate(eventFileArray[i]);
                                                j = j + 1;
                                                break;
                                            case 3:
                                                calendarObject.setCalendarEndDate(eventFileArray[i]);
                                                j = j + 1;
                                                break;
                                            case 4:
                                                calendarObject.setCalendarSortDate(eventFileArray[i]);
                                                calendarObjects.add(calendarObject);

                                                Log.v(TAG, "calendarObject ----> " + calendarObject.toString());

                                                editor = sharedPreferences.edit();
                                                Gson gson = new Gson();
                                                String jsonCalendarObject = gson.toJson(calendarObject); // myObject - instance of MyObject
                                                editor.putString("calendarObject" + "[" + k + "]", jsonCalendarObject);
                                                editor.putString("calendarSize", String.valueOf(calendarObjects.size()));
                                                editor.apply();

                                                j = 0;
                                                k++;

                                                break;
                                        }


                                    }

                                    // Will invoke overrided `toString()` method


                                    // Log.d(TAGC, "calendarObjects size is " + calendarObjects.size());

                                    for (CalendarObject object : calendarObjects) {
                                        Log.i(TAG, object.toString());
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

//**************************************************************************PROVIDER************************************************************************************
                                try {
                                    if (!sharedPreferences.getString("defaultRecord(47)", "No").equals("No")) {


                                        ParseFile providerFile = associationObject.get(0).getParseFile("ProviderFile");

                                        ArrayList<String> providerType = new ArrayList();
                                        String[] providerFileArray = null;


                                        byte[] providerFileData = new byte[0];
                                        try {
                                            providerFileData = providerFile.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        String providerFileString = null;
                                        try {
                                            providerFileString = new String(providerFileData, "UTF-8");
                                        } catch (UnsupportedEncodingException e2) {
                                            e2.printStackTrace();
                                        }
                                        Log.v(TAG, "providerFileString -----> " + providerFileString);

                                        providerFileArray = providerFileString.split("\\|", -1);

                                        Log.v(TAG, "providerFileArrayLength -----> " + providerFileArray.length);

                                        for (String providerField : providerFileArray) {

                                            Log.v(TAG, "providerField -----> " + providerField);


                                        }


                                        providerObjects = new ArrayList<ProviderObject>();


                                        for (i = 0, j = 0; i < providerFileArray.length; i++) {


                                            switch (j) {
                                                case 0:
                                                    providerObject = new ProviderObject();
                                                    providerObject.setProviderType(providerFileArray[i]);
                                                    j++;
                                                    Log.d(TAG, "provider type is " + providerFileArray[i]);
                                                    break;
                                                case 1:
                                                    providerObject.setProviderCount(providerFileArray[i]);
                                                    j++;
                                                    Log.d(TAG, "provider count is " + providerFileArray[i]);
                                                    break;
                                                case 2:
                                                    providerObject.setProviderList(providerFileArray[i]);
                                                    providerObjects.add(providerObject);
                                                    j = 0;

                                                    editor = sharedPreferences.edit();
                                                    Gson gson = new Gson();
                                                    String jsonProviderObject = gson.toJson(providerObject); // myObject - instance of MyObject
                                                    editor.putString("providerObject" + "[" + ((i + 1) / 3 - 1) + "]", jsonProviderObject);
                                                    editor.putString("providerSize", String.valueOf(providerObjects.size()));
                                                    editor.apply();

                                            }

                                        }


                                        Log.d(TAG, "providerObjects size is " + providerObjects.size());

                                        for (ProviderObject object : providerObjects) {
                                            Log.i(TAG, object.toString());
                                        }
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

//***************************************************************************MESSAGE***********************************************************************************


                                try {
                                    ParseFile postsFile = associationObject.get(0).getParseFile("MessageFile");

                                    String[] postFileArray = null;


                                    byte[] postFileData = new byte[0];
                                    try {
                                        postFileData = postsFile.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                    String postsFileString = null;
                                    try {
                                        postsFileString = new String(postFileData, "UTF-8");
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }

                                    Log.v(TAG, "postsFileString ---> " + postsFileString);

                                    postFileArray = postsFileString.split("\\|", -1);


                                    for (int i = 0; i < postFileArray.length; i++) {


                                        Log.v(TAG, i + " post " + postFileArray[i]);


                                    }


                                    mbObjects = new ArrayList<MBObject>();

                                    for (i = 0, j = 0; i < postFileArray.length; i++) {

                                        switch (j) {
                                            case 0:
                                                mbObject = new MBObject();
                                                mbObject.setMbName(postFileArray[i]);
                                                j++;
                                                break;
                                            case 1:
                                                mbObject.setMbPostDate(postFileArray[i]);
                                                j++;
                                                break;
                                            case 2:
                                                mbObject.setMbPost(postFileArray[i]);
                                                j++;
                                                break;
                                            case 3:
                                                mbObject.setMbPostDate2(postFileArray[i]);
                                                j++;
                                                break;

                                            case 4:
                                                mbObject.setMbPosterEmailAddress(postFileArray[i]);
                                                mbObjects.add(mbObject);
                                                editor = sharedPreferences.edit();
                                                Gson gson = new Gson();
                                                String jsonMbObject = gson.toJson(mbObject); // myObject - instance of MyObject
                                                editor.putString("mbObject" + "[" + (((i + 1) / 5) - 1) + "]", jsonMbObject);
                                                editor.apply();

                                                j = 0;

                                                break;
                                        }


                                    }

                                    // Will invoke overrided `toString()` method


                                    // Log.d(TAG, "oldMbSize ---------> " + String.valueOf(sharedPreferences.getInt("mbSize", 0)));
                                    // Log.d(TAG, "newMbSize mbObjects.size ---------> " + String.valueOf(mbObjects.size()));


                                    if (sharedPreferencesVisited.getInt("mbSize", 0) < mbObjects.size()) {


                                        redDot.setVisibility(View.VISIBLE);

                                        editor = sharedPreferencesVisited.edit();
                                        editor.putInt("mbSize", mbObjects.size());
                                        editor.apply();

                                        // Log.d(TAG, "red dot visible");


                                    } else {

                                        editor = sharedPreferencesVisited.edit();
                                        editor.putInt("mbSize", mbObjects.size());
                                        editor.apply();

                                        // Log.d(TAG, "red dot gone");

                                    }


                                    // Log.d(TAGMBPOST, "mbObjects size is " + mbObjects.size());

                                    for (MBObject object : mbObjects) {
                                        Log.i(TAG, object.toString());
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }


                                try {
                                    ParseFile maintenanceCategoryFile = associationObject.get(0).getParseFile("MaintenanceCategoryFile");

                                    String[] maintenanceCategoryFileArray;



                                    byte[] mcFileData = new byte[0];
                                    try {
                                        mcFileData = maintenanceCategoryFile.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                    String maintenanceCategoryFileString = null;
                                    try {
                                        maintenanceCategoryFileString = new String(mcFileData, "UTF-8");
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }
                                    Log.d(TAG, "maintenanceCategoryFileString --->" + maintenanceCategoryFileString);
                                    maintenanceCategoryFileArray = maintenanceCategoryFileString.split("\\|", -1);


                                    maintenanceCategoryObjects = new ArrayList<>();

                                    i = 0;
                                    j = 0;
                                    for (String maintenanceCatItem : maintenanceCategoryFileArray) {

                                        Log.d(TAG, "maintenanceCategoryObject item --->" + maintenanceCatItem + " i = " + i + " j = " + j);

                                        switch (j) {
                                            case 0:
                                                maintenanceCategoryObject = new MaintenanceCategoryObject();
                                                maintenanceCategoryObject.setMaintenanceCatName(maintenanceCatItem);
                                                j++;
                                                break;
                                            case 1:
                                                maintenanceCategoryObject.setMaintenanceCatEmail(maintenanceCatItem);
                                                maintenanceCategoryObjects.add(maintenanceCategoryObject);
                                                Log.d(TAG, "maintenanceCategoryObject --->" + maintenanceCategoryObject.toString());

                                                editor = sharedPreferences.edit();
                                                Gson gson = new Gson();
                                                String jsonMaintenanceCategoryObject = gson.toJson(maintenanceCategoryObject); // myObject - instance of MyObject
                                                editor.putString("maintenanceCategoryObject" + "[" + (maintenanceCategoryObjects.size() - 1) + "]", jsonMaintenanceCategoryObject);
                                                editor.putInt("maintenanceCategoryObjectsSize", maintenanceCategoryObjects.size());
                                                editor.apply();

                                                j = 0;

                                                break;
                                         }


                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }


                                try {
                                    ParseFile maintenanceFile = associationObject.get(0).getParseFile("MaintenanceFile");

                                    String[] maintenanceFileArray;
                                    String[] maintenanceItems;


                                    byte[] mFileData = new byte[0];
                                    try {
                                        mFileData = maintenanceFile.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                    String maintenanceFileString = null;
                                    try {
                                        maintenanceFileString = new String(mFileData, "UTF-8");
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }
                                    Log.d(TAG, "maintenanceFileString --->" + maintenanceFileString);
                                    maintenanceFileArray = maintenanceFileString.split("\\|", -1);


                                    maintenanceObjects = new ArrayList<>();

                                    i = 0;
                                    for (String maintenanceMember : maintenanceFileArray) {

                                        maintenanceItems = maintenanceMember.split("\\^", -1);
                                        maintenanceObject = new MaintenanceObject();
                                        maintenanceObject.setMaintenanceName(maintenanceItems[0]);
                                        maintenanceObject.setMaintenanceDate(maintenanceItems[1]);
                                        maintenanceObject.setMaintenanceDesc(maintenanceItems[2]);
                                        maintenanceObject.setMaintenanceNotes(maintenanceItems[3]);
                                        maintenanceObject.setMaintenanceCategory(maintenanceItems[4]);
                                        maintenanceObjects.add(maintenanceObject);
                                        Log.d(TAG, "maintenanceObject --->" + maintenanceObject.toString());

                                        editor = sharedPreferences.edit();
                                        Gson gson = new Gson();
                                        String jsonMaintenanceObject = gson.toJson(maintenanceObject); // myObject - instance of MyObject
                                        editor.putString("maintenanceObject" + "[" + i + "]", jsonMaintenanceObject);
                                        editor.putString("maintenanceObjectsSize", String.valueOf(maintenanceObjects.size()));
                                        editor.apply();

                                        i++;


                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }


//******************************************************************************GUESTS********************************************************************************

                                try {
                                    ParseFile guestFile = associationObject.get(0).getParseFile("GuestFile");

                                    String[] guestFileArray;
                                    String[] guestItems;


                                    byte[] guestFileData = new byte[0];
                                    try {
                                        guestFileData = guestFile.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                    String guestFileString = null;
                                    try {
                                        guestFileString = new String(guestFileData, "UTF-8");
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }
                                    Log.d(TAG, "guestFileString ---->" + guestFileString);
                                    guestFileArray = guestFileString.split("\\|", -1);


                                    guestObjects = new ArrayList<>();

                                    i = 0;
                                    for (String guest : guestFileArray) {

                                        guestItems = guest.split("\\^", -1);
                                        guestObject = new GuestObject();
                                        guestObject.setGuestOwner(guestItems[0]);
                                        guestObject.setGuestOwnerMemberNumber(guestItems[1]);
                                        guestObject.setGuestType(guestItems[2]);
                                        guestObject.setGuestStartdate(guestItems[3]);
                                        guestObject.setGuestEnddate(guestItems[4]);
                                        guestObject.setMondayAccess(guestItems[5]);
                                        guestObject.setTuesdayAccess(guestItems[6]);
                                        guestObject.setWednesdayAccess(guestItems[7]);
                                        guestObject.setThursdayAccess(guestItems[8]);
                                        guestObject.setFridayAccess(guestItems[9]);
                                        guestObject.setSaturdayAccess(guestItems[10]);
                                        guestObject.setSundayAccess(guestItems[11]);
                                        guestObject.setGuestDescription(guestItems[12]);
                                        guestObject.setGuestName(guestItems[13]);
                                        guestObject.setOwnerContactNumberType(guestItems[14]);
                                        guestObject.setOwnerContactNumber(guestItems[15]);
                                        guestObjects.add(guestObject);

                                        Log.d(TAG, "guestObject --->" + guestObject.toString());

                                        editor = sharedPreferences.edit();
                                        Gson gson = new Gson();
                                        String jsonGuestObject = gson.toJson(guestObject); // myObject - instance of MyObject
                                        editor.putString("guestObject" + "[" + i + "]", jsonGuestObject);
                                        editor.putInt("guestObjectsSize", guestObjects.size());
                                        editor.apply();

                                        i++;


                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }


//***********************************************************************PUSH***************************************************************************************


                                try {
                                    ParseFile pushHistoryFile = associationObject.get(0).getParseFile("PushFile");

                                    String[] pushHistoryFileArray = null;
                                    String[] pushHistoryFileArray2 = null;


                                    byte[] pushFileData = new byte[0];
                                    try {
                                        pushFileData = pushHistoryFile.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                    String pushHistoryFileString = null;
                                    try {
                                        pushHistoryFileString = new String(pushFileData, "UTF-8");
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }

                                    pushHistoryFileArray = pushHistoryFileString.split("\\|");


                                    for (String pushMessage : pushHistoryFileArray) {

                                        pushMessage.trim();


                                        // Log.v(PM, "push message: " + pushMessage);

                                    }

                                    // Log.v(PM, "pushHistoryFileArray length: " + pushHistoryFileArray.length);

                                    pushObjects = new ArrayList<PushObject>();
                                    pushObjectsMember = new ArrayList<PushObject>();


                                    for (i = 1, j = 1, k = 0; i < pushHistoryFileArray.length; i++) {

                                        pushHistoryFileArray2 = pushHistoryFileArray[i].split("\\^");

                                        for (String pf2member : pushHistoryFileArray2) {
                                            // Log.d(PM, "member is ---> " + pf2member + " i= " + i + " j= " + j);

                                            switch (j) {
                                                case 1:
                                                    j = j + 1;
                                                    break;
                                                case 2:
                                                    j = j + 1;
                                                    break;
                                                case 3:
                                                    pushObject = new PushObject();
                                                    pushObject.setDate(pf2member);
                                                    j = j + 1;
                                                    break;
                                                case 4:
                                                    pushObject.setPushNotifacation(pf2member);

                                                    pushObjects.add(pushObject);
                                                    editor = sharedPreferences.edit();
                                                    Gson gson = new Gson();
                                                    String jsonPushObject = gson.toJson(pushObject); // myObject - instance of MyObject
                                                    editor.putString("pushObject" + "[" + (i - 1) + "]", jsonPushObject);
                                                    editor.putInt("pushObjectsSize", pushObjects.size());
                                                    editor.commit();
                                                    j = 1;



                                                    if ( !pf2member.contains("Admin group only")) {

                                                        pushObjectsMember.add(pushObject);
                                                        editor = sharedPreferences.edit();
                                                        gson = new Gson();
                                                        jsonPushObject = gson.toJson(pushObject); // myObject - instance of MyObject
                                                        editor.putString("pushObjectMember" + "[" + k + "]", jsonPushObject);
                                                        editor.putInt("pushObjectsMemberSize", pushObjectsMember.size());
                                                        editor.commit();
                                                        k++;
                                                        j = 1;

                                                    }


                                            }


                                        }

                                    }


                                    // Log.d(PM, "pushObjects size is " + pushObjects.size());

                                    for (PushObject object : pushObjects) {
                                         Log.i(TAG, object.toString());
                                    }


                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }


//***********************************************************************PETS*************************************************************************************************

                                try {
                                    if (!sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {


                                        ParseFile petFile = associationObject.get(0).getParseFile("PetFile");

                                        String[] petFileArray = null;
                                        String[] petFileArray2 = null;


                                        byte[] petFileData = new byte[0];
                                        try {
                                            petFileData = petFile.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        String petFileString = null;
                                        try {
                                            petFileString = new String(petFileData, "UTF-8");
                                        } catch (UnsupportedEncodingException e2) {
                                            e2.printStackTrace();
                                        }

                                        petFileArray = petFileString.split("\\|", -1);


                                        for (String pet : petFileArray) {

                                            pet.trim();


                                             Log.v(TAG, "pet: " + pet);

                                        }

                                         Log.v(TAG, "petFileArray length: " + petFileArray.length);

                                        petObjects = new ArrayList<PetObject>();

                                        for (i = 0, j = 1; i < petFileArray.length; i++) {

                                            petFileArray2 = petFileArray[i].split("\\^", -1);

                                            for (String petField : petFileArray2) {
                                                 Log.d(TAG, "pet is ---> " + petField + " i = " + i + " j = " + j);

                                                switch (j) {
                                                    case 1:
                                                        petObject = new PetObject();
                                                        petObject.setOwner(petField);
                                                        j = j + 1;
                                                        break;
                                                    case 2:
                                                        petObject.setMemberNumber(petField);
                                                        j = j + 1;
                                                        break;
                                                    case 3:
                                                        petObject.setName(petField);
                                                        j = j + 1;
                                                        break;
                                                    case 4:
                                                        petObject.setType(petField);
                                                        j = j + 1;
                                                        break;
                                                    case 5:
                                                        petObject.setBreed(petField);
                                                        j = j + 1;
                                                        break;
                                                    case 6:
                                                        petObject.setColor(petField);
                                                        j = j + 1;
                                                        break;
                                                    case 7:
                                                        petObject.setWeight(petField);
                                                        j = j + 1;
                                                        break;
                                                    case 8:
                                                        petObject.setMisc(petField);
                                                        petObjects.add(petObject);

                                                        editor = sharedPreferences.edit();
                                                        Gson gson = new Gson();
                                                        String jsonPetObject = gson.toJson(petObject); // myObject - instance of MyObject
                                                        editor.putString("petObject" + "[" + i + "]", jsonPetObject);
                                                        editor.putInt("petObjectsSize", petObjects.size());
                                                        editor.commit();
                                                        j = 1;


                                                }


                                            }

                                        }


                                        // Log.d(PM, "petObjects size is " + petObjects.size());

                                        for (PetObject object : petObjects) {
                                            // Log.i(PM, object.toString());
                                        }

                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
//************************************************************AUTOS**************************************************************************************************************

                                try {
                                    if (!sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {


                                        ParseFile autoFile = associationObject.get(0).getParseFile("AutoFile");

                                        String[] autoFileArray = null;
                                        String[] autoFileArray2 = null;


                                        byte[] autoFileData = new byte[0];
                                        try {
                                            autoFileData = autoFile.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        String autoFileString = null;
                                        try {
                                            autoFileString = new String(autoFileData, "UTF-8");
                                        } catch (UnsupportedEncodingException e2) {
                                            e2.printStackTrace();
                                        }

                                        autoFileArray = autoFileString.split("\\|", -1);


                                        for (String auto : autoFileArray) {

                                            auto.trim();


                                            Log.v(TAG, "auto ---> " + auto);

                                        }

                                        Log.v(TAG, "autoFileArray length ---> " + autoFileArray.length);

                                        autoObjects = new ArrayList<AutoObject>();

                                        for (i = 0, j = 0; i < autoFileArray.length; i++) {

                                            autoFileArray2 = autoFileArray[i].split("\\^", -1);

                                            for (String autoField : autoFileArray2) {

                                                autoField = autoField.trim();

                                                switch (j) {
                                                    case 0:
                                                        autoObject = new AutoObject();
                                                        autoObject.setOwner(autoField);
                                                        j++;
                                                        break;
                                                    case 1:
                                                        autoObject.setMemberNumber(autoField);
                                                        j++;
                                                        break;
                                                    case 2:
                                                        autoObject.setMake(autoField);
                                                        j++;
                                                        break;
                                                    case 3:
                                                        autoObject.setModel(autoField);
                                                        j++;
                                                        break;
                                                    case 4:
                                                        autoObject.setColor(autoField);
                                                        j++;
                                                        break;
                                                    case 5:
                                                        autoObject.setYear(autoField);
                                                        j++;
                                                        break;
                                                    case 6:
                                                        autoObject.setPlate(autoField);
                                                        j++;
                                                        break;
                                                    case 7:
                                                        autoObject.setTag(autoField);
                                                        autoObjects.add(autoObject);

                                                        editor = sharedPreferences.edit();
                                                        Gson gson = new Gson();
                                                        String jsonAutoObject = gson.toJson(autoObject); // myObject - instance of MyObject
                                                        editor.putString("autoObject" + "[" + i + "]", jsonAutoObject);
                                                        editor.putInt("autosObjectsSize", autoObjects.size());
                                                        editor.apply();
                                                        j = 0;


                                                }


                                            }

                                        }


                                        // Log.d(PM, "autoObjects size is " + autoObjects.size());

                                        for (AutoObject object : autoObjects) {
                                            Log.i(TAG, object.toString());
                                        }


                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
//************************************************************************PDFS**************************************************************************************************


                                ParseFile budgetFile = associationObject.get(0).getParseFile("BudgetFile");
                                ParseFile byLawsFile = associationObject.get(0).getParseFile("ByLawsFile");
                                ParseFile expenseFile = associationObject.get(0).getParseFile("ExpenseFile");
                                ParseFile rulesFile = associationObject.get(0).getParseFile("RulesFile");
                                ParseFile minutesFile = associationObject.get(0).getParseFile("MinutesFile");
                                ParseFile misc1File = associationObject.get(0).getParseFile("MiscDoc1File");
                                ParseFile misc2File = associationObject.get(0).getParseFile("MiscDoc2File");
                                ParseFile misc3File = associationObject.get(0).getParseFile("MiscDoc3File");




                                try {
                                    editor.putString("budgetpdfurl", budgetFile.getUrl());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    // Log.d(TAG, "no such pdf file ");
                                    editor.putString("budgetpdfurl", null);
                                }
                                try {
                                    editor.putString("bylawspdfurl", byLawsFile.getUrl());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    // Log.d(TAG, "no such pdf file ");
                                    editor.putString("bylawspdfurl", null);
                                }
                                try {
                                    editor.putString("expensepdfurl", expenseFile.getUrl());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    // Log.d(TAG, "no such pdf file ");
                                    editor.putString("expensepdfurl", null);
                                }
                                try {
                                    editor.putString("rulespdfurl", rulesFile.getUrl());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    // Log.d(TAG, "no such pdf file ");
                                    editor.putString("rulespdf", null);
                                }
                                try {
                                    editor.putString("minutespdfurl", minutesFile.getUrl());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    // Log.d(TAG, "no such pdf file ");
                                    editor.putString("minutespdfurl", null);
                                }
                                try {
                                    editor.putString("m1pdfurl", misc1File.getUrl());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    // Log.d(TAG, "no such pdf file ");
                                }
                                try {
                                    editor.putString("m2pdfurl", misc2File.getUrl());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    // Log.d(TAG, "no such pdf file ");
                                    editor.putString("m2pdfurl", null);
                                }
                                try {
                                    editor.putString("m3pdfurl", misc3File.getUrl());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    // Log.d(TAG, "no such pdf file ");
                                    editor.putString("m3pdfurl", null);
                                }

                                editor.apply();




//***************************************************************************************************************************************************************************
                                Map<String, ?> keys = sharedPreferences.getAll();
                                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                                    Log.d(TAG, "map values MYPREFERENCES" + entry.getKey() + ": " + entry.getValue().toString());
                                }

                                keys = sharedPreferencesVisited.getAll();
                                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                                    Log.d(TAG, "map values VISITEDPREFERENCES " + entry.getKey() + ": " + entry.getValue().toString());
                                }
//****************************************************************************************************************************************************************************




                            } else {
                                Log.d(TAG, "Parse Exception");
                            }


                        }
                    });


            return null;
        }

        }





    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (sharedPreferences.getBoolean("visitedBefore", false)) {

            try {
                queryAssociations = new ParseQuery<>(installation.getString("AssociationCode"));
            } catch (Exception e) {
                queryAssociations = new ParseQuery<>(installation.getString("AssociationCode")).fromLocalDatastore();
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onBackPressed() {

        this.finish();
    }





}

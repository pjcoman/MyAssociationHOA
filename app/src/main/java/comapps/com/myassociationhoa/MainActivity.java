package comapps.com.myassociationhoa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.parse.SaveCallback;

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
import comapps.com.myassociationhoa.objects.AdminMBObject;
import comapps.com.myassociationhoa.objects.MBObject;
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

    MBObject mbObject;
    private ArrayList<MBObject> mbObjects;

    AdminMBObject admin_mbObject;
    private ArrayList<AdminMBObject> admin_mbObjects;



    private ParseInstallation installation;
    ParseQuery<ParseObject> queryAssociations;

    int messageCount = 0;



    String url;


    ImageLoader imageLoader;



    private String memberName;
    private String memberType;
    private String memberNumber;
    private String associationCode;
    private TextView associationName;

    Button b1_email; //email button
    Button b2_contacts; //contact button
    Button b3_directory; //directory button
    Button b4_weather; //weather button
    Button b5_calendar; //calendar button
    Button b6_budget; //budget button
    Button b7_documents; //documents button
    Button b8_messageboard; //message board button
    Button b9_myinfo; //my info button
    Button b10_pushhistory; //push history button
    Button b11_serviceproviders; //service providers button
    Button b12_changeadd; //change add assoc button
    Button b13_pets; //pets directory button
    Button b14_guests; //guests directory button
    Button b15_autos; //auto directory button
    Button b16_tools; //tools button
    Button b17_pushemail; //push email button
    Button b18_maintenance; //maintenance items button
    Button b18_maintenance_b;

    LinearLayout contentMain;

    LinearLayout ll1;
    LinearLayout ll2;
    LinearLayout ll3;

    Boolean mbVisit = false;

    ImageView redDot;

    int i = 0;
    int j = 0;
    int k = 0;

    int oldMBSize;

    boolean objectPinned;
    boolean fromChangeAdd;
    boolean fromImport;
    Bundle bundle;


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

        b1_email = (Button) findViewById(R.id.button);
        b2_contacts = (Button) findViewById(R.id.button2);
        b3_directory = (Button) findViewById(R.id.button3);
        b4_weather = (Button) findViewById(R.id.button4);
        b5_calendar = (Button) findViewById(R.id.button5);
        b6_budget = (Button) findViewById(R.id.button6);
        b7_documents = (Button) findViewById(R.id.button7);
        b8_messageboard = (Button) findViewById(R.id.button8);
        b9_myinfo = (Button) findViewById(R.id.button9);
        b10_pushhistory = (Button) findViewById(R.id.button10);
        b11_serviceproviders = (Button) findViewById(R.id.button11);
        b12_changeadd = (Button) findViewById(R.id.button12);
        b13_pets = (Button) findViewById(R.id.button13);
        b14_guests = (Button) findViewById(R.id.button14);
        b15_autos = (Button) findViewById(R.id.button15);
        b16_tools = (Button) findViewById(R.id.button16);
        b17_pushemail = (Button) findViewById(R.id.button17);
        b18_maintenance = (Button) findViewById(R.id.button18);
        b18_maintenance_b = (Button) findViewById(R.id.button18_b);


        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);

        installation = ParseInstallation.getCurrentInstallation();



        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

     /*   editorVisited = sharedPreferencesVisited.edit();
        editorVisited.putString("ASSOCIATIONS_JOINED", "Android Test HOA^Administrator^Android|Android Test HOA^Member^Android");
        editorVisited.putString("ASSOCIATIONS", "AndroidAdminAndroidMember");
        editorVisited.apply();*/

         Log.d(TAG, "visited before ------> " + sharedPreferences.getBoolean("visitedBefore", false));
        Log.d(TAG, "visited before ------> " + sharedPreferencesVisited.getBoolean("visitedBefore", false));


        if (!sharedPreferencesVisited.getBoolean("visitedBefore", false)) {



            // Log.d(TAG, "visited before ------> " + sharedPreferences.getBoolean("visitedBefore", false));

            Intent enterPasscode = new Intent();
            enterPasscode.setClass(MainActivity.this, PopEnterPasscode.class);
            enterPasscode.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(enterPasscode);



        } else {

            Log.d(TAG, "NOT FIRST VISIT");

            if ( sharedPreferencesVisited.getBoolean("SHOWREDDOT", true) || sharedPreferencesVisited.getBoolean("MBFIRSTVIEW", true)) {

                redDot.setVisibility(View.VISIBLE);


            }

            Log.i(TAG, "message count from visited ----> " + sharedPreferencesVisited.getInt("mbSize", 0));

            bundle = getIntent().getExtras();

            if ( bundle != null ) {

                fromChangeAdd = bundle.getBoolean("FROMCHANGEADD");
                fromImport = bundle.getBoolean("FROMIMPORTDOCS");

                if ( fromImport ) {

                    Toast toast = Toast.makeText(getBaseContext(), bundle.getString("FILEUPLOADED", "pdf") + " uploaded.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }

            Log.d(TAG, "MEMBERTYPE ----> " + installation.getString("MemberType") +  " <----");
            
            if ( installation.getString("MemberType").equals("Resigned") ||
                    installation.getString("MemberType").equals("Guest") || installation.getString("MemberType").equals("Master")  ) {

                b1_email.setEnabled(false);
                b2_contacts.setEnabled(false);
                b3_directory.setEnabled(false);
                b4_weather.setEnabled(false);
                b5_calendar.setEnabled(false);
                b6_budget.setEnabled(false);
                b7_documents.setEnabled(false);
                b8_messageboard.setEnabled(false);
                b9_myinfo.setEnabled(false);
                b10_pushhistory.setEnabled(false);
                b11_serviceproviders.setEnabled(false);
                b12_changeadd.setEnabled(false);
                b13_pets.setEnabled(false);
                b14_guests.setEnabled(false);
                b15_autos.setEnabled(false);
                b16_tools.setEnabled(false);
                b17_pushemail.setEnabled(false);
                b18_maintenance.setEnabled(false);
                b18_maintenance_b.setEnabled(false);

                Toast toast = Toast.makeText(getBaseContext(), "NOT REGISTERED", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                
                
            } else if ( installation.getString("MemberType").equals("Security")){

                b1_email.setEnabled(true);
                b2_contacts.setEnabled(true);
                b3_directory.setEnabled(true);
                b4_weather.setEnabled(false);
                b5_calendar.setEnabled(false);
                b6_budget.setEnabled(false);
                b7_documents.setEnabled(false);
                b8_messageboard.setEnabled(false);
                b9_myinfo.setEnabled(false);
                b10_pushhistory.setEnabled(false);
                b11_serviceproviders.setEnabled(false);
                b12_changeadd.setEnabled(true);
                b13_pets.setEnabled(true);
                b14_guests.setEnabled(true);
                b15_autos.setEnabled(true);
                b16_tools.setEnabled(false);
                b17_pushemail.setEnabled(false);
                b18_maintenance.setEnabled(false);
                b18_maintenance_b.setEnabled(false);

            }


            new RemoteDataTask().execute();



        }

        b1_email.setOnClickListener(this);
        b2_contacts.setOnClickListener(this);
        b3_directory.setOnClickListener(this);
        b4_weather.setOnClickListener(this);
        b5_calendar.setOnClickListener(this);
        b6_budget.setOnClickListener(this);
        b7_documents.setOnClickListener(this);
        b8_messageboard.setOnClickListener(this);
        b9_myinfo.setOnClickListener(this);
        b10_pushhistory.setOnClickListener(this);
        b11_serviceproviders.setOnClickListener(this);
        b12_changeadd.setOnClickListener(this);
        b13_pets.setOnClickListener(this);
        b14_guests.setOnClickListener(this);
        b15_autos.setOnClickListener(this);
        b16_tools.setOnClickListener(this);
        b17_pushemail.setOnClickListener(this);
        b18_maintenance.setOnClickListener(this);
        b18_maintenance_b.setOnClickListener(this);



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
                finish();
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

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class RemoteDataTask extends AsyncTask<Void, Void, Void> {






        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            installation = ParseInstallation.getCurrentInstallation();

            memberType = installation.getString("MemberType");
            associationCode = installation.getString("AssociationCode");
            memberName = installation.getString("memberName");
            memberNumber = installation.getString("memberNumber");

            editorVisited = sharedPreferencesVisited.edit();
            editorVisited.putString("MEMBERNUMBER", memberNumber);
            editorVisited.putString("MEMBERTYPE", memberType);
            editorVisited.putString("MEMBERNAME", memberName);
            editorVisited.putString("ASSOCIATIONCODE", associationCode);
            editorVisited.apply();

            Log.d(TAG, "associationCode is " + associationCode);


        }

        @Override
        protected Void doInBackground(Void... params) {



                if ( objectPinned && !fromChangeAdd) {
                    Log.d(TAG, "objectPinned is " + objectPinned);
                    queryAssociations = new ParseQuery<>(associationCode).fromLocalDatastore();
                } else {
                    Log.d(TAG, "objectPinned is " + objectPinned);
                    queryAssociations = new ParseQuery<>(associationCode);
                }



            queryAssociations.findInBackground(new FindCallback<ParseObject>() {
                        public void done(final List<ParseObject> associationObject, ParseException e) {

                            if (e == null) {



                                ParseObject.unpinAllInBackground(associationObject, new DeleteCallback() {
                                    public void done(ParseException e) {
                                        // Cache the new results.



                                        ParseObject.pinAllInBackground(associationCode, associationObject, new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                                objectPinned = true;





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
                                                if(url.length() > 0) {
                                                    niv.setImageUrl(url, imageLoader);
                                                }



                                                editor = sharedPreferences.edit();
                                                editor.putString("backgroundImageUrl", backgroundImage.getUrl());
                                                editor.putString("backgroundImage2Url", backgroundImage2.getUrl());

                                                editor.apply();







//****************************************************************DEFAULTS************************************************************************************************


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
                                                    editor.putString("defaultRecord(" + Integer.toString(i) + ")", defaultsFileArray[i].trim());
                                                    editor.apply();
                                                }

                                                associationName.setText(sharedPreferences.getString("defaultRecord(30)", ""));



                                                switch (memberType) {


                                                    case "Member":
                                                        b14_guests.setVisibility(View.GONE);
                                                        b16_tools.setVisibility(View.GONE);
                                                        b17_pushemail.setVisibility(View.GONE);

                                                        ll1.setWeightSum(5);
                                                        ll2.setWeightSum(5);
                                                        ll3.setWeightSum(5);



                                                        if (sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {
                                                            if (b13_pets != null) {
                                                                b13_pets.setVisibility(View.GONE);
                                                            } else {
                                                                b13_pets.setVisibility(View.VISIBLE);


                                                            }
                                                        }

                                                        if (sharedPreferences.getString("defaultRecord(35)", "No").equals("No") && memberType.equals("Member")) {

                                                            b13_pets.setVisibility(View.GONE);

                                                        }




                                                        // check defaults for auto

                                                        if (sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {
                                                            if (b15_autos != null) {
                                                                b15_autos.setVisibility(View.GONE);
                                                            } else {
                                                                b15_autos.setVisibility(View.VISIBLE);


                                                            }
                                                        }

                                                        if (sharedPreferences.getString("defaultRecord(37)", "No").equals("No") && memberType.equals("Member")) {

                                                            b15_autos.setVisibility(View.GONE);

                                                        }


                                                        // check defaults for guests

                                                        if (sharedPreferences.getString("defaultRecord(39)", "No").equals("No")) {
                                                            if (b14_guests != null) {
                                                                b14_guests.setVisibility(View.GONE);
                                                            } else {
                                                                b14_guests.setVisibility(View.VISIBLE);
                                                            }
                                                        }

                                                        // check default for service providers

                                                        if (sharedPreferences.getString("defaultRecord(43)", "No").equals("No")) {
                                                            if (b11_serviceproviders != null) {
                                                                b11_serviceproviders.setVisibility(View.GONE);
                                                            } else {
                                                                b11_serviceproviders.setVisibility(View.VISIBLE);
                                                            }
                                                        }

                                                        // check default for service providers

                                                        if (sharedPreferences.getString("defaultRecord(46)", "No").equals("No")) {
                                                            if (b18_maintenance != null) {
                                                                b18_maintenance.setVisibility(View.GONE);
                                                            } else {
                                                                b18_maintenance.setVisibility(View.VISIBLE);
                                                            }
                                                        }


         /* if ( b15_autos.getVisibility() == View.GONE && b13_pets.getVisibility() == View.GONE && b18_maintenance.getVisibility()
                                                        == View.GONE) {

                                                    ll1.setWeightSum(4);
                                                    ll2.setWeightSum(4);
                                                    ll3.setWeightSum(4);

                                                }

*/







                                                        break;
                                                    case "Administrator":


                                                        b13_pets.setVisibility(View.VISIBLE);
                                                        b14_guests.setVisibility(View.VISIBLE);
                                                        b15_autos.setVisibility(View.VISIBLE);
                                                        b16_tools.setVisibility(View.VISIBLE);
                                                        b17_pushemail.setVisibility(View.VISIBLE);
                                                        b18_maintenance.setVisibility(View.VISIBLE);




                                                        break;

                                                    case "Master":


                                                        b13_pets.setVisibility(View.VISIBLE);
                                                        b14_guests.setVisibility(View.VISIBLE);
                                                        b15_autos.setVisibility(View.VISIBLE);
                                                        b16_tools.setVisibility(View.VISIBLE);
                                                        b17_pushemail.setVisibility(View.VISIBLE);
                                                        b18_maintenance.setVisibility(View.VISIBLE);




                                                        break;


                                                }





                                                TextView associationName = (TextView) findViewById(R.id.textViewAssociationName);
                                                associationName.setText(defaultsFileArray[1]);

                                                contentMain.setVisibility(View.VISIBLE);


                                                ll1.setVisibility(View.VISIBLE);
                                                ll2.setVisibility(View.VISIBLE);
                                                ll3.setVisibility(View.VISIBLE);



                                                AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                                                remoteDataTaskClass.execute();



                                                Map<String, ?> keysVisited = sharedPreferencesVisited.getAll();
                                                for (Map.Entry<String, ?> entry : keysVisited.entrySet()) {
                                                    Log.d(TAG, "map values VISITEDPREFERENCES " + entry.getKey() + ": " + entry.getValue().toString());
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
                                                                messageCount++;
                                                                editor = sharedPreferences.edit();
                                                                Gson gson = new Gson();
                                                                String jsonMbObject = gson.toJson(mbObject); // myObject - instance of MyObject
                                                                editor.putString("mbObject" + "[" + (((i + 1) / 5) - 1) + "]", jsonMbObject);
                                                                editor.putInt("mbSize", postFileArray.length/5);
                                                                editor.apply();





                                                                j = 0;




                                                                break;
                                                        }





                                                    }

                                                    if ( mbObjects.size() > sharedPreferencesVisited.getInt("mbSize", 0) && mbVisit == false) {
                                                        redDot.setVisibility(View.VISIBLE);

                                                        mbVisit = true;

                                                        editorVisited = sharedPreferencesVisited.edit();
                                                        editorVisited.putBoolean("SHOWREDDOT", true);
                                                        editorVisited.apply();

                                                    } else if ( mbVisit == true ) {

                                                        editorVisited = sharedPreferencesVisited.edit();
                                                        editorVisited.putInt("mbSize", mbObjects.size());
                                                        editorVisited.apply();
                                                    }

                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                }



//***************************************************************************ADMIN MESSAGE**********************************************************************************


                                                try {
                                                    ParseFile admin_postsFile = associationObject.get(0).getParseFile("AdminMessageFile");


                                                    String[] admin_postFileArray = null;


                                                    byte[] admin_postFileData = new byte[0];
                                                    try {
                                                        admin_postFileData = admin_postsFile.getData();
                                                    } catch (ParseException e1) {
                                                        e1.printStackTrace();
                                                    }

                                                    String admin_postsFileString = "";
                                                    try {
                                                        admin_postsFileString = new String(admin_postFileData, "UTF-8");
                                                    } catch (UnsupportedEncodingException e2) {
                                                        e2.printStackTrace();
                                                    }

                                                    Log.v(TAG, "adminPostsFileString ---> " + admin_postsFileString);

                                                    admin_postFileArray = admin_postsFileString.split("\\|", -1);


                                     /*   for (int i = 0; i < admin_postFileArray.length; i++) {


                                            Log.v(TAG, i + "admin post ----> " + admin_postFileArray[i]);


                                        }
*/

                                                    admin_mbObjects = new ArrayList<>();

                                                    int adminmessageCount = 0;

                                                    for (i = 0, j = 0; i < admin_postFileArray.length; i++) {

                                                        switch (j) {
                                                            case 0:
                                                                admin_mbObject = new AdminMBObject();
                                                                admin_mbObject.setField1(admin_postFileArray[i].trim());
                                                                j++;
                                                                break;
                                                            case 1:
                                                                admin_mbObject.setField2(admin_postFileArray[i].trim());
                                                                j++;
                                                                break;
                                                            case 2:
                                                                admin_mbObject.setField3(admin_postFileArray[i].trim());
                                                                j++;
                                                                break;
                                                            case 3:
                                                                admin_mbObject.setField4(admin_postFileArray[i].trim());
                                                                j++;
                                                                break;
                                                            case 4:
                                                                admin_mbObject.setField5(admin_postFileArray[i].trim());
                                                                j++;
                                                                break;
                                                            case 5:
                                                                admin_mbObject.setField6(admin_postFileArray[i].trim());
                                                                j++;
                                                                break;
                                                            case 6:
                                                                admin_mbObject.setField7(admin_postFileArray[i].trim());
                                                                j++;
                                                                break;

                                                            case 7:
                                                                admin_mbObject.setField8(admin_postFileArray[i].trim());
                                                                admin_mbObjects.add(admin_mbObject);
                                                                adminmessageCount++;
                                                                editor = sharedPreferences.edit();
                                                                editorVisited = sharedPreferencesVisited.edit();
                                                                Gson gson = new Gson();
                                                                String jsonAdminMbObject = gson.toJson(admin_mbObject); // myObject - instance of MyObject
                                                                editor.putString("admin_mbObject" + "[" + (((i + 1) / 8) - 1) + "]", jsonAdminMbObject);
                                                                editorVisited.putInt("admin_mbSize", adminmessageCount);
                                                                editorVisited.apply();
                                                                editor.apply();

                                                                j = 0;

                                                                break;
                                                        }


                                                    }

                                                    i = 0;
                                                    for (AdminMBObject object : admin_mbObjects) {
                                                        Log.i(TAG, i + " admin mb post -----> " + object.toString());
                                                        i++;
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
                                                    editor.putString("m1pdfname", misc1File.getName().replaceAll(" ",""));
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                    // Log.d(TAG, "no such pdf file ");
                                                    editor.putString("m1pdfurl", null);
                                                }
                                                try {
                                                    editor.putString("m2pdfurl", misc2File.getUrl());
                                                    editor.putString("m2pdfname", misc2File.getName().replaceAll(" ",""));
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                    // Log.d(TAG, "no such pdf file ");
                                                    editor.putString("m2pdfurl", null);
                                                }
                                                try {
                                                    editor.putString("m3pdfurl", misc3File.getUrl());
                                                    editor.putString("m3pdfname", misc3File.getName().replaceAll(" ",""));
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                    // Log.d(TAG, "no such pdf file ");
                                                    editor.putString("m3pdfurl", null);
                                                }

                                                editor.apply();




                                            }
                                        });

                                    }
                                });


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

        if ( sharedPreferencesVisited.getBoolean("FROMMB", false) ) {

            redDot.setVisibility(View.INVISIBLE);


        }



        if (sharedPreferencesVisited.getBoolean("visitedBefore", false)) {

            new RemoteDataTask().execute();

        }
    }



  /*  @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (sharedPreferencesVisited.getBoolean("visitedBefore", false)) {

            new RemoteDataTask().execute();

        }
    }
*/

    @Override
    public void onBackPressed() {



        this.finish();
    }





}

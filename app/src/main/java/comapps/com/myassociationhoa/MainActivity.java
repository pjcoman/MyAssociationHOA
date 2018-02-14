package comapps.com.myassociationhoa;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import comapps.com.myassociationhoa.pets.PetsActivity;
import comapps.com.myassociationhoa.push_history.PushActivity;
import comapps.com.myassociationhoa.service_providers.ServiceProviderActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MAINACTIVITY";

    private static final String MYPREFERENCES = "MyPrefs";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editorVisited;

    private SimpleDateFormat formatter;


    private ParseInstallation installation;


    private String url;


    private String memberName;
    private String memberType;
    private String associationCode;
    private TextView associationName;

    private Button b1_email; //email button
    private Button b2_contacts; //contact button
    private Button b3_directory; //directory button
    private Button b4_weather; //weather button
    private Button b5_calendar; //calendar button
    private Button b6_budget; //budget button
    private Button b7_documents; //documents button
    private Button b8_messageboard; //message board button
    private Button b9_myinfo; //my info button
    private Button b10_pushhistory; //push history button
    private Button b11_serviceproviders; //service providers button
    private Button b12_changeadd; //change add assoc button
    private Button b13_pets; //pets directory button
    private Button b14_guests; //guests directory button
    private Button b15_autos; //auto directory button
    private Button b16_tools; //tools button
    private Button b17_pushemail; //push email button
    private Button b18_maintenance; //maintenance items button

    private ProgressBar progressBar;

    private FrameLayout frameLayout;


    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;

    private ImageView redDot;


    private boolean objectPinned;
    private boolean fromChangeAdd;

    private Date update;
    private Date visit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            setupWindowAnimations();

        }




        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {


            bar.setTitle("Main Menu Ver " + getResources().getString(R.string.app_version));

        }





        associationName = findViewById(R.id.textViewAssociationName);



        redDot = findViewById(R.id.imageViewRedDot);
        progressBar = findViewById(R.id.progressBar);


        frameLayout = findViewById(R.id.frameLayout);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);



        b1_email = findViewById(R.id.button);
        b2_contacts = findViewById(R.id.button2);
        b3_directory = findViewById(R.id.button3);
        b4_weather = findViewById(R.id.button4);
        b5_calendar = findViewById(R.id.button5);
        b6_budget = findViewById(R.id.button6);
        b7_documents = findViewById(R.id.button7);
        b8_messageboard = findViewById(R.id.button8);
        b9_myinfo = findViewById(R.id.button9);
        b10_pushhistory = findViewById(R.id.button10);
        b11_serviceproviders = findViewById(R.id.button11);
        b12_changeadd = findViewById(R.id.button12);
        b13_pets = findViewById(R.id.button13);
        b14_guests = findViewById(R.id.button14);
        b15_autos = findViewById(R.id.button15);
        b16_tools = findViewById(R.id.button16);
        b17_pushemail = findViewById(R.id.button17);
        b18_maintenance = findViewById(R.id.button18);
        Button b18_maintenance_b = findViewById(R.id.button18_b);




        installation = ParseInstallation.getCurrentInstallation();



        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();



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


            Bundle bundle = getIntent().getExtras();

            if ( bundle != null ) {

                fromChangeAdd = bundle.getBoolean("FROMCHANGEADD");
                boolean fromImport = bundle.getBoolean("FROMIMPORTDOCS");

                if (fromImport) {

                    Toast toast = Toast.makeText(getBaseContext(), bundle.getString("FILEUPLOADED", "pdf") + " uploaded.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }

            Log.d(TAG, "MEMBERTYPE ----> " + installation.getString("MemberType") +  " <----");
            
            if ( installation.getString("MemberType").equals("Resigned") ||
                    installation.getString("MemberType").equals("Guest") || installation.getString("MemberType").equals("Master")) {

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

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                public void run() {

                    frameLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }, 2000);




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

        ActivityOptions options = null;
        try {
            options = ActivityOptions.makeSceneTransitionAnimation(
                    MainActivity.this);
        } catch (Exception e) {


            Log.d(TAG, "sceneTransitionAnimation failed");


            e.printStackTrace();
        }


        Intent intent = new Intent();

        switch (v.getId()) {
            //handle multiple view click events
            case R.id.button:
                sendEmail();
                break;
            case R.id.button2:
                intent.setClass(this, ContactActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button3:
                intent.setClass(this, DirectoryActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button4:
                intent.setClass(this, WeatherActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button5:
                intent.setClass(this, CalendarActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button6:
                intent.setClass(this, BudgetActivity.class);
                startActivity(intent);
                break;
            case R.id.button7:
                intent.setClass(this, DocumentsActivity.class);
                startActivity(intent);
                break;

            case R.id.button8:
                intent.setClass(this, MBActivity.class);
                redDot.setVisibility(View.GONE);
                assert options != null;
                startActivity(intent, options.toBundle());

                break;
            case R.id.button9:
                intent.setClass(this, MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.button10:
                intent.setClass(this, PushActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button11:
                intent.setClass(this, ServiceProviderActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button12:
                intent.setClass(this, Change_Add_Associations_New.class);
                assert options != null;
                startActivity(intent, options.toBundle());

                break;
            case R.id.button13:
                intent.setClass(this, PetsActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button14:
                intent.setClass(this, GuestsActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button15:
                intent.setClass(this, AutosActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button16:
                intent.setClass(this, ToolsActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button17:
                intent.setClass(this, AdminPushActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
            case R.id.button18:
                intent.setClass(this, MaintenanceActivity.class);
                assert options != null;
                startActivity(intent, options.toBundle());
                break;
           /* case R.id.button18_b:
                intent.setClass(this, MaintenanceActivity.class);
                startActivity(intent);
                break;*/
        }


    }





    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            installation = ParseInstallation.getCurrentInstallation();

            memberType = installation.getString("MemberType");
            associationCode = installation.getString("AssociationCode");
            memberName = installation.getString("memberName");
            String memberNumber = installation.getString("memberNumber");

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

            progressBar.setVisibility(View.VISIBLE);

            ParseQuery<ParseObject> queryAssociations;
            if (objectPinned && !fromChangeAdd) {
                Log.d(TAG, "objectPinned is " + true);
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

                                                url = backgroundImage != null ? backgroundImage.getUrl() : null;
                                                ImageView iv = findViewById(R.id.imageView);

                                                try {
                                                    Glide.with(getApplicationContext())
                                                            .load(url)
                                                            .into(iv);
                                                } catch (Exception e1) {

                                                    Log.d(TAG, "Glide failed to load" +
                                                            "background url ---> " + url);
                                                    e1.printStackTrace();
                                                }

                                                Log.d(TAG, "iv url is " + url);

                                                editor = sharedPreferences.edit();
                                                editor.putString("backgroundImageUrl", backgroundImage != null ? backgroundImage.getUrl() : null);
                                                editor.putString("backgroundImage2Url", backgroundImage2 != null ? backgroundImage2.getUrl() : null);

                                                editor.apply();


//****************************************************************DEFAULTS************************************************************************************************


                                                ParseFile defaultsFile = associationObject.get(0).getParseFile("DefaultsFile");
                                                String[] defaultsFileArray;


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
                                                defaultsFileArray = defaultsFileString != null ? defaultsFileString.split("\\|") : new String[0];


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

                                                        // check defaults for pets

                                                        if (sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {
                                                            if (b13_pets != null) {
                                                                b13_pets.setVisibility(View.GONE);
                                                            } else {
                                                                assert false;
                                                                b13_pets.setVisibility(View.VISIBLE);

                                                                // check member defaults for pets

                                                                if (sharedPreferences.getString("defaultRecord(35)", "No").equals("No")) {
                                                                    {
                                                                        if (b13_pets != null) {
                                                                            b13_pets.setVisibility(View.GONE);
                                                                        } else {
                                                                            b13_pets.setVisibility(View.VISIBLE);
                                                                        }
                                                                    }


                                                            }
                                                        }

                                                            // check defaults for auto

                                                            if (sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {
                                                                if (b15_autos != null) {
                                                                    b15_autos.setVisibility(View.GONE);
                                                                } else {
                                                                    assert false;
                                                                    b15_autos.setVisibility(View.VISIBLE);

                                                                    // check member default for autos

                                                                    if (sharedPreferences.getString("defaultRecord(37)", "No").equals("No")) {
                                                                        if (b15_autos != null) {
                                                                            b15_autos.setVisibility(View.GONE);
                                                                        } else {
                                                                            b15_autos.setVisibility(View.VISIBLE);
                                                                        }
                                                                    }


                                                                }
                                                            }







                                                            // check member default for service providers

                                                            if (sharedPreferences.getString("defaultRecord(43)", "No").equals("No")) {
                                                                if (b11_serviceproviders != null) {
                                                                    b11_serviceproviders.setVisibility(View.GONE);
                                                                } else {
                                                                    assert false;
                                                                    b11_serviceproviders.setVisibility(View.VISIBLE);
                                                                }
                                                            }



                                                            if (sharedPreferences.getString("defaultRecord(46)", "No").equals("No")) {
                                                                if (b18_maintenance != null) {
                                                                    b18_maintenance.setVisibility(View.GONE);
                                                                } else {
                                                                    assert false;
                                                                    b18_maintenance.setVisibility(View.VISIBLE);
                                                                }
                                                            }






                                                        }

                                                        break;

                                                    case "Administrator":





                                                        b16_tools.setVisibility(View.VISIBLE);
                                                        b17_pushemail.setVisibility(View.VISIBLE);
                                                        b18_maintenance.setVisibility(View.VISIBLE);

                                                        // check default for service providers

                                                        if (sharedPreferences.getString("defaultRecord(43)", "No").equals("No")) {
                                                            if (b11_serviceproviders != null) {
                                                                b11_serviceproviders.setVisibility(View.GONE);
                                                            } else {
                                                                assert false;
                                                                b11_serviceproviders.setVisibility(View.VISIBLE);
                                                            }
                                                        }

                                                        // check defaults for pets

                                                        if (sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {
                                                            if (b13_pets != null) {
                                                                b13_pets.setVisibility(View.GONE);
                                                            } else {
                                                                assert false;
                                                                b13_pets.setVisibility(View.VISIBLE);


                                                            }
                                                        }

                                                        // check defaults for guests

                                                        if (sharedPreferences.getString("defaultRecord(39)", "No").equals("No")) {
                                                            if (b14_guests != null) {
                                                                b14_guests.setVisibility(View.GONE);
                                                            } else {
                                                                assert false;
                                                                b14_guests.setVisibility(View.VISIBLE);
                                                            }
                                                        }



                                                        // check defaults for auto

                                                        if (sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {
                                                            if (b15_autos != null) {
                                                                b15_autos.setVisibility(View.GONE);
                                                            } else {
                                                                assert false;
                                                                b15_autos.setVisibility(View.VISIBLE);


                                                            }
                                                        }


                                                        if (sharedPreferences.getString("defaultRecord(46)", "No").equals("No")) {
                                                            if (b18_maintenance != null) {
                                                                b18_maintenance.setVisibility(View.GONE);
                                                            } else {
                                                                b18_maintenance.setVisibility(View.VISIBLE);
                                                            }
                                                        }





                                                        break;

                                                    case "Master":

                                                        b1_email.setVisibility(View.VISIBLE);
                                                        b2_contacts.setVisibility(View.VISIBLE);
                                                        b3_directory.setVisibility(View.VISIBLE);
                                                        b4_weather.setVisibility(View.VISIBLE);
                                                        b5_calendar.setVisibility(View.VISIBLE);
                                                        b6_budget.setVisibility(View.VISIBLE);
                                                        b7_documents.setVisibility(View.VISIBLE);
                                                        b8_messageboard.setVisibility(View.VISIBLE);
                                                        b9_myinfo.setVisibility(View.VISIBLE);
                                                        b10_pushhistory.setVisibility(View.VISIBLE);
                                                        b11_serviceproviders.setVisibility(View.VISIBLE);
                                                        b12_changeadd.setVisibility(View.VISIBLE);
                                                        b13_pets.setVisibility(View.VISIBLE);
                                                        b14_guests.setVisibility(View.VISIBLE);
                                                        b15_autos.setVisibility(View.VISIBLE);
                                                        b16_tools.setVisibility(View.VISIBLE);
                                                        b17_pushemail.setVisibility(View.VISIBLE);
                                                        b18_maintenance.setVisibility(View.VISIBLE);




                                                        break;


                                                }


                                                TextView associationName = findViewById(R.id.textViewAssociationName);
                                                associationName.setText(defaultsFileArray[1]);





                                                AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                                                remoteDataTaskClass.execute();

                                                AsyncTask<Void, Void, Void> remoteDataTaskClassMB = new RemoteDataTaskClassMB(getApplicationContext());
                                                remoteDataTaskClassMB.execute();



                                                Map<String, ?> keysVisited = sharedPreferencesVisited.getAll();
                                                for (Map.Entry<String, ?> entry : keysVisited.entrySet()) {
                                                    Log.d(TAG, "map values VISITEDPREFERENCES " + entry.getKey() + ": " + entry.getValue().toString());
                                                }

  //***************************************************************************MESSAGE RED DOT***********************************************************************************

                                                formatter = new SimpleDateFormat("M/d/yy, h:mm a");



                                                String updateDateString = associationObject.get(0).getString("MessageDate");

                                                try {
                                                    update = formatter.parse(updateDateString);
                                                } catch (java.text.ParseException e1) {
                                                    e1.printStackTrace();
                                                }



                                                String visitDateString = sharedPreferencesVisited.getString("LASTMBVISIT", "1/1/16, 12:01 AM");



                                                try {
                                                    visit = formatter.parse(visitDateString);
                                                } catch (java.text.ParseException e1) {
                                                    e1.printStackTrace();
                                                }

                                                if ((update != null ? update.compareTo(visit) : 0) <= 0)
                                                {
                                                    Log.d(TAG, "visit is greater (more recent) than update *** visit ----> " + visit.toString() + " update ----> " + update.toString());
                                                    redDot.setVisibility(View.GONE);
                                                } else {
                                                    Log.d(TAG, "update is greater (more recent) than visit *** visit ----> " + visit.toString() + " update ----> " + update.toString());
                                                    redDot.setVisibility(View.VISIBLE);
                                                }

                                                editorVisited = sharedPreferencesVisited.edit();
                                                editorVisited.putBoolean("dataInitialDownloadRun", true);
                                                editorVisited.apply();


//***************************************************************************************************
               Map<String, ?> keys = sharedPreferences.getAll();
               for (Map.Entry<String, ?> entry : keys.entrySet()) {
                   Log.d(TAG, "map values MYPREFERENCES" + entry.getKey() +
                           ": " + entry.getValue().toString());
               }
               keys = sharedPreferencesVisited.getAll();
               for (Map.Entry<String, ?> entry : keys.entrySet()) {
                   Log.d(TAG, "map values MYPREFERENCES" + entry.getKey() +
                           ": " + entry.getValue().toString());
               }

//**************************************************************************************************


                                            }
                                        });

                                    }
                                });


                            } else {
                                Log.d(TAG, "Parse Exception");
                            }


                        }
                    });

           /* contentMain.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.INVISIBLE);



            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);*/

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

        Log.d(TAG, "visitedBefore onResume ----> " + sharedPreferencesVisited.getBoolean("visitedBefore", false));
        Log.d(TAG, "dataInitialDownloadRun onResume ----> " + sharedPreferencesVisited.getBoolean("dataInitialDownloadRun", false));

        if (sharedPreferencesVisited.getBoolean("visitedBefore", false) && sharedPreferencesVisited.getBoolean("dataInitialDownloadRun", false)) {

            AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
            remoteDataTaskClass.execute();

            Log.e(TAG, "***** Reload data *****");

        }


    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Log.e(TAG, "On Config Change LANDSCAPE");
        } else {

            Log.e(TAG, "On Config Change PORTRAIT");
        }


    }

    private void setupWindowAnimations() {



        // Re-enter transition is executed when returning to this activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);

        getWindow().setEnterTransition(slideTransition);


        Slide slideTransitionExit = new Slide();
        slideTransitionExit.setSlideEdge(Gravity.LEFT);
        getWindow().setExitTransition(slideTransitionExit);



    }


    @Override
    public void onBackPressed() {



        finishAfterTransition();


    }





}

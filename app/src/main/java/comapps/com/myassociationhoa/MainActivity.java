package comapps.com.myassociationhoa;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import comapps.com.myassociationhoa.autos.AutoActivity;
import comapps.com.myassociationhoa.budget.BudgetActivity;
import comapps.com.myassociationhoa.calendar.CalendarActivity;
import comapps.com.myassociationhoa.contact.ContactActivity;
import comapps.com.myassociationhoa.directory.DirectoryActivity;
import comapps.com.myassociationhoa.documents.DocumentsActivity;
import comapps.com.myassociationhoa.maintenance.MaintenanceActivity;
import comapps.com.myassociationhoa.messageboard.MBActivity;
import comapps.com.myassociationhoa.myinfo.MyInfoActivity;
import comapps.com.myassociationhoa.objects.AutoObject;
import comapps.com.myassociationhoa.objects.CalendarObject;
import comapps.com.myassociationhoa.objects.GuestObject;
import comapps.com.myassociationhoa.objects.MBObject;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAINACTIVITY";
    private static final String TAGC = "CALENDAR";
    public static final String TAGM = "MAINTENANCE";
    public static final String TAGP = "PROVIDER";
    public static final String PM = "PUSHMESSAGE";
    public static final String TAGPET = "PETOBJECT";
    public static final String TAGMBPOST = "MESSAGEBOARD";
    private static final String MYPREFERENCES = "MyPrefs";

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Integer numberOfPrefs;
    Context context;

    private ParseInstallation installation;

    String[] passCodes;

    private RosterObject rosterObject;
    private ArrayList<RosterObject> rosterObjects;
    private CalendarObject calendarObject;
    private ArrayList<CalendarObject> calendarObjects;
    MaintenanceObject maintenanceObject;
    private ArrayList<MaintenanceObject> maintenanceObjects;
    ProviderObject providerObject;
    private ArrayList<ProviderObject> providerObjects;
    MBObject mbObject;
    private ArrayList<MBObject> mbObjects;
    PushObject pushObject;
    private ArrayList<PushObject> pushObjects;



    PetObject petObject;
    private ArrayList<PetObject> petObjects;

    AutoObject autoObject;
    private ArrayList<AutoObject> autoObjects;

    GuestObject guestObject;
    private ArrayList<GuestObject> guestObjects;


    private LinearLayout content_main;

    private String objectId;
    private String installationId;


    private String memberName;
    private String memberType;
    private String memberDeviceName;
    private String memberNumber;
    private String associationCode;
    private TextView associationName;

    ImageView redDot;


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


        content_main = (LinearLayout) findViewById(R.id.content_main);

        associationName = (TextView) findViewById(R.id.textViewAssociationName);

        redDot = (ImageView) findViewById(R.id.imageViewRedDot);

        Button b1 = (Button) findViewById(R.id.button);
        Button b2 = (Button) findViewById(R.id.button2);
        Button b3 = (Button) findViewById(R.id.button3);
        Button b4 = (Button) findViewById(R.id.button4);
        Button b5 = (Button) findViewById(R.id.button5);
        Button b6 = (Button) findViewById(R.id.button6);
        Button b7 = (Button) findViewById(R.id.button7);
        Button b8 = (Button) findViewById(R.id.button8);
        Button b9 = (Button) findViewById(R.id.button9);
        Button b10 = (Button) findViewById(R.id.button10);
        Button b11 = (Button) findViewById(R.id.button11);
        Button b12 = (Button) findViewById(R.id.button12);
        Button b13 = (Button) findViewById(R.id.button13);
        Button b14 = (Button) findViewById(R.id.button14);
        Button b15 = (Button) findViewById(R.id.button15);
        Button b16 = (Button) findViewById(R.id.button16);
        Button b17 = (Button) findViewById(R.id.button17);
        Button b18 = (Button) findViewById(R.id.button18);


        LinearLayout ll1 = (LinearLayout) findViewById(R.id.ll1);
        LinearLayout ll2 = (LinearLayout) findViewById(R.id.ll2);
        LinearLayout ll3 = (LinearLayout) findViewById(R.id.ll3);
        LinearLayout ll4 = (LinearLayout) findViewById(R.id.ll4);
        LinearLayout ll5 = (LinearLayout) findViewById(R.id.ll5);
        LinearLayout ll6 = (LinearLayout) findViewById(R.id.ll6);


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        // Log.d(TAG, "visited before ------> " + sharedPreferences.getBoolean("visitedBefore", false));

        if (!sharedPreferences.getBoolean("visitedBefore", false)) {



            // Log.d(TAG, "visited before ------> " + sharedPreferences.getBoolean("visitedBefore", false));
            startActivity(new Intent(MainActivity.this, PopEnterPasscode.class));


        } else {

            // Log.d(TAG, "NOT FIRST VISIT");


            installation = ParseInstallation.getCurrentInstallation();

            memberType = ParseInstallation.getCurrentInstallation().getString("MemberType");

            // Log.d(TAG, "memberType is " + memberType);

            //   memberType = "Administrator";


            associationName.setText(sharedPreferences.getString("defaultRecord(30)", ""));


            switch (memberType) {


                case "Member":
                    b14.setVisibility(View.GONE);
                    b16.setVisibility(View.GONE);
                    b17.setVisibility(View.GONE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) b18.getLayoutParams();
                    layoutParams.weight = 3;


                    break;
                case "Administrator":


                    b13.setVisibility(View.VISIBLE);
                    b14.setVisibility(View.VISIBLE);
                    b15.setVisibility(View.VISIBLE);
                    b16.setVisibility(View.VISIBLE);
                    b17.setVisibility(View.VISIBLE);

                    break;

                case "Master":


                    b13.setVisibility(View.VISIBLE);
                    b14.setVisibility(View.VISIBLE);
                    b15.setVisibility(View.VISIBLE);
                    b16.setVisibility(View.VISIBLE);
                    b17.setVisibility(View.VISIBLE);

                    break;


            }

            // check defaults pets

            if (sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {
                if (b13 != null) {
                    b13.setVisibility(View.GONE);
                }
            }

            // check defaults for auto

            if (sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {
                if (b15 != null) {
                    b15.setVisibility(View.GONE);
                }
            }

            // check defaults for guests

            if (sharedPreferences.getString("defaultRecord(39)", "No").equals("No")) {
                if (b14 != null) {
                    b14.setVisibility(View.GONE);
                }
            }

            // check defaults for maintenance items

            if (sharedPreferences.getString("defaultRecord(46)", "No").equals("No")) {
                if (b18 != null) {
                    b18.setVisibility(View.GONE);
                }
            }

            // check defaults for service providers

            if (sharedPreferences.getString("defaultRecord(47)", "No").equals("No")) {
                if (b11 != null) {
                    b11.setVisibility(View.GONE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) b10.getLayoutParams();
                    layoutParams.weight = 1.5f;
                    layoutParams = (LinearLayout.LayoutParams) b12.getLayoutParams();
                    layoutParams.weight = 1.5f;
                }
            }

            if (b18.getVisibility() == View.GONE) {

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) b16.getLayoutParams();
                layoutParams.weight = 1.5f;
                layoutParams = (LinearLayout.LayoutParams) b17.getLayoutParams();
                layoutParams.weight = 1.5f;

            }


            if (b13.getVisibility() == View.GONE && b14.getVisibility() == View.GONE && b15.getVisibility() == View.GONE) {
                ll5.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll1.getLayoutParams();
                layoutParams.weight = 2.25f;
                layoutParams = (LinearLayout.LayoutParams) ll2.getLayoutParams();
                layoutParams.weight = 2.25f;
                layoutParams = (LinearLayout.LayoutParams) ll3.getLayoutParams();
                layoutParams.weight = 2.25f;
                layoutParams = (LinearLayout.LayoutParams) ll4.getLayoutParams();
                layoutParams.weight = 2.25f;
            }


            if (associationCode != null) {
                ParseQuery<ParseObject> queryInstallation = new ParseQuery<>(
                        associationCode);
                queryInstallation.setLimit(5);


                queryInstallation.findInBackground(new FindCallback<ParseObject>() {
                    public void done(final List<ParseObject> object, ParseException e) {
                        // Remove the previously cached results.
                        ParseObject.unpinAllInBackground(associationCode, new DeleteCallback() {
                            public void done(ParseException e) {
                                // Cache the new results.
                                ParseObject.pinAllInBackground(associationCode, object);
                            }
                        });
                    }
                });
            }


        }


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                sendEmail();

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(findViewById(R.id.button));




            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentContact = new Intent();
                                intentContact.setClass(MainActivity.this, ContactActivity.class);
                                startActivity(intentContact);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }

        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentDirectory = new Intent();
                                intentDirectory.setClass(MainActivity.this, DirectoryActivity.class);
                                startActivity(intentDirectory);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentWeather = new Intent();
                                intentWeather.setClass(MainActivity.this, WeatherActivity.class);
                                startActivity(intentWeather);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);




            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentCalendar = new Intent();
                                intentCalendar.setClass(MainActivity.this, CalendarActivity.class);
                                startActivity(intentCalendar);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentBudget = new Intent();
                                intentBudget.setClass(MainActivity.this, BudgetActivity.class);
                                startActivity(intentBudget);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentDocuments = new Intent();
                                intentDocuments.setClass(MainActivity.this, DocumentsActivity.class);
                                startActivity(intentDocuments);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentMessageBoard = new Intent();
                                intentMessageBoard.setClass(MainActivity.this, MBActivity.class);
                                startActivity(intentMessageBoard);

                                MainActivity.this.finish();

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentMyInfo = new Intent();
                                intentMyInfo.setClass(MainActivity.this, MyInfoActivity.class);
                                startActivity(intentMyInfo);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentPushHistory = new Intent();
                                intentPushHistory.setClass(MainActivity.this, PushActivity.class);
                                startActivity(intentPushHistory);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentServiceProviders = new Intent();
                                intentServiceProviders.setClass(MainActivity.this, ServiceProviderActivity.class);
                                startActivity(intentServiceProviders);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentChangeAddAssoc = new Intent();
                                intentChangeAddAssoc.setClass(MainActivity.this, Change_Add_Associations.class);
                                startActivity(intentChangeAddAssoc);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentPets = new Intent();
                                intentPets.setClass(MainActivity.this, PetsActivity.class);
                                startActivity(intentPets);


                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);

            }
        });

        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);

            }
        });

        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentAutos = new Intent();
                                intentAutos.setClass(MainActivity.this, AutoActivity.class);
                                startActivity(intentAutos);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);

            }
        });

        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentTools = new Intent();
                                intentTools.setClass(MainActivity.this, ToolsActivity.class);
                                startActivity(intentTools);


                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);


            }
        });

        findViewById(R.id.button17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);

            }
        });

        findViewById(R.id.button18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .withListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {}

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                Intent intentMaintenance = new Intent();
                                intentMaintenance.setClass(MainActivity.this, MaintenanceActivity.class);
                                startActivity(intentMaintenance);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {}

                            @Override
                            public void onAnimationRepeat(Animator animation) {}
                        }).playOn(v);



            }
        });

        if (sharedPreferences.getBoolean("visitedBefore", false)) {
            new RemoteDataTask().execute();
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

    private void sendEmail() {
        // TODO Auto-generated method stub

        // The following code is the implementation of Email client
        Intent intentSendEmail = new Intent(android.content.Intent.ACTION_SEND);
        intentSendEmail.setType("text/plain");
        String[] address = { "dewing@ewinginvestments.com" };

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

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {


            ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    final ParseInstallation installation = ParseInstallation.getCurrentInstallation();


                    objectId = ParseInstallation.getCurrentInstallation().getObjectId();
                    installationId = ParseInstallation.getCurrentInstallation().getInstallationId();
                    memberName = ParseInstallation.getCurrentInstallation().getString("memberName");
                    memberDeviceName = ParseInstallation.getCurrentInstallation().getString("memberName");
                    associationCode = ParseInstallation.getCurrentInstallation().getString("AssociationCode");
                    memberType = ParseInstallation.getCurrentInstallation().getString("MemberType");
                    memberNumber = ParseInstallation.getCurrentInstallation().getString("memberNumber");

                    // devicetoken is always null when app runs first time// Log.d(TAG, "objectId is " + objectId);

                    editor = sharedPreferences.edit();
                    editor.putString("MEMBERNAME", memberName);
                    editor.putString("MEMBERTYPE", memberType);
                    editor.putString("MEMBERNUMBER", memberNumber);
                    editor.apply();

                    // Log.d(TAG, "objectId is " + objectId);
                    // Log.d(TAG, "installationId is " + installationId);
                    // Log.d(TAG, "associationCode is " + associationCode);
                    // Log.d(TAG, "memberName is " + memberName);
                    // Log.d(TAG, "memberDeviceName is " + memberDeviceName);
                    // Log.d(TAG, "memberType is " + memberType);
                    // Log.d(TAG, "memberNumber is " + memberNumber);

                    // Locate the class table named with the current installations
                    // association code in Parse.com


                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseInstallation.getCurrentInstallation().getString("AssociationCode"));
                    query.findInBackground(new FindCallback<ParseObject>() {


                                               public void done(List<ParseObject> associations, ParseException e) {
                                                   if (e == null) {


                                                       //         // Log.d(TAG, "number of ParseObjects is " + associations.size());
                                                       //         // Log.d(TAG, "objectId is " + associations.get(0).getObjectId());
                                                       ParseFile backgroundImage = null;
                                                       try {
                                                           backgroundImage = (ParseFile) associations.get(0).get("Image1File");
                                                       } catch (Exception e1) {
                                                           e1.printStackTrace();
                                                       }
                                                       ParseFile backgroundImage2 = null;
                                                       try {
                                                           backgroundImage2 = (ParseFile) associations.get(0).get("Image2File");
                                                       } catch (Exception e1) {
                                                           e1.printStackTrace();
                                                       }



                                                   /*    ParseFile budgetPdfUrl = (ParseFile) associations.get(0).get("BudgetFile");


                                                       FileOutputStream outputStream;

                                                       try {
                                                           outputStream = openFileOutput("budgetfile.pdf", Context.MODE_PRIVATE);
                                                           outputStream.write(budgetPdfUrl.getData());
                                                           outputStream.close();
                                                           // Log.d(TAG, "budget.pdf created ");
                                                       } catch (Exception e3) {
                                                           e3.printStackTrace();
                                                       }*/

                                       /*                File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                                                       File dir = new File(root+"/MyAssociations/");
                                                       if(!dir.exists()) {
                                                           dir.mkdir();
                                                           // Log.d(TAG, "made dir ");
                                                       }
                                                       File fileToSDCard = new File(dir,"budgetfile.pdf");
                                                       if (fileToSDCard.exists()) {
                                                           try {
                                                               FileOutputStream fileOutputStream = new FileOutputStream(fileToSDCard);
                                                               try {
                                                                   fileOutputStream.write(budgetPdfUrl.getData());
                                                                   fileOutputStream.close();

                                                                   Toast.makeText(getApplicationContext(), "pdf saved", Toast.LENGTH_LONG).show();

                                                               } catch (IOException e1) {
                                                                   e1.printStackTrace();
                                                               } catch (ParseException e1) {
                                                                   e1.printStackTrace();
                                                               }
                                                           } catch (FileNotFoundException e1) {
                                                               e1.printStackTrace();
                                                           }

                                                       } else {

                                                           // Log.d(TAG, "budget.pdf never created ");

                                                       }  */


                                                       backgroundImage.getDataInBackground(new GetDataCallback() {
                                                           @Override
                                                           public void done(byte[] data, ParseException e) {
                                                               Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                                   content_main.setBackground(new BitmapDrawable(getResources(), bitmap));
                                                               }

                                                           }
                                                       });


                                                       editor = sharedPreferences.edit();
                                                       editor.putString("backgroundImage2Url", backgroundImage2.getUrl());
                                                       editor.apply();


                                                       ParseFile defaultsFile = associations.get(0).getParseFile("DefaultsFile");
                                                       String[] defaultsFileArray = null;

                                                       try {

                                                           byte[] file = defaultsFile.getData();


                                                           try {
                                                               String defaultsFileString = new String(file, "UTF-8");
                                                               // Log.d(TAG, "defaultsFileString is " + defaultsFileString);
                                                               defaultsFileArray = defaultsFileString.split("\\|");


                                                               for (int i = 0; i < defaultsFileArray.length; i++) {
                                                                   // Log.d(TAG, "defaultRecord(" + Integer.toString(i) + ") " + defaultsFileArray[i]);
                                                                   editor = sharedPreferences.edit();
                                                                   editor.putString("defaultRecord(" + Integer.toString(i) + ")", defaultsFileArray[i]);
                                                                   editor.apply();
                                                               }

                                                           } catch (UnsupportedEncodingException e1) {
                                                               e1.printStackTrace();
                                                           }
                                                       } catch (ParseException e1) {
                                                           e1.printStackTrace();
                                                       }


//**************************************************************************************************************************************************************

                                                       ParseFile rosterFile = associations.get(0).getParseFile("RosterFile");

                                                       String[] rosterFileArray = null;

                                                   try {
                                                      byte[] file = rosterFile.getData();
                                                        try {
                                                           String rosterFileString = new String(file, "UTF-8");

                                                            rosterFileArray = rosterFileString.split("\\|");


                                                            for(String member: rosterFileArray){

                                                                member.trim();


                                                                //      // Log.v(TAG, "MEMBER: " + member.substring(0,80));

                                                            }

                                                            //      // Log.v(TAG, "rosterFileArray length: " + rosterFileArray.length);





                                        rosterObjects = new ArrayList<RosterObject>();

                                        for (int i = 0; i < rosterFileArray.length; i++) {


                                            String[] rosterFields = rosterFileArray[i].split("\\^");

                                            //    // Log.d(TAG, "rosterFileArray[" + i + "] is " + rosterFileArray[i].substring(0, 40));
                                            //     // Log.d(TAG, "rosterFileArray[" + i + "] number of fields is " + count(rosterFileArray[i], '^'));
                                            //    // Log.d(TAG, "rosterFileArray member is " + rosterFileArray[i]);

                                            rosterObject = new RosterObject();

                                            for (int j = 0; j < rosterFields.length; j++) {
                                                //   // Log.d(TAG, "i = " + i + " , " + "j = " + j);
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
                                                }




                                            }

                                            rosterObjects.add(rosterObject);

                                            editor = sharedPreferences.edit();
                                            Gson gson = new Gson();
                                            String jsonRosterObject = gson.toJson(rosterObject); // myObject - instance of MyObject
                                            editor.putString("rosterObject" + "[" + i + "]", jsonRosterObject);
                                            editor.putInt("rosterSize", rosterObjects.size());
                                            editor.apply();


                                        }


                                                        } catch (UnsupportedEncodingException e1) {
                                                            e1.printStackTrace();
                                                        }

                                                   } catch (ParseException e1) {
                                                       e1.printStackTrace();
                                                   }


//*************************************************************************************************************************************************************


                                                       ParseFile eventFile = associations.get(0).getParseFile("EventFile");

                                                       String[] eventFileArray = null;

                                                       try {
                                                           byte[] file = eventFile.getData();
                                                           try {
                                                               String eventFileString = new String(file, "UTF-8");

                                                               eventFileArray = eventFileString.split("\\|");


                                                               for (String event : eventFileArray) {

                                                                   event.trim();


                                                                   // Log.v(TAGC, "EVENT: " + event);

                                                               }

                                                               // Log.v(TAGC, "eventFileArray length: " + eventFileArray.length);


                                                               calendarObjects = new ArrayList<CalendarObject>();

                                                               for (int i = 0, j = 0; i < eventFileArray.length; i++) {

                                                                   switch (j) {
                                                                       case 0:
                                                                           calendarObject = new CalendarObject();
                                                                           calendarObject.setCalendarText(eventFileArray[i]);
                                                                           j = j + 1;
                                                                           break;
                                                                       case 1:
                                                                           calendarObject.setCalendarText2(eventFileArray[i]);
                                                                           j = j + 1;
                                                                           break;
                                                                       case 2:
                                                                           calendarObject.setCalendarDetailText(eventFileArray[i]);
                                                                           j = j + 1;
                                                                           break;
                                                                       case 3:
                                                                           calendarObject.setCalendarStartDate(eventFileArray[i]);
                                                                           j = j + 1;
                                                                           break;
                                                                       case 4:
                                                                           calendarObject.setCalendarEndDate(eventFileArray[i]);
                                                                           j = j + 1;
                                                                           break;
                                                                       case 5:
                                                                           calendarObject.setCalendarSortDate(eventFileArray[i]);
                                                                           calendarObjects.add(calendarObject);

                                                                           editor = sharedPreferences.edit();
                                                                           Gson gson = new Gson();
                                                                           String jsonCalendarObject = gson.toJson(calendarObject); // myObject - instance of MyObject
                                                                           editor.putString("calendarObject" + "[" + i / 6 + "]", jsonCalendarObject);
                                                                           editor.putString("calendarSize", String.valueOf(calendarObjects.size()));
                                                                           editor.apply();

                                                                           j = 0;

                                                                           break;
                                                                   }

                                                                   // Log.d(TAGC, "i = " + i + " , " + "j = " + j);
                                                                   // Log.d(TAGC, "EventFileArray[" + i + "]item " + eventFileArray[i]);


                                                               }

                                                               // Will invoke overrided `toString()` method


                                                           } catch (UnsupportedEncodingException e1) {
                                                               e1.printStackTrace();
                                                           }

                                                       } catch (ParseException e1) {
                                                       }


                                                       // Log.d(TAGC, "calendarObjects size is " + calendarObjects.size());

                                                       for (CalendarObject object : calendarObjects) {
                                                           // Log.i(TAGC, object.toString());
                                 }

//**************************************************************************************************************************************************************
                                                       if (!sharedPreferences.getString("defaultRecord(47)", "No").equals("No")) {



                                                       ParseFile providerFile = associations.get(0).getParseFile("ProviderFile");

                                                       ArrayList<String> providerType = new ArrayList();
                                                       String[] providerFileArray = null;


                                                       try {
                                                           byte[] file = providerFile.getData();
                                                           try {
                                                               String providerFileString = new String(file, "UTF-8");
                                                               providerFileString = providerFileString.replace("|0|", "|0| |");

                                                             //  Log.v(TAGP, "providerFileString -----> " + providerFileString);

                                                               providerFileArray = providerFileString.split("\\|");

                                                               for ( String providerField: providerFileArray) {

                                                                   Log.v(TAGP, "providerField -----> " + providerField);

                                                               }



                                                               providerObjects = new ArrayList<ProviderObject>();


                                                               for (int i = 0, j = 0; i < providerFileArray.length; i++) {


                                                                   switch (j) {
                                                                       case 0:
                                                                           providerObject = new ProviderObject();
                                                                           providerObject.setProviderType(providerFileArray[i]);
                                                                           j++;
                                                                            Log.d(TAGP, "provider type is " + providerFileArray[i]);
                                                                           break;

                                                                       case 1:
                                                                           providerObject.setProviderCount(providerFileArray[i]);
                                                                           j++;
                                                                            Log.d(TAGP, "provider count is " + providerFileArray[i]);
                                                                           break;
                                                                       case 2:
                                                                           providerObject.setProviderList(providerFileArray[i]);
                                                                           Log.d(TAGP, "provider list is " + providerFileArray[i]);
                                                                           providerObjects.add(providerObject);
                                                                           j = 0;

                                                                           editor = sharedPreferences.edit();
                                                                           Gson gson = new Gson();
                                                                           String jsonProviderObject = gson.toJson(providerObject); // myObject - instance of MyObject
                                                                           editor.putString("providerObject" + "[" + ((i + 1)/3 - 1) + "]", jsonProviderObject);
                                                                           editor.putString("providerSize", String.valueOf(providerObjects.size()));
                                                                           editor.apply();


                                                                           break;


                                                                   }


                                                               }


                                                           } catch (UnsupportedEncodingException e1) {
                                                               e1.printStackTrace();
                                                           }

                                                       } catch (ParseException e1) {
                                                       }


                                                        Log.d(TAGP, "providerObjects size is " + providerObjects.size());

                                                       for (ProviderObject object : providerObjects) {
                                                           Log.i(TAGP, object.toString());
                                                       }
                                                       ;
                                                       }

//**************************************************************************************************************************************************************


                                                       ParseFile postsFile = associations.get(0).getParseFile("MessageFile");

                                                       String[] postFileArray = null;

                                                       try {
                                                           byte[] file = postsFile.getData();
                                                           try {
                                                               String postsFileString = new String(file, "UTF-8");

                                                               postFileArray = postsFileString.split("\\|");


                                                               for (String post : postFileArray) {

                                                                   post.trim();


                                                                   //        // Log.v(TAGMBPOST, "POST: " + post);

                                                               }

                                                               // Log.v(TAGMBPOST, "postFileArray length: " + postFileArray.length);


                                                               mbObjects = new ArrayList<MBObject>();

                                                               for (int i = 0, j = 0; i < postFileArray.length; i++) {

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
                                                                           editor.putString("mbObject" + "[" + (i + 1) / 5 + "]", jsonMbObject);

                                                                           editor.apply();

                                                                           j = 0;

                                                                           break;
                                                                   }


                                                               }

                                                               // Will invoke overrided `toString()` method


                                                           } catch (UnsupportedEncodingException e1) {
                                                               e1.printStackTrace();
                                                           }

                                                       } catch (ParseException e1) {
                                                       }


                                                       // Log.d(TAG, "oldMbSize ---------> " + String.valueOf(sharedPreferences.getInt("mbSize", 0)));
                                                       // Log.d(TAG, "newMbSize mbObjects.size ---------> " + String.valueOf(mbObjects.size()));


                                                       if (sharedPreferences.getInt("mbSize", 0) < mbObjects.size()) {


                                                           redDot.setVisibility(View.VISIBLE);

                                                           editor = sharedPreferences.edit();
                                                           editor.putInt("mbSize", mbObjects.size());
                                                           editor.apply();

                                                           // Log.d(TAG, "red dot visible");


                                                       } else {

                                                           editor = sharedPreferences.edit();
                                                           editor.putInt("mbSize", mbObjects.size());
                                                           editor.apply();

                                                           // Log.d(TAG, "red dot gone");

                                                       }




                                                       // Log.d(TAGMBPOST, "mbObjects size is " + mbObjects.size());

                                                       for (MBObject object : mbObjects) {
                                                           // Log.i(TAGMBPOST, object.toString());
                                                       }


//**************************************************************************************************************************************************************


                                                       ParseFile pushHistoryFile = associations.get(0).getParseFile("PushFile");

                                                       String[] pushHistoryFileArray = null;
                                                       String[] pushHistoryFileArray2 = null;


                                                       try {
                                                           byte[] file = pushHistoryFile.getData();
                                                           try {
                                                               String pushHistoryFileString = new String(file, "UTF-8");

                                                               pushHistoryFileArray = pushHistoryFileString.split("\\|");


                                                               for (String pushMessage : pushHistoryFileArray) {

                                                                   pushMessage.trim();


                                                                   // Log.v(PM, "push message: " + pushMessage);

                                                               }

                                                               // Log.v(PM, "pushHistoryFileArray length: " + pushHistoryFileArray.length);

                                                               pushObjects = new ArrayList<PushObject>();

                                                               for (int i = 1, j = 1; i < pushHistoryFileArray.length; i++) {

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
                                                                               editor.putString("pushObjectsSize", String.valueOf(pushObjects.size()));
                                                                               editor.commit();
                                                                               j = 1;


                                                                       }


                                                                   }

                                                               }

                                                           } catch (UnsupportedEncodingException e1) {
                                                               e1.printStackTrace();
                                                           }

                                                       } catch (ParseException e1) {
                                                       }


                                                       // Log.d(PM, "pushObjects size is " + pushObjects.size());

                                                       for (PushObject object : pushObjects) {
                                                           // Log.i(PM, object.toString());
                                                       }


//*****************************************************get pets**********************************************************************************************************************

                                                       if (!sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {



                                                       ParseFile petFile = associations.get(0).getParseFile("PetFile");

                                                       String[] petFileArray = null;
                                                       String[] petFileArray2 = null;


                                                       try {
                                                           byte[] file = petFile.getData();
                                                           try {
                                                               String petFileString = new String(file, "UTF-8");

                                                               petFileArray = petFileString.split("\\|");


                                                               for (String pet : petFileArray) {

                                                                   pet.trim();


                                                                   // Log.v(PM, "pet: " + pet);

                                                               }

                                                               // Log.v(PM, "petFileArray length: " + petFileArray.length);

                                                               petObjects = new ArrayList<PetObject>();

                                                               for (int i = 0, j = 1; i < petFileArray.length; i++) {

                                                                   petFileArray2 = petFileArray[i].split("\\^");

                                                                   for (String petField : petFileArray2) {
                                                                       // Log.d(PM, "pet is ---> " + petField + " i = " + i + " j = " + j);

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

                                                           } catch (UnsupportedEncodingException e1) {
                                                               e1.printStackTrace();
                                                           }

                                                       } catch (ParseException e1) {
                                                       }


                                                       // Log.d(PM, "petObjects size is " + petObjects.size());

                                                       for (PetObject object : petObjects) {
                                                           // Log.i(PM, object.toString());
                                                       }

                                                       }
//*****************************************************get autos**********************************************************************************************************************

                                                       if (!sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {



                                                       ParseFile autoFile = associations.get(0).getParseFile("AutoFile");

                                                       String[] autoFileArray = null;
                                                       String[] autoFileArray2 = null;


                                                       try {
                                                           byte[] file = autoFile.getData();
                                                           try {
                                                               String autoFileString = new String(file, "UTF-8");

                                                               autoFileArray = autoFileString.split("\\|");


                                                               for (String auto : autoFileArray) {

                                                                   auto.trim();


                                                                   // Log.v(PM, "auto: " + auto);

                                                               }

                                                               // Log.v(PM, "autoFileArray length: " + autoFileArray.length);

                                                               autoObjects = new ArrayList<AutoObject>();

                                                               for (int i = 0, j = 1; i < autoFileArray.length; i++) {

                                                                   autoFileArray2 = autoFileArray[i].split("\\^");

                                                                   for (String autoField : autoFileArray2) {
                                                                       // Log.d(PM, "auto is ---> " + autoField + " i = " + i + " j = " + j);

                                                                       switch (j) {
                                                                           case 1:
                                                                               autoObject = new AutoObject();
                                                                               autoObject.setOwner(autoField);
                                                                               j = j + 1;
                                                                               break;
                                                                           case 2:
                                                                               autoObject.setMemberNumber(autoField);
                                                                               j = j + 1;
                                                                               break;
                                                                           case 3:
                                                                               autoObject.setMake(autoField);
                                                                               j = j + 1;
                                                                               break;
                                                                           case 4:
                                                                               autoObject.setModel(autoField);
                                                                               j = j + 1;
                                                                               break;
                                                                           case 5:
                                                                               autoObject.setColor(autoField);
                                                                               j = j + 1;
                                                                               break;
                                                                           case 6:
                                                                               autoObject.setYear(autoField);
                                                                               j = j + 1;
                                                                               break;
                                                                           case 7:
                                                                               autoObject.setPlate(autoField);
                                                                               autoObjects.add(autoObject);

                                                                               editor = sharedPreferences.edit();
                                                                               Gson gson = new Gson();
                                                                               String jsonAutoObject = gson.toJson(autoObject); // myObject - instance of MyObject
                                                                               editor.putString("autoObject" + "[" + i + "]", jsonAutoObject);
                                                                               editor.putInt("autosObjectsSize", autoObjects.size());
                                                                               editor.commit();
                                                                               j = 1;


                                                                       }


                                                                   }

                                                               }

                                                           } catch (UnsupportedEncodingException e1) {
                                                               e1.printStackTrace();
                                                           }

                                                       } catch (ParseException e1) {
                                                       }


                                                       // Log.d(PM, "autoObjects size is " + autoObjects.size());

                                                       for (AutoObject object : autoObjects) {
                                                           // Log.i(PM, object.toString());
                                                       }


                                                       }
//*****************************************************get pdfs**********************************************************************************************************************


                                                       ParseFile budgetFile = associations.get(0).getParseFile("BudgetFile");
                                                       ParseFile byLawsFile = associations.get(0).getParseFile("ByLawsFile");
                                                       ParseFile expenseFile = associations.get(0).getParseFile("ExpenseFile");
                                                       ParseFile rulesFile = associations.get(0).getParseFile("RulesFile");
                                                       ParseFile minutesFile = associations.get(0).getParseFile("MinutesFile");
                                                       ParseFile misc1File = associations.get(0).getParseFile("MiscDoc1File");
                                                       ParseFile misc2File = associations.get(0).getParseFile("MiscDoc2File");
                                                       ParseFile misc3File = associations.get(0).getParseFile("MiscDoc3File");

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
                                                           ;
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
                                for(Map.Entry<String,?> entry : keys.entrySet()){
                                     Log.d(TAG, "map values " + entry.getKey() + ": " + entry.getValue().toString());
                                }
//***************************************************************************************************************************************************-+




                                TextView associationName = (TextView)findViewById(R.id.textViewAssociationName);
                                associationName.setText(defaultsFileArray[1]);


                                final LinearLayout contentMain = (LinearLayout)findViewById(R.id.content_main);

                                Picasso.with(getApplicationContext())
                                        .load(backgroundImage.getUrl())
                                        .into(new Target() {
                                            @Override
                                            @TargetApi(16)
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                int sdk = Build.VERSION.SDK_INT;
                                                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                                    contentMain.setBackgroundDrawable(new BitmapDrawable(bitmap));
                                                } else {
                                                    contentMain.setBackground(new BitmapDrawable(getResources(), bitmap));
                                                }
                                            }

                                            @Override
                                            public void onBitmapFailed(Drawable errorDrawable) {
                                                // use error drawable if desired
                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                // use placeholder drawable if desired
                                            }
                                        });




                            } else {
                                // Log.d(TAG, "parse exception");
                            }
                        }
                    });




                }
            });



            return null;
        }

        }

    private static int count(String s, char c) {
        return s.length()==0 ? 0 : (s.charAt(0)==c ? 1 : 0) + count(s.substring(1),c);
    }


   /* private static JSONObject getJsonFromServer(String url) throws IOException, JSONException {
        BufferedReader inputStream = null;
        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();
        inputStream = new BufferedReader(new InputStreamReader(
                dc.getInputStream()));
        String jsonResult = inputStream.readLine();

        // Log.d(TAG, "weather api is " + jsonResult);

        return new JSONObject(jsonResult);
    }
*/


    private void displayImage(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {

                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,
                                data.length);

                        if (bmp != null) {

                            // Log.e("parse file ok", " null");
                            // img.setImageBitmap(Bitmap.createScaledBitmap(bmp,
                            // (display.getWidth() / 5),
                            // (display.getWidth() /50), false));


                        }
                    } else {
                        // Log.e("paser after downloade", " null");
                    }

                }
            });
        } else {

            // Log.e("parse file", " null");

            // img.setImageResource(R.drawable.ic_launcher);

            img.setPadding(10, 10, 10, 10);
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
            new RemoteDataTask().execute();
        }
    }


    @Override
    public void onBackPressed() {

        this.finish();
    }





}

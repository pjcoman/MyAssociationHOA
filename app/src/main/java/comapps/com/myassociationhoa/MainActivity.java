package comapps.com.myassociationhoa;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
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
import java.util.Timer;

import comapps.com.myassociationhoa.budget.BudgetActivity;
import comapps.com.myassociationhoa.calendar.CalendarActivity;
import comapps.com.myassociationhoa.contact.ContactActivity;
import comapps.com.myassociationhoa.directory.DirectoryActivity;
import comapps.com.myassociationhoa.documents.DocumentsActivity;
import comapps.com.myassociationhoa.maintenance.MaitenanceActivity;
import comapps.com.myassociationhoa.messageboard.MessageBoardActivity;
import comapps.com.myassociationhoa.myinfo.MyInfoActivity;
import comapps.com.myassociationhoa.objects.RosterObject;
import comapps.com.myassociationhoa.push_history.PushHistoryActivity;
import comapps.com.myassociationhoa.service_providers.ServiceProviderActivity;
import comapps.com.myassociationhoa.weather.WeatherActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MAINACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;

    private FragmentManager mFragmentManager;

    RosterObject rosterObject;
    private ArrayList<RosterObject> rosterObjects;
    List<ParseObject> associations;


    String objectId;
    String installationId;

    String bodyMenu;
    int im;
    int alertCode;
    String statusNameCode;
    String statusAdminCode;
    String statusMemberCode;
    String statusGroupCode;
    String passwordMenu;

    String appShortName;
    String appLongName;
    String appEmailAdministrator;
    String appEmailGeneral;
    String appEmailPresident;
    String appEmailEvents;

    Image image1;
    Image imageWork;
    ImageView imageView;

    Timer viewTimer;
    ArrayList<Byte> menuData;
    String menuString;

    String memberName;
    String memberType;
    String memberDeviceName;
    String memberNumber;
    String associationCode;

    ProgressDialog progressDialog;


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


        sharedpreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        final Animation animScaleButton = AnimationUtils.loadAnimation(this,R.anim.scalebuttonsonclick);

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


        TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        TableRow row4 = (TableRow) findViewById(R.id.contentMainRow4);
        TableRow row5 = (TableRow) findViewById(R.id.contentMainRow5);





        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        memberType = installation.getCurrentInstallation().getString("MemberType");

        Log.d(TAG, "memberType is " + memberType);

     //   memberType = "Administrator";



        switch (memberType) {


            case "Member":
                row4.setVisibility(View.GONE);
                b16.setVisibility(View.GONE);
                b17.setVisibility(View.GONE);
                tableLayout.setWeightSum(5);
                row5.setWeightSum(1);


                break;
            case "Administrator":

                row4.setVisibility(View.VISIBLE);
                b13.setVisibility(View.VISIBLE);
                b14.setVisibility(View.VISIBLE);
                b15.setVisibility(View.VISIBLE);
                b16.setVisibility(View.VISIBLE);
                b17.setVisibility(View.VISIBLE);
                tableLayout.setWeightSum(6);
                row5.setWeightSum(3);
                break;

            case "Master":

                row4.setVisibility(View.VISIBLE);
                b13.setVisibility(View.VISIBLE);
                b14.setVisibility(View.VISIBLE);
                b15.setVisibility(View.VISIBLE);
                b16.setVisibility(View.VISIBLE);
                b17.setVisibility(View.VISIBLE);
                tableLayout.setWeightSum(6);
                row5.setWeightSum(3);
                break;
            case "Guest":
                break;

        }

        if ( associationCode != null ) {
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


       mFragmentManager = getFragmentManager();



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(findViewById(R.id.button));


                sendEmail();

            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentContact = new Intent();
                intentContact.setClass(MainActivity.this, ContactActivity.class);
                startActivity(intentContact);

            }

        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentDirectory = new Intent();
                intentDirectory.setClass(MainActivity.this, DirectoryActivity.class);
                startActivity(intentDirectory);

            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentWeather = new Intent();
                intentWeather.setClass(MainActivity.this, WeatherActivity.class);
                startActivity(intentWeather);


            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentCalendar = new Intent();
                intentCalendar.setClass(MainActivity.this, CalendarActivity.class);
                startActivity(intentCalendar);

            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(v);

                Intent intentBudget = new Intent();
                intentBudget.setClass(MainActivity.this, BudgetActivity.class);
                startActivity(intentBudget);

                }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentDocuments = new Intent();
                intentDocuments.setClass(MainActivity.this, DocumentsActivity.class);
                startActivity(intentDocuments);

            }
        });

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentMessageBoard = new Intent();
                intentMessageBoard.setClass(MainActivity.this, MessageBoardActivity.class);
                startActivity(intentMessageBoard);

            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentMyInfo = new Intent();
                intentMyInfo.setClass(MainActivity.this, MyInfoActivity.class);
                startActivity(intentMyInfo);

            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentPushHistory = new Intent();
                intentPushHistory.setClass(MainActivity.this, PushHistoryActivity.class);
                startActivity(intentPushHistory);

            }
        });

        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentServiceProviders = new Intent();
                intentServiceProviders.setClass(MainActivity.this, ServiceProviderActivity.class);
                startActivity(intentServiceProviders);

            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentChangeAddAssoc = new Intent();
                intentChangeAddAssoc.setClass(MainActivity.this, Change_Add_Associations.class);
                startActivity(intentChangeAddAssoc);

            }
        });

        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

            }
        });

        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

            }
        });

        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

            }
        });

        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

            }
        });

        findViewById(R.id.button17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

            }
        });

        findViewById(R.id.button18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentMaintenance = new Intent();
                intentMaintenance.setClass(MainActivity.this, MaitenanceActivity.class);
                startActivity(intentMaintenance);

            }
        });





        new RemoteDataTask().execute();

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

                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();


                    objectId = installation.getCurrentInstallation().getObjectId();
                    installationId = installation.getCurrentInstallation().getInstallationId();
                    memberName = installation.getCurrentInstallation().getString("memberName");
                    memberDeviceName = installation.getCurrentInstallation().getString("memberName");
                    associationCode = installation.getCurrentInstallation().getString("AssociationCode");
                    memberType = installation.getCurrentInstallation().getString("MemberType");
                    memberNumber = installation.getCurrentInstallation().getString("memberNumber");

                    // devicetoken is always null when app runs first timeLog.d(TAG, "objectId is " + objectId);



                    Log.d(TAG, "objectId is " + objectId);
                    Log.d(TAG, "installationId is " + installationId);
                    Log.d(TAG, "associationCode is " + associationCode);
                    Log.d(TAG, "memberName is " + memberName);
                    Log.d(TAG, "memberDeviceName is " + memberDeviceName);
                    Log.d(TAG, "memberType is " + memberType);
                    Log.d(TAG, "memberNumber is " + memberNumber);

                    // Locate the class table named with the current installations
                    // association code in Parse.com




                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(installation.getCurrentInstallation().getString("AssociationCode"))
                            .fromLocalDatastore();
                    query.findInBackground(new FindCallback<ParseObject>() {


                                               public void done(List<ParseObject> associations, ParseException e) {
                                                   if (e == null) {


                                                       Log.d(TAG, "number of ParseObjects is " + associations.size());
                                                       Log.d(TAG, "objectId is " + associations.get(0).getObjectId());
                                                       ParseFile backgroundImage = (ParseFile) associations.get(0).get("Image1File");
                                                       Log.d(TAG, "image address is " + backgroundImage.getUrl());


                                                       ParseFile defaultsFile = (ParseFile) associations.get(0).getParseFile("DefaultsFile");
                                                       String[] defaultsFileArray = null;

                                                       try {
                                                           byte[] file = defaultsFile.getData();
                                                           try {
                                                               String defaultsFileString = new String(file, "UTF-8");
                                                               Log.d(TAG, "defaultsFileString is " + defaultsFileString);
                                                               defaultsFileArray = defaultsFileString.split("\\|");

                                                               for (int i = 0; i + 1 < defaultsFileArray.length; i++) {
                                                                   Log.d(TAG, "defaultsFileString member is " + Integer.toString(i) + " " + defaultsFileArray[i]);
                                                                   SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                   editor.putString("defaultRecord(" + Integer.toString(i) + ")", defaultsFileArray[i]);
                                                                   editor.commit();
                                                               }

                                                           } catch (UnsupportedEncodingException e1) {
                                                               e1.printStackTrace();
                                                           }
                                                       } catch (ParseException e1) {
                                                           e1.printStackTrace();
                                                       }




                                               ParseFile rosterFile = (ParseFile) associations.get(0).getParseFile("RosterFile");

                                                       String[] rosterFileArray = null;

                                                   try {
                                                      byte[] file = rosterFile.getData();
                                                        try {
                                                           String rosterFileString = new String(file, "UTF-8");

                                                            rosterFileArray = rosterFileString.split("\\|");


                                                            for(String member: rosterFileArray){

                                                                member.trim();


                                                                Log.v(TAG, "MEMBER: " + member.substring(0,80));

                                                            }

                                                            Log.v(TAG, "roserFileArray length: " + rosterFileArray.length);



//**************************************************************************************************************************************************************

                                        rosterObjects = new ArrayList<RosterObject>();

                                        for (int i = 0; i < rosterFileArray.length; i++) {


                                            String[] rosterFields = rosterFileArray[i].split("\\^");

                                            Log.d(TAG, "rosterFileArray[" + i + "] is " + rosterFileArray[i].substring(0, 40));
                                            Log.d(TAG, "rosterFileArray[" + i + "] number of fields is " + count(rosterFileArray[i], '^'));
                                            //    Log.d(TAG, "rosterFileArray member is " + rosterFileArray[i]);

                                            rosterObject = new RosterObject();

                                            for (int j = 0; j < rosterFields.length; j++) {
                                                //   Log.d(TAG, "i = " + i + " , " + "j = " + j);
                                             //   Log.d(TAG, "rosterFileArray[" + i + "] field" + j + " is " + rosterFields[j]);

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
                                                        rosterObject.setBusinessName(rosterFields[j]);
                                                        break;
                                                    case 13:
                                                        rosterObject.setBusinessAddress1(rosterFields[j]);
                                                        break;
                                                    case 14:
                                                        rosterObject.setBusinessAddress2(rosterFields[j]);
                                                        break;
                                                    case 15:
                                                        rosterObject.setBusinessCity(rosterFields[j]);
                                                        break;
                                                    case 16:
                                                        rosterObject.setBusinessState(rosterFields[j]);
                                                        break;
                                                    case 17:
                                                        rosterObject.setBusinessZip(rosterFields[j]);
                                                        break;
                                                    case 18:
                                                        rosterObject.setBusinessPhone(rosterFields[j]);
                                                        break;
                                                    case 19:
                                                        rosterObject.setBusinessEmail(rosterFields[j]);
                                                        break;
                                                    case 20:
                                                        rosterObject.setRegistrationNumber(rosterFields[j]);
                                                        break;
                                                    case 21:
                                                        rosterObject.setSort(rosterFields[j]);
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
                                        }

                                                         // Will invoke overrided `toString()` method



//**************************************************************************************************************************************************************


                                                        } catch (UnsupportedEncodingException e1) {
                                                            e1.printStackTrace();
                                                        }

                                                   } catch (ParseException e1) {
                                                       e1.printStackTrace();
                                                   }




                                Log.d(TAG, "rosterObjects size is " + rosterObjects.size());

                                for (RosterObject object : rosterObjects) {
                                    Log.i(TAG, object.toString());
                                 }





                         /*       Map<String,?> keys = sharedpreferences.getAll();

                                for(Map.Entry<String,?> entry : keys.entrySet()){
                                    Log.d(TAG, "map values " + entry.getKey() + ": " +
                                            entry.getValue().toString());
                                }*/



                                TextView associationName = (TextView)findViewById(R.id.textViewAssociationName);
                                associationName.setText(defaultsFileArray[1]);


                                final LinearLayout contentMain = (LinearLayout)findViewById(R.id.content_main);

                                Picasso.with(getApplicationContext())
                                        .load(backgroundImage.getUrl())
                                        .into(new Target() {
                                            @Override
                                            @TargetApi(16)
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                int sdk = android.os.Build.VERSION.SDK_INT;
                                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
                                Log.d(TAG, "parse exception");
                            }
                        }
                    });




                }
            });



            return null;
        }

        }

    public static int count(String s, char c) {
        return s.length()==0 ? 0 : (s.charAt(0)==c ? 1 : 0) + count(s.substring(1),c);
    }





    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }




}

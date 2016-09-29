package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.RosterObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;



/**
 * Created by me on 6/28/2016.
 */
public class BuildTextDirectoryActivity extends AppCompatActivity {

    private static final String TAG = "BUILDTEXTDIRECTORY";
    public static final String MYPREFERENCES = "MyPrefs";
    public static final String VISITEDPREFERENCES = "VisitedPrefs";

    private RosterObject rosterObject;
    private ArrayList<RosterObject> rosterObjects;

    ParseQuery<ParseObject> query;

    String rosterFileString = "";
    String[] rosterFileArray;
    String rosterFileToEmail = "";


    File directoryToEmail;

    Button processButton;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesVisited;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorVisited;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.buildtextdirectory_layout);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Send Directory File");
        }



        processButton = (Button) findViewById(R.id.buttonProcess);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_APPEND);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width * 1, height * 1);

        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                query = new ParseQuery<ParseObject>(installation.getString("AssociationCode"));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, ParseException e) {


                        ParseFile rosterFile = assoc.get(0).getParseFile("RosterFile");


                        try {
                            byte[] file = rosterFile.getData();
                            try {
                                rosterFileString = new String(file, "UTF-8");

                                Log.d(TAG, "existing roster --->" + rosterFileString);

                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        rosterFileArray = rosterFileString.split("\\|", -1);

                        rosterObjects = new ArrayList<RosterObject>();

                        for (int i = 0; i < rosterFileArray.length; i++) {


                            String[] rosterFields = rosterFileArray[i].split("\\^", -1);



                            rosterObject = new RosterObject();


                            rosterObject.setNumber(rosterFields[0]);
                            rosterObject.setLastName(rosterFields[1]);
                            rosterObject.setFirstName(rosterFields[2]);
                            rosterObject.setMiddleName(rosterFields[3]);
                            rosterObject.setHomeAddress1(rosterFields[4]);
                            rosterObject.setHomeAddress2(rosterFields[5]);
                            rosterObject.setHomeCity(rosterFields[6]);
                            rosterObject.setHomeState(rosterFields[7]);
                            rosterObject.setHomeZip(rosterFields[8]);
                            rosterObject.setHomePhone(rosterFields[9]);
                            rosterObject.setMobilePhone(rosterFields[10]);
                            rosterObject.setEmail(rosterFields[11]);
                            rosterObject.setWinterName(rosterFields[12]);
                            rosterObject.setWinterAddress1(rosterFields[13]);
                            rosterObject.setWinterAddress2(rosterFields[14]);
                            rosterObject.setWinterCity(rosterFields[15]);
                            rosterObject.setWinterState(rosterFields[16]);
                            rosterObject.setWinterZip(rosterFields[17]);
                            rosterObject.setWinterPhone(rosterFields[18]);
                            rosterObject.setWinterEmail(rosterFields[19]);
                            rosterObject.setMemberNumber(rosterFields[20]);
                            rosterObject.setStatus(rosterFields[21]);
                            rosterObject.setEmergencyName(rosterFields[22]);
                            rosterObject.setEmergencyPhoneNumber(rosterFields[23]);
                            rosterObject.setActivationDate(rosterFields[24]);
                            rosterObject.setGroups(rosterFields[25]);



                            rosterObjects.add(rosterObject);



                        }

                        String rosterStringToAdd = "";

                        for (RosterObject object : rosterObjects) {
                            rosterStringToAdd = rosterStringToAdd +
                                    object.getLastName() + "\t" +
                                    object.getFirstName() + "\t" +
                                    object.getMiddleName() + "\t" +
                                    object.getHomeAddress1() + "\t" +
                                    object.getHomeAddress2() + "\t" +
                                    object.getHomeCity() + "\t" +
                                    object.getHomeState() + "\t" +
                                    object.getHomeZip() + "\t" +
                                    object.getHomePhone() + "\t" +
                                    object.getEmail() + "\t" +
                                    object.getWinterAddress1() + "\t" +
                                    object.getWinterAddress2() + "\t" +
                                    object.getWinterCity() + "\t" +
                                    object.getWinterState() + "\t" +
                                    object.getWinterZip() + "\t" +
                                    object.getWinterPhone() + "\t" +
                                    object.getWinterEmail() + "\t" +
                                    object.getMobilePhone() + "\t" +
                                    object.getEmergencyName() + "\t" +
                                    object.getEmergencyPhoneNumber() + "\n";
                        }



                        rosterFileToEmail = "Last Name\tFirst Name\tMiddle Name\tHOA Address Address 1\tHOA Address Address 2\t" +
                                "HOA Address City\tHOA Address State\tHOA Address Zip\tHOA Address Phone\tHOA Address E-Mail\tOther Address Address 1\t" +
                                "Other Address Address 2\tOther Address City\tOther Address State\tOther Address Zip\tOther Address Phone\t" +
                                "Other Address E-Mail\tMobile Phone\tEmergency Contact\tEmergency Number\n" + rosterStringToAdd;


                        Log.d(TAG, "update roster --->" + rosterFileToEmail);



                        try {
                            directoryToEmail = new File(getApplicationContext().getExternalFilesDir(null), "HOADirectory.txt");
                            directoryToEmail.deleteOnExit();
                            FileOutputStream fileoutputstream = new FileOutputStream(directoryToEmail.getPath());
                            fileoutputstream.write(rosterFileToEmail.getBytes());
                            fileoutputstream.flush();
                            fileoutputstream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("application/txt");
                        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(directoryToEmail));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Directory Text for " + installation.getString("AssociationCode"));
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Text File for HOA Directory");
                        startActivity(Intent.createChooser(emailIntent , "Send email..."));
                        finish();






                    }
                });

            }
        });


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



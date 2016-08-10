package comapps.com.myassociationhoa.maintenance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.MaintenanceObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class MaintenanceComment extends AppCompatActivity {

    private static final String TAG = "POPMAINTENANCECOMMENT";
    public static final String MYPREFERENCES = "MyPrefs";

    ParseQuery<ParseObject> query;
    String[] maintenanceFileArray;
    String maintenanceFileString;
    String maintenanceFileUpdate = "";
    MaintenanceObject maintenanceObject;

    EditText description;
    EditText notes;
    Button saveButton;
    Button mTypeButton;

    SharedPreferences sharedPreferences;

    int maintenanceIndex;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_maintenance_comment);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Edit Maintenance Item");
        }



        description = (EditText) findViewById(R.id.editTextDescription);
        notes = (EditText) findViewById(R.id.editTextNotes);
        saveButton = (Button) findViewById(R.id.buttonAddComment);
        mTypeButton = (Button) findViewById(R.id.buttonMaintenanceType);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            maintenanceIndex = extras.getInt("MAINTENANCEINDEX");
            Gson gson = new Gson();
            maintenanceObject = gson.fromJson(extras.getString("MAINTENANCEOBJECT"), MaintenanceObject.class);
            Log.d(TAG, "maintenance index ---> " + maintenanceIndex);

        }

        description.setText(maintenanceObject.getMaintenanceDesc());
        notes.setText(maintenanceObject.getMaintenanceNotes());
        mTypeButton.setText(maintenanceObject.getMaintenanceCategory());



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width * 1, height * 1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                query = new ParseQuery<ParseObject>(installation.getString("AssociationCode"));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, ParseException e) {


                        ParseFile messageFile = assoc.get(0).getParseFile("MessageFile");
                        maintenanceFileArray = null;

                        try {
                            byte[] file = messageFile.getData();
                            try {
                                maintenanceFileString = new String(file, "UTF-8");

                                Log.d(TAG, "existing mitems --->" + maintenanceFileString);

                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                      /*  String memberInfo = sharedPreferences.getString("MEMBER_INFO", "");
                        String[] memberInfoArray = memberInfo.split("\\^");


                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy H:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        String strDate = sdf.format(c.getTime());
                        String strDate2 = sdf2.format(c.getTime());*/



                       maintenanceFileUpdate = maintenanceFileString.replace(maintenanceObject.getMaintenanceDesc(), description.getText());
                       String maintenanceFileUpdateForUpload = maintenanceFileUpdate.replace(maintenanceObject.getMaintenanceNotes(), notes.getText());




                        Log.i(TAG, "maintenanceFileUpdate ---->" + maintenanceFileUpdateForUpload + "<----");

                        byte[] data = maintenanceFileUpdateForUpload.getBytes();
                        ParseFile MaintenanceFile = new ParseFile("Maintenance.txt", data);

                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy, hh:mm a");
                        String formattedDate = df.format(c.getTime());


                        try {
                            MaintenanceFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("MaintenanceFile", MaintenanceFile);
                        assoc.get(0).put("MaintenanceDate", formattedDate);

                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        Toast.makeText(getBaseContext(), "MAINTENANCE ITEM UPDATED", Toast.LENGTH_LONG).show();


                        Intent mainActivity = new Intent();
                        mainActivity.setClass(getApplicationContext(), MaintenanceActivityWithFragment.class);
                        startActivity(mainActivity);
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

        Intent intentMain = new Intent();
        intentMain.setClass(MaintenanceComment.this, MaintenanceActivityWithFragment.class);
        MaintenanceComment.this.finish();
        startActivity(intentMain);

    }


}



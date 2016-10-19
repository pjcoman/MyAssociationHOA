package comapps.com.myassociationhoa.maintenance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.objects.MaintenanceObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class MaintenanceComment extends AppCompatActivity {

    private static final String TAG = "POPMAINTENANCECOMMENT";
    private static final String MYPREFERENCES = "MyPrefs";

    private ParseQuery<ParseObject> query;
    private String[] maintenanceFileArray;
    private String maintenanceFileString;
    private MaintenanceObject maintenanceObject;

    private EditText description;
    private EditText notes;
    private Button saveButton;
    private Button mTypeButton;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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

            Gson gson = new Gson();
            maintenanceObject = gson.fromJson(extras.getString("MAINTENANCEOBJECT"), MaintenanceObject.class);


        }

        description.setText(maintenanceObject.getMaintenanceDesc());
        notes.setText(maintenanceObject.getMaintenanceNotes());
        mTypeButton.setText(maintenanceObject.getMaintenanceCategory());


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, height);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                query = new ParseQuery<>(installation.getString("AssociationCode"));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, ParseException e) {


                        ParseFile maintenanceFile = assoc.get(0).getParseFile("MaintenanceFile");
                        maintenanceFileArray = null;


                        byte[] file = new byte[0];
                        try {
                            file = maintenanceFile.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        try {
                            maintenanceFileString = new String(file, "UTF-8");
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }




                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy h:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        String strDate = sdf.format(c.getTime());
                        String strDate2 = sdf2.format(c.getTime());


                        Log.d(TAG, "existing maintenance items --->" + maintenanceFileString);

                        String maintenanceObjectToUpdate = maintenanceObject.getMaintenanceName() + "^" + maintenanceObject.getMaintenanceDate() + "^" +
                                maintenanceObject.getMaintenanceDesc() + "^" + maintenanceObject.getMaintenanceNotes() + "^" + maintenanceObject.getMaintenanceCategory();

                        String maintenanceObjectUpdated = maintenanceObject.getMaintenanceName() + "^" + maintenanceObject.getMaintenanceDate() +
                                "^" + description.getText().toString() + "^" + notes.getText().toString() + "^" + maintenanceObject.getMaintenanceCategory();
                        Log.d(TAG, "existing maintenance to update --->" + maintenanceObjectToUpdate);
                        Log.d(TAG, "existing maintenance item updated --->" + maintenanceObjectUpdated);

                    //    String maintenanceFileUpdate = maintenanceFileString.replaceAll(maintenanceObjectToUpdate, maintenanceObjectUpdated);

                        String maintenanceFileUpdate = maintenanceFileString.replace(maintenanceObjectToUpdate, maintenanceObjectUpdated);


                        Log.i(TAG, "maintenanceFileUpdate ----> " + maintenanceFileUpdate + " <----");

                        String maintenanceFileUpdateForUpload = maintenanceFileUpdate.trim();

                        byte[] data = maintenanceFileUpdateForUpload.getBytes();
                        ParseFile MaintenanceFile = new ParseFile("Maintenance.txt", data);



                        try {
                            MaintenanceFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("MaintenanceFile", MaintenanceFile);
                        assoc.get(0).put("MaintenanceDate", strDate);

                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            assoc.get(0).saveEventually();
                        }

                       /* editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String jsonMaintenanceObject = gson.toJson(maintenanceObject);
                        editor.putString("maintenanceObject" + "[" + maintenanceIndex + "]", jsonMaintenanceObject);
                        editor.apply();*/


                        Toast toast = Toast.makeText(getBaseContext(), "MAINTENANCE ITEM UPDATED", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();


                        AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                        remoteDataTaskClass.execute();


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
        intentMain.setClass(MaintenanceComment.this, MaintenanceActivity.class);
        MaintenanceComment.this.finish();
        startActivity(intentMain);

    }


}



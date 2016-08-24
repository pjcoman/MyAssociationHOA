package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.ToolsActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class BackupRestoreFilesActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "BACKUPFILESACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";
    public static final String VISITEDPREFERENCES = "VisitedPrefs";

    SharedPreferences sharedPreferences;

    ParseQuery<ParseObject> query;
    ParseFile file;
    ParseInstallation installation;

    String strDate;

    TextView associationName;
    TextView tvRosterBackup;
    TextView tvServiceBackup;
    TextView tvGuestBackup;
    TextView tvPetsBackup;
    TextView tvAutoBackup;
    TextView tvRosterBackupDate;
    TextView tvServiceBackupDate;
    TextView tvGuestBackupDate;
    TextView tvPetsBackupDate;
    TextView tvAutoBackupDate;
    TextView tvCancel;

    TextView title2;

    byte[] backupFileData;

    Bundle bundle;

    Boolean fromRestore;
    Boolean fromBackup;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.backup_assoc_file_layout);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
          /*  bar.setTitle("Update Pet Info");*/
        }

        title2 = (TextView) findViewById(R.id.textViewTitle2);

        bundle = getIntent().getExtras();

        if ( bundle != null ) {

            fromRestore = bundle.getBoolean("FROMTOOLSRESTORE");
            if ( fromRestore ) {
               title2.setText("Restore Association Files");
            } else {
                title2.setText("Backup Association Files");
            }




        }

        associationName = (TextView) findViewById(R.id.textViewAssocName);
        tvRosterBackup = (TextView) findViewById(R.id.textViewRosterBU);
        tvServiceBackup = (TextView) findViewById(R.id.textViewServiceProviderBU);
        tvGuestBackup = (TextView) findViewById(R.id.textViewGuestBU);
        tvPetsBackup = (TextView) findViewById(R.id.textViewPetsBU);
        tvAutoBackup = (TextView) findViewById(R.id.textViewAutoBU);
        tvRosterBackupDate = (TextView) findViewById(R.id.textViewRosterBUDate);
        tvServiceBackupDate = (TextView) findViewById(R.id.textViewServiceProviderBUDate);
        tvGuestBackupDate = (TextView) findViewById(R.id.textViewGuestBUDate);
        tvPetsBackupDate = (TextView) findViewById(R.id.textViewPetsBUDate);
        tvAutoBackupDate = (TextView) findViewById(R.id.textViewAutoBUDate);
        tvCancel = (TextView) findViewById(R.id.textViewCancel);

        associationName.setText(sharedPreferences.getString("defaultRecord(30)", ""));


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .7));



        installation = ParseInstallation.getCurrentInstallation();
        query = new ParseQuery<ParseObject>(installation.getString("AssociationCode"));
        try {

            tvRosterBackupDate.setText("last backed up " + query.getFirst().getString("RosterBackupDate"));
            tvServiceBackupDate.setText("last backed up " + query.getFirst().getString("ProviderBackupDate"));
            tvGuestBackupDate.setText("last backed up " + query.getFirst().getString("GuestBackupDate"));
            tvPetsBackupDate.setText("last backed up " + query.getFirst().getString("PetBackupDate"));
            tvAutoBackupDate.setText("last backed up " + query.getFirst().getString("AutoBackupDate"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvRosterBackup.setOnClickListener(this);
        tvServiceBackup.setOnClickListener(this);
        tvGuestBackup.setOnClickListener(this);
        tvPetsBackup.setOnClickListener(this);
        tvAutoBackup.setOnClickListener(this);
        tvCancel.setOnClickListener(this);



            }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {

        Intent intentMain = new Intent();
        intentMain.setClass(BackupRestoreFilesActivity.this, ToolsActivity.class);
        BackupRestoreFilesActivity.this.finish();
        startActivity(intentMain);

    }


    @Override
    public void onClick(View v) {

        query = new ParseQuery<ParseObject>(installation.getString("AssociationCode"));
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, H:mm a");
        strDate = sdf.format(c.getTime());

        Log.d(TAG, "view clicked ----> " + v.getId());

        if(v == tvRosterBackup) {



            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> assoc, ParseException e) {


                    if ( fromRestore ) {
                        file = assoc.get(0).getParseFile("RosterBackupFile");
                    } else {
                        file = assoc.get(0).getParseFile("RosterFile");
                    }


                    try {
                        backupFileData = file.getData();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    file = null;

                    if ( fromRestore ) {

                        file = new ParseFile("Roster.txt", backupFileData);

                    } else {

                        file = new ParseFile("RosterBackup.txt", backupFileData);
                    }

                    if ( fromRestore ) {


                        assoc.get(0).put("RosterFileDate", strDate);
                        assoc.get(0).put("RosterFile", file);

                    } else {


                        assoc.get(0).put("RosterBackupDate", strDate);
                        assoc.get(0).put("RosterBackupFile", file);
                    }





                    try {
                        assoc.get(0).save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    if ( fromRestore ) {


                        Toast toast = Toast.makeText(getBaseContext(), "Roster restored from Roster Backup.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {


                        Toast toast = Toast.makeText(getBaseContext(), "Roster Backup Updated", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }




                }
            });

        } else if ( v == tvServiceBackup ) {

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> assoc, ParseException e) {

                    if ( fromRestore ) {
                        file = assoc.get(0).getParseFile("ProviderBackupFile");
                    } else {
                        file = assoc.get(0).getParseFile("ProviderFile");
                    }

                    try {
                        backupFileData = file.getData();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    file = null;


                    if ( fromRestore ) {

                        file = new ParseFile("Provider.txt", backupFileData);

                    } else {

                        file = new ParseFile("ProviderBackup.txt", backupFileData);
                    }

                    if ( fromRestore ) {

                        assoc.get(0).put("ProviderDate", strDate);
                        assoc.get(0).put("ProviderFile", file);


                    } else {

                        assoc.get(0).put("ProviderBackupDate", strDate);
                        assoc.get(0).put("ProviderBackupFile", file);

                    }




                    try {
                        assoc.get(0).save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }



                    if ( fromRestore ) {


                        Toast toast = Toast.makeText(getBaseContext(), "Service Providers restored from Backup.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {


                        Toast toast = Toast.makeText(getBaseContext(), "Service Provider Backup Updated", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }




                }
            });

        } else if ( v == tvGuestBackup ) {

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> assoc, ParseException e) {

                    if ( fromRestore ) {
                        file = assoc.get(0).getParseFile("GuestBackupFile");
                    } else {
                        file = assoc.get(0).getParseFile("GuestFile");
                    }

                    try {
                        backupFileData = file.getData();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    file = null;

                    if ( fromRestore ) {

                        file = new ParseFile("GuestBackup.txt", backupFileData);

                    } else {
                        file = new ParseFile("GuestFile.txt", backupFileData);
                    }

                    if ( fromRestore ) {

                        assoc.get(0).put("GuestDate", strDate);
                        assoc.get(0).put("GuestFile", file);

                    } else {
                        assoc.get(0).put("GuestBackupDate", strDate);
                        assoc.get(0).put("GuestBackupFile", file);
                    }





                    try {
                        assoc.get(0).save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }



                    if ( fromRestore ) {


                        Toast toast = Toast.makeText(getBaseContext(), "Guests restored from Backup.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {


                        Toast toast = Toast.makeText(getBaseContext(), "Guests Backup Updated", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }



                }
            });

        } else if ( v == tvPetsBackup) {

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> assoc, ParseException e) {
                    if ( fromRestore ) {
                        file = assoc.get(0).getParseFile("PetBackupFile");
                    } else {
                        file = assoc.get(0).getParseFile("PetFile");
                    }


                    try {
                        backupFileData = file.getData();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    file = null;

                    if ( fromRestore ) {
                        file = new ParseFile("PetFile.txt", backupFileData);
                    } else {
                        file = new ParseFile("PetBackup.txt", backupFileData);
                    }


                    if ( fromRestore ) {
                        assoc.get(0).put("PetDate", strDate);
                        assoc.get(0).put("PetFile", file);
                    } else {
                        assoc.get(0).put("PetBackupDate", strDate);
                        assoc.get(0).put("PetBackupFile", file);
                    }




                    try {
                        assoc.get(0).save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }



                    if ( fromRestore ) {


                        Toast toast = Toast.makeText(getBaseContext(), "Pets restored from Backup.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {


                        Toast toast = Toast.makeText(getBaseContext(), "Pets Backup Updated", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }



                }
            });

        } else if ( v == tvAutoBackup) {

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> assoc, ParseException e) {

                    if ( fromRestore ) {

                        file = assoc.get(0).getParseFile("AutoBackupFile");

                    } else {

                        file = assoc.get(0).getParseFile("AutoFile");
                    }

                    try {
                        backupFileData = file.getData();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    file = null;

                    if ( fromRestore ) {

                        file = new ParseFile("AutoFile.txt", backupFileData);

                    } else {

                        file = new ParseFile("AutoBackup.txt", backupFileData);
                    }


                    if ( fromRestore ) {

                        assoc.get(0).put("AutoDate", strDate);
                        assoc.get(0).put("AutoFile", file);

                    } else {

                        assoc.get(0).put("AutoBackupDate", strDate);
                        assoc.get(0).put("AutoBackupFile", file);
                    }





                    try {
                        assoc.get(0).save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }



                    if ( fromRestore ) {


                        Toast toast = Toast.makeText(getBaseContext(), "Auto restored from Backup.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {


                        Toast toast = Toast.makeText(getBaseContext(), "Auto Backup Updated", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }


                }
            });

        } else if ( v == tvCancel) {

            finish();

        }


            thread.start();


    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(2500); // As I am using LENGTH_LONG in Toast

                finish();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}



package comapps.com.myassociationhoa.autos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.myinfo.PopInfo;
import comapps.com.myassociationhoa.objects.AutoObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopAutosAddAuto extends AppCompatActivity {

    private static final String TAG = "POPADDAUTO";
    public static final String MYPREFERENCES = "MyPrefs";
    public static final String VISITEDPREFERENCES = "VisitedPrefs";


    ParseQuery<ParseObject> query;
    String[] autoFileArray;
    String autoFileString = "";
    String autoFileUpdate = "";

    EditText etAutoMan;
    EditText etAutoModel;
    EditText etAutoYear;
    EditText etAutoColor;
    EditText etAutoLicense;
    EditText etAutoTag;

    TextView title;

    Button saveButton;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesVisited;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorVisited;

    Bundle bundle;
    Boolean forEdit = false;
    Boolean forDelete = false;

    String autoPassed;

    AutoObject autoObject;

    Toast toast;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_autos);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Add Auto");
        }

        saveButton = (Button) findViewById(R.id.buttonSaveMessage);
        title = (TextView) findViewById(R.id.textViewTitle);

        bundle = getIntent().getExtras();

        if ( bundle != null ) {

            forEdit = bundle.getBoolean("FOREDIT");
            forDelete = bundle.getBoolean("FORDELETE");
            autoPassed = bundle.getString("AUTOFOREDIT");

            Gson gson = new Gson();
            autoObject = gson.fromJson(autoPassed, AutoObject.class);

            Log.d(TAG, "auto object passed ----> " + autoObject.toString());



        }




        etAutoMan = (EditText) findViewById(R.id.editTextAutoMake);
        etAutoModel = (EditText) findViewById(R.id.editTextAutoModel);
        etAutoYear = (EditText) findViewById(R.id.editTextAutoYear);
        etAutoColor = (EditText) findViewById(R.id.editTextAutoColor);
        etAutoLicense = (EditText) findViewById(R.id.editTextAutoLicense);
        etAutoTag = (EditText) findViewById(R.id.editTextHoaTag);

        if ( forEdit ) {

            etAutoMan.setText(autoObject.getMake());
            etAutoModel.setText(autoObject.getModel());
            etAutoYear.setText(autoObject.getYear());
            etAutoColor.setText(autoObject.getColor());
            etAutoLicense.setText(autoObject.getPlate());
            etAutoTag.setText(autoObject.getTag());
            title.setText("Update Auto");
            bar.setTitle("Update Auto");

        }

        if ( forDelete ) {

            etAutoMan.setText(autoObject.getMake());
            etAutoModel.setText(autoObject.getModel());
            etAutoYear.setText(autoObject.getYear());
            etAutoColor.setText(autoObject.getColor());
            etAutoLicense.setText(autoObject.getPlate());
            etAutoTag.setText(autoObject.getTag());
            title.setText("Delete Auto");
            saveButton.setText("DELETE");
            saveButton.setTextColor(Color.RED);

            bar.setTitle("Delete Auto");

        }



        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_APPEND);


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


                        ParseFile autoFile = assoc.get(0).getParseFile("AutoFile");
                        autoFileArray = null;

                        try {
                            byte[] file = autoFile.getData();
                            try {
                                autoFileString = new String(file, "UTF-8");

                                Log.d(TAG, "existing autos --->" + autoFileString);

                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        String memberInfo = sharedPreferences.getString("MEMBER_INFO", "");

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, H:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        String strDate = sdf.format(c.getTime());




                        int lengthOfAutoFileString = autoFileString.length();


                        if ( forEdit ) {

                            String preEditAuto = autoObject.getMake() + "^" +
                                    autoObject.getModel() + "^" +
                                    autoObject.getColor() + "^" +
                                    autoObject.getYear() + "^" +
                                    autoObject.getPlate() + "^" +
                                    autoObject.getTag();

                            String postEditAuto = etAutoMan.getText() +
                                    "^" + etAutoModel.getText() +
                                    "^" + etAutoColor.getText() +
                                    "^" + etAutoYear.getText() +
                                    "^" + etAutoLicense.getText() +
                                    "^" + etAutoTag.getText();


                            autoFileUpdate = autoFileString.replace(preEditAuto, postEditAuto);

                            Log.d(TAG, "update autos --->" + autoFileUpdate);



                        } else if ( forDelete ) {

                            String preEditAuto = "|" + installation.getString("memberName") + "^" +
                                    installation.getString("memberNumber") + "^" + autoObject.getMake() + "^" +
                                    autoObject.getModel() + "^" +
                                    autoObject.getColor() + "^" +
                                    autoObject.getYear() + "^" +
                                    autoObject.getPlate() + "^" +
                                    autoObject.getTag();

                            autoFileUpdate = autoFileString.replace(preEditAuto, "");

                            Log.d(TAG, "update autos --->" + autoFileUpdate);


                        } else {

                            String newAuto = "|" + installation.getString("memberName") + "^" +
                                    installation.getString("memberNumber") + "^" + etAutoMan.getText() +
                                    "^" + etAutoModel.getText() +
                                    "^" + etAutoColor.getText() +
                                    "^" + etAutoYear.getText() +
                                    "^" + etAutoLicense.getText() +
                                    "^" + etAutoTag.getText();

                            autoFileUpdate = autoFileString + "|" + installation.getString("memberName") + "^" +
                                    installation.getString("memberNumber") + "^" + etAutoMan.getText() +
                                    "^" + etAutoModel.getText() +
                                    "^" + etAutoColor.getText() +
                                    "^" + etAutoYear.getText() +
                                    "^" + etAutoLicense.getText() +
                                    "^" + etAutoTag.getText();

                            if (lengthOfAutoFileString < 1) {

                                autoFileUpdate = autoFileUpdate.substring(1);

                            }

                            editorVisited = sharedPreferencesVisited.edit();

                            if (sharedPreferencesVisited.getString("MYAUTOS", "").equals("")) {
                                editorVisited.putString("MYAUTOS", newAuto.substring(1));
                            } else if (!sharedPreferencesVisited.getString("MYAUTOS", "").contains(newAuto)) {
                                editorVisited.putString("MYAUTOS", sharedPreferencesVisited.getString("MYAUTOS", "") + newAuto);
                            }
                            editorVisited.apply();

                            Log.d(TAG, "update autos --->" + autoFileUpdate);





                        }

                        if ( autoFileUpdate.substring(0,1).equals("|")) {
                            autoFileUpdate = autoFileUpdate.substring(1, autoFileUpdate.length());
                        } else if (autoFileUpdate.substring(autoFileUpdate.length() - 1, autoFileUpdate.length()).equals("|")) {
                            autoFileUpdate = autoFileUpdate.substring(0, autoFileUpdate.length() - 1);
                        }


                        byte[] data = autoFileUpdate.getBytes();
                        autoFile = new ParseFile("AutoFile.txt", data);


                        try {
                            autoFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("AutoDate", strDate);
                        assoc.get(0).put("AutoFile", autoFile);

                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            assoc.get(0).saveEventually();
                        }




                        AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                        remoteDataTaskClass.execute();



                        if ( forEdit ) {
                            toast = Toast.makeText(getBaseContext(), etAutoYear.getText() + " " + etAutoModel.getText() + " updated.", Toast.LENGTH_LONG);
                        } else if ( forDelete) {
                            toast = Toast.makeText(getBaseContext(), etAutoYear.getText() + " " + etAutoModel.getText() + " deleted.", Toast.LENGTH_LONG);
                        } else {
                            toast = Toast.makeText(getBaseContext(), etAutoYear.getText() + " " + etAutoModel.getText() + " added.", Toast.LENGTH_LONG);
                        }

                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();


                        Intent intent = new Intent();
                        intent.setClass(PopAutosAddAuto.this, PopInfo.class);
                        startActivity(intent);

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



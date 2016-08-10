package comapps.com.myassociationhoa.autos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import comapps.com.myassociationhoa.MainActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.AutoObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopAutosAddAuto extends AppCompatActivity {

    private static final String TAG = "POPADDPET";
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

    Button saveButton;

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

        setContentView(R.layout.pop_up_layout_autos);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Update Auto Info");
        }


        etAutoMan = (EditText) findViewById(R.id.editTextEventTitle);
        etAutoModel = (EditText) findViewById(R.id.editTextEventDetail);
        etAutoYear = (EditText) findViewById(R.id.editTextStartDate);
        etAutoColor = (EditText) findViewById(R.id.editTextAutoColor);
        etAutoLicense = (EditText) findViewById(R.id.editTextAutoLicense);
        etAutoTag = (EditText) findViewById(R.id.editTextHoaTag);

        saveButton = (Button) findViewById(R.id.buttonSaveMessage);

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

                        String newAuto = "|" + sharedPreferences.getString("MEMBERNAME", "") + "^" +
                                sharedPreferences.getString("MEMBERNUMBER", "") + "^" + etAutoMan.getText() +
                                "^" + etAutoModel.getText() +
                                "^" + etAutoColor.getText() +
                                "^" + etAutoYear.getText() +
                                "^" + etAutoLicense.getText() +
                                "^" + etAutoTag.getText();

                        autoFileUpdate = autoFileString + "|" + sharedPreferences.getString("MEMBERNAME", "") + "^" +
                                sharedPreferences.getString("MEMBERNUMBER", "") + "^" + etAutoMan.getText() +
                                "^" + etAutoModel.getText() +
                                "^" + etAutoColor.getText() +
                                "^" + etAutoYear.getText() +
                                "^" + etAutoLicense.getText() +
                                "^" + etAutoTag.getText();

                        if ( lengthOfAutoFileString < 1 ) {

                            autoFileUpdate = autoFileUpdate.substring(1);

                        }

                        editorVisited = sharedPreferencesVisited.edit();

                        if ( sharedPreferencesVisited.getString("MYAUTOS", "").equals("")) {
                           editorVisited.putString("MYAUTOS", newAuto.substring(1));
                        } else if (!sharedPreferencesVisited.getString("MYAUTOS","").contains(newAuto)){
                           editorVisited.putString("MYAUTOS", sharedPreferencesVisited.getString("MYAUTOS","") + newAuto);
                        }
                        editorVisited.apply();

                        Log.d(TAG, "update autos --->" + autoFileUpdate);



                        AutoObject autoObject = new AutoObject();
                        autoObject.setOwner(sharedPreferences.getString("MEMBERNAME", ""));
                        autoObject.setMemberNumber(sharedPreferences.getString("MEMBERNUMBER", ""));
                        autoObject.setMake(String.valueOf(etAutoMan.getText()));
                        autoObject.setModel(String.valueOf(etAutoModel.getText()));
                        autoObject.setYear(String.valueOf(etAutoYear.getText()));
                        autoObject.setColor(String.valueOf(etAutoColor.getText()));
                        autoObject.setPlate(String.valueOf(etAutoLicense.getText()));
                        autoObject.setTag(String.valueOf(etAutoTag.getText()));

                        Integer autoSizeInt = sharedPreferences.getInt("autoObjectsSize", 0);

                        editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String jsonAutoObject = gson.toJson(autoObject);
                        editor.putString("autoObject" + "[" + String.valueOf(autoSizeInt) + "]", jsonAutoObject);
                        editor.putInt("autoObjectsSize", autoSizeInt + 1);
                        editor.apply();


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
                        }


                        Toast toast = Toast.makeText(getBaseContext(), autoObject.getYear() + " " + autoObject.getMake() + " added.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();



                        Intent mainActivity = new Intent();
                        mainActivity.setClass(getApplicationContext(), MainActivity.class);
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
        intentMain.setClass(PopAutosAddAuto.this, AutosActivity.class);
        PopAutosAddAuto.this.finish();
        startActivity(intentMain);

    }


}



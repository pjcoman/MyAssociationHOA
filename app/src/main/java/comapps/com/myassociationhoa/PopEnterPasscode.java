package comapps.com.myassociationhoa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
@SuppressWarnings("ALL")
public class PopEnterPasscode extends AppCompatActivity {

    private static final String TAG = "POPENTERPASSCODE";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";
    private static final String ASSOCPREFERENCES = "AssocPrefs";

    private EditText passCode;
    private CheckBox checkBox;
    private Button cancelButton;
    private Button okButton;

    private ParseQuery<ParseObject> query;

    private Bundle bundle;

    private Boolean correctPasscode = true;


    private ArrayList<String> associationsJoinedArray;

    private String[] assocJoinedStringArray;
    private String assocJoinedUpdate;
    private Boolean fromChangeAddActivity;
    private String oldMemberNumber;
    private int numberOfPasscodes;


    private int passCodeSize;

    private Map<String, String> mapPasswordsMember;
    private Map<String, String> mapPasswordsAdmin;
    private Map<String, String> mapAssocLongNameMember;
    private Map<String, String> mapAssocLongNameAdmin;




    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences sharedPreferencesAssoc;

    private SharedPreferences.Editor editorVisited;

    private ParseInstallation installation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_enter_passcode);

        sharedPreferencesAssoc = getSharedPreferences(ASSOCPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);


        passCode = (EditText) findViewById(R.id.editTextPassCode);
        cancelButton = (Button) findViewById(R.id.buttonCancel);
        okButton = (Button) findViewById(R.id.buttonOK);

        okButton.setEnabled(false);

        showSoftInputFromWindow(this, passCode);

/*

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .9));
*/

        if ( sharedPreferencesVisited.getBoolean("visitedBefore", false)) {

            query = ParseQuery.getQuery("Home");

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    try {
                        ParseFile parseFile = (ParseFile) query.getFirst().get("NewHOAFile");
                        byte[] data = parseFile.getData();

                        String codesFileString = null;


                        try {
                            codesFileString = new String(data, "UTF-8");
                        } catch (UnsupportedEncodingException e2) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "not first visit");


                        String passCodes[] = codesFileString != null ? codesFileString.split("(\\|)|(\\^)", -1) : new String[0];

                        SharedPreferences.Editor editor = sharedPreferencesAssoc.edit();

                        int i = 0;
                        int j = 1;
                        int k = 0;
                        for (String passcode : passCodes) {
                            //   Log.d(TAG, "passcode member ----> " + passcode);


                            switch (j) {
                                case 1:
                                    i++;
                                    j++;
                                    k++;

                                    editor.putString("passcodeASSOC_LONGNAME" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc Long Name ----> " + passcode);
                                    break;
                                case 2:
                                    j++;
                                    k++;

                                    editor.putString("passcodeASSOC_NAME" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc name for Parse ----> " + passcode);
                                    break;
                                case 3:
                                    j++;
                                    k++;

                                    editor.putString("passcodeADMIN_PW" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc admin password ----> " + passcode);
                                    break;
                                case 4:
                                    j++;
                                    k++;

                                    editor.putString("passcodeMEMBER_PW" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc member password ----> " + passcode);
                                    break;
                                case 5:
                                    j = 1;
                                    k++;

                                    editor.putString("passcodeASSOC_SHORTNAME" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc short name ----> " + passcode);
                                    break;
                            }

                        }
                        Log.d(TAG, "passcode size is -----> " + k);
                        editor.putInt("passcodeSize", k);
                        editor.apply();


                    } catch (ParseException e1) {
                        e.printStackTrace();
                    }




                    mapPasswordsMember = new HashMap<>();
                    mapPasswordsAdmin = new HashMap<>();
                    mapAssocLongNameMember = new HashMap<>();
                    mapAssocLongNameAdmin = new HashMap<>();

                    passCodeSize = sharedPreferencesAssoc.getInt("passcodeSize", 0);

                    for (int i = 1; i <= passCodeSize / 5; i++) {

                        mapAssocLongNameAdmin.put(sharedPreferencesAssoc.getString("passcodeADMIN_PW(" + String.valueOf(i) + ")", ""),
                                sharedPreferencesAssoc.getString("passcodeASSOC_LONGNAME(" + String.valueOf(i) + ")", ""));

                        mapAssocLongNameMember.put(sharedPreferencesAssoc.getString("passcodeMEMBER_PW(" + String.valueOf(i) + ")", ""),
                                sharedPreferencesAssoc.getString("passcodeASSOC_LONGNAME(" + String.valueOf(i) + ")", ""));

                        mapPasswordsAdmin.put(sharedPreferencesAssoc.getString("passcodeADMIN_PW(" + String.valueOf(i) + ")", ""),
                                sharedPreferencesAssoc.getString("passcodeASSOC_NAME(" + String.valueOf(i) + ")", ""));

                        mapPasswordsMember.put(sharedPreferencesAssoc.getString("passcodeMEMBER_PW(" + String.valueOf(i) + ")", ""),
                                sharedPreferencesAssoc.getString("passcodeASSOC_NAME(" + String.valueOf(i) + ")", ""));


                    }

                    numberOfPasscodes = sharedPreferencesAssoc.getInt("passcodeSize", 0);
                    Log.d(TAG, "number of passcodes -----> " + numberOfPasscodes);


                    okButton.setEnabled(true);
                }
            });

        } else {

            query = ParseQuery.getQuery("Home");

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    try {
                        ParseFile parseFile = (ParseFile) query.getFirst().get("NewHOAFile");
                        byte[] data = parseFile.getData();

                        String codesFileString = null;


                        try {
                            codesFileString = new String(data, "UTF-8");
                        } catch (UnsupportedEncodingException e3) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "first visit codes from cloud" + codesFileString);


                        String passCodes[] = codesFileString != null ? codesFileString.split("(\\|)|(\\^)", -1) : new String[0];

                        SharedPreferences.Editor editor = sharedPreferencesAssoc.edit();

                        int i = 0;
                        int j = 1;
                        int k = 0;
                        for (String passcode : passCodes) {
                            //   Log.d(TAG, "passcode member ----> " + passcode);


                            switch (j) {
                                case 1:
                                    i++;
                                    j++;
                                    k++;

                                    editor.putString("passcodeASSOC_LONGNAME" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc Long Name ----> " + passcode);
                                    break;
                                case 2:
                                    j++;
                                    k++;

                                    editor.putString("passcodeASSOC_NAME" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc name for Parse ----> " + passcode);
                                    break;
                                case 3:
                                    j++;
                                    k++;

                                    editor.putString("passcodeADMIN_PW" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc admin password ----> " + passcode);
                                    break;
                                case 4:
                                    j++;
                                    k++;

                                    editor.putString("passcodeMEMBER_PW" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc member password ----> " + passcode);
                                    break;
                                case 5:
                                    j = 1;
                                    k++;

                                    editor.putString("passcodeASSOC_SHORTNAME" + "(" + i + ")", passcode);
                                    Log.d(TAG, "Assoc short name ----> " + passcode);
                                    break;
                            }

                        }
                        Log.d(TAG, "passcode size is -----> " + k);
                        editor.putInt("passcodeSize", k);
                        editor.apply();


                    } catch (ParseException e1) {
                        e.printStackTrace();
                    }


                    query = ParseQuery.getQuery("Home");

                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {



                            ParseObject.unpinAllInBackground();
                            ParseObject.pinAllInBackground(objects);

                            Log.d(TAG, "passwords received from cloud than saved locally. ");






                            mapPasswordsMember = new HashMap<>();
                            mapPasswordsAdmin = new HashMap<>();
                            mapAssocLongNameMember = new HashMap<>();
                            mapAssocLongNameAdmin = new HashMap<>();

                            passCodeSize = sharedPreferencesAssoc.getInt("passcodeSize", 0);

                            for (int i = 1; i <= passCodeSize / 5; i++) {

                                mapAssocLongNameAdmin.put(sharedPreferencesAssoc.getString("passcodeADMIN_PW(" + String.valueOf(i) + ")", ""),
                                        sharedPreferencesAssoc.getString("passcodeASSOC_LONGNAME(" + String.valueOf(i) + ")", ""));

                                mapAssocLongNameMember.put(sharedPreferencesAssoc.getString("passcodeMEMBER_PW(" + String.valueOf(i) + ")", ""),
                                        sharedPreferencesAssoc.getString("passcodeASSOC_LONGNAME(" + String.valueOf(i) + ")", ""));

                                mapPasswordsAdmin.put(sharedPreferencesAssoc.getString("passcodeADMIN_PW(" + String.valueOf(i) + ")", ""),
                                        sharedPreferencesAssoc.getString("passcodeASSOC_NAME(" + String.valueOf(i) + ")", ""));

                                mapPasswordsMember.put(sharedPreferencesAssoc.getString("passcodeMEMBER_PW(" + String.valueOf(i) + ")", ""),
                                        sharedPreferencesAssoc.getString("passcodeASSOC_NAME(" + String.valueOf(i) + ")", ""));


                            }

                            numberOfPasscodes = sharedPreferencesAssoc.getInt("passcodeSize", 0);
                            Log.d(TAG, "number of passcodes -----> " + numberOfPasscodes);


                            okButton.setEnabled(true);
                        }
                    });



                    mapPasswordsMember = new HashMap<>();
                    mapPasswordsAdmin = new HashMap<>();
                    mapAssocLongNameMember = new HashMap<>();
                    mapAssocLongNameAdmin = new HashMap<>();

                    passCodeSize = sharedPreferencesAssoc.getInt("passcodeSize", 0);

                    for (int i = 1; i <= passCodeSize / 5; i++) {

                        mapAssocLongNameAdmin.put(sharedPreferencesAssoc.getString("passcodeADMIN_PW(" + String.valueOf(i) + ")", ""),
                                sharedPreferencesAssoc.getString("passcodeASSOC_LONGNAME(" + String.valueOf(i) + ")", ""));

                        mapAssocLongNameMember.put(sharedPreferencesAssoc.getString("passcodeMEMBER_PW(" + String.valueOf(i) + ")", ""),
                                sharedPreferencesAssoc.getString("passcodeASSOC_LONGNAME(" + String.valueOf(i) + ")", ""));

                        mapPasswordsAdmin.put(sharedPreferencesAssoc.getString("passcodeADMIN_PW(" + String.valueOf(i) + ")", ""),
                                sharedPreferencesAssoc.getString("passcodeASSOC_NAME(" + String.valueOf(i) + ")", ""));

                        mapPasswordsMember.put(sharedPreferencesAssoc.getString("passcodeMEMBER_PW(" + String.valueOf(i) + ")", ""),
                                sharedPreferencesAssoc.getString("passcodeASSOC_NAME(" + String.valueOf(i) + ")", ""));


                    }

                    numberOfPasscodes = sharedPreferencesAssoc.getInt("passcodeSize", 0);
                    Log.d(TAG, "number of passcodes -----> " + numberOfPasscodes);


                    okButton.setEnabled(true);


                }
            });
        }






        bundle = getIntent().getExtras();

        if ( bundle != null ) {

            fromChangeAddActivity = bundle.getBoolean("FROMCHANGEADD");
            oldMemberNumber = bundle.getString("OLDMEMBERNUMBER");


            Log.d(TAG, "from change add ----> " + fromChangeAddActivity);


        } else {

            fromChangeAddActivity = false;

        }

        if (sharedPreferencesVisited.getBoolean("visitedBefore", false)) {

            assocJoinedStringArray = sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", "").split("\\|", -1);

            associationsJoinedArray = new ArrayList<>();

            Log.d(TAG, "ASSOCIATIONS_JOINED ----> " + sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", ""));

            for (String assoc : assocJoinedStringArray) {

                Log.d(TAG, "assocJoinedStringArray member ----> " + assoc);
                String[] assocArray = assoc.split("\\^", -1);
                associationsJoinedArray.add(assocArray[2]);

            }


        }



        installation = ParseInstallation.getCurrentInstallation();




        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);


                String passCodeEntered = passCode.getText().toString();

                if (mapPasswordsMember.containsKey(passCodeEntered)) {

                    if (sharedPreferencesVisited.getString("ASSOCIATIONS", "").contains(mapPasswordsMember.get(passCodeEntered) + "Member")) {

                        Toast toast = Toast.makeText(getBaseContext(), "Already a member.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {

                        installation.put("MemberType", "Member");
                        installation.put("AssociationCode", mapPasswordsMember.get(passCodeEntered));

                        try {
                            installation.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        assocJoinedUpdate = sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", "") +
                                "|" + mapAssocLongNameMember.get(passCodeEntered) + "^" + "Member" + "^" + mapPasswordsMember.get(passCodeEntered);

                        if (sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", "").equals("")) {
                            assocJoinedUpdate = assocJoinedUpdate.substring(1, assocJoinedUpdate.length());
                        }

                        Log.d(TAG, "association(s) -----> " + assocJoinedUpdate);


                        editorVisited = sharedPreferencesVisited.edit();
                        editorVisited.putString("ASSOCIATIONS_JOINED", assocJoinedUpdate);
                        editorVisited.putString("ASSOCIATIONS", sharedPreferencesVisited.getString("ASSOCIATIONS", "") + mapPasswordsMember.get(passCodeEntered) + "Member");
                        editorVisited.putBoolean("visitedBefore", true);


                        editorVisited.apply();

                        start();


                    }


                } else if (mapPasswordsAdmin.containsKey(passCodeEntered)) {

                    if (sharedPreferencesVisited.getString("ASSOCIATIONS", "").contains(mapPasswordsAdmin.get(passCodeEntered) + "Admin")) {

                        Toast toast = Toast.makeText(getBaseContext(), "Already an administator.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();



                    } else {

                        installation.put("MemberType", "Administrator");
                        installation.put("AssociationCode", mapPasswordsAdmin.get(passCodeEntered));

                        try {
                            installation.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        assocJoinedUpdate = sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", "") +
                                "|" + mapAssocLongNameAdmin.get(passCodeEntered) + "^" + "Administrator" + "^" + mapPasswordsAdmin.get(passCodeEntered);

                        if (sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", "").equals("")) {
                            assocJoinedUpdate = assocJoinedUpdate.substring(1, assocJoinedUpdate.length());
                        }

                        Log.d(TAG, "association(s) -----> " + assocJoinedUpdate);


                        editorVisited = sharedPreferencesVisited.edit();
                        editorVisited.putString("ASSOCIATIONS_JOINED", assocJoinedUpdate);
                        editorVisited.putString("ASSOCIATIONS", sharedPreferencesVisited.getString("ASSOCIATIONS", "") + mapPasswordsAdmin.get(passCodeEntered) + "Admin");
                        editorVisited.putBoolean("visitedBefore", true);


                        editorVisited.apply();

                        start();

                    }


                } else {

                    Toast toast = Toast.makeText(getBaseContext(), "PASSCODE INCORRECT.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    correctPasscode = false;

                    if ( sharedPreferencesVisited.getInt("PASSCODE_ATTEMPTS", 3) != 0 && sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", "").equals("")) {
                        editorVisited = sharedPreferencesVisited.edit();
                        editorVisited.putInt("PASSCODE_ATTEMPTS", sharedPreferencesVisited.getInt("PASSCODE_ATTEMPTS", 3) - 1);
                        editorVisited.apply();

                        Log.d(TAG, "attempts left ----> " + sharedPreferencesVisited.getInt("PASSCODE_ATTEMPTS", 3));

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    } else if (!sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", "").equals("")) {


                        finish();


                    } else {

                        toast = Toast.makeText(getBaseContext(), "CONTACT YOUR ADMINISTRATOR.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        editorVisited = sharedPreferencesVisited.edit();
                        editorVisited.remove("PASSCODE_ATTEMPTS");
                        editorVisited.apply();

                        Quit();
                    }




                }


            }

                });


                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Shake)
                                .duration(0)
                                .playOn(v);


                        PopEnterPasscode.this.finish();

                    }
                });


            }



   private void start() {

       if (!fromChangeAddActivity) {

           Intent registrationActivity = new Intent();
           registrationActivity.setClass(PopEnterPasscode.this, RegistrationActivity.class);
           startActivity(registrationActivity);
           finish();

       } else if (associationsJoinedArray.contains(installation.get("AssociationCode"))) {

           Log.d(TAG, "member number ----> " + sharedPreferencesVisited.getString(installation.get("AssociationCode") + "MemberNumber", ""));
           installation.put("memberNumber", sharedPreferencesVisited.getString(installation.get("AssociationCode") + "MemberNumber", ""));
           try {
               installation.save();
           } catch (ParseException e) {
               e.printStackTrace();
           }

           Intent changeAddAssocActivity = new Intent();
           changeAddAssocActivity.setClass(PopEnterPasscode.this, MainActivity.class);
           startActivity(changeAddAssocActivity);
           finish();


       } else  {

           Intent regActivity = new Intent();
           regActivity.setClass(getApplicationContext(), RegistrationActivity.class);
           regActivity.putExtra("FROMCHANGEADD", true);
           regActivity.putExtra("OLDMEMBERNUMBER", oldMemberNumber);
           startActivity(regActivity);
           finish();

       }



   }

    private void Quit() {
        super.finish();
    }

    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


}



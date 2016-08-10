package comapps.com.myassociationhoa;

import android.app.ProgressDialog;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopEnterPasscode extends AppCompatActivity {

    private static final String TAG = "POPENTERPASSCODE";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";
    private static final String ASSOCPREFERENCES = "AssocPrefs";

    private EditText passCode;
    private Button cancelButton;
    private Button okButton;

    ParseQuery query;

    Bundle bundle;


    ArrayList<String> associationsJoinedArray;
    String[] assocJoinedStringArray;
    String assocJoinedUpdate;
    Boolean fromChangeAddActivity;
    String oldMemberNumber;

    String rosterString;
    String rosterUpdate;
    String[] rosterFileArray;
    int numberOfMembers;

    private Boolean uniqueMemberNumber = false;

    ProgressDialog progressDialog;

    String numbersFileString;
    String memberNumberRandom;
    String memberInfo;
    String memberName;
    String assocCode;

    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences sharedPreferencesAssoc;

    SharedPreferences.Editor editorVisited;

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
        String numberOfPasscodes = sharedPreferencesAssoc.getString("passcodeSize", "");
        Log.d(TAG, "number of passcodes -----> " + numberOfPasscodes);


        passCode = (EditText) findViewById(R.id.editTextPassCode);
        cancelButton = (Button) findViewById(R.id.buttonCancel);
        okButton = (Button) findViewById(R.id.buttonOK);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .4));

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





        Integer passCodeSize = Integer.valueOf(sharedPreferencesAssoc.getString("passcodeSize", "0"));

        final Map<String, String> mapPasswordsMember = new HashMap<String, String>();
        final Map<String, String> mapPasswordsAdmin = new HashMap<String, String>();
        final Map<String, String> mapAssocLongNameMember = new HashMap<String, String>();
        final Map<String, String> mapAssocLongNameAdmin = new HashMap<String, String>();


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

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);


                String pw = "pw";

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
                                "|" + mapAssocLongNameAdmin.get(passCodeEntered) + "^" + "Administrator   " + "^" + mapPasswordsAdmin.get(passCodeEntered);

                        if (sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", "").equals("")) {
                            assocJoinedUpdate = assocJoinedUpdate.substring(1, assocJoinedUpdate.length());
                        }

                        Log.d(TAG, "association(s) -----> " + assocJoinedUpdate);


                        editorVisited = sharedPreferencesVisited.edit();
                        editorVisited.putString("ASSOCIATIONS_JOINED", assocJoinedUpdate);
                        editorVisited.putString("ASSOCIATIONS", sharedPreferencesVisited.getString("ASSOCIATIONS", "") + mapPasswordsAdmin.get(passCodeEntered) + "Admin");
                        editorVisited.putBoolean("visitedBefore", true);


                        editorVisited.apply();

                    }


                } else {

                    Toast toast = Toast.makeText(getBaseContext(), "PASSCODE INCORRECT.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


                    Intent mainActivity = new Intent();
                    mainActivity.setClass(PopEnterPasscode.this, MainActivity.class);
                    startActivity(mainActivity);


                }


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


                } else {

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, H:mm a");
                    final String updateDate = sdf.format(c.getTime());

                    query = new ParseQuery<ParseObject>(ParseInstallation.getCurrentInstallation().getString("AssociationCode"));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> assoc, ParseException e) {

                            while (!uniqueMemberNumber) {
                                ParseFile numbersFile = assoc.get(0).getParseFile("NumberFile");
                                String[] numbersFileArray = null;

                                memberNumberRandom = String.valueOf(generateRandom(10));


                                Log.d(TAG, "unique member number is " + memberNumberRandom);

                                try {
                                    byte[] file = numbersFile.getData();
                                    try {
                                        numbersFileString = new String(file, "UTF-8");
                                        Log.d(TAG, "numbersFileString is " + numbersFileString);
                                        numbersFileArray = numbersFileString.split("\\|");

                                        Log.d(TAG, "numbersFileArray length is " + numbersFileArray.length);

                                        for (int i = 0; i < numbersFileArray.length; i++) {

                                            uniqueMemberNumber = memberNumberRandom != numbersFileArray[i];

                                            Log.d(TAG, "numbersFileString member is " + Integer.toString(i) + " " + numbersFileArray[i]);



                                        }

                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    }
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }


                                Log.d(TAG, "unique member number is true/false " + uniqueMemberNumber);
                                installation.put("memberNumber", memberNumberRandom);
                                installation.saveInBackground();

                                numbersFileString = numbersFileString + "|" + memberNumberRandom;

                                Log.d(TAG, "numbersFileString is " + numbersFileString);

                                byte[] data = numbersFileString.getBytes();
                                ParseFile NumberFile = new ParseFile("Number.txt", data);


                                try {
                                    NumberFile.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }


                                assoc.get(0).put("NumberFile", NumberFile);
                                try {
                                    assoc.get(0).save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }



                                ParseFile rosterFile = assoc.get(0).getParseFile("RosterFile");


                                try {
                                    byte[] file = rosterFile.getData();
                                    try {
                                        rosterString = new String(file, "UTF-8");
                                        Log.d(TAG, "rosterString is " + rosterString);
                                        rosterString.trim();
                                        rosterFileArray = rosterString.split("\\|");
                                        numberOfMembers = rosterFileArray.length;
                                        Log.d(TAG, "no. of members = " + numberOfMembers);

                                        Log.d(TAG, "rosterString ----> " + rosterString);


                                        Toast.makeText(getBaseContext(), "UPDATING INFO", Toast.LENGTH_LONG).show();
                                        memberInfo = sharedPreferencesVisited.getString("MEMBER_INFO", "");



                                        String[] memberInfoArray = memberInfo.split("\\^");



                                        for (int i = 0; i < memberInfoArray.length; i++ ) {
                                            if ( i == 0 ) {

                                                rosterUpdate = rosterString + "|" + numberOfMembers + "^";

                                            } else if ( i == 20 ) {

                                                rosterUpdate = rosterUpdate + memberNumberRandom + "^";

                                            } else {

                                                rosterUpdate = rosterUpdate + memberInfoArray[i] + "^";
                                            }
                                        }

                                        editorVisited.putString("MEMBER_INFO", memberInfo.replaceAll(oldMemberNumber, memberNumberRandom));
                                        editorVisited.putString(installation.getString("AssociationCode") + "MemberNumber", memberNumberRandom);
                                        editorVisited.apply();


                                        rosterUpdate = rosterUpdate.substring(0, rosterUpdate.length() - 1);

                                        Log.d(TAG, "rosterUpdate ----> " + rosterUpdate);

                                        data = rosterUpdate.getBytes();
                                        ParseFile RosterFile = new ParseFile("Roster.txt", data);
                                        try {
                                            RosterFile.save();
                                        } catch (ParseException e2) {
                                            e2.printStackTrace();
                                        }
                                        assoc.get(0).put("RosterDate", updateDate);
                                        assoc.get(0).put("RosterFile", RosterFile);
                                        try {
                                            assoc.get(0).save();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }



                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    }
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                //******************************************************************************************************************

                                ParseFile petsFile = assoc.get(0).getParseFile("PetFile");


                                try {
                                    data = petsFile.getData();
                                    try {
                                        String petString = new String(data, "UTF-8");
                                        petString.trim();
                                        Log.d(TAG, "petString is " + petString);

                                        String myPets = sharedPreferencesVisited.getString("MYPETS", "");
                                        String myPetsChangeMemberNumber;

                                        if ( !myPets.equals("")) {

                                            myPetsChangeMemberNumber = myPets.replaceAll(oldMemberNumber, installation.get("memberNumber").toString());

                                            final String petUpdate;

                                            if ( petString.length() > 0) {

                                                petUpdate = petString + "|" + myPetsChangeMemberNumber;

                                            } else {

                                                petUpdate = myPetsChangeMemberNumber;

                                            }

                                            Log.d(TAG, "petUpdate ----> " + petUpdate);

                                            data = petUpdate.getBytes();
                                            ParseFile PetFile = new ParseFile("PetFile.txt", data);
                                            try {
                                                PetFile.save();
                                            } catch (ParseException e2) {
                                                e2.printStackTrace();
                                            }
                                            assoc.get(0).put("PetDate", updateDate);
                                            assoc.get(0).put("PetFile", PetFile);
                                            try {
                                                assoc.get(0).save();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                        }

                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    }
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                //******************************************************************************************************************

                                ParseFile guestsFile = assoc.get(0).getParseFile("GuestFile");


                                try {
                                    data = guestsFile.getData();
                                    try {
                                        String guestString = new String(data, "UTF-8");
                                        Log.d(TAG, "guestString is " + guestString);
                                        guestString.trim();

                                        String myGuests = sharedPreferencesVisited.getString("MYGUESTS", "");
                                        String myGuestsChangeMemberNumber;

                                        if ( !myGuests.equals(""))  {

                                            myGuestsChangeMemberNumber = myGuests.replaceAll(oldMemberNumber, installation.get("memberNumber").toString());

                                            final String guestUpdate;

                                            if ( guestString.length() > 0) {

                                                guestUpdate = guestString + "|" + myGuestsChangeMemberNumber;

                                            } else {

                                                guestUpdate = myGuestsChangeMemberNumber;

                                            }

                                            Log.d(TAG, "guestUpdate ----> " + guestUpdate);

                                            data = guestUpdate.getBytes();
                                            ParseFile GuestFile = new ParseFile("GuestFile.txt", data);
                                            try {
                                                GuestFile.save();
                                            } catch (ParseException e2) {
                                                e2.printStackTrace();
                                            }
                                            assoc.get(0).put("GuestDate", updateDate);
                                            assoc.get(0).put("GuestFile", GuestFile);
                                            try {
                                                assoc.get(0).save();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                        }

                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    }
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                //******************************************************************************************************************

                                ParseFile autoFile = assoc.get(0).getParseFile("AutoFile");


                                try {
                                    data = autoFile.getData();
                                    try {
                                        String autoString = new String(data, "UTF-8");
                                        Log.d(TAG, "autoString is " + autoString);
                                        autoString.trim();

                                        String myAutos = sharedPreferencesVisited.getString("MYAUTOS", "");
                                        String myAutosChangeMemberNumber;

                                        if ( !myAutos.equals(""))  {

                                            myAutosChangeMemberNumber = myAutos.replaceAll(oldMemberNumber, installation.get("memberNumber").toString());

                                            final String autosUpdate;

                                            if ( autoString.length() > 0) {

                                                autosUpdate = autoString + "|" + myAutosChangeMemberNumber;

                                            } else {

                                                autosUpdate = myAutosChangeMemberNumber;

                                            }

                                            Log.d(TAG, "autosUpdate ----> " + autosUpdate);

                                            data = autosUpdate.getBytes();
                                            ParseFile AutoFile = new ParseFile("AutoFile.txt", data);
                                            try {
                                                AutoFile.save();
                                            } catch (ParseException e2) {
                                                e2.printStackTrace();
                                            }
                                            assoc.get(0).put("AutoDate", updateDate);
                                            assoc.get(0).put("AutoFile", AutoFile);
                                            try {
                                                assoc.get(0).save();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                        }

                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    }
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                //******************************************************************************************************************


                            }




                        }
                    });








                    Intent mainActivity = new Intent();
                    mainActivity.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);
                    finish();

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




    private static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }



    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


}



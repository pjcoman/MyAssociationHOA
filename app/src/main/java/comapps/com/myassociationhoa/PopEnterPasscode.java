package comapps.com.myassociationhoa;

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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.ParseInstallation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopEnterPasscode extends AppCompatActivity {

    private static final String TAG = "POPENTERPASSCODE";
    private static final String MYPREFERENCES = "MyPrefs";

    private EditText passCode;
    private Button cancelButton;
    private Button okButton;

    String[] passcodes;
    String[] numbersFileArray;
    String associationsJoined;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ParseInstallation installation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_enter_passcode);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        String numberOfPasscodes = sharedPreferences.getString("passcodeSize", "");
        Log.d(TAG, "number of passcodes -----> " + numberOfPasscodes);


        passCode = (EditText) findViewById(R.id.editTextPassCode);
        cancelButton = (Button) findViewById(R.id.buttonCancel);
        okButton = (Button) findViewById(R.id.buttonOK);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .4));

        associationsJoined = sharedPreferences.getString("ASSOCIATIONS_JOINED", "");


        installation = ParseInstallation.getCurrentInstallation();



        Map<String, ?> keys = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d(TAG, "map values " + entry.getKey() + ": " +
                    entry.getValue().toString());
        }


        Integer passCodeSize = Integer.valueOf(sharedPreferences.getString("passcodeSize", "0"));

        final Map<String, String> mapPasswordsMember = new HashMap<String, String>();
        final Map<String, String> mapPasswordsAdmin = new HashMap<String, String>();
        final Map<String, String> mapAssocLongNameMember = new HashMap<String, String>();
        final Map<String, String> mapAssocLongNameAdmin = new HashMap<String, String>();


        for (int i = 1; i <= passCodeSize / 5; i++) {

            mapAssocLongNameAdmin.put(sharedPreferences.getString("passcodeADMIN_PW(" + String.valueOf(i) + ")", ""),
                    sharedPreferences.getString("passcodeASSOC_LONGNAME(" + String.valueOf(i) + ")", ""));

            mapAssocLongNameMember.put(sharedPreferences.getString("passcodeMEMBER_PW(" + String.valueOf(i) + ")", ""),
                    sharedPreferences.getString("passcodeASSOC_LONGNAME(" + String.valueOf(i) + ")", ""));

            mapPasswordsAdmin.put(sharedPreferences.getString("passcodeADMIN_PW(" + String.valueOf(i) + ")", ""),
                    sharedPreferences.getString("passcodeASSOC_NAME(" + String.valueOf(i) + ")", ""));

            mapPasswordsMember.put(sharedPreferences.getString("passcodeMEMBER_PW(" + String.valueOf(i) + ")", ""),
                    sharedPreferences.getString("passcodeASSOC_NAME(" + String.valueOf(i) + ")", ""));


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

                    installation.put("MemberType", "Member");
                    installation.put("AssociationCode", mapPasswordsMember.get(passCodeEntered));
                    associationsJoined = sharedPreferences.getString("ASSOCIATIONS_JOINED", "");
                    associationsJoined = associationsJoined + "|"+  mapAssocLongNameMember.get(passCodeEntered) + "^" + "Member" + "^" +  mapPasswordsMember.get(passCodeEntered);

                    if ( associationsJoined.substring(0,1).equals("|") ) {
                        associationsJoined = associationsJoined.substring(1, associationsJoined.length());
                    }

                    Log.d(TAG, "association(s) -----> " + associationsJoined);



                    editor = sharedPreferences.edit();
                    editor.putString("ASSOCIATIONS_JOINED", associationsJoined);
                    editor.putBoolean("visitedBefore", true);
                    editor.apply();




                } else if (mapPasswordsAdmin.containsKey(passCodeEntered)) {

                    installation.put("MemberType", "Administrator");
                    installation.put("AssociationCode", mapPasswordsAdmin.get(passCodeEntered));
                    associationsJoined = sharedPreferences.getString("ASSOCIATIONS_JOINED", "");
                    associationsJoined = associationsJoined + "|" + mapAssocLongNameAdmin.get(passCodeEntered) + "^" + "Administartor" + "^" +  mapPasswordsAdmin.get(passCodeEntered);

                    if ( associationsJoined.substring(0,1).equals("|") ) {
                        associationsJoined = associationsJoined.substring(1, associationsJoined.length());
                    }

                    Log.d(TAG, "association(s) -----> " + associationsJoined);




                    editor = sharedPreferences.edit();
                    editor.putString("ASSOCIATIONS_JOINED", associationsJoined);
                    editor.putBoolean("visitedBefore", true);
                    editor.apply();





                } else {

                    Toast.makeText(getApplicationContext(), "passcode incorrect.", Toast.LENGTH_LONG).show();

                    editor = sharedPreferences.edit();
                    editor.putBoolean("visitedBefore", false);
                    editor.apply();


                    Intent mainActivity = new Intent();
                    mainActivity.setClass(PopEnterPasscode.this, MainActivity.class);
                    startActivity(mainActivity);
                    finish();

                }


                Intent piActivity = new Intent();
                piActivity.setClass(PopEnterPasscode.this, RegistrationActivity.class);
                startActivity(piActivity);
                finish();








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



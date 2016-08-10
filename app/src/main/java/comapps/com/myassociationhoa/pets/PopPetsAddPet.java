package comapps.com.myassociationhoa.pets;

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
import comapps.com.myassociationhoa.objects.PetObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopPetsAddPet extends AppCompatActivity {

    private static final String TAG = "POPADDPET";
    public static final String MYPREFERENCES = "MyPrefs";
    public static final String VISITEDPREFERENCES = "VisitedPrefs";

    ParseQuery<ParseObject> query;
    String[] petFileArray;
    String petFileString = "";
    String petFileUpdate = "";

    EditText etPetName;
    EditText etPetType;
    EditText etPetBreed;
    EditText etPetColor;
    EditText etPetWeight;
    EditText etPetMisc;

    Button saveButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferencesVisited;
    SharedPreferences.Editor editorVisited;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_pets);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Update Pet Info");
        }


        etPetName = (EditText) findViewById(R.id.editTextPetName);
        etPetType = (EditText) findViewById(R.id.editTextPetType);
        etPetBreed = (EditText) findViewById(R.id.editTextPetBreed);
        etPetColor = (EditText) findViewById(R.id.editTextPetColor);
        etPetWeight = (EditText) findViewById(R.id.editTextPetWeight);
        etPetMisc = (EditText) findViewById(R.id.editTextPetMisc);

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


                        ParseFile petFile = assoc.get(0).getParseFile("PetFile");
                        petFileArray = null;

                        try {
                            byte[] file = petFile.getData();
                            try {
                                petFileString = new String(file, "UTF-8");

                                Log.d(TAG, "existing pets --->" + petFileString);

                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }



                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, H:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        String strDate = sdf.format(c.getTime());


                        int lengthOfPetFileString = petFileString.length();

                        String newPet = "|" + sharedPreferences.getString("MEMBERNAME", "") + "^" +
                                sharedPreferences.getString("MEMBERNUMBER", "") + "^" + etPetName.getText() +
                                "^" + etPetType.getText() +
                                "^" + etPetBreed.getText() +
                                "^" + etPetColor.getText() +
                                "^" + etPetWeight.getText() +
                                "^" + etPetMisc.getText();


                        petFileUpdate = petFileString + "|" + sharedPreferences.getString("MEMBERNAME", "") + "^" +
                                sharedPreferences.getString("MEMBERNUMBER", "") + "^" + etPetName.getText() +
                                "^" + etPetType.getText() +
                                "^" + etPetBreed.getText() +
                                "^" + etPetColor.getText() +
                                "^" + etPetWeight.getText() +
                                "^" + etPetMisc.getText();

                        if ( lengthOfPetFileString < 1 ) {

                            petFileUpdate = petFileUpdate.substring(1);

                        }

                        editorVisited = sharedPreferencesVisited.edit();

                        if ( sharedPreferencesVisited.getString("MYPETS", "").equals("")) {
                            editorVisited.putString("MYPETS", newPet.substring(1));
                        } else if (!sharedPreferencesVisited.getString("MYPETS","").contains(newPet)){
                            editorVisited.putString("MYPETS", sharedPreferencesVisited.getString("MYPETS","") + newPet);
                        }
                        editorVisited.apply();

                        Log.d(TAG, "update pets --->" + petFileUpdate);



                        PetObject petObject = new PetObject();
                        petObject.setOwner(sharedPreferences.getString("MEMBERNAME", ""));
                        petObject.setMemberNumber(sharedPreferences.getString("MEMBERNUMBER", ""));
                        petObject.setName(String.valueOf(etPetName.getText()));
                        petObject.setType(String.valueOf(etPetType.getText()));
                        petObject.setBreed(String.valueOf(etPetBreed.getText()));
                        petObject.setColor(String.valueOf(etPetColor.getText()));
                        petObject.setWeight(String.valueOf(etPetWeight.getText()));
                        petObject.setMisc(String.valueOf(etPetMisc.getText()));

                        Integer petSizeInt = sharedPreferences.getInt("petObjectsSize", 0);

                        editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String jsonPetObject = gson.toJson(petObject);
                        editor.putString("petObject" + "[" + String.valueOf(petSizeInt + 1) + "]", jsonPetObject);
                        editor.putInt("petObjectsSize", petSizeInt + 1);
                        editor.apply();


                        byte[] data = petFileUpdate.getBytes();
                        petFile = new ParseFile("PetFile.txt", data);


                        try {
                            petFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("PetDate", strDate);
                        assoc.get(0).put("PetFile", petFile);

                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }



                        Toast toast = Toast.makeText(getBaseContext(), petObject.getName() + " added.", Toast.LENGTH_LONG);
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
        intentMain.setClass(PopPetsAddPet.this, PetsActivity.class);
        PopPetsAddPet.this.finish();
        startActivity(intentMain);

    }


}



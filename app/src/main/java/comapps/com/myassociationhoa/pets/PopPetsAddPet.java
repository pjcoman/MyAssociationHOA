package comapps.com.myassociationhoa.pets;

import android.content.Context;
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
import comapps.com.myassociationhoa.objects.PetObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopPetsAddPet extends AppCompatActivity {

    private static final String TAG = "POPADDPET";
    private static final String MYPREFERENCES = "MyPrefs";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private ParseQuery<ParseObject> query;
    private String[] petFileArray;
    private String petFileString = "";
    private String petFileUpdate = "";

    private EditText etPetName;
    private EditText etPetType;
    private EditText etPetBreed;
    private EditText etPetColor;
    private EditText etPetWeight;
    private EditText etPetMisc;

    private TextView title;

    private Button saveButton;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences.Editor editorVisited;

    private Bundle bundle;
    private Boolean forEdit = false;
    private Boolean forDelete = false;

    private String petPassed;

    private PetObject petObject;

    private Toast toast;


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

        bundle = getIntent().getExtras();

        if ( bundle != null ) {

            forEdit = bundle.getBoolean("FOREDIT");
            forDelete = bundle.getBoolean("FORDELETE");
            petPassed = bundle.getString("PETFOREDIT");

            Gson gson = new Gson();
            petObject = gson.fromJson(petPassed, PetObject.class);

            Log.d(TAG, "pet object passed ----> " + petObject.toString());



        }





        etPetName = (EditText) findViewById(R.id.editTextPetName);
        etPetType = (EditText) findViewById(R.id.editTextPetType);
        etPetBreed = (EditText) findViewById(R.id.editTextPetBreed);
        etPetColor = (EditText) findViewById(R.id.editTextPetColor);
        etPetWeight = (EditText) findViewById(R.id.editTextPetWeight);
        etPetMisc = (EditText) findViewById(R.id.editTextPetMisc);



        saveButton = (Button) findViewById(R.id.buttonSaveMessage);
        title = (TextView) findViewById(R.id.textViewTitle);

        if ( forEdit ) {

            etPetName.setText(petObject.getName());
            etPetType.setText(petObject.getType());
            etPetBreed.setText(petObject.getBreed());
            etPetColor.setText(petObject.getColor());
            etPetWeight.setText(petObject.getWeight());
            etPetMisc.setText(petObject.getMisc());
            title.setText("Update Pet");
            assert bar != null;
            bar.setTitle("Update Pet");

        }

        if ( forDelete ) {

            etPetName.setText(petObject.getName());
            etPetType.setText(petObject.getType());
            etPetBreed.setText(petObject.getBreed());
            etPetColor.setText(petObject.getColor());
            etPetWeight.setText(petObject.getWeight());
            etPetMisc.setText(petObject.getMisc());
            title.setText("Delete Pet");
            saveButton.setText("DELETE");
            saveButton.setTextColor(Color.RED);

            assert bar != null;
            bar.setTitle("Delete Pet");

        }


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_APPEND);



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
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        String strDate = sdf.format(c.getTime());


                        int lengthOfPetFileString = petFileString.length();

                        if ( forEdit ) {

                            String preEditPet = petObject.getName() + "^" +
                                    petObject.getType() + "^" +
                                    petObject.getBreed() + "^" +
                                    petObject.getColor() + "^" +
                                    petObject.getWeight() + "^" +
                                    petObject.getMisc();

                            String postEditPet = etPetName.getText() +
                                    "^" + etPetType.getText() +
                                    "^" + etPetBreed.getText() +
                                    "^" + etPetColor.getText() +
                                    "^" + etPetWeight.getText() +
                                    "^" + etPetMisc.getText();


                            petFileUpdate = petFileString.replace(preEditPet, postEditPet);

                            Log.d(TAG, "update pets --->" + petFileUpdate);



                        } else if ( forDelete ) {

                            String preEditPet = "|" + installation.getString("memberName") + "^" +
                                    installation.getString("memberNumber") + "^" + petObject.getName() + "^" +
                                    petObject.getType() + "^" +
                                    petObject.getBreed() + "^" +
                                    petObject.getColor() + "^" +
                                    petObject.getWeight() + "^" +
                                    petObject.getMisc();

                            petFileUpdate = petFileString.replace(preEditPet, "");

                            Log.d(TAG, "update pets --->" + petFileUpdate);


                        } else {


                            String newPet = "|" + installation.getString("memberName") + "^" +
                                    installation.getString("memberNumber") + "^" + etPetName.getText() +
                                    "^" + etPetType.getText() +
                                    "^" + etPetBreed.getText() +
                                    "^" + etPetColor.getText() +
                                    "^" + etPetWeight.getText() +
                                    "^" + etPetMisc.getText();


                            petFileUpdate = petFileString + "|" + installation.getString("memberName") + "^" +
                                    installation.getString("memberNumber") + "^" + etPetName.getText() +
                                    "^" + etPetType.getText() +
                                    "^" + etPetBreed.getText() +
                                    "^" + etPetColor.getText() +
                                    "^" + etPetWeight.getText() +
                                    "^" + etPetMisc.getText();

                            if (lengthOfPetFileString < 1) {

                                petFileUpdate = petFileUpdate.substring(1);

                            }

                            editorVisited = sharedPreferencesVisited.edit();

                            if (sharedPreferencesVisited.getString("MYPETS", "").equals("")) {
                                editorVisited.putString("MYPETS", newPet.substring(1));
                            } else if (!sharedPreferencesVisited.getString("MYPETS", "").contains(newPet)) {
                                editorVisited.putString("MYPETS", sharedPreferencesVisited.getString("MYPETS", "") + newPet);
                            }
                            editorVisited.apply();

                            Log.d(TAG, "update pets --->" + petFileUpdate);


                            PetObject petObject = new PetObject();
                            petObject.setOwner(installation.getString("memberName"));
                            petObject.setMemberNumber(installation.getString("memberNumber"));
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

                            if (petFileUpdate.substring(0, 1).equals("|")) {
                                petFileUpdate = petFileUpdate.substring(1, petFileUpdate.length());
                            } else if (petFileUpdate.substring(petFileUpdate.length() - 1, petFileUpdate.length()).equals("|")) {
                                petFileUpdate = petFileUpdate.substring(0, petFileUpdate.length() - 1);
                            }

                        }


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
                            assoc.get(0).saveEventually();
                        }



                        if ( forEdit ) {
                            toast = Toast.makeText(getBaseContext(), etPetName.getText() + " updated.", Toast.LENGTH_LONG);
                        } else if ( forDelete) {
                            toast = Toast.makeText(getBaseContext(), etPetName.getText() + " deleted.", Toast.LENGTH_LONG);
                        } else {
                            toast = Toast.makeText(getBaseContext(), etPetName.getText() + " added.", Toast.LENGTH_LONG);
                        }

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

      finish();
    }


}



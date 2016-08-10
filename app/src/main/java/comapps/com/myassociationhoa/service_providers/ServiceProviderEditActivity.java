package comapps.com.myassociationhoa.service_providers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
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
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.ProviderObject;
import comapps.com.myassociationhoa.objects.ServiceProviderObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class ServiceProviderEditActivity extends AppCompatActivity {

    private static final String TAG = "SPEDITACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";

    ParseQuery<ParseObject> query;
    String[] providerFileArray;
    String providerFileString = "";


    ProviderObject providerObject;
    private ArrayList<ProviderObject> providerObjects;
    ServiceProviderObject serviceProviderObject;

    EditText editTextProviderName;
    EditText editTextProviderAddress;
    EditText editTextProviderAddress2;
    EditText editTextProviderCity;
    EditText editTextProviderState;
    EditText editTextProviderZip;
    EditText editTextProviderPhoneNumber;
    EditText editTextProviderNotes;

    String isSendingActivityServiceProviderAdd;
    String providerFileUpdateForUpload;

    Button saveButton;

    Bundle bundle;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String providerType;
    String serviceProviderObjectGson;
    String providerField;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_edit_service_provider);

        android.support.v7.app.ActionBar bar = getSupportActionBar();


        bundle = getIntent().getExtras();

        if ( bundle != null ) {

            isSendingActivityServiceProviderAdd = bundle.getString("FROMSERVICEPROVIDERADD");
            serviceProviderObjectGson = bundle.getString("serviceProviderObjectGson");
            providerType = bundle.getString("PROVIDERTYPE");
            Gson gson = new Gson();
            serviceProviderObject = gson.fromJson(serviceProviderObjectGson, ServiceProviderObject.class);


        }

        if (bar != null) {

            if ( isSendingActivityServiceProviderAdd.equals("NO")) {

                bar.setTitle("Edit Provider");

            } else {

                bar.setTitle("Add " + providerType + " Provider");

            }

        }



        editTextProviderName = (EditText) findViewById(R.id.editTextProviderName);
        editTextProviderAddress = (EditText) findViewById(R.id.editTextProviderAddress);
        editTextProviderAddress2 = (EditText) findViewById(R.id.editTextProviderAddress2);
        editTextProviderCity = (EditText) findViewById(R.id.editTextProviderCity);
        editTextProviderState = (EditText) findViewById(R.id.editTextProviderState);
        editTextProviderZip = (EditText) findViewById(R.id.editTextProviderZip);
        editTextProviderPhoneNumber = (EditText) findViewById(R.id.editTextProviderPhone);
        editTextProviderNotes = (EditText) findViewById(R.id.editTextProviderNotes);
        editTextProviderNotes.setSingleLine(false);

        if ( isSendingActivityServiceProviderAdd.equals("NO")) {

            editTextProviderName.setText(serviceProviderObject.getProviderName());
            editTextProviderAddress.setText(serviceProviderObject.getProviderAddress());
            editTextProviderAddress2.setText(serviceProviderObject.getProviderAddress2());
            editTextProviderCity.setText(serviceProviderObject.getProviderCity());
            editTextProviderState.setText(serviceProviderObject.getProviderState());
            editTextProviderZip.setText(serviceProviderObject.getProviderZip());
            editTextProviderPhoneNumber.setText(serviceProviderObject.getProviderPhoneNumber());
            editTextProviderNotes.setText(serviceProviderObject.getProviderNotes());


        }


        editTextProviderPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        saveButton = (Button) findViewById(R.id.buttonSave);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


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


                        ParseFile providerFile = assoc.get(0).getParseFile("ProviderFile");
                        providerFileArray = null;

                        try {
                            byte[] file = providerFile.getData();
                            try {
                                providerFileString = new String(file, "UTF-8");

                                Log.d(TAG, "existing providers --->" + providerFileString);

                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        if ( isSendingActivityServiceProviderAdd.equals("NO") ) {

                            String providerToUpdate = serviceProviderObject.getProviderName().trim() + "^" +
                                    serviceProviderObject.getProviderAddress().trim() + "^" +
                                    serviceProviderObject.getProviderAddress2().trim() + "^" +
                                    serviceProviderObject.getProviderCity().trim() + "^" +
                                    serviceProviderObject.getProviderState().trim() + "^" +
                                    serviceProviderObject.getProviderZip().trim() + "^" +
                                    serviceProviderObject.getProviderPhoneNumber().trim() + "^" +
                                    serviceProviderObject.getProviderNotes().trim();

                            String providerUpdated = editTextProviderName.getText().toString().trim() + "^" +
                                    editTextProviderAddress.getText().toString().trim() + "^" +
                                    editTextProviderAddress2.getText().toString().trim() + "^" +
                                    editTextProviderCity.getText().toString().trim() + "^" +
                                    editTextProviderState.getText().toString().trim() + "^" +
                                    editTextProviderZip.getText().toString().trim() + "^" +
                                    editTextProviderPhoneNumber.getText().toString().trim() + "^" +
                                    editTextProviderNotes.getText().toString().trim();

                            Log.d(TAG, "provider to update --->" + providerToUpdate);
                            Log.d(TAG, "provider updated --->" + providerUpdated);

                            providerFileUpdateForUpload = providerFileString.replace(providerToUpdate, providerUpdated);


                        } else {

                            String providerAdded = editTextProviderName.getText().toString().trim() + "^" +
                                    editTextProviderAddress.getText().toString().trim() + "^" +
                                    editTextProviderAddress2.getText().toString().trim() + "^" +
                                    editTextProviderCity.getText().toString().trim() + "^" +
                                    editTextProviderState.getText().toString().trim() + "^" +
                                    editTextProviderZip.getText().toString().trim() + "^" +
                                    editTextProviderPhoneNumber.getText().toString().trim() + "^" +
                                    editTextProviderNotes.getText().toString().trim();

                            Log.d(TAG, "provider to update --->" + providerFileString);
                            Log.d(TAG, "provider to be added --->" + providerAdded);

                            String[] providerFileStringSplit = providerFileString.split("\\|", -1);

                            providerFileUpdateForUpload = "";



                            Log.d(TAG, "providerFileStringSplit size --->" + providerFileStringSplit.length);

                            for ( int i = 0, j = 0; i < providerFileStringSplit.length; i++ ) {


                            providerField = providerFileStringSplit[i].trim();
                                Log.d(TAG, "providerField --->" + providerField);



                                if ( providerField.equals(providerType) || j > 0)  {
                                                switch (j) {
                                                    case 0:
                                                        providerFileUpdateForUpload = providerFileUpdateForUpload + "|" + providerField;
                                                        j++;
                                                break;
                                                case 1:
                                                    providerFileUpdateForUpload = providerFileUpdateForUpload + "|" + String.valueOf(Integer.valueOf(providerField) + 1) + "|";
                                                    j++;
                                                    break;
                                                case 2:

                                                    if ( providerField.length() == 0) {

                                                        providerFileUpdateForUpload = providerFileUpdateForUpload + providerField + providerAdded;


                                                    } else {

                                                        providerFileUpdateForUpload = providerFileUpdateForUpload + providerField + "^" + providerAdded;

                                                    }


                                                    j = 0;

                                             }

                                } else {

                                    providerFileUpdateForUpload = providerFileUpdateForUpload + "|" + providerField;

                                }





                            }





                        }






                        Log.d(TAG, "updated providers last character --->" + providerFileUpdateForUpload.charAt(providerFileUpdateForUpload.length() - 1));
                        String firstCharacter = String.valueOf(providerFileUpdateForUpload.charAt(0));
                        String lastCharacter = String.valueOf(providerFileUpdateForUpload.charAt(providerFileUpdateForUpload.length() - 1));
                        String secondToLastCharacter = String.valueOf(providerFileUpdateForUpload.charAt(providerFileUpdateForUpload.length() - 2));

                        if ( lastCharacter.equals("|") && !secondToLastCharacter.equals("0")) {

                            providerFileUpdateForUpload = providerFileUpdateForUpload.substring(0, providerFileUpdateForUpload.length() -1);

                        }

                        if ( firstCharacter.equals("|")) {

                            providerFileUpdateForUpload = providerFileUpdateForUpload.substring(1, providerFileUpdateForUpload.length());

                        }

                        Log.d(TAG, "providerFileUpdateForUpload --->" + providerFileUpdateForUpload);




                        byte[] data = providerFileUpdateForUpload.getBytes();
                        ParseFile ProviderFile = new ParseFile("Provider.txt", data);

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, H:mm a");
                        String strDate = sdf.format(c.getTime());



                      try {
                            ProviderFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("ProviderDate", strDate);
                        assoc.get(0).put("ProviderFile", ProviderFile);

                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }



                        providerFileString = providerFileUpdateForUpload;
                        //  Log.v(TAGP, "providerFileString -----> " + providerFileString);

                        providerFileArray = providerFileString.split("\\|", -1);



                        providerObjects = new ArrayList<>();

                        for (int i = 0, j = 0; i < providerFileArray.length; i++) {


                            switch (j) {
                                case 0:
                                    providerObject = new ProviderObject();
                                    providerObject.setProviderType(providerFileArray[i]);
                                    j++;
                                    Log.d(TAG, "provider type is " + providerFileArray[i]);
                                    break;
                                case 1:
                                    providerObject.setProviderCount(providerFileArray[i]);
                                    j++;
                                    Log.d(TAG, "provider count is " + providerFileArray[i]);
                                    break;
                                case 2:
                                    providerObject.setProviderList(providerFileArray[i]);
                                    providerObjects.add(providerObject);
                                    j = 0;

                                    editor = sharedPreferences.edit();
                                    Gson gson = new Gson();
                                    String jsonProviderObject = gson.toJson(providerObject); // myObject - instance of MyObject
                                    editor.putString("providerObject" + "[" + ((i + 1) / 3 - 1) + "]", jsonProviderObject);
                                    editor.putString("providerSize", String.valueOf(providerObjects.size()));
                                    editor.apply();

                            }

                        }





                        Intent serviceProviderActivity = new Intent();
                        serviceProviderActivity.setClass(getApplicationContext(), ServiceProviderActivity.class);
                        startActivity(serviceProviderActivity);
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
        intentMain.setClass(ServiceProviderEditActivity.this, ServiceProviderActivity.class);
        ServiceProviderEditActivity.this.finish();
        startActivity(intentMain);

    }


}



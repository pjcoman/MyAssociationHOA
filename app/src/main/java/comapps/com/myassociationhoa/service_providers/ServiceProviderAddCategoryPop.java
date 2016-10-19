package comapps.com.myassociationhoa.service_providers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.ProviderObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class ServiceProviderAddCategoryPop extends AppCompatActivity {

    private static final String TAG = "POPADDSERVICECATEGORY";
    private static final String MYPREFERENCES = "MyPrefs";

    private EditText editTextServiceCategory;
    private Button cancelButton;
    private Button okButton;

    private Integer providerSize;

    private ParseQuery<ParseObject> query;
    private String providerFileString;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ParseInstallation installation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_add_serviceprovider);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);



        editTextServiceCategory = (EditText) findViewById(R.id.editTextServiceToAdd);
        cancelButton = (Button) findViewById(R.id.buttonCancel);
        okButton = (Button) findViewById(R.id.buttonOK);

        okButton.setEnabled(false);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .4));


        installation = ParseInstallation.getCurrentInstallation();

        editTextServiceCategory.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                okButton.setEnabled(true);

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

                okButton.setEnabled(false);

            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                okButton.setEnabled(true);

            }
        });





        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                providerSize = Integer.valueOf(sharedPreferences.getString("providerSize", "0"));



                ProviderObject providerObject = new ProviderObject();
                providerObject.setProviderType(editTextServiceCategory.getText().toString());
                providerObject.setProviderCount("0");

                editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String jsonProviderObject = gson.toJson(providerObject); // myObject - instance of MyObject
                editor.putString("providerObject" + "[" + providerSize + "]", jsonProviderObject);
                editor.putString("providerSize", String.valueOf(providerSize + 1));
                editor.apply();



                query = new ParseQuery<>(installation.getString("AssociationCode"));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, ParseException e) {


                        ParseFile providerFile = assoc.get(0).getParseFile("ProviderFile");

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



                        String providerFileUpdateForUpload = providerFileString.trim() + "|" + editTextServiceCategory.getText().toString() + "|0|";




                        Log.d(TAG, "updated  providers --->" + providerFileUpdateForUpload);

                        String[] providerFileUpdateForUploadPreSort = providerFileUpdateForUpload.split("\\|");
                        String[] providerFileUpdateForUploadPostSort = new String[providerFileUpdateForUploadPreSort.length/3 + 1];

                        int i = 0; int j = 0;
                        for ( String provider: providerFileUpdateForUploadPreSort) {

                            Log.d(TAG, "updated provider --->" + provider);

                            int remainder = j % 3;

                            if ( remainder == 0 && j != 0 ) {

                                i++;
                            }



                                providerFileUpdateForUploadPostSort[i] = providerFileUpdateForUploadPostSort[i] + provider.trim() + "|";
                                providerFileUpdateForUploadPostSort[i] = providerFileUpdateForUploadPostSort[i].replace("null", "");


                            j++;
                        }

                        providerFileUpdateForUploadPostSort[i] = providerFileUpdateForUploadPostSort[i] + "|";

                        Collections.sort(Arrays.asList(providerFileUpdateForUploadPostSort), String.CASE_INSENSITIVE_ORDER);

                        providerFileUpdateForUpload = "";

                        for ( String provider: providerFileUpdateForUploadPostSort) {

                            Log.d(TAG, "updated provider post sort --->" + provider);

                            providerFileUpdateForUpload = providerFileUpdateForUpload + provider;


                        }

                        providerFileUpdateForUpload = providerFileUpdateForUpload.substring(0, providerFileUpdateForUpload.length() - 1);

                        Log.d(TAG, "updatedProviderFileUpdateForUpload ---> " + providerFileUpdateForUpload);

                        byte[] data;



                        data = providerFileUpdateForUpload.getBytes();






                        ParseFile ProviderFile = new ParseFile("Provider.txt", data);

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a");
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
                            assoc.get(0).saveEventually();
                        }



                        Intent serviceProviderActivity = new Intent();
                        serviceProviderActivity.setClass(getApplicationContext(), ServiceProviderActivity.class);
                        startActivity(serviceProviderActivity);
                        finish();


                    }
                });


            }





        });






        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);


                ServiceProviderAddCategoryPop.this.finish();

            }
        });


    }




    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


}



package comapps.com.myassociationhoa.maintenance;

import android.content.Context;
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

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.MaintenanceCategoryObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class AddMaintenanceCategory extends AppCompatActivity {

    private static final String TAG = "ADDMAINTENACECATEGORY";
    public static final String MYPREFERENCES = "MyPrefs";
    public static final String VISITEDPREFERENCES = "VisitedPrefs";


    ParseQuery<ParseObject> query;

    String maintenanceCategoryFileString = "";
    String maintenanceCategoryFileUpdate = "";

    EditText etCategoryName;
    EditText etCategoryEmail;

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

        setContentView(R.layout.layout_addmaintenance);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Update Auto Info");
        }


        etCategoryName = (EditText) findViewById(R.id.textViewAutoMake);
        etCategoryEmail = (EditText) findViewById(R.id.textViewAutoModel);

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


                        ParseFile categoryFile = assoc.get(0).getParseFile("MaintenanceCategoryFile");


                        try {
                            byte[] file = categoryFile.getData();
                            try {
                                maintenanceCategoryFileString = new String(file, "UTF-8");

                                Log.d(TAG, "existing categories --->" + maintenanceCategoryFileString);

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


                        String newCategory = "|" + etCategoryName.getText() +
                                "|" + etCategoryEmail.getText();

                        maintenanceCategoryFileUpdate = maintenanceCategoryFileString + newCategory;

                        if ( maintenanceCategoryFileString.length() < 1 ) {

                            maintenanceCategoryFileUpdate = maintenanceCategoryFileUpdate.substring(1);

                        }





                        MaintenanceCategoryObject object = new MaintenanceCategoryObject();
                        object.setMaintenanceCatName(String.valueOf(etCategoryName.getText()));
                        object.setMaintenanceCatEmail(String.valueOf(etCategoryEmail.getText()));

                        Integer categorySizeInt = sharedPreferences.getInt("maintenanceCategoryObjectSize", 0);

                        editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String jsonMCObject = gson.toJson(object);
                        editor.putString("maintenanceCategoryObject" + "[" + String.valueOf(categorySizeInt) + "]", jsonMCObject);
                        editor.putInt("maintenanceCategoryObjectsSize", categorySizeInt + 1);
                        editor.apply();


                        byte[] data = maintenanceCategoryFileUpdate.getBytes();
                        categoryFile = new ParseFile("Category.txt", data);


                        try {
                            categoryFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("MaintenanceCategoryDate", strDate);
                        assoc.get(0).put("MaintenanceCategoryFile", categoryFile);

                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        Toast toast = Toast.makeText(getBaseContext(), object.getMaintenanceCatName() + " added.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();



                       /* Intent mainActivity = new Intent();
                        mainActivity.setClass(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);*/
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



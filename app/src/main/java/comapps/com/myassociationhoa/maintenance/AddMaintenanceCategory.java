package comapps.com.myassociationhoa.maintenance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import comapps.com.myassociationhoa.objects.MaintenanceCategoryObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class AddMaintenanceCategory extends AppCompatActivity {

    private static final String TAG = "ADDMAINTENACECATEGORY";
    private static final String MYPREFERENCES = "MyPrefs";
    public static final String VISITEDPREFERENCES = "VisitedPrefs";


    private ParseQuery<ParseObject> query;

    private String maintenanceCategoryFileString = "";
    private String maintenanceCategoryFileUpdate = "";

    private EditText etCategoryName;
    private EditText etCategoryEmail;
    private TextView title;

    private Button saveButton;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Bundle bundle;
    private String passedCat;
    private String passedEmail;
    private String passedPosition;
    private String passedJsonObject;




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
            bar.setTitle("Add Maint. Category");
        }


        etCategoryName = (EditText) findViewById(R.id.editTextCatName);
        etCategoryEmail = (EditText) findViewById(R.id.editTextEmail);
        title = (TextView) findViewById(R.id.textViewTitle);

        bundle = getIntent().getExtras();



        if ( bundle != null ) {

            assert bar != null;
            bar.setTitle("Edit Maint. Category");
            title.setText("Edit Category");

            passedCat = bundle.getString("MAINTENANCECAT", "Category");
            passedEmail = bundle.getString("MAINTENANCEEMAIL", "Email Address");
            passedPosition = bundle.getString("MAINTENANCEPOSITION", "");
            passedJsonObject = bundle.getString("MAINTENACEJSONOBJECT", "");

            Log.d(TAG, "passedCat ----> " + passedCat);
            Log.d(TAG, "passedEmail ----> " + passedEmail);
            Log.d(TAG, "passedPosition ----> " + passedPosition);

            Log.d(TAG, "passed jsonObject ----> " + passedJsonObject);

            etCategoryName.setText(passedCat);
            etCategoryEmail.setText(passedEmail);

            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );

        }


        saveButton = (Button) findViewById(R.id.buttonSaveMessage);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);



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
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        String strDate = sdf.format(c.getTime());


                        if ( bundle == null ) {


                            String newCategory = "|" + etCategoryName.getText() +
                                    "|" + etCategoryEmail.getText();

                            maintenanceCategoryFileUpdate = maintenanceCategoryFileString + newCategory;

                            if (maintenanceCategoryFileString.length() < 1) {

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

                            Toast toast = Toast.makeText(getBaseContext(), object.getMaintenanceCatName() + " added.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        } else {

                            String existing = passedCat + "|" + passedEmail;
                            Log.d(TAG, "existingCatObject ----> " + existing);
                            String edited = String.valueOf(etCategoryName.getText()) + "|" + String.valueOf(etCategoryEmail.getText());
                            Log.d(TAG, "editedCatObject ----> " + edited);

                            maintenanceCategoryFileUpdate = maintenanceCategoryFileString;

                            Log.d(TAG, "pre update ----> " + maintenanceCategoryFileUpdate);

                            maintenanceCategoryFileUpdate = maintenanceCategoryFileUpdate.replace(existing, edited);

                            Log.d(TAG, "post update ----> " + maintenanceCategoryFileUpdate);

  /*                          MaintenanceCategoryObject maintenanceCategoryObject = new MaintenanceCategoryObject();
                            maintenanceCategoryObject.setMaintenanceCatName(String.valueOf(etCategoryName.getText()));
                            maintenanceCategoryObject.setMaintenanceCatEmail(String.valueOf(etCategoryEmail.getText()));
                            Gson gson = new Gson();
                            String jsonMCObject = gson.toJson(maintenanceCategoryObject);





                            for (int i = 0; i < Integer.valueOf(sharedPreferences.getInt("maintenanceCategoryObjectsSize", 0)); i++) {

                                String categoryObject = sharedPreferences.getString("maintenanceCategoryObject" + "[" + i + "]", "");

                               if ( passedJsonObject.equals(categoryObject)) {

                                    Log.d(TAG, "categoryObject is " + categoryObject);

                                    editor = sharedPreferences.edit();
                                    editor.putString("maintenanceCategoryObject" + "[" + i + "]", jsonMCObject);
                                    editor.apply();



                                }


                            }*/

                            AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                            remoteDataTaskClass.execute();



                            Toast toast = Toast.makeText(getBaseContext(), passedCat + " edited.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }

                        Log.d(TAG, "updated Category.txt ----> " + maintenanceCategoryFileUpdate);


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

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    @Override
    public void onBackPressed() {

       finish();

    }


}



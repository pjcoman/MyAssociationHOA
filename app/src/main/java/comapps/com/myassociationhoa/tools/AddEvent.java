package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.ToolsActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class AddEvent extends AppCompatActivity {

    private static final String TAG = "ADDEVENT";
    public static final String MYPREFERENCES = "MyPrefs";
    public static final String VISITEDPREFERENCES = "VisitedPrefs";


    ParseQuery<ParseObject> query;
    String[] eventFileArray;
    String eventFileString = "";
    String eventFileUpdate = "";

    EditText etEventTitle;
    EditText etEventDetail;
    EditText etEventStartDate;
    EditText etEventEndDate;


    Button saveButton;
    Button typeButton;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesVisited;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorVisited;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_addevent);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Update Auto Info");
        }


        etEventTitle = (EditText) findViewById(R.id.editTextEventTitle);
        etEventDetail = (EditText) findViewById(R.id.editTextEventDetail);
        etEventStartDate = (EditText) findViewById(R.id.editTextStartDate);
        etEventEndDate = (EditText) findViewById(R.id.editTextEndDate);



        saveButton = (Button) findViewById(R.id.buttonSave);
        typeButton = (Button) findViewById(R.id.buttonType);

        typeButton.setText("Append");

        saveButton.setEnabled(false);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_APPEND);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width * 1, height * 1);

        etEventStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });

        etEventEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

                saveButton.setEnabled(true);

            }
        });


        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( typeButton.getText().equals("Append")) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddEvent.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Add Events - Replace...");

                    // Setting Dialog Message
                    alertDialog.setMessage("This will delete all of the Current Events. ");

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
                            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                            typeButton.setText("Replace");
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                } else {

                    typeButton.setText("Append");

                }

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    String[] eventStartDateArray = etEventStartDate.getText().toString().split("\\/");
                    if ( eventStartDateArray[1].length() == 1 ) {
                        eventStartDateArray[1] = "0" + eventStartDateArray[1];
                    }
                    if ( eventStartDateArray[0].length() == 1 ) {
                    eventStartDateArray[0] = "0" + eventStartDateArray[0];
                    }

                    String newEvent = "|" + etEventTitle.getText() +
                            "|" + etEventDetail.getText() +
                            "|" + etEventStartDate.getText() +
                            "|" + etEventEndDate.getText() + "|" + eventStartDateArray[2] + eventStartDateArray[0] + eventStartDateArray[1];

                    editor = sharedPreferences.edit();
                    editor.putString("NEWCALENDAR", sharedPreferences.getString("NEWCALENDAR","") + newEvent);


                    if ( typeButton.getText().toString().equals("Append")) {
                        editor.putBoolean("CALENDAR_APPEND", true);
                    } else {
                        editor.putBoolean("CALENDAR_APPEND", false);
                    }


                    editor.apply();

                    Log.d(TAG, "events added --->" + sharedPreferences.getString("NEWCALENDAR",""));


                    Toast toast = Toast.makeText(getBaseContext(), "Event added.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


                } catch (Exception e) {

                    e.printStackTrace();
                }


                finish();


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
        intentMain.setClass(AddEvent.this, ToolsActivity.class);
        AddEvent.this.finish();
        startActivity(intentMain);

    }


}



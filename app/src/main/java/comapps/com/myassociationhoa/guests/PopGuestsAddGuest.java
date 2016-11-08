package comapps.com.myassociationhoa.guests;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.Locale;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.myinfo.PersonalInfoActivity;
import comapps.com.myassociationhoa.objects.GuestObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
@SuppressWarnings("ALL")
public class PopGuestsAddGuest extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "POPADDGUEST";
    private static final String MYPREFERENCES = "MyPrefs";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private ParseQuery<ParseObject> query;
    private String[] guestFileArray;
    private String guestFileString = "";
    private String guestFileUpdate = "";

    private EditText etGuestName;

    private Button guestType;
    private Button notifyBy;

    private EditText etStartDate;
    private EditText etEndDate;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    private ToggleButton tbMonday;
    private ToggleButton tbTuesday;
    private ToggleButton tbWednesday;
    private ToggleButton tbThursday;
    private ToggleButton tbFriday;
    private ToggleButton tbSaturday;
    private ToggleButton tbSunday;

    private EditText etGuesNotes;

    private LinearLayout datesLayout;

    private Button saveButton;

    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private SharedPreferences.Editor editorVisited;

    TextWatcher tw;
    
    private Bundle bundle;
    
    private Boolean forEdit = false;
    private Boolean forDelete = false;
    private String guestPassed;
    
    private GuestObject guestObject;

    private String startDate;
    private String endDate;

    private TextView title;
    


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_guests);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Update Guest Info");
        }

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);




        etGuestName = (EditText) findViewById(R.id.editTextGuestName);
        guestType = (Button) findViewById(R.id.buttonGuestType);
        notifyBy = (Button) findViewById(R.id.buttonNotifyBy);

        datesLayout = (LinearLayout) findViewById(R.id.datesLayout);
        etStartDate = (EditText) findViewById(R.id.editTextStartDate);
        etEndDate = (EditText) findViewById(R.id.editTextEndDate);



        tbMonday = (ToggleButton) findViewById(R.id.toggleButtonMonday);
        tbTuesday = (ToggleButton) findViewById(R.id.toggleButtonTuesday);
        tbWednesday = (ToggleButton) findViewById(R.id.toggleButtonWednesday);
        tbThursday = (ToggleButton) findViewById(R.id.toggleButtonThursday);
        tbFriday = (ToggleButton) findViewById(R.id.toggleButtonFriday);
        tbSaturday = (ToggleButton) findViewById(R.id.toggleButtonSaturday);
        tbSunday = (ToggleButton) findViewById(R.id.toggleButtonSunday);

        tbFriday.setWidth(tbMonday.getWidth());
        tbSaturday.setWidth(tbMonday.getWidth());
        tbSunday.setWidth(tbMonday.getWidth());


        etGuesNotes = (EditText) findViewById(R.id.editTextGuestNotes);

        saveButton = (Button) findViewById(R.id.buttonSaveMessage);
        title = (TextView) findViewById(R.id.textViewTitle);

        bundle = getIntent().getExtras();

        if ( bundle != null ) {

            forEdit = bundle.getBoolean("FOREDIT");
            forDelete = bundle.getBoolean("FORDELETE");
            guestPassed = bundle.getString("GUESTFOREDIT");

            Gson gson = new Gson();
            guestObject = gson.fromJson(guestPassed, GuestObject.class);

            Log.d(TAG, "guest object passed ----> " + guestObject.toString());

          

        }

        if ( forEdit ) {

            etGuestName.setText(guestObject.getGuestName());
            guestType.setText(guestObject.getGuestType());
            notifyBy.setText(guestObject.getOwnerContactNumberType());
            etStartDate.setText(guestObject.getGuestStartdate());
            etEndDate.setText(guestObject.getGuestEnddate());
            if ( guestObject.getSundayAccess().equals("Yes")) {
                tbSunday.setChecked(true);
            } else {
                tbSunday.setChecked(false);
            }
            if ( guestObject.getMondayAccess().equals("Yes")) {
                tbMonday.setChecked(true);
            } else {
                tbMonday.setChecked(false);
            }
            if ( guestObject.getTuesdayAccess().equals("Yes")) {
                tbTuesday.setChecked(true);
            } else {
                tbTuesday.setChecked(false);
            }
            if ( guestObject.getWednesdayAccess().equals("Yes")) {
                tbWednesday.setChecked(true);
            } else {
                tbWednesday.setChecked(false);
            }
            if ( guestObject.getThursdayAccess().equals("Yes")) {
                tbThursday.setChecked(true);
            } else {
                tbThursday.setChecked(false);
            }
            if ( guestObject.getFridayAccess().equals("Yes")) {
                tbFriday.setChecked(true);
            } else {
                tbFriday.setChecked(false);
            }
            if ( guestObject.getSaturdayAccess().equals("Yes")) {
                tbSaturday.setChecked(true);
            } else {
                tbSaturday.setChecked(false);
            }

            etGuesNotes.setText(guestObject.getGuestDescription());

            assert bar != null;
            bar.setTitle("Update Guest");
            title.setText("Update Guest");

        }

        if ( forDelete ) {

            etGuestName.setText(guestObject.getGuestName());
            guestType.setText(guestObject.getGuestType());
            notifyBy.setText(guestObject.getOwnerContactNumberType());
            etStartDate.setText(guestObject.getGuestStartdate());
            etEndDate.setText(guestObject.getGuestEnddate());
            if ( guestObject.getSundayAccess().equals("Yes")) {
                tbSunday.setChecked(true);
            } else {
                tbSunday.setChecked(false);
            }
            if ( guestObject.getMondayAccess().equals("Yes")) {
                tbMonday.setChecked(true);
            } else {
                tbMonday.setChecked(false);
            }
            if ( guestObject.getTuesdayAccess().equals("Yes")) {
                tbTuesday.setChecked(true);
            } else {
                tbTuesday.setChecked(false);
            }
            if ( guestObject.getWednesdayAccess().equals("Yes")) {
                tbWednesday.setChecked(true);
            } else {
                tbWednesday.setChecked(false);
            }
            if ( guestObject.getThursdayAccess().equals("Yes")) {
                tbThursday.setChecked(true);
            } else {
                tbThursday.setChecked(false);
            }
            if ( guestObject.getFridayAccess().equals("Yes")) {
                tbFriday.setChecked(true);
            } else {
                tbFriday.setChecked(false);
            }
            if ( guestObject.getSaturdayAccess().equals("Yes")) {
                tbSaturday.setChecked(true);
            } else {
                tbSaturday.setChecked(false);
            }

            etGuesNotes.setText(guestObject.getGuestDescription());

            title.setText("Delete Guest");

            saveButton.setText("DELETE");
            saveButton.setTextColor(Color.RED);

            assert bar != null;
            bar.setTitle("Delete Guest");

        }

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_APPEND);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, height);

        guestType.setText("Permanent");
        datesLayout.setVisibility(View.GONE);
        notifyBy.setText("Mobile");

        if ( !forDelete ) {

            guestType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String guestTypeString = guestType.getText().toString();

                    if (guestTypeString.equals("Permanent")) {
                        guestType.setText("Temporary");
                        datesLayout.setVisibility(View.VISIBLE);
                        findViewsById();
                        setDateTimeField();
                    } else {
                        guestType.setText("Permanent");
                        datesLayout.setVisibility(View.GONE);
                    }

                }
            });


            notifyBy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String notifyByString = notifyBy.getText().toString();

                    switch (notifyByString) {
                        case "Mobile":
                            notifyBy.setText("Text");
                            break;
                        case "Text":
                            notifyBy.setText("Home");
                            break;
                        case "Home":
                            notifyBy.setText("None");
                            break;
                        case "None":
                            notifyBy.setText("Mobile");
                            break;
                    }

                }
            });

        } else {

            guestType.setEnabled(false);
            notifyBy.setEnabled(false);
        }


        if ( datesLayout.getVisibility() == View.VISIBLE) {




        }




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                query = new ParseQuery<>(installation.getString("AssociationCode"));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, ParseException e) {


                        ParseFile guestFile = assoc.get(0).getParseFile("GuestFile");
                        guestFileArray = null;

                        try {
                            byte[] file = guestFile.getData();
                            try {
                                guestFileString = new String(file, "UTF-8");

                                Log.d(TAG, "existing guests --->" + guestFileString);

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

                        String notify = "";


                        String[] memberInfoArray = sharedPreferencesVisited.getString("MEMBER_INFO", "").split("\\^", -1);

                        int i = 0;
                        for ( String info: memberInfoArray) {

                            Log.d(TAG, "info ----> " + info + " " + i);
                            i++;
                        }

                        if ( notifyBy.getText().equals("Mobile") || notifyBy.getText().equals("Text")) {


                            try {
                                notify = memberInfoArray[10];

                                if ( notify.length() < 10 ) {

                                    Toast toast = Toast.makeText(getBaseContext(), "mobile number\n" + memberInfoArray[10] + "needs to be updated.", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

                                    Intent piActivity = new Intent();
                                    piActivity.setClass(getApplicationContext(), PersonalInfoActivity.class);
                                    piActivity.putExtra("FROMADDGUEST", true);
                                    startActivity(piActivity);


                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();




                            }

                        } else if ( notifyBy.getText().equals("Home")) {

                            try {
                                notify = memberInfoArray[9];

                                if ( notify.length() < 10 ) {

                                    Toast toast = Toast.makeText(getBaseContext(), "home number\n " + memberInfoArray[10] + "needs to be updated.", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

                                    Intent piActivity = new Intent();
                                    piActivity.setClass(getApplicationContext(), PersonalInfoActivity.class);
                                    piActivity.putExtra("FROMADDGUEST", true);
                                    startActivity(piActivity);
                                }

                            } catch (Exception e1) {
                                e1.printStackTrace();


                            }

                        }

                        int lengthOfGuestFileString = guestFileString.length();

                        if ( forEdit ) {



                            if (etStartDate.getText().equals(null)) {
                                startDate = "";
                                endDate = "";
                            } else {
                                startDate = etStartDate.getText().toString();
                                endDate = etEndDate.getText().toString();
                            }

                            String preEditGuest = guestObject.getGuestType() + "^" + guestObject.getGuestStartdate() + "^" + guestObject.getGuestEnddate()
                                    + "^" + guestObject.getMondayAccess() + "^" + guestObject.getTuesdayAccess() + "^" + guestObject.getWednesdayAccess() +
                                    "^" + guestObject.getThursdayAccess() + "^" + guestObject.getFridayAccess()
                                    + "^" + guestObject.getSaturdayAccess() + "^" + guestObject.getSundayAccess() + "^" + guestObject.getGuestDescription() +
                                    "^" + guestObject.getGuestName() + "^" + guestObject.getOwnerContactNumberType()
                                    + "^" + guestObject.getOwnerContactNumber();

                            String postEditGuest = guestType.getText() + "^" + startDate + "^" + endDate
                                    + "^" + tbMonday.getText() + "^" + tbTuesday.getText() + "^" + tbWednesday.getText() + "^" + tbThursday.getText() + "^" + tbFriday.getText()
                                    + "^" + tbSaturday.getText() + "^" + tbSunday.getText() + "^" + etGuesNotes.getText() + "^" + etGuestName.getText() + "^" + notifyBy.getText()
                                    + "^" + notify;


                            guestFileUpdate = guestFileString.replace(preEditGuest, postEditGuest);

                            Log.d(TAG, "update guests --->" + guestFileUpdate);



                        } else if ( forDelete ) {

                            String preEditGuest = "|" + installation.getString("memberName") + "^" +
                                    installation.getString("memberNumber") + "^" + guestObject.getGuestType() + "^" + guestObject.getGuestStartdate() + "^" + guestObject.getGuestEnddate()
                                    + "^" + guestObject.getMondayAccess() + "^" + guestObject.getTuesdayAccess() + "^" + guestObject.getWednesdayAccess() +
                                    "^" + guestObject.getThursdayAccess() + "^" + guestObject.getFridayAccess()
                                    + "^" + guestObject.getSaturdayAccess() + "^" + guestObject.getSundayAccess() + "^" + guestObject.getGuestDescription() +
                                    "^" + guestObject.getGuestName() + "^" + guestObject.getOwnerContactNumberType()
                                    + "^" + guestObject.getOwnerContactNumber();

                            guestFileUpdate = guestFileString.replace(preEditGuest, "");

                            Log.d(TAG, "update guests --->" + guestFileUpdate);


                        } else {


                            String startDate;
                            String endDate;

                            if (etStartDate.getText().equals(null)) {
                                startDate = "";
                                endDate = "";
                            } else {
                                startDate = etStartDate.getText().toString();
                                endDate = etEndDate.getText().toString();
                            }

                            String newGuest = "|" + installation.getString("memberName") + "^" +
                                    installation.getString("memberNumber") + "^" + guestType.getText() + "^" + startDate + "^" + endDate
                                    + "^" + tbMonday.getText() + "^" + tbTuesday.getText() + "^" + tbWednesday.getText() + "^" + tbThursday.getText() + "^" + tbFriday.getText()
                                    + "^" + tbSaturday.getText() + "^" + tbSunday.getText() + "^" + etGuesNotes.getText() + "^" + etGuestName.getText() + "^" + notifyBy.getText()
                                    + "^" + notify;

                            guestFileUpdate = guestFileString + "|" + installation.getString("memberName") + "^" +
                                    installation.getString("memberNumber") + "^" + guestType.getText() + "^" + startDate + "^" + endDate
                                    + "^" + tbMonday.getText() + "^" + tbTuesday.getText() + "^" + tbWednesday.getText() + "^" + tbThursday.getText() + "^" + tbFriday.getText()
                                    + "^" + tbSaturday.getText() + "^" + tbSunday.getText() + "^" + etGuesNotes.getText() + "^" + etGuestName.getText() + "^" + notifyBy.getText()
                                    + "^" + notify;

                            if (lengthOfGuestFileString < 1) {

                                guestFileUpdate = guestFileUpdate.substring(1);

                            }

                            editorVisited = sharedPreferencesVisited.edit();

                            if (sharedPreferencesVisited.getString("MYGUESTS", "").equals("")) {
                                editorVisited.putString("MYGUESTS", newGuest.substring(1));
                            } else if (!sharedPreferencesVisited.getString("MYGUESTS", "").contains(newGuest)) {
                                editorVisited.putString("MYGUESTS", sharedPreferencesVisited.getString("MYGUESTS", "") + newGuest);
                            }
                            editorVisited.apply();

                            Log.d(TAG, "update guests --->" + guestFileUpdate);



                        }

                        if ( guestFileUpdate.substring(0,1).equals("|")) {
                            guestFileUpdate = guestFileUpdate.substring(1, guestFileUpdate.length());
                        } else if (guestFileUpdate.substring(guestFileUpdate.length() - 1, guestFileUpdate.length()).equals("|")) {
                            guestFileUpdate = guestFileUpdate.substring(0, guestFileUpdate.length() - 1);
                        }


                        byte[] data = guestFileUpdate.getBytes();
                        guestFile = new ParseFile("GuestFile.txt", data);


                        try {
                            guestFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("GuestDate", strDate);
                        assoc.get(0).put("GuestFile", guestFile);

                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            assoc.get(0).saveEventually();
                        }

                        Toast toast;


                        if ( forEdit ) {
                            toast = Toast.makeText(getBaseContext(), guestObject.getGuestName() + " updated.", Toast.LENGTH_LONG);
                        } else if ( forDelete) {
                            toast = Toast.makeText(getBaseContext(), guestObject.getGuestName() + " deleted.", Toast.LENGTH_LONG);
                        } else {
                            toast = Toast.makeText(getBaseContext(), etGuestName.getText() + " added to guests.", Toast.LENGTH_LONG);
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

    private void findViewsById() {



            etStartDate = (EditText) findViewById(R.id.editTextStartDate);
            etStartDate.setInputType(InputType.TYPE_NULL);
            etStartDate.requestFocus();

            etEndDate = (EditText) findViewById(R.id.editTextEndDate);
            etEndDate.setInputType(InputType.TYPE_NULL);



    }

    private void setDateTimeField() {
        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etStartDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etEndDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {

       finish();
    }


    @Override
    public void onClick(View v) {

        if(v == etStartDate) {
            fromDatePickerDialog.show();
        } else if(v == etEndDate) {
            toDatePickerDialog.show();
        }

    }
}



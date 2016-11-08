package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
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

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.ToolsActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
@SuppressWarnings("ALL")
public class CalendarManageActivity extends AppCompatActivity {

    private static final String TAG = "CALENDARMANAGEACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    private FloatingActionButton mFab;
    private Bundle bundle;
    private Button createAndSend;
    private Boolean fromTools = false;

    private ParseQuery query;
    private String[] eventFileArray;
    private String eventFileString = "";
    private String eventFileUpdate = "";

    private SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesVisited;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorVisited;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_calendarmanage);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        createAndSend = (Button) findViewById(R.id.createAndSend);
        mFab.setVisibility(View.GONE);
        createAndSend.setVisibility(View.GONE);

        if (bar != null) {
            bar.setTitle("Calendar of Events");
        }

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        bundle = getIntent().getExtras();

        if ( bundle != null ) {

            fromTools = bundle.getBoolean("FROMTOOLS");
            if ( fromTools ) {
                mFab.setVisibility(View.VISIBLE);
                createAndSend.setVisibility(View.VISIBLE);
                assert bar != null;
                bar.setTitle("Event Manager");
            } else {
                mFab.setVisibility(View.GONE);
                createAndSend.setVisibility(View.GONE);

            }








        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddEvent = new Intent();
                intentAddEvent.setClass(CalendarManageActivity.this, AddEvent.class);
                startActivity(intentAddEvent);
                finish();
            }
        });

        createAndSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                query = new ParseQuery<>(installation.getString("AssociationCode"));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, ParseException e) {

                        ParseFile eventFile = assoc.get(0).getParseFile("AdminEventFile");
                        eventFileArray = null;

                        try {
                            byte[] file = eventFile.getData();
                            try {
                                eventFileString = new String(file, "UTF-8");

                                Log.d(TAG, "admin file events ---> Android|" + eventFileString);

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


                            eventFileUpdate = eventFileString;



                        Log.d(TAG, "events FileUpdate --->" + eventFileUpdate);



                        byte[] data = eventFileUpdate.getBytes();
                        eventFile = new ParseFile("calendar.txt", data);


                        try {
                            eventFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                      assoc.get(0).put("Eventdate", strDate);
                        assoc.get(0).put("EventFile", eventFile);

                       try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                        remoteDataTaskClass.execute();



                        Toast toast = Toast.makeText(getBaseContext(), "Events updated.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        Intent mainActivity = new Intent();
                        mainActivity.setClass(CalendarManageActivity.this, ToolsActivity.class);
                        startActivity(mainActivity);


                        finish();


                    }
                });

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if ( !fromTools ) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Guide();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Guide() {

        Intent loadGuide = new Intent();
        loadGuide.setClass(this, GuideActivity.class);
        startActivity(loadGuide);
        //     overridePendingTransition(R.anim.fadeinanimationgallery,R.anim.fadeoutanimationgallery);


    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));



        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.RIGHT);
        getWindow().setEnterTransition(slideTransition);


        Slide slideTransitionExit = new Slide();
        slideTransitionExit.setSlideEdge(Gravity.RIGHT);
        getWindow().setExitTransition(slideTransitionExit);



    }



    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


}

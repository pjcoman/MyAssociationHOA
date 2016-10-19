package comapps.com.myassociationhoa.messageboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.github.clans.fab.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class MBActivity extends AppCompatActivity {

    private static final String TAG = "MBACTIVITY";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";
    private static final String MYPREFERENCES = "MyPrefs";

    ParseQuery<ParseObject> query;
    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences.Editor editorVisited;
    private SharedPreferences sharedPreferences;


    private FloatingActionButton mFab;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setupWindowAnimations();


        setContentView(R.layout.content_main_mb);


        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Message Board");
        }








        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        String type = sharedPreferencesVisited.getString("MEMBERTYPE", "");

        editorVisited = sharedPreferencesVisited.edit();
        editorVisited.putBoolean("FROMMB", true);
        editorVisited.apply();




        mFab = (FloatingActionButton) findViewById(R.id.fab);



        if (sharedPreferences.getString("defaultRecord(38)", "No").equals("No") && type.toLowerCase().equals("member")) {
            Log.d(TAG, "member MB access ----> " + sharedPreferences.getString("defaultRecord(38)", "No") + " " + type.toLowerCase());
           mFab.setVisibility(View.GONE);
            } else {
            Log.d(TAG, "member MB access ----> " + sharedPreferences.getString("defaultRecord(38)", "No") + " " + type.toLowerCase());
             mFab.setVisibility(View.VISIBLE);
            }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdfIn = new SimpleDateFormat("M/d/yy, h:mm a");
                String strDateIn = sdfIn.format(new Date());
                editorVisited = sharedPreferencesVisited.edit();
                editorVisited.putString("LASTMBVISIT", strDateIn);
                editorVisited.commit();



                Intent mbAddActivity = new Intent();
                mbAddActivity.setClass(getApplicationContext(), PopMBAddMessage.class);
                startActivity(mbAddActivity);
                finish();



            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

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
    public void onBackPressed() {

       /* ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                MBActivity.this);*/

        SimpleDateFormat sdfIn = new SimpleDateFormat("M/d/yy, h:mm a");
        String strDateIn = sdfIn.format(new Date());
        editorVisited = sharedPreferencesVisited.edit();
        editorVisited.putString("LASTMBVISIT", strDateIn);
        editorVisited.commit();

      /*  Intent mainActivity = new Intent();
        mainActivity.setClass(this, MainActivity.class);
        startActivity(mainActivity);*/

        finishAfterTransition();





    }




}

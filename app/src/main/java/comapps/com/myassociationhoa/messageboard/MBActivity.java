package comapps.com.myassociationhoa.messageboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

    ParseQuery<ParseObject> query;
    SharedPreferences sharedPreferencesVisited;
    SharedPreferences.Editor editorVisited;

    private FloatingActionButton mFab;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_mb);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Message Board");
        }



        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        String type = sharedPreferencesVisited.getString("MEMBERTYPE", "");

        editorVisited = sharedPreferencesVisited.edit();
        editorVisited.putBoolean("FROMMB", true);
        editorVisited.putBoolean("MBFIRSTVIEW", false);
        editorVisited.putBoolean("SHOWREDDOT", false);
        editorVisited.apply();




        mFab = (FloatingActionButton) findViewById(R.id.fab);



            mFab.setVisibility(View.VISIBLE);



        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

    @Override
    public void onBackPressed() {


        this.finish();
    }


}

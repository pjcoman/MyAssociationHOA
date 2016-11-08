package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.AdminMBObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
@SuppressWarnings("ALL")
public class AdminMBRecyclerViewActivity extends AppCompatActivity {

    private static final String TAG = "ADMINMB_RECYCLERACTIVITY";

    private static final String VISITEDPREFERENCES = "VisitedPrefs";
    private static final String MYPREFERENCES = "MyPrefs";

    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences sharedPreferences;


    private RecyclerView recyclerView;
    private AdminMBRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private AdminMBObject adminMBObject;
    private ArrayList<AdminMBObject> posts;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_admin_mb);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Admin Message Board");
        }



        recyclerView = (RecyclerView) findViewById(R.id.admin_mb_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        posts = new ArrayList<>();
        Integer admin_mbItems = sharedPreferencesVisited.getInt("admin_mbSize", 0);

        for (int i = 0; i < admin_mbItems; i++) {

            String jsonAdminMbObject = sharedPreferences.getString("admin_mbObject" + "[" + i + "]", "");
            Log.d(TAG, "admin mb json string is " + jsonAdminMbObject);
            Gson gson = new Gson();
            adminMBObject = gson.fromJson(jsonAdminMbObject, AdminMBObject.class);
            posts.add(adminMBObject);

        }

        if ( sharedPreferencesVisited.getInt("admin_mbSize", 0) != 0 ) {

            adapter = new AdminMBRecyclerViewAdapter(this, posts);
            recyclerView.setAdapter(adapter);



        } else {



            Toast toast = Toast.makeText(this, "No messages.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            finish();


        }




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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (adapter != null) {
            adapter.restoreStates(savedInstanceState);
        }
    }

    private void setupList() {
        recyclerView = (RecyclerView) findViewById(R.id.admin_mb_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminMBRecyclerViewAdapter(this, posts);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {


        this.finish();
    }



}

package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.RosterObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class DirectoryUpdateActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    private static final String TAG = "DIRECTORYUPDATEACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";
    private ArrayList<RosterObject> rosterList;
    private DirectoryUpdateAdapter adapter;

    private SearchView search_view;


    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_directory);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Android Directory Update");
        }





        search_view = (SearchView) findViewById(R.id.search_view);
        search_view.setQueryHint("Search Name");

        int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) search_view.findViewById(searchSrcTextId);
        searchEditText.setTextSize(14);

        hideSoftKeyboard();

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        rosterList = new ArrayList<>();

        for (int i = 0; i < sharedPreferences.getInt("rosterSize", 0); i++) {

            String jsonRosterObject = sharedPreferences.getString("rosterObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            RosterObject rosterObject = gson.fromJson(jsonRosterObject, RosterObject.class);
            rosterObject.getLastName();
            rosterObject.getFirstName();
            rosterObject.getHomeAddress1();
            rosterObject.getHomeAddress2();
            rosterObject.getHomeCity();
            rosterObject.getHomeState();
            rosterObject.getHomeZip();
            rosterObject.getHomePhone();
            rosterObject.getMobilePhone();
            rosterObject.getEmail();
            rosterObject.getEmergencyName();
            rosterObject.getEmergencyPhoneNumber();
            rosterObject.getWinterAddress1();
            rosterObject.getWinterAddress2();
            rosterObject.getStatus();
            rosterObject.getWinterCity();
            rosterObject.getWinterState();
            rosterObject.getWinterZip();
            rosterObject.getWinterPhone();
            rosterObject.getMemberNumber();
            rosterObject.getWinterEmail();
            rosterObject.getGroups();


            rosterList.add(rosterObject);

        }

        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setTextFilterEnabled(true);



        adapter = new DirectoryUpdateAdapter(this, rosterList);
        lv.setAdapter(adapter);


        search_view.setOnQueryTextListener(this);





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

    private void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }





    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

}

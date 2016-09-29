package comapps.com.myassociationhoa.autos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.AutoObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class AutosMyInfoActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    private static final String TAG = "AUTOACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";
    ArrayList<AutoObject> autosList;
    AutosMyInfoAdapter adapter;
    ListView lv;
    Bundle bundle;
    String memberFilter = "";

    int autoCount = 0;

    SearchView search_view;

    EditText search;

    private FloatingActionButton mFab;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_autos);

        mFab = (FloatingActionButton) findViewById(R.id.fab);


        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("My Autos");
        }


        search_view = (SearchView) findViewById(R.id.search_view_autos);
        search_view.setQueryHint("Search Make, Model, Color or Plate");

        int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) search_view.findViewById(searchSrcTextId);
        searchEditText.setTextSize(14);


        hideSoftKeyboard();


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);



        bundle = getIntent().getExtras();

        Log.d(TAG,"details = " + bundle2string(bundle));



            memberFilter = bundle.getString("memberNumber");

            search_view.setVisibility(View.GONE);

            getData();


            if ( autoCount > 0 ) {

                if (!sharedPreferences.getBoolean("TOASTOFF", false)) {

                    Toast toast = Toast.makeText(getBaseContext(), "Click to edit auto.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    toast = Toast.makeText(getBaseContext(), "Long click to delete auto.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 50);
                    toast.show();

                }

                editor = sharedPreferences.edit();
                editor.putBoolean("TOASTOFF", true);
                editor.apply();

            }






        lv = (ListView) findViewById(R.id.list_view);
        lv.setTextFilterEnabled(true);



        adapter = new AutosMyInfoAdapter(this, autosList);
        lv.setAdapter(adapter);




        search_view.setOnQueryTextListener(this);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "mfab clicked");

                Intent autoAddActivity = new Intent();
                autoAddActivity.setClass(getApplicationContext(), PopAutosAddAuto.class);
                startActivity(autoAddActivity);
                finish();


            }
        });



            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "listview clicked " + position);
                }
            });

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.d(TAG, "listview longclicked " + position);

                    return false;
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

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void getData() {

        autosList = new ArrayList<AutoObject>();

        for (int i = 0; i < sharedPreferences.getInt("autosObjectsSize", 0); i++) {

            String jsonAutoObject = sharedPreferences.getString("autoObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            AutoObject autoObject = gson.fromJson(jsonAutoObject, AutoObject.class);
            autoObject.getOwner();
            autoObject.getMake();
            autoObject.getModel();
            autoObject.getMemberNumber();
            autoObject.getColor();
            autoObject.getPlate();
            autoObject.getYear();
            autoObject.getTag();

            if (memberFilter.equals("")) {

                autosList.add(autoObject);

            } else if (memberFilter.equals(autoObject.getMemberNumber())) {


                autosList.add(autoObject);
                autoCount++;


            }

            Collections.sort(autosList, new Comparator<AutoObject>()
            {
                @Override
                public int compare(AutoObject a1, AutoObject a2) {

                    return a1.getMake().compareTo(a2.getMake());
                }
            });



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

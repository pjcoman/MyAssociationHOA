package comapps.com.myassociationhoa.guests;

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
import comapps.com.myassociationhoa.objects.GuestObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
@SuppressWarnings("ALL")
public class GuestsMyInfoActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    private static final String TAG = "GUESTACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";
    private ArrayList<GuestObject> guestsList;
    private ListView lv;
    private GuestsAdapter adapter;
    private GuestsMyInfoAdapter adapterMyInfo;
    private Bundle bundle;
    private String memberFilter = "";

    private SearchView search_view;
    private FloatingActionButton mFab;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int guestsCount = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_guests);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setVisibility(View.GONE);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("My Guests");
        }


        search_view = (SearchView) findViewById(R.id.search_view_guests);
        search_view.setQueryHint("Search Owner or Guest Name");

        int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) search_view.findViewById(searchSrcTextId);
        searchEditText.setTextSize(14);

        hideSoftKeyboard();

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        bundle = getIntent().getExtras();



            memberFilter = bundle.getString("memberNumber");

                mFab.setVisibility(View.VISIBLE);




            search_view.setVisibility(View.GONE);


        getData();


            if ( guestsCount > 0 ) {

                if (!sharedPreferences.getBoolean("TOASTOFF", false)) {

                    Toast toast = Toast.makeText(getBaseContext(), "Click to edit guest.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    toast = Toast.makeText(getBaseContext(), "Long click to delete guest.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 50);
                    toast.show();

                }

                editor = sharedPreferences.edit();
                editor.putBoolean("TOASTOFF", true);
                editor.apply();

            }







        lv = (ListView) findViewById(R.id.list_view);
        lv.setTextFilterEnabled(true);



            adapterMyInfo = new GuestsMyInfoAdapter(this, guestsList);
            lv.setAdapter(adapterMyInfo);




        search_view.setOnQueryTextListener(this);




        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent guestAddActivity = new Intent();
                guestAddActivity.setClass(getApplicationContext(), PopGuestsAddGuest.class);
                startActivity(guestAddActivity);
                finish();


            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "listview clicked" + position);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "listview longclicked" + position);

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

    private void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void getData() {

        guestsList = new ArrayList<>();

        for (int i = 0; i < sharedPreferences.getInt("guestObjectsSize", 0); i++) {

            String jsonGuestObject = sharedPreferences.getString("guestObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            GuestObject guestObject = gson.fromJson(jsonGuestObject, GuestObject.class);
            guestObject.getGuestOwner();
            guestObject.getGuestOwnerMemberNumber();
            guestObject.getGuestType();
            guestObject.getGuestStartdate();
            guestObject.getGuestEnddate();
            guestObject.getMondayAccess();
            guestObject.getTuesdayAccess();
            guestObject.getWednesdayAccess();
            guestObject.getThursdayAccess();
            guestObject.getFridayAccess();
            guestObject.getSaturdayAccess();
            guestObject.getSundayAccess();
            guestObject.getGuestDescription();
            guestObject.getGuestName();
            guestObject.getOwnerContactNumberType();
            guestObject.getOwnerContactNumber();


            if (memberFilter.equals("")) {

                guestsList.add(guestObject);

            } else if (memberFilter.equals(guestObject.getGuestOwnerMemberNumber())) {


                guestsList.add(guestObject);
                guestsCount++;

            }

            Collections.sort(guestsList, new Comparator<GuestObject>()
            {
                @Override
                public int compare(GuestObject g1, GuestObject g2) {

                    return g1.getGuestName().compareTo(g2.getGuestName());
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

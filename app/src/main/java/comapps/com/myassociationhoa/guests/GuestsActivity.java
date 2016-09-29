package comapps.com.myassociationhoa.guests;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

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
public class GuestsActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    private static final String TAG = "GUESTACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";
    ArrayList<GuestObject> guestsList;
    ListView lv;
    GuestsAdapter adapter;
    String memberFilter = "";

    SearchView search_view;
    private FloatingActionButton mFab;

    Bundle bundle;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int guestsCount;


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
            bar.setTitle("Guests");
        }


        search_view = (SearchView) findViewById(R.id.search_view_guests);
        search_view.setQueryHint("Search Owner or Guest Name");

        int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) search_view.findViewById(searchSrcTextId);
        searchEditText.setTextSize(14);

        hideSoftKeyboard();

        bundle = getIntent().getExtras();

        if ( bundle != null ) {

            memberFilter = bundle.getString("memberNumber");
            if ( bundle.getBoolean("fromPopInfo")) {
                mFab.setVisibility(View.VISIBLE);
            } else {
                mFab.setVisibility(View.GONE);
            }

            bar.setTitle("My Pets");
            search_view.setVisibility(View.GONE);


        }



        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);







        getData();

        lv = (ListView) findViewById(R.id.list_view);
        lv.setTextFilterEnabled(true);



            adapter = new GuestsAdapter(this, guestsList);
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

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void getData() {

        guestsList = new ArrayList<GuestObject>();

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

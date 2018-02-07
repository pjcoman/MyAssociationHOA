package comapps.com.myassociationhoa.pets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import comapps.com.myassociationhoa.objects.PetObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
@SuppressWarnings("ALL")
public class PetsActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    private static final String TAG = "PETSACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";
    private ArrayList<PetObject> petsList;
    private PetsAdapter adapter;
    private Bundle bundle;
    private String memberFilter = "";

    private SearchView search_view;
    private FloatingActionButton mFab;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setupWindowAnimations();


        setContentView(R.layout.content_main_pets);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setVisibility(View.GONE);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Pets");
        }


        search_view = (SearchView) findViewById(R.id.search_view_pets);
        search_view.setQueryHint("Search Type or Color");

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

            assert bar != null;
            bar.setTitle("My Pets");
            search_view.setVisibility(View.GONE);


        }






        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        petsList = new ArrayList<>();

        for (int i = 0; i < sharedPreferences.getInt("petObjectsSize", 0); i++) {

            String jsonPetObject = sharedPreferences.getString("petObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            PetObject petObject = gson.fromJson(jsonPetObject, PetObject.class);
            petObject.getOwner();
            petObject.getType();
            petObject.getName();
            petObject.getBreed();
            petObject.getColor();
            petObject.getWeight();
            petObject.getMemberNumber();
            petObject.getMisc();

            if (memberFilter.equals("")) {

                petsList.add(petObject);

            } else if (memberFilter.equals(petObject.getMemberNumber())) {


                petsList.add(petObject);

            }

            Collections.sort(petsList, new Comparator<PetObject>()
            {
                @Override
                public int compare(PetObject p1, PetObject p2) {

                    return p1.getType().compareTo(p2.getType());
                }
            });


            ListView lv = (ListView) findViewById(R.id.list_view);
            lv.setTextFilterEnabled(true);


            adapter = new PetsAdapter(this, petsList);
            lv.setAdapter(adapter);


            search_view.setOnQueryTextListener(this);

        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent petAddActivity = new Intent();
                petAddActivity.setClass(getApplicationContext(), PopPetsAddPet.class);
                startActivity(petAddActivity);
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

    private void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

}

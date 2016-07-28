package comapps.com.myassociationhoa.pets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.PetObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class PetsActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    private static final String TAG = "PETACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";
    ArrayList<PetObject> petsList;
    PetsAdapter adapter;
    Bundle bundle;
    String memberFilter = "";

    SearchView search_view;
    private FloatingActionButton mFab;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_pets);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setVisibility(View.GONE);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Pets");
        }



        search_view = (SearchView) findViewById(R.id.search_view);
        search_view.setQueryHint("Search Type or Color");

        int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) search_view.findViewById(searchSrcTextId);
        searchEditText.setTextSize(14);


        bundle = getIntent().getExtras();

        if ( bundle != null ) {

            memberFilter = bundle.getString("memberNumber");
            if ( bundle.getString("fromPopInfo").equals("YES")) {
                mFab.setVisibility(View.VISIBLE);
            }

            bar.setTitle("My Pets");
            search_view.setVisibility(View.GONE);


        }






        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        petsList = new ArrayList<PetObject>();

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
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

}

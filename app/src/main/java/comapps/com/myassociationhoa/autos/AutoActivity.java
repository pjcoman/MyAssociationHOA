package comapps.com.myassociationhoa.autos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.AutoObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class AutoActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    private static final String TAG = "AUTOACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";
    ArrayList<AutoObject> autosList;
    AutoAdapter adapter;

    SearchView search_view;

    EditText search;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_autos);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Autos");
        }




        search_view = (SearchView) findViewById(R.id.search_view);
        search_view.setQueryHint("Search Make, Model or Plate");

        int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) search_view.findViewById(searchSrcTextId);
        searchEditText.setTextSize(14);


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


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
            autosList.add(autoObject);

        }

        ListView lv = (ListView) findViewById(R.id.list_view);
        lv.setTextFilterEnabled(true);



        adapter = new AutoAdapter(this, autosList);
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

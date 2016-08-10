package comapps.com.myassociationhoa.maintenance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.MaintenanceObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/22/2016.
 */
public class MaintenanceActivity extends AppCompatActivity {

    private static final String TAG = "SERVICEPROVIDERACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    private MaintenanceObject maintenanceObject;
    private ArrayList<MaintenanceObject> maintenanceObjects;
    private MaintenanceAdapter maintenanceAdapter;

    private SharedPreferences sharedPreferences;
    private ArrayList<String> maintenanceCategories = new ArrayList<String>();


    private String maintenanceCatString;

    private ListView maintenanceCatList;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_maintenance);

        maintenanceCatList = (ListView) findViewById(R.id.listViewMaintenanceCategories);
        back = (Button) findViewById(R.id.backToCatList);


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Select Category");
        }



        final int maintenanceObjectSize = Integer.valueOf(sharedPreferences.getString("maintenanceObjectsSize", "0"));

        maintenanceObjects = new ArrayList<>();

        maintenanceCategories.add("All");

        for (int i = 0; i < maintenanceObjectSize; i++) {

            String jsonMaintenanceObject = sharedPreferences.getString("maintenanceObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            maintenanceObject = gson.fromJson(jsonMaintenanceObject, MaintenanceObject.class);


            Log.d(TAG, "maintenance cat is " + maintenanceObject.getMaintenanceCategory());


            if ( !maintenanceCategories.contains(maintenanceObject.getMaintenanceCategory())) {
                maintenanceCategories.add(maintenanceObject.getMaintenanceCategory());

            }

            maintenanceObjects.add(maintenanceObject);


        }





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });




        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.textviewlist, maintenanceCategories);

        listAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String arg1, String arg0) {
                return arg1.compareTo(arg0);
            }
        });



        maintenanceCatList.setAdapter(listAdapter);


        maintenanceCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {


                maintenanceCatString = (String) adapter.getItemAtPosition(position);

                Log.d(TAG, "maintenance cat clicked is " + maintenanceCatString);
                String providerTypeSizeString;


                    android.support.v7.app.ActionBar bar = getSupportActionBar();

                    if (bar != null) {
                        bar.setTitle(maintenanceCatString);
                    }

                    back.setVisibility(View.VISIBLE);

                maintenanceCatList.invalidateViews();

                maintenanceCatList = (ListView) findViewById(R.id.listViewMaintenanceCategories);



                maintenanceObjects.clear();


                for (int i = 0; i < maintenanceObjectSize; i++) {

                    String jsonMaintenanceObject = sharedPreferences.getString("maintenanceObject" + "[" + i + "]", "");
                    Gson gson = new Gson();
                    maintenanceObject = gson.fromJson(jsonMaintenanceObject, MaintenanceObject.class);



                    if ( maintenanceObject.getMaintenanceCategory().equals(maintenanceCatString) || maintenanceCatString.equals("All")) {
                        maintenanceObjects.add(maintenanceObject);

                    }




                }



                maintenanceAdapter = new MaintenanceAdapter(MaintenanceActivity.this,
                        maintenanceObjects);



                // Binds the Adapter to the ListView
                maintenanceCatList.setAdapter(maintenanceAdapter);








            /*    } else {

                    Intent intentEditServiceProvider = new Intent();
                    intentEditServiceProvider.setClass(getApplicationContext(), ServiceProviderEditActivity.class);
                    intentEditServiceProvider.putExtra("serviceProviderObjectGson", "");
                    intentEditServiceProvider.putExtra("FROMSERVICEPROVIDERADD", "YES");
                    intentEditServiceProvider.putExtra("PROVIDERTYPE", providerTypeString);
                    startActivity(intentEditServiceProvider);




                }*/
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


}

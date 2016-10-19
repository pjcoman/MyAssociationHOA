package comapps.com.myassociationhoa.service_providers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.ProviderObject;
import comapps.com.myassociationhoa.objects.ServiceProviderObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/22/2016.
 */
public class ServiceProviderActivity extends AppCompatActivity {

    private static final String TAG = "SERVICEPROVIDERACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    private SharedPreferences sharedPreferences;
    private ListView listview;
    private final ArrayList<String> providerTypes = new ArrayList<>();
    private Integer providerSize;
    private List<ServiceProviderObject> providerList = null;
    private ServiceProviderAdapter serviceProviderAdapter;

    private FloatingActionButton mFabAddService;
    private FloatingActionButton mFabAddProvider;

    private String providerTypeString;

    private ListView providersTypeList;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setupWindowAnimations();


        setContentView(R.layout.content_main_providers);

        providersTypeList = (ListView) findViewById(R.id.listViewProviders);
        back = (Button) findViewById(R.id.backToProviders);
        mFabAddService = (FloatingActionButton) findViewById(R.id.fab);
        mFabAddProvider = (FloatingActionButton) findViewById(R.id.fab2);


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Service Categories");
        }



        providerSize = Integer.valueOf(sharedPreferences.getString("providerSize", "0"));

        for (int i = 0; i < providerSize; i++) {

            String jsonProviderObject = sharedPreferences.getString("providerObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            ProviderObject providerObject = gson.fromJson(jsonProviderObject, ProviderObject.class);

            Log.d(TAG, "provider type is " + providerObject.getProviderType());



            providerTypes.add(providerObject.getProviderType() + " (" + providerObject.getProviderCount() + ")");


        }





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });




        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, R.layout.textviewlistserviceproviders, android.R.id.text1, providerTypes);

        listAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String arg1, String arg0) {
                return arg1.compareTo(arg0);
            }
        });



        providersTypeList.setAdapter(listAdapter);


        providersTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {


                providerTypeString = (String) adapter.getItemAtPosition(position);
                int parenthesisOpenIndex = providerTypeString.indexOf("(");
                int parenthesisClosedIndex = providerTypeString.indexOf(")");
                Log.d(TAG, "provider type clicked is " + providerTypeString);
                String providerTypeSizeString;
                providerTypeSizeString = providerTypeString.substring(parenthesisOpenIndex + 1, parenthesisClosedIndex);
                Log.d(TAG, "provider type size is " + providerTypeSizeString);

                providerTypeString = providerTypeString.substring(0, parenthesisOpenIndex - 1);

           //

                    android.support.v7.app.ActionBar bar = getSupportActionBar();

                    if (bar != null) {
                        bar.setTitle(providerTypeString);
                    }

                    back.setVisibility(View.VISIBLE);
                    mFabAddService.setVisibility(View.GONE);

                if (sharedPreferences.getString("defaultRecord(47)", "No").equals("No") && ParseInstallation.getCurrentInstallation().get("MemberType").equals("Member")) {

                    mFabAddService.setVisibility(View.GONE);

                } else {

                    mFabAddProvider.setVisibility(View.VISIBLE);

                }




                    new RemoteDataTask().execute(providerTypeString);

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

        if (sharedPreferences.getString("defaultRecord(47)", "No").equals("No") && ParseInstallation.getCurrentInstallation().get("MemberType").equals("Member")) {

            mFabAddService.setVisibility(View.GONE);

        }


        mFabAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "fab add service clicked.");

                mFabAddService.setVisibility(View.GONE);

                Intent addServiceActivity = new Intent();
                addServiceActivity.setClass(ServiceProviderActivity.this, ServiceProviderAddCategoryPop.class);
                startActivity(addServiceActivity);



            }
        });

        mFabAddProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "fab add provider clicked.");

                try {



                    Intent intentEditServiceProvider = new Intent();
                    intentEditServiceProvider.setClass(getApplicationContext(), ServiceProviderEditActivity.class);
                    intentEditServiceProvider.putExtra("serviceProviderObjectGson", "");
                    intentEditServiceProvider.putExtra("FROMSERVICEPROVIDERADD", "YES");
                    intentEditServiceProvider.putExtra("PROVIDERTYPE", providerTypeString);
                    startActivity(intentEditServiceProvider);



                } catch (Exception ignored) {


                }


            }
        });







    }

    private class RemoteDataTask extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {

            String providerTypeFilter = params[0];
            Log.d(TAG, "provider type filter is " + providerTypeFilter);


          /*  try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            providerList = new ArrayList<>();


            try {

                providerSize = Integer.valueOf(sharedPreferences.getString("providerSize", ""));

                Log.d(TAG, "provider size is " + providerSize);

                for (int i = 0; i < providerSize; i++) {

                    String jsonProviderObject = sharedPreferences.getString("providerObject" + "[" + i + "]", "");
                    Gson gson = new Gson();
                    ProviderObject providerObject = gson.fromJson(jsonProviderObject, ProviderObject.class);

                   Log.d(TAG, "provider type is " + providerObject.getProviderType());

                    int count = Integer.valueOf(providerObject.getProviderCount());

                    Log.d(TAG, "provider count is " + providerObject.getProviderCount());
                    Log.d(TAG, "provider type is " + providerObject.getProviderType());
                    Log.d(TAG, "provider list is " + providerObject.getProviderList());

                    if (providerTypeFilter.equals(providerObject.getProviderType()) && (count > 0)) {

                    Log.d(TAG, "provider type added " + providerObject.getProviderType());



                        String[] providerListArray = providerObject.getProviderList().split("\\^", -1);

                        i = 0;

                        for ( String providerInfo: providerListArray) {

                            Log.d(TAG, "provider type added field " + i + " " + providerInfo);

                            i++;
                        }

                        int j = 0;

                        for (i = 0; i < providerListArray.length; i++) {


                            ServiceProviderObject object = new ServiceProviderObject();
                            object.setProviderName(providerListArray[j]);
                            j++;
                            object.setProviderAddress(providerListArray[j]);
                            j++;
                            object.setProviderAddress2(providerListArray[j]);
                            j++;
                            object.setProviderCity(providerListArray[j]);
                            j++;
                            object.setProviderState(providerListArray[j]);
                            j++;
                            object.setProviderZip(providerListArray[j]);
                            j++;
                            object.setProviderPhoneNumber(providerListArray[j]);
                            j++;
                            object.setProviderNotes(providerListArray[j]);
                            j++;

                            Log.d(TAG, "ServiceProviderObject is " + object.toString());
                            Log.d(TAG, "providerListArray length is " + providerListArray.length);
                            Log.d(TAG, "i is " + i);
                            Log.d(TAG, "j is " + j);


                            providerList.add(object);

                        }
                    }
                }


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listViewProviders);
            // Pass the results into ParseListViewAdapter.java
            serviceProviderAdapter = new ServiceProviderAdapter(ServiceProviderActivity.this,
                    providerList);



            // Binds the Adapter to the ListView
            listview.setAdapter(serviceProviderAdapter);

            refreshDisplay();


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

    private void refreshDisplay() {


        serviceProviderAdapter = new ServiceProviderAdapter(this, providerList);
        // Binds the Adapter to the ListView
        listview.setAdapter(serviceProviderAdapter);

        listview.invalidateViews();


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
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


}

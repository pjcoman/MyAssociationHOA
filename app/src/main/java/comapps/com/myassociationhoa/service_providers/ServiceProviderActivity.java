package comapps.com.myassociationhoa.service_providers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
    private ArrayList<String> providerTypes = new ArrayList<String>();
    private Integer providerSize;
    private List<ServiceProviderObject> providerList = null;
    private ServiceProviderAdapter serviceProviderAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_providers);

        final ListView providersTypeList = (ListView) findViewById(R.id.listViewProviders);
        final Button back = (Button) findViewById(R.id.backToProviders);


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

            providerTypes.add(providerObject.getProviderType());


        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });





        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, providerTypes);


        providersTypeList.setAdapter(listAdapter);


        providersTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String providerTypeString = (String) adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row



                android.support.v7.app.ActionBar bar = getSupportActionBar();

                if (bar != null) {
                    bar.setTitle(providerTypeString);
                }

                back.setVisibility(View.VISIBLE);

                new RemoteDataTask().execute(providerTypeString);
            }
        });




    }

    private class RemoteDataTask extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {

            String providerTypeFilter = params[0];
            Log.d(TAG, "provider type filter is " + providerTypeFilter);


            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            providerList = new ArrayList<>();


            try {

                providerSize = Integer.valueOf(sharedPreferences.getString("providerSize", ""));

                for (int i = 0; i < providerSize; i++) {

                    String jsonProviderObject = sharedPreferences.getString("providerObject" + "[" + i + "]", "");
                    Gson gson = new Gson();
                    ProviderObject providerObject = gson.fromJson(jsonProviderObject, ProviderObject.class);

                    Log.d(TAG, "provider type is " + providerObject.getProviderType());

                    int count = Integer.valueOf(providerObject.getProviderCount());

                    if (providerTypeFilter.equals(providerObject.getProviderType()) && (count > 0)) {

                        Log.d(TAG, "provider type added " + providerObject.getProviderType());

                        String providerListField = providerObject.getProviderList();
                        String[] providerListArray = providerListField.split("\\^");

                        int j = 0;

                        for (i = 0; i < Integer.valueOf(providerObject.getProviderCount()); i++) {


                            ServiceProviderObject object = new ServiceProviderObject();
                            object.setProviderName(providerListArray[j]);
                            j++;
                            object.setProviderAddress(providerListArray[j]);
                            j++;
                            object.setProviderAddress2(providerListArray[j]);
                            j++;
                            object.setProviderCity(providerListArray[j]);
                            j++;
                            object.setProviderCountry(providerListArray[j]);
                            j++;
                            object.setProviderZip(providerListArray[j]);
                            j++;
                            object.setProviderPhoneNumber(providerListArray[j]);
                            j++;
                            object.setProviderNotes(providerListArray[j]);
                            j++;
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

    private void sendEmail() {
        // TODO Auto-generated method stub

        // The following code is the implementation of Email client
        Intent intentSendEmail = new Intent(Intent.ACTION_SEND);
        intentSendEmail.setType("text/plain");
        String[] address = {"dewing@ewinginvestments.com"};

        intentSendEmail.putExtra(Intent.EXTRA_EMAIL, address);
        intentSendEmail.putExtra(Intent.EXTRA_SUBJECT,
                "Question from ");

        startActivityForResult((Intent.createChooser(intentSendEmail, "Email")), 1);

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



    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


}

package comapps.com.myassociationhoa.maintenance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.MaintenanceCategoryObject;
import comapps.com.myassociationhoa.objects.MaintenanceObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/22/2016.
 */
@SuppressWarnings("ALL")
public class MaintenanceActivity extends AppCompatActivity {

    private static final String TAG = "SERVICEPROVIDERACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private ParseQuery query;



    private MaintenanceObject maintenanceObject;
    private ArrayList<MaintenanceObject> maintenanceObjects;
    private MaintenanceCategoryObject maintenanceCategoryObject;
    private ArrayList<MaintenanceCategoryObject> maintenanceCategoryObjects;
    private MaintenanceAdapter maintenanceAdapter;

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesVisited;
    private final ArrayList<String> maintenanceCategories = new ArrayList<>();
    private String[] maintenanceCategoriesAll;
    private final ArrayList<String> maintenanceItemCategories = new ArrayList<>();


    private String maintenanceCatString;
    private String maintenanceString;
    private String pushMessageString;

    private EditText etMaintenanceDesc;
    private EditText etMaintenanceNotes;

    private ListView maintenanceCatList;
    private Button back;
    private Button saveItem;
    private Button buttonItemType;

    private FloatingActionButton mFab;
    private ScrollView addItem;

    int i = 0;
    private int j = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setupWindowAnimations();


        setContentView(R.layout.content_main_maintenance);

        maintenanceCatList = (ListView) findViewById(R.id.listViewMaintenanceCategories);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        back = (Button) findViewById(R.id.backToCatList);
        addItem = (ScrollView) findViewById(R.id.scrollViewAddItem);
        saveItem = (Button) findViewById(R.id.buttonSaveItem);
        buttonItemType = (Button) findViewById(R.id.buttonItemType);

        etMaintenanceDesc = (EditText) findViewById(R.id.editTextMaintenanceDesc);
        etMaintenanceNotes = (EditText) findViewById(R.id.editTextMaintenanceNotes);

        addItem.setVisibility(View.GONE);
        saveItem.setVisibility(View.GONE);


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Select Category");
        }

        //***************************************************MAINTENANCE ITEMS*****************************************************************************

        final int maintenanceObjectSize = Integer.valueOf(sharedPreferences.getString("maintenanceObjectsSize", "0"));

        maintenanceObjects = new ArrayList<>();
        maintenanceCategoryObjects = new ArrayList<>();

        for (int i = 0; i < maintenanceObjectSize; i++) {

            String jsonMaintenanceObject = sharedPreferences.getString("maintenanceObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            maintenanceObject = gson.fromJson(jsonMaintenanceObject, MaintenanceObject.class);


            Log.d(TAG, "maintenance category for item is " + maintenanceObject.getMaintenanceCategory());
            maintenanceItemCategories.add(maintenanceObject.getMaintenanceCategory());
            maintenanceObjects.add(maintenanceObject);


        }


        //***************************************************MAINTENANCE CATEGORIES*****************************************************************************


        final int maintenanceCategoryObjectSize = sharedPreferences.getInt("maintenanceCategoryObjectsSize", 0);

        maintenanceCategories.add("All");
        maintenanceCategoriesAll = new String[maintenanceCategoryObjectSize];

        for (int i = 0; i < maintenanceCategoryObjectSize; i++) {

            String jsonMaintenanceCategoryObject = sharedPreferences.getString("maintenanceCategoryObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            maintenanceCategoryObject = gson.fromJson(jsonMaintenanceCategoryObject, MaintenanceCategoryObject.class);


            Log.d(TAG, "maintenance category is " + maintenanceCategoryObject.getMaintenanceCatName());

            maintenanceCategoriesAll[i] = maintenanceCategoryObject.getMaintenanceCatName();

            for (String itemCategory : maintenanceItemCategories) {

                if (itemCategory.contains(maintenanceCategoryObject.getMaintenanceCatName()) &&
                        !maintenanceCategories.contains(maintenanceCategoryObject.getMaintenanceCatName())) {
                    maintenanceCategories.add(maintenanceCategoryObject.getMaintenanceCatName());
                }


            }

            maintenanceCategoryObjects.add(maintenanceCategoryObject);

        }
        //*******************************************************************************************************************************************************


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });


        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, R.layout.textviewlist_transparent, maintenanceCategories);

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

                                                          mFab.setVisibility(View.VISIBLE);
                                                          maintenanceCatString = (String) adapter.getItemAtPosition(position);

                                                          Log.d(TAG, "maintenance cat clicked is " + maintenanceCatString);


                                                          final android.support.v7.app.ActionBar bar = getSupportActionBar();

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


                                                              if (maintenanceObject.getMaintenanceCategory().equals(maintenanceCatString) || maintenanceCatString.equals("All") ||
                                                                      maintenanceObject.getMaintenanceCategory().contains(maintenanceCatString)) {
                                                                  maintenanceObjects.add(maintenanceObject);

                                                              }


                                                          }


                                                          maintenanceAdapter = new MaintenanceAdapter(MaintenanceActivity.this,
                                                                  maintenanceObjects);


                                                          // Binds the Adapter to the ListView
                                                          maintenanceCatList.setAdapter(maintenanceAdapter);

                                                          mFab.setOnClickListener(new View.OnClickListener() {
                                                              @Override
                                                              public void onClick(View v) {

                                                                  Log.d(TAG, "mfab clicked");
                                                                  addItem.setVisibility(View.VISIBLE);
                                                                  saveItem.setVisibility(View.VISIBLE);
                                                                  back.setVisibility(View.GONE);
                                                                  mFab.setVisibility(View.GONE);
                                                                  maintenanceCatList.setVisibility(View.GONE);
                                                                  buttonItemType.setText(maintenanceCategoriesAll[j]);


                                                                  assert bar != null;
                                                                  bar.setTitle("Add Maintenance Item");

                                                                  saveItem.setOnClickListener(new View.OnClickListener() {
                                                                      @Override
                                                                      public void onClick(View v) {

                                                                          final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                                                                          query = new ParseQuery<>(installation.getString("AssociationCode"));

                                                                          query.findInBackground(new FindCallback<ParseObject>() {
                                                                              @Override
                                                                              public void done(List<ParseObject> assoc, ParseException e) {


                                                                                  ParseFile categoryFile = assoc.get(0).getParseFile("MaintenanceFile");


                                                                                  try {
                                                                                      byte[] file = categoryFile.getData();
                                                                                      try {
                                                                                          maintenanceString = new String(file, "UTF-8");

                                                                                          Log.d(TAG, "existing maintenance items --->" + maintenanceString);

                                                                                      } catch (UnsupportedEncodingException e1) {
                                                                                          e1.printStackTrace();
                                                                                      }
                                                                                  } catch (ParseException e1) {
                                                                                      e1.printStackTrace();
                                                                                  }


                                                                                  Calendar c = Calendar.getInstance();
                                                                                  SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a");
                                                                                  SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                                                                                  String strDate = sdf.format(c.getTime());


                                                                                  String newMaintenanceItem = "|" + installation.getString("memberName") + "^" + strDate +
                                                                                          "^" + etMaintenanceDesc.getText() + "^" + etMaintenanceNotes.getText() + "^" + buttonItemType.getText();

                                                                                  String maintenanceFileUpdate = maintenanceString + newMaintenanceItem;

                                                                                  if (maintenanceString.length() < 1) {

                                                                                      maintenanceFileUpdate = maintenanceFileUpdate.substring(1);

                                                                                  }


                                                                                  byte[] data = maintenanceFileUpdate.getBytes();
                                                                                  ParseFile maintenanceFile = new ParseFile("Maintenance.txt", data);


                                                                                  try {
                                                                                      maintenanceFile.save();
                                                                                  } catch (ParseException e1) {
                                                                                      e1.printStackTrace();
                                                                                  }


                                                                                  assoc.get(0).put("MaintenanceDate", strDate);
                                                                                  assoc.get(0).put("MaintenanceFile", maintenanceFile);

                                                                                  try {
                                                                                      assoc.get(0).save();
                                                                                  } catch (ParseException e1) {
                                                                                      e1.printStackTrace();
                                                                                  }


                                                                                  Toast toast = Toast.makeText(getBaseContext(), "Maintenance item added.", Toast.LENGTH_LONG);
                                                                                  toast.setGravity(Gravity.CENTER, 0, 0);
                                                                                  toast.show();

                                                                                  if (sharedPreferencesVisited.getBoolean("PARSESERVER", false)) {

                                                                                      pushMessageString = "Maintenance item for " + buttonItemType.getText() + "\nSubmitted By: "
                                                                                              + installation.getString("memberName");

                                                                                      HashMap<String, Object> params = new HashMap<String, Object>();
                                                                                      params.put("AssociationCode", installation.getString("AssociationCode"));
                                                                                      params.put("MemberType", "Administrator");
                                                                                      params.put("Channel", "");
                                                                                      params.put("Message", pushMessageString);
                                                                                      ParseCloud.callFunctionInBackground("SendPush", params, new FunctionCallback<Object>() {
                                                                                          @Override
                                                                                          public void done(Object object, com.parse.ParseException e) {
                                                                                              if (e == null) {

                                                                                              }

                                                                                          }


                                                                                      });

                                                                                  } else {

                                                                                      ParseQuery pushQuery = ParseInstallation.getQuery();
                                                                                      pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));
                                                                                      pushQuery.whereEqualTo("MemberType", "Administrator");

                                                                                      ParsePush push = new ParsePush();
                                                                                      push.setQuery(pushQuery); // Set our Installation query

                                                                                      push.setMessage("Maintenance item for " + buttonItemType.getText() + "\nSubmitted By: "
                                                                                              + installation.getString("memberName"));

                                                                                      push.sendInBackground();

                                                                                  }



                                                                                  Intent intentSendEmail = new Intent(android.content.Intent.ACTION_SEND);
                                                                                  intentSendEmail.setType("text/plain");

                                                                                  for (MaintenanceCategoryObject object: maintenanceCategoryObjects) {

                                                                                      if ( object.getMaintenanceCatName().equals(buttonItemType.getText())) {

                                                                                          String[] address = {object.getMaintenanceCatEmail()};
                                                                                          intentSendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, address);
                                                                                      }
                                                                                  }


                                                                                  intentSendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                                                                          "Maintenance Request from " + installation.getString("memberName"));
                                                                                  intentSendEmail.putExtra(Intent.EXTRA_TEXT,
                                                                                          installation.getString("AssociationCode") + "\nMaintenance Description:\n" +
                                                                                  etMaintenanceDesc.getText() + "\nDate Submitted: " + strDate);

                                                                                  startActivityForResult((Intent.createChooser(intentSendEmail, "Email")), 1);





                                                                              }
                                                                          });

                                                                      }
                                                                  });

                                                                  buttonItemType.setOnClickListener(new View.OnClickListener() {


                                                                      @Override
                                                                      public void onClick(View v) {


                                                                          j++;
                                                                          buttonItemType.setText(maintenanceCategoriesAll[j]);

                                                                          if (j == maintenanceCategoriesAll.length - 1) {
                                                                              j = 0;
                                                                          }

                                                                      }
                                                                  });


                                                              }
                                                          });


                                                      }


                                                  }
        );
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

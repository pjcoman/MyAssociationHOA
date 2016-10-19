package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.maintenance.AddMaintenanceCategory;
import comapps.com.myassociationhoa.objects.MaintenanceCategoryObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/22/2016.
 */
public class EditMaintenanceActivity extends AppCompatActivity {

    private static final String TAG = "EDITMAINTENANCEACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    private MaintenanceCategoryObject maintenanceCategoryObject;



    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final ArrayList<String> maintenanceCategories = new ArrayList<>();
    private final ArrayList<String> maintenanceEmailAddress = new ArrayList<>();

    private ArrayList<MaintenanceCategoryObject> maintenanceCategoryObjects;

    private String[] maintenanceCategoriesAll;

    private ArrayAdapter<String> listAdapter;

    private String maintenanceCatString;
    private String maintenanceEmailString;

    private int maintenanceCategoryObjectSize;


    private ListView maintenanceCatList;

    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_edit_maintenance);

        maintenanceCatList = (ListView) findViewById(R.id.listViewMaintenanceCategories);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        TextView tvMaintenanceCatEmail = (TextView) findViewById(R.id.textViewList);

        mFab.setVisibility(View.VISIBLE);



        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Edit/Add Maint. Categories");
        }

        if ( sharedPreferences.getBoolean("EDITSERVICEHINT", true)) {

            Toast toast = Toast.makeText(getBaseContext(), "Use back button to cancel.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, -20, -20);
            toast.show();

            editor = sharedPreferences.edit();
            editor.putBoolean("EDITSERVICEHINT", false);
              editor.apply();

        }


        maintenanceCategoryObjects = new ArrayList<>();



        maintenanceCategoryObjectSize = sharedPreferences.getInt("maintenanceCategoryObjectsSize", 0);


        maintenanceCategoriesAll = new String[maintenanceCategoryObjectSize];

        for (int i = 0; i < maintenanceCategoryObjectSize; i++) {

            String jsonMaintenanceCategoryObject = sharedPreferences.getString("maintenanceCategoryObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            maintenanceCategoryObject = gson.fromJson(jsonMaintenanceCategoryObject, MaintenanceCategoryObject.class);


            Log.d(TAG, "maintenance category is " + maintenanceCategoryObject.getMaintenanceCatName());

           maintenanceCategories.add(maintenanceCategoryObject.getMaintenanceCatName() + "\n" + maintenanceCategoryObject.getMaintenanceCatEmail());
           maintenanceEmailAddress.add(maintenanceCategoryObject.getMaintenanceCatEmail());

            maintenanceCategoryObjects.add(maintenanceCategoryObject);

        }
        //*******************************************************************************************************************************************************





        listAdapter = new ArrayAdapter<String>(this, R.layout.textviewlist, maintenanceCategories){
            @Override
            public View getView(int position, View convertView,ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(R.id.textViewList);
                Log.d(TAG, "maintenance category text view ----> " + textView.getText() + " " + textView.getText().toString().indexOf("\n"));

                SpannableStringBuilder builder = new SpannableStringBuilder();

                String[] span = textView.getText().toString().split("\n");

                SpannableString str1= new SpannableString(span[0]);
                str1.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0, str1.length(), 0);
                builder.append(str1);

                builder.append("\n");

                SpannableString str2= new SpannableString(span[1]);
                str2.setSpan(new ForegroundColorSpan(Color.parseColor("#1A7929")), 0, str2.length(), 0);
                str2.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(str2);

                textView.setText(builder, TextView.BufferType.SPANNABLE);

                return view;
            }

        };

        listAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String arg1, String arg0) {
                return arg1.compareTo(arg0);
            }
        });




        maintenanceCatList.setAdapter(listAdapter);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "mfab clicked");

                Intent addMaintenanceActivity = new Intent();
                addMaintenanceActivity.setClass(getApplicationContext(), AddMaintenanceCategory.class);
                startActivity(addMaintenanceActivity);
                finish();





            }


        });




        maintenanceCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                      @Override
                                                      public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                                              long arg3) {

                                                          String[] tempString;
                                                          tempString = (adapter.getItemAtPosition(position)).toString().split("\n");

                                                          maintenanceCatString = tempString[0];
                                                          maintenanceEmailString = tempString[1];


                                                          Log.d(TAG, "maintenance cat clicked is " + maintenanceCatString);

                                                          maintenanceCategoryObject = new MaintenanceCategoryObject();
                                                          maintenanceCategoryObject.setMaintenanceCatName(maintenanceCatString);
                                                          maintenanceCategoryObject.setMaintenanceCatEmail(maintenanceEmailAddress.get(position));

                                                          Gson gson = new Gson();
                                                          String jsonMCObject = gson.toJson(maintenanceCategoryObject);

                                                          Intent editMaintenanceActivity = new Intent();
                                                          editMaintenanceActivity.setClass(getApplicationContext(), AddMaintenanceCategory.class);
                                                          editMaintenanceActivity.putExtra("MAINTENANCECAT", maintenanceCatString);
                                                          editMaintenanceActivity.putExtra("MAINTENANCEEMAIL", maintenanceEmailString);
                                                          editMaintenanceActivity.putExtra("MAINTENANCEPOSITION", String.valueOf(position));
                                                          editMaintenanceActivity.putExtra("MAINTENACEJSONOBJECT", jsonMCObject);
                                                          startActivity(editMaintenanceActivity);



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
    public void onBackPressed() {

        finish();

    }





    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }





}

package comapps.com.myassociationhoa;

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
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseInstallation;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/22/2016.
 */
public class Change_Add_Associations extends AppCompatActivity {

    private static final String TAG = "CHANGE_ADD_ASSOC";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    ListView listView;
    SharedPreferences sharedPreferencesVisited;

    private FloatingActionButton mFab;

    ParseInstallation installation;

    String[] associationsData;
    ArrayList<String> associationLongNameList;
    ArrayList<String> associationMemberTypeList;
    ArrayList<String> associationCodeForParseList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_change_add_assoc);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {


            bar.setTitle("Change/Add Associations");

        }

        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        associationsData = sharedPreferencesVisited.getString("ASSOCIATIONS_JOINED", "").split("\\|", -1);



        int i;

        associationLongNameList = new ArrayList<>();
        associationMemberTypeList = new ArrayList<>();
        associationCodeForParseList = new ArrayList<>();

        for ( i = 0; i < associationsData.length; i++) {


            String[] associationData = associationsData[i].split("\\^", -1);


            if ( associationData[1].substring(0,1).toLowerCase().equals("a")) {
                associationLongNameList.add(associationData[0] + " (a)");
            } else {
                associationLongNameList.add(associationData[0]);
            }

            associationMemberTypeList.add(associationData[1]);
            associationCodeForParseList.add(associationData[2]);


        }





        mFab = (FloatingActionButton) findViewById(R.id.fab);

        listView = (ListView) findViewById(R.id.listAssociations);

      /*  mFab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.show(true);
                mFab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                mFab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
            }
        }, 300);*/


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.textviewlist, associationLongNameList);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

               /* // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();
*/
                installation = ParseInstallation.getCurrentInstallation();

                installation.put("MemberType", associationMemberTypeList.get(position));
                installation.put("AssociationCode", associationCodeForParseList.get(position));
                installation.put("memberNumber", sharedPreferencesVisited.getString(installation.get("AssociationCode") + "MemberNumber", ""));
                Log.d(TAG, "member type ----> " + associationMemberTypeList.get(position));
                Log.d(TAG, "member number ----> " + associationCodeForParseList.get(position));
                Log.d(TAG, "member number ----> " + sharedPreferencesVisited.getString(installation.get("AssociationCode") + "MemberNumber", ""));

                try {
                    installation.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Intent mainActivity = new Intent();
                mainActivity.setClass(Change_Add_Associations.this, MainActivity.class);
                startActivity(mainActivity);
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

    public void AddChangeAssoc(View v) {

        installation = ParseInstallation.getCurrentInstallation();

        Intent addChangeActivity = new Intent();
        addChangeActivity.setClass(Change_Add_Associations.this, PopEnterPasscode.class);
        addChangeActivity.putExtra("FROMCHANGEADD", true);
        addChangeActivity.putExtra("OLDMEMBERNUMBER", installation.getString("memberNumber"));
        startActivity(addChangeActivity);






    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {

        this.finish();
    }





}


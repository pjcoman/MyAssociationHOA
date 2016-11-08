package comapps.com.myassociationhoa;

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
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/22/2016.
 */
@SuppressWarnings("ALL")
public class Change_Add_Associations extends AppCompatActivity {

    private static final String TAG = "CHANGE_ADD_ASSOC";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private ListView listView;
    private SharedPreferences sharedPreferencesVisited;


    private FloatingActionButton mFab;

    private ParseInstallation installation;

    private String[] associationsData;
    private ArrayList<String> associationLongNameList;
    private ArrayList<String> associationMemberTypeList;
    private ArrayList<String> associationCodeForParseList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setupWindowAnimations();


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
                associationLongNameList.add(associationData[0] + " (" + associationData[1].substring(0, 1) + ")");
            } else if ( associationData[1].substring(0,2).toLowerCase().equals("me")) {
                associationLongNameList.add(associationData[0] + " (" + associationData[1].substring(0, 1) + ")");
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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.textviewlist_transparent, associationLongNameList);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {



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

                ParseQuery<ParseObject> queryAssociations;
                queryAssociations = new ParseQuery<>(associationCodeForParseList.get(position));
                queryAssociations.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        ParseFile rosterFile = object.getParseFile("RosterFile");

                        String[] rosterFileArray;

                        byte[] rosterFileData = new byte[0];

                        try {
                            rosterFileData = rosterFile.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        String rosterFileString = null;

                        try {
                            rosterFileString = new String(rosterFileData, "UTF-8");
                        } catch (UnsupportedEncodingException e2) {
                            e2.printStackTrace();
                        }


                        Log.d(TAG, "rosterFileString is " + rosterFileString);

                        String memberNumber = sharedPreferencesVisited.getString(installation.get("AssociationCode") + "MemberNumber", "");
                        String memberType = associationMemberTypeList.get(position);

                        String numberAndType = memberNumber + "^" + memberType;


                        Log.d(TAG, "numberAndType is " + numberAndType);

                        String numberAndTypeToReplace[] = rosterFileString != null ? rosterFileString.split(memberNumber + "\\^") : new String[0];

                        Log.d(TAG, "numberAndTypeToReplace is " + numberAndTypeToReplace[1]);

                        String finalNumberAndTypeToReplace = memberNumber + "^" + numberAndTypeToReplace[1].substring(0, numberAndTypeToReplace[1].indexOf("^"));

                        Log.d(TAG, "finalNumberAndTypeToReplace is " + finalNumberAndTypeToReplace);

                        String rosterFileStringForUpdate = rosterFileString.replace(finalNumberAndTypeToReplace, numberAndType);

                        Log.d(TAG, "rosterFileForUpdate is " + rosterFileStringForUpdate);

                        byte[] data = rosterFileStringForUpdate.getBytes();
                        ParseFile RosterFile = new ParseFile("Roster.txt", data);


                        try {
                            RosterFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy, h:mm a");
                        final String formattedDate = df.format(c.getTime());



                        object.put("RosterFile", RosterFile);
                        object.put("RosterDate", formattedDate);

                        try {
                            object.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        Intent mainActivity = new Intent();
                        mainActivity.setClass(Change_Add_Associations.this, MainActivity.class);
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mainActivity.putExtra("FROMCHANGEADD", true);
                        startActivity(mainActivity);
                        finish();


                    }
                });




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


   /* @Override
    public void onBackPressed() {

        Intent mainActivity = new Intent();
        mainActivity.setClass(getApplicationContext(), MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivity);
        finish();


    }


*/


}


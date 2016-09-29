package comapps.com.myassociationhoa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.GetCallback;
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

import comapps.com.myassociationhoa.objects.MemberGroupObject;
import comapps.com.myassociationhoa.objects.RosterObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class AdminPushActivity extends AppCompatActivity {

    private static final String TAG = "PUSHACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";

    ParseQuery<ParseObject> query;
    ArrayList<String> uniqueGroupNames = new ArrayList<String>();
    ArrayList<String> uniqueGroupNamesWithCount = new ArrayList<String>();

    String allGroups = "";
    String pushFileString;


    ParseInstallation installation;




    TextView characterCount;
    EditText pushMessage;
    Button sendButton;
    Button groupsButton;

    RosterObject rosterObject;
    ArrayList<RosterObject> rosterObjects;
    MemberGroupObject memberGroupObject;
    ArrayList<MemberGroupObject> memberGroupObjects;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int rosterObjectSize;
    int i = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_push);


        pushMessage = (EditText) findViewById(R.id.editTextPushMessage);
        characterCount = (TextView) findViewById(R.id.textViewCount);
        sendButton = (Button) findViewById(R.id.buttonSendMessage);
        groupsButton = (Button) findViewById(R.id.buttonGroups);

        characterCount.setFocusable(false);
        sendButton.setEnabled(false);

        characterCount.setText("Message (190 characters remaining)");


        memberGroupObjects = new ArrayList<>();
        rosterObjects = new ArrayList<>();


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        rosterObjectSize = sharedPreferences.getInt("rosterSize", 0);

        for (int i = 0; i < rosterObjectSize; i++) {

            String jsonRosterObject = sharedPreferences.getString("rosterObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            rosterObject = gson.fromJson(jsonRosterObject, RosterObject.class);
            rosterObjects.add(rosterObject);


            if ( rosterObject.getGroups() != null || rosterObject.getGroups().length() != 0 )  {


                String[] splitGroups = rosterObject.getGroups().split(",", -1);


                for (String group: splitGroups) {

                    allGroups = allGroups + group + ".";

                }

                for (String group: splitGroups) {

                    Log.d(TAG, "rosterObject memberNumber and group ----> " + rosterObject.getMemberNumber() + " group is " + group);
                    memberGroupObject = new MemberGroupObject();
                    memberGroupObject.setMemberGroupObject_MemberNumber(rosterObject.getMemberNumber());
                    memberGroupObject.setMemberGroupObject_Group(group);
                    memberGroupObjects.add(memberGroupObject);



                    if ( !uniqueGroupNames.contains(group) && group.length() != 0 )  {

                        uniqueGroupNames.add(group);

                    }


                }



            }


        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {


        }


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width * 1, height * 1);

        Log.d(TAG, "uniqueGroupNames ----> " + uniqueGroupNames);
        Log.d(TAG, "allgroups ----> " + allGroups);


        for ( String name: uniqueGroupNames ) {

            int numberOfMembers = allGroups.split(name).length;

            Log.d(TAG, "numberOfMembers ----> " + numberOfMembers);

            String nameToAdd = name + "(" + (numberOfMembers - 1) + ")";
            uniqueGroupNamesWithCount.add(nameToAdd);


        }



        final int groupsSize = uniqueGroupNamesWithCount.size();

       if ( groupsSize == 0 ) {
           groupsButton.setEnabled(false);
       } else {
           groupsButton.setEnabled(true);
       }

        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( i != groupsSize ) {
                    groupsButton.setText(uniqueGroupNamesWithCount.get(i));
                    i++;
            } else {

                    groupsButton.setText("EVERYONE");
                    i = 0;

                }


            }
        });

        pushMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                characterCount.setText("Message (" + String.valueOf( 190 - count ) +  " characters remaining)");

            }

            @Override
            public void afterTextChanged(Editable s) {

                sendButton.setEnabled(true);

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



              /*  query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, ParseException e) {
*/





                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy H:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        SimpleDateFormat month = new SimpleDateFormat("M");
                        final String strDate = sdf.format(c.getTime());
                        String strDate2 = sdf2.format(c.getTime());
                        final String stringMonth = month.format(c.getTime());

                        installation = ParseInstallation.getCurrentInstallation();

                        query = new ParseQuery<ParseObject>(installation.getString("AssociationCode"));
                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {

                                if ( groupsButton.getText().toString().toLowerCase().equals("everyone")) {

                                    Log.d(TAG, "groupsButton text ----> " + groupsButton.getText().toString().toLowerCase());

                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));

                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery); // Set our Installation query

                                    push.setMessage("By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                            installation.getString("memberName") + "\n" + pushMessage.getText());

                                    push.sendInBackground();







                                } else {



                                    for ( MemberGroupObject memberGroupObject: memberGroupObjects) {



                                        if ( memberGroupObject.getMemberGroupObject_Group().toLowerCase().equals(groupsButton.getText().toString().toLowerCase())) {

                                            Log.d(TAG, " memberGroupObject.getMemberGroupObject_Group().toLowerCase() ----> " + memberGroupObject.getMemberGroupObject_Group().toLowerCase());
                                            Log.d(TAG, " buttonGroup to lowercase ----> " + groupsButton.getText().toString().toLowerCase());

                                            ParseQuery pushQuery = ParseInstallation.getQuery();
                                            pushQuery.whereEqualTo("memberNumber", memberGroupObject.getMemberGroupObject_MemberNumber());

                                            ParsePush push = new ParsePush();
                                            push.setQuery(pushQuery); // Set our Installation query

                                            push.setMessage("By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                                    installation.getString("memberName") + "\n" + pushMessage.getText());

                                            push.sendInBackground();



                                        }

                                    }

                                    ParseFile pushFile = object.getParseFile("PushFile");

                                    try {
                                        byte[] file = pushFile.getData();
                                        pushFileString = new String(file, "UTF-8");
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    }

                                    Log.d(TAG, "existing push notifications --->" + pushFileString);

                                    String pushFileUpdate = pushFileString + "|" + stringMonth + "^" + installation.getString("AssociationCode") + "^" + strDate
                                            + "^" + "By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                            installation.getString("memberName") + "\n" + pushMessage.getText();

                                    pushFileUpdate = pushFileUpdate.trim();

                                    byte[] pushData = pushFileUpdate.getBytes();
                                    pushFile = new ParseFile("Push.txt", pushData);

                                    object.put("PushFile", pushFile);
                                    try {
                                        object.save();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                        object.saveEventually();
                                    }



                                    AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                                    remoteDataTaskClass.execute();



                                }

                            }
                        });






                        Toast.makeText(getBaseContext(), "Push sent.", Toast.LENGTH_LONG).show();



                        finish();


                    }
                });

            }
     /*   });


    }*/


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


    @Override
    public void onBackPressed() {

       /* Intent intentMain = new Intent();
        intentMain.setClass(AdminPushActivity.this, MainActivity.class);
        AdminPushActivity.this.finish();
        startActivity(intentMain);
*/

        finish();


    }


}



package comapps.com.myassociationhoa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

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


            if ( rosterObject.getGroups() != null ) {


                String[] splitGroups = rosterObject.getGroups().split(",", -1);

                for (String group: splitGroups) {

                    Log.d(TAG, "rosterObject memberNumber and group ----> " + rosterObject.getMemberNumber() + " group is " + group);
                    memberGroupObject = new MemberGroupObject();
                    memberGroupObject.setMemberGroupObject_MemberNumber(rosterObject.getMemberNumber());
                    memberGroupObject.setMemberGroupObject_Group(group);
                    memberGroupObjects.add(memberGroupObject);


                  if ( !uniqueGroupNames.contains(group))  {

                      uniqueGroupNames.add(group);

                  }



                    Log.d(TAG, "memberNumbers ----> " + memberGroupObject.toString());


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



        Log.d(TAG, "unique member groups ----> " + uniqueGroupNames);

        final int groupsSize = uniqueGroupNames.size();

       if ( groupsSize == 0 ) {
           groupsButton.setEnabled(false);
       } else {
           groupsButton.setEnabled(true);
       }

        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( i != groupsSize ) {
                    groupsButton.setText(uniqueGroupNames.get(i));
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
                        String strDate = sdf.format(c.getTime());
                        String strDate2 = sdf2.format(c.getTime());

                        installation = ParseInstallation.getCurrentInstallation();

                        if ( groupsButton.getText().toString().toLowerCase().equals("everyone")) {

                            Log.d(TAG, "groupsButton text ----> " + groupsButton.getText().toString().toLowerCase());

                            ParseQuery pushQuery = ParseInstallation.getQuery();
                            pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));

                            ParsePush push = new ParsePush();
                            push.setQuery(pushQuery); // Set our Installation query

                            push.setMessage("By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                    installation.getString("memberName") + "\n" + pushMessage.getText());

                            try {
                                push.send();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


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
                                        installation.getString("memberName") + " " + pushMessage.getText());

                                try {
                                    push.send();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                        }



                        }




                        Toast.makeText(getBaseContext(), "Push sent.", Toast.LENGTH_LONG).show();


                        Intent mainActivity = new Intent();
                        mainActivity.setClass(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
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

        Intent intentMain = new Intent();
        intentMain.setClass(AdminPushActivity.this, MainActivity.class);
        AdminPushActivity.this.finish();
        startActivity(intentMain);

    }


}



package comapps.com.myassociationhoa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import java.util.List;

import comapps.com.myassociationhoa.objects.MemberGroupObject;
import comapps.com.myassociationhoa.objects.RosterObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class AdminPushActivity extends AppCompatActivity {

    private static final String TAG = "PUSHACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    private ParseQuery<ParseObject> query;
    private final ArrayList<String> uniqueGroupNames = new ArrayList<>();
    private final ArrayList<String> uniqueGroupNamesWithCount = new ArrayList<>();

    private String allGroups = "";
    private String pushFileString;


    private ParseInstallation installation;


    private String messageType;
    private TextView emailOrPush;
    private TextView characterCount;
    private EditText pushMessage;
    private Button sendButton;
    private Button groupsButton;

    private RosterObject rosterObject;
    private ArrayList<RosterObject> rosterObjects;
    private MemberGroupObject memberGroupObject;
    private ArrayList<MemberGroupObject> memberGroupObjects;



    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int rosterObjectSize;
    private int i = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setupWindowAnimations();

        setContentView(R.layout.pop_up_layout_push);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {


            bar.setTitle("Admin Push");

        }

        emailOrPush = (TextView) findViewById(R.id.textViewSendOptions);
        messageType = "PUSH";
        pushMessage = (EditText) findViewById(R.id.editTextPushMessage);
        characterCount = (TextView) findViewById(R.id.textViewCount);
        sendButton = (Button) findViewById(R.id.buttonSendMessage);
        groupsButton = (Button) findViewById(R.id.buttonGroups);

        characterCount.setFocusable(false);
        sendButton.setEnabled(false);
        emailOrPush.setEnabled(true);

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


            if (rosterObject.getGroups() != null || rosterObject.getGroups().length() != 0) {


                String[] splitGroups = rosterObject.getGroups().split(",");


                for (String group : splitGroups) {

                    group = group.trim();

                    if (group.length() > 0) {

                        allGroups = allGroups + group + ".";

                        Log.d(TAG, "rosterObject memberNumber and group ----> " + rosterObject.getMemberNumber() + " group is " + group);
                        memberGroupObject = new MemberGroupObject();
                        memberGroupObject.setMemberGroupObject_MemberNumber(rosterObject.getMemberNumber());
                        memberGroupObject.setMemberGroupObject_Group(group);
                        memberGroupObjects.add(memberGroupObject);

                    }

                    if (!uniqueGroupNames.contains(group) && group.length() != 0) {

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

        getWindow().setLayout(width, height);

        Log.d(TAG, "uniqueGroupNames ----> " + uniqueGroupNames);
        Log.d(TAG, "allgroups ----> " + allGroups);


        for (String name : uniqueGroupNames) {

            int numberOfMembers = allGroups.split(name).length - 1;

            Log.d(TAG, "numberOfMembers ----> " + numberOfMembers);

            String nameToAdd = name + " - " + numberOfMembers;
            uniqueGroupNamesWithCount.add(nameToAdd);


        }


        final int groupsSize = uniqueGroupNamesWithCount.size();

        if (groupsSize == 0) {
            groupsButton.setEnabled(false);
        } else {
            groupsButton.setEnabled(true);
        }

        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i != groupsSize) {
                    groupsButton.setText(uniqueGroupNamesWithCount.get(i));
                    i++;
                } else {

                    groupsButton.setText("EVERYONE");
                    i = 0;

                }


            }
        });

        emailOrPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailOrPush.getText().subSequence(0, 4).equals("Push")) {
                    emailOrPush.setText("Email The Message");
                    characterCount.setVisibility(View.INVISIBLE);
                    messageType = "EMAIL";
                } else {
                    emailOrPush.setText("Push the Message");
                    characterCount.setVisibility(View.VISIBLE);
                    messageType = "PUSH";
                }
            }
        });

        pushMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                characterCount.setText("Message (" + String.valueOf(190 - count) + " characters remaining)");

            }

            @Override
            public void afterTextChanged(Editable s) {

                sendButton.setEnabled(true);

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (messageType.equals("PUSH")) {


                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy h:mm a", java.util.Locale.getDefault());
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d", java.util.Locale.getDefault());
                    SimpleDateFormat month = new SimpleDateFormat("M", java.util.Locale.getDefault());
                    final String strDate = sdf.format(c.getTime());
                    String strDate2 = sdf2.format(c.getTime());
                    final String stringMonth = month.format(c.getTime());

                    installation = ParseInstallation.getCurrentInstallation();

                    query = new ParseQuery<>(installation.getString("AssociationCode"));
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {

                            if (groupsButton.getText().toString().toLowerCase().equals("everyone")) {

                                Log.d(TAG, "groupsButton text ----> " + groupsButton.getText().toString().toLowerCase());

                                ParseQuery pushQuery = ParseInstallation.getQuery();
                                pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));

                                ParsePush push = new ParsePush();
                                push.setQuery(pushQuery); // Set our Installation query

                                push.setMessage("By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                        installation.getString("memberName") + "\n" + pushMessage.getText());

                                push.sendInBackground();


                            } else {


                                for (MemberGroupObject memberGroupObject : memberGroupObjects) {

                                    Log.d(TAG, "groups button lowercase ----> " + groupsButton.getText().toString().toLowerCase());
                                    Log.d(TAG, "member group lowercase ----> " + memberGroupObject.getMemberGroupObject_Group().toLowerCase());

                                    String channelPreClean = groupsButton.getText().toString().toLowerCase();
                                    int dashPosition = channelPreClean.indexOf("-");
                                    String channel = channelPreClean.substring(0, dashPosition - 1);

                                    if (memberGroupObject.getMemberGroupObject_Group().toLowerCase().equals(channel)) {

                                        Log.d(TAG, " memberGroupObject.getMemberGroupObject_Group().toLowerCase() ----> " + memberGroupObject.getMemberGroupObject_Group().toLowerCase());
                                        Log.d(TAG, " buttonGroup to lowercase ----> " + channel);
                                        Log.d(TAG, " group member memberNumber ----> " + memberGroupObject.getMemberGroupObject_MemberNumber());

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
                                } catch (ParseException | UnsupportedEncodingException e1) {
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


                } else {

                    installation = ParseInstallation.getCurrentInstallation();



                    List<String> addressesList = new ArrayList<String>();




                    for (int i = 0; i < sharedPreferences.getInt("rosterSize", 0); i++) {

                        String jsonRosterObject = sharedPreferences.getString("rosterObject" + "[" + i + "]", "");
                        Gson gson = new Gson();
                        RosterObject rosterObject = gson.fromJson(jsonRosterObject, RosterObject.class);


                        rosterObject.getGroups();

                        if ( rosterObject.getGroups().toLowerCase().contains(groupsButton.getText().toString().toLowerCase()) ||
                                groupsButton.getText().toString().toLowerCase().equals("everyone")) {

                            addressesList.add(rosterObject.getEmail());

                        }



                    }

                    String[] addresses = new String[addressesList.size()];
                    addressesList.toArray(addresses);


                    Intent intentSendEmail = new Intent(android.content.Intent.ACTION_SEND);
                    intentSendEmail.setType("text/plain");


                    intentSendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, addresses);

                    if ( groupsButton.getText().toString().toLowerCase().equals("everyone")) {

                        intentSendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                sharedPreferences.getString("defaultRecord(1)","") + " member");


                    } else {

                        String[] groupName = groupsButton.getText().toString().split(" - ");

                        intentSendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                sharedPreferences.getString("defaultRecord(1)","") + " " + groupName[0] + " group member");

                    }

                    intentSendEmail.putExtra(Intent.EXTRA_TEXT, pushMessage.getText());

                    startActivityForResult((Intent.createChooser(intentSendEmail, "Email")), 1);




                }
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

   /* public void changeTitleText(View v)
    {
        TextView emailOrPush = (TextView) findViewById(R.id.textViewSendOptions);

        if ( emailOrPush.getText().subSequence(0,3).equals("Push")) {
            emailOrPush.setText("Email The Messge");
            characterCount.setVisibility(View.GONE);
        } else {
            emailOrPush.setText("Push the Message");
            characterCount.setVisibility(View.VISIBLE);
        }
    }*/



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




}



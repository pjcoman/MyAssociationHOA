package comapps.com.myassociationhoa.messageboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import comapps.com.myassociationhoa.MainActivity;
import comapps.com.myassociationhoa.OnEventListener;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.RemoteDataTaskClassMB;
import comapps.com.myassociationhoa.RemoteDataTaskClassMBCallBack;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
@SuppressWarnings("ALL")
public class PopMBAddMessage extends AppCompatActivity {

    private static final String TAG = "POPMB";
    private static final String MYPREFERENCES = "MyPrefs";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private ParseQuery<ParseObject> query;
    private String[] messageFileArray;
    private String messageFileString = "";
    private String messageFileUpdate = "";
    private String adminMessageFileString = "";
    private String adminMessageFileUpdate = "";

    private String memberEmail;

    private String pushFileString;
    private String pushMessageString;

    private EditText newMessage;
    private Button saveButton;

    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorVisited;

    RemoteDataTaskClass rdtc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_mb_newmessage);


        newMessage = (EditText) findViewById(R.id.editTextMessage);
        saveButton = (Button) findViewById(R.id.buttonSaveMessage);

        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width, height);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                query = new ParseQuery<>(installation.getString("AssociationCode"));

                Calendar c = Calendar.getInstance();
                final SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a");
                final SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                final SimpleDateFormat month = new SimpleDateFormat("M");
                final String strDate = sdf.format(c.getTime());
                final String strDate2 = sdf2.format(c.getTime());
                final String strMonth = month.format(c.getTime());

                memberEmail = sharedPreferencesVisited.getString("SUMMER_EMAIL", "");

                if (memberEmail.length() == 0) {

                    memberEmail = "email not available";

                }

//*************************************************************************************************************************************************************************

                if (sharedPreferences.getString("defaultRecord(48)", "Yes").equals("Yes") && installation.getString("MemberType").equals("Member")) {

                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> assoc, ParseException e) {


                            ParseFile adminMessageFile = assoc.get(0).getParseFile("AdminMessageFile");


                            try {
                                byte[] file = adminMessageFile.getData();
                                try {
                                    adminMessageFileString = new String(file, "UTF-8");

                                    Log.d(TAG, "existing admin messages --->" + messageFileString);

                                } catch (UnsupportedEncodingException e1) {
                                    e1.printStackTrace();
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }


                            String adminMessageToAdd = installation.getString("memberName").trim() +
                                    "|" + strDate +
                                    "|" + newMessage.getText().toString().trim() +
                                    "|" + strDate2 +
                                    "|" + sharedPreferencesVisited.getString("SUMMER_EMAIL", "").trim() +
                                    "|" + "0" +
                                    "|" +
                                    "|";


                            adminMessageFileUpdate = adminMessageToAdd + "|" + adminMessageFileString;
                            Log.d(TAG, "adminMessageFileUpdate ---->" + adminMessageFileUpdate);

                            if (adminMessageFileUpdate.substring(0, 1).equals("|")) {
                                adminMessageFileUpdate = adminMessageFileUpdate.substring(1);
                            }

                            if (adminMessageFileUpdate.substring(adminMessageFileUpdate.length() - 1).equals("|")) {
                                adminMessageFileUpdate = adminMessageFileUpdate.substring(0, adminMessageFileUpdate.length() - 1);
                            }

                            Log.d(TAG, "adminMessageFileUpdate for upload ---->" + adminMessageFileUpdate);

                            byte[] data = adminMessageFileUpdate.getBytes();
                            ParseFile AdminMessageFile = new ParseFile("AdminMessage.txt", data);


                            try {
                                AdminMessageFile.save();

                                AsyncTask<Void, Void, Void> remoteDataTaskClassMB = new RemoteDataTaskClassMB(getApplicationContext());
                                remoteDataTaskClassMB.execute();

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }


                            assoc.get(0).put("AdminMessageDate", strDate);
                            assoc.get(0).put("AdminMessageFile", AdminMessageFile);

                            try {
                                assoc.get(0).save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                            Toast toast = Toast.makeText(getBaseContext(), "Your message has been sent to the\nAssociation Admi" +
                                    "nistrator for review\nbefore posting", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            pushMessageString = installation.getString("memberName") +
                                    " has posted a new message comment for review for the Message Board";

                            if (sharedPreferencesVisited.getBoolean("PARSESERVER", false)) {


                                HashMap<String, Object> params = new HashMap<String, Object>();
                                params.put("AssociationCode", installation.getString("AssociationCode"));
                                params.put("MemberType", "Administrator");
                                params.put("Channel", "");
                                params.put("Message", pushMessageString);
                                ParseCloud.callFunctionInBackground("SendPush", params, new FunctionCallback<Object>() {
                                    @Override
                                    public void done(Object object, ParseException e) {
                                        if (e == null) {

                                            Toast.makeText(getBaseContext(), "Push sent.", Toast.LENGTH_LONG).show();
                                            finish();
                                        }

                                    }


                                });

                            } else {

                                ParseQuery pushQuery = ParseInstallation.getQuery();
                                pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));
                                pushQuery.whereEqualTo("MemberType", "Administrator");

                                ParsePush push = new ParsePush();
                                push.setQuery(pushQuery); // Set our Installation query

                                push.setMessage(pushMessageString);

                                push.sendInBackground();

                            }




                            ParseFile pushFile = assoc.get(0).getParseFile("PushFile");

                            try {
                                byte[] file = pushFile.getData();
                                pushFileString = new String(file, "UTF-8");
                            } catch (ParseException | UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }

                            Log.d(TAG, "existing push notifications --->" + pushFileString);

                            String pushFileUpdate = pushFileString + "|" + strMonth + "^" + installation.getString("AssociationCode") + "^" + strDate
                                    + "^" + installation.getString("memberName") +
                                    " has posted a new message comment for review for the Message Board";

                            pushFileUpdate = pushFileUpdate.trim();

                            byte[] pushData = pushFileUpdate.getBytes();
                            pushFile = new ParseFile("Push.txt", pushData);

                            assoc.get(0).put("PushFile", pushFile);
                            try {
                                assoc.get(0).save();

                                AsyncTask<Void, Void, Void> remoteDataTaskClassMB = new RemoteDataTaskClassMB(getApplicationContext());
                                remoteDataTaskClassMB.execute();

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                                assoc.get(0).saveEventually();
                            }



                            Intent mainActivity = new Intent();
                            mainActivity.setClass(getApplicationContext(), MainActivity.class);
                            startActivity(mainActivity);
                            finish();

                        }
                    });


//*************************************************************************************************************************************************************************
                } else {
//*************************************************************************************************************************************************************************

                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> assoc, ParseException e) {


                            ParseFile messageFile = assoc.get(0).getParseFile("MessageFile");
                            messageFileArray = null;

                            try {
                                byte[] file = messageFile.getData();
                                try {
                                    messageFileString = new String(file, "UTF-8");

                                    Log.d(TAG, "existing messages --->" + messageFileString);

                                } catch (UnsupportedEncodingException e1) {
                                    e1.printStackTrace();
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }


                            messageFileUpdate = installation.getString("memberName").trim() + "|" +
                                    strDate.trim() + "|" + newMessage.getText().toString().trim() + "|" + strDate2.trim() + "|" + memberEmail.trim() + "|" + messageFileString;

                            Log.d(TAG, "updated messages ----> " + messageFileUpdate);

                            byte[] data = messageFileUpdate.getBytes();
                            ParseFile MessageFile = new ParseFile("message.txt", data);


                            try {
                                MessageFile.save();

                                AsyncTask<Void, Void, Void> remoteDataTaskClassMB = new RemoteDataTaskClassMB(getApplicationContext());
                                remoteDataTaskClassMB.execute();

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }


                            assoc.get(0).put("MessageDate", strDate);
                            assoc.get(0).put("MessageFile", MessageFile);

                            try {
                                assoc.get(0).save();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                                assoc.get(0).saveEventually();
                            }



                            Toast toast = Toast.makeText(getBaseContext(), "MESSAGE POSTED", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();


                            ParseQuery pushQuery = ParseInstallation.getQuery();
                            pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));

                            if (sharedPreferencesVisited.getBoolean("PARSESERVER", false)) {

                                pushMessageString = "By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                        installation.getString("memberName") +
                                        " has posted a new message to the Message Board.";

                                HashMap<String, Object> params = new HashMap<String, Object>();
                                params.put("AssociationCode", installation.getString("AssociationCode"));
                                params.put("MemberType", "");
                                params.put("Channel", "Everyone");
                                params.put("Message", pushMessageString);
                                ParseCloud.callFunctionInBackground("SendPush", params, new FunctionCallback<Object>() {
                                    @Override
                                    public void done(Object object, ParseException e) {
                                        if (e == null) {

                                            Toast.makeText(getBaseContext(), "Push sent.", Toast.LENGTH_LONG).show();
                                            finish();
                                        }

                                    }


                                });

                            } else {

                                ParsePush push = new ParsePush();
                                push.setQuery(pushQuery); // Set our Installation query

                                push.setMessage("By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                        installation.getString("memberName") +
                                        " has posted a new message to the Message Board.");

                                push.sendInBackground();

                            }



                            ParseFile pushFile = assoc.get(0).getParseFile("PushFile");

                            try {
                                byte[] file = pushFile.getData();
                                pushFileString = new String(file, "UTF-8");
                            } catch (ParseException | UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }

                            Log.d(TAG, "existing push notifications --->" + pushFileString);

                            String pushFileUpdate = pushFileString + "|" + strMonth + "^" + installation.getString("AssociationCode") + "^" + strDate
                                    + "^" + installation.getString("memberName") +
                                    " has posted a new message comment for review for the Message Board";

                            pushFileUpdate = pushFileUpdate.trim();

                            byte[] pushData = pushFileUpdate.getBytes();
                            pushFile = new ParseFile("Push.txt", pushData);

                            assoc.get(0).put("PushFile", pushFile);
                            try {
                                assoc.get(0).save();
                                RemoteDataTaskClassMBCallBack remoteDataTaskClassMBCallBack = new RemoteDataTaskClassMBCallBack(getApplicationContext(), new
                                        OnEventListener<String>() {
                                            @Override
                                            public void onSuccess() {


                                                Intent mainActivity = new Intent();
                                                mainActivity.setClass(getApplicationContext(), MainActivity.class);
                                                startActivity(mainActivity);
                                                finish();


                                            }

                                            @Override
                                            public void onFailure(Exception e) {


                                            }
                                        });

                                remoteDataTaskClassMBCallBack.execute();

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                                assoc.get(0).saveEventually();
                            }





                        }
                    });
//*************************************************************************************************************************************************************************

                }
            }
        });
    }



    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {

       finish();

    }


}



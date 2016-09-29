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

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import comapps.com.myassociationhoa.MainActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.objects.MBObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopMBAddMessage extends AppCompatActivity {

    private static final String TAG = "POPMB";
    public static final String MYPREFERENCES = "MyPrefs";
    public static final String VISITEDPREFERENCES = "VisitedPrefs";

    ParseQuery<ParseObject> query;
    String[] messageFileArray;
    String messageFileString = "";
    String messageFileUpdate = "";

    String pushFileString;

    EditText newMessage;
    Button saveButton;

    SharedPreferences sharedVisitedPreferences;
    SharedPreferences sharedPreferences;
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

        setContentView(R.layout.pop_up_layout_mb);


        newMessage = (EditText) findViewById(R.id.editTextMessage);
        saveButton = (Button) findViewById(R.id.buttonSaveMessage);

        sharedVisitedPreferences = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width * 1, height * 1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                query = new ParseQuery<ParseObject>(installation.getString("AssociationCode"));

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



                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, H:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        SimpleDateFormat month = new SimpleDateFormat("M");
                        String strDate = sdf.format(c.getTime());
                        String strDate2 = sdf2.format(c.getTime());
                        String strMonth = month.format(c.getTime());

                        String memberEmail = sharedVisitedPreferences.getString("SUMMER_EMAIL","");

                        if ( memberEmail.length() == 0 ) {

                            memberEmail = "email not available";

                        }




                        messageFileUpdate = messageFileString + "|" + installation.getString("memberName") + "|" + "Posted: " +
                                strDate + "|" + newMessage.getText() + "|" + strDate2 + "|" + memberEmail;

                        messageFileUpdate = installation.getString("memberName") + "|" +
                                strDate + "|" + newMessage.getText() + "|" + strDate2 + "|" + memberEmail + "|" + messageFileString;

                        MBObject mbObject = new MBObject();
                        mbObject.setMbName(installation.getString("memberName"));
                        mbObject.setMbPostDate(strDate);
                        mbObject.setMbPost(String.valueOf(newMessage.getText()));
                        mbObject.setMbPostDate2(strDate2);
                        mbObject.setMbPosterEmailAddress(memberEmail);

                        if (sharedPreferences.getString("defaultRecord(48)", "Yes").equals("Yes") && installation.getString("MemberType").equals("Member")) {

                            String adminMessageFileString = "";

                            ParseFile adminMessageFile = assoc.get(0).getParseFile("AdminMessageFile");
                            messageFileArray = null;

                            try {
                                byte[] file = adminMessageFile.getData();
                                try {
                                    adminMessageFileString = new String(file, "UTF-8");

                                    Log.d(TAG, "existing admin messages --->" + adminMessageFileString);

                                } catch (UnsupportedEncodingException e1) {
                                    e1.printStackTrace();
                                }
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }



                            String adminMessageToAdd = "|" + installation.getString("memberName") + "|"
                                    + strDate + "|"
                                    + newMessage.getText() + "|"
                                    + strDate2  + "|"
                                    + sharedVisitedPreferences.getString("SUMMER_EMAIL","") + "|"
                                    + "0" + "|"
                                    + sharedVisitedPreferences.getString("FULL_NAME","") + "|"
                                    + " |" ;




                            String adminMessageFileUpdate = adminMessageFileString + adminMessageToAdd.trim();
                            Log.d(TAG, "adminMessageFileUpdate ---->" + adminMessageFileUpdate);

                            if ( adminMessageFileUpdate.substring(adminMessageFileUpdate.length() - 1).equals("|")) {
                                adminMessageFileUpdate = adminMessageFileUpdate.substring(0, adminMessageFileUpdate.length() - 1);
                            }

                            Log.d(TAG, "adminMessageFileUpdate after | removed from end ---->" + adminMessageFileUpdate);

                            byte[] data = adminMessageFileUpdate.getBytes();
                            ParseFile AdminMessageFile = new ParseFile("AdminMessage.txt", data);


                            try {
                                AdminMessageFile.save();
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

                            Toast toast = Toast.makeText(getBaseContext(),"Your message has been sent to the\nAssociation Admi" +
                                    "nistrator for review\nbefore posting", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();



                            ParseQuery pushQuery = ParseInstallation.getQuery();
                            pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));
                            pushQuery.whereEqualTo("MemberType", "Administrator");

                            ParsePush push = new ParsePush();
                            push.setQuery(pushQuery); // Set our Installation query

                            push.setMessage(installation.getString("memberName") +
                                    " has posted a new message comment for review for the Message Board");

                            push.sendInBackground();

                            ParseFile pushFile = assoc.get(0).getParseFile("PushFile");

                            try {
                                byte[] file = pushFile.getData();
                                pushFileString = new String(file, "UTF-8");
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            } catch (UnsupportedEncodingException e1) {
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
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                                assoc.get(0).saveEventually();
                            }


                            AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                            remoteDataTaskClass.execute();


                            Intent mainActivity = new Intent();
                            mainActivity.setClass(getApplicationContext(), MainActivity.class);
                            startActivity(mainActivity);
                            finish();







                        } else {



                        Integer mbSizeInt = sharedVisitedPreferences.getInt("mbSize", 0);

                        editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String jsonMbObject = gson.toJson(mbObject); // myObject - instance of MyObject
                        editor.putString("mbObject" + "[" + String.valueOf(mbSizeInt + 1) + "]", jsonMbObject);
                    //    editor.putInt("mbSize", mbSizeInt + 1);
                        editor.apply();





                        byte[] data = messageFileUpdate.getBytes();
                        ParseFile MessageFile = new ParseFile("message.txt", data);


                        try {
                            MessageFile.save();
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

                            Toast toast = Toast.makeText(getBaseContext(),"MESSAGE POSTED", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();



                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));

                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery); // Set our Installation query

                        push.setMessage("By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                installation.getString("memberName") +
                                " has posted a new message to the Message Board.");

                        push.sendInBackground();

                            ParseFile pushFile = assoc.get(0).getParseFile("PushFile");

                            try {
                                byte[] file = pushFile.getData();
                                pushFileString = new String(file, "UTF-8");
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            } catch (UnsupportedEncodingException e1) {
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
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                                assoc.get(0).saveEventually();
                            }



                            AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getApplicationContext());
                            remoteDataTaskClass.execute();

                            Intent mainActivity = new Intent();
                            mainActivity.setClass(getApplicationContext(), MainActivity.class);
                            startActivity(mainActivity);
                            finish();




                        }


                    }
                });

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



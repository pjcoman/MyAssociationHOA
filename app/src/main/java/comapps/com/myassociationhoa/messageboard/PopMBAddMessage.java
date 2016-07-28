package comapps.com.myassociationhoa.messageboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
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

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.MBObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopMBAddMessage extends AppCompatActivity {

    private static final String TAG = "POPMB";
    public static final String MYPREFERENCES = "MyPrefs";

    ParseQuery<ParseObject> query;
    String[] messageFileArray;
    String messageFileString = "";
    String messageFileUpdate = "";

    EditText newMessage;
    Button saveButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


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

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 1), (int) (height * 1));

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

                        String memberInfo = sharedPreferences.getString("MEMBER_INFO", "");
                        String[] memberInfoArray = memberInfo.split("\\^");

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, M d,yyyy H:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        String strDate = sdf.format(c.getTime());
                        String strDate2 = sdf2.format(c.getTime());

                        String memberEmail;

                        if ( memberInfoArray[11].length() == 0 || memberInfoArray[11] == null) {
                            memberEmail = "email not available";
                        } else {
                            memberEmail = memberInfoArray[11];
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
                        mbObject.setMbPosterEmailAddress(memberInfoArray[11]);

                        Integer mbSizeInt = sharedPreferences.getInt("mbSize", 0);

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
                        }

                        Toast.makeText(getBaseContext(), "MESSAGE POSTED", Toast.LENGTH_LONG).show();

                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereEqualTo("AssociationCode", "Android");

                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery); // Set our Installation query

                        push.setMessage("By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                installation.getString("memberName") +
                                " has posted a new message to the Message Board.");

                        push.sendInBackground();


                        Intent mbActivity = new Intent();
                        mbActivity.setClass(getApplicationContext(), MBActivity.class);
                        startActivity(mbActivity);
                        finish();


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

        Intent intentMain = new Intent();
        intentMain.setClass(PopMBAddMessage.this, MBActivity.class);
        PopMBAddMessage.this.finish();
        startActivity(intentMain);

    }


}



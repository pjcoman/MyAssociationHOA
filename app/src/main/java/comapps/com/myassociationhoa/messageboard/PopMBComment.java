package comapps.com.myassociationhoa.messageboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import comapps.com.myassociationhoa.MainActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.objects.MBObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopMBComment extends AppCompatActivity {

    private static final String TAG = "POPMBCOMMENT";
    public static final String MYPREFERENCES = "MyPrefs";

    ParseQuery<ParseObject> query;
    String[] messageFileArray;
    String messageFileString;
    String messageFileUpdate = "";

    String pushFileString;

    MBObject mbObject;

    EditText newComment;
    Button addButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int position;

    MainActivity mainActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_mb_comment);


        newComment = (EditText) findViewById(R.id.editTextComment);
        addButton = (Button) findViewById(R.id.buttonAddComment);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("position");
            String jsonMbObject = extras.getString("jsonMbObject");
            Log.d(TAG, "position ---> " + position);

            Gson gson = new Gson();
            mbObject = gson.fromJson(jsonMbObject, MBObject.class);
            mbObject.toString();

        }


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width * 1, height * 1);

        addButton.setOnClickListener(new View.OnClickListener() {
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





                        String[] messageStringArray = messageFileString.split("\\|");

                        int i = 0;

                        for (String postItems : messageStringArray) {


                            Log.i(TAG, "messageStringArray member ---->" + postItems + "   array index = " + i);

                            i++;
                        }

                        Log.d(TAG, "post item to add comment to ---->  " + messageStringArray[(position * 5) + 2]);
                        messageStringArray[(position * 5) + 2] = messageStringArray[(position * 5) + 2] + "\n\nComment by: " +
                                installation.getString("memberName") + "\n" + newComment.getText();
                        Log.d(TAG, "post comment item ---->  " + messageStringArray[(position * 5) + 2]);

                        MBObject mbObjectUpdated;
                        String postUpdate = mbObject.getMbPost() + "\n\nComment by: " +
                                installation.getString("memberName") + "\n" + newComment.getText();
                        mbObjectUpdated = mbObject;
                        mbObjectUpdated.setMbPost(postUpdate);

                        editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String jsonMbObject = gson.toJson(mbObjectUpdated); // myObject - instance of MyObject
                        editor.putString("mbObject" + "[" + position + "]", jsonMbObject);
                        //    editor.putInt("mbSize", mbSizeInt + 1);
                        editor.apply();





                        for (String postItems : messageStringArray) {

                            messageFileUpdate = messageFileUpdate + postItems + "|";


                        }

                        Log.i(TAG, "messageFileUpdate ---->" + messageFileUpdate + "<----");

                        byte[] data = messageFileUpdate.getBytes();
                        ParseFile MessageFile = new ParseFile("message.txt", data);

                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d H:mma");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        SimpleDateFormat month = new SimpleDateFormat("M");
                        String strDate = sdf.format(c.getTime());
                        String strDate2 = sdf2.format(c.getTime());
                        String strMonth = month.format(c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy, hh:mm a");

                        String formattedDate = df.format(c.getTime());
                        String formattedMonth = month.format(c.getTime());




                        try {
                            MessageFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("MessageFile", MessageFile);
                        assoc.get(0).put("MessageDate", formattedDate);

                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            assoc.get(0).saveEventually();
                        }





                        Toast.makeText(getBaseContext(), "MESSAGE POSTED", Toast.LENGTH_LONG).show();

                        ParseQuery pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));

                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery); // Set our Installation query

                        push.setMessage("By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                installation.getString("memberName") +
                                " has commented on a message on the Message Board.");

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
                                " has commented on a message on the Message Board";

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

        finish();

    }


}



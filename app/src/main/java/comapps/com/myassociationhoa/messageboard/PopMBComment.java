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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import comapps.com.myassociationhoa.MainActivity;
import comapps.com.myassociationhoa.R;
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

    EditText newComment;
    Button addButton;

    SharedPreferences sharedPreferences;

    int messageIndex;


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
            messageIndex = extras.getInt("messageindex");
            Log.d(TAG, "message index ---> " + messageIndex);
        }


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 1), (int) (height * 1));

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


                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy H:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        String strDate = sdf.format(c.getTime());
                        String strDate2 = sdf2.format(c.getTime());

                        String[] messageStringArray = messageFileString.split("\\|");

                        int i = 0;

                        for (String postItems : messageStringArray) {


                            Log.i(TAG, "messageStringArray member ---->" + postItems + "   array index = " + i);

                            i++;
                        }

                        Log.d(TAG, "post item to add comment to ---->  " + messageStringArray[(messageIndex * 5) + 2]);
                        messageStringArray[(messageIndex * 5) + 2] = messageStringArray[(messageIndex * 5) + 2] + "\n\nComment by: " +
                                installation.getString("memberName") + "\n" + newComment.getText();
                        Log.d(TAG, "post comment item ---->  " + messageStringArray[(messageIndex * 5) + 2]);


                        for (String postItems : messageStringArray) {

                            messageFileUpdate = messageFileUpdate + postItems + "|";


                        }

                        Log.i(TAG, "messageFileUpdate ---->" + messageFileUpdate + "<----");

                        byte[] data = messageFileUpdate.getBytes();
                        ParseFile MessageFile = new ParseFile("message.txt", data);


                        try {
                            MessageFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("MessageFile", MessageFile);
                        try {
                            assoc.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        Toast.makeText(getBaseContext(), "MESSAGE POSTED", Toast.LENGTH_LONG).show();


                        Intent mainActivity = new Intent();
                        mainActivity.setClass(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);
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
        intentMain.setClass(PopMBComment.this, MBActivity.class);
        PopMBComment.this.finish();
        startActivity(intentMain);

    }


}



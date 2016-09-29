package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.AdminMBObject;

/**
 * Created by me on 6/28/2016.
 */
class AdminMBAdapter extends ArrayAdapter<AdminMBObject> {

    public static final String TAG = "ADMINMESSAGEBOARDADAPTER";
    private static final String MYPREFERENCES = "MyPrefs";

    TextView mbName;
    TextView mbDate;
    TextView mbPost;

    Button sendEmail;
    Button addMessage;

    String outputDate;

    ParseQuery query;
    String messageFileString;
    String messageFileUpdate;


    SharedPreferences sharedPreferences;

    public AdminMBAdapter(Context context, ArrayList<AdminMBObject> posts) {
        super(context, 0, posts);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final AdminMBObject mbObject = getItem(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main_admin_mb_row, parent, false);

        }

        mbName = (TextView) convertView.findViewById(R.id.textViewPostName);
        mbDate = (TextView) convertView.findViewById(R.id.textViewPostDate);
        mbPost = (TextView) convertView.findViewById(R.id.textViewPost);


        sendEmail = (Button) convertView.findViewById(R.id.sendEmail);
        addMessage = (Button) convertView.findViewById(R.id.addMessageButton);


        mbName.setText(mbObject.getField1());

        String inputFormat = "M/d/yy, H:mm a";
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);

        try {
            Date date = formatter.parse(mbObject.getField2());
            outputDate = new SimpleDateFormat("EEE, MMM dd, yyyy").format(date);
            mbDate.setText("Posted:  " + outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        mbPost.setText(mbObject.getField3());


        sharedPreferences = getContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        mbPost.setMovementMethod(new ScrollingMovementMethod());


        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isLarger;

                isLarger = ((TextView) v).getLineCount()
                        * ((TextView) v).getLineHeight() > v.getHeight();
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        && isLarger) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);

                } else {
                    v.getParent().requestDisallowInterceptTouchEvent(false);

                }
                return false;
            }
        };

        mbPost.setOnTouchListener(listener);


        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", mbObject.getField5(), null));
                intent.putExtra(Intent.EXTRA_SUBJECT, sharedPreferences.getString("defaultRecord(0)", "") + " Administrator Response to Message");
                intent.putExtra(Intent.EXTRA_TEXT, "Responding to Message Dated: " + outputDate);
                getContext().startActivity(Intent.createChooser(intent, "Choose an Email client :"));

            }
        });


        addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // add to message board
                Log.v(TAG, "addMessage clicked ----> ");

                final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                query = new ParseQuery<ParseObject>(installation.getString("AssociationCode"));

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, com.parse.ParseException e) {


                        ParseFile messageFile = assoc.get(0).getParseFile("MessageFile");


                        try {
                            byte[] file = messageFile.getData();
                            try {
                                messageFileString = new String(file, "UTF-8");

                                Log.d(TAG, "existing messages --->" + messageFileString);

                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                        } catch (com.parse.ParseException e1) {
                            e1.printStackTrace();
                        }


                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, H:mm a");
                        String strDate = sdf.format(c.getTime());

                        String emailToAdd;

                        if ( mbObject.getField5() == null || mbObject.getField5().equals("")) {
                            emailToAdd = "email not available";
                        } else {
                            emailToAdd = mbObject.getField5();
                        }

                        messageFileUpdate = mbObject.getField1() + "|" + mbObject.getField2() +
                                "|" + mbObject.getField3() + "|" + mbObject.getField4() + "|" + emailToAdd + "|" + messageFileString;

                        if ( messageFileUpdate.substring(messageFileUpdate.length() - 1).equals("|")) {
                            messageFileUpdate = messageFileUpdate.substring(0, messageFileUpdate.length() - 1);
                        }

                        Log.d(TAG, "messages after update --->" + messageFileUpdate);



                        byte[] data = messageFileUpdate.getBytes();
                        ParseFile MessageFile = new ParseFile("message.txt", data);


                        try {
                            MessageFile.save();
                        } catch (com.parse.ParseException e1) {
                            e1.printStackTrace();
                        }


                        assoc.get(0).put("MessageDate", strDate);
                        assoc.get(0).put("MessageFile", MessageFile);

                        try {
                            assoc.get(0).save();
                        } catch (com.parse.ParseException e1) {
                            e1.printStackTrace();
                        }


                    }

                });
            }
        });





        return convertView;
    }


}



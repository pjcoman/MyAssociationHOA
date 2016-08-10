package comapps.com.myassociationhoa.messageboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.MBObject;

/**
 * Created by me on 6/28/2016.
 */
class MBAdapter extends ArrayAdapter<MBObject> {

    public static final String TAG = "MESSAGEBOARDADAPTER";
    private static final String MYPREFERENCES = "MyPrefs";

    TextView mbName;
    TextView mbDate;
    TextView mbPost;

    String outputDate;


    SharedPreferences sharedPreferences;

    public MBAdapter(Context context, ArrayList<MBObject> posts) {
        super(context, 0, posts);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MBObject mbObject = getItem(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main_mb_row, parent, false);

        }

        mbName = (TextView) convertView.findViewById(R.id.textViewPostName);
        mbDate = (TextView) convertView.findViewById(R.id.textViewPostDate);
        mbPost = (TextView) convertView.findViewById(R.id.textViewPost);


        Button sendEmail = (Button) convertView.findViewById(R.id.sendEmail);
        Button addComment = (Button) convertView.findViewById(R.id.addCommnetButton);


        mbName.setText(mbObject.getMbName());

        String inputFormat = "M/d/yy, H:mm a";
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);

        try {
            Date date = formatter.parse(mbObject.getMbPostDate());
            outputDate = new SimpleDateFormat("EEE, MMM dd, yyyy").format(date);
            mbDate.setText("Posted:  " + outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }




        mbPost.setText(mbObject.getMbPost());


        sharedPreferences = getContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.getString("defaultRecord(42)", "No").equals("No")) {
            if (addComment != null) {
                addComment.setVisibility(View.GONE);

            }
        }

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
                        "mailto", mbObject.getMbPosterEmailAddress(), null));
                intent.putExtra(Intent.EXTRA_SUBJECT, sharedPreferences.getString("defaultRecord(0)", "") + " Message Board Response");
                intent.putExtra(Intent.EXTRA_TEXT, "Responding to Message Dated: " + outputDate);
                getContext().startActivity(Intent.createChooser(intent, "Choose an Email client :"));

            }
        });


        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Gson gson = new Gson();
                String jsonMbObject = gson.toJson(mbObject); // myObject - instance of MyObject

                Intent intentAddComment = new Intent();
                intentAddComment.setClass(getContext(), PopMBComment.class);
                intentAddComment.putExtra("position", position);
                intentAddComment.putExtra("jsonMbObject", jsonMbObject);
                getContext().startActivity(intentAddComment);


            }
        });


        return convertView;
    }

    private static String TimeStampConverter(final String inputFormat,
                                             String inputTimeStamp, final String outputFormat)
            throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(
                inputFormat).parse(inputTimeStamp));
    }


}



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

import java.util.ArrayList;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.MBObject;

/**
 * Created by me on 6/28/2016.
 */
class MBAdapter extends ArrayAdapter<MBObject> {

    public static final String TAG = "MBADAPTER";
    private static final String MYPREFERENCES = "MyPrefs";


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

        TextView mbName = (TextView) convertView.findViewById(R.id.textViewPostName);
        final TextView mbDate = (TextView) convertView.findViewById(R.id.textViewPostDate);
        TextView mbPost = (TextView) convertView.findViewById(R.id.textViewPost);


        Button sendEmail = (Button) convertView.findViewById(R.id.sendEmail);
        Button addComment = (Button) convertView.findViewById(R.id.addCommnetButton);


        mbName.setText(mbObject.getMbName());
        mbDate.setText(mbObject.getMbPostDate());
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
                intent.putExtra(Intent.EXTRA_TEXT, "Responding to Message Dated: " + mbDate.getText());
                getContext().startActivity(Intent.createChooser(intent, "Choose an Email client :"));

            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentAddComment = new Intent();
                intentAddComment.setClass(getContext(), PopMBComment.class);
                intentAddComment.putExtra("messageindex", position);
                getContext().startActivity(intentAddComment);


            }
        });


        return convertView;
    }


}



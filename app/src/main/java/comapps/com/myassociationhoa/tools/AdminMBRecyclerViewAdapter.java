package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import comapps.com.myassociationhoa.OnEventListener;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClassMB;
import comapps.com.myassociationhoa.RemoteDataTaskClassMBCallBack;
import comapps.com.myassociationhoa.messageboard.MBActivity;
import comapps.com.myassociationhoa.objects.AdminMBObject;

/**
 * Created by me on 6/28/2016.
 */
@SuppressWarnings("ALL")
class AdminMBRecyclerViewAdapter extends RecyclerView.Adapter {



    private static final String TAG = "ADMINMB_RECYCLERADAPTER";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private final SharedPreferences sharedPreferencesVisited;
    private final Context context;
    private ArrayList<AdminMBObject> posts = new ArrayList<>();
    private final LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private static final String MYPREFERENCES = "MyPrefs";
    private String outputDate;
    private String pushMessageString;

    private SimpleDateFormat formatter;
    private final SharedPreferences sharedPreferences;



    public AdminMBRecyclerViewAdapter(Context context, ArrayList<AdminMBObject> posts) {
        this.context = context;
        this.posts = posts;
        this.mInflater = LayoutInflater.from(context);

        sharedPreferences = context.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesVisited = context.getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);

    }



    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = mInflater.inflate(R.layout.content_main_admin_mb_recyclerview_row_swipe, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {

        Log.v(TAG, "position is " + position);

        final ViewHolder holder = (ViewHolder) h;
        final AdminMBObject adminMBObject = posts.get(position);
        final String stringPosition = String.valueOf(position);

        if ( posts != null && 0 <= position && position < posts.size()) {

            holder.mbName.setText(adminMBObject.getPostName());
            String inputFormat = "M/d/yy, h:mm a";
            formatter = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
            try {
                Date date = formatter.parse(adminMBObject.getPostDate());
                outputDate = new SimpleDateFormat("EEE, MMM dd, yyyy").format(date);
                holder.mbDate.setText("Posted:  " + outputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.mbPost.setText(adminMBObject.getPostText());
            binderHelper.bind(holder.swipeRevealLayout, stringPosition);
            holder.bind(stringPosition);

        }




        holder.sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(TAG, "sendEmail clicked" + adminMBObject.toString());



                Date date = null;
                try {
                    date = formatter.parse(adminMBObject.getPostDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                outputDate = new SimpleDateFormat("EEE, MMM dd, yyyy").format(date);

                sendEmail(adminMBObject.getPostEmail());



            }
        });






      holder.addToMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Log.v(TAG, "addToMB clicked " + adminMBObject.toString());
                Log.v(TAG, "position is " + stringPosition);




                  final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                final ParseQuery query = new ParseQuery<>(installation.getString("AssociationCode"));


                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> assoc, com.parse.ParseException e) {


                        final Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a", java.util.Locale.getDefault());
                        String strDate = sdf.format(c.getTime());



                        ParseFile messageFile = assoc.get(0).getParseFile("MessageFile");

                        String messageFileString = "";


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



                        String emailToAdd;

                        if (adminMBObject.getPostEmail() == null || adminMBObject.getPostEmail().equals("")) {
                            emailToAdd = "email not available";
                        } else {
                            emailToAdd = adminMBObject.getPostEmail();
                        }

                        String messageFileUpdate;

                        messageFileUpdate = adminMBObject.getPostName() + "|" + adminMBObject.getPostDate() +
                                "|" + adminMBObject.getPostText() + "|" + adminMBObject.getPostSort() + "|" + emailToAdd + "|" + messageFileString;

                        if (messageFileUpdate.substring(messageFileUpdate.length() - 1).equals("|")) {
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

                        if (sharedPreferencesVisited.getBoolean("PARSESERVER", false)) {

                            pushMessageString = "By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                    adminMBObject.getPostName() +
                                    " has posted a new message to the Message Board.";

                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("AssociationCode", installation.getString("AssociationCode"));
                            params.put("MemberType", "Member");
                            params.put("Channel", "Everyone");
                            params.put("Message", pushMessageString);
                            ParseCloud.callFunctionInBackground("SendPush", params, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object object, com.parse.ParseException e) {
                                    if (e == null) {

                                    }

                                }


                            });

                        } else {

                            ParseQuery pushQuery = ParseInstallation.getQuery();
                            pushQuery.whereEqualTo("AssociationCode", ParseInstallation.getCurrentInstallation().getString("AssociationCode"));

                            ParsePush push = new ParsePush();
                            push.setQuery(pushQuery); // Set our Installation query

                            push.setMessage("By: " + sharedPreferences.getString("defaultRecord(1)", "") + "\n" +
                                    adminMBObject.getPostName() +
                                    " has posted a new message to the Message Board.");

                            push.sendInBackground();

                        }





                        ParseFile pushFile = assoc.get(0).getParseFile("PushFile");

                        String pushFileString = null;

                        try {
                            byte[] file = pushFile.getData();
                            pushFileString = new String(file, "UTF-8");
                        } catch (com.parse.ParseException | UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }

                        Log.d(TAG, "existing push notifications --->" + pushFileString);

                        final SimpleDateFormat month = new SimpleDateFormat("M");
                        final String strMonth = month.format(c.getTime());

                        String pushFileUpdate = pushFileString + "|" + strMonth + "^" + installation.getString("AssociationCode") + "^" + strDate
                                + "^" + installation.getString("memberName") +
                                " has posted a new message comment for review for the Message Board";

                        pushFileUpdate = pushFileUpdate.trim();

                        byte[] pushData = pushFileUpdate.getBytes();
                        pushFile = new ParseFile("Push.txt", pushData);

                        assoc.get(0).put("PushFile", pushFile);

                        try {
                            assoc.get(0).save();

                          

                        } catch (com.parse.ParseException e1) {
                            e1.printStackTrace();
                            assoc.get(0).saveEventually();
                        }










                        String adminMBUpdate = "";

                        for (int i = 0; i < posts.size(); i++) {

                            if ( i != Integer.parseInt(stringPosition) ) {

                                adminMBUpdate = adminMBUpdate + posts.get(i).toBeDeleted() + "|";
                            }

                        }

                        Log.d(TAG, "adminMBUpdate ----> " + adminMBUpdate);

                        byte[] adminMBData = adminMBUpdate.getBytes();
                        ParseFile adminMessageFile = new ParseFile("AdminMessage.txt", adminMBData);
                        try {
                            adminMessageFile.save();
                        } catch (com.parse.ParseException e3) {
                            e3.printStackTrace();
                        }


                        try {
                            query.getFirst().put("AdminMessageFile", adminMessageFile);
                            query.getFirst().put("AdminMessageDate", strDate);
                        } catch (com.parse.ParseException e2) {
                            e2.printStackTrace();
                        }

                        try {
                            query.getFirst().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {

                                    RemoteDataTaskClassMBCallBack remoteDataTaskClassMBCallBack = new RemoteDataTaskClassMBCallBack(context, new
                                            OnEventListener<String>() {
                                                @Override
                                                public void onSuccess() {

                                                    posts.remove(Integer.parseInt(stringPosition));
                                                    notifyItemRemoved(Integer.parseInt(stringPosition));

                                                    Toast.makeText(context, "Message Boards Updated."   , Toast.LENGTH_LONG).show();





                                                    ((AdminMBRecyclerViewActivity) context).finish();

                                                }

                                                @Override
                                                public void onFailure(Exception e) {

                                                    Toast.makeText(context, "ERROR: "+ e.getMessage(), Toast.LENGTH_LONG).show();

                                                }
                                            });

                                    remoteDataTaskClassMBCallBack.execute();








                                }}
                            );



                        } catch (com.parse.ParseException e1) {
                            e1.printStackTrace();
                        }




                    }
                });



            }
        });
    }


        @Override
    public int getItemCount()
    {
        if ( posts == null ) return 0;
        return posts.size();
    }





    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        private final SwipeRevealLayout swipeRevealLayout;
        private final View deleteLayout;
        private final TextView mbName;
        private final TextView mbDate;
        private final TextView mbPost;
        private final Button sendEmail;
        private final Button addToMB;



        public ViewHolder(View v) {
            super(v);
            swipeRevealLayout = (SwipeRevealLayout) v.findViewById(R.id.swipe_layout);
            deleteLayout = v.findViewById(R.id.swipe_delete);
            mbName = (TextView) v.findViewById(R.id.textViewPostName);
            mbDate = (TextView) v.findViewById(R.id.textViewPostDate);
            mbPost = (TextView) v.findViewById(R.id.textViewPost);

            sendEmail = (Button) v.findViewById(R.id.sendEmail);
            addToMB = (Button) v.findViewById(R.id.addToMBButton);

        }

        public void bind(final String data) {
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String adminMBUpdate = "";

                    for (int i = 0; i < posts.size(); i++) {

                        if ( i != Integer.parseInt(data) ) {

                            adminMBUpdate = adminMBUpdate + posts.get(i).toBeDeleted() + "|";
                        }

                    }

                    Log.d(TAG, "adminMBUpdate ----> " + adminMBUpdate);

                    byte[] adminMBData = adminMBUpdate.getBytes();
                    ParseFile adminMessageFile = new ParseFile("AdminMessage.txt", adminMBData);
                    try {
                        adminMessageFile.save();
                    } catch (com.parse.ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a", java.util.Locale.getDefault());
                    String strDate = sdf.format(c.getTime());

                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    ParseQuery query = new ParseQuery<>(installation.getString("AssociationCode"));
                    try {
                        query.getFirst().put("AdminMessageFile", adminMessageFile);
                        query.getFirst().put("AdminMessageDate", strDate);
                    } catch (com.parse.ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        query.getFirst().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(com.parse.ParseException e) {

                                AsyncTask<Void, Void, Void> remoteDataTaskClassMB = new RemoteDataTaskClassMB(context);
                                remoteDataTaskClassMB.execute();

                                AdminMBObject adminMBObject = posts.get(Integer.parseInt(data));

                                Log.d(TAG, "object to remove ----> " + adminMBObject.toBeDeleted());

                                posts.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());




                         }}
                    );



                    } catch (com.parse.ParseException e) {
                        e.printStackTrace();
                    }




                }
            });


        }




        @Override
        public void onClick(View v) {

        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)


    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }




    private void sendEmail(String email) {



        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, sharedPreferences.getString("defaultRecord(0)", "") + " Administrator Response to Message");
        intent.putExtra(Intent.EXTRA_TEXT, "Responding to Message Dated: " + outputDate);

        context.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }



    public void remove(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
    }

    public void startMBActivity() {



        Intent intentMB = new Intent();
        intentMB.setClass(context, MBActivity.class);
        context.startActivity(intentMB);

        ((AdminMBRecyclerViewActivity) context).finish();
    }


    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */



    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }
}
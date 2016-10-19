package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.objects.CalendarObject;

/**
 * Created by me on 6/28/2016.
 */
class CalendarManageAdapter extends ArrayAdapter<CalendarObject> {

    private static final String TAG = "CALENDARMANAGERADAPTER";
    private static final String MYPREFERENCES = "MyPrefs";


    private SharedPreferences sharedPreferences;

    private ParseInstallation installation;
    private String memberName;
    private String assocCode;

    private Date d1;
    private Date d2;
    private Date endDate;

    private TextView eventName;
    private TextView eventDetail;
    private TextView eventStartDate;
    private TextView eventEndDate;
    private TextView textViewTo;

    private Button deleteButton;

    private SimpleDateFormat input;
    private SimpleDateFormat outputStart;
    private SimpleDateFormat outputEnd;
    private SimpleDateFormat calendarAddFormat;
    private String startDateString;
    private String endDateString;

    private ArrayList<CalendarObject> events;
    Context context;



    public CalendarManageAdapter(Context context, ArrayList<CalendarObject> events) {
        super(context, R.layout.content_main_calendar_row_manage, events);
        this.events = events;
        this.context = context;


        installation = ParseInstallation.getCurrentInstallation();

    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        Log.d(TAG, "position ----> " + position);

        final CalendarObject calendarObject = getItem(position);

        sharedPreferences = getContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main_calendar_row_manage, parent, false);
            convertView.setTag(position);

        }



        eventName = (TextView) convertView.findViewById(R.id.textViewEventName);
        eventDetail = (TextView) convertView.findViewById(R.id.textViewEventDetail);
        eventStartDate = (TextView) convertView.findViewById(R.id.textViewDateStart);
        eventEndDate = (TextView) convertView.findViewById(R.id.textViewDateEnd);
        textViewTo = (TextView) convertView.findViewById(R.id.textViewTo);

        deleteButton = (Button) convertView.findViewById(R.id.deleteEventButton);

        input = new SimpleDateFormat("MM/dd/yyyy");
        outputStart = new SimpleDateFormat("E, MMM d");
        outputEnd = new SimpleDateFormat("E, MMM d, yyyy");
        calendarAddFormat = new SimpleDateFormat("yyyyMMdd");





        startDateString = calendarObject != null ? calendarObject.getCalendarStartDate() : null;
        endDateString = calendarObject.getCalendarEndDate();

        try {
            d1 = input.parse(startDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }




        try {
            d2 = input.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ( (eventEndDate.getText().toString()).contains(eventStartDate.getText().toString())) {
            eventEndDate.setText(outputEnd.format(d2));
            textViewTo.setText("");
            eventStartDate.setVisibility(View.GONE);

        } else {
            eventStartDate.setText(outputStart.format(d1));
            eventEndDate.setText(outputEnd.format(d2));
            textViewTo.setText("to");
            eventStartDate.setVisibility(View.VISIBLE);

        }

        notifyDataSetChanged();


        installation = ParseInstallation.getCurrentInstallation();
        memberName = installation.getString("memberName");
        assocCode = installation.getString("AssociationCode");


        eventName.setText(calendarObject.getCalendarText());
        eventDetail.setText(calendarObject.getCalendarDetailText());


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "delete button clicked is " + position);

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                String strDate = sdf.format(c.getTime());

                final ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                ParseQuery query = new ParseQuery<>(installation.getString("AssociationCode")).fromLocalDatastore();



                String eventFileString = "";


                try {
                    ParseFile eventFile = query.getFirst().getParseFile("AdminEventFile");
                    byte[] eventFileData = eventFile.getData();


                    eventFileString = new String(eventFileData, "UTF-8");

                } catch (com.parse.ParseException | UnsupportedEncodingException e1) {
                    e1.printStackTrace();

                }

                Log.d(TAG, "eventFileString ----> " + eventFileString);

                eventFileString = eventFileString + "|";

                Log.d(TAG, "calendarObject ---> " + calendarObject.toStringForDelete());

                String eventFileUpdate = eventFileString.replace(calendarObject.toStringForDelete(), "");

                Log.d(TAG, "eventFileForUpdate ---> " + eventFileUpdate);

                if ( eventFileUpdate.substring(eventFileUpdate.length() - 1).equals("|")) {

                    eventFileUpdate = eventFileUpdate.substring(0, eventFileUpdate.length() - 1);
                }

                Log.d(TAG, "eventFileForUpdate after | removed ---> " + eventFileUpdate);

                byte[] data = eventFileUpdate.getBytes();
                ParseFile eventFile = new ParseFile("EventFile.txt", data);


                try {
                    eventFile.save();
                } catch (com.parse.ParseException e1) {
                    e1.printStackTrace();
                }


                try {
                    query.getFirst().put("AdminEventDate", strDate);
                    query.getFirst().put("AdminEventFile", eventFile);
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }


                try {
                    query.getFirst().save();
                    query.getFirst().saveEventually();
                } catch (com.parse.ParseException e1) {
                    e1.printStackTrace();

                }





                Toast toast = Toast.makeText(getContext(), calendarObject.getCalendarText() +
                        " deleted.", Toast.LENGTH_LONG);




                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();



                AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(getContext());
                remoteDataTaskClass.execute();

                events.remove(position);
                notifyDataSetChanged();



            }
        });


        return convertView;
    }


}



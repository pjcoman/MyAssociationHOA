package comapps.com.myassociationhoa.calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.ParseInstallation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.CalendarObject;

/**
 * Created by me on 6/28/2016.
 */
class CalendarAdapter extends ArrayAdapter<CalendarObject> {

    private static final String TAG = "CALENDARADAPTER";
    private static final String MYPREFERENCES = "MyPrefs";


    SharedPreferences sharedPreferences;

    ParseInstallation installation;
    String memberName;
    String assocCode;

    Date d1;
    Date d2;
    Date endDate;

    TextView eventName;
    TextView eventDetail;
    TextView eventStartDate;
    TextView eventEndDate;
    TextView textViewTo;
    Button addButton;
    Button respondButton;
    SimpleDateFormat input;
    SimpleDateFormat outputStart;
    SimpleDateFormat outputEnd;
    SimpleDateFormat calendarAddFormat;
    String startDateString;
    String endDateString;



    public CalendarAdapter(Context context, ArrayList<CalendarObject> events) {
        super(context, 0, events);

    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        Log.d(TAG, "position ----> " + position);

        final CalendarObject calendarObject = getItem(position);

        sharedPreferences = getContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main_calendar_row, parent, false);
            convertView.setTag(position);

        }



        eventName = (TextView) convertView.findViewById(R.id.textViewEventName);
        eventDetail = (TextView) convertView.findViewById(R.id.textViewEventDetail);
        eventStartDate = (TextView) convertView.findViewById(R.id.textViewDateStart);
        eventEndDate = (TextView) convertView.findViewById(R.id.textViewDateEnd);
        textViewTo = (TextView) convertView.findViewById(R.id.textViewTo);
        addButton = (Button) convertView.findViewById(R.id.addButton);
        respondButton = (Button) convertView.findViewById(R.id.respondButton);

        input = new SimpleDateFormat("MM/dd/yyyy");
        outputStart = new SimpleDateFormat("E, MMM d");
        outputEnd = new SimpleDateFormat("E, MMM d, yyyy");
        calendarAddFormat = new SimpleDateFormat("yyyyMMdd");





        startDateString = calendarObject.getCalendarStartDate();
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












        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                String jsonCalendarObject = gson.toJson(calendarObject);
                CalendarObject calendarObject = gson.fromJson(jsonCalendarObject, CalendarObject.class);

                Log.d(TAG, "calendarObject on click ----> " + calendarObject.toString());


                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");
                try {
                    intent.putExtra("beginTime", input.parse(calendarObject.getCalendarStartDate()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                intent.putExtra("allDay", false);

                try {
                    endDate = input.parse(calendarObject.getCalendarEndDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }




                Log.d(TAG, "position calendar end date ----> " + calendarObject.getCalendarEndDate());

                String until = calendarObject.getCalendarEndDate().substring(6) + calendarObject.getCalendarEndDate().substring(0,2) +
                        calendarObject.getCalendarEndDate().substring(3,5);

                Log.d(TAG, "position until ----> " + until);

                if ( !calendarObject.getCalendarStartDate().equals(calendarObject.getCalendarEndDate())) {

                    intent.putExtra("rrule", "FREQ=DAILY;UNTIL=" + until);

                }



                try {
                    intent.putExtra("endTime", input.parse(calendarObject.getCalendarEndDate()).getTime() + (TimeUnit.DAYS.toMillis(1) - 60000));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                    intent.putExtra("title", calendarObject.getCalendarText());
                    //   intent.putExtra(CalendarContract.Events.CALENDAR_ID,3);
                getContext().startActivity(intent);

            }
        });










        respondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSendEmail = new Intent(android.content.Intent.ACTION_SEND);
                intentSendEmail.putExtra(Intent.EXTRA_EMAIL  , new String[]{sharedPreferences.getString("defaultRecord(5)","")});
                intentSendEmail.setType("text/plain");
            //    String[] address = {sharedPreferences.getString("", "")};
                intentSendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT,
                         assocCode + " Event Request");
                intentSendEmail.putExtra(Intent.EXTRA_TEXT, memberName + " responding to: " + eventName.getText().toString());

                getContext().startActivity((Intent.createChooser(intentSendEmail, "Email")));

            }
        });


        return convertView;
    }


}



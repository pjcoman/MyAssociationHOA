package comapps.com.myassociationhoa.calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseInstallation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.CalendarObject;

/**
 * Created by me on 6/28/2016.
 */
class CalendarAdapter extends ArrayAdapter<CalendarObject> {

    private static final String TAG = "CALENDARADAPTER";
    private static final String MYPREFERENCES = "MyPrefs";

    private Calendar calendar;
    SharedPreferences sharedPreferences;

    ParseInstallation installation;
    String memberName;
    String assocCode;


    public CalendarAdapter(Context context, ArrayList<CalendarObject> events) {
        super(context, 0, events);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final CalendarObject calendarObject = getItem(position);

        sharedPreferences = getContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main_calendar_row, parent, false);

        }

        calendar = Calendar.getInstance();

        final TextView eventName = (TextView) convertView.findViewById(R.id.textViewEventName);
        TextView eventDetail = (TextView) convertView.findViewById(R.id.textViewEventDetail);
        TextView eventStartDate = (TextView) convertView.findViewById(R.id.textViewDateStart);
        TextView eventEndDate = (TextView) convertView.findViewById(R.id.textViewDateEnd);
        TextView textViewTo = (TextView) convertView.findViewById(R.id.textViewTo);
        Button addButton = (Button) convertView.findViewById(R.id.addButton);
        Button respondButton = (Button) convertView.findViewById(R.id.respondButton);

        SimpleDateFormat input = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat outputStart = new SimpleDateFormat("E, MMM d");
        SimpleDateFormat outputEnd = new SimpleDateFormat("E, MMM d, yyyy");

        if ( (eventEndDate.getText().toString()).contains(eventStartDate.getText())) {
            eventStartDate.setVisibility(View.GONE);
            textViewTo.setVisibility(View.GONE);
        } else {
            eventStartDate.setVisibility(View.VISIBLE);
            textViewTo.setVisibility(View.VISIBLE);
        }


        Date d1 = null;
        // parse input
        try {
            d1 = input.parse(calendarObject.getCalendarStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date d2 = null;
        // parse input
        try {
            d2 = input.parse(calendarObject.getCalendarEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ( (eventEndDate.getText().toString()).contains(eventStartDate.getText())) {
            eventEndDate.setText(outputEnd.format(d2));
            textViewTo.setText("");
            notifyDataSetChanged();
        } else {
            eventStartDate.setText(outputStart.format(d1));
            eventEndDate.setText(outputEnd.format(d2));
            textViewTo.setText("to");
            notifyDataSetChanged();
        }


        installation = ParseInstallation.getCurrentInstallation();
        memberName = installation.getString("memberName");
        assocCode = installation.getString("AssociationCode");


        eventName.setText(calendarObject.getCalendarText());
        eventDetail.setText(calendarObject.getCalendarDetailText());


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_EDIT);

                try {
                    calendar.setTime(new SimpleDateFormat("E, MMM d").parse(calendarObject.getCalendarStartDate()));
                    intent.putExtra("beginTime", calendar.getTimeInMillis());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                intent.setType("vnd.android.cursor.item/event");

                try {
                    calendar.setTime(new SimpleDateFormat("E, MMM d yyyy").parse(calendarObject.getCalendarEndDate()));
                    intent.putExtra("endTime", calendar.getTimeInMillis());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                intent.putExtra("title", calendarObject.getCalendarText());
                getContext().startActivity(intent);

            }
        });


        respondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSendEmail = new Intent(android.content.Intent.ACTION_SEND);
                intentSendEmail.putExtra(Intent.EXTRA_EMAIL  , new String[]{sharedPreferences.getString("defaultRecord(2)","")});
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



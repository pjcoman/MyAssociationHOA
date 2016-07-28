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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.CalendarObject;

/**
 * Created by me on 6/28/2016.
 */
class CalendarAdapter extends ArrayAdapter<CalendarObject> {

    private static final String TAG = "CALENDARADAPTER";
    private static final String MYPREFERENCES = "MyPrefs";


    SharedPreferences sharedPreferences;


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

        final TextView eventName = (TextView) convertView.findViewById(R.id.textViewEventName);
        TextView eventDetail = (TextView) convertView.findViewById(R.id.textViewEventDetail);
        TextView eventStartDate = (TextView) convertView.findViewById(R.id.textViewDateStart);
        TextView eventEndDate = (TextView) convertView.findViewById(R.id.textViewDateEnd);
        Button addButton = (Button) convertView.findViewById(R.id.addButton);
        Button respondButton = (Button) convertView.findViewById(R.id.respondButton);

        eventName.setText(calendarObject.getCalendarText());
        eventDetail.setText(calendarObject.getCalendarDetailText());
        eventStartDate.setText(calendarObject.getCalendarStartDate());
        eventEndDate.setText(calendarObject.getCalendarEndDate());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);

                try {
                    cal.setTime(new SimpleDateFormat("E, MMM d").parse(calendarObject.getCalendarStartDate()));
                    intent.putExtra("beginTime", cal.getTimeInMillis());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                intent.setType("vnd.android.cursor.item/event");

                try {
                    cal.setTime(new SimpleDateFormat("E, MMM d yyyy").parse(calendarObject.getCalendarEndDate()));
                    intent.putExtra("endTime", cal.getTimeInMillis());
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
                intentSendEmail.setType("text/plain");

                intentSendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        calendarObject.getCalendarText() + " Request");
                intentSendEmail.putExtra(Intent.EXTRA_TEXT, sharedPreferences.getString("MEMBERNAME", "member") + " responding to " + calendarObject.getCalendarText2());

                getContext().startActivity((Intent.createChooser(intentSendEmail, "Email")));

            }
        });


        return convertView;
    }


}



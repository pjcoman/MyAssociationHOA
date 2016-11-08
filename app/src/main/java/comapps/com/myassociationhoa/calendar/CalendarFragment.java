package comapps.com.myassociationhoa.calendar;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.objects.CalendarObject;

/**
 * Created by me on 6/22/2016.
 */
@SuppressWarnings("ALL")
public class CalendarFragment extends ListFragment {

    private static final String TAG = "CALENDARFRAGMENT";
    private static final String MYPREFERENCES = "MyPrefs";


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        ArrayList<CalendarObject> events = new ArrayList<>();
        Integer calendarItems = Integer.valueOf(sharedPreferences.getString("calendarSize", ""));

        for (int i = 0; i < calendarItems; i++) {

            String jsonCalendarObject = sharedPreferences.getString("calendarObject" + "[" + i + "]", "");
            Log.d(TAG, "calendar json string is " + jsonCalendarObject);
            Gson gson = new Gson();
            CalendarObject calendarObject = gson.fromJson(jsonCalendarObject, CalendarObject.class);
            events.add(calendarObject);

        }


        CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity(), events);
        setListAdapter(calendarAdapter);


    }


}
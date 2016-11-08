package comapps.com.myassociationhoa.tools;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import comapps.com.myassociationhoa.objects.CalendarObject;

/**
 * Created by me on 6/25/2016.
 */
@SuppressWarnings("ALL")
public class CalendarManageFragment extends ListFragment {

    private static final String TAG = "ADMINCALENDARFRAGMENT";
    private static final String MYPREFERENCES = "MyPrefs";


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        ArrayList<CalendarObject> events = new ArrayList<>();
        Integer calendarItems = Integer.valueOf(sharedPreferences.getString("adminCalendarSize", ""));

        for (int i = 0; i < calendarItems; i++) {

            String jsonCalendarObject = sharedPreferences.getString("adminCalendarObject" + "[" + i + "]", "");
            Log.d(TAG, "calendar json string is " + jsonCalendarObject);
            Gson gson = new Gson();
            CalendarObject calendarObject = gson.fromJson(jsonCalendarObject, CalendarObject.class);
            events.add(calendarObject);

        }


        Collections.sort(events, new Comparator<CalendarObject>()
        {
            @Override
            public int compare(CalendarObject c1, CalendarObject c2) {

                return c1.getCalendarSortDate().compareTo(c2.getCalendarSortDate());
            }
        });


        CalendarManageAdapter calendarAdapter = new CalendarManageAdapter(getActivity(), events);
        setListAdapter(calendarAdapter);


    }



}
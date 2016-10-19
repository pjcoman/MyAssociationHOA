package comapps.com.myassociationhoa.tools;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.objects.CalendarObject;

/**
 * Created by me on 6/25/2016.
 */
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


        CalendarManageAdapter calendarAdapter = new CalendarManageAdapter(getActivity(), events);
        setListAdapter(calendarAdapter);


    }


}
package comapps.com.myassociationhoa.messageboard;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.objects.MBObject;

/**
 * Created by me on 6/22/2016.
 */
public class MBFragment extends ListFragment {

    private static final String TAG = "MBFRAGMENT";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";
    private static final String MYPREFERENCES = "MyPrefs";

    SharedPreferences sharedPreferencesVisited;
    SharedPreferences.Editor sditorVisted;
    SharedPreferences sharedPreferences;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferencesVisited = getActivity().getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        ArrayList<MBObject> posts = new ArrayList<>();
        Integer mbItems = sharedPreferences.getInt("mbSize", 0);

        for (int i = 0; i < mbItems; i++) {

            String jsonMbObject = sharedPreferences.getString("mbObject" + "[" + i + "]", "");
            Log.d(TAG, "mb json string is " + jsonMbObject);
            Gson gson = new Gson();
            MBObject mbObject = gson.fromJson(jsonMbObject, MBObject.class);
            posts.add(mbObject);

        }




        MBAdapter mbAdapter = new MBAdapter(getActivity(), posts);
        setListAdapter(mbAdapter);


    }


}

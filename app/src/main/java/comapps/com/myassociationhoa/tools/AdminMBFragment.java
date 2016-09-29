package comapps.com.myassociationhoa.tools;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.objects.AdminMBObject;

/**
 * Created by me on 6/22/2016.
 */
public class AdminMBFragment extends ListFragment {

    private static final String TAG = "ADMINMBFRAGMENT";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";
    private static final String MYPREFERENCES = "MyPrefs";

    SharedPreferences sharedPreferencesVisited;
    SharedPreferences sharedPreferences;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferencesVisited = getActivity().getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        ArrayList<AdminMBObject> posts = new ArrayList<>();
        Integer admin_mbItems = sharedPreferencesVisited.getInt("admin_mbSize", 0);

        for (int i = 0; i < admin_mbItems; i++) {

            String jsonAdminMbObject = sharedPreferences.getString("admin_mbObject" + "[" + i + "]", "");
            Log.d(TAG, "admin mb json string is " + jsonAdminMbObject);
            Gson gson = new Gson();
            AdminMBObject adminMBObject = gson.fromJson(jsonAdminMbObject, AdminMBObject.class);
            posts.add(adminMBObject);

        }


        AdminMBAdapter mbAdapter = new AdminMBAdapter(getActivity(), posts);
        setListAdapter(mbAdapter);


    }


}

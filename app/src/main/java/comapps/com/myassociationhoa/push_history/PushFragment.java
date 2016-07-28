package comapps.com.myassociationhoa.push_history;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.objects.PushObject;

/**
 * Created by me on 6/22/2016.
 */
public class PushFragment extends ListFragment {

    private static final String TAG = "PUSHFRAGMENT";
    private static final String MYPREFERENCES = "MyPrefs";


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        ArrayList<PushObject> notifications = new ArrayList<>();
        Integer pushItems = Integer.valueOf(sharedPreferences.getString("pushObjectsSize", "0"));

        for (int i = 0; i < pushItems; i++) {

            String jsonPushObject = sharedPreferences.getString("pushObject" + "[" + i + "]", "");
            Log.d(TAG, "push json string is " + jsonPushObject);
            Gson gson = new Gson();
            PushObject pushObject = gson.fromJson(jsonPushObject, PushObject.class);
            notifications.add(pushObject);

        }


        PushAdapter pushAdapter = new PushAdapter(getActivity(), notifications);
        setListAdapter(pushAdapter);


    }


}

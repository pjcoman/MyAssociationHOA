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
    private static final String MYPREFERENCES = "MyPrefs";
    private SharedPreferences sharedPreferences;
    private MBObject mbObject;
    private ArrayList<MBObject> posts;
    private MBAdapter mbAdapter;


    private int i;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        sharedPreferences = getActivity().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        posts = new ArrayList<>();
        Integer mbItems = sharedPreferences.getInt("mbSize", 0);

        for (i = 0; i < mbItems; i++) {

            String jsonMbObject = sharedPreferences.getString("mbObject" + "[" + i + "]", "");
            Log.d(TAG, "mb json string is " + jsonMbObject);
            Gson gson = new Gson();
            mbObject = gson.fromJson(jsonMbObject, MBObject.class);
            posts.add(mbObject);

        }


        mbAdapter = new MBAdapter(getActivity(), posts);
        setListAdapter(mbAdapter);


        mbAdapter.setDropDownViewResource(posts.size());
    }


    }







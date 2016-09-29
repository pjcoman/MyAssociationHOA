package comapps.com.myassociationhoa;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by me on 6/15/2016.
 */
public class ParseApplication extends Application  {

    private static final String TAG = "PARSEAPPLICATION";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";
    private static final String ASSOCPREFERENCES = "AssocPrefs";


    private SharedPreferences sharedPreferencesAssoc;
    private SharedPreferences sharedPreferencesVisited;


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);

        Parse.enableLocalDatastore(getApplicationContext());

        // Add your initialization code here

        Log.d(TAG, "memberType is ----> " + sharedPreferencesVisited.getString("MEMBERTYPE", ""));


      /*   if (sharedPreferencesVisited.getString("MEMBERTYPE", "").equals("Administrator") || sharedPreferencesVisited.getString("MEMBERTYPE", "").equals("Master")) {

       Parse.initialize(this, "O1qVaYT9FwLYZ7BxWdZZijEVxVqxbY4JkVwjFOSD", "lAChntUw39NKIMNTALtexvP8RBL2819XTGPDWSqd");
        } else {*/
            Parse.initialize(this, "O1qVaYT9FwLYZ7BxWdZZijEVxVqxbY4JkVwjFOSD", "FxH2jyeQfyEEfrVnXoRgdpmhVs49uEK2EEfCJ07q");
            //  }


            ParsePush.subscribeInBackground("", new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "successfully subscribed to the broadcast channel.");
                    } else {
                        Log.e(TAG, "failed to subscribe for push", e);
                    }
                }
            });


            ParseUser.enableAutomaticUser();
            ParseACL defaultACL = new ParseACL();

            // If you would like all objects to be private by default, remove this line.
            defaultACL.setPublicReadAccess(true);

            ParseACL.setDefaultACL(defaultACL, true);

            //   PushService.setDefaultPushCallback(this, MainActivity.class);


        }

    }

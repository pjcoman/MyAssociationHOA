package comapps.com.myassociationhoa;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by me on 6/15/2016.
 */
@SuppressWarnings("ALL")
public class ParseApplication extends Application  {

    private static final String TAG = "PARSEAPPLICATION";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private SharedPreferences sharedPreferencesVisited;

    private SharedPreferences.Editor sharedPreferencesVisitedEditor;


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);


        Parse.enableLocalDatastore(getApplicationContext());

        // Add your initialization code here

        Log.d(TAG, "memberType is ----> " + sharedPreferencesVisited.getString("MEMBERTYPE", ""));

        //****************************************************************************************************************************************

    /*  try {
            Parse.initialize(this, "O1qVaYT9FwLYZ7BxWdZZijEVxVqxbY4JkVwjFOSD", "FxH2jyeQfyEEfrVnXoRgdpmhVs49uEK2EEfCJ07q");

            Log.d(TAG, "logged in to parse.com.");

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

            sharedPreferencesVisitedEditor = sharedPreferencesVisited.edit();
            sharedPreferencesVisitedEditor.putBoolean("PARSESERVER", false);
            sharedPreferencesVisitedEditor.commit();




        } catch (Exception e) {
            e.printStackTrace();


            sharedPreferencesVisitedEditor = sharedPreferencesVisited.edit();
            sharedPreferencesVisitedEditor.putBoolean("PARSESERVER", true  );
            sharedPreferencesVisitedEditor.commit();


            Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                    .applicationId("O1qVaYT9FwLYZ7BxWdZZijEVxVqxbY4JkVwjFOSD")
                    .clientKey("FxH2jyeQfyEEfrVnXoRgdpmhVs49uEK2EEfCJ07q")
                    .server("https://api.myassociation.info:1337/parse/")
                    .enableLocalDataStore()
                    .build()


            );

            Log.d(TAG, "logged in to parseserver");



        }
*/
        //********************************************************************************************************************************************/


        sharedPreferencesVisitedEditor = sharedPreferencesVisited.edit();
        sharedPreferencesVisitedEditor.putBoolean("PARSESERVER", true);
        sharedPreferencesVisitedEditor.commit();


        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("O1qVaYT9FwLYZ7BxWdZZijEVxVqxbY4JkVwjFOSD")
                .clientKey("FxH2jyeQfyEEfrVnXoRgdpmhVs49uEK2EEfCJ07q")
                .server("https://api.myassociation.info:1337/parse/")
                .enableLocalDataStore()
                .build()


        );

        Log.d(TAG, "logged in to parseserver");



        Log.d(TAG, "firebase token ----> " + FirebaseInstanceId.getInstance().getToken());






            ParseUser.enableAutomaticUser();
            ParseACL defaultACL = new ParseACL();

            // If you would like all objects to be private by default, remove this line.
            defaultACL.setPublicReadAccess(true);

            ParseACL.setDefaultACL(defaultACL, true);

            //   PushService.setDefaultPushCallback(this, MainActivity.class);


        }

    }

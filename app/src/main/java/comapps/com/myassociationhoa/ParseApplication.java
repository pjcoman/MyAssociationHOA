package comapps.com.myassociationhoa;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by me on 6/15/2016.
 */
public class ParseApplication extends Application  {

    private static final String TAG = "PARSEAPPLICATION";

    private static final String ASSOCPREFERENCES = "AssocPrefs";


    private SharedPreferences sharedPreferencesAssoc;
    private SharedPreferences sharedPreferencesVisited;


    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(getApplicationContext());

        // Add your initialization code here
        Parse.initialize(this, "O1qVaYT9FwLYZ7BxWdZZijEVxVqxbY4JkVwjFOSD", "FxH2jyeQfyEEfrVnXoRgdpmhVs49uEK2EEfCJ07q");
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

        //   PushService.setDefaultPushCallback(this, MainActivity.class);

        ParseQuery queryAssociations = new ParseQuery<>(ParseInstallation.getCurrentInstallation().getString("AssociationCode"));
        queryAssociations.findInBackground(new FindCallback() {
            @Override
            public void done(final List objects, ParseException e) {
                ParseObject.unpinAllInBackground(ParseInstallation.getCurrentInstallation().getString("AssociationCode"), new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseObject.pinAllInBackground(ParseInstallation.getCurrentInstallation().getString("AssociationCode"), objects);
                    }
                });

            }

            @Override
            public void done(Object o, Throwable throwable) {

            }
        });



    }

}

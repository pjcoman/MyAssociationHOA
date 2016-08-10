package comapps.com.myassociationhoa;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by me on 6/15/2016.
 */
public class ParseApplication extends Application  {

    private static final String TAG = "PARSEAPPLICATION";

    private static final String ASSOCPREFERENCES = "AssocPrefs";


    private SharedPreferences sharedPreferencesAssoc;


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

        sharedPreferencesAssoc = getSharedPreferences(ASSOCPREFERENCES, Context.MODE_PRIVATE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Home");
        try {
            ParseFile parseFile = (ParseFile) query.getFirst().get("NewHOAFile");
            byte[] data = parseFile.getData();

            String codesFileString = null;


            try {
                codesFileString = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //   Log.d(TAG, "codes -----> " + codesFileString);


            String passCodes[] = codesFileString.split("(\\|)|(\\^)", -1);

            SharedPreferences.Editor editor = sharedPreferencesAssoc.edit();

            int i = 0;
            int j = 1;
            int k = 0;
            for (String passcode : passCodes) {
                //   Log.d(TAG, "passcode member ----> " + passcode);


                switch (j) {
                    case 1:
                        i++;
                        j++;
                        k++;

                        editor.putString("passcodeASSOC_LONGNAME" + "(" + i + ")", passcode);
                        Log.d(TAG, "Assoc Long Name ----> " + passcode);
                        break;
                    case 2:
                        j++;
                        k++;

                        editor.putString("passcodeASSOC_NAME" + "(" + i + ")", passcode);
                        Log.d(TAG, "Assoc name for Parse ----> " + passcode );
                        break;
                    case 3:
                        j++;
                        k++;

                        editor.putString("passcodeADMIN_PW" + "(" + i + ")", passcode);
                        Log.d(TAG, "Assoc admin password ----> " + passcode);
                        break;
                    case 4:
                        j++;
                        k++;

                        editor.putString("passcodeMEMBER_PW" + "(" + i + ")", passcode);
                        Log.d(TAG, "Assoc member password ----> " + passcode);
                        break;
                    case 5:
                        j = 1;
                        k++;

                        editor.putString("passcodeASSOC_SHORTNAME" + "(" + i + ")", passcode);
                        Log.d(TAG, "Assoc short name ----> " + passcode);
                        break;
                }

            }
            Log.d(TAG, "passcode size is -----> " + k);
            editor.putString("passcodeSize", String.valueOf(k));
            editor.apply();


        } catch (ParseException e) {
            e.printStackTrace();
        }


        ParseQuery<ParseObject> queryAssociations = new ParseQuery<>(
                ParseInstallation.getCurrentInstallation().getString("AssociationCode"));
        queryAssociations.setLimit(5);


        queryAssociations.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> object, ParseException e) {
                // Remove the previously cached results.



                ParseObject.unpinAllInBackground(ParseInstallation.getCurrentInstallation().getString("AssociationCode"), new DeleteCallback() {
                    public void done(ParseException e) {
                        // Cache the new results.
                        ParseObject.pinAllInBackground(ParseInstallation.getCurrentInstallation().getString("AssociationCode"), object);
                    }
                });
            }
        });





        Log.d(TAG, "App started up");


    }

}

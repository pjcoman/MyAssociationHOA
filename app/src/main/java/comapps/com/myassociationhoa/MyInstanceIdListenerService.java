package comapps.com.myassociationhoa;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by me on 10/18/2016.
 */

@SuppressWarnings("ALL")
public class MyInstanceIdListenerService extends FirebaseInstanceIdService {

    private static final String TAG = "MYINSTANCEIDLISTENERSERVICE";

    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences.Editor editorVisited;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.

        sharedPreferencesVisited = getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        //   sendRegistrationToServer(refreshedToken);

        editorVisited = sharedPreferencesVisited.edit();
        editorVisited.putString("FIREBASETOKEN", refreshedToken);
        editorVisited.apply();
    }

}
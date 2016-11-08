package comapps.com.myassociationhoa;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by me on 6/25/2016.
 */
@SuppressWarnings("ALL")
class SharedPreferenceWeather {

    private final SharedPreferences prefs;

    public SharedPreferenceWeather(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    // If the user has not chosen a city yet, return
    // Sydney as the default city
    String getCity() {
        return prefs.getString("city", "Naples, Fl");
    }

    void setCity(String city) {
        prefs.edit().putString("city", city).apply();
    }

}
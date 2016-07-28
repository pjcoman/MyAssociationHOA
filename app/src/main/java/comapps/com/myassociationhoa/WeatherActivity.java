package comapps.com.myassociationhoa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WEATHERACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    private SharedPreferences sharedPreferences;
    private Context context;
    private LinearLayout weatherLayout;
    private JSONObject jsonObj;

    private TextView currentWeatherDescription;
    private TextView currentWeatherTemp;
    private TextView currentWeatherWindDirection;
    TextView currentWeatherWindSpeed;
    private TextView forecastTempMaxMin;
    private TextView forecastDescription;
    private TextView forecastWindDirection;
    TextView forecastWindSpeed;

    String associationNameText;
    private String currentWeatherDescriptionText;
    private String currentWeatherTempText;
    private String currentWeatherWindDirectionText;
    private String currentWeatherWindSpeedText;
    private String forecastTempMaxMinText;
    private String forecastDescriptionText;
    private String forecastWindDirectionText;
    private String forecastWindSpeedText;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_weather);


        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {


            bar.setTitle("Weather");

        }

        weatherLayout = (LinearLayout) findViewById(R.id.content_main_weather);
        TextView associationName = (TextView) findViewById(R.id.textViewAssociationName);

        currentWeatherDescription = (TextView) findViewById(R.id.textViewCurrentDescription);
        currentWeatherTemp = (TextView) findViewById(R.id.textViewCurrentTemp);
        currentWeatherWindDirection = (TextView) findViewById(R.id.textViewCurrentWindDirection);
        forecastTempMaxMin = (TextView) findViewById(R.id.textViewHighLow);
        forecastDescription = (TextView) findViewById(R.id.textViewForecastDescription);
        forecastWindDirection = (TextView) findViewById(R.id.textViewForecastWindDirection);




        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        String backgroundImageUrl = sharedPreferences.getString("backgroundImage2Url", "");

        Log.d(TAG, "background image address is " + backgroundImageUrl);


        Picasso.with(context).load(backgroundImageUrl).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                weatherLayout.setBackgroundDrawable(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });

        associationName.setText(sharedPreferences.getString("defaultRecord(1)", ""));

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String zip = sharedPreferences.getString("defaultRecord(23)", "");
        String url = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + zip
                + "&format=json&key=u5emertxp3xptfs3vzskteyk";

        // Request a string response from the URL.


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "weather online response is ----> " + response);

                        try {
                            jsonObj = new JSONObject(response);
                            currentWeatherDescriptionText = jsonObj.getJSONObject("data").getJSONArray("current_condition").
                                    getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");

                            currentWeatherTempText = jsonObj.getJSONObject("data").getJSONArray("current_condition")
                                    .getJSONObject(0).getString("temp_F") + (char) 0x00B0;

                            currentWeatherWindDirectionText = "Wind " + (jsonObj.getJSONObject("data").getJSONArray("current_condition")
                                    .getJSONObject(0).getString("winddir16Point") + " @ ") + jsonObj.getJSONObject("data").getJSONArray("current_condition").
                                    getJSONObject(0).getString("windspeedMiles") + "mph";

                            forecastTempMaxMinText = "High " + jsonObj.getJSONObject("data").getJSONArray("weather")
                                    .getJSONObject(0).getString("tempMaxF") + (char) 0x00B0 + " "
                                    + "Low " + jsonObj.getJSONObject("data").getJSONArray("weather")
                                    .getJSONObject(0).getString("tempMinF") + (char) 0x00B0;

                            forecastDescriptionText = "Forecast " + jsonObj.getJSONObject("data").getJSONArray("weather").
                                    getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");

                            forecastWindDirectionText = jsonObj.getJSONObject("data").getJSONArray("weather")
                                    .getJSONObject(0).getString("winddir16Point") + " @ " + jsonObj.getJSONObject("data").getJSONArray("weather")
                                    .getJSONObject(0).getString("windspeedMiles") + "mph";

                            currentWeatherDescription.setText(currentWeatherDescriptionText);
                            currentWeatherTemp.setText(currentWeatherTempText);
                            currentWeatherWindDirection.setText(currentWeatherWindDirectionText);
                            forecastTempMaxMin.setText(forecastTempMaxMinText);
                            forecastDescription.setText(forecastDescriptionText);
                            forecastWindDirection.setText(forecastWindDirectionText);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "That didn't work!");
            }
        });


// Add the request to the RequestQueue.
        queue.add(stringRequest);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Guide();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Guide() {

        Intent loadGuide = new Intent();
        loadGuide.setClass(this, GuideActivity.class);
        startActivity(loadGuide);
        //     overridePendingTransition(R.anim.fadeinanimationgallery,R.anim.fadeoutanimationgallery);


    }




    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


}

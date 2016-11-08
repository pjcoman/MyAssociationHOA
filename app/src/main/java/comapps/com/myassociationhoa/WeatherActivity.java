package comapps.com.myassociationhoa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/25/2016.
 */
@SuppressWarnings("ALL")
public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WEATHERACTIVITY";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";
    private static final String MYPREFERENCES = "MyPrefs";
    private SharedPreferences sharedPreferences;
    private JSONObject jsonObj;
    private TextView currentWeatherDescription;
    private TextView currentWeatherTemp;
    private TextView currentWeatherWindDirection;
    private TextView forecastTempMaxMin;
    private TextView forecastDescription;
    private TextView forecastWindDirection;



    private String currentWeatherDescriptionText;
    private String currentWeatherTempText;
    private String currentWeatherWindDirectionText;
    private String forecastTempMaxMinText;
    private String forecastDescriptionText;
    private String forecastWindDirectionText;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setupWindowAnimations();


        setContentView(R.layout.content_main_weather);


        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {


            bar.setTitle("Weather");

        }


        TextView associationName = (TextView) findViewById(R.id.textViewAssociationName);
        currentWeatherDescription = (TextView) findViewById(R.id.textViewCurrentDescription);
        currentWeatherTemp = (TextView) findViewById(R.id.textViewCurrentTemp);
        currentWeatherWindDirection = (TextView) findViewById(R.id.textViewCurrentWindDirection);
        forecastTempMaxMin = (TextView) findViewById(R.id.textViewHighLow);
        forecastDescription = (TextView) findViewById(R.id.textViewForecastDescription);
        forecastWindDirection = (TextView) findViewById(R.id.textViewForecastWindDirection);


        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);



        String urlImage = sharedPreferences.getString("backgroundImage2Url","");



        ImageView iv = (ImageView) findViewById(R.id.ivWeather);
        if(urlImage.length() > 0)
            Glide.with(getApplicationContext())
                    .load(urlImage)
                    .into(iv);


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

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));



        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.RIGHT);
        getWindow().setEnterTransition(slideTransition);


        Slide slideTransitionExit = new Slide();
        slideTransitionExit.setSlideEdge(Gravity.RIGHT);
        getWindow().setExitTransition(slideTransitionExit);



    }



    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


}

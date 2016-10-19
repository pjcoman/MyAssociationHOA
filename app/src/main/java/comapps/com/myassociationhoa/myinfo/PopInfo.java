package comapps.com.myassociationhoa.myinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.ParseInstallation;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.autos.AutosMyInfoActivity;
import comapps.com.myassociationhoa.guests.GuestsMyInfoActivity;
import comapps.com.myassociationhoa.pets.PetsMyInfoActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopInfo extends AppCompatActivity {

    private static final String TAG = "POPINFO";
    private static final String MYPREFERENCES = "MyPrefs";

    private SharedPreferences sharedPreferences;

    private ParseInstallation installation;

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_info);

        TextView personalInfo = (TextView) findViewById(R.id.textViewMyInfo);
        TextView petInfo = (TextView) findViewById(R.id.textViewPetInfo);
        TextView autoInfo = (TextView) findViewById(R.id.textViewAutoInfo);
        TextView guestInfo = (TextView) findViewById(R.id.textViewGuestInfo);

        TextView cancel = (TextView) findViewById(R.id.textViewCancel);

        installation = ParseInstallation.getCurrentInstallation();

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {
            if (petInfo != null) {
                petInfo.setVisibility(View.GONE);
            }
        }

        if (sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {
            if (autoInfo != null) {
                autoInfo.setVisibility(View.GONE);
            }
        }

        if (sharedPreferences.getString("defaultRecord(39)", "No").equals("No")) {
            if (guestInfo != null) {
                guestInfo.setVisibility(View.GONE);
            }
        }


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));


        assert personalInfo != null;
        personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent intent = new Intent();
                intent.putExtra("FROMMYINFO", true);
                intent.setClass(PopInfo.this, PersonalInfoActivity.class);
                startActivity(intent);



            }
        });

        assert petInfo != null;
        petInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent intent = new Intent();
                intent.setClass(PopInfo.this, PetsMyInfoActivity.class);
                intent.putExtra("memberNumber", installation.getString("memberNumber"));
                intent.putExtra("fromPopInfo", true);
                startActivity(intent);


            }
        });

        assert autoInfo != null;
        autoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent intent = new Intent();
                intent.setClass(PopInfo.this, AutosMyInfoActivity.class);
                intent.putExtra("memberNumber", installation.getString("memberNumber"));
                intent.putExtra("fromPopInfo", true);
                startActivity(intent);



            }
        });

        assert guestInfo != null;
        guestInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent intent = new Intent();
                intent.setClass(PopInfo.this, GuestsMyInfoActivity.class);
                intent.putExtra("memberNumber", installation.getString("memberNumber"));
                intent.putExtra("fromPopInfo", true);
                startActivity(intent);



            }
        });


        assert cancel != null;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

              finish();

            }
        });


    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {


        finish();


    }


}



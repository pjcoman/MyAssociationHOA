package comapps.com.myassociationhoa.documents;

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

import comapps.com.myassociationhoa.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
@SuppressWarnings("ALL")
public class PopDocuments extends AppCompatActivity {

    private static final String TAG = "POPUPACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs" ;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_documents);

        TextView title = (TextView) findViewById(R.id.textViewTitle);

        TextView rulesPdf = (TextView) findViewById(R.id.textViewRules);
        TextView byLawsPdf = (TextView) findViewById(R.id.textViewByLaws);
        TextView minutesPdf = (TextView) findViewById(R.id.textViewMinutes);
        TextView misc1 = (TextView) findViewById(R.id.textViewMisc1Pdf);
        TextView misc2 = (TextView) findViewById(R.id.textViewMisc2Pdf);
        TextView misc3 = (TextView) findViewById(R.id.textViewMisc3Pdf);
        TextView cancel = (TextView) findViewById(R.id.textViewCancel);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);



        title.setText(sharedPreferences.getString("defaultRecord(1)", ""));

        if ( sharedPreferences.getString("rulespdfurl", null) == null ) {
            misc1.setVisibility(View.GONE);
        }

        if ( sharedPreferences.getString("bylawspdfurl", null) == null ) {
            misc2.setVisibility(View.GONE);
        }

        if ( sharedPreferences.getString("minutespdfurl", null) == null ) {
            misc3.setVisibility(View.GONE);
        }

        if ( sharedPreferences.getString("defaultRecord(31)", null) == null ) {
            misc1.setVisibility(View.GONE);
        } else {

            misc1.setText(sharedPreferences.getString("defaultRecord(31)",""));
        }

        if ( sharedPreferences.getString("defaultRecord(32)", null) == null ) {
            misc2.setVisibility(View.GONE);
        }  else {

            misc2.setText(sharedPreferences.getString("defaultRecord(32)",""));
        }

        if ( sharedPreferences.getString("defaultRecord(33)", null) == null ) {
            misc3.setVisibility(View.GONE);
        }  else {
            
            misc3.setText(sharedPreferences.getString("defaultRecord(33)",""));
        }



        DisplayMetrics dm =  new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.9));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        rulesPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent intentRules = new Intent();
                intentRules.setClass(PopDocuments.this, DocumentReaderActivity.class);
                intentRules.putExtra("file", "rules");
                startActivity(intentRules);

            }
        });

        byLawsPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent intentByLaws = new Intent();
                intentByLaws.setClass(PopDocuments.this, DocumentReaderActivity.class);
                intentByLaws.putExtra("file", "bylaws");
                startActivity(intentByLaws);

            }
        });

        minutesPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent intentMinutes = new Intent();
                intentMinutes.setClass(PopDocuments.this, DocumentReaderActivity.class);
                intentMinutes.putExtra("file", "minutes");
                startActivity(intentMinutes);

            }
        });

        misc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent misc1Intent = new Intent();
                misc1Intent.setClass(PopDocuments.this, DocumentReaderActivity.class);
                misc1Intent.putExtra("file", "misc1");
                misc1Intent.putExtra("name", sharedPreferences.getString("defaultRecord(31)", ""));
                startActivity(misc1Intent);

            }
        });

        misc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent misc2Intent = new Intent();
                misc2Intent.setClass(PopDocuments.this, DocumentReaderActivity.class);
                misc2Intent.putExtra("file", "misc2");
                misc2Intent.putExtra("name", sharedPreferences.getString("defaultRecord(32)", ""));
                startActivity(misc2Intent);

            }
        });

        misc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(0)
                        .playOn(v);

                Intent misc3Intent = new Intent();
                misc3Intent.setClass(PopDocuments.this, DocumentReaderActivity.class);
                misc3Intent.putExtra("file", "misc3");
                misc3Intent.putExtra("name", sharedPreferences.getString("defaultRecord(33)", ""));
                startActivity(misc3Intent);

            }
        });

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

       this.finish();


    }


}



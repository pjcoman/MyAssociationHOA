package comapps.com.myassociationhoa;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import comapps.com.myassociationhoa.calendar.CalendarActivity;
import comapps.com.myassociationhoa.documents.PopEmailDocuments;
import comapps.com.myassociationhoa.maintenance.AddMaintenanceCategory;
import comapps.com.myassociationhoa.tools.BackupRestoreFilesActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/22/2016.
 */
public class ToolsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "TOOLSACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";

    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.content_tools);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Tool Box");
        }


        Button b1 = (Button) findViewById(R.id.button);
        Button b2 = (Button) findViewById(R.id.button2);
        Button b3 = (Button) findViewById(R.id.button3);
        Button b4 = (Button) findViewById(R.id.button4);
        Button b5 = (Button) findViewById(R.id.button5);
        Button b6 = (Button) findViewById(R.id.button6);
        Button b7 = (Button) findViewById(R.id.button7);
        Button b8 = (Button) findViewById(R.id.button8);
        Button b9 = (Button) findViewById(R.id.button9);
        Button b10 = (Button) findViewById(R.id.button10);
        Button b11 = (Button) findViewById(R.id.button11);
        Button b12 = (Button) findViewById(R.id.button12);




        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b10.setOnClickListener(this);
        b11.setOnClickListener(this);
        b12.setOnClickListener(this);


    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent();

        switch (v.getId()) {
            //handle multiple view click events
            case R.id.button:
                intent.setClass(getApplicationContext(), CalendarActivity.class);
                break;
            case R.id.button2:
                break;
            case R.id.button3:
                break;
            case R.id.button4:
                intent.setClass(getApplicationContext(), BackupRestoreFilesActivity.class);
                intent.putExtra("FROMTOOLSBACKUP", true);
                break;
            case R.id.button5:
                intent.setClass(getApplicationContext(), BackupRestoreFilesActivity.class);
                intent.putExtra("FROMTOOLSRESTORE", true);
                break;
            case R.id.button6:
                break;
            case R.id.button7:
                intent.setClass(getApplicationContext(), PopEmailDocuments.class);
                break;
            case R.id.button8:
                break;
            case R.id.button9:
                break;
            case R.id.button10:
                break;
            case R.id.button11:
                intent.setClass(getApplicationContext(), AddMaintenanceCategory.class);
                break;
            case R.id.button12:
                break;

        }

        intent.putExtra("FROMTOOLS", true);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {


        this.finish();
    }


}


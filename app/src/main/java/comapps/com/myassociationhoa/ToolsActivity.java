package comapps.com.myassociationhoa;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import comapps.com.myassociationhoa.documents.PopEmailDocuments;
import comapps.com.myassociationhoa.tools.AdminMBRecyclerViewActivity;
import comapps.com.myassociationhoa.tools.BackupRestoreFilesActivity;
import comapps.com.myassociationhoa.tools.BuildTextDirectoryActivity;
import comapps.com.myassociationhoa.tools.CalendarManageActivity;
import comapps.com.myassociationhoa.tools.DirectoryUpdateActivity;
import comapps.com.myassociationhoa.tools.EditMaintenanceActivity;
import comapps.com.myassociationhoa.tools.GuestAccessActivity;
import comapps.com.myassociationhoa.tools.ImportActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/22/2016.
 */
@SuppressWarnings("ALL")
public class ToolsActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "TOOLSACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    Context context;

    int FILE_CODE = 0;

    private SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setupWindowAnimations();

        setContentView(R.layout.content_tools);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Tool Box");
        }

        Button bEventCal = (Button) findViewById(R.id.button);
        Button bLoadBudgetExpense = (Button) findViewById(R.id.button2);
        Button bUpdateRoster = (Button) findViewById(R.id.button3);
        Button bBackupAssoc = (Button) findViewById(R.id.button4);
        Button bRestoreAssoc = (Button) findViewById(R.id.button5);
        Button bBuildText = (Button) findViewById(R.id.button6);
        Button bEmailPdf = (Button) findViewById(R.id.button7);

        Button bGuestAccess = (Button) findViewById(R.id.button10);
        Button bEditMaint = (Button) findViewById(R.id.button11);
        Button bAdminMessage = (Button) findViewById(R.id.button12);




        bEventCal.setOnClickListener(this);
        bLoadBudgetExpense.setOnClickListener(this);
        bUpdateRoster.setOnClickListener(this);
        bBackupAssoc.setOnClickListener(this);
        bRestoreAssoc.setOnClickListener(this);
        bBuildText.setOnClickListener(this);
        bEmailPdf.setOnClickListener(this);

        bGuestAccess.setOnClickListener(this);
        bEditMaint.setOnClickListener(this);
        bAdminMessage.setOnClickListener(this);





        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);




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


    @Override
    public void onClick(View v) {

        Intent intent = new Intent();

        int FILE_CODE = 0;

        switch (v.getId()) {
            //handle multiple view click events
            case R.id.button:
                intent.setClass(getApplicationContext(), CalendarManageActivity.class);
                break;

            case R.id.button2:
                intent.setClass(getApplicationContext(), ImportActivity.class);
                break;


            case R.id.button3:
                intent.setClass(getApplicationContext(), DirectoryUpdateActivity.class);
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
                intent.setClass(getApplicationContext(), BuildTextDirectoryActivity.class);
                break;
            case R.id.button7:
                intent.setClass(getApplicationContext(), PopEmailDocuments.class);
                break;
            case R.id.button10:
                intent.setClass(getApplicationContext(), GuestAccessActivity.class);
                break;
            case R.id.button11:
                intent.setClass(getApplicationContext(), EditMaintenanceActivity.class);
                break;
            case R.id.button12:
                intent.setClass(getApplicationContext(), AdminMBRecyclerViewActivity.class);
                break;

        }

        intent.putExtra("FROMTOOLS", true);
        startActivity(intent);

    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));



        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.BOTTOM);
        getWindow().setEnterTransition(slideTransition);


        Slide slideTransitionExit = new Slide();
        slideTransitionExit.setSlideEdge(Gravity.TOP);
        getWindow().setExitTransition(slideTransitionExit);



    }

    @Override
    public void onBackPressed() {

        finishAfterTransition();
    }






}


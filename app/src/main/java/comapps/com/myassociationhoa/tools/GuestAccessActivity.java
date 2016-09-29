package comapps.com.myassociationhoa.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import comapps.com.myassociationhoa.GuideActivity;
import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.GuestAccessObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/22/2016.
 */
public class GuestAccessActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GUESTACCESSACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";


    SharedPreferences sharedPreferences;

    private GuestAccessObject guestAccessObject;
    private ArrayList<GuestAccessObject> guestAccessObjects;

    private Button sendText;
    private Button sendHtml;
    private Button clearGuestAccess;
    ParseQuery query;

    String guestAccessReport = "";
    String guestAccessReportHtml = "";
    String lineSep = System.getProperty("line.separator");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        setContentView(R.layout.content_main_guestaccess);

        sendText = (Button) findViewById(R.id.buttonText);
        sendHtml = (Button) findViewById(R.id.buttonHtml);
        clearGuestAccess = (Button) findViewById(R.id.buttonClear);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle("Guest Access Report");
        }

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        int guestAccessObjectsSize = sharedPreferences.getInt("guestAccessObjectsSize", 0);

        guestAccessObjects = new ArrayList<>();


        guestAccessReportHtml = "<h5>" + sharedPreferences.getString("defaultRecord(1)","") + "</h5><h5>Guest Access Report</h5><table width='200' border='1'><tr>" +
                "<td>Owner Name</td><td>Access Date</td><td>Guest Name</td><<td>Guest Type</td><td>Contact Type</td></tr><tr>" +
                "<td>_________________________</td><td>____________________</td><td>_________________________</td><td>_______________</td>" +
                "<td>_______________</td></tr>";

        for (int i = 0; i < guestAccessObjectsSize; i++) {

            String jsonGuestAccessObject = sharedPreferences.getString("guestAccessObject" + "[" + i + "]", "");
            Gson gson = new Gson();
            guestAccessObject = gson.fromJson(jsonGuestAccessObject, GuestAccessObject.class);


            Log.d(TAG, "guestAccessObject is ----> " + guestAccessObject.toString());
            guestAccessObjects.add(guestAccessObject);

            guestAccessReport = guestAccessReport + guestAccessObject.toString() + lineSep;

            guestAccessReportHtml = guestAccessReportHtml + "<tr><td>" + guestAccessObject.getGuestAccessOwner() + "</td><td>" +
            guestAccessObject.getGuestAccessDate() + "</td><td>" +
            guestAccessObject.getGuestAccessName() + "</td><td>" +
            guestAccessObject.getGuestAccessType() + "</td><td>" +
            guestAccessObject.getGuestAccessContactType() + "</td></tr>";



        }

        guestAccessReportHtml = guestAccessReportHtml + "</table>\n";



        Log.d(TAG, "guestAccessReportText is ----> " + guestAccessReport);
        Log.d(TAG, "guestAccessReportHtml is ----> " + guestAccessReportHtml);


        sendText.setOnClickListener(this);
        sendHtml.setOnClickListener(this);
        clearGuestAccess.setOnClickListener(this);


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

        byte[] data;
        File guestAccessFile = null;
        Intent intent = new Intent();

        switch (v.getId()) {
            //handle multiple view click events
            case R.id.buttonText:

                try {
                    guestAccessFile = new File(getApplicationContext().getExternalFilesDir(null), "guestAccessReport.txt");
                    guestAccessFile.deleteOnExit();
                    FileOutputStream fileoutputstream = null;
                    fileoutputstream = new FileOutputStream(guestAccessFile.getPath());
                    fileoutputstream.write(guestAccessReportHtml.getBytes());
                    fileoutputstream.flush();
                    fileoutputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.d(TAG, "guestAccessFile size is " + guestAccessFile);

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("application/pdf");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    List<ResolveInfo> resInfoList =
                            getPackageManager()
                                    .queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        grantUriPermission(packageName, Uri.fromFile(guestAccessFile),
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }


                //   emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputPdf));
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(guestAccessFile));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Guest Access Html for " + sharedPreferences.getString("defaultRecord(1)", ""));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                break;
            case R.id.buttonHtml:

                try {
                    guestAccessFile = new File(getApplicationContext().getExternalFilesDir(null), "guestAccessReport.html");
                    guestAccessFile.deleteOnExit();
                    FileOutputStream fileoutputstream = null;
                    fileoutputstream = new FileOutputStream(guestAccessFile.getPath());
                    fileoutputstream.write(guestAccessReport.getBytes());
                    fileoutputstream.flush();
                    fileoutputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.d(TAG, "guestAccessFile size is " + guestAccessFile);

                emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("application/pdf");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    List<ResolveInfo> resInfoList =
                            getPackageManager()
                                    .queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        grantUriPermission(packageName, Uri.fromFile(guestAccessFile),
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }


                //   emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputPdf));
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(guestAccessFile));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Guest Access Text for " + sharedPreferences.getString("defaultRecord(1)", ""));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                break;
            case R.id.buttonClear:


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want to clear?");

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(GuestAccessActivity.this, "You clicked yes button", Toast.LENGTH_LONG).show();


                        final ParseInstallation installation = ParseInstallation.getCurrentInstallation();

                        query = new ParseQuery<ParseObject>(installation.getString("AssociationCode"));

                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> assoc, ParseException e) {



                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, H:mm a");
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                                String strDate = sdf.format(c.getTime());




                                String guestAccessFile = "";


                                byte[] data = guestAccessFile.getBytes();
                                ParseFile guestFile = new ParseFile("GuestAccess.txt", data);


                                try {
                                    guestFile.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }


                                assoc.get(0).put("GuestAccessDate", strDate);
                                assoc.get(0).put("GuestAccessFile", guestFile);

                                try {
                                    assoc.get(0).save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                    assoc.get(0).saveEventually();
                                }



                                Toast toast = Toast.makeText(getBaseContext(), "GuestAccessFile cleared.", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();




                                /*Intent mainActivity = new Intent();
                                mainActivity.setClass(getApplicationContext(), MainActivity.class);
                                startActivity(mainActivity);*/
                                finish();


                            }
                        });

                    }
                });







                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        break;
                    }





    }
}

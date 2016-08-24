package comapps.com.myassociationhoa.documents;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import comapps.com.myassociationhoa.MainActivity;
import comapps.com.myassociationhoa.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class PopEmailDocuments extends AppCompatActivity {

    private static final String TAG = "POPEMAILACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";

    ParseInstallation installation;
    String assocCode;
    SharedPreferences sharedPreferences;

    ParseQuery queryAssociations;

    TextView title;
    TextView title2;
    TextView rulesPdf;
    TextView byLawsPdf;
    TextView minutesPdf;
    TextView misc1;
    TextView misc2;
    TextView misc3;
    TextView cancel;


    ParseFile parseFile;
    URI uri = null;
    File pdfToEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_documents);

        title = (TextView) findViewById(R.id.textViewTitle);
        title2 = (TextView) findViewById(R.id.textViewTitle2);
        rulesPdf = (TextView) findViewById(R.id.textViewRules);
        byLawsPdf = (TextView) findViewById(R.id.textViewByLaws);
        minutesPdf = (TextView) findViewById(R.id.textViewMinutes);
        misc1 = (TextView) findViewById(R.id.textViewMisc1Pdf);
        misc2 = (TextView) findViewById(R.id.textViewMisc2Pdf);
        misc3 = (TextView) findViewById(R.id.textViewMisc3Pdf);
        cancel = (TextView) findViewById(R.id.textViewCancel);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);



        title.setText(sharedPreferences.getString("defaultRecord(1)", ""));
        title2.setText("E-Mail HOA Documents");

        if ( sharedPreferences.getString("rulespdfurl", null) == null ) {
            misc1.setVisibility(View.GONE);
        }

        if ( sharedPreferences.getString("bylawspdfurl", null) == null ) {
            misc2.setVisibility(View.GONE);
        }

        if ( sharedPreferences.getString("minutespdfurl", null) == null ) {
            misc3.setVisibility(View.GONE);
        }

        if ( sharedPreferences.getString("m1pdfurl", null) == null ) {
            misc1.setVisibility(View.GONE);
        }

        if ( sharedPreferences.getString("m2pdfurl", null) == null ) {
            misc2.setVisibility(View.GONE);
        }

        if ( sharedPreferences.getString("m3pdfurl", null) == null ) {
            misc3.setVisibility(View.GONE);
        }



        DisplayMetrics dm =  new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.7));


        installation = ParseInstallation.getCurrentInstallation();

        queryAssociations = new ParseQuery<>(installation.getString("AssociationCode"));





        queryAssociations.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> associationObject, ParseException e) {


                final ParseFile byLawsFile = associationObject.get(0).getParseFile("ByLawsFile");
                final ParseFile rulesFile = associationObject.get(0).getParseFile("RulesFile");
                final ParseFile minutesFile = associationObject.get(0).getParseFile("MinutesFile");
                final ParseFile misc1File = associationObject.get(0).getParseFile("MiscDoc1File");
                final ParseFile misc2File = associationObject.get(0).getParseFile("MiscDoc2File");
                final ParseFile misc3File = associationObject.get(0).getParseFile("MiscDoc3File");


                rulesPdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Shake)
                                .duration(0)
                                .playOn(v);



                        rulesFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                Log.d(TAG, "data size is " + data.length);


                                try {
                                    pdfToEmail = new File (getApplicationContext().getExternalFilesDir(null), "rules.pdf");
                                    pdfToEmail.deleteOnExit();
                                    FileOutputStream fileoutputstream = new FileOutputStream(pdfToEmail.getPath());
                                    fileoutputstream.write(data);
                                    fileoutputstream.flush();
                                    fileoutputstream.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }


                                Log.d(TAG, "outputPdf size is " + pdfToEmail.length());

                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("application/pdf");

                                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                }
                                else {
                                    List<ResolveInfo> resInfoList=
                                            getPackageManager()
                                                    .queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                                    for (ResolveInfo resolveInfo : resInfoList) {
                                        String packageName = resolveInfo.activityInfo.packageName;
                                        grantUriPermission(packageName, Uri.fromFile(pdfToEmail),
                                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                }


                             //   emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputPdf));
                                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfToEmail));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-Mail Rules Documents");
                                startActivity(Intent.createChooser(emailIntent , "Send email..."));

                            }
                        });







                    }
                });

                byLawsPdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Shake)
                                .duration(0)
                                .playOn(v);

                        byLawsFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                Log.d(TAG, "data size is " + data.length);


                                try {
                                    pdfToEmail = new File (getApplicationContext().getExternalFilesDir(null), "bylaws.pdf");
                                    pdfToEmail.deleteOnExit();
                                    FileOutputStream fileoutputstream = new FileOutputStream(pdfToEmail.getPath());
                                    fileoutputstream.write(data);
                                    fileoutputstream.flush();
                                    fileoutputstream.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }


                                Log.d(TAG, "outputPdf size is " + pdfToEmail.length());

                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("application/pdf");

                                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                }
                                else {
                                    List<ResolveInfo> resInfoList=
                                            getPackageManager()
                                                    .queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                                    for (ResolveInfo resolveInfo : resInfoList) {
                                        String packageName = resolveInfo.activityInfo.packageName;
                                        grantUriPermission(packageName, Uri.fromFile(pdfToEmail),
                                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                }


                                //   emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputPdf));
                                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfToEmail));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-Mail ByLaws Documents");
                                startActivity(Intent.createChooser(emailIntent , "Send email..."));

                            }
                        });


                    }
                });

                minutesPdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Shake)
                                .duration(0)
                                .playOn(v);

                        minutesFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                Log.d(TAG, "data size is " + data.length);


                                try {
                                    pdfToEmail = new File (getApplicationContext().getExternalFilesDir(null), "minutes.pdf");
                                    pdfToEmail.deleteOnExit();
                                    FileOutputStream fileoutputstream = new FileOutputStream(pdfToEmail.getPath());
                                    fileoutputstream.write(data);
                                    fileoutputstream.flush();
                                    fileoutputstream.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }


                                Log.d(TAG, "outputPdf size is " + pdfToEmail.length());

                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("application/pdf");

                                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                }
                                else {
                                    List<ResolveInfo> resInfoList=
                                            getPackageManager()
                                                    .queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                                    for (ResolveInfo resolveInfo : resInfoList) {
                                        String packageName = resolveInfo.activityInfo.packageName;
                                        grantUriPermission(packageName, Uri.fromFile(pdfToEmail),
                                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                }


                                //   emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputPdf));
                                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfToEmail));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-Mail Minutes Documents");
                                startActivity(Intent.createChooser(emailIntent , "Send email..."));

                            }
                        });


                    }
                });

                misc1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Shake)
                                .duration(0)
                                .playOn(v);

                        misc1File.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                Log.d(TAG, "data size is " + data.length);


                                try {
                                    pdfToEmail = new File (getApplicationContext().getExternalFilesDir(null), "misc1.pdf");
                                    pdfToEmail.deleteOnExit();
                                    FileOutputStream fileoutputstream = new FileOutputStream(pdfToEmail.getPath());
                                    fileoutputstream.write(data);
                                    fileoutputstream.flush();
                                    fileoutputstream.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }


                                Log.d(TAG, "outputPdf size is " + pdfToEmail.length());

                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("application/pdf");

                                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                }
                                else {
                                    List<ResolveInfo> resInfoList=
                                            getPackageManager()
                                                    .queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                                    for (ResolveInfo resolveInfo : resInfoList) {
                                        String packageName = resolveInfo.activityInfo.packageName;
                                        grantUriPermission(packageName, Uri.fromFile(pdfToEmail),
                                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                }


                                //   emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputPdf));
                                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfToEmail));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-Mail Misc1 Documents");
                                startActivity(Intent.createChooser(emailIntent , "Send email..."));

                            }
                        });

                    }
                });

                misc2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Shake)
                                .duration(0)
                                .playOn(v);

                        misc2File.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                Log.d(TAG, "data size is " + data.length);


                                try {
                                    pdfToEmail = new File (getApplicationContext().getExternalFilesDir(null), "misc2.pdf");
                                    pdfToEmail.deleteOnExit();
                                    FileOutputStream fileoutputstream = new FileOutputStream(pdfToEmail.getPath());
                                    fileoutputstream.write(data);
                                    fileoutputstream.flush();
                                    fileoutputstream.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }


                                Log.d(TAG, "outputPdf size is " + pdfToEmail.length());

                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("application/pdf");

                                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                }
                                else {
                                    List<ResolveInfo> resInfoList=
                                            getPackageManager()
                                                    .queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                                    for (ResolveInfo resolveInfo : resInfoList) {
                                        String packageName = resolveInfo.activityInfo.packageName;
                                        grantUriPermission(packageName, Uri.fromFile(pdfToEmail),
                                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                }


                                //   emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputPdf));
                                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfToEmail));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-Mail Misc2 Documents");
                                startActivity(Intent.createChooser(emailIntent , "Send email..."));

                            }
                        });

                    }
                });

                misc3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Shake)
                                .duration(0)
                                .playOn(v);

                        misc3File.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                Log.d(TAG, "data size is " + data.length);


                                try {
                                    pdfToEmail = new File (getApplicationContext().getExternalFilesDir(null), "misc3.pdf");
                                    pdfToEmail.deleteOnExit();
                                    FileOutputStream fileoutputstream = new FileOutputStream(pdfToEmail.getPath());
                                    fileoutputstream.write(data);
                                    fileoutputstream.flush();
                                    fileoutputstream.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }


                                Log.d(TAG, "outputPdf size is " + pdfToEmail.length());

                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("application/pdf");

                                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
                                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                }
                                else {
                                    List<ResolveInfo> resInfoList=
                                            getPackageManager()
                                                    .queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);

                                    for (ResolveInfo resolveInfo : resInfoList) {
                                        String packageName = resolveInfo.activityInfo.packageName;
                                        grantUriPermission(packageName, Uri.fromFile(pdfToEmail),
                                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                }


                                //   emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputPdf));
                                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfToEmail));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-Mail Misc3 Documents");
                                startActivity(Intent.createChooser(emailIntent , "Send email..."));

                            }
                        });


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


        });




    }



    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {

        Intent intentMain = new Intent();
        intentMain.setClass(PopEmailDocuments.this, MainActivity.class);
        PopEmailDocuments.this.finish();
        startActivity(intentMain);


    }


}


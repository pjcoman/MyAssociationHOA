package comapps.com.myassociationhoa.tools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import comapps.com.myassociationhoa.MainActivity;
import comapps.com.myassociationhoa.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/28/2016.
 */
public class ImportActivity extends AppCompatActivity {

    private static final String TAG = "IMPORTBUDGETACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    private static final int DIALOG_UPLOAD_PROGRESS = 0;

    private ProgressDialog mProgressDialog;

    private SharedPreferences sharedPreferences;

    private ParseInstallation installation;
    private ParseQuery query;
    private ParseFile parseFile;
    private AlertDialog.Builder alertDialog;

    String fileNameMisc;
    private String pdfFile;
    private String parseColumn;

    private TextView tvBudget;
    private TextView tvExpenses;
    private TextView tvRules;
    private TextView tvByLaws;
    private TextView tvMinutes;
    private TextView tvMisc1;
    private TextView tvMisc2;
    private TextView tvMisc3;
    private TextView tvCancel;


    private File pdfToLoad;
    private InputStream is;
    private OutputStream os;
    private ParseObject object;

    int FILE_CODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_import);

        TextView title = (TextView) findViewById(R.id.textViewTitle);
        TextView title2 = (TextView) findViewById(R.id.textViewTitle2);
        tvBudget = (TextView) findViewById(R.id.textViewBudget);
        tvExpenses = (TextView) findViewById(R.id.textViewExpenses);
        tvRules = (TextView) findViewById(R.id.textViewRules);
        tvByLaws = (TextView) findViewById(R.id.textViewByLaws);
        tvMinutes = (TextView) findViewById(R.id.textViewMinutes);
        tvMisc1 = (TextView) findViewById(R.id.textViewMisc1);
        tvMisc2 = (TextView) findViewById(R.id.textViewMisc2);
        tvMisc3 = (TextView) findViewById(R.id.textViewMisc3);
        tvCancel = (TextView) findViewById(R.id.textViewCancel);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);



        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if (bar != null) {


            bar.setTitle("Load HOA Documents");

        }


        if (title != null) {
            title.setText(sharedPreferences.getString("defaultRecord(1)", ""));
        }

        title2.setText("Load Documents");

      /*  DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

*/
        final Intent intent = getIntent();
        final Uri uri = intent.getData();
        final ContentResolver cr = getContentResolver();

        if (uri != null) {

            installation = ParseInstallation.getCurrentInstallation();
            query = new ParseQuery<>(installation.getString("AssociationCode"));

            tvMisc1.setText(sharedPreferences.getString("defaultRecord(31)",""));
            if ( tvMisc1.getText() == "" ) {
                tvMisc1.setVisibility(View.GONE);
            }
            tvMisc2.setText(sharedPreferences.getString("defaultRecord(32)",""));
            if ( tvMisc2.getText() == "" ) {
                tvMisc2.setVisibility(View.GONE);
            }
            tvMisc3.setText(sharedPreferences.getString("defaultRecord(33)",""));
            if ( tvMisc3.getText() == "" ) {
                tvMisc3.setVisibility(View.GONE);
            }





            try {
                object = query.getFirst();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                is = cr.openInputStream(uri);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }


            tvBudget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                  pdfFile = "Budget.pdf";
                  parseColumn = "BudgetFile";

                  new UploadFileAsync().execute(pdfFile, parseColumn);

                }


            });

            tvExpenses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pdfFile = "Expense.pdf";
                    parseColumn = "ExpenseFile";

                    new UploadFileAsync().execute(pdfFile, parseColumn);


                }
            });

            tvRules.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pdfFile = "Rules.pdf";
                    parseColumn = "RulesFile";

                    new UploadFileAsync().execute(pdfFile, parseColumn);



                }
            });

            tvByLaws.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pdfFile = "ByLaws.pdf";
                    parseColumn = "ByLawsFile";

                    new UploadFileAsync().execute(pdfFile, parseColumn);


                }
            });

            tvMinutes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pdfFile = "Minutes.pdf";
                    parseColumn = "MinutesFile";

                    new UploadFileAsync().execute(pdfFile, parseColumn);


                }
            });

            tvMisc1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pdfFile = sharedPreferences.getString("defaultRecord(31)", "") + ".pdf";
                    parseColumn = "MiscDoc1File";

                    new UploadFileAsync().execute(pdfFile, parseColumn);


                }
            });

            tvMisc2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pdfFile = sharedPreferences.getString("defaultRecord(32)", "") + ".pdf";
                    parseColumn = "MiscDoc2File";

                    new UploadFileAsync().execute(pdfFile, parseColumn);



                }
            });

            tvMisc3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pdfFile = sharedPreferences.getString("defaultRecord(33)", "") + ".pdf";
                    parseColumn = "MiscDoc3File";

                    new UploadFileAsync().execute(pdfFile, parseColumn);




                }
            });



            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    finish();

                }
            });

        } else {

            Toast toast = Toast.makeText(getBaseContext(), "Open attachment from email and choose MyAssociation to open.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            toast = Toast.makeText(getBaseContext(), "Or save attachment to phone (or computer drive your phone has access to) and choose" +
                    " MyAssociation to open from File Manager/Browser/Explorer app.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();





            finish();


        }






        }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_UPLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Uploading pdf file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }






    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {

      this.finish();

    }


    //************************************************************************************************************************************************



    private class UploadFileAsync extends AsyncTask<String, String, String> {

        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_UPLOAD_PROGRESS);

            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(ImportActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            int count;

                        try {
                            pdfToLoad = new File(getApplicationContext().getExternalFilesDir(null), params[0]);
                            pdfToLoad.deleteOnExit();
                            os = new FileOutputStream(pdfToLoad.getPath());

                            int read = 0;
                            byte[] bytes = new byte[1024];

                            while ((read = is.read(bytes)) != -1) {
                                os.write(bytes, 0, read);
                            }

                            System.out.println("Done!");

                            byte[] bFile = new byte[(int) pdfToLoad.length()];
                            FileInputStream fileInputStream = new FileInputStream(pdfToLoad);
                            fileInputStream.read(bFile);
                            fileInputStream.close();

                            parseFile = new ParseFile(params[0], bFile);

                            os.flush();
                            os.close();


                            Log.d(TAG, "outputPdf size is " + pdfToLoad.length());


                            parseFile.save();

                        } catch (IOException | ParseException e) {
                            e.printStackTrace();

                        }

            object.put(params[1], parseFile);



            object.saveEventually();

            return null;

        }

        protected void onProgressUpdate(String... progress) {
            Log.d(TAG, "upload progress is ----> " + progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_UPLOAD_PROGRESS);

            {
                            Intent mainActivity = new Intent();
                            mainActivity.setClass(ImportActivity.this, MainActivity.class);
                            mainActivity.putExtra("FROMIMPORTDOCS", true);
                            mainActivity.putExtra("FILEUPLOADED", pdfFile);
                            startActivity(mainActivity);

                            finish();
                        }

        }
    }

}











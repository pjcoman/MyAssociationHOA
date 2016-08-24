package comapps.com.myassociationhoa.budget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import comapps.com.myassociationhoa.R;

/**
 * Created by me on 6/28/2016.
 */
public class BudgetReaderActivity extends AppCompatActivity {

    private static final String TAG = "BUDGETREADERACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    Context context = this;
    SharedPreferences sharedPreferences;

    String pdfURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main_pdf_reader);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        String pdfChoice = getIntent().getStringExtra("file");


        WebView webView = new WebView(BudgetReaderActivity.this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
        webView.setWebViewClient(new Callback());

        switch (pdfChoice) {
            case "budget":
                pdfURL = sharedPreferences.getString("budgetpdfurl", "");
                break;
            case "expense":
                pdfURL = sharedPreferences.getString("expensepdfurl", "");
                break;

        }


        webView.loadUrl(
                "http://docs.google.com/gview?embedded=true&url=" + pdfURL);

        setContentView(webView);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }

    @Override
    public void onBackPressed() {

        Intent intentPopBudget = new Intent();
        intentPopBudget.setClass(BudgetReaderActivity.this, PopBudget.class);
        this.finish();
        startActivity(intentPopBudget);

    }


}

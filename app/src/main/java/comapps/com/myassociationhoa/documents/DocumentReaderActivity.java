package comapps.com.myassociationhoa.documents;

import android.content.Context;
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
public class DocumentReaderActivity extends AppCompatActivity {

    private static final String TAG = "DOCUMENTREADERACTIVITY";
    private static final String MYPREFERENCES = "MyPrefs";

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main_pdf_reader);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        String pdfChoice = getIntent().getStringExtra("file");

        WebView webView = new WebView(DocumentReaderActivity.this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
        webView.setWebViewClient(new Callback());

        String pdfURL = "";

        switch (pdfChoice) {
            case "rules":
                pdfURL = sharedPreferences.getString("rulespdfurl", "");
                break;
            case "bylaws":
                pdfURL = sharedPreferences.getString("bylawspdfurl", "");
                break;
            case "minutes":
                pdfURL = sharedPreferences.getString("minutespdfurl", "");
                break;
            case "misc1":
                pdfURL = sharedPreferences.getString("m1pdfurl", "");
                break;
            case "misc2":
                pdfURL = sharedPreferences.getString("m2pdfurl", "");
                break;
            case "misc3":
                pdfURL = sharedPreferences.getString("m3pdfurl", "");
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


}

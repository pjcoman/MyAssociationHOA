package comapps.com.myassociationhoa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by me on 6/14/2016.
 */
@SuppressWarnings("ALL")
public class GuideActivity extends Activity
        {


            @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .setDefaultFontPath("fonts/MerriweatherSans-Italic.ttf")
        .setFontAttrId(R.attr.fontPath)
        .build());

        setContentView(R.layout.guidelayout);

                Button closeButton = (Button) findViewById(R.id.guideCloseButton);

       closeButton.setOnClickListener(
               new Button.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                               finish();

                       }
               }
       );



//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int)(width*1),(int)(height*1));
//        getWindow().setGravity(Gravity.CENTER_VERTICAL);
//        getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
//
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);



       /* sv = (ScrollView) findViewById(R.id.scrollView);
        sv.fullScroll(View.FOCUS_DOWN);*/
                TextView guide = (TextView) findViewById(R.id.textViewGuide);
        guide.setMovementMethod(new ScrollingMovementMethod());
        guide.setVerticalScrollBarEnabled(true);
        guide.setVerticalFadingEdgeEnabled(false);



        }

@Override
protected void attachBaseContext (Context newBase){

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

        }



        }









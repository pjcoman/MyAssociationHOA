package comapps.com.myassociationhoa.budget;

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
public class PopBudget extends AppCompatActivity {

    private static final String TAG = "POPBUDGETUPACTIVITY";
    public static final String MYPREFERENCES = "MyPrefs";

    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/palabi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.pop_up_layout_budget);

        TextView title = (TextView) findViewById(R.id.textViewTitle);

        TextView budgetPdf = (TextView) findViewById(R.id.textViewBudget);
        TextView expensesPdf = (TextView) findViewById(R.id.textViewExpenses);
        TextView cancel = (TextView) findViewById(R.id.textViewCancel);

        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


        if (title != null) {
            title.setText(sharedPreferences.getString("defaultRecord(1)", ""));
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .5));


        budgetPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentBudget = new Intent();
                intentBudget.setClass(PopBudget.this, BudgetReaderActivity.class);
                intentBudget.putExtra("file", "budget");
                startActivity(intentBudget);
                finish();

            }
        });

        expensesPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(v);

                Intent intentExpenses = new Intent();
                intentExpenses.setClass(PopBudget.this, BudgetReaderActivity.class);
                intentExpenses.putExtra("file", "expense");
                startActivity(intentExpenses);
                finish();

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
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



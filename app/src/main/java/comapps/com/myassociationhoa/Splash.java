package comapps.com.myassociationhoa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by me on 8/13/2016.
 */
@SuppressWarnings("ALL")
public class Splash extends Activity implements Animation.AnimationListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Animation a = AnimationUtils
                .loadAnimation(this, R.anim.startanimation);
        a.setAnimationListener(this);

        ImageView iv = (ImageView) findViewById(R.id.enter);
        //	iv.clearAnimation();
        iv.startAnimation(a);


    }

    @Override
    public void onAnimationEnd(Animation animation) {



        //  overridePendingTransition(R.anim.fadeinanimation, 0);

        //  Start2.this.overridePendingTransition(R.anim.fadeinanimation, 0);



        Intent mainIntent = new Intent().setClass(Splash.this,
                MainActivity.class);

        startActivity(mainIntent);

        finish();

        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

}

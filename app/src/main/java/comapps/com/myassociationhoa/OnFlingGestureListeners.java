package comapps.com.myassociationhoa;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by me on 10/27/2016.
 */

@SuppressWarnings("ALL")
public abstract class OnFlingGestureListeners implements View.OnTouchListener {

    private final GestureDetector gdt = new GestureDetector(new GestureListener());

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        gdt.onTouchEvent(event);
        return true;
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 10;
        private static final int SWIPE_THRESHOLD_VELOCITY = 10;
        private static final int SWIPE_MAX_OFF_PATH = 250;


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onRightToLeft();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onLeftToRight();
                return true;
            }
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                onBottomToTop();
                return true;
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                onTopToBottom();
                return true;
            }

            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            onTouchDown();
            return true;
        }


    }

    public abstract void onRightToLeft();

    public abstract void onLeftToRight();

    public abstract void onBottomToTop();

    public abstract void onTopToBottom();

    public abstract void onTouchDown();
}
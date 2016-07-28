package comapps.com.myassociationhoa;

/**
 * Created by me on 7/16/2016.
 */


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

public class AsyncTaskExecutor<Params, Progress, Result> {

    @SuppressLint("NewApi")
    public AsyncTask<Params, Progress, Result> execute(final AsyncTask<Params, Progress, Result> asyncTask, final Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            return asyncTask.execute(params);
        }
    }

}
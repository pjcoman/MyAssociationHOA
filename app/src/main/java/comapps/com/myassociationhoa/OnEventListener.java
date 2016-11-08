package comapps.com.myassociationhoa;

/**
 * Created by me on 10/4/2016.
 */


    @SuppressWarnings("ALL")
    public interface OnEventListener<T> {
        void onSuccess();
        void onFailure(Exception e);
    }


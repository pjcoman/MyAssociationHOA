package comapps.com.myassociationhoa;

/**
 * Created by me on 10/4/2016.
 */


    public interface OnEventListener<T> {
        void onSuccess();
        void onFailure(Exception e);
    }


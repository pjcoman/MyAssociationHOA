package comapps.com.myassociationhoa.push_history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.PushObject;

/**
 * Created by me on 6/28/2016.
 */
class PushAdapter extends ArrayAdapter<PushObject> {

    public static final String TAG = "PUSHADAPTER";


    public PushAdapter(Context context, ArrayList<PushObject> notifications) {
        super(context, 0, notifications);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        PushObject pushObject = getItem(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main_push_row, parent, false);

        }

        TextView date = (TextView) convertView.findViewById(R.id.textViewPushDate);
        TextView notification = (TextView) convertView.findViewById(R.id.textViewPushNotification);


        date.setText(pushObject.getDate());
        notification.setText(pushObject.getPushNotifacation());


        return convertView;
    }


}



package comapps.com.myassociationhoa.maintenance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.MaintenanceObject;

/**
 * Created by me on 6/28/2016.
 */
class MaintenanceAdapter extends ArrayAdapter<MaintenanceObject> {

    public static final String TAG = "MAINTENANCEADAPTER";


    public MaintenanceAdapter(Context context, ArrayList<MaintenanceObject> maintenanceItems) {
        super(context, 0, maintenanceItems);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MaintenanceObject maintenanceObject = getItem(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main_maintenance_row, parent, false);

        }

        TextView maintName = (TextView) convertView.findViewById(R.id.textViewMaintName);
        TextView maintNotes = (TextView) convertView.findViewById(R.id.textViewMaintNotes);
        TextView maintDate = (TextView) convertView.findViewById(R.id.textViewMaintDate);
        TextView maintDesc = (TextView) convertView.findViewById(R.id.textViewMaintDesc);
        TextView maintCat = (TextView) convertView.findViewById(R.id.textViewMaintCat);
        TextView maintNumber = (TextView) convertView.findViewById(R.id.textViewMaintNumber);

        Button editDesc = (Button) convertView.findViewById(R.id.editButton);


        try {
            maintName.setText(maintenanceObject.getMaintenanceName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            maintNotes.setText(maintenanceObject.getMaintenanceNotes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            maintDate.setText(maintenanceObject.getMaintenanceDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            maintDesc.setText(maintenanceObject.getMaintenanceDesc());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            maintCat.setText(maintenanceObject.getMaintenanceCategory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            maintNumber.setText(maintenanceObject.getMaintenanceNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }


        editDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                String jsonMaintenanceObject = gson.toJson(maintenanceObject);

                Intent intentEditMaintenanceNotes = new Intent();
                intentEditMaintenanceNotes.setClass(getContext(), MaintenanceComment.class);
                intentEditMaintenanceNotes.putExtra("MAINTENANCEINDEX", position);
                intentEditMaintenanceNotes.putExtra("MAINTENANCEOBJECT", jsonMaintenanceObject);
                getContext().startActivity(intentEditMaintenanceNotes);

            }
        });


        return convertView;
    }


}



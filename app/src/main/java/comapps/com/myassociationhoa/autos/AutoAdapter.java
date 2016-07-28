package comapps.com.myassociationhoa.autos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.AutoObject;

/**
 * Created by me on 6/28/2016.
 */
class AutoAdapter extends ArrayAdapter<AutoObject> implements Filterable {

    public static final String TAG = "AUTOADAPTER";

    private ArrayList<AutoObject> autosList;
    private Context context;
    private AutoFilter autoFilter;
    private ArrayList<AutoObject> autosFilterList;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public AutoAdapter(Context context, ArrayList<AutoObject> autosList) {
        super(context, R.layout.content_main_autos_row, autosList);
        this.autosList = autosList;
        this.context = context;
        autosFilterList = autosList;

    }

    public int getCount() {
        return autosList.size();
    }

    public AutoObject getItem(int position) {
        return autosList.get(position);
    }

    public long getItemId(int position) {
        return autosList.indexOf(getItem(position));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        AutoObject autoObject = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.content_main_autos_row, parent, false);

        }

        TextView ownerName = (TextView) convertView.findViewById(R.id.autoOwner);
        TextView autoMake = (TextView) convertView.findViewById(R.id.autoMake);
        TextView autoModel = (TextView) convertView.findViewById(R.id.autoModel);
        TextView autoColor = (TextView) convertView.findViewById(R.id.autoColor);
        TextView autoPlate = (TextView) convertView.findViewById(R.id.autoPLate);

        AutoObject auto = autosList.get(position);


        try {
            ownerName.setText(autoObject.getOwner());
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            autoMake.setText(autoObject.getMake());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            autoModel.setText(autoObject.getModel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            autoColor.setText(autoObject.getColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            autoPlate.setText(autoObject.getPlate());
        } catch (Exception e) {
            e.printStackTrace();
        }



        return convertView;
    }



    @Override
    public Filter getFilter() {
        if (autoFilter == null) {
            autoFilter = new AutoFilter();
        }
        return autoFilter;
    }

    private class AutoFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() < 1) {

                results.count = autosFilterList.size();
                results.values = autosFilterList;

            } else {

                String searchString = constraint.toString().toUpperCase();
                ArrayList filterList = new ArrayList();

                for (AutoObject auto : autosFilterList) {


                    if ( (auto.getMake().toUpperCase()).contains(searchString) || (auto.getModel().toUpperCase()).contains(searchString) ||
                            (auto.getPlate().toUpperCase()).contains(searchString) ) {
                        filterList.add(auto);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;


            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            autosList = (ArrayList<AutoObject>) results.values;
            notifyDataSetChanged();
        }

    }

}

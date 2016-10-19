package comapps.com.myassociationhoa.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.myinfo.PersonalInfoActivity;
import comapps.com.myassociationhoa.objects.RosterObject;

/**
 * Created by me on 6/28/2016.
 */
class DirectoryUpdateAdapter extends ArrayAdapter<RosterObject> implements Filterable {

    private static final String TAG = "DIRECTORYADAPTER";
    private static final String MYPREFERENCES = "MyPrefs";




    private ArrayList<RosterObject> rosterList;
    private final Context context;
    private RosterFilter rosterFilter;
    private final ArrayList<RosterObject> rosterFilterList;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ScrollView memberInfo;

    private TextView textViewLastName;
    private TextView textViewFirstName;
    private TextView textViewCreateDateAndMN;
    private TextView textViewMiddleName;



    public DirectoryUpdateAdapter(Context context, ArrayList<RosterObject> rosterList) {
        super(context, R.layout.content_main_directory_row, rosterList);
        this.rosterList = rosterList;
        this.context = context;
        rosterFilterList = rosterList;

    }

    public int getCount() {
        return rosterList.size();
    }

    public RosterObject getItem(int position) {
        return rosterList.get(position);
    }

    public long getItemId(int position) {
        return rosterList.indexOf(getItem(position));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final RosterObject rosterObject = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.content_main_directory_row_adminupdate, parent, false);

        }

        memberInfo = (ScrollView) convertView.findViewById(R.id.scrollViewMemberInfo);

        textViewLastName = (TextView) convertView.findViewById(R.id.lastNameTextView);
        textViewFirstName = (TextView) convertView.findViewById(R.id.firstNameTextView);
        textViewMiddleName = (TextView) convertView.findViewById(R.id.middleNameTextView);
        textViewCreateDateAndMN = (TextView) convertView.findViewById(R.id.createDatePlusMemberNumber);



        final RosterObject roster = rosterList.get(position);

        textViewLastName.setText(rosterObject != null ? rosterObject.getLastName() : null);
        textViewFirstName.setText(rosterObject.getFirstName());
        textViewCreateDateAndMN.setText(rosterObject.getActivationDate() + " " + rosterObject.getMemberNumber());
        textViewMiddleName.setText(rosterObject.getMiddleName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Position " + position);
                Log.d(TAG, "Original Position " + roster.getNumber());

                Gson gson = new Gson();
                String jsonRosterObjectForUpdate = gson.toJson(rosterObject); // myObject - instance of MyObject

                Intent intentAdminUpdate = new Intent();
                intentAdminUpdate.setClass(getContext().getApplicationContext(), PersonalInfoActivity.class);
                intentAdminUpdate.putExtra("ROSTEROBJECTTOUPDATE", jsonRosterObjectForUpdate);
                intentAdminUpdate.putExtra("FROMADMINUPDATE", true);
                intentAdminUpdate.putExtra("ROSTERPOSITION", position);
                getContext().startActivity(intentAdminUpdate);

                ((DirectoryUpdateActivity) context).finish();

            }
        });



        return convertView;
    }





    @Override
    public Filter getFilter() {
        if (rosterFilter == null) {
            rosterFilter = new RosterFilter();
        }
        return rosterFilter;
    }

    private class RosterFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() < 1) {

                results.count = rosterFilterList.size();
                results.values = rosterFilterList;

            } else {

                String searchString = constraint.toString().toUpperCase();
                ArrayList filterList = new ArrayList();

                for (RosterObject rosterObject : rosterFilterList) {


                    if ( (rosterObject.getLastName().toUpperCase()).contains(searchString) || (rosterObject.getFirstName().toUpperCase()).contains(searchString)) {
                        filterList.add(rosterObject);
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
            rosterList = (ArrayList<RosterObject>) results.values;
            notifyDataSetChanged();
        }

    }

}

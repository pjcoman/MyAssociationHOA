package comapps.com.myassociationhoa.pets;

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
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.PetObject;

/**
 * Created by me on 6/28/2016.
 */
@SuppressWarnings("ALL")
class PetsMyInfoAdapter extends ArrayAdapter<PetObject> implements Filterable {

    private static final String TAG = "PETSADAPTER";

    private ArrayList<PetObject> petsList;
    private final Context context;
    private PetFilter petFilter;
    private final ArrayList<PetObject> petsFilterList;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public PetsMyInfoAdapter(Context context, ArrayList<PetObject> petsList) {
        super(context, R.layout.content_main_pets_row, petsList);
        this.petsList = petsList;
        this.context = context;
        petsFilterList = petsList;

    }

    public int getCount() {
        return petsList.size();
    }

    public PetObject getItem(int position) {
        return petsList.get(position);
    }

    public long getItemId(int position) {
        return petsList.indexOf(getItem(position));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final PetObject petObject = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.content_main_pets_row, parent, false);

        }

        TextView ownerName = (TextView) convertView.findViewById(R.id.petOwner);
        TextView petType = (TextView) convertView.findViewById(R.id.tvGuestName);
        TextView petName = (TextView) convertView.findViewById(R.id.petName);
        TextView petBreed = (TextView) convertView.findViewById(R.id.tvGuestContactType);
        TextView petColor = (TextView) convertView.findViewById(R.id.tvGuestStartDate);
        TextView petWeight = (TextView) convertView.findViewById(R.id.tvGuestEndDate);
        TextView petMisc = (TextView) convertView.findViewById(R.id.tvGuestAccessMonday);

        PetObject pet = petsList.get(position);


        try {
            ownerName.setText(petObject != null ? petObject.getOwner() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            petType.setText(petObject != null ? petObject.getType() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            petName.setText(petObject != null ? petObject.getName() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            petBreed.setText(petObject != null ? petObject.getBreed() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            petColor.setText(petObject != null ? petObject.getColor() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            petWeight.setText((petObject != null ? petObject.getWeight() : null) + " lbs.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            petMisc.setText(petObject != null ? petObject.getMisc() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "listview clicked " + position);

                Gson gson = new Gson();


                String jsonPetObject = gson.toJson(petObject);

                Intent petAddActivity = new Intent();
                petAddActivity.setClass(context.getApplicationContext(), PopPetsAddPet.class);
                petAddActivity.putExtra("PETFOREDIT", jsonPetObject);
                petAddActivity.putExtra("FOREDIT", true);
                context.startActivity(petAddActivity);
                ((Activity)context).finish();





            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d(TAG, "listview long clicked" + position);

                Gson gson = new Gson();


                String jsonPetObject = gson.toJson(petObject);

                Intent petAddActivity = new Intent();
                petAddActivity.setClass(context.getApplicationContext(), PopPetsAddPet.class);
                petAddActivity.putExtra("PETFOREDIT", jsonPetObject);
                petAddActivity.putExtra("FORDELETE", true);
                context.startActivity(petAddActivity);
                ((Activity)context).finish();


                return false;
            }
        });


        return convertView;


    }



    @Override
    public Filter getFilter() {
        if (petFilter == null) {
            petFilter = new PetFilter();
        }
        return petFilter;
    }

    private class PetFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() < 1) {

                results.count = petsFilterList.size();
                results.values = petsFilterList;

            } else {

                String searchString = constraint.toString().toUpperCase();
                ArrayList filterList = new ArrayList();

                for (PetObject pet : petsFilterList) {


                    if ( (pet.getType().toUpperCase()).contains(searchString) || (pet.getColor().toUpperCase()).contains(searchString)) {
                        filterList.add(pet);
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
            petsList = (ArrayList<PetObject>) results.values;
            notifyDataSetChanged();
        }

    }

}

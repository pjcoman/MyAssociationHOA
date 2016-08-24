package comapps.com.myassociationhoa.guests;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.GuestObject;

/**
 * Created by me on 6/28/2016.
 */
class GuestsAdapter extends ArrayAdapter<GuestObject> implements Filterable {

    public static final String TAG = "GUESTSADAPTER";

    ParseInstallation installation;
    ParseQuery query;

    private ArrayList<GuestObject> guestsList;
    private Context context;
    private GuestFilter guestFilter;
    private ArrayList<GuestObject> guestsFilterList;

    Button recordAccess;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public GuestsAdapter(Context context, ArrayList<GuestObject> guestsList) {
        super(context, R.layout.content_main_guests_row, guestsList);
        this.guestsList = guestsList;
        this.context = context;
        guestsFilterList = guestsList;

        installation = ParseInstallation.getCurrentInstallation();

    }

    public int getCount() {
        return guestsList.size();
    }

    public GuestObject getItem(int position) {
        return guestsList.get(position);
    }

    public long getItemId(int position) {
        return guestsList.indexOf(getItem(position));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final GuestObject guestObject = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.content_main_guests_row, parent, false);

        }

        LinearLayout datesLayout = (LinearLayout) convertView.findViewById(R.id.datesLayout);

        TextView guestName = (TextView) convertView.findViewById(R.id.tvAutoMake);
        TextView guestType = (TextView) convertView.findViewById(R.id.guestType);
        TextView guestContactType = (TextView) convertView.findViewById(R.id.tvAutoModel);
        TextView guestStartDate = (TextView) convertView.findViewById(R.id.tvAutoColor);
        TextView guestEndDate = (TextView) convertView.findViewById(R.id.guestEndDate);
        TextView guestNotes = (TextView) convertView.findViewById(R.id.guestNotes);

        TextView guestMondayAccess = (TextView) convertView.findViewById(R.id.tvAutoPlate);
        TextView guestTuesdayAccess = (TextView) convertView.findViewById(R.id.guestTuesdayAccess);
        TextView guestWednesdayAccess = (TextView) convertView.findViewById(R.id.guestWednesdayAccess);
        TextView guestThursdayAccess = (TextView) convertView.findViewById(R.id.guestThursdayAccess);
        TextView guestFridayAccess = (TextView) convertView.findViewById(R.id.guestFridayAccess);
        TextView guestSaturdayAccess = (TextView) convertView.findViewById(R.id.guestSaturdayAccess);
        TextView guestSundayAccess = (TextView) convertView.findViewById(R.id.guestSundayAccess);

        TextView guestOwnerName = (TextView) convertView.findViewById(R.id.guestOwnerName);

        recordAccess = (Button) convertView.findViewById(R.id.guestRecordAccess);

        if ( installation.getString("MemberType").equals("Member")) {

            recordAccess.setVisibility(View.GONE);

        } else {

            recordAccess.setVisibility(View.VISIBLE);

        }



        final GuestObject guest = guestsList.get(position);

     /*   if ( guest.getOwnerContactNumberType().equals("None")) {

            recordAccess.setVisibility(View.GONE);

        } else {

            recordAccess.setVisibility(View.VISIBLE);

        }
*/
        try {
            guestName.setText(guestObject.getGuestName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            guestType.setText(guestObject.getGuestType());
            if ( guestObject.getGuestType().equals("Permanent")) {
                datesLayout.setVisibility(View.GONE);
            } else {

                datesLayout.setVisibility(View.VISIBLE);
                try {

                    guestStartDate.setText(guestObject.getGuestStartdate());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    guestEndDate.setText(guestObject.getGuestEnddate());
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            guestContactType.setText(guestObject.getOwnerContactNumberType());
        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if ( guestObject.getMondayAccess().equals("Yes")) {

                guestMondayAccess.setVisibility(View.VISIBLE);
            } else {
                guestMondayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if ( guestObject.getTuesdayAccess().equals("Yes")) {

                guestTuesdayAccess.setVisibility(View.VISIBLE);
            } else {
                guestTuesdayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if ( guestObject.getWednesdayAccess().equals("Yes")) {

                guestWednesdayAccess.setVisibility(View.VISIBLE);
            } else {
                guestWednesdayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if ( guestObject.getThursdayAccess().equals("Yes")) {

                guestThursdayAccess.setVisibility(View.VISIBLE);
            } else {
                guestThursdayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if ( guestObject.getFridayAccess().equals("Yes")) {

                guestFridayAccess.setVisibility(View.VISIBLE);
            } else {
                guestFridayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if ( guestObject.getSaturdayAccess().equals("Yes")) {

                guestSaturdayAccess.setVisibility(View.VISIBLE);
            } else {
                guestSaturdayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if ( guestObject.getSundayAccess().equals("Yes")) {

                guestSundayAccess.setVisibility(View.VISIBLE);
            } else {
                guestSundayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            guestOwnerName.setText(guestObject.getGuestOwner());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            guestNotes.setText(guestObject.getGuestDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }

        recordAccess.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if ( guest.getOwnerContactNumberType().equals("Mobile") || guest.getOwnerContactNumberType().equals("Home")) {

            Intent contactOwner = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", String.valueOf(guest.getOwnerContactNumber()), null));
            context.startActivity(contactOwner);

        } else if ( guest.getOwnerContactNumberType().equals("Text") ){

            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", guest.getOwnerContactNumber(), null)));
        } else {



        }


        query = new ParseQuery<ParseObject>(installation.getString("AssociationCode"));

        ParseFile guestAccessFile;
        byte[] data = new byte[0];
        String guestAccessString = null;
        String[] guestAccessStringArray;
        ArrayList<String> guestAccessArray;
        String guestAccessUpdate = "";

        try {
            guestAccessFile =  query.getFirst().getParseFile("GuestAccessFile");
            data = guestAccessFile.getData();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            guestAccessString = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, H:mm a");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
        String strDate = sdf.format(c.getTime());

        Log.d(TAG, "existing guestAccessString ---> " + guestAccessString);

        if ( guestAccessString.length() != 0 ) {

            guestAccessUpdate = guestAccessString + "|" + guestObject.getGuestOwner() + "^" + strDate + "^" + guestObject.getGuestName() + "^" + guestObject.getGuestType()
                    + "^" + guestObject.getOwnerContactNumberType();
            Log.d(TAG, "updated guestAccessString ---> " + guestAccessString);

            guestAccessStringArray = guestAccessUpdate.split("\\|", -1);
            guestAccessArray = new ArrayList<String>(Arrays.asList(guestAccessStringArray));


            Collections.sort(guestAccessArray);

            for ( String accessItem: guestAccessArray) {

                Log.d(TAG, "access item ---> " + accessItem);


            }


        } else {

            guestAccessString = guestObject.getGuestOwner() + "^" + strDate + "^" + guestObject.getGuestName() + "^" + guestObject.getGuestType()
                    + "^" + guestObject.getOwnerContactNumberType();
            Log.d(TAG, "updated guestAccessString from empty ---> " + guestAccessString);

        }


        data = guestAccessUpdate.getBytes();
        guestAccessFile = new ParseFile("GuestAccess.txt", data);


        try {
            guestAccessFile.save();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }


        try {
            query.getFirst().put("GuestAccessDate", strDate);
            query.getFirst().put("GuestAccessFile", guestAccessFile);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {
            query.getFirst().save();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }


        Toast toast = Toast.makeText(getContext(), "Guest access updated.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();


    }
});






        return convertView;
    }



    @Override
    public Filter getFilter() {
        if (guestFilter == null) {
            guestFilter = new GuestFilter();
        }
        return guestFilter;
    }

    private class GuestFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() < 1) {

                results.count = guestsFilterList.size();
                results.values = guestsFilterList;

            } else {

                String searchString = constraint.toString().toUpperCase();
                ArrayList filterList = new ArrayList();

                for (GuestObject guest : guestsFilterList) {


                    if ( (guest.getGuestOwner().toUpperCase()).contains(searchString) || (guest.getGuestName().toUpperCase()).contains(searchString)) {
                        filterList.add(guest);
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
            guestsList = (ArrayList<GuestObject>) results.values;
            notifyDataSetChanged();
        }

    }

}

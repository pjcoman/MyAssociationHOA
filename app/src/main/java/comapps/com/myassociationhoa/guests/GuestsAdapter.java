package comapps.com.myassociationhoa.guests;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.objects.GuestObject;

/**
 * Created by me on 6/28/2016.
 */
@SuppressWarnings("ALL")
class GuestsAdapter extends ArrayAdapter<GuestObject> implements Filterable {

    private static final String TAG = "GUESTSADAPTER";

    private final ParseInstallation installation;
    private ParseQuery query;

    private ArrayList<GuestObject> guestsList;
    private final Context context;
    private GuestFilter guestFilter;
    private final ArrayList<GuestObject> guestsFilterList;

    private Button recordAccess;
    private Button deleteGuest;



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

        TextView guestName = (TextView) convertView.findViewById(R.id.tvGuestName);
        TextView guestType = (TextView) convertView.findViewById(R.id.tvGuestType);
        TextView guestContactType = (TextView) convertView.findViewById(R.id.tvGuestContactType);
        TextView guestStartDate = (TextView) convertView.findViewById(R.id.tvGuestStartDate);
        TextView guestEndDate = (TextView) convertView.findViewById(R.id.tvGuestEndDate);
        TextView guestNotes = (TextView) convertView.findViewById(R.id.tvGuestNotes);

        TextView guestMondayAccess = (TextView) convertView.findViewById(R.id.tvGuestAccessMonday);
        TextView guestTuesdayAccess = (TextView) convertView.findViewById(R.id.tvGuestTuesdayAccess);
        TextView guestWednesdayAccess = (TextView) convertView.findViewById(R.id.tvGuestWednesdayAccess);
        TextView guestThursdayAccess = (TextView) convertView.findViewById(R.id.tvGuestThursdayAccess);
        TextView guestFridayAccess = (TextView) convertView.findViewById(R.id.tvGuestFridayAccess);
        TextView guestSaturdayAccess = (TextView) convertView.findViewById(R.id.tvGuestSaturdayAccess);
        TextView guestSundayAccess = (TextView) convertView.findViewById(R.id.tvGuestSundayAccess);

        TextView guestOwnerName = (TextView) convertView.findViewById(R.id.tvGuestOwnerName);

        recordAccess = (Button) convertView.findViewById(R.id.guestRecordAccess);
        deleteGuest = (Button) convertView.findViewById(R.id.deleteGuest);

        if ( installation.getString("MemberType").equals("Member")) {

            recordAccess.setVisibility(View.GONE);
            deleteGuest.setVisibility(View.GONE);

        } else if ( installation.getString("MemberType").equals("Security")) {

            recordAccess.setVisibility(View.VISIBLE);
            deleteGuest.setVisibility(View.GONE);

        } else {

            recordAccess.setVisibility(View.VISIBLE);
            deleteGuest.setVisibility(View.VISIBLE);


        }



        final GuestObject guest = guestsList.get(position);

        try {
            guestName.setText(guestObject != null ? guestObject.getGuestName() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            guestType.setText(guestObject != null ? guestObject.getGuestType() : null);
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
            guestContactType.setText(guestObject != null ? guestObject.getOwnerContactNumberType() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (guestObject != null && guestObject.getMondayAccess().equals("Yes")) {

                guestMondayAccess.setVisibility(View.VISIBLE);
            } else {
                guestMondayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (guestObject != null && guestObject.getTuesdayAccess().equals("Yes")) {

                guestTuesdayAccess.setVisibility(View.VISIBLE);
            } else {
                guestTuesdayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (guestObject != null && guestObject.getWednesdayAccess().equals("Yes")) {

                guestWednesdayAccess.setVisibility(View.VISIBLE);
            } else {
                guestWednesdayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (guestObject != null && guestObject.getThursdayAccess().equals("Yes")) {

                guestThursdayAccess.setVisibility(View.VISIBLE);
            } else {
                guestThursdayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (guestObject != null && guestObject.getFridayAccess().equals("Yes")) {

                guestFridayAccess.setVisibility(View.VISIBLE);
            } else {
                guestFridayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (guestObject != null && guestObject.getSaturdayAccess().equals("Yes")) {

                guestSaturdayAccess.setVisibility(View.VISIBLE);
            } else {
                guestSaturdayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (guestObject != null && guestObject.getSundayAccess().equals("Yes")) {

                guestSundayAccess.setVisibility(View.VISIBLE);
            } else {
                guestSundayAccess.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            guestOwnerName.setText(guestObject != null ? guestObject.getGuestOwner() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            guestNotes.setText(guestObject != null ? guestObject.getGuestDescription() : null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recordAccess.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        switch (guest.getOwnerContactNumberType()) {
            case "Mobile":
            case "Home":

                Intent contactOwner = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", String.valueOf(guest.getOwnerContactNumber()), null));
                context.startActivity(contactOwner);

                break;
            case "Text":

                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", guest.getOwnerContactNumber(), null)));
                break;
            default:


                break;
        }


        query = new ParseQuery<>(installation.getString("AssociationCode"));

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
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
        String strDate = sdf.format(c.getTime());

        Log.d(TAG, "existing guestAccessString ---> " + guestAccessString);

        if ( (guestAccessString != null ? guestAccessString.length() : 0) != 0 ) {

            guestAccessUpdate = guestAccessString + "|" + (guestObject != null ? guestObject.getGuestOwner() : null) + "^" + strDate + "^" + guestObject.getGuestName() + "^" + guestObject.getGuestType()
                    + "^" + guestObject.getOwnerContactNumberType();
            Log.d(TAG, "updated guestAccessString ---> " + guestAccessString);

            guestAccessStringArray = guestAccessUpdate.split("\\|", -1);
            guestAccessArray = new ArrayList<>(Arrays.asList(guestAccessStringArray));


            Collections.sort(guestAccessArray);

            for ( String accessItem: guestAccessArray) {

                Log.d(TAG, "access item ---> " + accessItem);


            }


        } else {

            guestAccessString = (guestObject != null ? guestObject.getGuestOwner() : null) + "^" + strDate + "^" + guestObject.getGuestName() + "^" + guestObject.getGuestType()
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

        deleteGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // Setting Dialog Title
                alertDialog.setTitle("DeleteGuest Confirm");

                // Setting Dialog Message
                alertDialog.setMessage("Delete this guest?");

                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.delete);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, h:mm a");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yy-M-d");
                        String strDate = sdf.format(c.getTime());

                        final ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        query = new ParseQuery<>(installation.getString("AssociationCode")).fromLocalDatastore();



                        String guestsFileString = "";


                        try {
                            ParseFile guestFile = query.getFirst().getParseFile("GuestFile");
                            byte[] guestFileData = guestFile.getData();


                            guestsFileString = new String(guestFileData, "UTF-8");

                        } catch (ParseException | UnsupportedEncodingException e1) {
                            e1.printStackTrace();

                        }

                        Log.d(TAG, "guestFileString ---->" + guestsFileString);

            /*    Toast toast = Toast.makeText(getContext(), "Delete clicked." + " " + position, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();*/

                        Log.d(TAG, "guestObject ---> " + guestObject.toStringForDelete());

                        String guestFileUpdate = guestsFileString.replace(guestObject.toStringForDelete(), "");

                        Log.d(TAG, "guestFileForUpdate ---> " + guestsFileString);

                        byte[] data = guestFileUpdate.getBytes();
                        ParseFile guestFile = new ParseFile("GuestFile.txt", data);


                        try {
                            guestFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        try {
                            query.getFirst().put("GuestDate", strDate);
                            query.getFirst().put("GuestFile", guestFile);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        try {
                            query.getFirst().save();
                            query.getFirst().saveEventually();
                        } catch (ParseException e1) {
                            e1.printStackTrace();

                        }





                        Toast toast = Toast.makeText(getContext(), guestObject.getGuestName() +
                                " deleted.", Toast.LENGTH_LONG);




                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();



                        AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(context);
                        remoteDataTaskClass.execute();

                        guestsList.remove(position);
                        notifyDataSetChanged();



                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();





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

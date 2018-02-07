package comapps.com.myassociationhoa.directory;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.RemoteDataTaskClass;
import comapps.com.myassociationhoa.objects.RosterObject;

import static comapps.com.myassociationhoa.R.id.memberNumber;

/**
 * Created by me on 6/28/2016.
 */
@SuppressWarnings("ALL")
class DirectoryAdapter extends ArrayAdapter<RosterObject> implements Filterable {

    private static final String TAG = "DIRECTORYADAPTER";
    private static final String MYPREFERENCES = "MyPrefs";




    private ArrayList<RosterObject> rosterList;
    private final Context context;
    private RosterFilter rosterFilter;
    private final ArrayList<RosterObject> rosterFilterList;

    private final String memberType;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private ScrollView memberInfo;

    private TextView textViewLastName;
    private TextView textViewFirstName;
    private TextView textViewAddress1;
    private TextView textViewMiddleName;
    private TextView textViewActDate;
    private TextView textViewMemberNumber;

    private Button deleteDirectoryItem;

    private ParseQuery query;



    public DirectoryAdapter(Context context, ArrayList<RosterObject> rosterList) {
        super(context, R.layout.content_main_directory_row, rosterList);
        this.rosterList = rosterList;
        this.context = context;
        rosterFilterList = rosterList;

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        memberType = installation.getString("MemberType");

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

            convertView = inflater.inflate(R.layout.content_main_directory_row, parent, false);

        }

        memberInfo = (ScrollView) convertView.findViewById(R.id.scrollViewMemberInfo);

        textViewLastName = (TextView) convertView.findViewById(R.id.lastNameTextView);
        textViewFirstName = (TextView) convertView.findViewById(R.id.firstNameTextView);
        textViewMiddleName = (TextView) convertView.findViewById(R.id.middleNameTextView);
        textViewAddress1 = (TextView) convertView.findViewById(R.id.address1TextView);
        textViewActDate = (TextView) convertView.findViewById(R.id.activateDate);
        textViewMemberNumber = (TextView) convertView.findViewById(memberNumber);

        deleteDirectoryItem = (Button) convertView.findViewById(R.id.deleteDirectoryItem);

        if ( memberType.equals("Administrator") || memberType.equals("Master")) {

            deleteDirectoryItem.setVisibility(View.VISIBLE);
        } else {
            deleteDirectoryItem.setVisibility(View.GONE);
            textViewActDate.setVisibility(View.GONE);
            textViewMemberNumber.setVisibility(View.GONE);
        }



        final RosterObject roster = rosterList.get(position);

        textViewLastName.setText(rosterObject != null ? rosterObject.getLastName() : null);
        textViewFirstName.setText(rosterObject.getFirstName());
        textViewAddress1.setText(rosterObject.getHomeAddress1());
        textViewMiddleName.setText(rosterObject.getMiddleName());
        textViewActDate.setText(rosterObject.getActivationDate());
        textViewMemberNumber.setText(rosterObject.getMemberNumber());



       convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Position " + position);
         //       Log.d(TAG, "Original Position " + roster.getNumber());

                Intent intentFullMember = new Intent();
                intentFullMember.setClass(getContext().getApplicationContext(), DirectoryRosterMemberActivity.class);
                intentFullMember.putExtra("object_number", position);
                getContext().startActivity(intentFullMember);

            }
        });





    deleteDirectoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Position/Object to delete ----> " + position);
                //       Log.d(TAG, "Original Position " + roster.getNumber());

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // Setting Dialog Title
                alertDialog.setTitle("Delete Directory Confirm");

                // Setting Dialog Message
                alertDialog.setMessage("Delete this item?");

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



                        String rosterFileString = "";


                        try {
                            ParseFile guestFile = query.getFirst().getParseFile("RosterFile");
                            byte[] rosterFileData = guestFile.getData();


                            rosterFileString = new String(rosterFileData, "UTF-8");

                        } catch (ParseException | UnsupportedEncodingException e1) {
                            e1.printStackTrace();

                        }

                        rosterFileString = rosterFileString + "|";

                        Log.d(TAG, "rosterFileString ---->" + rosterFileString);

            /*    Toast toast = Toast.makeText(getContext(), "Delete clicked." + " " + position, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();*/

                        Log.d(TAG, "rosterObject ---> " + rosterObject.toString());

                        String rosterFileUpdate = rosterFileString.replace(rosterObject.toString() + "|", "");

                        if ( rosterFileUpdate.substring(rosterFileUpdate.length() - 1, rosterFileUpdate.length()).equals("|")) {
                            rosterFileUpdate = rosterFileUpdate.substring(0, rosterFileUpdate.length() - 1);
                        }

                        String[] rosterUpdateFileArray = rosterFileUpdate.split("\\|", -1);


                        int i = 0;
                        String rosterFileUpdateRenumbered = "";

                        for (String rosterItem : rosterUpdateFileArray) {

                            rosterItem.trim();
                            String rosterItemArray[] = rosterItem.split("\\^", 2);

                            Log.d(TAG, "rosterItemArray is " + rosterItemArray[0] + " " + rosterItemArray[1] +  " **** i is " + i);

                            rosterFileUpdateRenumbered = rosterFileUpdateRenumbered + String.valueOf(i) + "^" + rosterItemArray[1] + "|";

                            i++;



                        }

                        if ( rosterFileUpdateRenumbered.substring(rosterFileUpdateRenumbered.length() - 1, rosterFileUpdateRenumbered.length()).equals("|")) {
                            rosterFileUpdateRenumbered = rosterFileUpdateRenumbered.substring(0, rosterFileUpdateRenumbered.length() - 1);
                        }



                        Log.d(TAG, "rosterFileUpdateRenumbered ---> " + rosterFileUpdateRenumbered);

                        byte[] data = rosterFileUpdateRenumbered.getBytes();
                        ParseFile rosterFile = new ParseFile("Roster.txt", data);


                        try {
                            rosterFile.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        try {
                            query.getFirst().put("RosterDate", strDate);
                            query.getFirst().put("RosterFile", rosterFile);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        try {
                            query.getFirst().save();
                            query.getFirst().saveEventually();
                        } catch (ParseException e1) {
                            e1.printStackTrace();

                        }





                        Toast toast = Toast.makeText(getContext(), rosterObject.getFirstName() + " " + rosterObject.getLastName() +
                                " deleted.", Toast.LENGTH_LONG);




                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();



                        AsyncTask<Void, Void, Void> remoteDataTaskClass = new RemoteDataTaskClass(context);
                        remoteDataTaskClass.execute();

                        rosterList.remove(position);
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


                    if ( (rosterObject.getLastName().toUpperCase()).contains(searchString) || (rosterObject.getFirstName().toUpperCase()).contains(searchString)
                            || (rosterObject.getHomeAddress1().toUpperCase()).contains(searchString)) {
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

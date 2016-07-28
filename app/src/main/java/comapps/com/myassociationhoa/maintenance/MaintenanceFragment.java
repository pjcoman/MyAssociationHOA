package comapps.com.myassociationhoa.maintenance;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.MaintenanceObject;

/**
 * Created by me on 6/22/2016.
 */
public class MaintenanceFragment extends ListFragment {

    private static final String TAG = "MAINTENANCEFRAGMENT";
    private static final String MYPREFERENCES = "MyPrefs";
    MaintenanceObject maintenanceObject;
    MaintenanceAdapter adapter;
    ArrayList<MaintenanceObject> maintenanceObjects;
    SharedPreferences sharedPreferences;
    ListView maintenanceItemList;

    List<String> maintenanceCategoryNames;
    ArrayAdapter<String> listAdapter;






    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        ArrayList<MaintenanceObject> maintenanceItems = new ArrayList<>();
        //   Integer maintenanceItemsCount = Integer.valueOf(sharedPreferences.getString("maintenanceSize", ""));

        maintenanceItemList = (ListView) getActivity().findViewById(R.id.listViewMaintenanceCats);
        maintenanceCategoryNames = new ArrayList<>();

        maintenanceCategoryNames.add("ALL");

        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, maintenanceCategoryNames);


        maintenanceItemList.setAdapter(listAdapter);

        maintenanceItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "maintenance category list row --> " + position);

                maintenanceItemList.setVisibility(View.GONE);


               new RemoteDataTask(maintenanceCategoryNames.get(position)).execute();


            }
        });

        new RemoteDataTask("All").execute();



    }

    private class RemoteDataTask extends AsyncTask<String, Void, Void> {

        private String data;

        public RemoteDataTask(String passedData) {

            data = passedData;

        }


        @Override
        protected Void doInBackground(String... params){

                final String maintCat = data;

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseInstallation.getCurrentInstallation().getString("AssociationCode"));
                query.findInBackground(new FindCallback<ParseObject>() {


                    public void done(List<ParseObject> associations, ParseException e) {
                        if (e == null) {
                            ParseFile maintenanceFile = associations.get(0).getParseFile("MaintenanceFile");

                            String[] maintenanceFileArray = null;

                            try {

                                byte[] file = maintenanceFile.getData();


                                try {
                                    String maintenanceFileString = new String(file, "UTF-8");

                                    maintenanceFileArray = maintenanceFileString.split("\\|");


                                    for (String item : maintenanceFileArray) {

                                        item.trim();


                                        //                   Log.v(TAGM, "maintenance item: " + item);

                                    }

                                    //               Log.v(TAGM, "maintenanceFileArray length: " + maintenanceFileArray.length);


                                    maintenanceObjects = new ArrayList<MaintenanceObject>();

                                    for (int i = 0; i < maintenanceFileArray.length; i++) {


                                        String[] maintenanceFields = maintenanceFileArray[i].split("\\^");
                                        int j = 1;
                                        for (String member : maintenanceFields) {

                                            //                         Log.d(TAG, "memmber --> "+ member + " " + j);
                                            j = j + 1;
                                        }


                                        maintenanceObject = new MaintenanceObject();

                                        for (j = 0; j < maintenanceFields.length; j++) {


                                            switch (j) {
                                                case 0:
                                                    maintenanceObject.setMaintenanceName(maintenanceFields[j]);
                                                    break;
                                                case 1:
                                                    maintenanceObject.setMaintenanceDate(maintenanceFields[j]);
                                                    break;
                                                case 2:
                                                    maintenanceObject.setMaintenanceDesc(maintenanceFields[j]);
                                                    break;
                                                case 3:
                                                    maintenanceObject.setMaintenanceNotes(maintenanceFields[j]);
                                                    break;
                                                case 4:
                                                    maintenanceObject.setMaintenanceCategory(maintenanceFields[j]);
                                                    if (maintenanceCategoryNames.contains(maintenanceFields[j])) {
                                                        //true
                                                    } else {
                                                        maintenanceCategoryNames.add(maintenanceFields[j]);
                                                        listAdapter.notifyDataSetChanged();
                                                    }
                                                    break;


                                            }


                                        }

                                        if ( maintenanceObject.getMaintenanceCategory().equals(maintCat) ||
                                                maintCat.equals("ALL")
                                        ) {

                                            maintenanceObjects.add(maintenanceObject);

                                        }





                                    }


                                    // Will invoke overrided `toString()` method


                                } catch (UnsupportedEncodingException e1) {
                                    e1.printStackTrace();
                                }

                            } catch (ParseException e1) {
                            }

                            adapter = new MaintenanceAdapter(getActivity(), maintenanceObjects);
                            setListAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            //           Log.d(TAGM, "maintenanceObjects size is " + maintenanceObjects.size());

                            for (MaintenanceObject object : maintenanceObjects) {
                                //               Log.i(TAGM, object.toString());
                            }


//**************************************************************************************************************************************************************


                        }
                    }



                });



            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


    }
}
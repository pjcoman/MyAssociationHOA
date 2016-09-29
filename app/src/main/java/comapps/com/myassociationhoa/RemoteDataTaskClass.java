package comapps.com.myassociationhoa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import comapps.com.myassociationhoa.objects.AutoObject;
import comapps.com.myassociationhoa.objects.CalendarObject;
import comapps.com.myassociationhoa.objects.GuestAccessObject;
import comapps.com.myassociationhoa.objects.GuestObject;
import comapps.com.myassociationhoa.objects.MaintenanceCategoryObject;
import comapps.com.myassociationhoa.objects.MaintenanceObject;
import comapps.com.myassociationhoa.objects.PetObject;
import comapps.com.myassociationhoa.objects.ProviderObject;
import comapps.com.myassociationhoa.objects.PushObject;
import comapps.com.myassociationhoa.objects.RosterObject;

/**
 * Created by me on 8/28/2016.
 */
public class RemoteDataTaskClass extends AsyncTask<Void, Void, Void> {

    Context context;

    public RemoteDataTaskClass(Context context) {
        super();
        this.context = context;


    }

    private static final String TAG = "REMOTEDATATASKCLASS";

    private static final String MYPREFERENCES = "MyPrefs";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferencesVisited;
    SharedPreferences.Editor editorVisited;



    private ParseInstallation installation;
    ParseQuery<ParseObject> queryAssociations;



    private RosterObject rosterObject;
    private ArrayList<RosterObject> rosterObjects;

    private CalendarObject calendarObject;
    private ArrayList<CalendarObject> calendarObjects;

    MaintenanceObject maintenanceObject;
    private ArrayList<MaintenanceObject> maintenanceObjects;

    MaintenanceCategoryObject maintenanceCategoryObject;
    private ArrayList<MaintenanceCategoryObject> maintenanceCategoryObjects;

    ProviderObject providerObject;
    private ArrayList<ProviderObject> providerObjects;



    PushObject pushObject;
    private ArrayList<PushObject> pushObjectsMember;
    private ArrayList<PushObject> pushObjects;

    PetObject petObject;
    private ArrayList<PetObject> petObjects;

    AutoObject autoObject;
    private ArrayList<AutoObject> autoObjects;

    GuestObject guestObject;
    private ArrayList<GuestObject> guestObjects;
    GuestAccessObject guestAccessObject;
    private ArrayList<GuestAccessObject> guestAccessObjects;

    private String memberType;
    private String associationCode;
    String memberNumber;




    int i = 0;
    int j = 0;
    int k = 0;





    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        installation = ParseInstallation.getCurrentInstallation();

        memberType = installation.getString("MemberType");
        associationCode = installation.getString("AssociationCode");
        memberNumber = installation.getString("memberNumber");


        sharedPreferences = context.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesVisited = context.getSharedPreferences(VISITEDPREFERENCES, Context.MODE_PRIVATE);



    }

    @Override
    protected Void doInBackground(Void... params) {

            queryAssociations = new ParseQuery<>(associationCode);




        queryAssociations.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> associationObject, ParseException e) {

                if (e == null) {



                    ParseObject.unpinAllInBackground(associationObject, new DeleteCallback() {
                        public void done(ParseException e) {
                            // Cache the new results.



                            ParseObject.pinAllInBackground(associationCode, associationObject, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {





//****************************************************************************ROSTER**********************************************************************************

                                    ParseFile rosterFile = associationObject.get(0).getParseFile("RosterFile");

                                    String[] rosterFileArray;

                                    byte[] rosterFileData = new byte[0];

                                    try {
                                        rosterFileData = rosterFile.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                    String rosterFileString = null;

                                    try {
                                        rosterFileString = new String(rosterFileData, "UTF-8");
                                    } catch (UnsupportedEncodingException e2) {
                                        e2.printStackTrace();
                                    }


                                    Log.d(TAG, "rosterFileString is " + rosterFileString);

                                    rosterFileArray = rosterFileString.split("\\|", -1);


                                    for (String member : rosterFileArray) {

                                        member.trim();

                                        Log.d(TAG, "rosterFileArray member is " + member);

                                    }


                                    rosterObjects = new ArrayList<RosterObject>();

                                    for (i = 0; i < rosterFileArray.length; i++) {


                                        String[] rosterFields = rosterFileArray[i].split("\\^", -1);



                                        rosterObject = new RosterObject();


                                        rosterObject.setNumber(rosterFields[0]);
                                        rosterObject.setLastName(rosterFields[1]);
                                        rosterObject.setFirstName(rosterFields[2]);
                                        rosterObject.setMiddleName(rosterFields[3]);
                                        rosterObject.setHomeAddress1(rosterFields[4]);
                                        rosterObject.setHomeAddress2(rosterFields[5]);
                                        rosterObject.setHomeCity(rosterFields[6]);
                                        rosterObject.setHomeState(rosterFields[7]);
                                        rosterObject.setHomeZip(rosterFields[8]);
                                        rosterObject.setHomePhone(rosterFields[9]);
                                        rosterObject.setMobilePhone(rosterFields[10]);
                                        rosterObject.setEmail(rosterFields[11]);
                                        rosterObject.setWinterName(rosterFields[12]);
                                        rosterObject.setWinterAddress1(rosterFields[13]);
                                        rosterObject.setWinterAddress2(rosterFields[14]);
                                        rosterObject.setWinterCity(rosterFields[15]);
                                        rosterObject.setWinterState(rosterFields[16]);
                                        rosterObject.setWinterZip(rosterFields[17]);
                                        rosterObject.setWinterPhone(rosterFields[18]);
                                        rosterObject.setWinterEmail(rosterFields[19]);
                                        rosterObject.setMemberNumber(rosterFields[20]);
                                        rosterObject.setStatus(rosterFields[21]);
                                        rosterObject.setEmergencyName(rosterFields[22]);
                                        rosterObject.setEmergencyPhoneNumber(rosterFields[23]);
                                        rosterObject.setActivationDate(rosterFields[24]);
                                        rosterObject.setGroups(rosterFields[25]);

                                        if ( memberNumber.equals(rosterObject.getMemberNumber())) {
                                            String groupsForParseChannels[] = rosterFields[25].split(",");
                                            final List channelsToAdd = Arrays.asList(groupsForParseChannels);
                                            installation.put("memberName", rosterFields[2] + " " + rosterFields[1]);
                                            installation.put("MemberType", rosterFields[21]);
                                            installation.put("channels", channelsToAdd);
                                            installation.saveEventually();

                                        }

                                        Log.d(TAG, "MEMBERTYPE ----> " + installation.getString("MemberType") +  "<----");

                                        rosterObjects.add(rosterObject);


                                        editor = sharedPreferences.edit();
                                        Gson gson = new Gson();
                                        String jsonRosterObject = gson.toJson(rosterObject); // myObject - instance of MyObject
                                        editor.putString("rosterObject" + "[" + i + "]", jsonRosterObject);
                                        editor.putInt("rosterSize", rosterObjects.size());

                                        Log.d(TAG, "rosterObject added ----> " + jsonRosterObject +  "<----");
                                        Log.d(TAG, "rosterObjects size is ----> " + rosterObjects.size());

                                        if ( rosterObject.getMemberNumber().equals(memberNumber)) {

                                            Log.d(TAG, "rosterObject memberNumber ----> " + rosterObject.getMemberNumber() + " " + memberNumber + "<----");
                                            editor.putString("ROSTEROBJECTMYINFO",jsonRosterObject);
                                            Log.d(TAG, "ROSTEROBJECTMYINFO added ----> " + jsonRosterObject +  "<----");
                                        }

                                        editor.apply();






                                    }




//*************************************************************************CALENDAR************************************************************************************


                                    try {
                                        ParseFile eventFile = associationObject.get(0).getParseFile("EventFile");

                                        String[] eventFileArray = null;


                                        byte[] eventFileData = new byte[0];
                                        try {
                                            eventFileData = eventFile.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        String eventFileString = null;
                                        try {
                                            eventFileString = new String(eventFileData, "UTF-8");
                                        } catch (UnsupportedEncodingException e2) {
                                            e2.printStackTrace();
                                        }

                                        eventFileArray = eventFileString.split("\\|", -1);


                                        for (String event : eventFileArray) {

                                            event.trim();


                                            Log.v(TAG, "EVENT: " + event);

                                        }

                                        Log.v(TAG, "eventFileArray length: " + eventFileArray.length);


                                        calendarObjects = new ArrayList<CalendarObject>();

                                        for (i = 1, j = 0, k = 0; i < eventFileArray.length; i++) {

                                            switch (j) {
                                                case 0:
                                                    calendarObject = new CalendarObject();
                                                    calendarObject.setCalendarText(eventFileArray[i]);
                                                    j = j + 1;
                                                    break;
                                                case 1:
                                                    calendarObject.setCalendarDetailText(eventFileArray[i]);
                                                    j = j + 1;
                                                    break;
                                                case 2:
                                                    calendarObject.setCalendarStartDate(eventFileArray[i]);
                                                    j = j + 1;
                                                    break;
                                                case 3:
                                                    calendarObject.setCalendarEndDate(eventFileArray[i]);
                                                    j = j + 1;
                                                    break;
                                                case 4:
                                                    calendarObject.setCalendarSortDate(eventFileArray[i]);
                                                    calendarObjects.add(calendarObject);

                                                    Log.v(TAG, "calendarObject ----> " + calendarObject.toString());

                                                    editor = sharedPreferences.edit();
                                                    Gson gson = new Gson();
                                                    String jsonCalendarObject = gson.toJson(calendarObject); // myObject - instance of MyObject
                                                    editor.putString("calendarObject" + "[" + k + "]", jsonCalendarObject);
                                                    editor.putString("calendarSize", String.valueOf(calendarObjects.size()));
                                                    editor.apply();

                                                    j = 0;
                                                    k++;

                                                    break;
                                            }


                                        }

                                        for (CalendarObject object : calendarObjects) {
                                            Log.i(TAG, object.toString());
                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }

//**************************************************************************PROVIDER************************************************************************************
                                    try {



                                            ParseFile providerFile = associationObject.get(0).getParseFile("ProviderFile");

                                            ArrayList<String> providerType = new ArrayList();
                                            String[] providerFileArray = null;


                                            byte[] providerFileData = new byte[0];
                                            try {
                                                providerFileData = providerFile.getData();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                            String providerFileString = null;
                                            try {
                                                providerFileString = new String(providerFileData, "UTF-8");
                                            } catch (UnsupportedEncodingException e2) {
                                                e2.printStackTrace();
                                            }
                                            Log.v(TAG, "providerFileString -----> " + providerFileString);

                                            providerFileArray = providerFileString.split("\\|", -1);

                                            Log.v(TAG, "providerFileArrayLength -----> " + providerFileArray.length);

                                            for (String providerField : providerFileArray) {

                                                Log.v(TAG, "providerField -----> " + providerField);


                                            }


                                            providerObjects = new ArrayList<ProviderObject>();


                                            for (i = 0, j = 0; i < providerFileArray.length; i++) {


                                                switch (j) {
                                                    case 0:
                                                        providerObject = new ProviderObject();
                                                        providerObject.setProviderType(providerFileArray[i]);
                                                        j++;
                                                        Log.d(TAG, "provider type is " + providerFileArray[i]);
                                                        break;
                                                    case 1:
                                                        providerObject.setProviderCount(providerFileArray[i]);
                                                        j++;
                                                        Log.d(TAG, "provider count is " + providerFileArray[i]);
                                                        break;
                                                    case 2:
                                                        providerObject.setProviderList(providerFileArray[i]);
                                                        providerObjects.add(providerObject);
                                                        j = 0;

                                                        editor = sharedPreferences.edit();
                                                        Gson gson = new Gson();
                                                        String jsonProviderObject = gson.toJson(providerObject); // myObject - instance of MyObject
                                                        editor.putString("providerObject" + "[" + ((i + 1) / 3 - 1) + "]", jsonProviderObject);
                                                        editor.putString("providerSize", String.valueOf(providerObjects.size()));
                                                        editor.apply();

                                                }

                                            }


                                            Log.d(TAG, "providerObjects size is " + providerObjects.size());

                                            for (ProviderObject object : providerObjects) {
                                                Log.i(TAG, object.toString());
                                            }

                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }



// ***************************************************************************MAINTENANCE***********************************************************************************
                                    try {
                                        ParseFile maintenanceCategoryFile = associationObject.get(0).getParseFile("MaintenanceCategoryFile");

                                        String[] maintenanceCategoryFileArray;



                                        byte[] mcFileData = new byte[0];
                                        try {
                                            mcFileData = maintenanceCategoryFile.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        String maintenanceCategoryFileString = null;
                                        try {
                                            maintenanceCategoryFileString = new String(mcFileData, "UTF-8");
                                        } catch (UnsupportedEncodingException e2) {
                                            e2.printStackTrace();
                                        }
                                        Log.d(TAG, "maintenanceCategoryFileString --->" + maintenanceCategoryFileString);
                                        maintenanceCategoryFileArray = maintenanceCategoryFileString.split("\\|", -1);


                                        maintenanceCategoryObjects = new ArrayList<>();

                                        i = 0;
                                        j = 0;
                                        for (String maintenanceCatItem : maintenanceCategoryFileArray) {

                                            Log.d(TAG, "maintenanceCategoryObject item --->" + maintenanceCatItem + " i = " + i + " j = " + j);

                                            switch (j) {
                                                case 0:
                                                    maintenanceCategoryObject = new MaintenanceCategoryObject();
                                                    maintenanceCategoryObject.setMaintenanceCatName(maintenanceCatItem);
                                                    j++;
                                                    break;
                                                case 1:
                                                    maintenanceCategoryObject.setMaintenanceCatEmail(maintenanceCatItem);
                                                    maintenanceCategoryObjects.add(maintenanceCategoryObject);
                                                    Log.d(TAG, "maintenanceCategoryObject --->" + maintenanceCategoryObject.toString());

                                                    editor = sharedPreferences.edit();
                                                    Gson gson = new Gson();
                                                    String jsonMaintenanceCategoryObject = gson.toJson(maintenanceCategoryObject); // myObject - instance of MyObject
                                                    editor.putString("maintenanceCategoryObject" + "[" + (maintenanceCategoryObjects.size() - 1) + "]", jsonMaintenanceCategoryObject);
                                                    editor.putInt("maintenanceCategoryObjectsSize", maintenanceCategoryObjects.size());
                                                    editor.apply();

                                                    j = 0;

                                                    break;
                                            }


                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }


                                    try {
                                        ParseFile maintenanceFile = associationObject.get(0).getParseFile("MaintenanceFile");

                                        String[] maintenanceFileArray;
                                        String[] maintenanceItems;


                                        byte[] mFileData = new byte[0];
                                        try {
                                            mFileData = maintenanceFile.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        String maintenanceFileString = null;
                                        try {
                                            maintenanceFileString = new String(mFileData, "UTF-8");
                                        } catch (UnsupportedEncodingException e2) {
                                            e2.printStackTrace();
                                        }
                                        Log.d(TAG, "maintenanceFileString --->" + maintenanceFileString);
                                        maintenanceFileArray = maintenanceFileString.split("\\|", -1);


                                        maintenanceObjects = new ArrayList<>();

                                        i = 0;
                                        for (String maintenanceMember : maintenanceFileArray) {

                                            maintenanceItems = maintenanceMember.split("\\^", -1);
                                            maintenanceObject = new MaintenanceObject();
                                            maintenanceObject.setMaintenanceName(maintenanceItems[0]);
                                            maintenanceObject.setMaintenanceDate(maintenanceItems[1]);
                                            maintenanceObject.setMaintenanceDesc(maintenanceItems[2]);
                                            maintenanceObject.setMaintenanceNotes(maintenanceItems[3]);
                                            maintenanceObject.setMaintenanceCategory(maintenanceItems[4]);
                                            maintenanceObjects.add(maintenanceObject);
                                            Log.d(TAG, "maintenanceObject --->" + maintenanceObject.toString());

                                            editor = sharedPreferences.edit();
                                            Gson gson = new Gson();
                                            String jsonMaintenanceObject = gson.toJson(maintenanceObject); // myObject - instance of MyObject
                                            editor.putString("maintenanceObject" + "[" + i + "]", jsonMaintenanceObject);
                                            editor.putString("maintenanceObjectsSize", String.valueOf(maintenanceObjects.size()));
                                            editor.apply();

                                            i++;


                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }

//******************************************************************************GUESTS********************************************************************************

                                    try {
                                        ParseFile guestAccessFile = associationObject.get(0).getParseFile("GuestAccessFile");

                                        String[] guestAccessFileArray;
                                        String[] guestAccessItems;


                                        byte[] guestAccessFileData = new byte[0];
                                        try {
                                            guestAccessFileData = guestAccessFile.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        String guestAccessFileString = null;
                                        try {
                                            guestAccessFileString = new String(guestAccessFileData, "UTF-8");
                                        } catch (UnsupportedEncodingException e2) {
                                            e2.printStackTrace();
                                        }
                                        Log.d(TAG, "guestAccessFileString ---->" + guestAccessFileString);
                                        guestAccessFileArray = guestAccessFileString.split("\\|", -1);


                                        guestAccessObjects = new ArrayList<>();

                                        i = 0;
                                        for (String accessItem : guestAccessFileArray) {

                                            guestAccessItems = accessItem.split("\\^", -1);
                                            guestAccessObject = new GuestAccessObject();
                                            guestAccessObject.setGuestAccessOwner(guestAccessItems[0]);
                                            guestAccessObject.setGuestAccessDate(guestAccessItems[1]);
                                            guestAccessObject.setGuestAccessName(guestAccessItems[2]);
                                            guestAccessObject.setGuestAccessType(guestAccessItems[3]);
                                            guestAccessObject.setGuestAccessContactType(guestAccessItems[4]);
                                            guestAccessObjects.add(guestAccessObject);

                                            Log.d(TAG, "guestAccessObject --->" + guestAccessObject.toString());

                                            editor = sharedPreferences.edit();
                                            Gson gson = new Gson();
                                            String jsonGuestAccessObject = gson.toJson(guestAccessObject); // myObject - instance of MyObject
                                            editor.putString("guestAccessObject" + "[" + i + "]", jsonGuestAccessObject);
                                            editor.putInt("guestAccessObjectsSize", guestAccessObjects.size());
                                            editor.apply();

                                            i++;


                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }

//******************************************************************************GUESTS********************************************************************************

                                    try {
                                        ParseFile guestFile = associationObject.get(0).getParseFile("GuestFile");

                                        String[] guestFileArray;
                                        String[] guestItems;


                                        byte[] guestFileData = new byte[0];
                                        try {
                                            guestFileData = guestFile.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        String guestFileString = null;
                                        try {
                                            guestFileString = new String(guestFileData, "UTF-8");
                                        } catch (UnsupportedEncodingException e2) {
                                            e2.printStackTrace();
                                        }
                                        Log.d(TAG, "guestFileString ---->" + guestFileString);
                                        guestFileArray = guestFileString.split("\\|", -1);


                                        guestObjects = new ArrayList<>();

                                        i = 0;
                                        for (String guest : guestFileArray) {

                                            guestItems = guest.split("\\^", -1);
                                            guestObject = new GuestObject();
                                            guestObject.setGuestOwner(guestItems[0]);
                                            guestObject.setGuestOwnerMemberNumber(guestItems[1]);
                                            guestObject.setGuestType(guestItems[2]);
                                            guestObject.setGuestStartdate(guestItems[3]);
                                            guestObject.setGuestEnddate(guestItems[4]);
                                            guestObject.setMondayAccess(guestItems[5]);
                                            guestObject.setTuesdayAccess(guestItems[6]);
                                            guestObject.setWednesdayAccess(guestItems[7]);
                                            guestObject.setThursdayAccess(guestItems[8]);
                                            guestObject.setFridayAccess(guestItems[9]);
                                            guestObject.setSaturdayAccess(guestItems[10]);
                                            guestObject.setSundayAccess(guestItems[11]);
                                            guestObject.setGuestDescription(guestItems[12]);
                                            guestObject.setGuestName(guestItems[13]);
                                            guestObject.setOwnerContactNumberType(guestItems[14]);
                                            guestObject.setOwnerContactNumber(guestItems[15]);
                                            guestObjects.add(guestObject);

                                            Log.d(TAG, "guestObject --->" + guestObject.toString());

                                            editor = sharedPreferences.edit();
                                            Gson gson = new Gson();
                                            String jsonGuestObject = gson.toJson(guestObject); // myObject - instance of MyObject
                                            editor.putString("guestObject" + "[" + i + "]", jsonGuestObject);
                                            editor.putInt("guestObjectsSize", guestObjects.size());
                                            editor.apply();

                                            i++;


                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }



//***********************************************************************PUSH***************************************************************************************



                                    try {
                                        ParseFile pushHistoryFile = associationObject.get(0).getParseFile("PushFile");

                                        String[] pushHistoryFileArray = null;
                                        String[] pushHistoryFileArray2 = null;


                                        byte[] pushFileData = new byte[0];
                                        try {
                                            pushFileData = pushHistoryFile.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        String pushHistoryFileString = null;
                                        try {
                                            pushHistoryFileString = new String(pushFileData, "UTF-8");
                                        } catch (UnsupportedEncodingException e2) {
                                            e2.printStackTrace();
                                        }

                                        pushHistoryFileArray = pushHistoryFileString.split("\\|");


                                        for (String pushMessage : pushHistoryFileArray) {

                                            pushMessage.trim();


                                            // Log.v(PM, "push message: " + pushMessage);

                                        }

                                        // Log.v(PM, "pushHistoryFileArray length: " + pushHistoryFileArray.length);

                                        pushObjects = new ArrayList<PushObject>();
                                        pushObjectsMember = new ArrayList<PushObject>();


                                        for (i = 1, j = 1, k = 0; i < pushHistoryFileArray.length; i++) {

                                            pushHistoryFileArray2 = pushHistoryFileArray[i].split("\\^");

                                            for (String pf2member : pushHistoryFileArray2) {
                                                // Log.d(PM, "member is ---> " + pf2member + " i= " + i + " j= " + j);

                                                switch (j) {
                                                    case 1:
                                                        j = j + 1;
                                                        break;
                                                    case 2:
                                                        j = j + 1;
                                                        break;
                                                    case 3:
                                                        pushObject = new PushObject();
                                                        pushObject.setDate(pf2member);
                                                        j = j + 1;
                                                        break;
                                                    case 4:
                                                        pushObject.setPushNotifacation(pf2member);

                                                        pushObjects.add(pushObject);
                                                        editor = sharedPreferences.edit();
                                                        Gson gson = new Gson();
                                                        String jsonPushObject = gson.toJson(pushObject); // myObject - instance of MyObject
                                                        editor.putString("pushObject" + "[" + (i - 1) + "]", jsonPushObject);
                                                        editor.putInt("pushObjectsSize", pushObjects.size());
                                                        editor.commit();
                                                        j = 1;



                                                        if ( !pf2member.contains("Admin group only")) {

                                                            pushObjectsMember.add(pushObject);
                                                            editor = sharedPreferences.edit();
                                                            gson = new Gson();
                                                            jsonPushObject = gson.toJson(pushObject); // myObject - instance of MyObject
                                                            editor.putString("pushObjectMember" + "[" + k + "]", jsonPushObject);
                                                            editor.putInt("pushObjectsMemberSize", pushObjectsMember.size());
                                                            editor.commit();
                                                            k++;
                                                            j = 1;

                                                        }


                                                }


                                            }

                                        }


                                        // Log.d(PM, "pushObjects size is " + pushObjects.size());

                                        for (PushObject object : pushObjects) {
                                            Log.i(TAG, object.toString());
                                        }


                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }


//***********************************************************************PETS*************************************************************************************************

                                    try {
                                        if (!sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {


                                            ParseFile petFile = associationObject.get(0).getParseFile("PetFile");

                                            String[] petFileArray = null;
                                            String[] petFileArray2 = null;


                                            byte[] petFileData = new byte[0];
                                            try {
                                                petFileData = petFile.getData();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                            String petFileString = null;
                                            try {
                                                petFileString = new String(petFileData, "UTF-8");
                                            } catch (UnsupportedEncodingException e2) {
                                                e2.printStackTrace();
                                            }

                                            petFileArray = petFileString.split("\\|", -1);


                                            for (String pet : petFileArray) {

                                                pet.trim();


                                                Log.v(TAG, "pet: " + pet);

                                            }

                                            Log.v(TAG, "petFileArray length: " + petFileArray.length);

                                            petObjects = new ArrayList<PetObject>();

                                            for (i = 0, j = 1; i < petFileArray.length; i++) {

                                                petFileArray2 = petFileArray[i].split("\\^", -1);

                                                for (String petField : petFileArray2) {
                                                    Log.d(TAG, "pet is ---> " + petField + " i = " + i + " j = " + j);

                                                    switch (j) {
                                                        case 1:
                                                            petObject = new PetObject();
                                                            petObject.setOwner(petField);
                                                            j = j + 1;
                                                            break;
                                                        case 2:
                                                            petObject.setMemberNumber(petField);
                                                            j = j + 1;
                                                            break;
                                                        case 3:
                                                            petObject.setName(petField);
                                                            j = j + 1;
                                                            break;
                                                        case 4:
                                                            petObject.setType(petField);
                                                            j = j + 1;
                                                            break;
                                                        case 5:
                                                            petObject.setBreed(petField);
                                                            j = j + 1;
                                                            break;
                                                        case 6:
                                                            petObject.setColor(petField);
                                                            j = j + 1;
                                                            break;
                                                        case 7:
                                                            petObject.setWeight(petField);
                                                            j = j + 1;
                                                            break;
                                                        case 8:
                                                            petObject.setMisc(petField);
                                                            petObjects.add(petObject);

                                                            editor = sharedPreferences.edit();
                                                            Gson gson = new Gson();
                                                            String jsonPetObject = gson.toJson(petObject); // myObject - instance of MyObject
                                                            editor.putString("petObject" + "[" + i + "]", jsonPetObject);
                                                            editor.putInt("petObjectsSize", petObjects.size());
                                                            editor.commit();
                                                            j = 1;


                                                    }


                                                }

                                            }


                                            // Log.d(PM, "petObjects size is " + petObjects.size());

                                            for (PetObject object : petObjects) {
                                                // Log.i(PM, object.toString());
                                            }

                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
//************************************************************AUTOS**************************************************************************************************************

                                    try {
                                        if (!sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {


                                            ParseFile autoFile = associationObject.get(0).getParseFile("AutoFile");

                                            String[] autoFileArray = null;
                                            String[] autoFileArray2 = null;


                                            byte[] autoFileData = new byte[0];
                                            try {
                                                autoFileData = autoFile.getData();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                            String autoFileString = null;
                                            try {
                                                autoFileString = new String(autoFileData, "UTF-8");
                                            } catch (UnsupportedEncodingException e2) {
                                                e2.printStackTrace();
                                            }

                                            autoFileArray = autoFileString.split("\\|", -1);


                                            for (String auto : autoFileArray) {

                                                auto.trim();


                                                Log.v(TAG, "auto ---> " + auto);

                                            }

                                            Log.v(TAG, "autoFileArray length ---> " + autoFileArray.length);

                                            autoObjects = new ArrayList<AutoObject>();

                                            for (i = 0, j = 0; i < autoFileArray.length; i++) {

                                                autoFileArray2 = autoFileArray[i].split("\\^", -1);

                                                for (String autoField : autoFileArray2) {

                                                    autoField = autoField.trim();

                                                    switch (j) {
                                                        case 0:
                                                            autoObject = new AutoObject();
                                                            autoObject.setOwner(autoField);
                                                            j++;
                                                            break;
                                                        case 1:
                                                            autoObject.setMemberNumber(autoField);
                                                            j++;
                                                            break;
                                                        case 2:
                                                            autoObject.setMake(autoField);
                                                            j++;
                                                            break;
                                                        case 3:
                                                            autoObject.setModel(autoField);
                                                            j++;
                                                            break;
                                                        case 4:
                                                            autoObject.setColor(autoField);
                                                            j++;
                                                            break;
                                                        case 5:
                                                            autoObject.setYear(autoField);
                                                            j++;
                                                            break;
                                                        case 6:
                                                            autoObject.setPlate(autoField);
                                                            j++;
                                                            break;
                                                        case 7:
                                                            autoObject.setTag(autoField);
                                                            autoObjects.add(autoObject);

                                                            editor = sharedPreferences.edit();
                                                            Gson gson = new Gson();
                                                            String jsonAutoObject = gson.toJson(autoObject); // myObject - instance of MyObject
                                                            editor.putString("autoObject" + "[" + i + "]", jsonAutoObject);
                                                            editor.putInt("autosObjectsSize", autoObjects.size());
                                                            editor.apply();
                                                            j = 0;


                                                    }


                                                }

                                            }


                                            // Log.d(PM, "autoObjects size is " + autoObjects.size());

                                            for (AutoObject object : autoObjects) {
                                                Log.i(TAG, object.toString());
                                            }


                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }


//***************************************************************************************************************************************************************************
                                    Map<String, ?> keys = sharedPreferences.getAll();
                                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                                        Log.d(TAG, "map values MYPREFERENCES" + entry.getKey() + ": " + entry.getValue().toString());
                                    }

//****************************************************************************************************************************************************************************









                                }
                            });

                        }
                    });









                } else {
                    Log.d(TAG, "Parse Exception");
                }


            }
        });




        return null;
    }




}
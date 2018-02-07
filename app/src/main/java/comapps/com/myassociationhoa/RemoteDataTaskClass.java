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

import comapps.com.myassociationhoa.objects.AdminMBObject;
import comapps.com.myassociationhoa.objects.AutoObject;
import comapps.com.myassociationhoa.objects.CalendarObject;
import comapps.com.myassociationhoa.objects.GuestAccessObject;
import comapps.com.myassociationhoa.objects.GuestObject;
import comapps.com.myassociationhoa.objects.MBObject;
import comapps.com.myassociationhoa.objects.MaintenanceCategoryObject;
import comapps.com.myassociationhoa.objects.MaintenanceObject;
import comapps.com.myassociationhoa.objects.PetObject;
import comapps.com.myassociationhoa.objects.ProviderObject;
import comapps.com.myassociationhoa.objects.PushObject;
import comapps.com.myassociationhoa.objects.RosterObject;

/**
 * Created by me on 8/28/2016.
 */
@SuppressWarnings("ALL")
public class RemoteDataTaskClass extends AsyncTask<Void, Void, Void> {

    private final Context context;

    public RemoteDataTaskClass(Context context) {
        super();
        this.context = context;


    }

    private static final String TAG = "REMOTEDATATASKCLASS";

    private static final String MYPREFERENCES = "MyPrefs";
    private static final String VISITEDPREFERENCES = "VisitedPrefs";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SharedPreferences sharedPreferencesVisited;
    private SharedPreferences.Editor editorVisited;



    private ParseInstallation installation;
    private ParseQuery<ParseObject> queryAssociations;



    private RosterObject rosterObject;
    private ArrayList<RosterObject> rosterObjects;

    private CalendarObject calendarObject;
    private ArrayList<CalendarObject> calendarObjects;

    private CalendarObject adminCalendarObject;
    private ArrayList<CalendarObject> adminCalendarObjects;

    private MaintenanceObject maintenanceObject;
    private ArrayList<MaintenanceObject> maintenanceObjects;

    private MaintenanceCategoryObject maintenanceCategoryObject;
    private ArrayList<MaintenanceCategoryObject> maintenanceCategoryObjects;

    private ProviderObject providerObject;
    private ArrayList<ProviderObject> providerObjects;

    private AdminMBObject adminMBObject;
    private ArrayList<AdminMBObject> adminMBObjects;
    private MBObject mbObject;
    private ArrayList<MBObject> mbObjects;



    private PushObject pushObject;
    private ArrayList<PushObject> pushObjectsMember;
    private ArrayList<PushObject> pushObjects;

    private PetObject petObject;
    private ArrayList<PetObject> petObjects;

    private AutoObject autoObject;
    private ArrayList<AutoObject> autoObjects;

    private GuestObject guestObject;
    private ArrayList<GuestObject> guestObjects;
    private GuestAccessObject guestAccessObject;
    private ArrayList<GuestAccessObject> guestAccessObjects;

    private String memberType;
    private String associationCode;
    private String memberNumber;




    private int i = 0;
    private int j = 0;
    private int k = 0;





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

                                    String[] rosterFileArray;
                                    String rosterFileString;
                                    try {
                                        ParseFile rosterFile = associationObject.get(0).getParseFile("RosterFile");
                                        byte[] rosterFileData;
                                        rosterFileData = rosterFile.getData();
                                        rosterFileString = new String(rosterFileData, "UTF-8");
                                        editorVisited = sharedPreferencesVisited.edit();
                                        editorVisited.putString("ROSTER", rosterFileString);
                                        editorVisited.commit();
                                    } catch (ParseException | UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                        rosterFileString = sharedPreferencesVisited.getString("ROSTER", "");
                                    }


                                    Log.d(TAG, "rosterFileString is " + rosterFileString);

                                    rosterFileArray = rosterFileString.split("\\|", -1);


                                    for (String member : rosterFileArray) {

                                        member.trim();

                                        Log.d(TAG, "rosterFileArray member is " + member);

                                    }


                                    rosterObjects = new ArrayList<>();

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

                                        if (memberNumber.equals(rosterObject.getMemberNumber())) {
                                            String groupsForParseChannels[] = rosterFields[25].split(",");
                                            final List channelsToAdd = Arrays.asList(groupsForParseChannels);
                                            installation.put("memberName", rosterFields[2] + " " + rosterFields[1]);
                                            installation.put("MemberType", rosterFields[21]);
                                            installation.put("channels", channelsToAdd);
                                            installation.saveEventually();

                                        }

                                        Log.d(TAG, "MEMBERTYPE ----> " + installation.getString("MemberType") + "<----");

                                        rosterObjects.add(rosterObject);


                                        editor = sharedPreferences.edit();
                                        Gson gson = new Gson();
                                        String jsonRosterObject = gson.toJson(rosterObject); // myObject - instance of MyObject
                                        editor.putString("rosterObject" + "[" + i + "]", jsonRosterObject);
                                        editor.putInt("rosterSize", rosterObjects.size());

                                        Log.d(TAG, "rosterObject added ----> " + jsonRosterObject + "<----");
                                        Log.d(TAG, "rosterObjects size is ----> " + rosterObjects.size());

                                        if (rosterObject.getMemberNumber().equals(memberNumber)) {

                                            Log.d(TAG, "rosterObject memberNumber ----> " + rosterObject.getMemberNumber() + " " + memberNumber + "<----");
                                            editor.putString("ROSTEROBJECTMYINFO", jsonRosterObject);
                                            Log.d(TAG, "ROSTEROBJECTMYINFO added ----> " + jsonRosterObject + "<----");
                                        }

                                        editor.apply();

                                    }

//*************************************************************************CALENDAR************************************************************************************

                                    String[] eventFileArray;
                                    String eventFileString;
                                    try {
                                        ParseFile eventFile = associationObject.get(0).getParseFile("EventFile");
                                        byte[] eventFileData;
                                        eventFileData = eventFile.getData();
                                        eventFileString = new String(eventFileData, "UTF-8");
                                        editorVisited = sharedPreferencesVisited.edit();
                                        editorVisited.putString("EVENTS", eventFileString);
                                        editorVisited.commit();
                                    } catch (ParseException | UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                        eventFileString = sharedPreferencesVisited.getString("EVENTS", "");
                                    }


                                    eventFileArray = eventFileString.split("\\|", -1);


                                    for (String event : eventFileArray) {

                                        event.trim();


                                        Log.v(TAG, "EVENT: " + event);

                                    }

                                    Log.v(TAG, "eventFileArray length: " + eventFileArray.length);


                                    calendarObjects = new ArrayList<>();

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

                                    String[] adminEventFileArray;
                                    String adminEventFileString;
                                    try {
                                        ParseFile adminEventFile = associationObject.get(0).getParseFile("AdminEventFile");
                                        byte[] adminEventFileData;

                                        if ( adminEventFile == null ) {
                                            adminEventFileString = "";
                                        } else {
                                            adminEventFileData = adminEventFile.getData();
                                            adminEventFileString = new String(adminEventFileData, "UTF-8");

                                        }


                                        editorVisited = sharedPreferencesVisited.edit();
                                        editorVisited.putString("ADMINEVENTS", adminEventFileString);
                                        editorVisited.commit();
                                    } catch (ParseException | UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                        adminEventFileString = sharedPreferencesVisited.getString("ADMINEVENTS", "");
                                    }


                                    adminEventFileArray = adminEventFileString.split("\\|", -1);


                                    for (String eventInfo : adminEventFileArray) {

                                        eventInfo.trim();


                                        Log.v(TAG, "EVENTINFO: " + eventInfo);

                                    }

                                    Log.v(TAG, "eventFileArray length: " + adminEventFileArray.length);


                                    adminCalendarObjects = new ArrayList<>();

                                    for (i = 1, j = 0, k = 0; i < adminEventFileArray.length; i++) {

                                        switch (j) {
                                            case 0:
                                                adminCalendarObject = new CalendarObject();
                                                adminCalendarObject.setCalendarText(adminEventFileArray[i]);
                                                j = j + 1;
                                                break;
                                            case 1:
                                                adminCalendarObject.setCalendarDetailText(adminEventFileArray[i]);
                                                j = j + 1;
                                                break;
                                            case 2:
                                                adminCalendarObject.setCalendarStartDate(adminEventFileArray[i]);
                                                j = j + 1;
                                                break;
                                            case 3:
                                                adminCalendarObject.setCalendarEndDate(adminEventFileArray[i]);
                                                j = j + 1;
                                                break;
                                            case 4:
                                                adminCalendarObject.setCalendarSortDate(adminEventFileArray[i]);
                                                adminCalendarObjects.add(adminCalendarObject);

                                                Log.v(TAG, "adminCalendarObject ----> " + adminCalendarObject.toString());

                                                editor = sharedPreferences.edit();
                                                Gson gson = new Gson();
                                                String jsonCalendarObject = gson.toJson(adminCalendarObject); // myObject - instance of MyObject
                                                editor.putString("adminCalendarObject" + "[" + k + "]", jsonCalendarObject);
                                                editor.putString("adminCalendarSize", String.valueOf(adminCalendarObjects.size()));
                                                editor.apply();

                                                j = 0;
                                                k++;

                                                break;
                                        }


                                    }


//**************************************************************************PROVIDER************************************************************************************

                                    String[] providerFileArray;
                                    String providerFileString;
                                    try {
                                        ParseFile providerFile = associationObject.get(0).getParseFile("ProviderFile");
                                        byte[] providerFileData;
                                        providerFileData = providerFile.getData();
                                        providerFileString = new String(providerFileData, "UTF-8");
                                        editorVisited = sharedPreferencesVisited.edit();
                                        editorVisited.putString("PROVIDERS", providerFileString);
                                        editorVisited.commit();
                                    } catch (ParseException | UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                        providerFileString = sharedPreferencesVisited.getString("PROVIDERS", "");
                                    }

                                    Log.v(TAG, "providerFileString -----> " + providerFileString);

                                    providerFileArray = providerFileString.split("\\|", -1);

                                    Log.v(TAG, "providerFileArrayLength -----> " + providerFileArray.length);

                                    for (String providerField : providerFileArray) {

                                        Log.v(TAG, "providerField -----> " + providerField);


                                    }


                                    providerObjects = new ArrayList<>();


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


// ***************************************************************************MAINTENANCE***********************************************************************************

                                    try {
                                        if (!sharedPreferences.getString("defaultRecord(46)", "No").equals("No")) {

                                            String[] maintenanceCategoryFileArray;
                                            String maintenanceCategoryFileString;
                                            try {
                                                ParseFile maintenanceCategoryFile = associationObject.get(0).getParseFile("MaintenanceCategoryFile");
                                                byte[] mcFileData;
                                                mcFileData = maintenanceCategoryFile.getData();
                                                maintenanceCategoryFileString = new String(mcFileData, "UTF-8");
                                                editorVisited = sharedPreferencesVisited.edit();
                                                editorVisited.putString("MAINTENANCECATEGORY", maintenanceCategoryFileString);
                                                editorVisited.commit();
                                            } catch (ParseException | UnsupportedEncodingException e1) {
                                                e1.printStackTrace();
                                                maintenanceCategoryFileString = sharedPreferencesVisited.getString("MAINTENANCECATEGORY", "");
                                            }


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


                                            String[] maintenanceFileArray;
                                            String[] maintenanceItems;
                                            String maintenanceFileString = null;
                                            try {
                                                ParseFile maintenanceFile = associationObject.get(0).getParseFile("MaintenanceFile");
                                                byte[] mFileData = new byte[0];
                                                mFileData = maintenanceFile.getData();
                                                maintenanceFileString = "";
                                                maintenanceFileString = new String(mFileData, "UTF-8");
                                                editorVisited = sharedPreferencesVisited.edit();
                                                editorVisited.putString("MAINTENANCEITEMS", maintenanceFileString);
                                                editorVisited.commit();
                                            } catch (ParseException | UnsupportedEncodingException e1) {
                                                e1.printStackTrace();
                                                maintenanceFileString = sharedPreferencesVisited.getString("MAINTENANCEITEMS", "");
                                            }


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

                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();

                                        Log.d(TAG, "maintenance error ---> " + e1.toString());
                                    }

//******************************************************************************GUESTS********************************************************************************

                                    try {
                                        if (!sharedPreferences.getString("defaultRecord(39)", "No").equals("No")) {


                                            String[] guestAccessFileArray;
                                            String[] guestAccessItems;
                                            String guestAccessFileString;
                                            try {
                                                ParseFile guestAccessFile = associationObject.get(0).getParseFile("GuestAccessFile");
                                                byte[] guestAccessFileData;
                                                guestAccessFileData = guestAccessFile.getData();
                                                guestAccessFileString = new String(guestAccessFileData, "UTF-8");
                                                editorVisited = sharedPreferencesVisited.edit();
                                                editorVisited.putString("GUESTACCESS", guestAccessFileString);
                                                editorVisited.commit();
                                            } catch (ParseException | UnsupportedEncodingException e1) {
                                                e1.printStackTrace();
                                                guestAccessFileString = sharedPreferencesVisited.getString("GUESTACCESS", "");
                                            }

                                            Log.d(TAG, "guestAccessFileString ---->" + guestAccessFileString);
                                            guestAccessFileArray = guestAccessFileString.split("\\|", -1);


                                            guestAccessObjects = new ArrayList<>();

                                            i = 0;
                                            for (String accessItem : guestAccessFileArray) {

                                                Log.d(TAG, "accessItem ----> " + accessItem);

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


                                            String[] guestFileArray;
                                            String[] guestItems;
                                            String guestsFileString;
                                            try {
                                                ParseFile guestFile = associationObject.get(0).getParseFile("GuestFile");
                                                byte[] guestFileData;
                                                guestFileData = guestFile.getData();
                                                guestsFileString = new String(guestFileData, "UTF-8");
                                                editorVisited = sharedPreferencesVisited.edit();
                                                editorVisited.putString("GUESTS", guestsFileString);
                                                editorVisited.commit();
                                            } catch (ParseException | UnsupportedEncodingException e1) {
                                                e1.printStackTrace();
                                                guestsFileString = sharedPreferencesVisited.getString("GUESTS", "");
                                            }

                                            Log.d(TAG, "guestFileString ---->" + guestsFileString);
                                            guestFileArray = guestsFileString.split("\\|", -1);


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

                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        Log.d(TAG, "guests error ---> " + e1.toString());
                                    }

//***********************************************************************PUSH***************************************************************************************

                                    String[] pushHistoryFileArray;
                                    String[] pushHistoryFileArray2;
                                    String pushHistoryFileString;
                                    try {
                                        ParseFile pushHistoryFile = associationObject.get(0).getParseFile("PushFile");
                                        byte[] pushFileData;
                                        pushFileData = pushHistoryFile.getData();
                                        pushHistoryFileString = new String(pushFileData, "UTF-8");
                                        editorVisited = sharedPreferencesVisited.edit();
                                        editorVisited.putString("PUSHHISTORY", pushHistoryFileString);
                                        editorVisited.commit();
                                    } catch (ParseException | UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                        pushHistoryFileString = sharedPreferencesVisited.getString("PUSHHISTORY", "");
                                    }


                                    pushHistoryFileArray = pushHistoryFileString.split("\\|", -1);


                                    for (String pushMessage : pushHistoryFileArray) {

                                        pushMessage.trim();


                                         Log.v(TAG, "push message: " + pushMessage);

                                    }

                                    // Log.v(PM, "pushHistoryFileArray length: " + pushHistoryFileArray.length);

                                    pushObjects = new ArrayList<>();
                                    pushObjectsMember = new ArrayList<>();


                                    for (i = 1, j = 1, k = 0; i < pushHistoryFileArray.length; i++) {

                                        pushHistoryFileArray2 = pushHistoryFileArray[i].split("\\^", -1);

                                        for (String pushStringData : pushHistoryFileArray2) {
                                             Log.d(TAG, "push message pf2member is ---> " + pushStringData.toLowerCase() + " i= " + i + " j= " + j);

                                            switch (j) {
                                                case 1:
                                                    pushObject = new PushObject();
                                                    pushObject.setMonth(pushStringData);
                                                    j = j + 1;
                                                    break;
                                                case 2:
                                                    pushObject.setHoaAndGroup(pushStringData);
                                                    j = j + 1;
                                                    break;
                                                case 3:
                                                    pushObject.setDate(pushStringData);
                                                    j = j + 1;
                                                    break;
                                                case 4:
                                                    pushObject.setPushNotifacation(pushStringData);
                                                    pushObjects.add(pushObject);
                                                    editor = sharedPreferences.edit();
                                                    Gson gson = new Gson();
                                                    String jsonPushObject = gson.toJson(pushObject); // myObject - instance of MyObject
                                                    editor.putString("pushObject" + "[" + (i - 1) + "]", jsonPushObject);
                                                    editor.putInt("pushObjectsSize", pushObjects.size());
                                                    editor.commit();
                                                    j = 1;


                                                    if (!pushObject.toString().toLowerCase().replaceAll("\\s+", "").contains("admingroup")) {

                                                        Log.d(TAG, "member push message !contains admin  ---> " + pushObject.toString());

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


//***********************************************************************PETS*************************************************************************************************


                                    if (!sharedPreferences.getString("defaultRecord(34)", "No").equals("No")) {

                                        String[] petFileArray;
                                        String[] petFileArray2;
                                        String petsFileString;
                                        try {
                                            ParseFile petFile = associationObject.get(0).getParseFile("PetFile");
                                            byte[] petFileData;
                                            petFileData = petFile.getData();
                                            petsFileString = new String(petFileData, "UTF-8");
                                            editorVisited = sharedPreferencesVisited.edit();
                                            editorVisited.putString("PETS", petsFileString);
                                            editorVisited.commit();
                                        } catch (ParseException | UnsupportedEncodingException e1) {
                                            e1.printStackTrace();
                                            petsFileString = sharedPreferencesVisited.getString("PETS", "");
                                        }

                                        petFileArray = petsFileString.split("\\|", -1);


                                        for (String pet : petFileArray) {

                                            pet.trim();


                                            Log.v(TAG, "pet: " + pet);

                                        }

                                        Log.v(TAG, "petFileArray length: " + petFileArray.length);

                                        petObjects = new ArrayList<>();

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


                                    }


//************************************************************AUTOS**************************************************************************************************************


                                    if (!sharedPreferences.getString("defaultRecord(36)", "No").equals("No")) {


                                        String[] autoFileArray;
                                        String[] autoFileArray2;
                                        String autoFileString;
                                        try {
                                            ParseFile autoFile = associationObject.get(0).getParseFile("AutoFile");
                                            byte[] autoFileData;
                                            autoFileData = autoFile.getData();
                                            autoFileString = new String(autoFileData, "UTF-8");
                                            editorVisited = sharedPreferencesVisited.edit();
                                            editorVisited.putString("AUTOS", autoFileString);
                                            editorVisited.commit();
                                        } catch (ParseException | UnsupportedEncodingException e1) {
                                            e1.printStackTrace();
                                            autoFileString = sharedPreferencesVisited.getString("AUTOS", "");
                                        }


                                        autoFileArray = autoFileString.split("\\|", -1);


                                        for (String auto : autoFileArray) {

                                            auto.trim();


                                            Log.v(TAG, "auto ---> " + auto);

                                        }

                                        Log.v(TAG, "autoFileArray length ---> " + autoFileArray.length);

                                        autoObjects = new ArrayList<>();

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

                                    }
//***************************************************************************MESSAGE***********************************************************************************

                                    String[] postFileArray;
                                    String postsFileString;
                                    try {
                                        ParseFile postsFile = associationObject.get(0).getParseFile("MessageFile");
                                        byte[] postFileData;
                                        postFileData = postsFile.getData();
                                        postsFileString = new String(postFileData, "UTF-8");
                                        editorVisited = sharedPreferencesVisited.edit();
                                        editorVisited.putString("POSTS", postsFileString);
                                        editorVisited.commit();
                                    } catch (ParseException | UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                        postsFileString = sharedPreferencesVisited.getString("POSTS", "");
                                    }


                                    postFileArray = postsFileString.split("\\|", -1);


                                    for (int i = 0; i < postFileArray.length; i++) {


                                        Log.v(TAG, i + " post " + postFileArray[i]);


                                    }


                                    mbObjects = new ArrayList<>();


                                    for (i = 0, j = 0; i < postFileArray.length; i++) {

                                        switch (j) {
                                            case 0:
                                                mbObject = new MBObject();
                                                mbObject.setMbName(postFileArray[i]);
                                                j++;
                                                break;
                                            case 1:
                                                mbObject.setMbPostDate(postFileArray[i]);
                                                j++;
                                                break;
                                            case 2:
                                                mbObject.setMbPost(postFileArray[i]);
                                                j++;
                                                break;
                                            case 3:
                                                mbObject.setMbPostDate2(postFileArray[i]);
                                                j++;
                                                break;

                                            case 4:
                                                mbObject.setMbPosterEmailAddress(postFileArray[i]);
                                                mbObjects.add(mbObject);
                                                editor = sharedPreferences.edit();
                                                Gson gson = new Gson();
                                                String jsonMbObject = gson.toJson(mbObject); // myObject - instance of MyObject
                                                editor.putString("mbObject" + "[" + (((i + 1) / 5) - 1) + "]", jsonMbObject);
                                                editor.putInt("mbSize", postFileArray.length / 5);
                                                editor.apply();


                                                j = 0;

                                                break;
                                        }


                                    }


//***************************************************************************ADMIN MESSAGE**********************************************************************************
                                    if (!sharedPreferences.getString("defaultRecord(48)", "No").equals("No")) {


                                        String[] admin_postFileArray;
                                        String admin_postsFileString;
                                        try {
                                            ParseFile admin_postsFile = associationObject.get(0).getParseFile("AdminMessageFile");
                                            byte[] admin_postFileData;
                                            admin_postFileData = admin_postsFile.getData();
                                            admin_postsFileString = new String(admin_postFileData, "UTF-8");
                                            editorVisited = sharedPreferencesVisited.edit();
                                            editorVisited.putString("ADMINPOSTS", admin_postsFileString);
                                            editorVisited.commit();
                                        } catch (ParseException | UnsupportedEncodingException e1) {
                                            e1.printStackTrace();
                                            admin_postsFileString = sharedPreferencesVisited.getString("ADMINPOSTS", "");
                                        }


                                        Log.v(TAG, "adminPostsFileString ---> " + admin_postsFileString);


                                        if (admin_postsFileString.equals("") || admin_postsFileString == null) {

                                            editorVisited = sharedPreferencesVisited.edit();
                                            editorVisited.putInt("admin_mbSize", 0);
                                            editorVisited.apply();


                                        } else {

                                            admin_postFileArray = admin_postsFileString.split("\\|", -1);

                                            adminMBObjects = new ArrayList<>();

                                            int adminmessageCount = 0;

                                            for (i = 0, j = 0; i < admin_postFileArray.length; i++) {

                                                switch (j) {
                                                    case 0:
                                                        adminMBObject = new AdminMBObject();
                                                        adminMBObject.setPostName(admin_postFileArray[i].trim());
                                                        j++;
                                                        break;
                                                    case 1:
                                                        adminMBObject.setPostDate(admin_postFileArray[i].trim());
                                                        j++;
                                                        break;
                                                    case 2:
                                                        adminMBObject.setPostText(admin_postFileArray[i].trim());
                                                        j++;
                                                        break;
                                                    case 3:
                                                        adminMBObject.setPostSort(admin_postFileArray[i].trim());
                                                        j++;
                                                        break;
                                                    case 4:
                                                        adminMBObject.setPostEmail(admin_postFileArray[i].trim());
                                                        j++;
                                                        break;
                                                    case 5:
                                                        adminMBObject.setPostComment(admin_postFileArray[i].trim());
                                                        j++;
                                                        break;
                                                    case 6:
                                                        adminMBObject.setPostNameComment(admin_postFileArray[i].trim());
                                                        j++;
                                                        break;
                                                    case 7:
                                                        adminMBObject.setPostOriginalText(admin_postFileArray[i].trim());
                                                        adminMBObjects.add(adminMBObject);
                                                        adminmessageCount++;
                                                        editor = sharedPreferences.edit();
                                                        editorVisited = sharedPreferencesVisited.edit();
                                                        Gson gson = new Gson();
                                                        String jsonAdminMbObject = gson.toJson(adminMBObject); // myObject - instance of MyObject
                                                        editor.putString("admin_mbObject" + "[" + (((i + 1) / 8) - 1) + "]", jsonAdminMbObject);
                                                        editorVisited.putInt("admin_mbSize", adminmessageCount);
                                                        editorVisited.apply();
                                                        editor.apply();

                                                        j = 0;

                                                        break;
                                                }

                                            }
                                        }

                                    }

//************************************************************************PDFS**************************************************************************************************


                                    ParseFile budgetFile = associationObject.get(0).getParseFile("BudgetFile");
                                    ParseFile byLawsFile = associationObject.get(0).getParseFile("ByLawsFile");
                                    ParseFile expenseFile = associationObject.get(0).getParseFile("ExpenseFile");
                                    ParseFile rulesFile = associationObject.get(0).getParseFile("RulesFile");
                                    ParseFile minutesFile = associationObject.get(0).getParseFile("MinutesFile");
                                    ParseFile misc1File = associationObject.get(0).getParseFile("MiscDoc1File");
                                    ParseFile misc2File = associationObject.get(0).getParseFile("MiscDoc2File");
                                    ParseFile misc3File = associationObject.get(0).getParseFile("MiscDoc3File");




                                    try {
                                        editor.putString("budgetpdfurl", budgetFile.getUrl());
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        // Log.d(TAG, "no such pdf file ");
                                        editor.putString("budgetpdfurl", null);
                                    }
                                    try {
                                        editor.putString("bylawspdfurl", byLawsFile.getUrl());
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        // Log.d(TAG, "no such pdf file ");
                                        editor.putString("bylawspdfurl", null);
                                    }
                                    try {
                                        editor.putString("expensepdfurl", expenseFile.getUrl());
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        // Log.d(TAG, "no such pdf file ");
                                        editor.putString("expensepdfurl", null);
                                    }
                                    try {
                                        editor.putString("rulespdfurl", rulesFile.getUrl());
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        // Log.d(TAG, "no such pdf file ");
                                        editor.putString("rulespdf", null);
                                    }
                                    try {
                                        editor.putString("minutespdfurl", minutesFile.getUrl());
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        // Log.d(TAG, "no such pdf file ");
                                        editor.putString("minutespdfurl", null);
                                    }
                                    try {
                                        editor.putString("m1pdfurl", misc1File.getUrl());
                                        editor.putString("m1pdfname", misc1File.getName().replaceAll(" ",""));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        // Log.d(TAG, "no such pdf file ");
                                        editor.putString("m1pdfurl", null);
                                    }
                                    try {
                                        editor.putString("m2pdfurl", misc2File.getUrl());
                                        editor.putString("m2pdfname", misc2File.getName().replaceAll(" ",""));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        // Log.d(TAG, "no such pdf file ");
                                        editor.putString("m2pdfurl", null);
                                    }
                                    try {
                                        editor.putString("m3pdfurl", misc3File.getUrl());
                                        editor.putString("m3pdfname", misc3File.getName().replaceAll(" ",""));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        // Log.d(TAG, "no such pdf file ");
                                        editor.putString("m3pdfurl", null);
                                    }

                                    editor.apply();


//***********************************************************************************************************************************************

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
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
import java.util.List;

import comapps.com.myassociationhoa.objects.AdminMBObject;
import comapps.com.myassociationhoa.objects.MBObject;

/**
 * Created by me on 8/28/2016.
 */
@SuppressWarnings("ALL")
public class RemoteDataTaskClassMB extends AsyncTask<Void, Void, Void> {

    private final Context context;

    public RemoteDataTaskClassMB(Context context) {
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




    private AdminMBObject adminMBObject;
    private MBObject mbObject;
    private ArrayList<AdminMBObject> adminMBObjects;
    private ArrayList<MBObject> mbObjects;







    private String memberType;
    private String associationCode;
    private String memberNumber;




    private int i = 0;
    private int j = 0;
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


                                    String[] postFileArray = new String[0];
                                    String postsFileString = null;
                                    try {
                                        ParseFile postsFile = associationObject.get(0).getParseFile("MessageFile");
                                        postFileArray = null;
                                        byte[] postFileData = new byte[0];
                                        postFileData = postsFile.getData();
                                        postsFileString = null;
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


                                    String[] admin_postFileArray = new String[0];
                                    String admin_postsFileString = null;
                                    try {
                                        ParseFile admin_postsFile = associationObject.get(0).getParseFile("AdminMessageFile");
                                        admin_postFileArray = null;
                                        byte[] admin_postFileData = new byte[0];
                                        admin_postFileData = admin_postsFile.getData();
                                        admin_postsFileString = "";
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
//************************************************************************************************************************************************

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
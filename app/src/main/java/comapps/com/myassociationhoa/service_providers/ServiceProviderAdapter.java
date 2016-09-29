package comapps.com.myassociationhoa.service_providers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.ServiceProviderObject;

/**
 * Created by me on 6/28/2016.
 */
class ServiceProviderAdapter extends BaseAdapter {

    public static final String TAG = "SERVICEPROVIDERADAPTER";

    private static final String MYPREFERENCES = "MyPrefs";

    private SharedPreferences sharedPreferences;

    private final Context context;
    private final ArrayList<ServiceProviderObject> providerList;
    private final LayoutInflater inflater;
    private List<ServiceProviderObject> list = null;


    public ServiceProviderAdapter(Context context, List<ServiceProviderObject> list) {

        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.providerList = new ArrayList<ServiceProviderObject>();
        this.providerList.addAll(list);

        sharedPreferences = getContext().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);


    }

    private Context getContext() {
        return context;
    }


    public class ViewHolder {


        public TextView providerName;
        public TextView providerAddress;
        public TextView providerAddress2;
        public TextView providerCity;
        public TextView providerState;
        public TextView providerZip;
        public Button callButton;
        public Button editButton;
        public TextView providerNotes;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder holder;


        if (view == null) {


            holder = new ViewHolder();
            view = inflater.inflate(R.layout.content_main_provider_row_detail, parent, false);
            holder.providerName = (TextView) view.findViewById(R.id.textViewProviderName);
            holder.providerAddress = (TextView) view.findViewById(R.id.textViewProviderAddress);
            holder.providerAddress2 = (TextView) view.findViewById(R.id.textViewProviderAddress2);
            holder.providerCity = (TextView) view.findViewById(R.id.textViewProviderCity);
            holder.providerState = (TextView) view.findViewById(R.id.textViewProviderState);
            holder.providerZip = (TextView) view.findViewById(R.id.textViewProviderZip);
            holder.providerNotes = (TextView) view.findViewById(R.id.textViewProviderNotes);

            holder.callButton = (Button) view.findViewById(R.id.callButton);
            holder.editButton = (Button) view.findViewById(R.id.editButton);


            view.setTag(holder);


        } else {

            holder = (ViewHolder) view.getTag();
        }


        holder.providerName.setText(providerList.get(position).getProviderName());
        holder.providerAddress.setText(providerList.get(position).getProviderAddress());
        holder.providerAddress2.setText(providerList.get(position).getProviderAddress2());
        holder.providerCity.setText(providerList.get(position).getProviderCity());
        holder.providerState.setText(providerList.get(position).getProviderState());
        holder.providerZip.setText(providerList.get(position).getProviderZip());
        holder.providerNotes.setText(providerList.get(position).getProviderNotes());
        holder.callButton.setText(providerList.get(position).getProviderPhoneNumber());


        final String phoneNumber = providerList.get(position).getProviderPhoneNumber();

        if (sharedPreferences.getString("defaultRecord(47)", "No").equals("No") && ParseInstallation.getCurrentInstallation().get("MemberType").equals("Member")) {

            holder.editButton.setVisibility(View.GONE);

        }


        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Log.d("TAG", "call button pressed");
                    Uri uri = Uri.parse("tel:" + phoneNumber);
                    Intent i = new Intent(Intent.ACTION_DIAL, uri);
                    getContext().startActivity(i);


                } catch (Exception e) {


                }

            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Gson gson = new Gson();
                    String jsonString;

                    Intent intentEditServiceProvider = new Intent();
                    intentEditServiceProvider.setClass(getContext(), ServiceProviderEditActivity.class);
                    intentEditServiceProvider.putExtra("serviceProviderObjectGson", gson.toJson(providerList.get(position)));
                    intentEditServiceProvider.putExtra("FROMSERVICEPROVIDERADD", "NO");
                    intentEditServiceProvider.putExtra("PROVIDERTYPE", "");
                    getContext().startActivity(intentEditServiceProvider);


                } catch (Exception e) {


                }

            }
        });


        return view;

    }


}

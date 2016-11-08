package comapps.com.myassociationhoa.service_providers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comapps.com.myassociationhoa.R;
import comapps.com.myassociationhoa.objects.ProviderObject;

/**
 * Created by me on 6/28/2016.
 */
@SuppressWarnings("ALL")
class ServiceAdapter extends BaseAdapter {

    public static final String TAG = "SERVICEADAPTER";

    private final Context context;
    private final ArrayList<ProviderObject> providerObjectList;
    private final LayoutInflater inflater;
    private List<ProviderObject> servicesList = null;


    public ServiceAdapter(Context context, List<ProviderObject> servicesList) {

        this.context = context;
        this.servicesList = servicesList;
        inflater = LayoutInflater.from(context);
        this.providerObjectList = new ArrayList<>();
        this.providerObjectList.addAll(servicesList);


    }

    private Context getContext() {
        return context;
    }


    public class ViewHolder {


        public TextView serviceItem;
        public ImageView addButtonImageView;


    }

    @Override
    public int getCount() {
        return servicesList.size();
    }

    @Override
    public Object getItem(int position) {
        return servicesList.get(position);
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
            view = inflater.inflate(R.layout.content_main_providers_listview_layout, parent, false);
            holder.serviceItem = (TextView) view.findViewById(R.id.textProvider);
            holder.addButtonImageView = (ImageView) view.findViewById(R.id.addButton);



            view.setTag(holder);


        } else {

            holder = (ViewHolder) view.getTag();
        }


        holder.serviceItem.setText(servicesList.get(position).getProviderType() + " (" + servicesList.get(position).getProviderCount() + ")");

        if ( servicesList.get(position).getProviderCount().equals("0")) {

            holder.addButtonImageView.setVisibility(View.VISIBLE);

        }



        return view;

    }


}

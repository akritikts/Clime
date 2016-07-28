package in.silive.clime.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import in.silive.clime.R;

/**
 * Created by akriti on 23/7/16.
 */
public class HListAdapter extends BaseAdapter {
    LayoutInflater inflater;

    public HListAdapter() {
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;
        view1 = inflater.inflate(R.layout.h_list_adapter, null);
        Holder holder = new Holder();
        holder.h_temp = (TextView) view1.findViewById(R.id.h_temp);
        holder.h_details = (TextView) view1.findViewById(R.id.h_details);
        holder.h_summary = (TextView) view1.findViewById(R.id.h_summary);
        holder.h_time = (TextView) view1.findViewById(R.id.h_time);
        holder.h_img = (ImageView) view1.findViewById(R.id.h_img);
        return view;
    }

    public class Holder {
        TextView h_temp;
        TextView h_details;
        TextView h_summary;
        TextView h_time;
        ImageView h_img;

    }
}

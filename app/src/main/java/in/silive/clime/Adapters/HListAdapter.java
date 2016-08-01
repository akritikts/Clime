package in.silive.clime.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnhiott.darkskyandroidlib.models.DataPoint;

import java.util.ArrayList;
import java.util.List;

import in.silive.clime.R;

/**
 * Created by akriti on 23/7/16.
 */
public class HListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<DataPoint> list;
    ArrayList<String> res_temp;
    ArrayList<String> res_summary;

    public HListAdapter() {
    }

    public HListAdapter(LayoutInflater inflater, List list) {
        this.inflater = inflater;
        this.list = list;
    }


    /*public HListAdapter(LayoutInflater inflater,ArrayList<String> res_summary, ArrayList<String> res_temp) {
        this.inflater = inflater;
        this.res_summary = res_summary;
        this.res_temp = res_temp;
    }
*/
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
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
        DataPoint dp =  list.get(i);
        holder.h_temp.setText(String.valueOf(dp.getTemperature()));
        holder.h_summary.setText(dp.getSummary());
        return view1;
    }

    public class Holder {
        TextView h_temp;
        TextView h_details;
        TextView h_summary;
        TextView h_time;
        ImageView h_img;
    }
}

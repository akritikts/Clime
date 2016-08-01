package in.silive.clime.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import in.silive.clime.Adapters.HListAdapter;
import in.silive.clime.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HourFragment extends Fragment {
    ListView hrs_list;
     public  static List list;


    public HourFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hour, container, false);
        hrs_list = (ListView)view.findViewById(R.id.hrs_list);
        hrs_list.setAdapter(new HListAdapter(getActivity().getLayoutInflater(),list));
        return view;
    }

    public void setList(List list) {
        this.list = list;
    }
}

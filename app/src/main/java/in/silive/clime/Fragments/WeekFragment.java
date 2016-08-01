package in.silive.clime.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class WeekFragment extends Fragment {
    ListView wk_list;
    public static List list;


    public WeekFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        wk_list = (ListView)view.findViewById(R.id.wk_list);
        wk_list.setAdapter(new HListAdapter(getActivity().getLayoutInflater(),list));
        return view;
    }
    public void setList(List list) {
        this.list = list;
    }

}

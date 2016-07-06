package in.silive.clime.Fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import in.silive.clime.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogSearch extends DialogFragment implements View.OnClickListener {
    public static String search;
    private EditText city;
    private Button ok, cancel;

    public DialogSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setCancelable(true);
        getDialog().setTitle("Search City");
        View view = inflater.inflate(R.layout.fragment_dialog_search, container, false);
        city = (EditText) view.findViewById(R.id.city);
        ok = (Button) view.findViewById(R.id.ok);
        ok.setOnClickListener(this);
        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                search = city.getText().toString();
                Log.d("TAG", "user's search request : " + search);
                setSearch(search);

                break;
            case R.id.cancel:
                dismiss();
                break;
        }

    }

    public String getSearch() {
        Log.d("TAG", "user's search request : " + search + " returned");
        return search;
    }

    public void setSearch(String search) {
        this.search = search;

    }
}

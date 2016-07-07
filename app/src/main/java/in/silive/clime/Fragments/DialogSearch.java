package in.silive.clime.Fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
        city.setSingleLine(true);
        city.setInputType(InputType.TYPE_CLASS_TEXT);
        city.setMaxLines(1);
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
                setSearch(search,1);
                dismiss();

                break;
            case R.id.cancel:
                search = city.getText().toString();
                Log.d("TAG", "user's search request cancel: " + search);
                setSearch(search,2);
                dismiss();
                break;
        }

    }

    public void setSearch(String search,int flag) {
        this.search = search;
        this.listener.SetData(search,flag);
    }
    public static interface Listener{
        public void SetData(String data,int id);
    }
    Listener listener;
    public void setListener(Listener listener){
        this.listener = listener;
    }
}

package in.silive.clime.Fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.silive.clime.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogGPS extends DialogFragment implements View.OnClickListener {
    Button gps_ok;



    public DialogGPS() {
        super();
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
        setCancelable(false);
        getDialog().setTitle("Settings Alert");
        View view = inflater.inflate(R.layout.fragment_dialog_gps, container, false);
        gps_ok = (Button) view.findViewById(R.id.gps_ok);
        gps_ok.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.gps_ok) {
            dismiss();
        }

    }
}

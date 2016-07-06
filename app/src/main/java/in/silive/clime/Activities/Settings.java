package in.silive.clime.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import in.silive.clime.R;

public class Settings extends AppCompatActivity {
    private Toolbar mt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mt = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mt);
        mt.setTitle("Settings");
    }
}

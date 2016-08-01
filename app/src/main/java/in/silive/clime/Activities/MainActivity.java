package in.silive.clime.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.DataPoint;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.silive.clime.Adapters.VPagerAdapter;
import in.silive.clime.Fragments.DialogGPS;
import in.silive.clime.Fragments.DialogSearch;
import in.silive.clime.Fragments.HourFragment;
import in.silive.clime.Fragments.WeekFragment;
import in.silive.clime.Models.Constants;
import in.silive.clime.Models.GetCityLocation;
import in.silive.clime.Models.WeatherData;
import in.silive.clime.R;
import in.silive.clime.Services.FetchAddressIntentService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static String LSummary,LSummaryW;
    public static String LTemp,LTempW;
    public static List list,l;
    static String mAddressOutput;
    final boolean mAddressRequested = true;
    Context context;
    double latitude, longitude;
    WeatherData weatherData;
    LinearLayout weather_info;
    TextView city_text, temp, temp_unit, sky_desc, current_time, date_day, current_time_min;
    ImageView icon;
    ImageButton ref;
    TextView humidity, dew, cloud, precip;
    ViewPager vw_pager;
    String APIKey = "5b29d34aeee88dc47264e71ed058a592";
    boolean mRequestingLocationUpdates = true;
    LocationListener mLocationListener;
    String mLastUpdateTime;
    String serach_city;
    int i;// flag for search city
    GetCityLocation getCityLocation;
    Animation animation;
    VPagerAdapter vPagerAdapter;
    private Toolbar mToolbar;
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private AddressResultReceiver mResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "MainActivity created");
        context = getApplicationContext();
        //Initializing the layout elements
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Clime");
        animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        weather_info = (LinearLayout) findViewById(R.id.weather_info);
        current_time = (TextView) findViewById(R.id.current_time);
        current_time_min = (TextView) findViewById(R.id.current_time_min);
        date_day = (TextView) findViewById(R.id.date_day);
        city_text = (TextView) findViewById(R.id.city_text);
        temp = (TextView) findViewById(R.id.temp);
        temp_unit = (TextView) findViewById(R.id.temp_unit);
        humidity = (TextView) findViewById(R.id.humidity);
        dew = (TextView) findViewById(R.id.dew);
        precip = (TextView) findViewById(R.id.precip);
        cloud = (TextView) findViewById(R.id.cloud);
        sky_desc = (TextView) findViewById(R.id.sky_desc);
        icon = (ImageView) findViewById(R.id.icon);
        vw_pager = (ViewPager) findViewById(R.id.vw_pager);

        ref = (ImageButton) findViewById(R.id.ref);
        ref.setAnimation(animation);
        animation.cancel();
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayLocation();
                new GetData(MainActivity.this).execute();
            }
        });
        Log.d("TAG", "Layout initialized");
        Toast.makeText(this, "Please wait for the data to finish loading", Toast.LENGTH_SHORT).show();

        updateValuesFromBundle(savedInstanceState);
        Log.d("TAG", "Updated layout from bundle");
        checkConnection();


        // First we need to check availability of play services
        if (checkPlayServices()) {
            Log.d("TAG", "Play services available");
            buildGoogleApiClient();
            Log.d("TAG", "Google aPI client built");
        }
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                displayLocation();

            }
        };
        weatherData = new WeatherData();
        mResultReceiver = new AddressResultReceiver(new Handler());
        Log.d("TAG", "Result receiver initialised");
        ForecastApi.create(APIKey);
        Log.d("TAG", "Weather API created");
        new GetData(this).execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // mToolbar.setTitle("Clime");
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_search) {
            Log.d("TAG", "search pressed");
            DialogSearch dialogSearch = new DialogSearch();
            dialogSearch.show(getFragmentManager(), "Select City");
            Log.d("TAG", "dialog created");
            dialogSearch.setListener(new DialogSearch.Listener() {
                @Override
                public void SetData(String data, int id) {
                    serach_city = data;
                    i = id;
                    Log.d("TAG", "dialog values initialised");
                    Log.d("TAG", "city received in MainAct" + serach_city);
                    if (i == 1) {
                        getCityLocation = new GetCityLocation(context, serach_city);
                        latitude = getCityLocation.getLat();
                        longitude = getCityLocation.getLng();
                        mLastLocation = getCityLocation.getLocation();
                        Log.d("TAG", latitude + " " + longitude + "inside the updated dialog");
                        startIntentService();
                        Log.d("TAG", "intent service started after dialog");
                    }

                }
            });

            //new GetData(context,2).execute();


        }


        return super.onOptionsItemSelected(item);
    }


    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            Snackbar snackbar = Snackbar
                    .make(weather_info, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            checkConnection();
                        }
                    });

// Changing message text color
            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        } else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startIntentService();
                }
            }, 2000);


        }

    }

    void displayLocation() {
        Log.d("TAG", "Getting location");
        if (Build.VERSION.SDK_INT >= 24 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            Log.d("TAG", latitude + " " + longitude + "inside display");
            //String place = GetCity(latitude, longitude);
            startIntentService();
            String place = mResultReceiver.getAddress();
            Log.d("TAG", place + " ");
        } else {
            Log.d("TAG", "no location");
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    public void showAlert() {
        DialogGPS dialogGPS = new DialogGPS();
        dialogGPS.show(getFragmentManager(), "Alert");
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(resultCode)) {
                api.showErrorDialogFragment(this, resultCode, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE);
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();
        if (mAddressRequested) {
            startIntentService();
        }
        //mListAdapter.setGoogleApiclient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    protected void startIntentService() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.d("TAG", "start service excites GetData");

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("TAG", "dialog");
            showAlert();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 2000);
            Intent intent = new Intent(this, FetchAddressIntentService.class);
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
            startService(intent);
            new GetData(context).execute();
        }
    }

    public void updateTimeOnEachSecond() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                final Calendar c;
                c = Calendar.getInstance();
                Log.d("TAG", "time changed");
                final int hrs = c.get(Calendar.HOUR_OF_DAY);
                final int min = c.get(Calendar.MINUTE);
                final int sec = c.get(Calendar.SECOND);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        current_time.setText(String.valueOf(hrs + " :"));
                        current_time_min.setText(String.valueOf(min));
                        /*mLastUpdateTime = String.valueOf(hrs+":");
                        mLastUpdateTime.concat(String.valueOf(min + " :"));
                        mLastUpdateTime.concat(String.valueOf(sec));*/
                    }
                });

            }
        }, 0, 1000);

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("REQUESTING_LOCATION_UPDATES_KEY",
                mRequestingLocationUpdates);
        savedInstanceState.putParcelable("LOCATION_KEY", mLastLocation);
        savedInstanceState.putString("LAST_UPDATED_TIME_STRING_KEY", mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains("REQUESTING_LOCATION_UPDATES_KEY")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        "REQUESTING_LOCATION_UPDATES_KEY");
                //setButtonsEnabledState();
            }
            if (savedInstanceState.keySet().contains("LOCATION_KEY")) {
                mLastLocation = savedInstanceState.getParcelable("LOCATION_KEY");
            }
            if (savedInstanceState.keySet().contains("LAST_UPDATED_TIME_STRING_KEY")) {
                mLastUpdateTime = savedInstanceState.getString(
                        "LAST_UPDATED_TIME_STRING_KEY");
                updateTimeOnEachSecond();
            }

        }
    }

    /*public void fetchAddressButtonHandler(View view) {
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
        *//*updateUIWidgets();*//*
    }*/
    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        String str;

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            Log.d("TAG", "result received");
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            city_text.setText(mAddressOutput);
            Log.d("TAG", "result received " + mAddressOutput);
            str = mAddressOutput;
            //displayAddressOutput();
            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(context, "address_found", Toast.LENGTH_SHORT).show();
            }
        }

        public String getAddress() {
            Log.d("TAG", "result received getter : " + mAddressOutput);

            return str;
        }
    }

    //Class to get API data
    public class GetData extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        int flag;


        public GetData(Context c) {
            this.progressDialog = new ProgressDialog(MainActivity.this);
            this.flag = flag;
            Log.d("TAG", latitude + " " + longitude + "inside getData");
        }

        public void UpdateUI(WeatherData getData) {
            temp.setText(" " + getData.getTemperature());
            temp_unit.setText("C");
            sky_desc.setText(getData.getDesc());
            date_day.setText(getData.getMydate());
            cloud.setText("Pressure : " + getData.getPres());
            Log.d("TAG", getData.getMax() + " " + getData.getMin() + "max min");
            Log.d("TAG", getData.getPres() + " " + "pressure");
            precip.setText("Precipitation : " + getData.getPrec());
            humidity.setText("Humidity : " + getData.getHumid());
            dew.setText("Dew Point : " + getData.getDewp());
            vPagerAdapter = new VPagerAdapter(getSupportFragmentManager(),list,l);
            vw_pager.setAdapter(vPagerAdapter);
            vw_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (position) {

                        case 0:
                            HourFragment fg = new HourFragment();
                            fg.setList(list);


                        case 1:
                            WeekFragment wf = new WeekFragment();
                            wf.setList(l);



                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            updateTimeOnEachSecond();

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setMessage("Loading");
            // progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            progressDialog.show();
            animation.start();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 1000);
            animation.cancel();

        }


        @Override
        protected String doInBackground(Void... params) {

            Log.d("TAG", latitude + "" + longitude);

            RequestBuilder weather = new RequestBuilder();
            Request request = new Request();
            request.setLat(latitude + "");
            request.setLng(longitude + "");
            request.setUnits(Request.Units.UK);
            request.setLanguage(Request.Language.ENGLISH);
            request.addExcludeBlock(Request.Block.CURRENTLY);
            request.removeExcludeBlock(Request.Block.CURRENTLY);
            weather.getWeather(request, new Callback<WeatherResponse>() {
                @Override
                public void success(WeatherResponse weatherResponse, Response response) {
                    Log.d("TAG", "Temp: " + weatherResponse.getCurrently().getTemperature());
                    Log.d("TAG", "Summary: " + weatherResponse.getCurrently().getSummary());
                    Log.d("TAG", "Hourly Sum: " + weatherResponse.getHourly().getSummary());
                    weatherData.setTemperature(weatherResponse.getCurrently().getTemperature());
                    weatherData.setDesc(weatherResponse.getCurrently().getSummary());
                    String img = weatherResponse.getCurrently().getIcon();
                    //icon.setImageResource(Integer.parseInt(img));
                    //setIcon(img);
                    weatherData.setHrs(weatherResponse.getHourly().getSummary());
                    weatherData.setMydate(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
                    weatherData.setPres(weatherResponse.getCurrently().getPressure());
                    weatherData.setPrec(weatherResponse.getCurrently().getPrecipIntensity());
                    weatherData.setHumid(weatherResponse.getCurrently().getHumidity());
                    weatherData.setDewp(weatherResponse.getCurrently().getDewPoint());
                    weatherData.setMax(weatherResponse.getCurrently().getTemperatureMax());
                    weatherData.setMin(weatherResponse.getCurrently().getTemperatureMin());
                    list = weatherResponse.getHourly().getData();
                    DataPoint dp = (DataPoint) list.get(0);
                    LTemp = String.valueOf(dp.getTemperature());
                    LSummary = dp.getSummary();
                    Log.d("Tag2", "List hourly" + list);
                    Log.d("tag2", String.valueOf(dp.getTemperature()));
                    Toast.makeText(MainActivity.this, String.valueOf(dp.getTemperature()), Toast.LENGTH_SHORT).show();
                     l = weatherResponse.getDaily().getData();
                    DataPoint dataPoint = (DataPoint)l.get(1);
                    LTempW = String.valueOf(dataPoint.getTemperature()) ;
                    LSummaryW= String.valueOf(dataPoint.getSummary()) ;
                    Log.d("tag3", String.valueOf(dataPoint.getSummary()));
                    Toast.makeText(MainActivity.this, String.valueOf(dataPoint.getSummary()), Toast.LENGTH_SHORT).show();
                    UpdateUI(weatherData);

                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Log.d("TAG", "Error while calling: " + retrofitError.getUrl());
                    Log.d("TAG", retrofitError.toString());
                }
            });


            return null;
        }
    }
}



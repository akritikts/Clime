package in.silive.clime.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.silive.clime.Activities.MainActivity;
import in.silive.clime.Models.Constants;

/**
 * Created by akriti on 5/7/16.
 */
public class FetchAddressIntentService extends IntentService {
    protected ResultReceiver mReceiver;
    MainActivity mainActivity;
    public FetchAddressIntentService(String name) {
        super(name);
    }
    public FetchAddressIntentService() {
        // Use the TAG to name the worker thread.
        super("TAG");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Log.d("TAG","dialog");
            mainActivity = new MainActivity();
            mainActivity.showAlert();

        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        if (mReceiver == null) {
            Log.wtf("TAG", "No receiver received. There is nowhere to send the results.");
            return;
        }

        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                Constants.LOCATION_DATA_EXTRA);
        Log.d("TAG",location+" location inside fetch address");




        List<Address> addresses = null;

        try {

             if((location==null)) {

            addresses = geocoder.getFromLocation(
                    20,
                    80,
                    // In this sample, get just a single address.
                    1);}
            else {
                 addresses = geocoder.getFromLocation(
                         location.getLatitude(),
                         location.getLongitude(),
                         // In this sample, get just a single address.
                         1);

             }
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "service_not_available";
            Log.e("TAG", errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "invalid_lat_long_used";
            Log.e("TAG", errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "no_address_found";
                Log.e("TAG", errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            StringBuilder strReturnedAddress = new StringBuilder("");

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(address.getAddressLine(i)).append("\n");
            }
            Log.i("TAG", "address_found");
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    strReturnedAddress.toString());
        }
    }
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }

}


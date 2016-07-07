package in.silive.clime.Models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by akriti on 7/7/16.
 */
public class GetCityLocation {
    public static double lat, lng;
   public static Location location;

    public GetCityLocation(Context c, String city) {
        Log.d("TAG","GetCity Location called");
        getAddressFromLocation(city, c, new Handler());

    }

    public  Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        GetCityLocation.location = location;
    }

    public  double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        GetCityLocation.lat = lat;
    }

    public  double getLng() {
        return lng;
    }

    public static void setLng(double lng) {
        GetCityLocation.lng = lng;
    }

    public void getAddressFromLocation(final String locationAddress,
                                       final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = null;
                    addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address;
                        address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        Log.d("TAG", sb + " address");
                        Log.d("TAG", "location inside GetCityLocation " + lat + " " + lng);
                        lat = address.getLatitude();
                        lng = address.getLongitude();
                        setLat(lat);
                        setLng(lng);
                        location.setLatitude(lat);
                        location.setLongitude(lng);
                        setLocation(location);
                    }
                } catch (IOException e) {
                    Log.e("TAG", "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n\nLatitude and Longitude :\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}

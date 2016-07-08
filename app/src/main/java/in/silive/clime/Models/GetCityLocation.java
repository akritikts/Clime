package in.silive.clime.Models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Created by akriti on 7/7/16.
 */
public class GetCityLocation {
    public static double lat, lng;
    public static Location location;

    public GetCityLocation(Context c, String city) {
        Log.d("TAG", "GetCity Location called");
        //getAddressFromLocation(city, c, new Handler());
        new fetchLatLongFromService(city).execute();

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        GetCityLocation.location = location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        GetCityLocation.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
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

    public class fetchLatLongFromService extends
            AsyncTask<Void, Void, StringBuilder> {
        String place;


        public fetchLatLongFromService(String place) {
            super();
            this.place = place;

        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {

            try {
                URL H_url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=" + this.place + "&sensor=false");
                HttpURLConnection H_connection = (HttpURLConnection) H_url.openConnection();

                Log.d("TAG", "connection");
                H_connection.setRequestMethod("GET");
                H_connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                H_connection.connect();
                BufferedReader H_bufferedReader = new BufferedReader(new InputStreamReader(H_connection.getInputStream()));
                StringBuilder jsonResults = new StringBuilder();
                String a = "";
                while (((a = H_bufferedReader.readLine()) != null)) {
                    jsonResults.append(a);
                }

                return jsonResults;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result.toString());

                    lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");


                    lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");
                    Log.d("TAG", "location inside GetCityLocation " + lat + " " + lng);
                    location.setLatitude(lat);
                    location.setLongitude(lng);
                    setLocation(location);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

                /*JSONObject jsonObj = new JSONObject(result.toString());
                JSONArray resultJsonArray = jsonObj.getJSONArray("results");

                // Extract the Place descriptions from the results
                // resultList = new ArrayList<String>(resultJsonArray.length());

                JSONObject before_geometry_jsonObj = resultJsonArray
                        .getJSONObject(0);

                JSONObject geometry_jsonObj = before_geometry_jsonObj
                        .getJSONObject("geometry");

                JSONObject location_jsonObj = geometry_jsonObj
                        .getJSONObject("location");

                String lat_helper = location_jsonObj.getString("lat");
                lat = Double.valueOf(lat_helper);
                location.setLatitude(lat);


                String lng_helper = location_jsonObj.getString("lng");
                lng = Double.valueOf(lng_helper);
                location.setLongitude(lng);
                setLocation(location);


                LatLng point = new LatLng(lat, lng);


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }*/
        }
    }
}

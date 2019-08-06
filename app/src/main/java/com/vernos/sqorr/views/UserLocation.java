package com.vernos.sqorr.views;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vernos.sqorr.R;
import com.vernos.sqorr.utilities.APIs;
import com.vernos.sqorr.utilities.GPSTracker;
import com.vernos.sqorr.utilities.LocationTrack;
import com.vernos.sqorr.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.vernos.sqorr.utilities.Utilities.checkLocationPermission;

public class UserLocation extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.init_status_bar_color));
        }

        init();
    }

    GPSTracker gpsTracker;
    TextView location_btn;
    String city, state, country;
    String sessionToken, playerMode, avatar, amount_cash, amount_token, wins,acc_name;

    private void init() {

        if (getIntent().getExtras().getString("sessionToken") != null) {
            sessionToken = getIntent().getExtras().getString("sessionToken");
            playerMode = getIntent().getExtras().getString("userPlayMode");
            avatar = getIntent().getExtras().getString("avatar");
            amount_cash = getIntent().getExtras().getString("amount_cash");
            amount_token = getIntent().getExtras().getString("amount_token");
            wins = getIntent().getExtras().getString("wins");
            acc_name = getIntent().getExtras().getString("acc_name");
//            Utilities.showAlertBox(getApplicationContext(),getcardID);
            Log.e("C sessionToken----", sessionToken);
        }


        location_btn = findViewById(R.id.tvEnable);
        location_btn.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission(getApplicationContext(), UserLocation.this);
//                    getAddress();

        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvEnable:



                try {
                    Dexter.withActivity(UserLocation.this)
                            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {


                                    //  if (Helper.isGPSEnabled(MainActivity.this)) {
                                    LocationTrack locationTrack = new LocationTrack(UserLocation.this);
                                    if (locationTrack.canGetLocation) {
                                        double lat = locationTrack.getLatitude();
                                        double lon = locationTrack.getLongitude();

                                        try {
                                            Geocoder gcd = new Geocoder(UserLocation.this, Locale.getDefault());
                                            List<Address> addresses = gcd.getFromLocation(lat,
                                                    lon, 1);

                                            if (addresses.size() > 0) {

                                                String state_txt = addresses.get(0).getAdminArea();
                                                String city_txt = addresses.get(0).getLocality();
                                                String country_txt = addresses.get(0).getCountryName();
                                                state = state_txt;
                                                {

                                                    Log.e("Address", city
                                                            + state + country);

                                                    JSONObject jsonObj = new JSONObject();

                                                    try {
                                                        jsonObj.put("city", city_txt);
                                                        jsonObj.put("state", state_txt);
                                                        jsonObj.put("country", country_txt);

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    AndroidNetworking.post(APIs.LOCATION_USER)
                                                            .addJSONObjectBody(jsonObj) // posting json
                                                            .addHeaders("sessionToken", sessionToken)
                                                            .setPriority(Priority.MEDIUM)
                                                            .build()
                                                            .getAsString(new StringRequestListener() {
                                                                @Override
                                                                public void onResponse(String response) {

                                                                    Log.e("***Location: ", response.toString());
                                                                    if (response.equalsIgnoreCase("true")) {
                                                                        Intent in = new Intent(UserLocation.this, Dashboard.class);
                                                                        in.putExtra("sessionToken", sessionToken);
                                                                        in.putExtra("userrole", playerMode);
                                                                        in.putExtra("avatar", avatar);
                                                                        in.putExtra("amount_cash", amount_cash);
                                                                        in.putExtra("amount_token", amount_token);
                                                                        in.putExtra("wins", wins);
                                                                        in.putExtra("acc_name", acc_name);

                                                                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        startActivity(in);

                                                                    } else {
//                                                                        Toast.makeText(getApplicationContext(), " No address associated with hostname", Toast.LENGTH_LONG).show();
                                                                        Utilities.showAlertBox(UserLocation.this, getString(R.string.location_msg) + " " + state, getString(R.string.location_msg));
                                                                    }

                                                                }

                                                                @Override
                                                                public void onError(ANError anError) {
                                                                    Log.e("js", "user----error-------" + anError);

                                                                }
                                                            });
                                                }
//
                                            } else {
                                                Log.e("test--", "enable loction");
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                      /*  } else {
                                            Toast.makeText(MainActivity.this, "Location service not enabled", Toast.LENGTH_LONG).show();
                                        }*/


                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {
                                    // check for permanent denial of permission
                                    if (response.isPermanentlyDenied()) {
                                        // navigate user to app settings
//                                        Toast.makeText(getApplicationContext(), "Location service not enabled", Toast.LENGTH_LONG).show();
                                        Utilities.showAlertBoxLoc(UserLocation.this, getResources().getString(R.string.enable_location_title), getResources().getString(R.string.enable_location_msg));
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }


                            }).check();


                    // finish();
                } catch (Exception e) {

                }
//


                break;
        }
    }

    private void getAddress() {

        gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

//            Log.e("Address", "" + latitude);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                updateLocationAPI(city, state, country);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

//    public void showSettingsAlert() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                UserLocation.this);
//        alertDialog.setTitle("SETTINGS");
//        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
//        alertDialog.setPositiveButton("Settings",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(
//                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        UserLocation.this.startActivity(intent);
//                    }
//                });
//        alertDialog.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        alertDialog.show();
//    }
//

    private void updateLocationAPI(String city, final String state, String country) {

        Log.e("Address", city
                + state + country);

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("city", city);
            jsonObj.put("state", state);
            jsonObj.put("country", country);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(APIs.LOCATION_USER)
                .addJSONObjectBody(jsonObj) // posting json
                .addHeaders("sessionToken", sessionToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("***Location: ", response.toString());
                        if (response.equalsIgnoreCase("true")) {
                            Intent in = new Intent(UserLocation.this, Dashboard.class);
//                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            in.putExtra("sessionToken", sessionToken);
                            in.putExtra("userrole", playerMode);
                            in.putExtra("avatar", avatar);
                            in.putExtra("amount_cash", amount_cash);
                            in.putExtra("amount_token", amount_token);
                            in.putExtra("wins", wins);

                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);

                        } else {
                            Toast.makeText(getApplicationContext(), " No address associated with hostname", Toast.LENGTH_LONG).show();
                            Utilities.showAlertBox(UserLocation.this, getString(R.string.location_msg) + " " + state, getString(R.string.location_msg));
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("js", "user----error-------" + anError);

                    }
                });
    }
}

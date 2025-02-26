package com.example.contactlistproject.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.contactlistproject.R;
import com.google.android.material.snackbar.Snackbar;

public class MapActivity extends AppCompatActivity {
    Location currentBestLocation;

    LocationManager locationManager;

    LocationListener gpsListener;
    LocationListener networkListener;
    final int PERMISSION_REQUEST_LOCATION = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_map);
        initGetLocationButton();
        initListButton();
        initSettingsButton();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_contact_map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }




    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }

        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);

                    if (isBetterLocation(location)) {
                        currentBestLocation = location;

                        txtLatitude.setText(String.valueOf(currentBestLocation.getLatitude()));
                        txtLongitude.setText(String.valueOf(currentBestLocation.getLongitude()));
                        txtAccuracy.setText(String.valueOf(currentBestLocation.getAccuracy()));
                    }
                }
                public void onStatusChanged(String provider, int status, Bundle extras){}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider){}
            };

            networkListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);

                    if (isBetterLocation(location)) {

                        currentBestLocation = location;

                        txtLatitude.setText(String.valueOf(currentBestLocation.getLatitude()));
                        txtLongitude.setText(String.valueOf(currentBestLocation.getLongitude()));
                        txtAccuracy.setText(String.valueOf(currentBestLocation.getAccuracy()));
                    }
                }
                public void onStatusChanged(String provider, int status, Bundle extras){}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider){}
            };


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String permissions[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else  {
                    Toast.makeText(MapActivity.this, "MyContactList will not locate your contacts",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        try {
            locationManager.removeUpdates(networkListener);
            locationManager.removeUpdates(gpsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///
    private void initGetLocationButton() {
        Button locationButton = (Button) findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(MapActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    MapActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            )) {
                                Snackbar.make(findViewById(R.id.activity_contact_map),
                                                "MyContactList requires this permission to locate " +
                                                        "your contacts", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Ok", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ActivityCompat.requestPermissions(
                                                        MapActivity.this,
                                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_LOCATION
                                                );

                                            }
                                        })
                                        .show();

                            } else {
                                ActivityCompat.requestPermissions(
                                        MapActivity.this,
                                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_REQUEST_LOCATION);
                            }
                        } else {
                            startLocationUpdates();
                        }
                    } else {
                        startLocationUpdates();
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error requesting permission",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private boolean isBetterLocation(Location location) {
        boolean isBetter = false;
        if (currentBestLocation == null) {
            isBetter = true;
        } else if (location.getAccuracy() <= currentBestLocation.getAccuracy()) {
            isBetter = true;
        } else if (location.getTime() - currentBestLocation.getTime() > 5 * 60 * 1000) {
            isBetter = true;
        }
        return isBetter;
    }


    /// Nav Bar
    private void initListButton(){
        ImageButton ibList=findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton(){
        ImageButton ibList=findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}
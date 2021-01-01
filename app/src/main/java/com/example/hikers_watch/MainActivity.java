package com.example.hikers_watch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationListener locationListener;
    LocationManager locationManager;
    TextView latitude,longitude,Accuracy,Altitude,Address;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude=findViewById(R.id.latitude);
                longitude=findViewById(R.id.longitude);
                Accuracy=findViewById(R.id.accuracy);
                Altitude=findViewById(R.id.altitude);
                Address=findViewById(R.id.address);
                Log.i("latitude : ",String.valueOf(location.getLatitude()));
                latitude.setText(String.valueOf(location.getLatitude()));
                Log.i("longitude : ",String.valueOf(location.getLongitude()));
                longitude.setText(String.valueOf(location.getLongitude()));
                Log.i("Altitude: ",String.valueOf(location.getAltitude()));
                Altitude.setText(String.valueOf(location.getAltitude()));
                Log.i("Accuracy : ",String.valueOf(location.getAccuracy()));
                Accuracy.setText(String.valueOf(location.getAccuracy()));

                // Now lets work on Address :

                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address>list=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    String address="";
                    if(list!=null && list.size()>0){
                        if(list.get(0).getCountryName()!=null){
                            address+=list.get(0).getCountryName()+",";
                        }
                        if(list.get(0).getAdminArea()!=null){
                            address+=list.get(0).getAdminArea()+",";
                        }

                        if(list.get(0).getLocality()!=null){
                            address+=list.get(0).getLocality()+",";
                        }
                        if(list.get(0).getSubAdminArea()!=null){
                            address+=list.get(0).getSubAdminArea()+",";
                        }
                        if(list.get(0).getPostalCode()!=null){
                            address+=list.get(0).getPostalCode()+",/";
                        }
                        Address.setText(address);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }
            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //     Log.i("location",location.toString());
            }
        };

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }
    }
}
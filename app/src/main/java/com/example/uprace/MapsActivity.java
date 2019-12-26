package com.example.uprace;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> latLngs=new ArrayList<LatLng>();
    private static final int MY_LOCATION_REQUEST_CODE = 1977;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private void showPermissionRequest()
    {
        List<String> permissions =new ArrayList<>();
        int fine_location = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        int corase_location = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(fine_location!= PackageManager.PERMISSION_GRANTED)
        {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(corase_location!=PackageManager.PERMISSION_GRANTED)
        {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(permissions.isEmpty())
        {
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(this,permissions.toArray(new String[permissions.size()]),MY_LOCATION_REQUEST_CODE);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showPermissionRequest();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMyLocationChange(Location location) {
                latLngs.add(new LatLng(location.getLatitude() ,location.getLongitude()));
                if (latLngs.size()==1 || latLngs.size()%100==0){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude() ,
                        location.getLongitude()),16));}
                if (latLngs.size()>=2)
                {
                     mMap.addPolyline(new PolylineOptions()
                            .add(latLngs.get(latLngs.size()-1),latLngs.get(latLngs.size()-2))
                            .width(5).color(Color.RED));
                     latLngs.remove(0);
                }
            }
        });

    }
}

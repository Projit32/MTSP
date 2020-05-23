package com.example.smith.officeapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Displaydetails extends AppCompatActivity implements OnMapReadyCallback {
    AttendanceModel attendanceModel;
    ArrayList<LatLng> RouteLocations=new ArrayList<>();
    GoogleMap map;
    private Boolean mLocationPermissionsGranted = false;
    private static final String TAG = "MainActivity";


    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    int CurrenntPos;
    ToggleButton FullLocationDetails;
    LatLng CurrentLocation;
    TextView Name,Status,Route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaydetails);
        getLocationPermission();

        FullLocationDetails=findViewById(R.id.FullLocationButton);
        FullLocationDetails.setEnabled(false);
        Name=findViewById(R.id.LabourName);
        Status=findViewById(R.id.status);
        Route=findViewById(R.id.RouteAssigned);

        attendanceModel=getIntent().getExtras().getParcelable("attendancedetails");
        CurrentLocation=new LatLng(attendanceModel.getLat(),attendanceModel.getLng());
        CurrenntPos=attendanceModel.getCurrent();
        if(CurrenntPos==-1)
            CurrenntPos=0;

        Name.setText("Name : "+attendanceModel.getLabourName());
        Status.setText("Status :"+attendanceModel.getStatus());
        Route.setText("Route :"+attendanceModel.getRoute());

        getRoute();

        SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map=googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
        }
        else {

            //map.getUiSettings().setMyLocationButtonEnabled(false);
            //map.setMyLocationEnabled(true);
            FullLocationDetails.setEnabled(true);

            putDetails();
            moveCamera(CurrentLocation, 15f);
            FullLocationDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                    {
                        moveCamera(CurrentLocation, 8f);
                        for(int i = 0; i<RouteLocations.size();i++)
                        {
                            if(i==0)
                            {
                                MarkerOptions markerOptions= new MarkerOptions();
                                markerOptions.position(RouteLocations.get(i));
                                markerOptions.title("Location number :"+Integer.toString(i+1));
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                map.addMarker(markerOptions);
                            }
                            else if(i!=CurrenntPos)
                            {

                                MarkerOptions markerOptions= new MarkerOptions();
                                markerOptions.position(RouteLocations.get(i));
                                markerOptions.title("Location number :"+Integer.toString(i+1));
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                map.addMarker(markerOptions);

                            }
                        }
                    }
                    else
                    {
                        map.clear();
                        putDetails();
                        moveCamera(CurrentLocation, 15f);
                    }
                }
            });

        }

    }
    public void putDetails()
    {
        MarkerOptions markerOptions= new MarkerOptions();
        markerOptions.position(CurrentLocation);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        map.addMarker(markerOptions);

        markerOptions.position(RouteLocations.get(CurrenntPos%RouteLocations.size()));
        markerOptions.title("Starting Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        map.addMarker(markerOptions);

        markerOptions.position(RouteLocations.get((CurrenntPos+1)%RouteLocations.size()));
        markerOptions.title("Ending Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        map.addMarker(markerOptions);
    }

    public void getRoute() {
        try {
            String str = attendanceModel.getLocations();
            int fi = 0, li = 0;
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if (ch == ' ') {
                    li = i;
                    String[] latlong = str.substring(fi, li).trim().split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    RouteLocations.add(new LatLng(latitude,longitude));
                    fi = li + 1;
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void getLocationPermission(){

        Log.d(TAG, "getLocationPermission: getting location permissions");

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,

                Manifest.permission.ACCESS_COARSE_LOCATION};



        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),

                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),

                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                mLocationPermissionsGranted = true;



            }else{

                ActivityCompat.requestPermissions(this,

                        permissions,

                        LOCATION_PERMISSION_REQUEST_CODE);

            }

        }else{

            ActivityCompat.requestPermissions(this,

                    permissions,

                    LOCATION_PERMISSION_REQUEST_CODE);

        }

    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: called.");

        mLocationPermissionsGranted = false;



        switch(requestCode){

            case LOCATION_PERMISSION_REQUEST_CODE:{

                if(grantResults.length > 0){

                    for(int i = 0; i < grantResults.length; i++){

                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){

                            mLocationPermissionsGranted = false;

                            Log.d(TAG, "onRequestPermissionsResult: permission failed");

                            return;

                        }

                    }

                    Log.d(TAG, "onRequestPermissionsResult: permission granted");

                    mLocationPermissionsGranted = true;

                    //initialize our map



                }

            }

        }

    }

    public void moveCamera(final LatLng latLng, float zoom) {

        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }

    @Override
    protected void onPause() {
        finishAndRemoveTask();
        super.onPause();
    }
}

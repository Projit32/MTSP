package com.example.smith.tsp;





import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;

import android.location.Geocoder;

import android.location.Location;

import android.os.Bundle;

import android.support.annotation.NonNull;

import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.KeyEvent;

import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.example.smith.tsp.models.PlaceInfo;



import java.io.IOException;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {

    ArrayList<LatLng> PositionList;
    List<Address> addresses;
    ArrayList<String> mAddresses;
    Geocoder geocoder;
    @Override

    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "onMapReady: map is ready");

        geocoder=new Geocoder(this, Locale.getDefault());
        addresses=new ArrayList<>();
        mAddresses=new ArrayList<>();
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                PositionList.add(latLng);
                //Marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                try {
                    addresses=(geocoder.getFromLocation(latLng.latitude,latLng.longitude,1));
                    //Toast.makeText(MapActivity.this, (addresses.get(0).getAddressLine(0).toString()), Toast.LENGTH_SHORT).show();
                    mAddresses.add(addresses.get(0).getAddressLine(0).toString());
                }
                catch (Exception e)
                {
                    Toast.makeText(MapActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                markerOptions.title(addresses.get(0).getAddressLine(0));
                mMap.addMarker(markerOptions);
                addresses.clear();
                if(PositionList.size()>1)
                    Submit.setVisibility(View.VISIBLE);
            }
        });



        if (mLocationPermissionsGranted) {

            getDeviceLocation();



            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,

                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;

            }

            mMap.setMyLocationEnabled(true);

            //mMap.getUiSettings().setMyLocationButtonEnabled(false);

            //edited
            mSearchText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    init();
                }
            });
            //init();

        }

    }



    private static final String TAG = "MapActivity";



    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final float DEFAULT_ZOOM = 15f;

    private static final LatLngBounds LAT_LNG_BOUNDS= new LatLngBounds(new LatLng(-40,-168  ),new LatLng(71,136));

    private PlaceInfo mPlace;

    //widgets

    public AutoCompleteTextView mSearchText;



    //vars

    private Boolean mLocationPermissionsGranted = false;
    String baseUrl,stationname;
    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private GoogleApiClient mygoogleapiclient;
    private Button Submit;
    public ImageView AddCurrent;
    LatLng latLng1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        baseUrl=getIntent().getExtras().getString("url1");
        stationname=getIntent().getExtras().getString("station");
        mSearchText =  findViewById(R.id.input_search);
        PositionList= new ArrayList<>();
        AddCurrent=findViewById(R.id.addcurrentloc);

        Submit=findViewById(R.id.button_submit);
        Submit.setVisibility(View.GONE);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapActivity.this,Show_locations.class);
                i.putExtra("places",PositionList);
                i.putExtra("url2",baseUrl);
                i.putExtra("mAddresses",mAddresses);
                i.putExtra("station",stationname);
                startActivity(i);
            }
        });


        getLocationPermission();

        if(PositionList.size()>1)
            Submit.setVisibility(View.VISIBLE);

    }



    private void init(){

        Log.d(TAG, "init: initializing");
        mygoogleapiclient=new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        placeAutoCompleteAdapter=new PlaceAutoCompleteAdapter(this,mygoogleapiclient,LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(placeAutoCompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)
                {

                    geoLocate();
                    return true;
                }

                return  false;

            }

        });

        /*mGps.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Log.d(TAG, "onClick: clicked gps icon");

                getDeviceLocation();

            }});*/
        hideSoftKeyboard();
    }



    public void geoLocate(){

        AddCurrent.setVisibility(View.VISIBLE);
        Log.d(TAG, "geoLocate: geolocating");



        String searchString = mSearchText.getText().toString();



        Geocoder geocoder = new Geocoder(MapActivity.this);

        List<Address> list = new ArrayList<>();

        try{

            list = geocoder.getFromLocationName(searchString, 1);

        }catch (IOException e){

            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );

        }



        if(list.size() > 0){

            Address address = list.get(0);



            Log.d(TAG, "geoLocate: found a location: " + address.toString());

            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,(address.getAddressLine(0)).toString());



        }

    }



    private void getDeviceLocation(){

        Log.d(TAG, "getDeviceLocation: getting the devices current location");



        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{

            if(mLocationPermissionsGranted){



                final Task location = mFusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {

                    @Override

                    public void onComplete(@NonNull Task task) {

                        if(task.isSuccessful()){

                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            String dummy="My location";
                            latLng1 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),

                                    DEFAULT_ZOOM,dummy);



                        }else{

                            Log.d(TAG, "onComplete: current location is null");

                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();

                        }

                    }

                });

            }

        }catch (SecurityException e){

            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );

        }

    }



    private void moveCamera(final LatLng latLng, float zoom, String add){

        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        final String ad1=add;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        AddCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PositionList.add(latLng1);
                    Toast.makeText(MapActivity.this, "Your current location has been marked", Toast.LENGTH_SHORT).show();
                    AddCurrent.setVisibility(View.GONE);
                    addresses = (geocoder.getFromLocation(latLng1.latitude, latLng1.longitude, 1));
                    mAddresses.add(addresses.get(0).getAddressLine(0));
                    Toast.makeText(MapActivity.this, addresses.get(0).getAddressLine(0).toString(), Toast.LENGTH_SHORT).show();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng1);
                    markerOptions.title("My location");
                    mMap.addMarker(markerOptions);
                    if(PositionList.size()>1)
                        Submit.setVisibility(View.VISIBLE);
                }
                catch (Exception e)
                {
                    Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSearchText.setText("");

        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(add);
        mMap.addMarker(markerOptions);*/
        //ekhane kichu hobe
    }



    private void initMap(){

        Log.d(TAG, "initMap: initializing map");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);



        mapFragment.getMapAsync(MapActivity.this);

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

                initMap();

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

                    initMap();

                }

            }

        }

    }

    private void hideSoftKeyboard(){

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    /*

        --------------------------- google places API autocomplete suggestions -----------------

     */



    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {

        @Override

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            hideSoftKeyboard();



            final AutocompletePrediction item = placeAutoCompleteAdapter.getItem(i);

            final String placeId = item.getPlaceId();



            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi

                    .getPlaceById(mygoogleapiclient, placeId);



            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

        }

    };



    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {

        @Override

        public void onResult(@NonNull PlaceBuffer places) {

            if(!places.getStatus().isSuccess()){

                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());

                places.release();

                return;

            }

            final Place place = places.get(0);
            AddCurrent.setVisibility(View.VISIBLE);
            AddCurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PositionList.add(place.getLatLng());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(place.getLatLng());
                    markerOptions.title(place.getAddress().toString());
                    mMap.addMarker(markerOptions);
                    AddCurrent.setVisibility(View.GONE);
                }
            });


            try{

                mPlace = new PlaceInfo();

                mPlace.setName(place.getName().toString());

                Log.d(TAG, "onResult: name: " + place.getName());

                mPlace.setAddress(place.getAddress().toString());

                Log.d(TAG, "onResult: address: " + place.getAddress());

//                mPlace.setAttributions(place.getAttributions().toString());

//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());

                mPlace.setId(place.getId());

                Log.d(TAG, "onResult: id:" + place.getId());

                mPlace.setLatlng(place.getLatLng());

                Log.d(TAG, "onResult: latlng: " + place.getLatLng());

                mPlace.setRating(place.getRating());

                Log.d(TAG, "onResult: rating: " + place.getRating());

                mPlace.setPhoneNumber(place.getPhoneNumber().toString());

                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());

                mPlace.setWebsiteUri(place.getWebsiteUri());

                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());



                Log.d(TAG, "onResult: place: " + mPlace.toString());

            }catch (NullPointerException e){

                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );

            }



            moveCamera(new LatLng(place.getViewport().getCenter().latitude,

                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName());



            places.release();

        }

    };




}




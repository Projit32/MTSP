package com.example.smith.mapdirect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    GoogleMap map;
    ArrayList<LatLng> Route=new ArrayList<>();
    String BaseURL="";
    String FetchedRoute;
    LatLng CurrentLocation;
    String LabourName;
    String StatusMessage="";
    public int currentloc=0;
    Handler handler= new Handler();
    Thread thread;

    Status status;



    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private static final String TAG = "MainActivity";


    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    Button leave,reached,proceed,done,logout;
    Realm realm;
    Retrofit retrofit;
    RequestInterface requestInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FetchedRoute=getIntent().getExtras().getString("Route");
        LabourName=getIntent().getExtras().getString("Lname");
        BaseURL=getIntent().getExtras().getString("URL");
        currentloc= getIntent().getExtras().getInt("crt");



        retrofit=new RetrofitUtility(BaseURL).getRetrofit();
        requestInterface= retrofit.create(RequestInterface.class);



        leave=findViewById(R.id.leave);
        logout=findViewById(R.id.log_out);
        reached=findViewById(R.id.reached);
        reached.setEnabled(false);
        proceed=findViewById(R.id.Proceed);
        proceed.setEnabled(false);
        done=findViewById(R.id.done);
        done.setEnabled(false);




        getLocationPermission();
        getRoute();
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);
        getDeviceLocation();

        //Creating a thread for current location
         thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    try {
                        Thread.sleep(60000);
                        getDeviceLocation();
                        UpdateStatus(StatusMessage,CurrentLocation);

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





        if(currentloc>=0 && mLocationPermissionsGranted)
        {

            leave.setEnabled(false);
            reached.setEnabled(true);
            proceed.setEnabled(false);
            done.setEnabled(true);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(currentloc<Route.size()-1)
                    ShowDirection(CurrentLocation,Route.get(currentloc+1));
                    else
                    {
                        DoneForToday();
                        UpdateStatus("Task Completed", CurrentLocation);
                    }
                    moveCamera(new LatLng(CurrentLocation.latitude, CurrentLocation.longitude),
                            15f,"My Location");
                }
            },1000);

            thread.start();

        }




        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentloc=0;
                leave.setEnabled(false);
                reached.setEnabled(true);
                proceed.setEnabled(false);
                done.setEnabled(true);
                moveCamera(new LatLng(CurrentLocation.latitude, CurrentLocation.longitude),
                        15f,"My Location");
                ShowDirection(CurrentLocation,Route.get(currentloc+1));
                updateCounter();
                UpdateStatus("Left Office", CurrentLocation);
                thread.start();


            }
        });



        reached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reached.setEnabled(false);
                proceed.setEnabled(true);
                currentloc++;
                UpdateStatus("Location Reached", CurrentLocation);
                updateCounter();
            }
        });




        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DoneForToday();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm=Realm.getDefaultInstance();
                RealmResults<Credentials> results = realm.where(Credentials.class).findAll();
                realm.beginTransaction();
                results.deleteAllFromRealm();
                realm.commitTransaction();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });



        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentloc<=Route.size()-2) {
                    UpdateStatus("Proceeding to the next location", CurrentLocation);
                    ShowDirection(Route.get(currentloc),Route.get(currentloc+1));
                    reached.setEnabled(true);
                    proceed.setEnabled(false);

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Your Route is Complete", Toast.LENGTH_SHORT).show();
                    currentloc=Route.size();
                    UpdateStatus("Task Completed", CurrentLocation);
                    proceed.setEnabled(false);
                    reached.setEnabled(false);
                }
            }
        });

    }
    public void DoneForToday()
    {
        try {
            done.setEnabled(false);
            reached.setEnabled(false);
            LatLng last, first;
            first = Route.get(0);
            ShowDirection(CurrentLocation,first);

            Toast.makeText(MainActivity.this, "Route to office", Toast.LENGTH_SHORT).show();
            if(currentloc!=Route.size())
            {
                UpdateStatus("Leaving Early", CurrentLocation);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
        }
        else {

            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.setMyLocationEnabled(true);
        }





    }
    public  void ShowDirection(LatLng x,LatLng y)
    {

        map.clear();
        MarkerOptions options = new MarkerOptions();

        options.position(x);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        map.addMarker(options);
        options.position(y);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        map.addMarker(options);


        String url = getDirectionsUrl(x, y);

        DownloadTask downloadTask = new DownloadTask();

        downloadTask.execute(url);

    }

    public void updateCounter()
    {
        //Updating location to database
        Call<ResponseMessage> call = requestInterface.UpdateCounter(LabourName, Integer.toString(currentloc));
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                //Toast.makeText(MainActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpdateStatus(String msg, LatLng Current)
    {
        StatusMessage=msg;
        status = new Status(LabourName,Current.latitude,Current.longitude,msg);
        Call<ResponseMessage> call = requestInterface.UpdateStatus(status);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                //Toast.makeText(MainActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }







    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Building the parameters to the web service
        String key1="YOUR_API_KEY";
        String parameters = str_origin+"&"+str_dest+"&"+key1;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(20);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
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

    public void getRoute() {
        try {
            String str = FetchedRoute;
            int fi = 0, li = 0;
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if (ch == ' ') {
                    li = i;
                    String[] latlong = str.substring(fi, li).trim().split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    Route.add(new LatLng(latitude, longitude));
                    fi = li + 1;
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            CurrentLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                        }
                        else{

                            Log.d(TAG, "onComplete: current location is null");

                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();

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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


    }

}

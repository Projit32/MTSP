package com.example.smith.tsp;

import android.app.Dialog;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.GoogleApiAvailability;



public class MainActivity extends AppCompatActivity  {
    String baseUrl="YOUR_DOMAIN_NAME/";

    TextView notice;

    private static final String TAG = "MainActivity";



    private static final int ERROR_DIALOG_REQUEST = 9001;





    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        notice=findViewById(R.id.notice);


        if(isServicesOK()){

            init();

        }

    }



    private void init(){

        Button btnMap = (Button) findViewById(R.id.button2);
        btnMap.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,StationCreate.class);
                intent.putExtra("url1",baseUrl);
                startActivity(intent);

            }

        });

    }



    public boolean isServicesOK(){

        Log.d(TAG, "isServicesOK: checking google services version");



        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);



        if(available == ConnectionResult.SUCCESS){

            //everything is fine and the user can make map requests
            notice.setText("Google Play Services is working");
            Log.d(TAG, "isServicesOK: Google Play Services is working");

            return true;

        }

        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){

            //an error occured but we can resolve it

            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            notice.setText("An error occured but we can fix it");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);

            dialog.show();

        }else{

            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();

        }

        return false;

    }



}

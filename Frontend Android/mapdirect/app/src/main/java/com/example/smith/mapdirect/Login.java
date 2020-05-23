package com.example.smith.mapdirect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {

    EditText Username,Password;
    Button Submit;
    String BaseURL="http://YOUR_DOMAIN_NAME/";
    LocationModel locationModel;
    Realm realm;
    RealmResults<Credentials> realmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Realm.init(this);
        realm= Realm.getDefaultInstance();
        realmResults=realm.where(Credentials.class).findAll();
        if(realmResults.size()>0)
        {
            Credentials credentials= realmResults.get(0);
            FetchData(credentials.getUserName(),credentials.getPassword());
        }
        Username=findViewById(R.id.username);
        Password=findViewById(R.id.password);
        Submit=findViewById(R.id.submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Username.getText().toString().equalsIgnoreCase("") || !Password.getText().toString().equalsIgnoreCase("")){
                FetchData(Username.getText().toString(),Password.getText().toString());

                }
                else
                    Toast.makeText(Login.this, "Enter Your Details Correctly", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void FetchData(final String User, final String Pass)
    {
        Retrofit retrofit= new RetrofitUtility(BaseURL).getRetrofit();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<LocationModel> call= requestInterface.getLocation(User,Pass);
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                if( !(response.body().getRoute().equalsIgnoreCase("ERROR")) )
                {
                    if(realmResults.size()==0)
                    {
                        realm.beginTransaction();
                        Credentials credentials= realm.createObject(Credentials.class);
                        credentials.setUserName(User);
                        credentials.setPassword(Pass);
                        realm.commitTransaction();
                    }


                    locationModel=response.body();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("Route",locationModel.getRoute());
                    intent.putExtra("Lname",locationModel.getLname());
                    intent.putExtra("crt",locationModel.getCurrloc());
                    intent.putExtra("URL",BaseURL);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(Login.this, response.body().getRoutename(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

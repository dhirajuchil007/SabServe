package com.velocityappsdj.subserve;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.velocityappsdj.subserve.POJOS.FireBaseUtils;
import com.velocityappsdj.subserve.POJOS.Provider;

import java.util.HashMap;
import java.util.Map;

public class ProviderDetails extends AppCompatActivity implements OnMapReadyCallback {
    Button getLocation;
    public static final int PLACE_PICKER_REQUEST=1;
    public TextView address;
    Provider serviceProvider;
    EditText addreddLine2;
    EditText providerName;
    public boolean newUser=true;
    Button submit;
    com.velocityappsdj.subserve.POJOS.LatLng ll;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PROVIDER="provider";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceProvider=new Provider();
        setContentView(R.layout.activity_provider_details);
        getLocation=findViewById(R.id.get_location);
        ll=new com.velocityappsdj.subserve.POJOS.LatLng(0.0,0.0);
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference(PROVIDER);
        address=findViewById(R.id.address_title);
        providerName=findViewById(R.id.provider_name);
        addreddLine2=findViewById(R.id.address_line_2);


        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(ProviderDetails.this), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
        submit=findViewById(R.id.submit_provider_details);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()&&newUser)
                {
                    serviceProvider.setAddressLine2(addreddLine2.getText().toString());
                    serviceProvider.setAddressLine1(address.getText().toString());
                    serviceProvider.setName(providerName.getText().toString());
                    serviceProvider.setId(FireBaseUtils.getFirebaseId());
                    myRef.child(FireBaseUtils.getFirebaseId()).setValue(serviceProvider);
                    Intent intent=new Intent(ProviderDetails.this,ServicesActivity.class);
                    startActivity(intent);
                }
                else if(!newUser)
                {
                    myRef.child(FireBaseUtils.getFirebaseId()).child(getString(R.string.address)).setValue(address.getText().toString());
                    myRef.child(FireBaseUtils.getFirebaseId()).child(getString(R.string.addressline2_field)).setValue(addreddLine2.getText().toString());
                    myRef.child(FireBaseUtils.getFirebaseId()).child(getString(R.string.location)).setValue(ll);
                    myRef.child(FireBaseUtils.getFirebaseId()).child(getString(R.string.name)).setValue(providerName.getText().toString());
                    Intent intent=new Intent(ProviderDetails.this,ServicesActivity.class);
                    startActivity(intent);
                }


            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent=getIntent();
        if(intent.getStringExtra(ServicesActivity.DETAILS_ACTION).equals(ServicesActivity.DETAILS_ACTION))
        {
            newUser=false;
            populateFields();
        }
    }
    public boolean validateFields(){
        if(addreddLine2.getText().toString().matches(""))
        {
            return false;
        }
        if(providerName.getText().toString().matches("")){
            return false;
        }
        if(address.getText().toString().equals(getString(R.string.address)))
            return false;
        return true;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(ll.getLatitude(),ll.getLongitude())));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ll.getLatitude(),ll.getLongitude())));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ll.getLatitude(), ll.getLongitude()), 15.0f));

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(ProviderDetails.this,data);
                ll=new com.velocityappsdj.subserve.POJOS.LatLng(place.getLatLng().latitude,place.getLatLng().longitude);

                serviceProvider.setLocation(new com.velocityappsdj.subserve.POJOS.LatLng(ll.getLatitude(),ll.getLongitude()));



                String toastMsg = String.format("Place: %s", place.getName());
                address.setText(place.getName());

                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void populateFields(){
        myRef.child(FireBaseUtils.getFirebaseId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Provider p=dataSnapshot.getValue(Provider.class);
                HashMap p=(HashMap)dataSnapshot.getValue();
                Log.d("Provdetails", "onDataChange: "+dataSnapshot.getValue());
                addreddLine2.setText(p.get(getString(R.string.addressline2_field)).toString());
                address.setText(p.get(getString(R.string.address)).toString());
                providerName.setText(p.get(getString(R.string.name)).toString());

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.child(FireBaseUtils.getFirebaseId()).child(getString(R.string.location)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Provide", "snapshot"+dataSnapshot.getValue());
                ll=dataSnapshot.getValue(com.velocityappsdj.subserve.POJOS.LatLng.class);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(ProviderDetails.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

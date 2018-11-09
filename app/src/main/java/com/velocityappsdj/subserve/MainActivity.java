package com.velocityappsdj.subserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.velocityappsdj.subserve.POJOS.LatLng;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.velocityappsdj.subserve.ProviderDetails.PROVIDER;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 123;
    FirebaseDatabase database;
    DatabaseReference myRef;
    LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            private static final String TAG ="mainactivity" ;

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null)
                {


                    String firebaseId=user.getUid();
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference(PROVIDER);
                    Query userQuery=myRef.child(firebaseId);
                    userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for(DataSnapshot d: dataSnapshot.getChildren())
                            {
                                Log.d(TAG, "onlogonacc "+d.getValue());
                                Map<String,Double> loc=new HashMap();
                                if(d.getKey().equals("location"))
                                    loc =(HashMap)d.getValue();
                                location=new LatLng(loc.get("latitude"),loc.get("longitude"));

                            }


//                    Log.d(TAG, "onDataChange: "+provider.getName());
                            if(location==null) {
                                Intent intent = new Intent(MainActivity.this, ProviderDetails.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                // Log.d(TAG, "updateUI: closed");
                                finish();
                            }
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, ServicesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                // Log.d(TAG, "updateUI: closed");
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else
                {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setLogo(R.drawable.ic_launcher_foreground)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),

                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                           ))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==RC_SIGN_IN)
        {
            if(resultCode==RESULT_OK)
            {

            }
            else {
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}

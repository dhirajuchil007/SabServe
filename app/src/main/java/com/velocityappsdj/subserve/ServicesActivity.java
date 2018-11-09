package com.velocityappsdj.subserve;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.velocityappsdj.subserve.Adapters.ServiceAdapter;
import com.velocityappsdj.subserve.POJOS.FireBaseUtils;
import com.velocityappsdj.subserve.POJOS.Provider;
import com.velocityappsdj.subserve.POJOS.Service;
import com.velocityappsdj.subserve.POJOS.ServiceType;

import java.util.ArrayList;
import java.util.Map;

import static com.velocityappsdj.subserve.ProviderDetails.PROVIDER;

public class ServicesActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    Provider provider;
    RecyclerView servicesRecyclerView;
    ServiceAdapter serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        servicesRecyclerView=findViewById(R.id.service_rv);
        //serviceAdapter=new ServiceAdapter();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        servicesRecyclerView.setLayoutManager(layoutManager);
        //servicesRecyclerView.setAdapter(serviceAdapter);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)

                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com")
                )

                .build();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("ex1");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("ex2");
       // new DrawerBuilder().withActivity(this).build();
       Drawer it= new DrawerBuilder()
                .withActivity(this)
               .withToolbar(toolbar)
               .withAccountHeader(headerResult)

                .addDrawerItems(
                        //pass your items here
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new SecondaryDrawerItem()
                )
                .build();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddService.class);
                startActivity(intent);
            }
        });
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PROVIDER);
        Query userQuery=myRef.child(FireBaseUtils.getFirebaseId()).child(getString(R.string.services));
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Map<String,String>> temp=new ArrayList<>();
                ArrayList<Service> services=new ArrayList<>();

             for(DataSnapshot d: dataSnapshot.getChildren())
             {
                 Log.d("TEST", "onDataChange: "+d.getValue());
                 String sname=d.getKey();
                 ArrayList<ServiceType> stypes=new ArrayList<>();
                 temp=(ArrayList<Map<String,String>>) d.getValue();
                 for(Map p: temp)
                 {
                     stypes.add(new ServiceType((String)p.get("name"),(String)p.get("price")));
                 }
                 services.add(new Service(sname,stypes));


             }


                   Log.d("TEST", "onServiceAdd: "+services.size());
                   serviceAdapter=new ServiceAdapter(services);
                   servicesRecyclerView.setAdapter(serviceAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}

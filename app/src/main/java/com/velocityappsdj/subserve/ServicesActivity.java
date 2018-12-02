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
    private static final String TAG ="ServicesActivity" ;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference serviceTypesReference;
    Provider provider;
    RecyclerView servicesRecyclerView;
    ServiceAdapter serviceAdapter;
    ArrayList<ServiceType> serviceTypes;
    ArrayList<Service> services;
    public static final String DETAILS_ACTION="detailsaction";
    public static final String SERVICE_LIST="servicelist";

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
        serviceTypes=new ArrayList<>();
        services=new ArrayList<>();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)

                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com")
                )

                .build();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(getString(R.string.myaccount));
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
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                   @Override
                   public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                       int choice=(int)drawerItem.getIdentifier();
                       switch(choice){
                           case 1:Intent intent=new Intent(ServicesActivity.this,ProviderDetails.class);
                           intent.putExtra(DETAILS_ACTION,DETAILS_ACTION);
                           startActivity(intent);

                       }
                       return false;
                   }
               })
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
        myRef = database.getReference(getString(R.string.services));
        Query userQuery=myRef.child(FireBaseUtils.getFirebaseId());
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Map<String,String>> temp=new ArrayList<>();


             for(DataSnapshot d: dataSnapshot.getChildren())
             {
       //  Log.d("TEST", "onDataChange: "+d.getValue());
               //Service  s =new Service();
              final Service  s=d.getValue(Service.class);
               String id=d.getKey();
                 Log.d("TEST", ": "+id);
               //  final ArrayList<ServiceType> serviceTypes=new ArrayList<>();
                 serviceTypesReference=database.getReference(getString(R.string.servicetypes)).child(id);
                 serviceTypesReference.addListenerForSingleValueEvent(new ValueEventListener() {

                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                        //services.clear();
                         ArrayList<ServiceType> serviceTypes=new ArrayList<>();
                         Service tempService=new Service(s.getName());
                         for(DataSnapshot dd:dataSnapshot.getChildren())
                         {
                             ServiceType st= dd.getValue(ServiceType.class);
                             Log.d("TEST", "typename: "+st.getName());
                             serviceTypes.add(st);
                         }
                         tempService.setServiceTypeArrayList(serviceTypes);

                         services.add(tempService);
                         serviceAdapter=new ServiceAdapter(services);
                         servicesRecyclerView.setAdapter(serviceAdapter);
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                         Log.d(TAG, databaseError.getMessage());

                     }
                 });
                // Log.d("TEST", "typename: "+serviceTypes.get(0).getName());
                 s.setServiceTypeArrayList(serviceTypes);

//                 String sname=d.getKey();
//                 ArrayList<ServiceType> stypes=new ArrayList<>();
//                 temp=(ArrayList<Map<String,String>>) d.getValue();
//                 for(Map p: temp)
//                 {
//                     stypes.add(new ServiceType((String)p.get("name"),(String)p.get("price")));
//                 }
//                 services.add(new Service(sname,stypes));


             }


                   Log.d("TEST", "onServiceAdd: "+services.size());
//                   serviceAdapter=new ServiceAdapter(services);
//                   servicesRecyclerView.setAdapter(serviceAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(TAG, databaseError.getMessage());
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        services.clear();
    }


}

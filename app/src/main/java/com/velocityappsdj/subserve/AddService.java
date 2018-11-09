package com.velocityappsdj.subserve;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.velocityappsdj.subserve.Adapters.AddServiceAdapter;
import com.velocityappsdj.subserve.POJOS.FireBaseUtils;
import com.velocityappsdj.subserve.POJOS.Service;
import com.velocityappsdj.subserve.POJOS.ServiceType;

import java.util.ArrayList;

import static com.velocityappsdj.subserve.ProviderDetails.PROVIDER;

public class AddService extends AppCompatActivity {
    public static final String CUSTOM_MESSAGE="custommessage";
    public static final String PARCELABLE_ARRAYLIST="parcelablearraylist";
    Button addServiceType;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText serviceName;
    RecyclerView serviceTypeRecyclerView;
    ArrayList<ServiceType> serviceTypes;
    AddServiceAdapter addServiceAdapter;
    Button submit;
    public static final String TAG="AddService";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        serviceTypeRecyclerView=findViewById(R.id.service_type_recyclerview);
        serviceTypes=new ArrayList<>();
        serviceTypes.add(new ServiceType("",""));
        serviceName=findViewById(R.id.service_name_edit_text);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(PROVIDER);
        if(savedInstanceState!=null)
        {
            serviceTypes=savedInstanceState.getParcelableArrayList(PARCELABLE_ARRAYLIST);
        }

        addServiceAdapter=new AddServiceAdapter(serviceTypes);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        serviceTypeRecyclerView.setLayoutManager(layoutManager);
        serviceTypeRecyclerView.setAdapter(addServiceAdapter);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(CUSTOM_MESSAGE));
        addServiceType=findViewById(R.id.add_servicetype_button);
        addServiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // hideKeyboard(AddService.this);
                ArrayList<ServiceType> temp=serviceTypes;


                addServiceAdapter=new AddServiceAdapter(serviceTypes);
                serviceTypeRecyclerView.setAdapter(addServiceAdapter);
//                serviceTypes.clear();
//                serviceTypes.addAll(temp);
                serviceTypes.add(new ServiceType("",""));
                addServiceAdapter.notifyDataSetChanged();

            }
        });
        submit=findViewById(R.id.submit_service);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()) {
                    Service s = new Service(serviceName.getText().toString(), serviceTypes);
                    myRef.child(FireBaseUtils.getFirebaseId()).child(getString(R.string.services)).child(s.getName()).setValue(s.getServiceTypeList());
                    onBackPressed();
                }

            }
        });

    }
    public boolean validateFields(){
        if(serviceName.getText().toString().matches(""))
        {
            return false;
        }
        for(ServiceType s: serviceTypes)
        {
            if(s.getName()==""||s.getPrice()=="")
            {
                Log.d(TAG, "validateFields: empty servicetype field");
                return false;
            
            }
        }


       return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PARCELABLE_ARRAYLIST,serviceTypes);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            serviceTypes=intent.getParcelableArrayListExtra(PARCELABLE_ARRAYLIST);
            for(ServiceType s:serviceTypes)
            Log.d(TAG, "onReceive: "+s.getName());

        }
    };

}

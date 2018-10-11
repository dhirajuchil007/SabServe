package com.velocityappsdj.subserve.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.velocityappsdj.subserve.AddService;
import com.velocityappsdj.subserve.POJOS.ServiceType;
import com.velocityappsdj.subserve.R;

import java.util.ArrayList;
import java.util.List;

import static com.velocityappsdj.subserve.AddService.TAG;

public class AddServiceAdapter extends RecyclerView.Adapter<AddServiceAdapter.MyViewHolder>{

    ArrayList<ServiceType> serviceTypes;

    public AddServiceAdapter(ArrayList<ServiceType> serviceTypes){
        this.serviceTypes=serviceTypes;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{


        public TextView indexNo;
        public EditText serviceType;
        public EditText price;


        public MyViewHolder(View itemView) {
            super(itemView);
            indexNo=itemView.findViewById(R.id.service_type_index);
            serviceType =itemView.findViewById(R.id.service_type_et);
            price=itemView.findViewById(R.id.service_type_price_et);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_service_recycler_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {
        ServiceType serviceType=serviceTypes.get(position);
        final int pos=position;
        Log.d(TAG, "onBindViewHolder: "+pos);
        if(serviceType.getName()!=""||serviceType.getName()!=null)
        {
            holder.serviceType.setText(serviceType.getName());
           final Context context=holder.itemView.getContext();
            holder.serviceType.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    serviceTypes.get(pos).setName(s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Intent intent=new Intent(AddService.CUSTOM_MESSAGE);
                    intent.putParcelableArrayListExtra(AddService.PARCELABLE_ARRAYLIST,serviceTypes);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


                }
            });
            holder.price.setText(String.valueOf(serviceType.getPrice()));
            holder.price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    serviceTypes.get(pos).setPrice(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Intent intent=new Intent(AddService.CUSTOM_MESSAGE);
                    intent.putParcelableArrayListExtra(AddService.PARCELABLE_ARRAYLIST,serviceTypes);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                }
            });
            holder.indexNo.setText(String.valueOf(position+1));
        }

    }

    @Override
    public int getItemCount() {
        return serviceTypes.size();
    }


}

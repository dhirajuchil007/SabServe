package com.velocityappsdj.subserve.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.velocityappsdj.subserve.POJOS.Service;
import com.velocityappsdj.subserve.R;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder >{
    ArrayList<Service> serviceArrayList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView serviceName;
        TextView numServiceTypes;

        public MyViewHolder(View itemView) {
            super(itemView);
            serviceName=itemView.findViewById(R.id.service_name_rv);
            numServiceTypes=itemView.findViewById(R.id.service_no_rv);
        }

    }

    public ServiceAdapter(ArrayList<Service> serviceArrayList) {
        this.serviceArrayList = serviceArrayList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.services_list_item,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.serviceName.setText(serviceArrayList.get(position).getName());
        holder.numServiceTypes.setText(String.valueOf(serviceArrayList.get(position).getServiceTypeList().size()));
    }

    @Override
    public int getItemCount() {
        return this.serviceArrayList.size();
    }


}

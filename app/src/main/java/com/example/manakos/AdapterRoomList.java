package com.example.manakos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRoomList extends RecyclerView.Adapter<AdapterRoomList.ViewHolder> {

    Context context;
    private SelectRoom select;
    ArrayList<Tenant> tenants;

    public AdapterRoomList(Context context, ArrayList<Tenant> tenants, SelectRoom listener) {
        this.context = context;
        this.tenants = tenants;
        select = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reportlayout, parent, false);
        return new AdapterRoomList.ViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull AdapterRoomList.ViewHolder holder, int position) {
        Tenant tenant = tenants.get(position);
        holder.Number.setText(tenant.getRID());
        holder.content.setText(tenant.getDate());
        holder.stat.setText("End in:");
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.onItemClicked(tenants.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tenants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView Number, content;
        TextView stat;
        public CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Number = itemView.findViewById(R.id.que);
            content = itemView.findViewById(R.id.repa);
            stat = itemView.findViewById(R.id.status1);
            card = itemView.findViewById(R.id.cardd);
        }
    }
}


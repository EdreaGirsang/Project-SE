package com.example.manakos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterReport extends RecyclerView.Adapter<AdapterReport.ViewHolder>{

    ArrayList<pending> PenAl;
    Context context;

    int type;
    private SelectListenerr listenerr;

    public AdapterReport(ArrayList<pending> penAl, Context context, int type) {
        PenAl = penAl;
        this.context = context;
        this.type = type;
    }

    public AdapterReport(ArrayList<pending> penAl, Context context, SelectListenerr listenerr, int type) {
        PenAl = penAl;
        this.context = context;
        this.listenerr = listenerr;
        this.type = type;
    }

    @NonNull
    @Override
    public AdapterReport.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reportlayout, parent, false);

        return new AdapterReport.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReport.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        pending pen = PenAl.get(position);
        String status;
        holder.card.setCardBackgroundColor(Color.parseColor("#000000"));
        holder.Number.setText(pen.RoomNumber);
        holder.content.setText(pen.Message);
        if(pen.getCondition() == 1){
            status = "pending";
            holder.stat.setText(status);
        }
        else if (pen.getCondition() == 0){
            status = "completed";
            holder.stat.setText(status);
            holder.card.setCardBackgroundColor(Color.parseColor("#BEBEBE"));
        }
        if(type != 1){
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerr.onItemClicked(PenAl.get(position));
                    pen.setCondition(0);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return PenAl.size();
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

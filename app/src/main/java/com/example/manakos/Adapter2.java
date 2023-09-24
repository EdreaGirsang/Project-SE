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

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder>{

    ArrayList<pending> PenAl;
    Context context;
    private SelectListenerr listenerr;

    public Adapter2(ArrayList<pending> penAl, Context context) {
        PenAl = penAl;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reportlayout, parent, false);

        return new Adapter2.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter2.ViewHolder holder,@SuppressLint("RecyclerView") int position) {
        pending pen = PenAl.get(position);

        holder.Number.setText(pen.RoomNumber);
        holder.content.setText(pen.Message);
    }

    @Override
    public int getItemCount() {
        return PenAl.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView Number, content;
        public CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Number = itemView.findViewById(R.id.que);
            content = itemView.findViewById(R.id.repa);
            card = itemView.findViewById(R.id.cardd);
        }
    }
}

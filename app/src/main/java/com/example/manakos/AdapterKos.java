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

public class AdapterKos extends RecyclerView.Adapter<AdapterKos.ViewHolder> {

    Context context;
    private SelectListener select;
    ArrayList<Kos> KosAL;

    public AdapterKos(Context context, ArrayList<Kos> kosAL, SelectListener listener) {
        this.context = context;
        KosAL = kosAL;
        select = listener;
    }

    @NonNull
    @Override
    public AdapterKos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterKos.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Kos kos = KosAL.get(position);

        holder.Name.setText(kos.Name);
        holder.Avail.setText(String.valueOf(kos.Available));
        holder.UID.setText(kos.KID);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.onItemClicked(KosAL.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return KosAL.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView Name, Avail, UID;
        public CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.NameKos);
            UID = itemView.findViewById(R.id.k0);
            Avail = itemView.findViewById(R.id.RoomCount);
            card = itemView.findViewById(R.id.cardd);
        }
    }
}

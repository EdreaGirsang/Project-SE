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

public class AnnounceAdapter extends RecyclerView.Adapter<AnnounceAdapter.ViewHolder> {

    Context context;
    ArrayList<Announce> AnnoAL;
    SelectAnn select;

    public AnnounceAdapter(Context context, ArrayList<Announce> Ann, SelectAnn select) {
        this.context = context;
        AnnoAL = Ann;
        this.select = select;
    }

    @NonNull
    @Override
    public AnnounceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.announcementlist, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnounceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Announce announce = AnnoAL.get(position);
        holder.Title.setText(announce.getTitle());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.onItemClicked(AnnoAL.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return AnnoAL.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView Title;
        public CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.Title);
            card = itemView.findViewById(R.id.annttl);
        }
    }
}

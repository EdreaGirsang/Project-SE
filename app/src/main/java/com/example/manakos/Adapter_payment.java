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

public class Adapter_payment extends RecyclerView.Adapter<Adapter_payment.ViewHolder> {

    Context context;
    private SelectListener select;
    ArrayList<ProcessPayment> processPayments;

    public Adapter_payment(Context context, ArrayList<ProcessPayment> processPayments) {
        this.context = context;
        this.processPayments = processPayments;
//        select = listener;
    }

    @NonNull
    @Override
    public Adapter_payment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.child_rv_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_payment.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ProcessPayment payment = processPayments.get(position);

        holder.title.setText(payment.title);
        holder.date.setText(payment.date);
        holder.rupiah.setText(payment.rupiah);

//        holder.card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                select.onItemClicked(payment_infos.get(position));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return processPayments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,date,rupiah;

        public CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.duedate);
            rupiah = itemView.findViewById(R.id.payment);
        }
    }
}

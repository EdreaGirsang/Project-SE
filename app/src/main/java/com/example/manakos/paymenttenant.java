package com.example.manakos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class paymenttenant extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<ProcessPayment> paymentList = new ArrayList<>();
    private ArrayList<ProcessPayment> filteredPaymentList = new ArrayList<>();
    View emptyLayout;

    private Adapter_payment adapterPayment;

    private void generateDummyData() {
        paymentList.add(new ProcessPayment("Pembayaran Listrik", "01/10/2023", "Rp 150.000", ProcessPayment.PaymentStatus.UNPAID));
        paymentList.add(new ProcessPayment("Pembayaran Air", "02/10/2023", "Rp 50.000", ProcessPayment.PaymentStatus.PAID));
        paymentList.add(new ProcessPayment("Pembelian Pulsa", "03/10/2023", "Rp 100.000", ProcessPayment.PaymentStatus.ONGOING));
        paymentList.add(new ProcessPayment("Pembayaran Kartu Kredit", "04/10/2023", "Rp 1.000.000", ProcessPayment.PaymentStatus.UNPAID));
        paymentList.add(new ProcessPayment("Pembayaran Internet", "05/10/2023", "Rp 300.000", ProcessPayment.PaymentStatus.PAID));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymenttenant);
        recyclerView = findViewById(R.id.rv);
        emptyLayout = findViewById(R.id.null_data);
        Tenant tenant = getIntent().getParcelableExtra("Tenant");
        TextView greet = (TextView) findViewById(R.id.greet);
//        greet.setText("ROOM " + tenant.getRID());

        generateDummyData();
        adapterPayment = new Adapter_payment(this,filteredPaymentList);
        recyclerView.setAdapter(adapterPayment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RadioGroup radioGroup = findViewById(R.id.view_info);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioUnpaid:
                    filterPayments(ProcessPayment.PaymentStatus.UNPAID);
                    break;
                case R.id.radioOngoing:
                    filterPayments(ProcessPayment.PaymentStatus.ONGOING);
                    break;
                case R.id.radioPaid:
                    filterPayments(ProcessPayment.PaymentStatus.PAID);
                    break;
            }
            updateNoDataVisibility();
        });


    }

    private void filterPayments(ProcessPayment.PaymentStatus status) {
        filteredPaymentList.clear();
        for (ProcessPayment payment : paymentList) {
            if (payment.getStatus() == status) {
                filteredPaymentList.add(payment);
            }
        }
        adapterPayment.notifyDataSetChanged();
    }

    private void updateNoDataVisibility() {
        if (filteredPaymentList.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
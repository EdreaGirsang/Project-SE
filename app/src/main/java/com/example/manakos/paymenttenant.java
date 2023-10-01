package com.example.manakos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class paymenttenant extends AppCompatActivity implements SelectPayment{
    RecyclerView recyclerView;
    String CO;
    TextView info;
    Bitmap bitmap;
    Dialog myDialog;
    Dialog myDialog1;
    Tenant tenant;
    ImageView pay;
    private ArrayList<ProcessPayment> paymentList = new ArrayList<>();
    private ArrayList<ProcessPayment> filteredPaymentList = new ArrayList<>();
    View emptyLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private Adapter_payment adapterPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymenttenant);
        tenant = getIntent().getParcelableExtra("Tenant");
        recyclerView = findViewById(R.id.rv);
        emptyLayout = findViewById(R.id.null_data);
        myDialog1 = new Dialog(this);
        myDialog = new Dialog(this);
        TextView greet = (TextView) findViewById(R.id.greet);
        greet.setText("ROOM " + tenant.getRID());
        String endDate = tenant.getDate().replace("_","/");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DocumentReference docRef = db.collection("users").document(tenant.getUID()).collection("Residence").document(tenant.getKID())
                .collection("Rooms").document(tenant.getRID()).collection("Upcoming").document("unpaid");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    if (Integer.valueOf(ds.get("Status").toString()) == 0){
                        paymentList.add(new ProcessPayment("Tagihan Kos", endDate, "-", ProcessPayment.PaymentStatus.UNPAID));
                    } else if (Integer.valueOf(ds.get("Status").toString()) == 1) {
                        paymentList.add(new ProcessPayment("Tagihan Kos", CO, "-", ProcessPayment.PaymentStatus.ONGOING));
                    }
                    else {
                        paymentList.add(new ProcessPayment("Tagihan Kos", CO, "-", ProcessPayment.PaymentStatus.PAID));
                    }
                }
            }
        });
        adapterPayment = new Adapter_payment(this,filteredPaymentList,this);
        recyclerView.setAdapter(adapterPayment);
        recyclerView.setHasFixedSize(true);
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

    @Override
    public void onItemClicked(ProcessPayment processPayment) {
        myDialog.setContentView(R.layout.pop_up_payment);
        TextView Title = (TextView) myDialog.findViewById(R.id.title);
        TextView due = (TextView) myDialog.findViewById(R.id.duedate);
        Button up = (Button) myDialog.findViewById(R.id.upload_payment);
        Button close = (Button) myDialog.findViewById(R.id.back_bt_payment);
        info = (TextView) myDialog.findViewById(R.id.payment);
        pay = (ImageView) myDialog.findViewById(R.id.prove);
        Title.setText(processPayment.title);
        due.setText(processPayment.date);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog1.setContentView(R.layout.paymentimage);
                ImageView iv =(ImageView) myDialog1.findViewById(R.id.pay2);
                iv.setImageBitmap(bitmap);
                Button cls = (Button) myDialog1.findViewById(R.id.close);
                cls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog1.dismiss();
                        bitmap = null;
                    }
                });
                myDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog1.show();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    ActivityResultLauncher<Intent> Res = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        final Uri path = data.getData();
                        Thread thread = new Thread(()-> {
                            try{
                                InputStream inputStream = getContentResolver().openInputStream(path);
                                bitmap = BitmapFactory.decodeStream(inputStream);
                                pay.post(()->{
                                    pay.setImageBitmap(bitmap);
                                });
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        });
                        thread.start();
                    }
                }
            }
    );

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        Res.launch(intent);
    }

}
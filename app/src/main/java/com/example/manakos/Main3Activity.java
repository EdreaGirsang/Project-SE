package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {

    Dialog myDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        myDialog = new Dialog(this);
        Tenant tenant = getIntent().getParcelableExtra("Tenant");
        Button Rep = (Button) findViewById(R.id.rep);

        Rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopup(v, tenant);
            }
        });

    }

    public void showpopup(View v, Tenant tenant) {
        myDialog.setContentView(R.layout.popupreport);
        Button rpbtn = (Button) myDialog.findViewById(R.id.send);
        EditText report1 = (EditText) myDialog.findViewById(R.id.report);
        View laundry = (View) myDialog.findViewById(R.id.laundry);
        View trash = (View) myDialog.findViewById(R.id.trash);

        laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportmaker(tenant, "Please pick my LAUNDRY!!!", 1);
            }
        });

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportmaker(tenant, "My trash is FULL", 2);
            }
        });

        rpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportmaker(tenant, report1.getText().toString(), 3);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void reportmaker(Tenant tenant, String isi, int type){
        DocumentReference dr = db.collection("users").document(tenant.getUID()).
                collection("Residence").document(tenant.getKID()).collection("reports").document();
        Map<String, Object> report =new HashMap<>();
        report.put("RoomNumber", tenant.getRID());
        report.put("Message", isi);
        report.put("type", type);
        report.put("condition", 1);

        dr.set(report).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Success to add Report",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to Report",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
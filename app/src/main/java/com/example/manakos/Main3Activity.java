package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        EditText report1 = (EditText) findViewById(R.id.report);
        Tenant tenant = getIntent().getParcelableExtra("Tenant");
        Button rpbtn = (Button) findViewById(R.id.repbtn);
        Spinner spinnerLanguages=findViewById(R.id.spinner_languages);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLanguages.setAdapter(adapter);

        rpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DocumentReference dr = db.collection("users").document(tenant.getUID()).
                      collection("Residence").document(tenant.getKID()).collection("reports").document();
                Map<String, Object> report =new HashMap<>();
                report.put("RoomNumber", tenant.getRID());
                report.put("Message", report1.getText().toString());
                report.put("type", 3);
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
        });
    }
}
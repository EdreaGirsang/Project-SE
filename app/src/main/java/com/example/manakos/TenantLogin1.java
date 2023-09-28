package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TenantLogin1 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenantlogin1);
        EditText own = (EditText) findViewById(R.id.OwnId);
        EditText KID =(EditText) findViewById(R.id.KosId);
        Button LgnBtn= (Button) findViewById(R.id.LgnBtn);
        LgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = db.collection("users").document(own.getText().toString()).collection("Residence").document(KID.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Intent intent = new Intent(TenantLogin1.this, TenantLogin2.class);
                                intent.putExtra("UID", own.getText().toString());
                                intent.putExtra("KID", KID.getText().toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),"Not Found!",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"error!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

}
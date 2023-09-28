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

public class PengurusLogin extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengurus_login);
        EditText owner = (EditText) findViewById(R.id.OwnId);
        EditText KosId = (EditText) findViewById(R.id.KosId);
        Button lgn = (Button) findViewById(R.id.LgnBtn);

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = db.collection("users").document(owner.getText().toString()).collection("Residence").document(KosId.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Intent intent = new Intent(PengurusLogin.this, OwnerRoomPage.class);
                                intent.putExtra("UID", owner.getText().toString());
                                intent.putExtra("KID", KosId.getText().toString());
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
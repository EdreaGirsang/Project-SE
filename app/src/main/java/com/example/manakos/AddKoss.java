package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddKoss extends AppCompatActivity {

    int id;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_koss);

        String UserId = getIntent().getExtras().getString("UID");
        int unit = getIntent().getExtras().getInt("unit");
        EditText name_input = findViewById(R.id.KosName);
        EditText Room_input = findViewById(R.id.AvailRm);
        Button LgnBtn= (Button) findViewById(R.id.lgn2);
        LgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_input.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Empty Field!",Toast.LENGTH_SHORT).show();
                }
                else{
                    DocumentReference dr = db.collection("users").document(UserId);
                    dr.update("unit", unit + 1 ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            saveIn(UserId , name_input.getText().toString(), unit+1);
                            Intent intent = new Intent(AddKoss.this, home_o.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Fail to Update Unit!",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public void saveIn(String UID, String name, int unit2){
        String K = "K";

        DocumentReference KosRef = db
                .collection("users").document(UID)
                .collection("Residence").document(K.concat(Integer.toString(unit2)));
        Map<String, Object> room = new HashMap<>();
        room.put("KID", K.concat(Integer.toString(unit2)));
        room.put("Name",name);
        room.put("Available", 0);
        KosRef.set(room).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),Integer.toString(unit2),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fail to Add Unit!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
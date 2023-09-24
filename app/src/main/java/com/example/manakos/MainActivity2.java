package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity2 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_login);
        EditText room = (EditText) findViewById(R.id.roomId);
        Button LgnBtn= (Button) findViewById(R.id.lgn2);
        String UID = getIntent().getExtras().getString("UID");
        String KID = getIntent().getExtras().getString("KID");
        LgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = db.collection("users").document(UID).collection("Residence").document(KID).collection("Rooms").document(room.getText().toString());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                String R1 = room.getText().toString();
                                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity2.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("hasLoggedIn", true);
                                editor.putString("UID", UID);
                                editor.putString("KID", KID);
                                editor.putString("RID", R1);
                                editor.commit();
                                Tenant tenant = new Tenant(UID,KID,R1);
                                Intent intent = new Intent(MainActivity2.this, Main3Activity.class);
                                Toast.makeText(getApplicationContext(),tenant.getRID(),Toast.LENGTH_SHORT).show();
                                 intent.putExtra("Tenant", tenant);
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
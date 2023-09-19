package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddRoom extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        String UserID = getIntent().getExtras().getString("UID");
        String KosId = getIntent().getExtras().getString("KID");
        int avail = getIntent().getExtras().getInt("avail");
        Button btn = (Button) findViewById(R.id.lgn21);
        TextView RmID = (TextView) findViewById(R.id.NoRoom);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIn(UserID, avail + 1, KosId,RmID.getText().toString());
            }
        });
    }

    public void saveIn(String UID, int avail, String KID, String RID){
        String K = "K";

        DocumentReference KosRef = db
                .collection("users").document(UID)
                .collection("Residence").document(KID).collection("Rooms").document(RID);
        Map<String, Object> room = new HashMap<>();
        room.put("RoomNumber",RID);
        room.put("Available", 0);
        KosRef.set(room).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),Integer.toString(avail),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fail to Add Unit!",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
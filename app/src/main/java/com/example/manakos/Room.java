package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Room extends AppCompatActivity implements SelectListenerr {

    RecyclerView ListReport;
    Tenant tenant;
    int av;
    ArrayList<pending> Pen;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    Adapter2 adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        View AddR = (View) findViewById(R.id.Reckanan);
        String UserID = getIntent().getExtras().getString("UID");
        String KosId = getIntent().getExtras().getString("KID");
        Pen = new ArrayList<pending>();
        tenant = new Tenant(UserID, KosId, "--");
        int avail = getIntent().getExtras().getInt("avail");
        av = avail;
        ListReport = findViewById(R.id.ReportList);
        ListReport.setHasFixedSize(true);
        ListReport.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter2(Pen,Room.this, this);
        ListReport.setAdapter(adapter);
        Repget(UserID,KosId);
        AddR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Room.this, AddRoom.class);
                intent.putExtra("UID", UserID);
                intent.putExtra("KID", KosId);
                intent.putExtra("avail", avail);
                startActivity(intent);
            }
        });
    }

    private void Repget(String UID, String KID) {
        db.collection("users").document(UID).collection("Residence").document(KID).collection("reports")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(getApplicationContext(),"Cant Load!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pending check = new pending();
                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                check = dc.getDocument().toObject(pending.class);
                                if(check.getCondition() == 1){
                                    check.setRepID(dc.getDocument().getId().toString());
                                    Pen.add(check);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void onItemClicked(pending pen) {
        DocumentReference dr = db.collection("users").document(tenant.getUID()).collection("Residence").document(tenant.getKID()).collection("reports").document(pen.getRepID());
        dr.update("condition", 0 ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Completed",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fail to Update Unit!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
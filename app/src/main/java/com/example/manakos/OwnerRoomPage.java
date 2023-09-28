package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OwnerRoomPage extends AppCompatActivity implements SelectListenerr {

    RecyclerView ListReport;
    Dialog myDialog;
    Tenant tenant;
    RecyclerView rv2;
    int av;
    ArrayList<pending> Pen;
    ArrayList<pending> pen;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    AdapterReport adapter;

    AdapterReport adapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_room_page);
        View AddR = (View) findViewById(R.id.Reckanan);
        String UserID = getIntent().getExtras().getString("UID");
        String KosId = getIntent().getExtras().getString("KID");
        Pen = new ArrayList<pending>();
        pen = new ArrayList<pending>();
        tenant = new Tenant(UserID, KosId, "--");
        int avail = getIntent().getExtras().getInt("avail");
        av = avail;
        myDialog = new Dialog(this);
        ListReport = findViewById(R.id.ReportList);
        ListReport.setHasFixedSize(true);
        ImageView histo = (ImageView) findViewById(R.id.historyicn);
        ListReport.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterReport(Pen, OwnerRoomPage.this, this, 2);
        adapter1 = new AdapterReport(pen, OwnerRoomPage.this, 1);
        ListReport.setAdapter(adapter);
        Repget(UserID,KosId);

        histo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopup1(v, tenant);
            }
        });
        AddR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerRoomPage.this, AddRoom.class);
                intent.putExtra("UID", UserID);
                intent.putExtra("KID", KosId);
                intent.putExtra("avail", avail);
                startActivity(intent);
            }
        });
    }

    private void showpopup1(View v, Tenant tenant) {
        myDialog.setContentView(R.layout.popup_complete_report);
        rv2 = myDialog.findViewById(R.id.rv11);
        rv2.setHasFixedSize(true);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(adapter1);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
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
                                else {
                                    check.setRepID(dc.getDocument().getId().toString());
                                    pen.add(check);
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
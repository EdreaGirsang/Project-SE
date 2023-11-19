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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OwnerRoomPage extends AppCompatActivity implements SelectListenerr, SelectAnn {

    RecyclerView ListReport;
    RecyclerView ListAnnounce;
    Dialog myDialog;
    Tenant tenant;
    RecyclerView rv2;
    int av;
    ArrayList<pending> Pen;
    ArrayList<pending> pen;

    View Lrec;

    ArrayList<Announce> ann;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    AdapterReport adapter;
    AdapterReport adapter1;
    AnnounceAdapter adapterann;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_room_page);
        View AddR = (View) findViewById(R.id.Reckanan);
        Lrec = findViewById(R.id.Lrec);
        String UserID = getIntent().getExtras().getString("UID");
        String KosId = getIntent().getExtras().getString("KID");
        Pen = new ArrayList<pending>();
        pen = new ArrayList<pending>();
        ann = new ArrayList<Announce>();
        tenant = new Tenant(UserID, KosId, "--", "0");
        int avail = getIntent().getExtras().getInt("avail");
        av = avail;
        myDialog = new Dialog(this);
        ListAnnounce = findViewById(R.id.RvA);
        ListAnnounce.setHasFixedSize(true);
        ListAnnounce.setLayoutManager(new LinearLayoutManager(this));
        ListReport = findViewById(R.id.ReportList);
        ListReport.setHasFixedSize(true);
        ImageView histo = (ImageView) findViewById(R.id.historyicn);
        TextView addAnnounce = (TextView) findViewById(R.id.addAnnounce);
        ListReport.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterReport(Pen, OwnerRoomPage.this, this, 2);
        adapter1 = new AdapterReport(pen, OwnerRoomPage.this, 1);
        adapterann = new AnnounceAdapter(OwnerRoomPage.this, ann, this);
        ListAnnounce.setAdapter(adapterann);
        ListReport.setAdapter(adapter);
        Annget(UserID, KosId);
        Repget(UserID,KosId);

        Lrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OwnerRoomPage.this, ListRoom.class);
                intent.putExtra("UID", UserID);
                intent.putExtra("KID", KosId);
                intent.putExtra("avail", avail);
                startActivity(intent);
            }
        });

        addAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showannounce();
            }
        });

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

    private void showannounce(){
        myDialog.setContentView(R.layout.popup_announcement_owner);
        EditText title = (EditText) myDialog.findViewById(R.id.title1);
        EditText Massage = (EditText) myDialog.findViewById(R.id.content);
        Button sbmt = (Button) myDialog.findViewById(R.id.submit);
        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference dr = db.collection("users").document(tenant.getUID()).
                        collection("Residence").document(tenant.getKID()).collection("announce").document();
                Map<String, Object> announce = new HashMap<>();
                announce.put("Title", title.getText().toString());
                announce.put("Content", Massage.getText().toString());
                dr.set(announce).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Announce Added!",Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Cant Add!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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

    @Override
    public void onItemClicked(Announce announce) {
        myDialog.setContentView(R.layout.popup_announcement_tenant);
        TextView Content = (TextView) myDialog.findViewById(R.id.content11);
        Content.setText(announce.Content);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void Annget(String UID, String KID) {
        db.collection("users").document(UID).collection("Residence").document(KID).collection("announce")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(getApplicationContext(),"Cant Load!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Announce check = new Announce();
                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                check = dc.getDocument().toObject(Announce.class);
                                ann.add(check);
                            }
                            adapterann.notifyDataSetChanged();
                        }
                    }
                });

    }
}
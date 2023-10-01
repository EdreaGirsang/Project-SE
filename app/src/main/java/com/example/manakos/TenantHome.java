package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TenantHome extends AppCompatActivity implements SelectAnn{

    Dialog myDialog;
    int i;
    RecyclerView ListAnnounce;
    String enddate;
    RecyclerView rv;
    RecyclerView rv2;
    ArrayList<pending> Pen;
    ArrayList<pending> pen;
    ArrayList<Announce> ann;
    AdapterReport adapter, adapter1;
    AnnounceAdapter adapterann;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenanthome);
        myDialog = new Dialog(this);
        Pen = new ArrayList<pending>();
        pen = new ArrayList<pending>();
        ann = new ArrayList<Announce>();
        ListAnnounce = findViewById(R.id.RvA);
        ListAnnounce.setHasFixedSize(true);
        ListAnnounce.setLayoutManager(new LinearLayoutManager(this));
        adapterann = new AnnounceAdapter(TenantHome.this, ann, this);
        ListAnnounce.setAdapter(adapterann);
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Tenant tenant = getIntent().getParcelableExtra("Tenant");
        Button Rep = (Button) findViewById(R.id.rep);
        adapter = new AdapterReport(Pen, TenantHome.this,1);
        adapter1 = new AdapterReport(pen, TenantHome.this,1);
        rv.setAdapter(adapter);
        Repget(tenant.getUID(),tenant.getKID(),tenant.getRID());
        Annget(tenant.getUID(), tenant.getKID());
        TextView txt = (TextView) findViewById(R.id.greet);
        ImageView histo = (ImageView)findViewById(R.id.historyicn);
        ImageView out = (ImageView) findViewById(R.id.out);
        ImageView IV = (ImageView) findViewById(R.id.payment);
        txt.setText("Room " + tenant.getRID());

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(TenantLogin2.PREFS_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.putString("UID", "0");
                editor.putString("KID", "0");
                editor.putString("RID", "0");
                editor.putString("Date", "0");
                editor.commit();

                Intent intent = new Intent(TenantHome.this, FirstPage.class);
                startActivity(intent);
            }
        });

        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                Intent intent = new Intent(TenantHome.this, paymenttenant.class);
                DocumentReference dr = db.collection("users").document(tenant.getUID()).collection("Residence").document(tenant.getKID()).collection("Rooms").document(tenant.getRID());
                dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot ds = task.getResult();
                        enddate = ds.get("Check-Out").toString();
                    }
                });
                Toast.makeText(getApplicationContext(),tenant.getRID(),Toast.LENGTH_SHORT).show();
                intent.putExtra("Tenant", tenant);
                startActivity(intent);
            }
        });

        histo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopup1(v, tenant);
            }
        });

        Rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopup(v, tenant);
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
                myDialog.dismiss();
            }
        });

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportmaker(tenant, "My trash is FULL", 2);
                myDialog.dismiss();
            }
        });

        rpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportmaker(tenant, report1.getText().toString(), 3);
                myDialog.dismiss();
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

    private void Repget(String UID, String KID, String RID) {

        db.collection("users").document(UID).collection("Residence").document(KID).collection("reports").orderBy("condition", Query.Direction.DESCENDING)
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
                                if(check.getRoomNumber().equals(RID) && check.getCondition() == 1){
                                    Pen.add(dc.getDocument().toObject(pending.class));
                                } else if (check.getRoomNumber().equals(RID) && check.getCondition() == 0) {
                                    pen.add(check);
                                }

                            }
                            adapter.notifyDataSetChanged();
                        }
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
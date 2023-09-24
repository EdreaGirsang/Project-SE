package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Map;

public class Main3Activity extends AppCompatActivity{

    Dialog myDialog;
    int i;
    RecyclerView rv;
    ArrayList<pending> Pen;
    ArrayList<pending> pen;
    Adapter2 adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        myDialog = new Dialog(this);
        Pen = new ArrayList<pending>();
        pen = new ArrayList<pending>();
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Tenant tenant = getIntent().getParcelableExtra("Tenant");
        Button Rep = (Button) findViewById(R.id.rep);
        adapter = new Adapter2(Pen,Main3Activity.this);
        rv.setAdapter(adapter);
        Repget(tenant.getUID(),tenant.getKID(),tenant.getRID());
        TextView txt = (TextView) findViewById(R.id.greet);
        ImageView out = (ImageView) findViewById(R.id.out);
        ImageView IV = (ImageView) findViewById(R.id.payment);
        txt.setText("Room " + tenant.getRID());

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity2.PREFS_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.putString("UID", "0");
                editor.putString("KID", "0");
                editor.putString("RID", "0");
                editor.commit();

                Intent intent = new Intent(Main3Activity.this, FirstPage.class);
                startActivity(intent);
            }
        });

        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main3Activity.this, paymenttenant.class);
                intent.putExtra("Tenant", tenant);
                startActivity(intent);
            }
        });

        Rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopup(v, tenant);
            }
        });

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

        db.collection("users").document(UID).collection("Residence").document(KID).collection("reports")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(getApplicationContext(),"Cant Load!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for(DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                Pen.add(dc.getDocument().toObject(pending.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }
}
package com.example.manakos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class home_o extends AppCompatActivity implements SelectListener{

    RecyclerView rv;

    int i;
    ArrayList<Kos> kos;
    Adapter adapter;
    String UserId;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_o);
        i = 0;
        rv = findViewById(R.id.KosList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        kos = new ArrayList<Kos>();
        adapter = new Adapter(home_o.this, kos,this);
        UserId = getIntent().getExtras().getString("UID");
        rv.setAdapter(adapter);
        kosget(UserId);
        TextView btn = (TextView) findViewById(R.id.plus);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_o.this, AddKoss.class);
                intent.putExtra("UID", UserId);
                intent.putExtra("unit", i);
                startActivity(intent);
            }
        });
    }

    private void kosget(String UID) {

        db.collection("users").document(UID).collection("Residence")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Toast.makeText(getApplicationContext(),"Cant Load!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){
                                kos.add(dc.getDocument().toObject(Kos.class));
                            }
                            i++;
                            adapter.notifyDataSetChanged();
                        }

                    }
                });

    }

    @Override
    public void onItemClicked(Kos kos) {
        Toast.makeText(this, kos.getKID(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(home_o.this, Room.class);
        intent.putExtra("KID", kos.getKID());
        intent.putExtra("UID", UserId);
        intent.putExtra("avail", kos.getAvailable());
        startActivity(intent);
    }
}
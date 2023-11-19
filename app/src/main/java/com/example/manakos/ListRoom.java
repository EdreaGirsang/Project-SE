package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListRoom extends AppCompatActivity implements SelectRoom{

    ArrayList<String> room;
    ArrayList<Tenant> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String Date;
    RecyclerView rv;
    AdapterRoomList adapterRoomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_room);
        room = new ArrayList<>();
        list = new ArrayList<Tenant>();
        String UserID = getIntent().getExtras().getString("UID");
        String KosId = getIntent().getExtras().getString("KID");
        String av = getIntent().getExtras().getString("avail");
        room.add("101");
        rv = findViewById(R.id.roomlist);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapterRoomList = new AdapterRoomList(ListRoom.this, list, this);
        rv.setAdapter(adapterRoomList);
        db.collection("users").document(UserID).collection("Residence").document(KosId).collection("Rooms")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference docref =  db.collection("users").document(UserID).collection("Residence").document(KosId).collection("Rooms").document(document.getId());
                                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                Date = document.get("Check-Out").toString();
                                                list.add(new Tenant(UserID, KosId, document.getId(), Date));
                                                adapterRoomList.notifyDataSetChanged();
                                            }
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"N", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }

    @Override
    public void onItemClicked(Tenant tenant) {

    }
}
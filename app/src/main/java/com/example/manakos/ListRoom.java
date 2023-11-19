package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListRoom extends AppCompatActivity implements SelectRoom{

    ArrayList<String> room;
    ArrayList<Tenant> list;
    StorageReference sr;
    Dialog myDialog;
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
        myDialog =new Dialog(this);
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
        myDialog.setContentView(R.layout.popup_paymentconfirmation);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img = myDialog.findViewById(R.id.prove);
        Button cancel = myDialog.findViewById(R.id.cancelButton);
        Button confirm = myDialog.findViewById(R.id.confirmButton);
        sr = FirebaseStorage.getInstance().getReference("images/"+tenant.getUID()+"/"+tenant.getKID()+"/"+tenant.getRID()+"/"+tenant.getDate());
        try {
            File local = File.createTempFile("tempfile",".jpg");
            sr.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(local.getAbsolutePath());
                    img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"No Payment Uploaded!!!", Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        myDialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference dr = db.collection("users").document(tenant.getUID()).collection("Residence").document(tenant.getKID()).collection("Rooms").document(tenant.getRID()).collection("Upcoming").document("unpaid");
                DocumentReference dref = db.collection("users").document(tenant.getUID()).collection("Residence").document(tenant.getKID()).collection("Rooms").document(tenant.getRID());
                dr.update("Status", 0);
                String[] sepdate2 = tenant.getDate().split("_", 0);
                if(Integer.valueOf(sepdate2[1]) == 12){
                    int j = Integer.valueOf(sepdate2[2])+1;
                    tenant.setDate(sepdate2[0]+"_1_"+j);
                    dr.update("OutDate", tenant.getDate());
                    dref.update("Check-Out", tenant.getDate());
                }
                else {
                    int j = Integer.valueOf(sepdate2[1]);
                    j = j +1;
                    tenant.setDate(sepdate2[0]+"_"+j+"_"+sepdate2[2]);
                    dr.update("OutDate", tenant.getDate());
                    dref.update("Check-Out", tenant.getDate());

                }
                int i =0;
                for(Tenant tenant1 : list){
                    if (tenant1.getRID() == tenant.getRID()){
                        list.remove(i);
                        list.add(tenant);
                        adapterRoomList.notifyDataSetChanged();
                    }
                    i++;
                }
                myDialog.dismiss();
            }
        });
    }
}
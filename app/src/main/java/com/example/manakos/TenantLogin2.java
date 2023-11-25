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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class TenantLogin2 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenantlogin2);
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
                                SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
                                Calendar calendar = Calendar.getInstance();
                                Calendar calendar1 = Calendar.getInstance();
                                String R1 = room.getText().toString();
                                String tgl = formatter.format(calendar.getTime()).toString();
                                String CekDoc = document.get("Available").toString();
                                if (Integer.valueOf(CekDoc) == 0){
                                    calendar1.add(Calendar.MONTH, 1);
                                    DocumentReference ref = db.collection("users").document(UID).collection("Residence").document(KID).collection("Rooms").document(R1).collection("Upcoming").document("unpaid");
                                    Map<String, Object> unpaid = new HashMap<>();
                                    unpaid.put("OutDate", formatter.format(calendar1.getTime()));
                                    unpaid.put("Status", 0);
                                    ref.set(unpaid);
                                    DocumentReference dr = db.collection("users").document(UID).collection("Residence").document(KID).collection("Rooms").document(R1).collection("History").document(tgl);
                                    SendToast(CekDoc);
                                    Map<String, Object> pay = new HashMap<>();
                                    pay.put("date", formatter.format(calendar.getTime()));
                                    dr.set(pay).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            DocumentReference docRe = db.collection("users").document(UID).collection("Residence").document(KID).collection("Rooms").document(room.getText().toString());
                                            SendToast("HALO ORANG BARU!!!");
                                            docRe.update("Available", 1);
                                            docRe.update("Check-In", formatter.format(calendar.getTime()));
                                            calendar.add(Calendar.MONTH, 1);
                                            docRe.update("Check-Out", formatter.format(calendar.getTime()));
                                        }
                                    });
                                }
                                String dt = formatter.format(calendar.getTime());
                                SetLog(UID, KID, R1, document.get("Check-Out").toString());
                                Tenant tenant = new Tenant(UID,KID,R1,dt);
                                Intent intent = new Intent(TenantLogin2.this, TenantHome.class);
                                intent.putExtra("Tenant", tenant);
                                SendToast(dt);
                                startActivity(intent);
                            } else {
                                SendToast("Not Found");
                            }
                        } else {
                            SendToast("ERROR!");
                        }
                    }
                });
            }
        });

    }

    private  void SendToast(String isi){
        Toast.makeText(getApplicationContext(),isi,Toast.LENGTH_SHORT).show();
    }
    private void SetLog(String UID, String KID, String RID, String Date){
        SharedPreferences sharedPreferences = getSharedPreferences(TenantLogin2.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hasLoggedIn", true);
        editor.putString("UID", UID);
        editor.putString("KID", KID);
        editor.putString("RID", RID);
        editor.putString("Date", Date);
        editor.commit();
    }
}
package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisPemilik extends AppCompatActivity {
    Dialog myDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regis_pemilik);
        mAuth = FirebaseAuth.getInstance();
        myDialog = new Dialog(this);
        EditText Email = (EditText) findViewById(R.id.Email);
        EditText Pass = (EditText) findViewById(R.id.Pass);
        EditText conf = (EditText) findViewById(R.id.ConfPass);
        EditText Username = (EditText) findViewById(R.id.Username);
        Button lgn = (Button) findViewById(R.id.lgn2);

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Pass.getText().toString().equals("") || Username.getText().toString().equals("") || conf.getText().toString().equals("") || Email.getText().toString().equals("")){
                    showpopup2(v);
                } else{
                    if (Pass.getText().toString().equals(conf.getText().toString())) {
                        if(Email.getText().toString().contains(".") && Email.getText().toString().contains("@")){
                            regis(Email.getText().toString(), Pass.getText().toString(), Username.getText().toString());
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Email tidak Valid!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        showpopup(v);
                    }
                }
            }
        });

    }

    private void reload(){
        Intent intent = new Intent(RegisPemilik.this, Login_pemilik.class );
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    public void showpopup2(View v){
        TextView txtclose;
        myDialog.setContentView(R.layout.popup3);
        txtclose = (TextView) myDialog.findViewById(R.id.up);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public void showpopup(View v){
        TextView txtclose;
        myDialog.setContentView(R.layout.popup2);
        txtclose = (TextView) myDialog.findViewById(R.id.ppup);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void regis(String email, String Pass, String Username){
        mAuth.createUserWithEmailAndPassword(email, Pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = db.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("password", Pass);
                            user.put("name", Username);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(),"berhasil membuat akun",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
package com.example.manakos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_pemilik extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_pemilik);
        EditText email = (EditText) findViewById(R.id.Email);
        EditText pass = (EditText) findViewById(R.id.Pass);
        TextView t = (TextView) findViewById(R.id.create);
        Button LgnBtn = (Button) findViewById(R.id.lgn2);
        mAuth = FirebaseAuth.getInstance();
        LgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString().equals("") || email.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Fill the Field!!!",Toast.LENGTH_SHORT).show();
                }else{
                    if(email.getText().toString().contains(".") && email.getText().toString().contains("@")){
                        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(Login_pemilik.this, OwnerHome.class);
                                    Toast.makeText(getApplicationContext(),"Login Berhasil!",Toast.LENGTH_SHORT).show();
                                    String UserID = mAuth.getCurrentUser().getUid();
                                    intent.putExtra("UID", UserID);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),"Email tidak valid!!!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_pemilik.this, RegisPemilik.class );
                startActivity(intent);
            }
        });
    }
}
package com.example.manakos;

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

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDialog = new Dialog(this);
        EditText KID =(EditText) findViewById(R.id.KosId);
        Button LgnBtn= (Button) findViewById(R.id.LgnBtn);
        String value = KID.getText().toString();
        LgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KID.getText().toString().equals("Admin")){
                    accesssecond();
                }
                else{
                    showpopup(v);
                }

            }
        });
    }
    public void accesssecond(){
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }

    public void showpopup(View v){
        TextView txtclose;
        myDialog.setContentView(R.layout.popup1);
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
}
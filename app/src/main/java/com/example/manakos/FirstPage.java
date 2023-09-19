package com.example.manakos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        Button TntBtn= (Button) findViewById(R.id.TenantBtn);
        Button PmlBtn= (Button) findViewById(R.id.PemilikBtn);
        Button pngbtn = (Button) findViewById(R.id.PengurusBtn);
        TntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TntClicked();
            }
        });
        PmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PmlClicked();
            }
        });

        pngbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPage.this, inpengurus.class);
                startActivity(intent);
            }
        });

    }

    public void TntClicked(){
        Intent intent = new Intent(FirstPage.this, MainActivity.class );
        startActivity(intent);
    }

    public void PmlClicked(){
        Intent intent = new Intent(FirstPage.this, Login_pemilik.class );
        startActivity(intent);
    }
}
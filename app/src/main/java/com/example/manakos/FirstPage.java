package com.example.manakos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity2.PREFS_NAME,0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);
                String UID = sharedPreferences.getString("UID", "0");
                String KID = sharedPreferences.getString("KID", "0");
                String RID = sharedPreferences.getString("RID", "0");
                Tenant tenant = new Tenant(UID, KID, RID);

                if(hasLoggedIn){
                    Intent intent = new Intent(FirstPage.this, Main3Activity.class);
                    intent.putExtra("Tenant", tenant);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

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
        Intent intent = new Intent(FirstPage.this, paymenttenant.class );
        startActivity(intent);
    }

    public void PmlClicked(){
        Intent intent = new Intent(FirstPage.this, Login_pemilik.class );
        startActivity(intent);
    }
}
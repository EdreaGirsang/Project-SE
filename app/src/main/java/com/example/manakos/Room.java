package com.example.manakos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Room extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        View AddR = (View) findViewById(R.id.Reckanan);
        String UserID = getIntent().getExtras().getString("UID");
        String KosId = getIntent().getExtras().getString("KID");
        int avail = getIntent().getExtras().getInt("avail");
        AddR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Room.this, AddRoom.class);
                intent.putExtra("UID", UserID);
                intent.putExtra("KID", KosId);
                intent.putExtra("avail", avail);
                startActivity(intent);
            }
        });
    }
}
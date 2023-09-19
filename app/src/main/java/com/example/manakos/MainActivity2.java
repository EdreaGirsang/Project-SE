package com.example.manakos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_login);
        Button LgnBtn= (Button) findViewById(R.id.lgn2);
        LgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accesssecond();
            }
        });

    }
    public void accesssecond(){
        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);
    }
}
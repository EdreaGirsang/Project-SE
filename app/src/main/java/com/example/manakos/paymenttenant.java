package com.example.manakos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class paymenttenant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymenttenant);
        Tenant tenant = getIntent().getParcelableExtra("Tenant");
        TextView greet = (TextView) findViewById(R.id.greet);
        greet.setText("ROOM " + tenant.getRID());

    }
}
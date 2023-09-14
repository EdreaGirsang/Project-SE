package com.example.manakos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddKoss extends AppCompatActivity {

    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_koss);

        EditText name_input = findViewById(R.id.KosName);
        EditText Room_input = findViewById(R.id.AvailRm);
        Button LgnBtn= (Button) findViewById(R.id.lgn2);
        LgnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper db = new MyDatabaseHelper(AddKoss.this);
                db.add_kos(name_input.getText().toString().trim(), Integer.valueOf(Room_input.getText().toString().trim()), id);
            }
        });
    }
}
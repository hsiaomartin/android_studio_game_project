package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity_setup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        final EditText p1= findViewById(R.id.editTextTextP1);
        final EditText p2= findViewById(R.id.editTextTextP2);
        final EditText p3= findViewById(R.id.editTextTextP3);
        final EditText p4= findViewById(R.id.editTextTextP4);

        Button setup_btn = (Button) findViewById(R.id.button_setup_start);
        setup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("mode", "single");
                intent.putExtra("player1",p1.getText().toString());
                intent.putExtra("player2",p2.getText().toString());
                intent.putExtra("player3",p3.getText().toString());
                intent.putExtra("player4",p4.getText().toString());
                startActivity(intent);
            }
        });

    }
}
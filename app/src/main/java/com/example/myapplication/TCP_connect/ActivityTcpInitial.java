package com.example.myapplication.TCP_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class ActivityTcpInitial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_initial);
        final EditText Name= findViewById(R.id.editTextTextPersonName);
        final EditText IP= findViewById(R.id.editTextIP);
        final EditText Port= findViewById(R.id.editTextPort);
        Button send = findViewById(R.id.button_connect_server);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityTcpInitial.this, MainActivity.class);
                i.putExtra("mode", "multi");
                i.putExtra("Name", Name.getText().toString());
                i.putExtra("IP", IP.getText().toString());
                i.putExtra("Port", Port.getText().toString());
                startActivity(i);
            }
        });
    }
}
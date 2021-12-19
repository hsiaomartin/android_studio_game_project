package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.TCP_connect.ActivityClient;
import com.example.myapplication.TCP_connect.ActivityTcpInitial;

public class gameStart extends AppCompatActivity {
    Button game_Start;
    Button game_Help;

    private static Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);


       // game_Help = (Button) findViewById(R.id.HELP_Button);
        //game_Start = (Button) findViewById(R.id.START_button);
        gameConnect_func();

    }

    public void button_Game_Start(View button) {
        Intent intent = new Intent(this,Activity_setup.class);
        startActivity(intent);
    }
    public void button_Game_Help(View button) {
        Intent intent = new Intent(this,gameHelp.class);
        startActivity(intent);
    }

    public void gameConnect_func(){
        Button game_Connect = (Button) findViewById(R.id.connect_Button);
        game_Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityTcpInitial.class);
                startActivity(intent);
            }
        });
    }

}
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class gameStart extends AppCompatActivity {
    Button game_Start;
    Button game_Help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        game_Start = (Button) findViewById(R.id.START_button);
        game_Help = (Button) findViewById(R.id.HELP_Button);
    }

    public void button_Game_Start(View button) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void button_Game_Help(View button) {
        Intent intent = new Intent(this,gameHelp.class);
        startActivity(intent);
    }
}
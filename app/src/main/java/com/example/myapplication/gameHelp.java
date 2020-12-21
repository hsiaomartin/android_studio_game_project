package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class gameHelp extends AppCompatActivity {
    Button button_Return_Menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_help);
        button_Return_Menu = findViewById(R.id.button_Game_Help);
    }

    public void on_Button_Return_Menu(View button) {
        Intent intent = new Intent(this,gameStart.class);
        startActivity(intent);
    }
}
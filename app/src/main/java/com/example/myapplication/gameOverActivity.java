package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class gameOverActivity extends AppCompatActivity {
    Button return_Menu;
    TextView winner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        return_Menu = (Button)findViewById(R.id.button_Return_Menu);
        winner = (TextView)findViewById(R.id.textView_Winner);
        Intent intent = getIntent();
        winner.setText(String.format("%s 是贏家 !", intent.getStringExtra("winner")));
    }
    public void button_Return_Menu(View button) {
        Intent intent = new Intent(this,gameStart.class);
        startActivity(intent);
    }
}
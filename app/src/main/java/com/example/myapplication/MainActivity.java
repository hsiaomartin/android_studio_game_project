package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //-遊戲參數------------
    short PLAYER_NUM=4;
    short WIN_SCORE = (new gameMethod()).getWIN_SCORE();
    Player[] player;
    //--遊戲介面元件-------------
    Button test_Button;
    TextView deck_Counter;
    TextView[] player_Score;
    //---------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test_Button = (Button) findViewById(R.id.action_Button);
        deck_Counter = (TextView) findViewById(R.id.textView_deck_Counter);
        player_Score = new TextView[PLAYER_NUM];
        player_Score[0] = (TextView) findViewById(R.id.playerA_Score);
        player_Score[1] = (TextView) findViewById(R.id.playerB_Score);
        player_Score[2] = (TextView) findViewById(R.id.playerC_Score);
        player_Score[3] = (TextView) findViewById(R.id.playerD_Score);
        game_initial();
    }
    //遊戲初始化
    public void game_initial(){

        player = new Player[PLAYER_NUM];
        for (int i = 0 ;i<PLAYER_NUM;i++){
            player[i] = new Player("p"+(i+1));
            player_Score[i].setText(String.format("%s/%s", player[i].getPlayer_Score(), WIN_SCORE));
        }


    }
    //用來 debug 的 button
    public void on_test_ButtonClicked(View button) {
        new DataThread().start();
        player[0].setPlayer_Score((short) 8);
        player_Score[0].setText(String.format("%s/%s", player[0].getPlayer_Score(), WIN_SCORE));
        for(int i = 0 ;i < PLAYER_NUM;i++)
            if((new gameMethod()).is_Win(player[i].getPlayer_Score())){
                Intent intent = new Intent(this,gameOverActivity.class);
                intent.putExtra("winner",player[i].getPlayer_Name());
                this.finish();
                startActivity(intent);
            }

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            deck_Counter.setText((String)msg.obj);
        };
    };

    public class DataThread extends Thread{
        @Override
        public void run() {
            for(int i = 0; i <= 40; i++) {
                try {
                    Thread.sleep(15);
                }
                catch (InterruptedException e) {
                }
                final String data ="x"+ String.valueOf(i);
                mHandler.sendMessage(mHandler.obtainMessage(0, data));
            }
        }
    }

}
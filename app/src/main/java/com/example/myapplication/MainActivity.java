package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    //-遊戲參數------------
    int PLAYER_NUM=4;
    int WIN_SCORE = (new gameMethod()).getWIN_SCORE();
    Player[] player;
    int player_Pointer;
    Deck myDeck;
    boolean[] is_Card_Taken;
    int deck_Index;
    int[] deck_Pointer;
    //--遊戲介面元件-------------
    Button test_Button;
    TextView deck_Counter;  //Deck 剩餘數量顯示
    TextView[] player_Score; //玩家分數
    ImageButton[] flip_Card; //翻開卡片
    //---------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test_Button = (Button) findViewById(R.id.action_Button);
        deck_Counter = (TextView) findViewById(R.id.textView_deck_Counter);
        flip_Card = new ImageButton[4];
        player_Score = new TextView[PLAYER_NUM];
        is_Card_Taken = new boolean[4];
        deck_Index = 0;
        deck_Pointer = new int[4];
        player_Pointer = 0;

        player_Score[0] = (TextView) findViewById(R.id.playerA_Score);
        player_Score[1] = (TextView) findViewById(R.id.playerB_Score);
        player_Score[2] = (TextView) findViewById(R.id.playerC_Score);
        player_Score[3] = (TextView) findViewById(R.id.playerD_Score);

        flip_Card[0] = (ImageButton) findViewById(R.id.flip_Card_1);
        flip_Card[1] = (ImageButton) findViewById(R.id.flip_Card_2);
        flip_Card[2] = (ImageButton) findViewById(R.id.flip_Card_3);
        flip_Card[3] = (ImageButton) findViewById(R.id.flip_Card_4);

        for(int i = 0 ;i< 4 ;i++){
            is_Card_Taken[i] = false;
        }

        game_initial();
    }
    //遊戲初始化
    public void game_initial(){
        //read deck value
        InputStreamReader deck_Value = new InputStreamReader(this.getResources().openRawResource(R.raw.carddata));
        myDeck = new Deck(deck_Value);
        player = new Player[PLAYER_NUM];

        //set player name and initial score
        for (int i = 0 ;i<PLAYER_NUM;i++){
            player[i] = new Player("player "+(i+1));
            player_Score[i].setText(String.format("%s/%s", player[i].getPlayer_Score(), WIN_SCORE));
        }

        //牌面初始化
        for(int i = 0 ; i< 4 ;i++){
            int imageResource = getResources().getIdentifier((myDeck.getCards().get(i)).getCard_Picture(), null, getPackageName());
            flip_Card[i].setImageResource(imageResource);
            deck_Pointer[i] = i;
            deck_Index++;
        }



    }

    public void on_flip_Card0_Clicked(View ImageButton){
        System.out.println("card 1 clicked : score = "+ (myDeck.getCards().get(deck_Pointer[0])).getCard_Score());
        System.out.println("card 1 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[0])).getPermanent_PointA());
        System.out.println("card 1 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[0])).getPermanent_PointB());
        System.out.println("card 1 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[0])).getPermanent_PointC());
        System.out.println("card 1 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[0])).getPermanent_PointD());
        System.out.println("card 1 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[0])).getNeed_PointA());
        System.out.println("card 1 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[0])).getNeed_PointB());
        System.out.println("card 1 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[0])).getNeed_PointC());
        System.out.println("card 1 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[0])).getNeed_PointD());

        //if((myDeck.getCards().get(deck_Pointer[0])).card_Is_Available())

    }
    public void on_flip_Card1_Clicked(View ImageButton){
        System.out.println("card 2 clicked : score = "+ (myDeck.getCards().get(deck_Pointer[1])).getCard_Score());
        System.out.println("card 2 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[1])).getPermanent_PointA());
        System.out.println("card 2 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[1])).getPermanent_PointB());
        System.out.println("card 2 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[1])).getPermanent_PointC());
        System.out.println("card 2 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[1])).getPermanent_PointD());
        System.out.println("card 2 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[1])).getNeed_PointA());
        System.out.println("card 2 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[1])).getNeed_PointB());
        System.out.println("card 2 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[1])).getNeed_PointC());
        System.out.println("card 2 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[1])).getNeed_PointD());
    }
    public void on_flip_Card2_Clicked(View ImageButton){
        System.out.println("card 3 clicked : score = "+ (myDeck.getCards().get(deck_Pointer[2])).getCard_Score());
        System.out.println("card 3 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[2])).getPermanent_PointA());
        System.out.println("card 3 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[2])).getPermanent_PointB());
        System.out.println("card 3 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[2])).getPermanent_PointC());
        System.out.println("card 3 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[2])).getPermanent_PointD());
        System.out.println("card 3 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[2])).getNeed_PointA());
        System.out.println("card 3 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[2])).getNeed_PointB());
        System.out.println("card 3 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[2])).getNeed_PointC());
        System.out.println("card 3 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[2])).getNeed_PointD());
    }
    public void on_flip_Card3_Clicked(View ImageButton){
        System.out.println("card 4 clicked : score = "+ (myDeck.getCards().get(deck_Pointer[3])).getCard_Score());
        System.out.println("card 4 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[3])).getPermanent_PointA());
        System.out.println("card 4 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[3])).getPermanent_PointB());
        System.out.println("card 4 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[3])).getPermanent_PointC());
        System.out.println("card 4 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[3])).getPermanent_PointD());
        System.out.println("card 4 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[3])).getNeed_PointA());
        System.out.println("card 4 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[3])).getNeed_PointB());
        System.out.println("card 4 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[3])).getNeed_PointC());
        System.out.println("card 4 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[3])).getNeed_PointD());
    }

    //用來 debug 的 button
    public void on_test_ButtonClicked(View button) {
        new DataThread().start();

        //player[0].setPlayer_Score((short) 8);
        player_Score[0].setText(String.format("%s/%s", player[0].getPlayer_Score(), WIN_SCORE));
        for(int i = 0 ;i < PLAYER_NUM;i++)
            if((new gameMethod()).is_Win(player[i].getPlayer_Score())){
                Intent intent = new Intent(this,gameOverActivity.class);
                intent.putExtra("winner",player[i].getPlayer_Name());
                this.finish();
                startActivity(intent);
            }

        player_Pointer = (player_Pointer++)%PLAYER_NUM;
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
                    Thread.sleep(i);
                }
                catch (InterruptedException e) {
                }
                final String data ="x"+ String.valueOf(i);
                mHandler.sendMessage(mHandler.obtainMessage(0, data));
            }
        }
    }

}
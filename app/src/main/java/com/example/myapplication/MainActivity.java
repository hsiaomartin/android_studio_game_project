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
    int PLAYER_NUM=4; //玩家人數
    int FLIP_CARD_NUM = 4 ;//翻開的卡片數量
    int RESOURCE_NUM = 4; //資源種類數量
    int RESOURCE_TAKEN_MAX = 3;//回合可拿取資源上限
    int coin_Taken_Counter = 0;
    int WIN_SCORE = (new gameMethod()).getWIN_SCORE();//勝利分數
    Player[] player;
    int player_Pointer;//指向目前回合的玩家
    Deck myDeck;
    boolean[] is_Card_Taken;//介面上的卡片是否被拿取
    int deck_Index;//卡組目錄
    int[] deck_Pointer;//指向卡組中的卡片
    String resource_Initial ="0+0";
    //--遊戲介面元件-------------
    Button test_Button; //功能驗證按鈕
    TextView deck_Counter;  //Deck 剩餘數量顯示
    TextView[] textView_Player_Score; //玩家分數
    TextView textView_Player1_A_resource;
    TextView textView_Player1_B_resource;
    TextView textView_Player1_C_resource;
    TextView textView_Player1_D_resource;

    TextView textView_Player2_A_resource;
    TextView textView_Player2_B_resource;
    TextView textView_Player2_C_resource;
    TextView textView_Player2_D_resource;

    TextView textView_Player3_A_resource;
    TextView textView_Player3_B_resource;
    TextView textView_Player3_C_resource;
    TextView textView_Player3_D_resource;

    TextView textView_Player4_A_resource;
    TextView textView_Player4_B_resource;
    TextView textView_Player4_C_resource;
    TextView textView_Player4_D_resource;

    ImageButton[] button_coin;
    TextView[] textView_coin;
    ImageButton[] button_Hand;

    ImageButton[] flip_Card; //翻開卡片
    TextView[] textView_Flip_Card;//翻開卡片的描述

    //---------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //--遊戲介面元件-------------
        setContentView(R.layout.activity_main);
        test_Button = (Button) findViewById(R.id.action_Button);
        deck_Counter = (TextView) findViewById(R.id.textView_deck_Counter);

        flip_Card = new ImageButton[FLIP_CARD_NUM];
        flip_Card[0] = (ImageButton) findViewById(R.id.flip_Card_1);
        flip_Card[1] = (ImageButton) findViewById(R.id.flip_Card_2);
        flip_Card[2] = (ImageButton) findViewById(R.id.flip_Card_3);
        flip_Card[3] = (ImageButton) findViewById(R.id.flip_Card_4);

        textView_Player_Score = new TextView[PLAYER_NUM];
        textView_Player_Score[0] = (TextView) findViewById(R.id.playerA_Score);
        textView_Player_Score[1] = (TextView) findViewById(R.id.playerB_Score);
        textView_Player_Score[2] = (TextView) findViewById(R.id.playerC_Score);
        textView_Player_Score[3] = (TextView) findViewById(R.id.playerD_Score);

        textView_Player1_A_resource = (TextView)findViewById(R.id.p1A_resource);
        textView_Player1_B_resource = (TextView)findViewById(R.id.p1B_resource);
        textView_Player1_C_resource = (TextView)findViewById(R.id.p1C_resource);
        textView_Player1_D_resource = (TextView)findViewById(R.id.p1D_resource);

        textView_Player2_A_resource = (TextView)findViewById(R.id.p2A_resource);
        textView_Player2_B_resource = (TextView)findViewById(R.id.p2B_resource);
        textView_Player2_C_resource = (TextView)findViewById(R.id.p2C_resource);
        textView_Player2_D_resource = (TextView)findViewById(R.id.p2D_resource);

        textView_Player3_A_resource = (TextView)findViewById(R.id.p3A_resource);
        textView_Player3_B_resource = (TextView)findViewById(R.id.p3B_resource);
        textView_Player3_C_resource = (TextView)findViewById(R.id.p3C_resource);
        textView_Player3_D_resource = (TextView)findViewById(R.id.p3D_resource);

        textView_Player4_A_resource = (TextView)findViewById(R.id.p4A_resource);
        textView_Player4_B_resource = (TextView)findViewById(R.id.p4B_resource);
        textView_Player4_C_resource = (TextView)findViewById(R.id.p4C_resource);
        textView_Player4_D_resource = (TextView)findViewById(R.id.p4D_resource);

        textView_Player1_A_resource.setText(resource_Initial);
        textView_Player1_B_resource.setText(resource_Initial);
        textView_Player1_C_resource.setText(resource_Initial);
        textView_Player1_D_resource.setText(resource_Initial);

        textView_Player2_A_resource.setText(resource_Initial);
        textView_Player2_B_resource.setText(resource_Initial);
        textView_Player2_C_resource.setText(resource_Initial);
        textView_Player2_D_resource.setText(resource_Initial);

        textView_Player3_A_resource.setText(resource_Initial);
        textView_Player3_B_resource.setText(resource_Initial);
        textView_Player3_C_resource.setText(resource_Initial);
        textView_Player3_D_resource.setText(resource_Initial);

        textView_Player4_A_resource.setText(resource_Initial);
        textView_Player4_B_resource.setText(resource_Initial);
        textView_Player4_C_resource.setText(resource_Initial);
        textView_Player4_D_resource.setText(resource_Initial);

        textView_Flip_Card = new TextView[FLIP_CARD_NUM];
        textView_Flip_Card[0] = (TextView) findViewById(R.id.textView_flip_Card_1);
        textView_Flip_Card[1] = (TextView) findViewById(R.id.textView_flip_Card_2);
        textView_Flip_Card[2] = (TextView) findViewById(R.id.textView_flip_Card_3);
        textView_Flip_Card[3] = (TextView) findViewById(R.id.textView_flip_Card_4);

        button_coin = new ImageButton[RESOURCE_NUM];
        button_coin[0] = (ImageButton)findViewById(R.id.coinA_Button);
        button_coin[1] = (ImageButton)findViewById(R.id.coinB_Button);
        button_coin[2] = (ImageButton)findViewById(R.id.coinC_Button);
        button_coin[3] = (ImageButton)findViewById(R.id.coinD_Button);

        textView_coin = new TextView[RESOURCE_NUM];
        textView_coin[0] = (TextView)findViewById(R.id.coinA_TextView);
        textView_coin[1] = (TextView)findViewById(R.id.coinB_TextView);
        textView_coin[2] = (TextView)findViewById(R.id.coinC_TextView);
        textView_coin[3] = (TextView)findViewById(R.id.coinD_TextView);

        button_Hand = new ImageButton[RESOURCE_TAKEN_MAX];
        button_Hand[0] = (ImageButton)findViewById(R.id.button_Hand1);
        button_Hand[1] = (ImageButton)findViewById(R.id.button_Hand2);
        button_Hand[2] = (ImageButton)findViewById(R.id.button_Hand3);

        //-遊戲參數------------
        is_Card_Taken = new boolean[FLIP_CARD_NUM];
        deck_Index = 0;
        deck_Pointer = new int[FLIP_CARD_NUM];
        player_Pointer = 0;
        for(int i = 0 ;i< FLIP_CARD_NUM ;i++){
            is_Card_Taken[i] = false;
        }
        //--------------------------------------




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
            textView_Player_Score[i].setText(String.format("%s/%s", player[i].getPlayer_Score(), WIN_SCORE));
        }

        //牌面初始化
        for(int i = 0 ; i< 4 ;i++){
            int imageResource = getResources().getIdentifier((myDeck.getCards().get(i)).getCard_Picture(), null, getPackageName());
            flip_Card[i].setImageResource(imageResource);
            textView_Flip_Card[i].setText((myDeck.getCards().get(i)).getCard_Data());
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

    public void on_button_coinA_Clicked(View ImageButton){
        if(coin_Taken_Counter<RESOURCE_TAKEN_MAX){
            int imageResource = getResources().getIdentifier("@mipmap/coin_a", null, getPackageName());
            //player[0].setNeed_PointA(1);
            button_Hand[coin_Taken_Counter].setImageResource(imageResource);
            coin_Taken_Counter++;
        }

    }
    //用來 debug 的 button
    public void on_test_ButtonClicked(View button) {
        new DataThread().start();

        //player[0].setPlayer_Score((int) 8);
        textView_Player_Score[0].setText(String.format("%s/%s", player[0].getPlayer_Score(), WIN_SCORE));
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
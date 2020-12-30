package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
    boolean MY_DEBUG = false;
    //-遊戲參數------------
    int PLAYER_NUM=4; //玩家人數
    int FLIP_CARD_NUM = 4 ;//翻開的卡片數量
    int RESOURCE_NUM = 4; //資源種類數量
    int RESOURCE_TAKEN_MAX = 3;//回合可拿取資源上限
    int WIN_SCORE = (new gameMethod()).getWIN_SCORE();//勝利分數

    int[] coin_Limit;


    int[] need_Coin_Taken;//紀錄什麼籌碼被拿走了
    int coin_Taken_Counter = 0;
    Player[] player;
    int player_Pointer;//指向目前回合的玩家

    Deck myDeck;
    int deck_Counter_Value = 40;
    int card_Taken_No;//介面上被拿取的卡片代號
    int deck_Index;//卡組目錄
    int[] deck_Pointer;//指向卡組中的卡片
    String resource_Initial ="0+0";
    //--遊戲介面元件-------------
    Button test_Button; //功能驗證按鈕
    TextView deck_Counter;  //Deck 剩餘數量顯示
    TextView[] textView_Player_Score; //玩家分數
    TextView[] textView_Player_Name;

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

    ImageButton[] button_coin;//介面上的籌碼按鈕
    TextView[] textView_coin;
    ImageButton[] button_Hand;

    ImageButton[] flip_Card; //翻開卡片
    TextView[] textView_Flip_Card;//翻開卡片的描述
    ImageButton button_Chosen_Card;//被選擇的卡片
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

        textView_Player_Name = new TextView[PLAYER_NUM];
        textView_Player_Name[0] = (TextView) findViewById(R.id.PlayerA_Name);
        textView_Player_Name[1] = (TextView) findViewById(R.id.PlayerB_Name);
        textView_Player_Name[2] = (TextView) findViewById(R.id.PlayerC_Name);
        textView_Player_Name[3] = (TextView) findViewById(R.id.PlayerD_Name);

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
        if(!MY_DEBUG){
            textView_Flip_Card[0].setVisibility(View.INVISIBLE);
            textView_Flip_Card[1].setVisibility(View.INVISIBLE);
            textView_Flip_Card[2].setVisibility(View.INVISIBLE);
            textView_Flip_Card[3].setVisibility(View.INVISIBLE);
        }

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

        button_Chosen_Card = (ImageButton)findViewById(R.id.chosen_Card);
        //-遊戲參數------------
        deck_Index = 0;
        deck_Pointer = new int[FLIP_CARD_NUM];
        player_Pointer = 0;
        card_Taken_No = -1; //被拿取的卡片代號
        for(int i = 0 ;i< FLIP_CARD_NUM ;i++){
            deck_Counter_Value--;
        }

        //初始化介面上的籌碼數量
        for(int i = 0 ;i<4 ;i++){
            textView_coin[i].setText(String.valueOf(7));
        }

        need_Coin_Taken = new int[4];//紀錄4種籌碼的拿取數量
        for (int i = 0 ;i<4;i++)
            need_Coin_Taken[i] = 0;

        coin_Limit = new int[4];
        coin_Limit[0] = (new gameMethod()).getCoinA_Amount();//籌碼A數量限制
        coin_Limit[1] = (new gameMethod()).getCoinB_Amount();//籌碼B數量限制
        coin_Limit[2] = (new gameMethod()).getCoinC_Amount();//籌碼C數量限制
        coin_Limit[3] = (new gameMethod()).getCoinD_Amount();//籌碼D數量限制
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

        //new DataThread().start();

        //牌面初始化
        for(int i = 0 ; i< 4 ;i++){
            //從洗好的牌堆中，將第 0~3 中的卡片圖示拿出
            int imageResource = getResources().getIdentifier((myDeck.getCards().get(i)).getCard_Picture(), null, getPackageName());
            flip_Card[i].setImageResource(imageResource);
            //將卡片數值提示文字設定在卡片可拿取位置
            textView_Flip_Card[i].setText((myDeck.getCards().get(i)).getCard_Data());
            //卡片可拿取位置設定從牌堆中拿取的新卡片
            deck_Pointer[i] = i;
            //deck_Index指向牌堆中的下一張卡片
            deck_Index++;
        }
        textView_Player_Name[0].setBackgroundColor(Color.RED);
        //textView_Player_Name[0].setBackgroundColor(Color.parseColor("#E6E6E6"));

        deck_Counter.setText(String.valueOf(deck_Counter_Value));
    }

    public void on_flip_Card0_Clicked(View ImageButton){
        if(MY_DEBUG){
            System.out.println("card 1 clicked : score = "+ (myDeck.getCards().get(deck_Pointer[0])).getCard_Score());
            System.out.println("card 1 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[0])).getPermanent_PointA());
            System.out.println("card 1 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[0])).getPermanent_PointB());
            System.out.println("card 1 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[0])).getPermanent_PointC());
            System.out.println("card 1 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[0])).getPermanent_PointD());
            System.out.println("card 1 clicked : A = "+ (myDeck.getCards().get(deck_Pointer[0])).getNeed_PointA());
            System.out.println("card 1 clicked : B = "+ (myDeck.getCards().get(deck_Pointer[0])).getNeed_PointB());
            System.out.println("card 1 clicked : C = "+ (myDeck.getCards().get(deck_Pointer[0])).getNeed_PointC());
            System.out.println("card 1 clicked : D = "+ (myDeck.getCards().get(deck_Pointer[0])).getNeed_PointD());
        }

        //如果拿了籌碼，不能拿牌
        if(coin_Taken_Counter<=0)
        if((myDeck.getCards().get(deck_Pointer[0])).card_Is_Available(player[player_Pointer].getResourceA(),player[player_Pointer].getResourceB(),player[player_Pointer].getResourceC(),player[player_Pointer].getResourceD())){
            //更新拿取的卡片樣式
            int imageResource = getResources().getIdentifier("@mipmap/"+(myDeck.getCards().get(deck_Pointer[0])).getCard_Name(), null, getPackageName());
            button_Chosen_Card.setImageResource(imageResource);
            card_Taken_No = 0;

            //將被拿去的卡片圖示清空
            int imageResource2 = getResources().getIdentifier("@mipmap/c_null", null, getPackageName());
            flip_Card[0].setImageResource(imageResource2);
        }

    }
    public void on_flip_Card1_Clicked(View ImageButton){
        if(MY_DEBUG){
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
        //如果拿了籌碼，不能拿牌
        if(coin_Taken_Counter<=0)
        if((myDeck.getCards().get(deck_Pointer[1])).card_Is_Available(player[player_Pointer].getResourceA(),player[player_Pointer].getResourceB(),player[player_Pointer].getResourceC(),player[player_Pointer].getResourceD())){
            //更新拿取的卡片樣式
            int imageResource = getResources().getIdentifier("@mipmap/"+(myDeck.getCards().get(deck_Pointer[1])).getCard_Name(), null, getPackageName());
            button_Chosen_Card.setImageResource(imageResource);
            card_Taken_No = 1;

            //將被拿去的卡片圖示清空
            int imageResource2 = getResources().getIdentifier("@mipmap/c_null", null, getPackageName());
            flip_Card[1].setImageResource(imageResource2);
        }
    }
    public void on_flip_Card2_Clicked(View ImageButton){
        if(MY_DEBUG){
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
        //如果拿了籌碼，不能拿牌
        if(coin_Taken_Counter<=0)
        if((myDeck.getCards().get(deck_Pointer[2])).card_Is_Available(player[player_Pointer].getResourceA(),player[player_Pointer].getResourceB(),player[player_Pointer].getResourceC(),player[player_Pointer].getResourceD())){
            //更新拿取的卡片樣式
            int imageResource = getResources().getIdentifier("@mipmap/"+(myDeck.getCards().get(deck_Pointer[2])).getCard_Name(), null, getPackageName());
            button_Chosen_Card.setImageResource(imageResource);
            card_Taken_No = 2;

            //將被拿去的卡片圖示清空
            int imageResource2 = getResources().getIdentifier("@mipmap/c_null", null, getPackageName());
            flip_Card[2].setImageResource(imageResource2);
        }
    }
    public void on_flip_Card3_Clicked(View ImageButton){
        if(MY_DEBUG){
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
        //如果拿了籌碼，不能拿牌
        if(coin_Taken_Counter<=0)
        if((myDeck.getCards().get(deck_Pointer[3])).card_Is_Available(player[player_Pointer].getResourceA(),player[player_Pointer].getResourceB(),player[player_Pointer].getResourceC(),player[player_Pointer].getResourceD())){
            //更新拿取的卡片樣式
            int imageResource = getResources().getIdentifier("@mipmap/"+(myDeck.getCards().get(deck_Pointer[3])).getCard_Name(), null, getPackageName());
            button_Chosen_Card.setImageResource(imageResource);
            card_Taken_No = 3;

            //將被拿去的卡片圖示清空
            int imageResource2 = getResources().getIdentifier("@mipmap/c_null", null, getPackageName());
            flip_Card[3].setImageResource(imageResource2);
        }
    }

    public void on_button_Chosen_Card(View ImageButton){

    }

    //取籌碼A
    public void on_button_coinA_Clicked(View ImageButton){
        //如果拿了卡片，或沒有籌碼了，不能拿籌碼
       if (card_Taken_No == -1 || coin_Limit[0]>0)
        if(coin_Taken_Counter<RESOURCE_TAKEN_MAX){
            int imageResource = getResources().getIdentifier("@mipmap/coin_a", null, getPackageName());
            //player[0].setNeed_PointA(1);
            button_Hand[coin_Taken_Counter].setImageResource(imageResource);
            coin_Taken_Counter++;
            need_Coin_Taken[0]++;//紀錄A籌碼的拿取數量
        }

    }
    //取籌碼B
    public void on_button_coinB_Clicked(View ImageButton){
        //如果拿了卡片，或沒有籌碼了，不能拿籌碼
        if (card_Taken_No == -1 || coin_Limit[1]>0)
        if(coin_Taken_Counter<RESOURCE_TAKEN_MAX){
            int imageResource = getResources().getIdentifier("@mipmap/coin_b", null, getPackageName());
            //player[0].setNeed_PointA(1);
            button_Hand[coin_Taken_Counter].setImageResource(imageResource);
            coin_Taken_Counter++;
            need_Coin_Taken[1]++;//紀錄B籌碼的拿取數量
        }

    }
    //取籌碼C
    public void on_button_coinC_Clicked(View ImageButton){
        //如果拿了卡片，或沒有籌碼了，不能拿籌碼
        if (card_Taken_No == -1 || coin_Limit[2]>0)
        if(coin_Taken_Counter<RESOURCE_TAKEN_MAX){
            int imageResource = getResources().getIdentifier("@mipmap/coin_c", null, getPackageName());
            //player[0].setNeed_PointA(1);
            button_Hand[coin_Taken_Counter].setImageResource(imageResource);
            coin_Taken_Counter++;
            need_Coin_Taken[2]++;//紀錄C籌碼的拿取數量
        }

    }
    //取籌碼D
    public void on_button_coinD_Clicked(View ImageButton){
        //如果拿了卡片，或沒有籌碼了，不能拿籌碼
        if (card_Taken_No == -1 || coin_Limit[3]>0)
        if(coin_Taken_Counter<RESOURCE_TAKEN_MAX){
            int imageResource = getResources().getIdentifier("@mipmap/coin_d", null, getPackageName());
            //player[0].setNeed_PointA(1);
            button_Hand[coin_Taken_Counter].setImageResource(imageResource);
            coin_Taken_Counter++;
            need_Coin_Taken[3]++;//紀錄D籌碼的拿取數量
        }

    }

    //用來 debug 的 button
    public void on_test_ButtonClicked(View button) {


        //player[0].setPlayer_Score((int) 8);
        //textView_Player_Score[0].setText(String.format("%s/%s", player[0].getPlayer_Score(), WIN_SCORE));
        for(int i = 0 ;i < PLAYER_NUM;i++)
            if((new gameMethod()).is_Win(player[i].getPlayer_Score())){
                Intent intent = new Intent(this,gameOverActivity.class);
                intent.putExtra("winner",player[i].getPlayer_Name());
                this.finish();
                startActivity(intent);
            }

        //如果是拿籌碼當作回合動作
        if(coin_Taken_Counter>0){
            //將拿取的籌碼數紀錄，數量為 (原本手上數量 + 本回合拿取數量)
            player[player_Pointer].setNeed_PointA( player[player_Pointer].getNeed_PointA() + need_Coin_Taken[0] );
            player[player_Pointer].setNeed_PointB( player[player_Pointer].getNeed_PointB() + need_Coin_Taken[1] );
            player[player_Pointer].setNeed_PointC( player[player_Pointer].getNeed_PointC() + need_Coin_Taken[2] );
            player[player_Pointer].setNeed_PointD( player[player_Pointer].getNeed_PointD() + need_Coin_Taken[3] );
        }
        else if(card_Taken_No != -1){
            //如果是拿卡片當作回合動作，更新獲取的永久籌碼，以及分數
            player[player_Pointer].setPermanent_PointA( player[player_Pointer].getPermanent_PointA() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointA() );
            player[player_Pointer].setPermanent_PointB( player[player_Pointer].getPermanent_PointB() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointB() );
            player[player_Pointer].setPermanent_PointC( player[player_Pointer].getPermanent_PointC() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointC() );
            player[player_Pointer].setPermanent_PointD( player[player_Pointer].getPermanent_PointD() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointD() );
            player[player_Pointer].setPlayer_Score( player[player_Pointer].getPlayer_Score() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getCard_Score() );

            //更新玩家手中的消耗籌碼
            //如果手中的永久籌碼數量少於要消耗掉的籌碼，減少玩家手中的消耗籌碼，並將消耗籌碼歸還且更新介面
            if(player[player_Pointer].getPermanent_PointA() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA()){
                player[player_Pointer].setNeed_PointA( player[player_Pointer].getNeed_PointA() - ( (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA() - player[player_Pointer].getPermanent_PointA() ) );
                coin_Limit[0] +=( (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA() - player[player_Pointer].getPermanent_PointA() );
                textView_coin[0].setText(String.valueOf(coin_Limit[0]));
            }
            if(player[player_Pointer].getPermanent_PointB() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB()){
                player[player_Pointer].setNeed_PointB( player[player_Pointer].getNeed_PointB() - ( (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB() - player[player_Pointer].getPermanent_PointB() ) );
                coin_Limit[1] +=( (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB() - player[player_Pointer].getPermanent_PointB());
                textView_coin[1].setText(String.valueOf(coin_Limit[1]));

            }
            if(player[player_Pointer].getPermanent_PointC() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC()){
                player[player_Pointer].setNeed_PointC( player[player_Pointer].getNeed_PointC() - ( (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC() - player[player_Pointer].getPermanent_PointC() ) );
                coin_Limit[2] +=( (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC() - player[player_Pointer].getPermanent_PointC());
                textView_coin[2].setText(String.valueOf(coin_Limit[2]));
            }
            if(player[player_Pointer].getPermanent_PointD() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD()){
                player[player_Pointer].setNeed_PointD( player[player_Pointer].getNeed_PointD() - ( (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD() - player[player_Pointer].getPermanent_PointD() ) );
                coin_Limit[3] +=( (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD() - player[player_Pointer].getPermanent_PointD());
                textView_coin[3].setText(String.valueOf(coin_Limit[3]));
            }
        }


        //更新介面上的消耗籌碼數量
        String update_Player_ResourceA = player[player_Pointer].getNeed_PointA()+"+"+player[player_Pointer].getPermanent_PointA();
        String update_Player_ResourceB = player[player_Pointer].getNeed_PointB()+"+"+player[player_Pointer].getPermanent_PointB();
        String update_Player_ResourceC = player[player_Pointer].getNeed_PointC()+"+"+player[player_Pointer].getPermanent_PointC();
        String update_Player_ResourceD = player[player_Pointer].getNeed_PointD()+"+"+player[player_Pointer].getPermanent_PointD();

        if(player_Pointer == 0){
            textView_Player_Score[0].setText(String.format("%s/%s", player[0].getPlayer_Score(), WIN_SCORE));
            textView_Player1_A_resource.setText(update_Player_ResourceA);
            textView_Player1_B_resource.setText(update_Player_ResourceB);
            textView_Player1_C_resource.setText(update_Player_ResourceC);
            textView_Player1_D_resource.setText(update_Player_ResourceD);
        }
        else if(player_Pointer == 1){
            textView_Player_Score[1].setText(String.format("%s/%s", player[1].getPlayer_Score(), WIN_SCORE));
            textView_Player2_A_resource.setText(update_Player_ResourceA);
            textView_Player2_B_resource.setText(update_Player_ResourceB);
            textView_Player2_C_resource.setText(update_Player_ResourceC);
            textView_Player2_D_resource.setText(update_Player_ResourceD);
        }
        else if(player_Pointer == 2){
            textView_Player_Score[2].setText(String.format("%s/%s", player[2].getPlayer_Score(), WIN_SCORE));
            textView_Player3_A_resource.setText(update_Player_ResourceA);
            textView_Player3_B_resource.setText(update_Player_ResourceB);
            textView_Player3_C_resource.setText(update_Player_ResourceC);
            textView_Player3_D_resource.setText(update_Player_ResourceD);
        }
        else if(player_Pointer == 3){
            textView_Player_Score[3].setText(String.format("%s/%s", player[3].getPlayer_Score(), WIN_SCORE));
            textView_Player4_A_resource.setText(update_Player_ResourceA);
            textView_Player4_B_resource.setText(update_Player_ResourceB);
            textView_Player4_C_resource.setText(update_Player_ResourceC);
            textView_Player4_D_resource.setText(update_Player_ResourceD);
        }
        //將被拿取位置的卡片補上新抽的卡片
        if(card_Taken_No != -1){
            //從洗好的牌堆中，將第 deck_Index 中的卡片圖示拿出
            int imageResource = getResources().getIdentifier((myDeck.getCards().get(deck_Index)).getCard_Picture(), null, getPackageName());
            flip_Card[card_Taken_No].setImageResource(imageResource);
            //將卡片數值提示文字設定在卡片被拿取位置
            textView_Flip_Card[card_Taken_No].setText((myDeck.getCards().get(deck_Index)).getCard_Data());
            //卡片被拿取位置設定從牌堆中拿取的新卡片
            deck_Pointer[card_Taken_No] = deck_Index;
            //deck_Index指向牌堆中的下一張卡片
            deck_Index++;

            // 將介面上，手中拿取的卡片圖案更新為空白
            int imageResource2 = getResources().getIdentifier("@mipmap/c_null", null, getPackageName());
            button_Chosen_Card.setImageResource(imageResource2);
        }


        coin_Taken_Counter = 0;//拿取得消耗籌碼總數歸零
        card_Taken_No = -1;//將拿去的卡片初始化

        // 將介面上，手中拿去的消耗籌碼圖案更新為空白
        int imageResource = getResources().getIdentifier("@mipmap/coin_null", null, getPackageName());
        for(int i =0;i<3;i++){
            button_Hand[i].setImageResource(imageResource);
        }

        //將本回合被拿取的籌碼堆更新數量
        for(int i = 0 ;i<4 ;i++){
            if(need_Coin_Taken[i]>0){
                coin_Limit[i] -=need_Coin_Taken[i];
                textView_coin[i].setText(String.valueOf(coin_Limit[i]));
            }
        }

        //將本回合拿取的消耗籌碼數歸零
        for(int i = 0 ;i <4 ;i++){
            need_Coin_Taken[i]=0;
        }

        player_Pointer = (++player_Pointer)%PLAYER_NUM;//換下一位玩家操作
        System.out.println("turn to: player "+ String.valueOf(player_Pointer));
        for(int i = 0 ;i<4 ;i++)
            textView_Player_Name[i].setBackgroundColor(Color.parseColor("#E6E6E6"));
        textView_Player_Name[player_Pointer].setBackgroundColor(Color.RED);

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
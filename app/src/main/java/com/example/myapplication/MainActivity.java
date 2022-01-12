package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.TCP_connect.TCPClient;
import com.example.myapplication.TCP_connect.TCP_connect;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    boolean MY_DEBUG = false;
    Map<String, String> status_type = new HashMap<>();

    String single_mode = "single";
    String multi_mode = "multi";
    String current_mode = single_mode;
    Bundle extras;
    boolean GameStart = false;
    //-遊戲參數------------
    int PLAYER_NUM=4; //玩家人數
    int FLIP_CARD_NUM = 4 ;//翻開的卡片數量
    int RESOURCE_NUM = 4; //資源種類數量
    int RESOURCE_TAKEN_MAX = 3;//回合可拿取資源上限
    int WIN_SCORE = gameMethod.getWIN_SCORE();//勝利分數
    int PLAYER_RESOURCE_MAX = gameMethod.getHand_Coin_Limit();//玩家手中消耗籌碼上限

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
    String resource_Initial ="<b><tt>0</tt></b>(+0)"; //0(+0) 單一資源總和(+永久籌碼)
    //--遊戲介面元件-------------
    Button test_Button; //功能驗證按鈕
    TextView deck_Counter;  //Deck 剩餘數量顯示
    TextView[] textView_Player_Score; //玩家分數
    TextView[] textView_Player_Name;

    TextView[] textView_Player_A_resource = new TextView[PLAYER_NUM];
    TextView[] textView_Player_B_resource = new TextView[PLAYER_NUM];
    TextView[] textView_Player_C_resource = new TextView[PLAYER_NUM];
    TextView[] textView_Player_D_resource = new TextView[PLAYER_NUM];

    TextView[] textView_Player_Total_Res;//玩家手上持有的消耗籌碼總數

    ImageButton[] button_coin;//介面上的籌碼按鈕
    TextView[] textView_coin;
    ImageButton[] button_Hand;

    ImageButton[] flip_Card; //翻開卡片
    TextView[] textView_Flip_Card;//翻開卡片的食物描述
    ImageButton button_Chosen_Card;//被選擇的卡片

    TextView textView_hint_message; //提示玩家可做行為
    //---------------------
    //----tcp-----
    ExecutorService exec;
    MyBroadcast myBroadcast;
    StringBuffer stringBuffer;
    //TCPServer tcpServer;
    TCPClient tcpClient;
    String send_Msg;
    int current_player_counter = 0;
    int my_Seq = -1;
    //------------

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

        textView_Player_A_resource[0] = (TextView)findViewById(R.id.p1A_resource);
        textView_Player_B_resource[0] = (TextView)findViewById(R.id.p1B_resource);
        textView_Player_C_resource[0] = (TextView)findViewById(R.id.p1C_resource);
        textView_Player_D_resource[0] = (TextView)findViewById(R.id.p1D_resource);

        textView_Player_A_resource[1] = (TextView)findViewById(R.id.p2A_resource);
        textView_Player_B_resource[1] = (TextView)findViewById(R.id.p2B_resource);
        textView_Player_C_resource[1] = (TextView)findViewById(R.id.p2C_resource);
        textView_Player_D_resource[1] = (TextView)findViewById(R.id.p2D_resource);

        textView_Player_A_resource[2] = (TextView)findViewById(R.id.p3A_resource);
        textView_Player_B_resource[2] = (TextView)findViewById(R.id.p3B_resource);
        textView_Player_C_resource[2] = (TextView)findViewById(R.id.p3C_resource);
        textView_Player_D_resource[2] = (TextView)findViewById(R.id.p3D_resource);

        textView_Player_A_resource[3] = (TextView)findViewById(R.id.p4A_resource);
        textView_Player_B_resource[3] = (TextView)findViewById(R.id.p4B_resource);
        textView_Player_C_resource[3] = (TextView)findViewById(R.id.p4C_resource);
        textView_Player_D_resource[3] = (TextView)findViewById(R.id.p4D_resource);

        textView_Player_A_resource[0].setText(Html.fromHtml(resource_Initial));
        textView_Player_B_resource[0].setText(Html.fromHtml(resource_Initial));
        textView_Player_C_resource[0].setText(Html.fromHtml(resource_Initial));
        textView_Player_D_resource[0].setText(Html.fromHtml(resource_Initial));

        textView_Player_A_resource[1].setText(Html.fromHtml(resource_Initial));
        textView_Player_B_resource[1].setText(Html.fromHtml(resource_Initial));
        textView_Player_C_resource[1].setText(Html.fromHtml(resource_Initial));
        textView_Player_D_resource[1].setText(Html.fromHtml(resource_Initial));

        textView_Player_A_resource[2].setText(Html.fromHtml(resource_Initial));
        textView_Player_B_resource[2].setText(Html.fromHtml(resource_Initial));
        textView_Player_C_resource[2].setText(Html.fromHtml(resource_Initial));
        textView_Player_D_resource[2].setText(Html.fromHtml(resource_Initial));

        textView_Player_A_resource[3].setText(Html.fromHtml(resource_Initial));
        textView_Player_B_resource[3].setText(Html.fromHtml(resource_Initial));
        textView_Player_C_resource[3].setText(Html.fromHtml(resource_Initial));
        textView_Player_D_resource[3].setText(Html.fromHtml(resource_Initial));


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

        button_Chosen_Card = (ImageButton)findViewById(R.id.chosen_Card);

        textView_hint_message = (TextView)findViewById(R.id.hint_message);
        textView_hint_message.setMovementMethod(ScrollingMovementMethod.getInstance());

        textView_Player_Total_Res = new TextView[PLAYER_NUM]; //玩家的消耗籌碼總數
        textView_Player_Total_Res[0] =(TextView)findViewById(R.id.p1_Total_res);
        textView_Player_Total_Res[1] =(TextView)findViewById(R.id.p2_Total_res);
        textView_Player_Total_Res[2] =(TextView)findViewById(R.id.p3_Total_res);
        textView_Player_Total_Res[3] =(TextView)findViewById(R.id.p4_Total_res);

        //初始化介面上的籌碼數量
        for(int i = 0 ;i<4 ;i++){
            textView_coin[i].setText(String.valueOf(7));
        }

        // 將介面上，手中拿去的消耗籌碼圖案更新為空白
        int imageResource = getResources().getIdentifier("@mipmap/coin_null", null, getPackageName());
        for(int i =0;i<3;i++){
            button_Hand[i].setImageResource(imageResource);
        }

        //-遊戲參數------------
        deck_Index = 0;
        deck_Pointer = new int[FLIP_CARD_NUM];
        player_Pointer = 0; //指向目前動作的玩家，初始化預設指向第一位玩家
        card_Taken_No = -1; //被拿取的卡片代號




        need_Coin_Taken = new int[4];//紀錄4種籌碼的拿取數量
        for (int i = 0 ;i<4;i++)
            need_Coin_Taken[i] = 0;

        coin_Limit = new int[4];
        coin_Limit[0] = (new gameMethod()).getCoinA_Amount();//籌碼A數量限制
        coin_Limit[1] = (new gameMethod()).getCoinB_Amount();//籌碼B數量限制
        coin_Limit[2] = (new gameMethod()).getCoinC_Amount();//籌碼C數量限制
        coin_Limit[3] = (new gameMethod()).getCoinD_Amount();//籌碼D數量限制
        //--------------------------------------

        //-tcp------------

        status_type.put("player","player");

        extras = getIntent().getExtras();
        current_mode = extras.getString("mode");
        if(current_mode.equals(multi_mode)){
            String Name = extras.getString("Name");
            String IP = extras.getString("IP");
            int Port = Integer.parseInt(extras.getString("Port"));

            exec = Executors.newCachedThreadPool();
            myBroadcast = new MyBroadcast();
            stringBuffer = new StringBuffer();

            //註冊廣播，接收來自對方設備回傳的資料
            IntentFilter intentFilter = new IntentFilter(TCPClient.RECEIVE_ACTION);
            registerReceiver(myBroadcast, intentFilter);

            tcpClient = new TCPClient(IP, Port,Name, this);
            exec.execute(tcpClient);
            game_initial();
        }
        else if(current_mode.equals(single_mode)){
            game_initial();
        }
        //-----------------------




    }

    public void show_hint(String msg){
//把xml的資源轉成view
        LayoutInflater inflater = getLayoutInflater();
        //R.layout.toast_layout XML名稱
        //R.id.toast_layoutt XML裡面Layout ID
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layoutt));
        //透過 inflater跟View方式來取得元件的控制權
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = Toast.makeText(this, "Toast置中顯示", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(layout);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);

    }
    //遊戲初始化
    public void game_initial(){
        //讀卡組數值及食物名稱
        InputStreamReader deck_Value = new InputStreamReader(this.getResources().openRawResource(R.raw.carddata));
        InputStreamReader food_Name =  new InputStreamReader(this.getResources().openRawResource(R.raw.foodname));
        myDeck = new Deck(deck_Value ,food_Name);
        player = new Player[PLAYER_NUM];

        //set player name and initial score
        for (int i = 0 ;i<PLAYER_NUM;i++){
            player[i] = new Player("player "+(i+1));
            textView_Player_Score[i].setText(String.format("%s/%s", player[i].getPlayer_Score(), WIN_SCORE));
            if(current_mode.equals(single_mode)){
                WIN_SCORE = extras.getInt("winScore");
                textView_Player_Score[i].setText(String.format("%s/%s", player[i].getPlayer_Score(), WIN_SCORE));
            }
        }
        if(current_mode.equals(single_mode)){
            //setup player name from previous activity

            player[0].setPlayer_Name(extras.getString("player1"));
            player[1].setPlayer_Name(extras.getString("player2"));
            player[2].setPlayer_Name(extras.getString("player3"));
            player[3].setPlayer_Name(extras.getString("player4"));
        }
        else if(current_mode.equals(multi_mode)){
            player[0].setPlayer_Name("");
            player[1].setPlayer_Name("");
            player[2].setPlayer_Name("");
            player[3].setPlayer_Name("");

            //player name will refresh in receive
        }


        //更新介面上的名字，食材總數
        for (int i = 0 ;i<PLAYER_NUM;i++){
            textView_Player_Name[i].setText(player[i].getPlayer_Name());
            textView_Player_Total_Res[i].setText(String.valueOf(player[i].getTotal_Need_Point()));
        }
        if(current_mode.equals(single_mode)) {
            for(int i = 0 ;i< FLIP_CARD_NUM ;i++){
                deck_Counter_Value--;
            }
            //牌面初始化
            for(int i = 0 ; i< 4 ;i++){
                //從洗好的牌堆中，將第 0~3 中的卡片圖示拿出
                int imageResource = getResources().getIdentifier((myDeck.getCards().get(i)).getCard_Picture(), null, getPackageName());
                flip_Card[i].setImageResource(imageResource);
                //將卡片數值提示文字設定在卡片可拿取位置
                textView_Flip_Card[i].setText((myDeck.getCards().get(i)).getFood_Name());
                //卡片可拿取位置設定從牌堆中拿取的新卡片
                deck_Pointer[i] = i;
                //deck_Index指向牌堆中的下一張卡片
                deck_Index++;
            }

            player_Pointer = (int) (Math.random() * (3 - 0 + 1));//隨機一位玩家作為初始玩家
            textView_Player_Name[player_Pointer].setBackgroundColor(Color.RED);//初始玩家標記為紅色
            show_hint(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
            textView_hint_message.setText(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
        }

        deck_Counter.setText("x"+String.valueOf(deck_Counter_Value));
    }

    public void action_hint(int pointer){
        System.out.println("card "+(pointer+1)+" clicked : score = "+ (myDeck.getCards().get(deck_Pointer[pointer])).getCard_Score());
        System.out.println("card "+(pointer+1)+" clicked : A = "+ (myDeck.getCards().get(deck_Pointer[pointer])).getPermanent_PointA());
        System.out.println("card "+(pointer+1)+" clicked : B = "+ (myDeck.getCards().get(deck_Pointer[pointer])).getPermanent_PointB());
        System.out.println("card "+(pointer+1)+" clicked : C = "+ (myDeck.getCards().get(deck_Pointer[pointer])).getPermanent_PointC());
        System.out.println("card "+(pointer+1)+" clicked : D = "+ (myDeck.getCards().get(deck_Pointer[pointer])).getPermanent_PointD());
        System.out.println("card "+(pointer+1)+" clicked : A = "+ (myDeck.getCards().get(deck_Pointer[pointer])).getNeed_PointA());
        System.out.println("card "+(pointer+1)+" clicked : B = "+ (myDeck.getCards().get(deck_Pointer[pointer])).getNeed_PointB());
        System.out.println("card "+(pointer+1)+" clicked : C = "+ (myDeck.getCards().get(deck_Pointer[pointer])).getNeed_PointC());
        System.out.println("card "+(pointer+1)+" clicked : D = "+ (myDeck.getCards().get(deck_Pointer[pointer])).getNeed_PointD());
    }

    public void on_flip_Card0_Clicked(View ImageButton){
        if((player_Pointer == my_Seq)||current_mode.equals(single_mode)) {
            if (MY_DEBUG) {
                action_hint(0);
            }

            //如果拿了籌碼，不能拿牌，且拿牌後不能拿其他牌
            boolean card_Available = (myDeck.getCards().get(deck_Pointer[0])).card_Is_Available(player[player_Pointer].getResourceA(), player[player_Pointer].getResourceB(), player[player_Pointer].getResourceC(), player[player_Pointer].getResourceD());
            if (coin_Taken_Counter <= 0 && card_Taken_No == -1 && card_Available) {
                if (card_Available) {
                    //更新拿取的卡片樣式
                    int imageResource = getResources().getIdentifier("@mipmap/" + (myDeck.getCards().get(deck_Pointer[0])).getCard_Name(), null, getPackageName());
                    button_Chosen_Card.setImageResource(imageResource);
                    card_Taken_No = 0;

                    //將被拿去的卡片圖示清空
                    int imageResource2 = getResources().getIdentifier("@mipmap/c_null2", null, getPackageName());
                    flip_Card[0].setImageResource(imageResource2);
                }
            } else {
                if (card_Taken_No != -1)
                    show_hint(String.format("由於玩家 %s 您已拿取卡片，無法於本回合再次拿取卡片", player[player_Pointer].getPlayer_Name()));
                    //textView_hint_message.setText(String.format("由於玩家 %s 您已拿取卡片，無法於本回合再次拿取卡片", player[player_Pointer].getPlayer_Name()));
                else if (coin_Taken_Counter > 0)
                    show_hint(String.format("由於玩家 %s 您已選擇拿取籌碼，無法於本回合拿取卡片", player[player_Pointer].getPlayer_Name()));
                    //textView_hint_message.setText(String.format("由於玩家 %s 您已選擇拿取籌碼，無法於本回合拿取卡片", player[player_Pointer].getPlayer_Name()));
                else if (!card_Available)
                    show_hint(String.format("玩家 %s 您資源不足以購買該卡片", player[player_Pointer].getPlayer_Name()));
                //textView_hint_message.setText("您的資源不足以購買該卡片");
            }
        }

    }
    public void on_flip_Card1_Clicked(View ImageButton){
        if((player_Pointer == my_Seq)||current_mode.equals(single_mode)) {
            if (MY_DEBUG) {
                action_hint(1);
            }
            //如果拿了籌碼，不能拿牌，且拿牌後不能拿其他牌
            boolean card_Available = (myDeck.getCards().get(deck_Pointer[1])).card_Is_Available(player[player_Pointer].getResourceA(), player[player_Pointer].getResourceB(), player[player_Pointer].getResourceC(), player[player_Pointer].getResourceD());
            if (coin_Taken_Counter <= 0 && card_Taken_No == -1 && card_Available) {
                if (card_Available) {
                    //更新拿取的卡片樣式
                    int imageResource = getResources().getIdentifier("@mipmap/" + (myDeck.getCards().get(deck_Pointer[1])).getCard_Name(), null, getPackageName());
                    button_Chosen_Card.setImageResource(imageResource);
                    card_Taken_No = 1;

                    //將被拿去的卡片圖示清空
                    int imageResource2 = getResources().getIdentifier("@mipmap/c_null2", null, getPackageName());
                    flip_Card[1].setImageResource(imageResource2);
                }
            } else {
                if (card_Taken_No != -1)
                    show_hint(String.format("由於玩家 %s 您已拿取卡片，無法於本回合再次拿取卡片", player[player_Pointer].getPlayer_Name()));
                    //textView_hint_message.setText(String.format("由於玩家 %s 您已拿取卡片，無法於本回合再次拿取卡片", player[player_Pointer].getPlayer_Name()));
                else if (coin_Taken_Counter > 0)
                    show_hint(String.format("由於玩家 %s 您已選擇拿取籌碼，無法於本回合拿取卡片", player[player_Pointer].getPlayer_Name()));
                    //textView_hint_message.setText(String.format("由於玩家 %s 您已選擇拿取籌碼，無法於本回合拿取卡片", player[player_Pointer].getPlayer_Name()));
                else if (!card_Available)
                    show_hint(String.format("玩家 %s 您資源不足以購買該卡片", player[player_Pointer].getPlayer_Name()));
                //textView_hint_message.setText("您的資源不足以購買該卡片");
            }
        }
    }
    public void on_flip_Card2_Clicked(View ImageButton){
        if((player_Pointer == my_Seq)||current_mode.equals(single_mode)) {
            if (MY_DEBUG) {
                action_hint(2);
            }
            //如果拿了籌碼，不能拿牌，且拿牌後不能拿其他牌
            boolean card_Available = (myDeck.getCards().get(deck_Pointer[2])).card_Is_Available(player[player_Pointer].getResourceA(), player[player_Pointer].getResourceB(), player[player_Pointer].getResourceC(), player[player_Pointer].getResourceD());
            if (coin_Taken_Counter <= 0 && card_Taken_No == -1 && card_Available) {
                if (card_Available) {
                    //更新拿取的卡片樣式
                    int imageResource = getResources().getIdentifier("@mipmap/" + (myDeck.getCards().get(deck_Pointer[2])).getCard_Name(), null, getPackageName());
                    button_Chosen_Card.setImageResource(imageResource);
                    card_Taken_No = 2;

                    //將被拿去的卡片圖示清空
                    int imageResource2 = getResources().getIdentifier("@mipmap/c_null2", null, getPackageName());
                    flip_Card[2].setImageResource(imageResource2);
                }
            } else {
                if (card_Taken_No != -1)
                    show_hint(String.format("由於玩家 %s 您已拿取卡片，無法於本回合再次拿取卡片", player[player_Pointer].getPlayer_Name()));
                    //textView_hint_message.setText(String.format("由於玩家 %s 您已拿取卡片，無法於本回合再次拿取卡片", player[player_Pointer].getPlayer_Name()));
                else if (coin_Taken_Counter > 0)
                    show_hint(String.format("由於玩家 %s 您已選擇拿取籌碼，無法於本回合拿取卡片", player[player_Pointer].getPlayer_Name()));
                    //textView_hint_message.setText(String.format("由於玩家 %s 您已選擇拿取籌碼，無法於本回合拿取卡片", player[player_Pointer].getPlayer_Name()));
                else if (!card_Available)
                    show_hint(String.format("玩家 %s 您資源不足以購買該卡片", player[player_Pointer].getPlayer_Name()));
                //textView_hint_message.setText("您的資源不足以購買該卡片");
            }
        }
    }
    public void on_flip_Card3_Clicked(View ImageButton){
        if((player_Pointer == my_Seq)||current_mode.equals(single_mode)) {
            if (MY_DEBUG) {
                action_hint(3);
            }
            //如果拿了籌碼，不能拿牌，且拿牌後不能拿其他牌
            boolean card_Available = (myDeck.getCards().get(deck_Pointer[3])).card_Is_Available(player[player_Pointer].getResourceA(), player[player_Pointer].getResourceB(), player[player_Pointer].getResourceC(), player[player_Pointer].getResourceD());
            if (coin_Taken_Counter <= 0 && card_Taken_No == -1 && card_Available) {
                if (card_Available) {
                    //更新拿取的卡片樣式
                    int imageResource = getResources().getIdentifier("@mipmap/" + (myDeck.getCards().get(deck_Pointer[3])).getCard_Name(), null, getPackageName());
                    button_Chosen_Card.setImageResource(imageResource);
                    card_Taken_No = 3;

                    //將被拿去的卡片圖示清空
                    int imageResource2 = getResources().getIdentifier("@mipmap/c_null2", null, getPackageName());
                    flip_Card[3].setImageResource(imageResource2);
                }
            } else {
                if (card_Taken_No != -1)
                    show_hint(String.format("由於玩家 %s 您已拿取卡片，無法於本回合再次拿取卡片", player[player_Pointer].getPlayer_Name()));
                    //textView_hint_message.setText(String.format("由於玩家 %s 您已拿取卡片，無法於本回合再次拿取卡片", player[player_Pointer].getPlayer_Name()));
                else if (coin_Taken_Counter > 0)
                    show_hint(String.format("由於玩家 %s 您已選擇拿取籌碼，無法於本回合拿取卡片", player[player_Pointer].getPlayer_Name()));
                    //textView_hint_message.setText(String.format("由於玩家 %s 您已選擇拿取籌碼，無法於本回合拿取卡片", player[player_Pointer].getPlayer_Name()));
                else if (!card_Available)
                    show_hint(String.format("玩家 %s 您資源不足以購買該卡片", player[player_Pointer].getPlayer_Name()));
                //textView_hint_message.setText("您的資源不足以購買該卡片");
            }
        }
    }

    public void on_button_Chosen_Card(View ImageButton){

    }

    //取消耗籌碼A
    public void on_button_coinA_Clicked(View ImageButton){
        if((player_Pointer == my_Seq)||current_mode.equals(single_mode)) {
            int player_Resource = ((player[player_Pointer].getTotal_Need_Point() + coin_Taken_Counter));//玩家手上的資源 + 預計拿取的消耗籌碼數量
            int valid_Taken = (new gameMethod()).is_Over_Once_Resource_Max(need_Coin_Taken[0], need_Coin_Taken[1], need_Coin_Taken[2], need_Coin_Taken[3], "A", coin_Limit[0]);//檢查玩家預計拿的籌碼是否正常
            //如果沒拿卡片，且籌碼堆還有消耗籌碼A，且玩家手上沒超出上限，且該籌碼沒拿超過2個，且拿了2個相同籌碼不能再拿其他資源，可以拿消耗籌碼A
            if (card_Taken_No == -1 && coin_Limit[0] > 0 && player_Resource < PLAYER_RESOURCE_MAX && need_Coin_Taken[0] < 2 && (valid_Taken == 0) && coin_Taken_Counter < RESOURCE_TAKEN_MAX) {
                int imageResource = getResources().getIdentifier("@mipmap/coin_a", null, getPackageName());
                button_Hand[coin_Taken_Counter].setImageResource(imageResource);
                coin_Taken_Counter++;//紀錄可拿的消耗籌碼數量
                need_Coin_Taken[0]++;//紀錄消耗籌碼A的拿取數量
                coin_Limit[0]--;//減少可拿取的消耗籌碼A數量
                textView_coin[0].setText(String.valueOf(coin_Limit[0]));//更新介面上可拿取的消耗籌碼A數量

            } else {
                String msg = "0";
                if (card_Taken_No != -1)
                    msg = (String.format("由於玩家 %s 您已選擇拿取卡片，無法於本回合拿取籌碼", player[player_Pointer].getPlayer_Name()));
                else if (coin_Limit[0] <= 0)
                    msg = (String.format("由於該籌碼堆已無籌碼，玩家 %s 無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (player_Resource >= PLAYER_RESOURCE_MAX)
                    msg = (String.format("由於玩家 %s 可拿取的籌碼已達上限，無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (coin_Taken_Counter >= RESOURCE_TAKEN_MAX)
                    msg = (String.format("由於玩家 %s 本回合可拿取的籌碼已達上限，無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (need_Coin_Taken[0] >= 2)
                    msg = ("該籌碼堆僅能同時拿取 2 個");
                else if (valid_Taken == 1)
                    msg = ("您已拿取了 2 個相同的籌碼，於本回合無法再拿取該籌碼");
                else if (valid_Taken == 2)
                    msg = ("您不能拿取 2 個相同的籌碼");
                else if (valid_Taken == 3)
                    msg = ("該籌碼堆不足 4 個，無法再次拿取");
                //更新介面提示文字
                show_hint(msg);
                //textView_hint_message.setText(msg);

            }
        }
    }
    //取籌碼B
    public void on_button_coinB_Clicked(View ImageButton){
        if((player_Pointer == my_Seq)||current_mode.equals(single_mode)) {
            int player_Resource = ((player[player_Pointer].getTotal_Need_Point() + coin_Taken_Counter));//玩家手上的資源 + 預計拿取的消耗籌碼數量
            int valid_Taken = (new gameMethod()).is_Over_Once_Resource_Max(need_Coin_Taken[0], need_Coin_Taken[1], need_Coin_Taken[2], need_Coin_Taken[3], "B", coin_Limit[1]);//檢查玩家預計拿的籌碼是否正常

            //如果沒拿卡片，且籌碼堆還有消耗籌碼B，且玩家手上沒超出上限，且該籌碼沒拿超過2個，且拿了2個相同籌碼不能再拿其他資源，可以拿消耗籌碼B
            if (card_Taken_No == -1 && coin_Limit[1] > 0 && player_Resource < PLAYER_RESOURCE_MAX && need_Coin_Taken[1] < 2 && (valid_Taken == 0) && coin_Taken_Counter < RESOURCE_TAKEN_MAX) {
                int imageResource = getResources().getIdentifier("@mipmap/coin_b", null, getPackageName());
                button_Hand[coin_Taken_Counter].setImageResource(imageResource);
                coin_Taken_Counter++;//紀錄可拿的消耗籌碼數量
                need_Coin_Taken[1]++;//紀錄消耗籌碼B的拿取數量
                coin_Limit[1]--;//減少可拿取的消耗籌碼B數量
                textView_coin[1].setText(String.valueOf(coin_Limit[1]));//更新介面上可拿取的消耗籌碼B數量
            } else {
                String msg = "0";
                if (card_Taken_No != -1)
                    msg = (String.format("由於玩家 %s 您已選擇拿取卡片，無法於本回合拿取籌碼", player[player_Pointer].getPlayer_Name()));
                else if (coin_Limit[1] <= 0)
                    msg = (String.format("由於該籌碼堆已無籌碼，玩家 %s 無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (player_Resource >= PLAYER_RESOURCE_MAX)
                    msg = (String.format("由於玩家 %s 可拿取的籌碼已達上限，無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (coin_Taken_Counter >= RESOURCE_TAKEN_MAX)
                    msg = (String.format("由於玩家 %s 本回合可拿取的籌碼已達上限，無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (need_Coin_Taken[1] >= 2)
                    msg = ("該籌碼堆僅能同時拿取 2 個");
                else if (valid_Taken == 1)
                    msg = ("您已拿取了 2 個相同的籌碼，於本回合無法再拿取該籌碼");
                else if (valid_Taken == 2)
                    msg = ("您不能拿取 2 個相同的籌碼");
                else if (valid_Taken == 3)
                    msg = ("該籌碼堆不足 4 個，無法再次拿取");
                //更新介面提示文字
                show_hint(msg);
                //textView_hint_message.setText(msg);
            }
        }
    }
    //取籌碼C
    public void on_button_coinC_Clicked(View ImageButton){
        if((player_Pointer == my_Seq)||current_mode.equals(single_mode)) {
            int player_Resource = ((player[player_Pointer].getTotal_Need_Point() + coin_Taken_Counter));//玩家手上的資源 + 預計拿取的消耗籌碼數量
            int valid_Taken = (new gameMethod()).is_Over_Once_Resource_Max(need_Coin_Taken[0], need_Coin_Taken[1], need_Coin_Taken[2], need_Coin_Taken[3], "C", coin_Limit[2]);//檢查玩家預計拿的籌碼是否正常

            //如果沒拿卡片，且籌碼堆還有消耗籌碼C，且玩家手上沒超出上限，且該籌碼沒拿超過2個，且拿了2個相同籌碼不能再拿其他資源，可以拿消耗籌碼C
            if (card_Taken_No == -1 && coin_Limit[2] > 0 && player_Resource < PLAYER_RESOURCE_MAX && need_Coin_Taken[2] < 2 && (valid_Taken == 0) && coin_Taken_Counter < RESOURCE_TAKEN_MAX) {
                int imageResource = getResources().getIdentifier("@mipmap/coin_c", null, getPackageName());
                button_Hand[coin_Taken_Counter].setImageResource(imageResource);
                coin_Taken_Counter++;//紀錄可拿的消耗籌碼數量
                need_Coin_Taken[2]++;//紀錄消耗籌碼C的拿取數量
                coin_Limit[2]--;//減少可拿取的消耗籌碼C數量
                textView_coin[2].setText(String.valueOf(coin_Limit[2]));//更新介面上可拿取的消耗籌碼C數量
            } else {
                String msg = "0";
                if (card_Taken_No != -1)
                    msg = (String.format("由於玩家 %s 您已選擇拿取卡片，無法於本回合拿取籌碼", player[player_Pointer].getPlayer_Name()));
                else if (coin_Limit[2] <= 0)
                    msg = (String.format("由於該籌碼堆已無籌碼，玩家 %s 無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (player_Resource >= PLAYER_RESOURCE_MAX)
                    msg = (String.format("由於玩家 %s 可拿取的籌碼已達上限，無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (coin_Taken_Counter >= RESOURCE_TAKEN_MAX)
                    msg = (String.format("由於玩家 %s 本回合可拿取的籌碼已達上限，無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (need_Coin_Taken[2] >= 2)
                    msg = ("該籌碼堆僅能同時拿取 2 個");
                else if (valid_Taken == 1)
                    msg = ("您已拿取了 2 個相同的籌碼，於本回合無法再拿取該籌碼");
                else if (valid_Taken == 2)
                    msg = ("您不能拿取 2 個相同的籌碼");
                else if (valid_Taken == 3)
                    msg = ("該籌碼堆不足 4 個，無法再次拿取");
                //更新介面提示文字
                show_hint(msg);
                //textView_hint_message.setText(msg);
            }
        }
    }
    //取籌碼D
    public void on_button_coinD_Clicked(View ImageButton){
        if((player_Pointer == my_Seq)||current_mode.equals(single_mode)) {
            int player_Resource = ((player[player_Pointer].getTotal_Need_Point() + coin_Taken_Counter));//玩家手上的資源 + 預計拿取的消耗籌碼數量
            int valid_Taken = (new gameMethod()).is_Over_Once_Resource_Max(need_Coin_Taken[0], need_Coin_Taken[1], need_Coin_Taken[2], need_Coin_Taken[3], "D", coin_Limit[3]);//檢查玩家預計拿的籌碼是否正常

            //如果沒拿卡片，且籌碼堆還有消耗籌碼D，且玩家手上沒超出上限，且該籌碼沒拿超過2個，且拿了2個相同籌碼不能再拿其他資源，可以拿籌碼
            if (card_Taken_No == -1 && coin_Limit[3] > 0 && player_Resource < PLAYER_RESOURCE_MAX && need_Coin_Taken[3] < 2 && (valid_Taken == 0) && coin_Taken_Counter < RESOURCE_TAKEN_MAX) {
                int imageResource = getResources().getIdentifier("@mipmap/coin_d", null, getPackageName());
                button_Hand[coin_Taken_Counter].setImageResource(imageResource);
                coin_Taken_Counter++;//紀錄可拿的消耗籌碼數量
                need_Coin_Taken[3]++;//紀錄消耗籌碼D的拿取數量
                coin_Limit[3]--;//減少可拿取的消耗籌碼D數量
                textView_coin[3].setText(String.valueOf(coin_Limit[3]));//更新介面上可拿取的消耗籌碼B數量
            } else {
                String msg = "0";
                if (card_Taken_No != -1)
                    msg = (String.format("由於玩家 %s 您已選擇拿取卡片，無法於本回合拿取籌碼", player[player_Pointer].getPlayer_Name()));
                else if (coin_Limit[3] <= 0)
                    msg = (String.format("由於該籌碼堆已無籌碼，玩家 %s 無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (player_Resource >= PLAYER_RESOURCE_MAX)
                    msg = (String.format("由於玩家 %s 可拿取的籌碼已達上限，無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (coin_Taken_Counter >= RESOURCE_TAKEN_MAX)
                    msg = (String.format("由於玩家 %s 本回合可拿取的籌碼已達上限，無法於本回合拿取該籌碼", player[player_Pointer].getPlayer_Name()));
                else if (need_Coin_Taken[3] >= 2)
                    msg = ("該籌碼堆僅能同時拿取 2 個");
                else if (valid_Taken == 1)
                    msg = ("您已拿取了 2 個相同的籌碼，於本回合無法再拿取該籌碼");
                else if (valid_Taken == 2)
                    msg = ("您不能拿取 2 個相同的籌碼");
                else if (valid_Taken == 3)
                    msg = ("該籌碼堆不足 4 個，無法再次拿取");
                //更新介面提示文字
                show_hint(msg);
                //textView_hint_message.setText(msg);

            }
        }
    }

    // ok button
    public void on_test_ButtonClicked(View button) {
        if((player_Pointer == my_Seq)&&current_mode.equals(multi_mode)){
            send_Msg = "nothing ha ha";
            int token[]=new int[4];
            //如果是拿籌碼當作回合動作
            if (coin_Taken_Counter > 0) {
                //將拿取的籌碼數紀錄，數量為 (原本手上數量 + 本回合拿取數量)
                player[player_Pointer].setNeed_PointA(player[player_Pointer].getNeed_PointA() + need_Coin_Taken[0]);
                player[player_Pointer].setNeed_PointB(player[player_Pointer].getNeed_PointB() + need_Coin_Taken[1]);
                player[player_Pointer].setNeed_PointC(player[player_Pointer].getNeed_PointC() + need_Coin_Taken[2]);
                player[player_Pointer].setNeed_PointD(player[player_Pointer].getNeed_PointD() + need_Coin_Taken[3]);
                token =new int[]{player[player_Pointer].getNeed_PointA(),player[player_Pointer].getNeed_PointB(),player[player_Pointer].getNeed_PointC(),player[player_Pointer].getNeed_PointD()};
                send_Msg="token ";
            }
            else if (card_Taken_No != -1) {
                //如果是拿卡片當作回合動作
                send_Msg =String.format("card %s ",card_Taken_No);
                //更新玩家手中的消耗籌碼
                //如果手中的永久籌碼數量少於要消耗掉的籌碼，減少玩家手中的消耗籌碼，並將消耗籌碼歸還且更新介面
                if (player[player_Pointer].getPermanent_PointA() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA()) {
                    player[player_Pointer].setNeed_PointA(player[player_Pointer].getNeed_PointA() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA() - player[player_Pointer].getPermanent_PointA()));
                    //將玩家歸還的籌碼A加回籌碼堆
                    coin_Limit[0] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA() - player[player_Pointer].getPermanent_PointA());
                    textView_coin[0].setText(String.valueOf(coin_Limit[0]));
                }
                if (player[player_Pointer].getPermanent_PointB() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB()) {
                    player[player_Pointer].setNeed_PointB(player[player_Pointer].getNeed_PointB() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB() - player[player_Pointer].getPermanent_PointB()));
                    //將玩家歸還的籌碼B加回籌碼堆
                    coin_Limit[1] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB() - player[player_Pointer].getPermanent_PointB());
                    textView_coin[1].setText(String.valueOf(coin_Limit[1]));

                }
                if (player[player_Pointer].getPermanent_PointC() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC()) {
                    player[player_Pointer].setNeed_PointC(player[player_Pointer].getNeed_PointC() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC() - player[player_Pointer].getPermanent_PointC()));
                    //將玩家歸還的籌碼C加回籌碼堆
                    coin_Limit[2] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC() - player[player_Pointer].getPermanent_PointC());
                    textView_coin[2].setText(String.valueOf(coin_Limit[2]));
                }
                if (player[player_Pointer].getPermanent_PointD() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD()) {
                    player[player_Pointer].setNeed_PointD(player[player_Pointer].getNeed_PointD() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD() - player[player_Pointer].getPermanent_PointD()));
                    //將玩家歸還的籌碼D加回籌碼堆
                    coin_Limit[3] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD() - player[player_Pointer].getPermanent_PointD());
                    textView_coin[3].setText(String.valueOf(coin_Limit[3]));
                }

                //更新獲取的永久籌碼，以及分數
                player[player_Pointer].setPermanent_PointA(player[player_Pointer].getPermanent_PointA() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointA());
                player[player_Pointer].setPermanent_PointB(player[player_Pointer].getPermanent_PointB() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointB());
                player[player_Pointer].setPermanent_PointC(player[player_Pointer].getPermanent_PointC() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointC());
                player[player_Pointer].setPermanent_PointD(player[player_Pointer].getPermanent_PointD() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointD());
                player[player_Pointer].setPlayer_Score(player[player_Pointer].getPlayer_Score() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getCard_Score());
            }


            //更新介面上玩家資源的消耗籌碼數量
            String update_Player_ResourceA = "<b><tt>" + player[player_Pointer].getResourceA() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointA() + ")";
            String update_Player_ResourceB = "<b><tt>" + player[player_Pointer].getResourceB() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointB() + ")";
            String update_Player_ResourceC = "<b><tt>" + player[player_Pointer].getResourceC() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointC() + ")";
            String update_Player_ResourceD = "<b><tt>" + player[player_Pointer].getResourceD() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointD() + ")";

            textView_Player_Total_Res[player_Pointer].setText(String.valueOf(player[player_Pointer].getTotal_Need_Point()));
            textView_Player_Score[player_Pointer].setText(String.format("%s/%s", player[player_Pointer].getPlayer_Score(), WIN_SCORE));
            textView_Player_A_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceA));
            textView_Player_B_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceB));
            textView_Player_C_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceC));
            textView_Player_D_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceD));

            //將被拿取位置的卡片補上新抽的卡片
            if (card_Taken_No != -1) {
                //從洗好的牌堆中，將第 deck_Index 中的卡片圖示拿出
                int imageResource = getResources().getIdentifier((myDeck.getCards().get(deck_Index)).getCard_Picture(), null, getPackageName());
                flip_Card[card_Taken_No].setImageResource(imageResource);
                //將卡片數值提示文字設定在卡片被拿取位置
                textView_Flip_Card[card_Taken_No].setText((myDeck.getCards().get(deck_Index)).getFood_Name());
                //卡片被拿取位置設定從牌堆中拿取的新卡片
                deck_Pointer[card_Taken_No] = deck_Index;
                //減少牌堆總數並更新介面
                deck_Counter_Value--;
                deck_Counter.setText("x" + String.valueOf(deck_Counter_Value));
                //deck_Index指向牌堆中的下一張卡片
                deck_Index++;

                // 將介面上，手中拿取的卡片圖案更新為空白
                int imageResource2 = getResources().getIdentifier("@mipmap/c_null", null, getPackageName());
                button_Chosen_Card.setImageResource(imageResource2);
            }



            card_Taken_No = -1;//將拿去的卡片初始化

            // 將介面上，手中拿去的消耗籌碼圖案更新為空白
            int imageResource = getResources().getIdentifier("@mipmap/coin_null", null, getPackageName());
            for (int i = 0; i < 3; i++) {
                button_Hand[i].setImageResource(imageResource);
            }

            //將本回合被拿取的籌碼堆更新數量
            for (int i = 0; i < 4; i++) {
                if (need_Coin_Taken[i] > 0) {
                    //coin_Limit[i] -=need_Coin_Taken[i];
                    textView_coin[i].setText(String.valueOf(coin_Limit[i]));

                }
                if(coin_Taken_Counter>0)
                    send_Msg+= coin_Limit[i]+" ";
            }
            if(coin_Taken_Counter>0)
                send_Msg += String.format("%s %s %s %s ", token[0], token[1], token[2], token[3]);

            coin_Taken_Counter = 0;//拿取得消耗籌碼總數歸零

            //將本回合拿取的消耗籌碼數歸零
            for (int i = 0; i < 4; i++) {
                need_Coin_Taken[i] = 0;
            }

            //檢查玩家勝利
            for (int i = 0; i < PLAYER_NUM; i++)
                if ((new gameMethod()).is_Win(player[i].getPlayer_Score())) {
                    Intent intent = new Intent(this, gameOverActivity.class);
                    intent.putExtra("winner", player[i].getPlayer_Name());
                    this.finish();
                    startActivity(intent);
                }


            //player_Pointer = (++player_Pointer) % PLAYER_NUM;//換下一位玩家操作
            //System.out.println("turn to: player " + String.valueOf(player_Pointer));
            //for (int i = 0; i < 4; i++)
            //    textView_Player_Name[i].setBackgroundColor(Color.parseColor("#E6E6E6"));
            //textView_Player_Name[player_Pointer].setBackgroundColor(Color.RED);
            //show_hint(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
            //textView_hint_message.setText(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));




            //切換開關在Client模式時
            if (tcpClient == null)return;
            if (send_Msg.length() == 0 || !tcpClient.getStatus()) return;
            exec.execute(() -> tcpClient.send(send_Msg));

        }
        else {


            //如果是拿籌碼當作回合動作
            if (coin_Taken_Counter > 0) {
                //將拿取的籌碼數紀錄，數量為 (原本手上數量 + 本回合拿取數量)
                player[player_Pointer].setNeed_PointA(player[player_Pointer].getNeed_PointA() + need_Coin_Taken[0]);
                player[player_Pointer].setNeed_PointB(player[player_Pointer].getNeed_PointB() + need_Coin_Taken[1]);
                player[player_Pointer].setNeed_PointC(player[player_Pointer].getNeed_PointC() + need_Coin_Taken[2]);
                player[player_Pointer].setNeed_PointD(player[player_Pointer].getNeed_PointD() + need_Coin_Taken[3]);
            } else if (card_Taken_No != -1) {
                //如果是拿卡片當作回合動作

                //更新玩家手中的消耗籌碼
                //如果手中的永久籌碼數量少於要消耗掉的籌碼，減少玩家手中的消耗籌碼，並將消耗籌碼歸還且更新介面
                if (player[player_Pointer].getPermanent_PointA() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA()) {
                    player[player_Pointer].setNeed_PointA(player[player_Pointer].getNeed_PointA() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA() - player[player_Pointer].getPermanent_PointA()));
                    //將玩家歸還的籌碼A加回籌碼堆
                    coin_Limit[0] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA() - player[player_Pointer].getPermanent_PointA());
                    textView_coin[0].setText(String.valueOf(coin_Limit[0]));
                }
                if (player[player_Pointer].getPermanent_PointB() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB()) {
                    player[player_Pointer].setNeed_PointB(player[player_Pointer].getNeed_PointB() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB() - player[player_Pointer].getPermanent_PointB()));
                    //將玩家歸還的籌碼B加回籌碼堆
                    coin_Limit[1] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB() - player[player_Pointer].getPermanent_PointB());
                    textView_coin[1].setText(String.valueOf(coin_Limit[1]));

                }
                if (player[player_Pointer].getPermanent_PointC() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC()) {
                    player[player_Pointer].setNeed_PointC(player[player_Pointer].getNeed_PointC() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC() - player[player_Pointer].getPermanent_PointC()));
                    //將玩家歸還的籌碼C加回籌碼堆
                    coin_Limit[2] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC() - player[player_Pointer].getPermanent_PointC());
                    textView_coin[2].setText(String.valueOf(coin_Limit[2]));
                }
                if (player[player_Pointer].getPermanent_PointD() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD()) {
                    player[player_Pointer].setNeed_PointD(player[player_Pointer].getNeed_PointD() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD() - player[player_Pointer].getPermanent_PointD()));
                    //將玩家歸還的籌碼D加回籌碼堆
                    coin_Limit[3] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD() - player[player_Pointer].getPermanent_PointD());
                    textView_coin[3].setText(String.valueOf(coin_Limit[3]));
                }

                //更新獲取的永久籌碼，以及分數
                player[player_Pointer].setPermanent_PointA(player[player_Pointer].getPermanent_PointA() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointA());
                player[player_Pointer].setPermanent_PointB(player[player_Pointer].getPermanent_PointB() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointB());
                player[player_Pointer].setPermanent_PointC(player[player_Pointer].getPermanent_PointC() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointC());
                player[player_Pointer].setPermanent_PointD(player[player_Pointer].getPermanent_PointD() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointD());
                player[player_Pointer].setPlayer_Score(player[player_Pointer].getPlayer_Score() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getCard_Score());
            }


            //更新介面上玩家資源的消耗籌碼數量
            String update_Player_ResourceA = "<b><tt>" + player[player_Pointer].getResourceA() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointA() + ")";
            String update_Player_ResourceB = "<b><tt>" + player[player_Pointer].getResourceB() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointB() + ")";
            String update_Player_ResourceC = "<b><tt>" + player[player_Pointer].getResourceC() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointC() + ")";
            String update_Player_ResourceD = "<b><tt>" + player[player_Pointer].getResourceD() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointD() + ")";

            textView_Player_Total_Res[player_Pointer].setText(String.valueOf(player[player_Pointer].getTotal_Need_Point()));
            textView_Player_Score[player_Pointer].setText(String.format("%s/%s", player[player_Pointer].getPlayer_Score(), WIN_SCORE));
            textView_Player_A_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceA));
            textView_Player_B_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceB));
            textView_Player_C_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceC));
            textView_Player_D_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceD));

            //將被拿取位置的卡片補上新抽的卡片
            if (card_Taken_No != -1) {
                //從洗好的牌堆中，將第 deck_Index 中的卡片圖示拿出
                int imageResource = getResources().getIdentifier((myDeck.getCards().get(deck_Index)).getCard_Picture(), null, getPackageName());
                flip_Card[card_Taken_No].setImageResource(imageResource);
                //將卡片數值提示文字設定在卡片被拿取位置
                textView_Flip_Card[card_Taken_No].setText((myDeck.getCards().get(deck_Index)).getFood_Name());
                //卡片被拿取位置設定從牌堆中拿取的新卡片
                deck_Pointer[card_Taken_No] = deck_Index;
                //減少牌堆總數並更新介面
                deck_Counter_Value--;
                deck_Counter.setText("x" + String.valueOf(deck_Counter_Value));
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
            for (int i = 0; i < 3; i++) {
                button_Hand[i].setImageResource(imageResource);
            }

            //將本回合被拿取的籌碼堆更新數量
            for (int i = 0; i < 4; i++) {
                if (need_Coin_Taken[i] > 0) {
                    //coin_Limit[i] -=need_Coin_Taken[i];
                    textView_coin[i].setText(String.valueOf(coin_Limit[i]));
                }
            }

            //將本回合拿取的消耗籌碼數歸零
            for (int i = 0; i < 4; i++) {
                need_Coin_Taken[i] = 0;
            }

            //檢查玩家勝利
            for (int i = 0; i < PLAYER_NUM; i++)
                //if ((new gameMethod()).is_Win(player[i].getPlayer_Score())) {
                if (player[i].getPlayer_Score()>=WIN_SCORE) {
                    Intent intent = new Intent(this, gameOverActivity.class);
                    intent.putExtra("winner", player[i].getPlayer_Name());
                    this.finish();
                    startActivity(intent);
                }


            player_Pointer = (++player_Pointer) % PLAYER_NUM;//換下一位玩家操作
            System.out.println("turn to: player " + String.valueOf(player_Pointer));
            for (int i = 0; i < 4; i++)
                textView_Player_Name[i].setBackgroundColor(Color.parseColor("#E6E6E6"));
            textView_Player_Name[player_Pointer].setBackgroundColor(Color.RED);
            show_hint(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
            //textView_hint_message.setText(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
        }
    }


/*
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            deck_Counter.setText((String)msg.obj);
        };
    };
*/

    public void whoWin(){
        //檢查玩家勝利
        for (int i = 0; i < PLAYER_NUM; i++)
            if ((new gameMethod()).is_Win(player[i].getPlayer_Score())) {
                Intent intent = new Intent(this, gameOverActivity.class);
                intent.putExtra("winner", player[i].getPlayer_Name());
                this.finish();
                startActivity(intent);
            }
    }

    //String status_type[] = {"Welcome!","player"};
private class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String hint_msg="";
        String mAction = intent.getAction();
        assert mAction != null;
        /**接收來自server回傳之訊息*/
        switch (mAction) {
            case TCPClient.RECEIVE_ACTION:
                String msg = intent.getStringExtra(TCPClient.RECEIVE_STRING);
                String[] tokens = msg.split(" ");
                Log.d("token count", String.valueOf(tokens.length));
                for (String t:tokens) {
                    Log.d("token",t);
                }
                if(tokens[0].equals("Welcome!")){
                    //exec.execute(() -> tcpClient.send(tcpClient.Name));
                    my_Seq = Integer.parseInt(tokens[1]);
                    Log.d("do",tokens[0]);
                    exec.execute(() -> tcpClient.send("player"));
                }
                else if(tokens[0].equals("player")){
                    current_player_counter = Integer.parseInt(tokens[1]);
                    show_hint("有人加入 ("+current_player_counter+"/"+PLAYER_NUM+")");
                    Log.d("do",tokens[0]);
                    for(int i = 0;i<tokens.length-2;i++){
                        player[i].setPlayer_Name(tokens[i+2]);
                        player[i].setPlayer_seq_num(i);

                        textView_Player_Name[i].setText(player[i].getPlayer_Name());
                    }
                    if(current_player_counter == PLAYER_NUM){
                        Log.d("do","shuffle");
                        exec.execute(() -> tcpClient.send("shuffle"));
                    }

                }
                else if(tokens[0].equals("shuffle")){
                    show_hint("Game Start!");
                    GameStart = true;
                    int[] random_Deck = new int[Deck.deck_Num];
                    for(int i = 0;i<random_Deck.length;i++){
                        random_Deck[i] = Integer.parseInt(tokens[i+1]);
                    }
                    myDeck.shuffle_Deck(random_Deck);

                    for(int i = 0 ;i< FLIP_CARD_NUM ;i++){
                        deck_Counter_Value--;
                    }

                    //牌面初始化
                    for(int i = 0 ; i< 4 ;i++){
                        //從洗好的牌堆中，將第 0~3 中的卡片圖示拿出
                        int imageResource = getResources().getIdentifier((myDeck.getCards().get(i)).getCard_Picture(), null, getPackageName());
                        flip_Card[i].setImageResource(imageResource);
                        //將卡片數值提示文字設定在卡片可拿取位置
                        textView_Flip_Card[i].setText((myDeck.getCards().get(i)).getFood_Name());
                        //卡片可拿取位置設定從牌堆中拿取的新卡片
                        deck_Pointer[i] = i;
                        //deck_Index指向牌堆中的下一張卡片
                        deck_Index++;
                    }
                    deck_Counter.setText("x"+String.valueOf(deck_Counter_Value));
                    exec.execute(() -> tcpClient.send("whoFirst"));
                }
                else if(tokens[0].equals("whoFirst")){
                    show_hint(player[Integer.parseInt(tokens[1])].getPlayer_Name() + " first!");
                    player_Pointer = Integer.parseInt(tokens[1]);

                    textView_Player_Name[player_Pointer].setBackgroundColor(Color.RED);//初始玩家標記為紅色

                    hint_msg = String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name());
                    //show_hint(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
                    Log.d("whoFirst",tokens[1]+hint_msg);
                }
                else if(tokens[0].equals("token")){
                    for(int i = 0;i<RESOURCE_NUM;i++){
                        coin_Limit[i] = Integer.parseInt(tokens[i+1]);//可拿取的消耗籌碼數量
                        textView_coin[i].setText(String.valueOf(coin_Limit[i]));//更新介面上可拿取的消耗籌碼數量
                    }
                    //將拿取的籌碼數紀錄，數量為 (原本手上數量 + 本回合拿取數量)
                    player[player_Pointer].setNeed_PointA(Integer.parseInt(tokens[5]));
                    player[player_Pointer].setNeed_PointB(Integer.parseInt(tokens[6]));
                    player[player_Pointer].setNeed_PointC(Integer.parseInt(tokens[7]));
                    player[player_Pointer].setNeed_PointD(Integer.parseInt(tokens[8]));

                    //更新介面上玩家資源的消耗籌碼數量
                    String update_Player_ResourceA = "<b><tt>" + player[player_Pointer].getResourceA() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointA() + ")";
                    String update_Player_ResourceB = "<b><tt>" + player[player_Pointer].getResourceB() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointB() + ")";
                    String update_Player_ResourceC = "<b><tt>" + player[player_Pointer].getResourceC() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointC() + ")";
                    String update_Player_ResourceD = "<b><tt>" + player[player_Pointer].getResourceD() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointD() + ")";

                    textView_Player_Total_Res[player_Pointer].setText(String.valueOf(player[player_Pointer].getTotal_Need_Point()));
                    textView_Player_Score[player_Pointer].setText(String.format("%s/%s", player[player_Pointer].getPlayer_Score(), WIN_SCORE));
                    textView_Player_A_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceA));
                    textView_Player_B_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceB));
                    textView_Player_C_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceC));
                    textView_Player_D_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceD));

                    //將本回合被拿取的籌碼堆更新數量
                    for (int i = 0; i < 4; i++) {
                        if (need_Coin_Taken[i] > 0) {
                            //coin_Limit[i] -=need_Coin_Taken[i];
                            textView_coin[i].setText(String.valueOf(coin_Limit[i]));
                        }
                    }



                    player_Pointer = (++player_Pointer) % PLAYER_NUM;//換下一位玩家操作
                    System.out.println("turn to: player " + String.valueOf(player_Pointer));
                    for (int i = 0; i < 4; i++)
                        textView_Player_Name[i].setBackgroundColor(Color.parseColor("#E6E6E6"));
                    textView_Player_Name[player_Pointer].setBackgroundColor(Color.RED);
                    show_hint(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
                    //textView_hint_message.setText(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
                    hint_msg = String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name());
                }
                else if(tokens[0].equals("card")){
                    card_Taken_No = Integer.parseInt(tokens[1]);
                    //更新玩家手中的消耗籌碼
                    //如果手中的永久籌碼數量少於要消耗掉的籌碼，減少玩家手中的消耗籌碼，並將消耗籌碼歸還且更新介面
                    if (player[player_Pointer].getPermanent_PointA() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA()) {
                        player[player_Pointer].setNeed_PointA(player[player_Pointer].getNeed_PointA() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA() - player[player_Pointer].getPermanent_PointA()));
                        //將玩家歸還的籌碼A加回籌碼堆
                        coin_Limit[0] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointA() - player[player_Pointer].getPermanent_PointA());
                        textView_coin[0].setText(String.valueOf(coin_Limit[0]));
                    }
                    if (player[player_Pointer].getPermanent_PointB() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB()) {
                        player[player_Pointer].setNeed_PointB(player[player_Pointer].getNeed_PointB() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB() - player[player_Pointer].getPermanent_PointB()));
                        //將玩家歸還的籌碼B加回籌碼堆
                        coin_Limit[1] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointB() - player[player_Pointer].getPermanent_PointB());
                        textView_coin[1].setText(String.valueOf(coin_Limit[1]));

                    }
                    if (player[player_Pointer].getPermanent_PointC() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC()) {
                        player[player_Pointer].setNeed_PointC(player[player_Pointer].getNeed_PointC() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC() - player[player_Pointer].getPermanent_PointC()));
                        //將玩家歸還的籌碼C加回籌碼堆
                        coin_Limit[2] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointC() - player[player_Pointer].getPermanent_PointC());
                        textView_coin[2].setText(String.valueOf(coin_Limit[2]));
                    }
                    if (player[player_Pointer].getPermanent_PointD() < (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD()) {
                        player[player_Pointer].setNeed_PointD(player[player_Pointer].getNeed_PointD() - ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD() - player[player_Pointer].getPermanent_PointD()));
                        //將玩家歸還的籌碼D加回籌碼堆
                        coin_Limit[3] += ((myDeck.getCards().get(deck_Pointer[card_Taken_No])).getNeed_PointD() - player[player_Pointer].getPermanent_PointD());
                        textView_coin[3].setText(String.valueOf(coin_Limit[3]));
                    }

                    //更新獲取的永久籌碼，以及分數
                    player[player_Pointer].setPermanent_PointA(player[player_Pointer].getPermanent_PointA() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointA());
                    player[player_Pointer].setPermanent_PointB(player[player_Pointer].getPermanent_PointB() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointB());
                    player[player_Pointer].setPermanent_PointC(player[player_Pointer].getPermanent_PointC() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointC());
                    player[player_Pointer].setPermanent_PointD(player[player_Pointer].getPermanent_PointD() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getPermanent_PointD());
                    player[player_Pointer].setPlayer_Score(player[player_Pointer].getPlayer_Score() + (myDeck.getCards().get(deck_Pointer[card_Taken_No])).getCard_Score());



                    //更新介面上玩家資源的消耗籌碼數量
                    String update_Player_ResourceA = "<b><tt>" + player[player_Pointer].getResourceA() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointA() + ")";
                    String update_Player_ResourceB = "<b><tt>" + player[player_Pointer].getResourceB() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointB() + ")";
                    String update_Player_ResourceC = "<b><tt>" + player[player_Pointer].getResourceC() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointC() + ")";
                    String update_Player_ResourceD = "<b><tt>" + player[player_Pointer].getResourceD() + "</tt></b>" + "(+" + player[player_Pointer].getPermanent_PointD() + ")";

                    textView_Player_Total_Res[player_Pointer].setText(String.valueOf(player[player_Pointer].getTotal_Need_Point()));
                    textView_Player_Score[player_Pointer].setText(String.format("%s/%s", player[player_Pointer].getPlayer_Score(), WIN_SCORE));
                    textView_Player_A_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceA));
                    textView_Player_B_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceB));
                    textView_Player_C_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceC));
                    textView_Player_D_resource[player_Pointer].setText(Html.fromHtml(update_Player_ResourceD));

                    //將被拿取位置的卡片補上新抽的卡片
                    if (card_Taken_No != -1) {
                        //從洗好的牌堆中，將第 deck_Index 中的卡片圖示拿出
                        int imageResource = getResources().getIdentifier((myDeck.getCards().get(deck_Index)).getCard_Picture(), null, getPackageName());
                        flip_Card[card_Taken_No].setImageResource(imageResource);
                        //將卡片數值提示文字設定在卡片被拿取位置
                        textView_Flip_Card[card_Taken_No].setText((myDeck.getCards().get(deck_Index)).getFood_Name());
                        //卡片被拿取位置設定從牌堆中拿取的新卡片
                        deck_Pointer[card_Taken_No] = deck_Index;
                        //減少牌堆總數並更新介面
                        deck_Counter_Value--;
                        deck_Counter.setText("x" + String.valueOf(deck_Counter_Value));
                        //deck_Index指向牌堆中的下一張卡片
                        deck_Index++;

                        // 將介面上，手中拿取的卡片圖案更新為空白
                        int imageResource2 = getResources().getIdentifier("@mipmap/c_null", null, getPackageName());
                        button_Chosen_Card.setImageResource(imageResource2);
                    }



                    card_Taken_No = -1;//將拿去的卡片初始化

                    // 將介面上，手中拿去的消耗籌碼圖案更新為空白
                    int imageResource = getResources().getIdentifier("@mipmap/coin_null", null, getPackageName());
                    for (int i = 0; i < 3; i++) {
                        button_Hand[i].setImageResource(imageResource);
                    }

                    //將本回合被拿取的籌碼堆更新數量
                    for (int i = 0; i < 4; i++) {
                        if (need_Coin_Taken[i] > 0) {
                            //coin_Limit[i] -=need_Coin_Taken[i];
                            textView_coin[i].setText(String.valueOf(coin_Limit[i]));
                        }
                    }
                    coin_Taken_Counter = 0;//拿取得消耗籌碼總數歸零

                    //將本回合拿取的消耗籌碼數歸零
                    for (int i = 0; i < 4; i++) {
                        need_Coin_Taken[i] = 0;
                    }

                    //檢查玩家勝利
                    for (int i = 0; i < PLAYER_NUM; i++)
                        if ((new gameMethod()).is_Win(player[i].getPlayer_Score())) {
                            whoWin();
                        }


                    player_Pointer = (++player_Pointer) % PLAYER_NUM;//換下一位玩家操作
                    System.out.println("turn to: player " + String.valueOf(player_Pointer));
                    for (int i = 0; i < 4; i++)
                        textView_Player_Name[i].setBackgroundColor(Color.parseColor("#E6E6E6"));
                    textView_Player_Name[player_Pointer].setBackgroundColor(Color.RED);
                    show_hint(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
                    textView_hint_message.setText(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));

                }
                //else{
                //    player_Pointer = (++player_Pointer) % PLAYER_NUM;//換下一位玩家操作
                //    System.out.println("turn to: player " + String.valueOf(player_Pointer));
                //    for (int i = 0; i < 4; i++)
                //        textView_Player_Name[i].setBackgroundColor(Color.parseColor("#E6E6E6"));
                //    textView_Player_Name[player_Pointer].setBackgroundColor(Color.RED);
                //    show_hint(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
                //    textView_hint_message.setText(String.format("玩家 %s 的回合", player[player_Pointer].getPlayer_Name()));
                //}
                //byte[] bytes = intent.getByteArrayExtra(TCPClient.RECEIVE_BYTES);
                if((player_Pointer == my_Seq) && GameStart){
                    test_Button.setEnabled(true);
                    test_Button.setVisibility(View.VISIBLE);
                }
                else{
                    test_Button.setEnabled(false);
                    test_Button.setVisibility(View.INVISIBLE);
                }

                stringBuffer.append("收到： ").append(msg).append("\n");

                //textView_hint_message.setText(stringBuffer);
                textView_hint_message.setText(hint_msg);

                break;

        }
    }
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消監聽廣播
        if(current_mode.equals(multi_mode)){
            unregisterReceiver(myBroadcast);
            if (tcpClient != null && tcpClient.getStatus()) {
                tcpClient.destroy();
                tcpClient.closeClient();

            }
        }

    }

}
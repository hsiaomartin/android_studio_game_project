package com.example.myapplication;

import androidx.annotation.Nullable;

public class gameMethod {
    private static int WIN_SCORE = 8; //遊戲勝利分數
    private int coinA_Amount = 7; //籌碼A的數量
    private int coinB_Amount = 7; //籌碼B的數量
    private int coinC_Amount = 7; //籌碼C的數量
    private int coinD_Amount = 7; //籌碼D的數量
    static private int hand_Coin_Limit = 10; //手上的消耗籌碼上限
    private int once_Resource_Max = 2; //一回合可拿取的同種消耗籌碼最大上限
    public boolean is_Win(int player_Score) {
        if(player_Score>=WIN_SCORE)
            return true;
        else
            return false;
    }

    public static int getWIN_SCORE() {
        return WIN_SCORE;
    }

    public int getCoinA_Amount() {
        return coinA_Amount;
    }

    public int getCoinB_Amount() {
        return coinB_Amount;
    }

    public int getCoinC_Amount() {
        return coinC_Amount;
    }

    public int getCoinD_Amount() {
        return coinD_Amount;
    }


    static public int getHand_Coin_Limit() {
        return hand_Coin_Limit;
    }

    //檢查拿取消耗籌碼時，是否違規
    //0:任意拿取
    //1:不能再拿取籌碼
    //2:不能再拿取相同籌碼
    public int is_Over_Once_Resource_Max(int coinA,int coinB,int coinC,int coinD,String next_Coin){
        char next_token = next_Coin.charAt(0);//下一個選取的籌碼

        // 如果已經拿取了2個消耗籌碼
        if ( (coinA + coinB + coinC + coinD) == once_Resource_Max ){
            if( (coinA>=once_Resource_Max) || (coinB>=once_Resource_Max) || (coinC>=once_Resource_Max) || (coinD>=once_Resource_Max) )
                return 1;
            else if( coinA==0 && coinB==0 && coinC==1 && coinD==1 && (next_token == 'A' ||next_token == 'B' )){ //0011 只能再拿A或B
                return 0;
            }
            else if( coinA==0 && coinB==1 && coinC==0 && coinD==1 && (next_token == 'A' ||next_token == 'C' )){ //0101 只能再拿A或C
                return 0;
            }
            else if( coinA==1 && coinB==0 && coinC==0 && coinD==1 && (next_token == 'B' ||next_token == 'C' )){ //1001 只能再拿B或C
                return 0;
            }
            else if( coinA==0 && coinB==1 && coinC==1 && coinD==0 && (next_token == 'A' ||next_token == 'D' )){ //0110 只能再拿A或D
                return 0;
            }
            else if( coinA==1 && coinB==0 && coinC==1 && coinD==0 && (next_token == 'B' ||next_token == 'D' )){ //1010 只能再拿B或D
                return 0;
            }
            else if( coinA==1 && coinB==1 && coinC==0 && coinD==0 && (next_token == 'C' ||next_token == 'D' )){ //1100 只能再拿C或D
                return 0;
            }
            else return 2;
        }
        else return 0;

    }
}

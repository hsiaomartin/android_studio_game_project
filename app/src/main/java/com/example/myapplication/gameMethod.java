package com.example.myapplication;

import androidx.annotation.Nullable;

public class gameMethod {
    private int WIN_SCORE = 2;
    private int coinA_Amount = 7;
    private int coinB_Amount = 7;
    private int coinC_Amount = 7;
    private int coinD_Amount = 7;
    public boolean is_Win(int player_Score) {
        if(player_Score>=WIN_SCORE)
            return true;
        else
            return false;
    }

    public int getWIN_SCORE() {
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

    public void setCoinA_Amount(int coinA_Amount) {
        this.coinA_Amount = coinA_Amount;
    }

    public void setCoinB_Amount(int coinB_Amount) {
        this.coinB_Amount = coinB_Amount;
    }

    public void setCoinC_Amount(int coinC_Amount) {
        this.coinC_Amount = coinC_Amount;
    }

    public void setCoinD_Amount(int coinD_Amount) {
        this.coinD_Amount = coinD_Amount;
    }

}

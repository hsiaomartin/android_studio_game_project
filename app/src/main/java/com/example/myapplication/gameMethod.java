package com.example.myapplication;

import androidx.annotation.Nullable;

public class gameMethod {
    private short WIN_SCORE = 8;
    public boolean is_Win(short player_Score) {
        if(player_Score>=WIN_SCORE)
            return true;
        else
            return false;
    }

    public short getWIN_SCORE() {
        return WIN_SCORE;
    }
}

package com.example.myapplication;

import androidx.annotation.Nullable;

public class gameMethod {
    private int WIN_SCORE = 8;
    public boolean is_Win(int player_Score) {
        if(player_Score>=WIN_SCORE)
            return true;
        else
            return false;
    }

    public int getWIN_SCORE() {
        return WIN_SCORE;
    }
}

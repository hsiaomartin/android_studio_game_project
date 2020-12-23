package com.example.myapplication;

public class Player {
    private short player_Score = 0;
    private short permanent_PointA = 0;
    private short permanent_PointB = 0;
    private short permanent_PointC = 0;
    private short permanent_PointD = 0;
    private short need_PointA = 0;
    private short need_PointB = 0;
    private short need_PointC = 0;
    private short need_PointD = 0 ;
    private short hand_limit = 10;
    private String player_Name;
    public Player(String name){
        player_Name = name;
    }
    public short getPlayer_Score() {
        return player_Score;
    }

    public short getPermanent_PointA() {
        return permanent_PointA;
    }

    public short getPermanent_PointB() {
        return permanent_PointB;
    }

    public short getPermanent_PointC() {
        return permanent_PointC;
    }

    public short getPermanent_PointD() {
        return permanent_PointD;
    }

    public short getNeed_PointA() {
        return need_PointA;
    }

    public short getNeed_PointB() {
        return need_PointB;
    }

    public short getNeed_PointC() {
        return need_PointC;
    }

    public short getNeed_PointD() {
        return need_PointD;
    }

    public String getPlayer_Name() {
        return player_Name;
    }

    public void setNeed_PointA(short need_PointA) {
        this.need_PointA = need_PointA;
    }

    public void setNeed_PointB(short need_PointB) {
        this.need_PointB = need_PointB;
    }

    public void setNeed_PointC(short need_PointC) {
        this.need_PointC = need_PointC;
    }

    public void setNeed_PointD(short need_PointD) {
        this.need_PointD = need_PointD;
    }

    public void setPermanent_PointA(short permanent_PointA) {
        this.permanent_PointA = permanent_PointA;
    }

    public void setPermanent_PointB(short permanent_PointB) {
        this.permanent_PointB = permanent_PointB;
    }

    public void setPermanent_PointC(short permanent_PointC) {
        this.permanent_PointC = permanent_PointC;
    }

    public void setPermanent_PointD(short permanent_PointD) {
        this.permanent_PointD = permanent_PointD;
    }

    public void setPlayer_Name(String player_Name) {
        this.player_Name = player_Name;
    }

    public void setPlayer_Score(short player_Score) {
        this.player_Score = player_Score;
    }

    //檢查玩家手上籌碼有無超出持有籌碼上限
    public boolean is_Over_Limit(){
        if((need_PointA+need_PointB+need_PointC+need_PointD)>hand_limit){
            return false;
        }
        else
            return true;
    }
}

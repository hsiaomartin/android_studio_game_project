package com.example.myapplication;

public class Player {
    private int player_Score = 0;
    private int permanent_PointA = 0;
    private int permanent_PointB = 0;
    private int permanent_PointC = 0;
    private int permanent_PointD = 0;
    private int need_PointA = 0;
    private int need_PointB = 0;
    private int need_PointC = 0;
    private int need_PointD = 0;
    private int hand_limit = 10;
    private String player_Name;
    public Player(String name){
        player_Name = name;
    }
    public int getPlayer_Score() {
        return player_Score;
    }

    public int getPermanent_PointA() {
        return permanent_PointA;
    }

    public int getPermanent_PointB() {
        return permanent_PointB;
    }

    public int getPermanent_PointC() {
        return permanent_PointC;
    }

    public int getPermanent_PointD() {
        return permanent_PointD;
    }

    public int getNeed_PointA() {
        return need_PointA;
    }

    public int getNeed_PointB() {
        return need_PointB;
    }

    public int getNeed_PointC() {
        return need_PointC;
    }

    public int getNeed_PointD() {
        return need_PointD;
    }

    public int getResourceA(){
        return need_PointA+permanent_PointA;
    }
    public int getResourceB(){
        return need_PointB+permanent_PointB;
    }
    public int getResourceC(){
        return need_PointC+permanent_PointC;
    }
    public int getResourceD(){
        return need_PointD+permanent_PointD;
    }

    public String getPlayer_Name() {
        return player_Name;
    }

    public void setNeed_PointA(int need_PointA) {
        this.need_PointA = need_PointA;
    }

    public void setNeed_PointB(int need_PointB) {
        this.need_PointB = need_PointB;
    }

    public void setNeed_PointC(int need_PointC) {
        this.need_PointC = need_PointC;
    }

    public void setNeed_PointD(int need_PointD) {
        this.need_PointD = need_PointD;
    }

    public void setPermanent_PointA(int permanent_PointA) {
        this.permanent_PointA = permanent_PointA;
    }

    public void setPermanent_PointB(int permanent_PointB) {
        this.permanent_PointB = permanent_PointB;
    }

    public void setPermanent_PointC(int permanent_PointC) {
        this.permanent_PointC = permanent_PointC;
    }

    public void setPermanent_PointD(int permanent_PointD) {
        this.permanent_PointD = permanent_PointD;
    }

    public void setPlayer_Name(String player_Name) {
        this.player_Name = player_Name;
    }

    public void setPlayer_Score(int player_Score) {
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

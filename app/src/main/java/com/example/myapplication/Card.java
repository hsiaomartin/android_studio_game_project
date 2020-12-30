package com.example.myapplication;

public class Card {
    private String card_Data;
    private int card_Score;
    private int permanent_PointA;
    private int permanent_PointB;
    private int permanent_PointC;
    private int permanent_PointD;
    private int need_PointA;
    private int need_PointB;
    private int need_PointC;
    private int need_PointD;
    private String card_Picture;
    private String card_Name;
    /*
    封包格式:
    共有 9 種資料 [8:0] card_Data
    [score|permanent_A|permanent_B|permanent_C|permanent_D|need_A|need_B|need_C|need_D]
    score : 分數(0~2)
    permanent_A : 永久籌碼A(0 or 1)
    permanent_B : 永久籌碼B(0 or 1)
    permanent_C : 永久籌碼C(0 or 1)
    permanent_D : 永久籌碼D(0 or 1)
    永久籌碼ABCD僅能為，1000、0100、0010、0001
    need_A : 消耗籌碼A
    need_B : 消耗籌碼B
    need_C : 消耗籌碼C
    need_D : 消耗籌碼D
    消耗籌碼ABCD總和為3~5
     */

    public Card(String card_Info,String card_Picture,String card_Name){
        card_Data = card_Info;
        card_Score =  (int)(card_Info.charAt(0)-'0');
        permanent_PointA = (int)(card_Info.charAt(1)-'0');
        permanent_PointB = (int)(card_Info.charAt(2)-'0');
        permanent_PointC = (int)(card_Info.charAt(3)-'0');
        permanent_PointD = (int)(card_Info.charAt(4)-'0');
        need_PointA = (int)(card_Info.charAt(5)-'0');
        need_PointB = (int)(card_Info.charAt(6)-'0');
        need_PointC = (int)(card_Info.charAt(7)-'0');
        need_PointD = (int)(card_Info.charAt(8)-'0');
        this.card_Picture = card_Picture;
        this.card_Name = card_Name;
    }

    public String getCard_Data() {
        return card_Data;
    }

    public int getCard_Score() {
        return card_Score;
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


    public String getCard_Picture() {
        return card_Picture;
    }


    public String getCard_Name() {
        return card_Name;
    }

    public boolean card_Is_Available(int point_A, int point_B, int point_C, int point_D){
        if((point_A>=need_PointA)&&(point_B>=need_PointB)&&(point_C>=need_PointC)&&(point_D>=need_PointD))
            return true;
        else
            return false;
    }
}
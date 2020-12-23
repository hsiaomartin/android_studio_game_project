package com.example.myapplication;

public class Card {
    private short card_Score;
    private short permanent_PointA;
    private short permanent_PointB;
    private short permanent_PointC;
    private short permanent_PointD;
    private short need_PointA;
    private short need_PointB;
    private short need_PointC;
    private short need_PointD;
    private String card_Picture;
    public Card(String card_Info,String card_Picture){
        card_Score =  (short)(card_Info.charAt(0)-'0');
        permanent_PointA = (short)(card_Info.charAt(1)-'0');
        permanent_PointB = (short)(card_Info.charAt(2)-'0');
        permanent_PointC = (short)(card_Info.charAt(3)-'0');
        permanent_PointD = (short)(card_Info.charAt(4)-'0');
        need_PointA = (short)(card_Info.charAt(5)-'0');
        need_PointB = (short)(card_Info.charAt(6)-'0');
        need_PointC = (short)(card_Info.charAt(7)-'0');
        need_PointD = (short)(card_Info.charAt(8)-'0');
        this.card_Picture = card_Picture;
    }
    public short getCard_Score() {
        return card_Score;
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

    public String getCard_Picture() {
        return card_Picture;
    }

    public boolean card_Is_Available(short point_A,short point_B,short point_C,short point_D){
        if((point_A>=need_PointA)&&(point_B>=need_PointB)&&(point_C>=need_PointC)&&(point_D>=need_PointD))
            return true;
        else
            return false;
    }
}
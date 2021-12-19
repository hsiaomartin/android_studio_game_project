package com.example.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;
    public static int deck_Num = 40;
    StringBuilder sb = new StringBuilder();
    StringBuilder sb_Food_Name = new StringBuilder();
    public Deck(InputStreamReader isr_Card_Name,InputStreamReader isr_Food_Name){
        this.cards = new ArrayList<>();
        //讀取卡組數據
        getDeckData(isr_Card_Name);
        //讀取卡組食物名稱
        getDeckData_Food(isr_Food_Name);
        //產生卡組
        generateDeck();
        //卡組洗牌
        shuffle_Deck();
    }


    public ArrayList<Card> getCards() {
        return cards;
    }

    public void getDeckData(InputStreamReader isr){
        String line;
        BufferedReader br = new BufferedReader(isr);
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDeckData_Food(InputStreamReader isr){
        String line;
        BufferedReader br = new BufferedReader(isr);
        try {
            while ((line = br.readLine()) != null) {
                sb_Food_Name.append(line);
                sb_Food_Name.append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateDeck(){
        String[] tokens = (sb.toString()).split(" ");
        String[] tokens_Food = (sb_Food_Name.toString()).split(" ");
        for (int i = 0 ; i<tokens.length ; i++) {
            String token = tokens[i];
            String token_Food = tokens_Food[i];
            cards.add(new Card(token,"@mipmap/c"+i,"c"+i,token_Food));
            //System.out.println("value : "+(getCards().get(i)).getCard_Score());
        }

    }

    public void shuffle_Deck(){
        Collections.shuffle(cards);
    }
    public void shuffle_Deck(int random[]){
        ArrayList<Card> temp_cards = new ArrayList<>();
        for (int i = 0 ; i<deck_Num;i++){

            temp_cards.add(cards.get(random[i]));
        }
        cards = temp_cards;
    }

}

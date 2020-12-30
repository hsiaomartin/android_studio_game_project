package com.example.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;
    private int deck_Num = 40;
    StringBuilder sb = new StringBuilder();

    public Deck(InputStreamReader isr){
        this.cards = new ArrayList<>();
        //讀取卡組數據
        getDeckData(isr);
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

    public void generateDeck(){
        String[] tokens = (sb.toString()).split(" ");
        for (int i = 0 ; i<tokens.length ; i++) {
            String token = tokens[i];
            cards.add(new Card(token,"@mipmap/c"+i,"c"+i));
            //System.out.println("value : "+(getCards().get(i)).getCard_Score());
        }

    }

    public void shuffle_Deck(){
        Collections.shuffle(cards);
    }
}

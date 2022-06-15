package com.example.fc;

import java.io.Serializable;
import java.util.ArrayList;

public class Class_CardSet implements Serializable {
    private String id;
    private String name;
    private int size;
    private ArrayList<Class_Card> cardArrayList;

    public Class_CardSet(){}
    public Class_CardSet(String name) {
        this.name = name;
        this.size = 0;
        cardArrayList = new ArrayList<>();
    }

    public Class_CardSet(String name, ArrayList<Class_Card> cardArrayList){
        this.name = name;
        this.cardArrayList = cardArrayList;
        this.size = cardArrayList.size();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Class_Card> getCardArrayList() {
        return cardArrayList;
    }

    public void setCardArrayList(ArrayList<Class_Card> cardArrayList) {
        this.cardArrayList = cardArrayList;
    }
}

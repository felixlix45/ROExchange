package com.example.roexchange.model;

public class Item {
    private String name;
    private int type;
    private int lastPrice;
    private String lastDate;
    private String types;

    public void Item(String types) {
        this.types = types;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public void Item(String name, int type, int lastPrice, String lastDate) {
        this.name = name;
        this.type = type;
        this.lastPrice = lastPrice;
        this.lastDate = lastDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(int lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
}

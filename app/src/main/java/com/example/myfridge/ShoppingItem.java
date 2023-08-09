package com.example.myfridge;

public class ShoppingItem {
    private int id;
    private String name;
    private int quantity;
    private byte[] image;

    public ShoppingItem(int id, String name, int quantity, byte[] image) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public byte[] getImage() {
        return image;
    }
}

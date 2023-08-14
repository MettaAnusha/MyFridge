package com.example.myfridge.Models;

import static android.provider.Settings.System.DATE_FORMAT;

import android.graphics.Bitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemModel {
    private int id;
    private String name;
    private int quantity;
    private String expiryDate;
    private byte[] image;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public ItemModel(int id, String name, int quantity, String addedDate, byte[] image) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = addedDate;
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

    public String getAddedDate() {
        return expiryDate;
    }
    public int getDaysUntilExpiry() {

            Date today = new Date();
        Date expiry = null;
        try {
            expiry = DATE_FORMAT.parse(expiryDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long difference = expiry.getTime() - today.getTime();
            return (int) (difference / (1000 * 60 * 60 * 24));

    }

    public byte[] getImage() {
        return image;
    }
}

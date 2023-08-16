package com.example.myfridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.myfridge.Models.ItemModel;
import com.example.myfridge.Models.Usermodel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myfridge.db";
    private static final int DATABASE_VERSION = 1;


    // Table and column names

    //User
    static public final String TABLE_USER = "User";
    public static final String TABLE_SHOPPING_LIST = "shopping_list";
    public static final String TABLE_ITEMS = "items";
    static public final String COL_USERNAME = "username";
    static  public final String COL_EMAIL_ID = "emailId";
    static  public final String COL_PASSWORD = "password";


    private static final String COLUMN_IMAGE = "image";
    private static final String TABLE_HISTORY = "history";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_ADDED_DATE = "added_date";
    private static final String COLUMN_COMPLETED = "completed";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUser = "CREATE TABLE " + TABLE_USER + " (" +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL_ID + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                "PRIMARY KEY(" + COL_USERNAME + ")" +
                ");";
        String createTableItems = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_EXPIRY_DATE + " TEXT" + //
                ")";

        String createTableShoppingList = "CREATE TABLE " + TABLE_SHOPPING_LIST +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                COLUMN_ADDED_DATE + " TEXT NOT NULL, " +
                COLUMN_IMAGE + " BLOB, " + // Add the image column
                COLUMN_COMPLETED + " INTEGER NOT NULL" +
                ")";

        String createTableHistory = "CREATE TABLE " + TABLE_HISTORY +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                COLUMN_ADDED_DATE + " TEXT NOT NULL" +
                ")";
        db.execSQL(createTableUser);
        db.execSQL(createTableItems);
        db.execSQL(createTableShoppingList);
        db.execSQL(createTableHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
            onCreate(db);
        }
    }


    public Usermodel verifyCredentials(String username, String password) {
        Usermodel usermodel =  null;


        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] columns = {COL_USERNAME, COL_PASSWORD,COL_EMAIL_ID};
            String selection = COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?";
            String[] selectionArgs = {username, password};

            Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                int usernameIndex = cursor.getColumnIndex(COL_USERNAME);
                int passwordIndex = cursor.getColumnIndex(COL_PASSWORD);
                int emailIndex = cursor.getColumnIndex(COL_EMAIL_ID);

                if (usernameIndex >= 0 && passwordIndex >= 0) {
                    String fetchedUsername = cursor.getString(usernameIndex);
                    String fetchedPassword = cursor.getString(passwordIndex);
                    String fetchedEmail = cursor.getString(emailIndex);

                    usermodel = new Usermodel(fetchedUsername,fetchedEmail,fetchedPassword);
                }
            }
            cursor.close();
            db.close();
            return usermodel;

        } catch (Exception e) {
            return  null;
        }
    }

    public long insertItem(String tableName, String name, int quantity, byte[] image,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_EXPIRY_DATE, date);

        // Insert image data
        return db.insert(tableName, null, values);
    }
    public long insertShoppingItem(String name, int quantity, byte[] image, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_ADDED_DATE, date);
        values.put(COLUMN_COMPLETED, 0);
        // Insert image data
        return db.insert(TABLE_SHOPPING_LIST, null, values);
    }
    public List<ShoppingItem> getShoppingListItems() {
        List<ShoppingItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHOPPING_LIST, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                byte[] image= cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));

                ShoppingItem item = new ShoppingItem(id, name, quantity, image);
                items.add(item);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return items;
    }

    public long insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_EMAIL_ID, email);
        values.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result;
    }

    public List<ItemModel> getAllItems() {
        List<ItemModel> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_QUANTITY,
                COLUMN_IMAGE,
                COLUMN_EXPIRY_DATE
        };

        Cursor cursor = db.query(
                TABLE_ITEMS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE));

                ItemModel item = new ItemModel(id, name, quantity,  expiryDate,image);
                itemList.add(item);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return itemList;
    }

    public void updateShoppingItem(String originalItemName, String updatedName, int updatedQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, updatedName);
        values.put(COLUMN_QUANTITY, updatedQuantity);

        String whereClause = COLUMN_NAME + " = ?";
        String[] whereArgs = {originalItemName};

        db.update(TABLE_ITEMS, values, whereClause, whereArgs);
        db.close();
    }
    public ShoppingItem getShoppingItem(String itemName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_QUANTITY,
                COLUMN_IMAGE
        };

        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {itemName};

        Cursor cursor = db.query(
                TABLE_SHOPPING_LIST,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ShoppingItem shoppingItem = null;

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));

            shoppingItem = new ShoppingItem(id, name, quantity, image);
            cursor.close();
        }

        return shoppingItem;
    }

    public ItemModel getItem(String itemName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_QUANTITY,
                COLUMN_IMAGE,
                COLUMN_EXPIRY_DATE
        };

        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {itemName};

        Cursor cursor = db.query(
                TABLE_ITEMS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ItemModel shoppingItem = null;

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
            String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE));

            shoppingItem = new ItemModel(id, name, quantity,expiryDate, image);
            cursor.close();
        }

        return shoppingItem;
    }
    public void deleteItem(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_NAME + " = ?";
        String[] whereArgs = {itemName};

        db.delete(TABLE_ITEMS, whereClause, whereArgs);
        db.close();
    }
    public void deleteShoppingItem(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_NAME + " = ?";
        String[] whereArgs = {itemName};

        db.delete(TABLE_SHOPPING_LIST, whereClause, whereArgs);
        db.close();
    }
}

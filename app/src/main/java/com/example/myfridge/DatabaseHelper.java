package com.example.myfridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myfridge.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_ITEMS = "items";
    public static final String TABLE_SHOPPING_LIST = "shopping_list";
    private static final String COLUMN_IMAGE = "image";
    private static final String TABLE_HISTORY = "history";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_ADDED_DATE = "added_date";
    private static final String COLUMN_COMPLETED = "completed";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableItems = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " INTEGER NOT NULL" +
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

    public long insertItem(String tableName, String name, int quantity, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_IMAGE, image); // Insert image data
        return db.insert(tableName, null, values);
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



}

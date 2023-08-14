package com.example.myfridge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        recyclerView = findViewById(R.id.recyclerViewShoppingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListAdapter(this);

        databaseHelper = new DatabaseHelper(this);
        List<ShoppingItem> shoppingItems = databaseHelper.getShoppingListItems();

        adapter.setShoppingItems(shoppingItems);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        Log.i("ButtonClicked" ,"ButtonUpdate");
        if(v.getId() == R.id.btnUpdateItem)
        {
            Intent intent = new Intent(ShoppingListActivity.this, UpdateItemActivity.class);
            startActivity(intent);
        }
    }
}

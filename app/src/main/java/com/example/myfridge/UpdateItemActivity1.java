package com.example.myfridge;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import com.example.myfridge.databinding.ActivityUpdateItem1Binding;


public class UpdateItemActivity1 extends AppCompatActivity {

    private ActivityUpdateItem1Binding updateItemBinding1;
    private DatabaseHelper databaseHelper;
    private String originalItemName;
    private String previousFragmentClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize data binding
        updateItemBinding1 = ActivityUpdateItem1Binding.inflate(getLayoutInflater());
        setContentView(updateItemBinding1.getRoot());

        // Get the original item name and previous fragment class name from the Intent
        originalItemName = getIntent().getStringExtra("originalItemName");
        previousFragmentClassName = getIntent().getStringExtra("previousFragment");

        // Initialize the DatabaseHelper instance
        databaseHelper = new DatabaseHelper(this);

        // Fetch the item details and populate the UI
        populateItemDetails(originalItemName);

        updateItemBinding1.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateShoppingItem1(originalItemName);
            }
        });

    }

    private void populateItemDetails(String itemName) {
        if (itemName != null) {
            ShoppingItem item = databaseHelper.getShoppingItem(itemName);
            if (item != null) {
                updateItemBinding1.edtUpdatedItemName.setText(item.getName());
                updateItemBinding1.edtUpdatedQuantity.setText(String.valueOf(item.getQuantity()));
            }
        }
    }
    private void updateShoppingItem1(String originalItemName) {
        String updatedName = updateItemBinding1.edtUpdatedItemName.getText().toString();
        String updatedQuantityString = updateItemBinding1.edtUpdatedQuantity.getText().toString();

        if (updatedName.isEmpty() || updatedQuantityString.isEmpty()) {
            // Handle validation errors
            return;
        }

        int updatedQuantity = Integer.parseInt(updatedQuantityString);

        if (originalItemName != null) {
            databaseHelper.updateShoppingItem(originalItemName, updatedName, updatedQuantity);

            if (previousFragmentClassName != null) {
                try {
                    Class<?> previousFragmentClass = Class.forName(previousFragmentClassName);
                    Intent intent = new Intent(UpdateItemActivity1.this, previousFragmentClass);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            setResult(RESULT_OK);
            finish();
        }
    }

}

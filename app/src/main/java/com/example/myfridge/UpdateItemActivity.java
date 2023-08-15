package com.example.myfridge;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myfridge.Models.ItemModel;
import com.example.myfridge.databinding.ActivityUpdateItemBinding;

public class UpdateItemActivity extends AppCompatActivity {

    private ActivityUpdateItemBinding updateItemBinding;
    private DatabaseHelper databaseHelper;
    private String originalItemName;
    private String previousFragmentClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize data binding
        updateItemBinding = ActivityUpdateItemBinding.inflate(getLayoutInflater());
        setContentView(updateItemBinding.getRoot());

        // Get the original item name and previous fragment class name from the Intent
        originalItemName = getIntent().getStringExtra("originalItemName");
        previousFragmentClassName = getIntent().getStringExtra("previousFragment");

        // Initialize the DatabaseHelper instance
        databaseHelper = new DatabaseHelper(this);

        // Fetch the item details and populate the UI
        populateItemDetails(originalItemName);

        updateItemBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateShoppingItem(originalItemName);
            }
        });

        updateItemBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateItemActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void populateItemDetails(String itemName) {
        if (itemName != null) {
            ItemModel item = databaseHelper.getItem(itemName);
            if (item != null) {
                updateItemBinding.edtUpdatedItemName.setText(item.getName());
                updateItemBinding.edtUpdatedQuantity.setText(String.valueOf(item.getQuantity()));
            }
        }
    }

    private void updateShoppingItem(String originalItemName) {
        String updatedName = updateItemBinding.edtUpdatedItemName.getText().toString();
        String updatedQuantityString = updateItemBinding.edtUpdatedQuantity.getText().toString();

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
                    Intent intent = new Intent(UpdateItemActivity.this, previousFragmentClass);
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

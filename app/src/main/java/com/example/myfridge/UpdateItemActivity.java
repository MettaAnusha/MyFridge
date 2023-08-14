package com.example.myfridge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.myfridge.databinding.ActivityUpdateItemBinding;

public class UpdateItemActivity extends AppCompatActivity {

    ActivityUpdateItemBinding updateItemBinding;
    private DatabaseHelper dbh;
    private String originalItemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_item);

        View view = updateItemBinding.getRoot();
        setContentView(view);

        dbh = new DatabaseHelper(this);

        // Get the original item name from the Intent
        originalItemName = getIntent().getStringExtra("originalItemName");

        // Fetch the item details and populate the UI
        populateItemDetails(originalItemName);

        updateItemBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem(originalItemName);
            }
        });
    }
    private void populateItemDetails(String itemName) {
        if (itemName != null) {
            ShoppingItem item = dbh.getShoppingItem(itemName);
            if (item != null) {
                updateItemBinding.edtUpdatedItemName.setText(item.getName());
                updateItemBinding.edtUpdatedQuantity.setText(String.valueOf(item.getQuantity()));
            }
        }
    }

    private void updateItem(String originalItemName) {
        String updatedName = updateItemBinding.edtUpdatedItemName.getText().toString().trim();
        String updatedQuantityStr = updateItemBinding.edtUpdatedQuantity.getText().toString().trim();

        if (updatedName.isEmpty() || updatedQuantityStr.isEmpty()) {
            updateItemBinding.tilUpdatedItemName.setError("Name and quantity are required");
            return;
        }

        int updatedQuantity = Integer.parseInt(updatedQuantityStr);

        if (updatedQuantity <= 0) {
            updateItemBinding.tilUpdatedQuantity.setError("Quantity must be greater than 0");
            return;
        }

        if (originalItemName != null) {
            dbh.updateShoppingItem(originalItemName, updatedName, updatedQuantity);
            setResult(RESULT_OK);
            finish();
        }
    }
}
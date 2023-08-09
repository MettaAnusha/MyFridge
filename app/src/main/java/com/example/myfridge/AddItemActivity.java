package com.example.myfridge;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfridge.databinding.ActivityAddItemBinding;

import java.io.ByteArrayOutputStream;

public class AddItemActivity extends AppCompatActivity {

    private ActivityAddItemBinding binding;
    private DatabaseHelper databaseHelper;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private byte[] itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = binding.editTextItemName.getText().toString().trim();
                String quantity = binding.editTextQuantity.getText().toString().trim();

                if (!itemName.isEmpty() && !quantity.isEmpty()) {
                    int quantityValue = Integer.parseInt(quantity);


                    long result = databaseHelper.insertItem(DatabaseHelper.TABLE_SHOPPING_LIST, itemName, quantityValue, itemImage);

                    if (result != -1) {
                        Toast.makeText(AddItemActivity.this, "Item added to shopping list", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                    } else {
                        Toast.makeText(AddItemActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddItemActivity.this, "Please enter a valid item name and quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.buttonAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void clearInputFields() {
        binding.editTextItemName.setText("");
        binding.editTextQuantity.setText("");
        binding.imageViewItemPicture.setImageDrawable(null);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            if (imageBitmap != null) {
                binding.imageViewItemPicture.setImageBitmap(imageBitmap);
                itemImage = convertBitmapToByteArray(imageBitmap);
            }
        }
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}

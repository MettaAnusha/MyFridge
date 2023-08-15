package com.example.myfridge;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfridge.databinding.ActivityAddItemBinding;
import com.example.myfridge.databinding.ActivityAddShoppingItemBinding;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

public class AddShoppingItem extends AppCompatActivity {

    private ActivityAddShoppingItemBinding binding;
    private DatabaseHelper databaseHelper;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private byte[] itemImage;
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private EditText DateEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddShoppingItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        DateEditText = binding.edtDateEditText;

        // Initialize DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the selected date
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        DateEditText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Show the DatePicker when the EditText is clicked
        DateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        binding.buttonAddItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String itemName = binding.editTextItemName.getText().toString().trim();
                String quantity = binding.editTextQuantity.getText().toString().trim();
                try{
                    if (!itemName.isEmpty() && !quantity.isEmpty()) {
                        int quantityValue = Integer.parseInt(quantity);
                        Date today = new Date();

                        String dateStr = binding.edtDateEditText.getText().toString().trim();
                        long result = databaseHelper.insertShoppingData(DatabaseHelper.TABLE_SHOPPING_LIST, itemName, quantityValue, itemImage,dateStr);

                        if (result != -1) {
                            Toast.makeText(AddShoppingItem.this, "Item added to shopping list", Toast.LENGTH_SHORT).show();
                            clearInputFields();
                            finish();
                        } else {
                            Toast.makeText(AddShoppingItem.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AddShoppingItem.this, "Please enter a valid item name and quantity", Toast.LENGTH_SHORT).show();
                    }}
                catch (Exception e){
                    Log.d("InsertError",e.toString());
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
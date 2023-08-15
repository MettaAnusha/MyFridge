package com.example.myfridge;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myfridge.databinding.ActivityAddItemBinding;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    private ActivityAddItemBinding binding;
    private DatabaseHelper databaseHelper;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private byte[] itemImage;
    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private EditText DateEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
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
                    String dateStr = binding.edtDateEditText.getText().toString().trim();
                       long result = databaseHelper.insertItem(DatabaseHelper.TABLE_ITEMS, itemName, quantityValue, itemImage,dateStr);

                       if (result != -1) {
                           Toast.makeText(AddItemActivity.this, "Item added to Pantry", Toast.LENGTH_SHORT).show();
                           clearInputFields();
                           finish();
                       } else {
                           Toast.makeText(AddItemActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                       }
                   }
                 else {
                    Toast.makeText(AddItemActivity.this, "Please enter a valid item name and quantity", Toast.LENGTH_SHORT).show();

                 }}
                catch (Exception e){
                    Log.d("InsertError",e.toString());
                }
            }
        });

        binding.buttonAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check and request camera permission
                checkCameraPermissionAndCaptureImage();
            }
        });
    }

    private void checkCameraPermissionAndCaptureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Permission already granted, proceed with capturing the image
            dispatchTakePictureIntent();
        }
    }
    private void clearInputFields() {
        binding.editTextItemName.setText("");
        binding.editTextQuantity.setText("");
        binding.imageViewItemPicture.setImageDrawable(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with capturing the image
                dispatchTakePictureIntent();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
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

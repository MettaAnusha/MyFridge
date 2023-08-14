package com.example.myfridge.Common;

import static android.widget.Toast.makeText;

import android.content.Context;
import android.widget.Toast;

public  class Common {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
package com.example.myfridge;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myfridge.Common.KeywordsUtils;
import com.example.myfridge.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding homeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding= ActivityHomeBinding.inflate(getLayoutInflater());
        View view = homeBinding.getRoot();
        setContentView(view);
        SetBottomNavigation();
        setDefaultFragment();
        homeBinding.buttonToolbarAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItem = (int) homeBinding.bottomNavView.getSelectedItemId();

                if(selectedItem == R.id.nav_history){
                    Intent intent = new Intent(HomeActivity.this, AddItemActivity.class);
                    startActivity(intent);
                }
               else if(selectedItem == R.id.nav_shopping){
                    Intent intent = new Intent(HomeActivity.this, AddShoppingItem.class);
                    startActivity(intent);
                }
               else{


                }

            }
        });
    }
    private void setDefaultFragment() {
        Fragment defaultFragment = new ItemListFragment(); // Change to the appropriate fragment class

        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.frame, defaultFragment);
        fragTrans.commit();
    }
    private void SetBottomNavigation()
    {


            homeBinding.bottomNavView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item)
            {
                navigation(item);
            }
        });

        homeBinding.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigation(item);
                return true; // Return true to indicate the item is selected
            }
        });

    }
    public void navigation(MenuItem item){
        Fragment frag = null;
        if (item.getItemId() == R.id.nav_shopping) {
            frag = new ShoppingItemListFragment();

        } else if (item.getItemId() == R.id.nav_history) {
            frag = new ItemListFragment();
        } else{
            SharedPreferences sharedPreferences = getSharedPreferences(KeywordsUtils.SP_userSession, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(KeywordsUtils.SP_userName);
            editor.apply();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (frag != null) {
            FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
            fragTrans.replace(R.id.frame, frag);
            fragTrans.commit();
        }
    }



}
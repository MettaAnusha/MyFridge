package com.example.myfridge;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class ShoppingItemListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shoppingitem_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewShoppingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ShoppingListAdapter(requireContext());

        databaseHelper = new DatabaseHelper(requireContext());
        List<ShoppingItem> shoppingItems = databaseHelper.getShoppingListItems();

        adapter.setShoppingItems(shoppingItems);
        recyclerView.setAdapter(adapter);
    }
}
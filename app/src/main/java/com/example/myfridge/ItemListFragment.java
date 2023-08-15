package com.example.myfridge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfridge.Models.ItemModel;



import java.util.List;

public class ItemListFragment extends Fragment {

    private RecyclerView recyclerViewItems;
    private ItemAdapter itemAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

        recyclerViewItems = rootView.findViewById(R.id.recyclerView);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        List<ItemModel> itemList = databaseHelper.getAllItems();

        if (itemList != null && !itemList.isEmpty()) {
            itemAdapter = new ItemAdapter(getContext(), itemList);
            recyclerViewItems.setAdapter(itemAdapter);
        }

        return rootView;
    }
}

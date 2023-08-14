package com.example.myfridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfridge.BitmapUtils;
import com.example.myfridge.Models.ItemModel;
import com.example.myfridge.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<ItemModel> itemList;
    private Context context;

    public ItemAdapter(Context context, List<ItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.textViewItemName.setText(item.getName());
        holder.textViewQuantity.setText(String.valueOf(item.getQuantity()));
        holder.expiryDate.setText("Expired in: "+String.valueOf(item.getDaysUntilExpiry()));

        // Set the item image if available
        if (item.getImage() != null) {

            holder.imageViewItem.setImageBitmap(BitmapUtils.getImage(item.getImage()));
        } else {
            holder.imageViewItem.setImageResource(R.drawable.images); // Set a placeholder image
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewItemName;
        public TextView textViewQuantity;
        public ImageView imageViewItem;
        public TextView expiryDate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            expiryDate = itemView.findViewById(R.id.textViewExpiry);
        }
    }
}

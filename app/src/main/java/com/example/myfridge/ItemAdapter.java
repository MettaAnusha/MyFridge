package com.example.myfridge;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfridge.Models.ItemModel;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<ItemModel> itemList;
    private final Context context;

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
        holder.textViewQuantity.setText(context.getString(R.string.quantity_d, item.getQuantity()));
        if (item.getDaysUntilExpiry() > 0) {
            holder.expiryDate.setText(context.getString(R.string.expires_d_days, item.getDaysUntilExpiry()));
        } else {
            holder.expiryDate.setText(R.string.expired);
        }

        // Set the item image if available
        if (item.getImage() != null) {
            holder.imageViewItem.setImageBitmap(BitmapUtils.getImage(item.getImage()));
        } else {
            holder.imageViewItem.setImageResource(R.drawable.images); // Set a placeholder image
        }

        // Set click listeners for update and delete buttons
        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the UpdateActivity with item's data
                Intent intent = new Intent(context, UpdateItemActivity.class);
                intent.putExtra("originalItemName", item.getName()); // Pass the original item name
                context.startActivity(intent);

            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    databaseHelper.deleteItem(item.getName());

                    int removedPosition = holder.getAdapterPosition();
                    itemList.remove(removedPosition);
                    notifyItemRemoved(removedPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void setItemList(List<ItemModel> itemList) {
        this.itemList = itemList;
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
        public Button buttonUpdate;
        public Button buttonDelete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            expiryDate = itemView.findViewById(R.id.textViewExpiry);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

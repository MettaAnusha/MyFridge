package com.example.myfridge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<ShoppingItem> shoppingItems;
    private LayoutInflater inflater;
    private Context context; // Add this context variable

    public ShoppingListAdapter(Context context) {
        this.context = context; // Initialize the context
        inflater = LayoutInflater.from(context);
    }

    public void setShoppingItems(List<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_shopping_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem item = shoppingItems.get(position);
        holder.itemNameTextView.setText(item.getName());
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
        if (item.getImage() != null) {
            holder.itemImageView.setImageBitmap(BitmapUtils.getImage(item.getImage()));
        } else {
            holder.itemImageView.setImageResource(R.drawable.images);
        }
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
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.deleteItem(item.getName());

                int removedPosition = holder.getAdapterPosition();
                shoppingItems.remove(removedPosition);
                notifyItemRemoved(removedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView quantityTextView;
        ImageView itemImageView;
        public Button buttonUpdate;
        public Button buttonDelete;

        ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.textViewItemName);
            quantityTextView = itemView.findViewById(R.id.textViewQuantity);
            itemImageView = itemView.findViewById(R.id.imageViewItem);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

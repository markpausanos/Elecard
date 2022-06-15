package com.example.fc;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Class_CardSetHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mTitle, mCount;
    Interface_ItemClickListener itemClickListener;
    public Class_CardSetHolder(@NonNull View itemView) {
        super(itemView);

        this.mTitle = itemView.findViewById(R.id.row_title);
        this.mCount = itemView.findViewById(R.id.row_count);
        itemView.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClickListener(view, getLayoutPosition());
    }
    public void setItemClickListener(Interface_ItemClickListener id){
        this.itemClickListener = id;
    }
}

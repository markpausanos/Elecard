package com.example.fc;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Class_CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mQuestion, mOption;
    Interface_ItemClickListener itemClickListener;

    public Class_CardHolder(@NonNull View itemView) {
        super(itemView);

        this.mQuestion = itemView.findViewById(R.id.flashcard_question);
        this.mOption = itemView.findViewById(R.id.flashcard_option);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClickListener(view, getLayoutPosition());
    }

    public void setItemClickListener(Interface_ItemClickListener id) {
        this.itemClickListener = id;
    }
}

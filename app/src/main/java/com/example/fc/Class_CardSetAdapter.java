package com.example.fc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Class_CardSetAdapter extends RecyclerView.Adapter<Class_CardSetHolder> {

    Context context;
    ArrayList<Class_CardSet> cardSets;

    public Class_CardSetAdapter(Context context, ArrayList<Class_CardSet> cardSets) {
        this.context = context;
        this.cardSets = cardSets;
    }

    @NonNull
    @Override
    public Class_CardSetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_row, null);
        return new Class_CardSetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Class_CardSetHolder holder, int position) {
        holder.mTitle.setText(cardSets.get(position).getName());
        holder.mCount.setText(Integer.toString(cardSets.get(position).getSize()));

        holder.setItemClickListener(new Interface_ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(context, CardSet_Edit.class);
                intent.putExtra("cardSet", cardSets.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardSets.size();
    }
}

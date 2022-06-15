package com.example.fc;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Class_CardAdapter extends RecyclerView.Adapter<Class_CardHolder>{

    Context context;
    ArrayList<Class_Card> cards;
    Dialog dialog;
    DB_Cards db_cards;
    public Class_CardAdapter(Context context, ArrayList<Class_Card> cards) {
        this.context = context;
        this.cards = cards;
        db_cards = new DB_Cards();
    }

    @NonNull
    @Override
    public Class_CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_flashcard, null);
        return new Class_CardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Class_CardHolder holder, int position) {
        holder.mQuestion.setText(cards.get(position).getQuestion());
        holder.mOption.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.mOption);
            popupMenu.inflate(R.menu.option_flashcard);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch(menuItem.getItemId()){
                    case R.id.option_editFlashcard:
                        editFlashCard(cards.get(position));
                        break;
                    case R.id.option_deleteFlashcard:
                        DB_Cards db_cards = new DB_Cards();
                        db_cards.deleteFlashCard(cards.get(position));
                        break;
                }
                return true;
            });
            popupMenu.show();
        });
    }
    public void editFlashCard(Class_Card card){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_customflashcards);
        Button cancel = dialog.findViewById(R.id.addFlashCancel);
        Button save = dialog.findViewById(R.id.addFlashSave);
        EditText question = dialog.findViewById(R.id.addFlashQuestion);
        EditText answer = dialog.findViewById(R.id.addFlashAnswer);
        save.setText("save");

        question.setText(card.getQuestion());
        answer.setText(card.getAnswer());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question.getText().toString().isEmpty()){
                    question.setError("Question must not be empty!");
                    question.requestFocus();
                    return;
                }
                if(answer.getText().toString().isEmpty()){
                    answer.setError("Answer must not be empty");
                    answer.requestFocus();
                    return;
                }
                db_cards.updateFlashCard(card, question.getText().toString(), answer.getText().toString());
                Toast.makeText(context, "Flashcard updated!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public int getItemCount() {
        return cards.size();
    }
}

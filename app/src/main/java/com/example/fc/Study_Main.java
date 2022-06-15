package com.example.fc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

public class Study_Main extends AppCompatActivity {
    Class_CardSet cardSet;
    ArrayList<Class_Card> cards;
    TextView question, answer, count;
    EasyFlipView flipView;
    Dialog dialog;
    DB_Cards db_cards;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3_study_main);
        Intent intent = getIntent();
        index = 0;
        dialog = new Dialog(this);
        db_cards = new DB_Cards();
        cardSet = (Class_CardSet) intent.getSerializableExtra("cardSet");
        cards = cardSet.getCardArrayList();
        count = findViewById(R.id.countFlashcards);
        flipView = findViewById(R.id.easyFlipView);
        question = findViewById(R.id.questionCard);
        answer = findViewById(R.id.answerCard);
        count.setText((index+1) + "/" + cardSet.getSize());
        flipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                if(newCurrentSide == EasyFlipView.FlipState.BACK_SIDE){
                    answer.setVisibility(View.VISIBLE);
                }else{
                    answer.setVisibility(View.INVISIBLE);
                }
            }
        });
        setCard();
    }
    public void setCard(){
        if(flipView.isBackSide()){
            answer.setVisibility(View.INVISIBLE);
            flipView.flipTheView();
        }
        count.setText((index+1) + "/" + cards.size());
        answer.setText(cards.get(index).getAnswer());
        question.setText(cards.get(index).getQuestion());
    }
    public void onClick(View view){
        switch(view.getId()){
            case R.id.stopFlashCard:
                finish();
                break;
            case R.id.editFlashCard:
                editFlashCard(cards.get(index));
                break;
            case R.id.backFlashcard:
                if(index != 0){
                    index--;
                    setCard();
                }
                break;
            case R.id.nextFlashcard:
                if(index != cards.size()-1){
                    index++;
                    setCard();
                }
                break;
            case R.id.deleteFlashCard:
                deleteFlashCard();

        }
    }
    public void editFlashCard(Class_Card card) {
        dialog = new Dialog(this);
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
                if (question.getText().toString().isEmpty()) {
                    question.setError("Question must not be empty!");
                    question.requestFocus();
                    return;
                }
                if (answer.getText().toString().isEmpty()) {
                    answer.setError("Answer must not be empty");
                    answer.requestFocus();
                    return;
                }
                db_cards.updateFlashCard(card, question.getText().toString(), answer.getText().toString());
                Toast.makeText(Study_Main.this, "Flashcard updated!", Toast.LENGTH_LONG).show();
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
    public void deleteFlashCard(){
        dialog.setContentView(R.layout.dialog_deletecard);
        Button confirm = dialog.findViewById(R.id.deleteFCardConfirm);
        Button cancel = dialog.findViewById(R.id.deleteFCardCancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean deleteLast = false;
                if(index == cards.size()-1){
                    deleteLast = true;
                }
                db_cards.deleteFlashCard(cardSet, cards.get(index), cards.size()-1);
                cards.remove(index);
                if(cards.size() == 0){
                    finish();
                    return;
                }
                if(deleteLast){
                    index--;
                }

                setCard();
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
}
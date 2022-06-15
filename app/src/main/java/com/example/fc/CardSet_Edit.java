package com.example.fc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CardSet_Edit extends AppCompatActivity {
    RecyclerView mRecyclerView;
    Class_CardAdapter mAdapter;
    TextView cardSetCount, cardSetName, isEmptyArray;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    DB_Cards db_cards;
    Class_CardSet cardSet;
    Dialog dialog;
    Button play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_edit);
        Intent intent = getIntent();
        auth = FirebaseAuth.getInstance();
        play = findViewById(R.id.cardSet_play);
        cardSet = (Class_CardSet) intent.getSerializableExtra("cardSet");
        mRecyclerView = findViewById(R.id.recyclerViewEditSet);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference =  FirebaseDatabase.getInstance().getReference().child("users").child(auth.getUid()).child("cardset").child(cardSet.getId());
        db_cards = new DB_Cards();
        cardSetName = findViewById(R.id.cardSet_title);
        cardSetCount = findViewById(R.id.cardSet_count);
        isEmptyArray = findViewById(R.id.isEmpty);
        progressBar = findViewById(R.id.cardSet_progressbar);
        cardSetName.setText(cardSet.getName());
        String cardwithS = cardSet.getSize() > 1 ? " cards" : " card";
        cardSetCount.setText(cardSet.getSize() + cardwithS);
        dialog = new Dialog(this);
        onLoadFlashCards();
    }

    private void onLoadFlashCards(){
        db_cards.getFlashCards(cardSet).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Class_Card> cardsTemp = new ArrayList<>();
                int size = 0;
                for(DataSnapshot data: snapshot.getChildren()){
                    Class_Card card = data.getValue(Class_Card.class);
                    cardsTemp.add(card);
                    size++;
                }
                if(size == 0){
                    isEmptyArray.setVisibility(View.VISIBLE);
                    play.setEnabled(false);
                }else{
                    isEmptyArray.setVisibility(View.GONE);
                    play.setEnabled(true);
                }
                String cardwithS = cardSet.getSize() > 1 ? " cards" : " card";
                cardSetCount.setText(size + cardwithS);
                cardSet = db_cards.updateCardSetSize(cardSet, size);
                cardSet.setCardArrayList(cardsTemp);
                mAdapter = new Class_CardAdapter(CardSet_Edit.this, cardsTemp);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CardSet_Edit.this, "Failed to connect!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.cardSet_kebab:
                TextView kebab = findViewById(R.id.cardSet_kebab);
                PopupMenu popupMenu = new PopupMenu(this, kebab);
                popupMenu.inflate(R.menu.option_cardset);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch(menuItem.getItemId()){
                        case R.id.option_editCardSet:
                            editCardSetName();
                            break;
                        case R.id.option_deleteCardSet:
                            deleteCardSet();
                            break;
                    }
                    return true;
                });
                popupMenu.show();
                break;
            case R.id.cardSet_add:
                addFlashCard();
                break;
            case R.id.cardSet_play:
                Intent intent =  new Intent(this, Study_Main.class);
                intent.putExtra("cardSet", cardSet);
                startActivity(intent);
                break;
            case R.id.cardSet_cancel:
                finish();
        }

    }
    public void editCardSetName(){
        dialog.setContentView(R.layout.dialog_editcardset);
        EditText inp = dialog.findViewById(R.id.editNewCardset);
        Button confirm = dialog.findViewById(R.id.editCardConfirm);
        Button cancel = dialog.findViewById(R.id.editCardCancel);
        inp.setText(cardSet.getName());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inp.getText().toString().isEmpty()){
                    inp.setError("Name must not be empty!");
                    return;
                }
                cardSet = db_cards.updateCardSetName(cardSet, inp.getText().toString());
                cardSetName.setText(inp.getText());
                Toast.makeText(CardSet_Edit.this, "Card set name updated!", Toast.LENGTH_LONG).show();
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
    public void deleteCardSet(){
        dialog.setContentView(R.layout.dialog_deletecardset);
        Button confirm = dialog.findViewById(R.id.deleteCardConfirm);
        Button cancel = dialog.findViewById(R.id.deleteCardCancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_cards.deleteCardSet(cardSet);
                finish();
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
    public void addFlashCard(){
        dialog.setContentView(R.layout.dialog_customflashcards);
        Button cancel = dialog.findViewById(R.id.addFlashCancel);
        Button save = dialog.findViewById(R.id.addFlashSave);
        EditText question = dialog.findViewById(R.id.addFlashQuestion);
        EditText answer = dialog.findViewById(R.id.addFlashAnswer);
        save.setText("add");
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
                db_cards.createFlashCard(cardSet, new Class_Card(question.getText().toString(), answer.getText().toString()));
                Toast.makeText(CardSet_Edit.this, "Flashcard added to this set!", Toast.LENGTH_LONG).show();
                question.setText("");
                answer.setText("");
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
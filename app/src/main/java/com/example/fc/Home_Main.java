package com.example.fc;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home_Main extends AppCompatActivity {
    Dialog dialog;
    RecyclerView mRecyclerView;
    Class_CardSetAdapter mAdapter;
    DB_Cards db_cards;
    FirebaseAuth auth;
    ProgressBar progressBar;
    AppCompatButton option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);
        setContentView(R.layout.activity1_home);
        auth = FirebaseAuth.getInstance();
        option = findViewById(R.id.home_options);
        mRecyclerView = findViewById(R.id.recyclerViewHome);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        db_cards = new DB_Cards();
        progressBar = findViewById(R.id.home_progressbar);
        getNickName();
        onLoadCardSets();

    }
    private void getNickName(){
        TextView welcomeUser = findViewById(R.id.welcomeUserText);
        DatabaseReference dEmail = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getUid()).child("nickname");

        dEmail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                welcomeUser.setText("Hello, " + snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void onLoadCardSets(){
        db_cards.getCardSets().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Class_CardSet> cardSetsTemp = new ArrayList<>();
                for(DataSnapshot data: snapshot.getChildren()){
                    Class_CardSet cardSet = data.getValue(Class_CardSet.class);
                    cardSetsTemp.add(cardSet);
                }
                mAdapter = new Class_CardSetAdapter(Home_Main.this, cardSetsTemp);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home_Main.this, "Failed to connect!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addCardDialog(View view){
        dialog.setContentView(R.layout.dialog_createcardset);
        EditText inp = dialog.findViewById(R.id.home_createNewCardset);
        Button add = dialog.findViewById(R.id.home_addCardConfirm);
        Button cancel = dialog.findViewById(R.id.home_addCardCancel);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_cards.createCardSet(new Class_CardSet(inp.getText().toString()));
                Toast.makeText(Home_Main.this, "Card set added!", Toast.LENGTH_LONG).show();
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
    public void optionHome(View view){
        PopupMenu popupMenu = new PopupMenu(this, option);
        popupMenu.inflate(R.menu.option_home);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.option_editnickname:
                    changeNameDialog();
                    break;
                case R.id.option_logout:
                    try {
                        auth.signOut();
                    } catch (Exception e) {
                        Toast.makeText(Home_Main.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                    break;
            }
            return true;
        });
        popupMenu.show();
    }
    public void changeNameDialog(){
        dialog.setContentView(R.layout.dialog_editusername);
        EditText inp = dialog.findViewById(R.id.editNicknameINP);
        Button confirm = dialog.findViewById(R.id.nameEditConfirm);
        Button cancel = dialog.findViewById(R.id.nameEditCancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inp.getText().toString().isEmpty()){
                    inp.setError("Name cannot be empty!");
                    return;
                }
                db_cards.updateUserNickName(inp.getText().toString());
                getNickName();
                Toast.makeText(Home_Main.this, "Nickname updated!", Toast.LENGTH_LONG).show();
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

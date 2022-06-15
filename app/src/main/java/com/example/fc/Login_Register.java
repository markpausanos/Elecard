package com.example.fc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Login_Register extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth regAuth;
    TextInputEditText inpEmail, inpPass, inpConfirm, inpName;
    ProgressBar progressBar;
    TextView backToLogin;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity0_register);
        regAuth = FirebaseAuth.getInstance();
        inpEmail = findViewById(R.id.reg_inp_email);
        inpPass = findViewById(R.id.reg_inp_password);
        inpConfirm = findViewById(R.id.reg_confirmpassword);
        inpName = findViewById(R.id.reg_inp_nickname);
        register = findViewById(R.id.reg_register);
        progressBar = findViewById(R.id.reg_progressbar);
        backToLogin = findViewById(R.id.reg_backtologin);
        backToLogin.setOnClickListener(this::onClick);
        register.setOnClickListener(this::onClick);
    }
    public void registerUser(){
        String email = inpEmail.getText().toString().trim();
        String password = inpPass.getText().toString().trim();
        String confirm = inpConfirm.getText().toString().trim();
        String name = inpName.getText().toString().trim();
        if(email.isEmpty()){
            inpEmail.setError("Email field must not be empty!");
            inpEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inpEmail.setError("Please provide a valid email!");
            inpEmail.requestFocus();
            return;
        }
        if(name.isEmpty()){
            inpName.setError("Name must not be empty!");
            inpName.requestFocus();
            return;
        }
        if(password.isEmpty()){
            inpPass.setError("Password must not be empty!");
            inpPass.requestFocus();
            return;
        }
        if(password.length() <= 6){
            inpPass.setError("Password length too small!");
            inpPass.requestFocus();
            return;
        }
        if(!password.equals(confirm)){
            inpConfirm.setError("Passwords do not match!");
            inpConfirm.requestFocus();
            return;
        }
        
        progressBar.setVisibility(View.VISIBLE);
        regAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Class_User classUser = new Class_User(email, name);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(classUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Login_Register.this, "Check email to verify account!", Toast.LENGTH_LONG).show();
//                                        String id = FirebaseDatabase.getInstance().getReference("users").child(regAuth.getUid()).child("cardset").push().getKey();
//                                        HashMap<String,String> temp = new HashMap<>();
//                                        temp.put("id", id);
//                                        temp.put("name", "default");
//                                        Class_Card cards = new Class_Card("Question",  "Answer");
//                                        FirebaseDatabase.getInstance().getReference("users").child(regAuth.getUid()).child("cardset").child(id).setValue(temp);
//                                        String idcard = FirebaseDatabase.getInstance().getReference("users").child(regAuth.getUid()).child("cardset").child(id).child("flashcards").push().getKey();
//                                        cards.setId(idcard);
//                                        FirebaseDatabase.getInstance().getReference("users").child(regAuth.getUid()).child("cardset").child(id).child("flashcards").child(idcard).setValue(cards);



                                    }
                                    else{
                                        Toast.makeText(Login_Register.this, "Failed to register, try again!", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        }else{
                            Toast.makeText(Login_Register.this, "Failed to register, try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
        startActivity(new Intent(this, Login_Main.class));
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.reg_backtologin:
                startActivity(new Intent(this, Login_Main.class));
                break;
            case R.id.reg_register:
                registerUser();
                break;

        }

    }
}
package com.example.fc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class Login_Main extends AppCompatActivity implements View.OnClickListener{
    private TextView register, forgotPass, login;
    private TextInputEditText inpEmail, inpPass;
    private Button signIn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity0_login);
        auth = FirebaseAuth.getInstance();
        register = findViewById(R.id.main_register);
        login = findViewById(R.id.main_login);

        register.setOnClickListener(this::onClick);
        signIn = findViewById(R.id.main_login);
        signIn.setOnClickListener(this::onClick);

        inpEmail = findViewById(R.id.main_inp_email);
        inpPass = findViewById(R.id.main_inp_password);
        progressBar = findViewById(R.id.main_progressbar);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.main_login:
                System.out.println("Login clicked!");
                userLogin();
                break;
            case R.id.main_register:
                System.out.println("Clicked register");
                startActivity(new Intent(this, Login_Register.class));
                break;
            case R.id.main_forgotPass:
                startActivity(new Intent(this, Login_ForgotPass.class));
                break;
        }
    }
    private void userLogin(){
        String email = inpEmail.getText().toString().trim();
        String password = inpPass.getText().toString().trim();
        if(email.isEmpty()){
            inpEmail.setError("Email is required!");
            inpEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inpEmail.setError("Please enter a valid email!");
            inpEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            inpPass.setError("Password is required!");
            inpPass.requestFocus();
            return;
        }
        if(password.length() <= 6){
            inpPass.setError("Password length too small.");
            inpPass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        Intent intent = new Intent(Login_Main.this, Home_Main.class);
                        progressBar.setVisibility(View.GONE);
                        progressBar.requestFocus();
                        startActivity(intent);
                        inpEmail.setText("");
                        inpPass.setText("");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        user.sendEmailVerification();
                        Toast.makeText(Login_Main.this, "Check email to verify account", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(Login_Main.this, "Failed to validate credentials. Check your Internet connection or your credentials!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
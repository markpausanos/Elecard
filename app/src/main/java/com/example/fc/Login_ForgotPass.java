package com.example.fc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Login_ForgotPass extends AppCompatActivity {
    private TextInputEditText inpEmail;
    private Button resetPass;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity0_forgot_pass);

        inpEmail = findViewById(R.id.forgot_email);
        resetPass = findViewById(R.id.forgot_reset);
        progressBar = findViewById(R.id.forgot_progressbar);

        auth = FirebaseAuth.getInstance();
    }

    public void resetPass(View view) {
        if(view.getId() == R.id.backToHome){
            finish();
            return;
        }
        resetPassword();
    }
    public void resetPassword(){
        String email = inpEmail.getText().toString().trim();

        if(email.isEmpty()){
            inpEmail.setError("Email is required!");
            inpEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inpEmail.setError("Please provide a valid email!");
            inpEmail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login_ForgotPass.this, "Check your email to reset password.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(Login_ForgotPass.this, "Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
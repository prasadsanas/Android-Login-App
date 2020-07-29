package com.example.subjectparadise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {


    EditText username;
    Button forgotPasswordSubmit;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        username=findViewById(R.id.username);
        forgotPasswordSubmit=findViewById(R.id.forgotPasswordSubmit);

        firebaseAuth=FirebaseAuth.getInstance();

        forgotPasswordSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(username.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(forgotPassword.this,
                                    "Password send to your email.",Toast.LENGTH_LONG).show();

                            Intent i = new Intent(forgotPassword.this, MainActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(forgotPassword.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
}

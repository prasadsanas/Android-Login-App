package com.example.subjectparadise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    TextView Register;
    private Button Login;
    private EditText email;
   // private EditText username;
    private EditText password;
    FirebaseAuth firebaseAuth;
    CheckBox showPassword;
    private FirebaseAuth.AuthStateListener AuthStateListener;


    private static final int PER_LOGIN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        Register = (TextView) findViewById(R.id.Register);

        Login = (Button) findViewById(R.id.login);
        showPassword=findViewById(R.id.showPassword);
        email = (EditText) findViewById(R.id.username);
//           username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);


        AuthStateListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseUser != null && mFirebaseUser.isEmailVerified()) {
                    Toast.makeText(MainActivity.this, "You are logged in.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, blank.class);
                    startActivity(intent);
                }

            }
        };

        if (!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {
            Login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String memail = email.getText().toString();
                    String mpassword = password.getText().toString();
                    if (!(memail.isEmpty() && mpassword.isEmpty())) {
                        firebaseAuth.signInWithEmailAndPassword(memail, mpassword)
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            if(Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()){
                                                Intent intent = new Intent(MainActivity.this, blank.class);
                                                email.setText("");
                                                password.setText("");
                                                startActivity(intent);


                                            }else {
                                                Toast.makeText(MainActivity.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }

                }

            });


            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Signup.class);
                    startActivity(intent);
                }
            });

            TextView forgotPassword;
            forgotPassword = (TextView) findViewById(R.id.forgotPassword);
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, forgotPassword.class);
                    startActivity(intent);
                }
            });


            showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }else{
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(AuthStateListener);
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }


    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access application. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }


}


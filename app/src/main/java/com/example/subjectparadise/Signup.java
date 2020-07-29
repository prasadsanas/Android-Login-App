package com.example.subjectparadise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class Signup extends AppCompatActivity {

    public static final String TAG = Signup.class.getSimpleName();
    private Button signup;
    private EditText name;
    private EditText number;
    private EditText username;
    private EditText email;
    private EditText password;

    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signup = (Button) findViewById(R.id.signup);
        name=(EditText) findViewById((R.id.name));
        number=(EditText) findViewById((R.id.number));
        username=(EditText) findViewById((R.id.username));
        email=(EditText) findViewById((R.id.email));
        password=(EditText) findViewById((R.id.password));

        if (!isConnected(Signup.this)) buildDialog(Signup.this).show();
        else {
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mname = name.getText().toString();
                    String mnumber = number.getText().toString();
                    String musername = username.getText().toString();
                    final String memail = email.getText().toString();
                    String mpassword = password.getText().toString();

                    if (mname.isEmpty() || mnumber.isEmpty() || musername.isEmpty() || memail.isEmpty() || mpassword.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Fill all the details", Toast.LENGTH_SHORT).show();
                    } else {

                        Users userDetail = new Users(mname, mnumber, musername, memail, mpassword);
                        mDatabase.push().child(musername).setValue(userDetail);
                        Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_LONG).show();


                        firebaseAuth.createUserWithEmailAndPassword(memail, mpassword)
                                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {

                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful())
                                        {
                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Signup.this,"Verification Link has been sent.",Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG,"onFailure: Email not sent "+ e.getMessage());
                                                }
                                            });


                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        }
                                        else {
                                            Toast.makeText(Signup.this,"Error Occured. Try Again" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
//                    Intent i = new Intent(Signup.this, MainActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                   startActivity(i);
                }
            });
        }
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

class Users{
    public Users(String name, String number, String username, String email, String password) {
        this.name = name;
        this.number = number;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String name;
    public String number;
    public String username;
    public String email;
    public String password;

    public Users(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
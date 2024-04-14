package com.example.firebaseproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseproject.Modals.Users;
import com.example.firebaseproject.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button register;

    private EditText username,password,email;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ActivityRegisterBinding binding;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        register=findViewById(R.id.register);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);


        progressDialog=new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're Creating your Account");

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        binding.alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String username1=username.getText().toString();
                String password1=password.getText().toString();
                String email1=email.getText().toString();
                if (TextUtils.isEmpty(username1) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(email1)){
                    Toast.makeText(RegisterActivity.this, "Enter email and password both", Toast.LENGTH_SHORT).show();
                }
                else {
                    regis(email1,password1) ;
                }
            }
        });


    }
    private void regis(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Users user=new Users(binding.username.getText().toString(),
                                         binding.email.getText().toString(),
                                         binding.password.getText().toString());
                    String id=task.getResult().getUser().getUid();
                    database.getReference().child("Users").child(id).setValue(user);
                    Toast.makeText(RegisterActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "createUserWithEmail:success");
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));


                }
                else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
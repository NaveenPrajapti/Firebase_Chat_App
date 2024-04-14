package com.example.firebaseproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.firebaseproject.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    TextView registeracount;
    Button login;
     FirebaseAuth auth;
     FirebaseDatabase database;
    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView google;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginbtn);
        google=findViewById(R.id.signin_google);

        registeracount = findViewById(R.id.register_account);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");

        //google sign in
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        gsc= GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        //go to Register account
        registeracount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String ema=email.getText().toString();
                String pass=password.getText().toString();
                if (TextUtils.isEmpty(ema) || TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this, "Please Enter email and password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else {
                    auth.signInWithEmailAndPassword(binding.email.getText().toString(),
                                    binding.password.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        if (auth.getCurrentUser()!=null){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
    int RC_SIGN_IN=99;
    private void signIn(){
        Intent signIntent=gsc.getSignInIntent();
      startActivityForResult(signIntent,RC_SIGN_IN);

    }

    //Google SignIn Authentication
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount>task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
           GoogleSignInAccount account=task.getResult(ApiException.class);
           Log.d("TAG","FirebaseAuthWithGoogle" +account.getId());
           firebaseAuth(account.getIdToken());
            }
            catch (Exception e){
                Toast.makeText(this,"GoogleSignInFaild", Toast.LENGTH_SHORT).show();
                Log.d("TAG","GoogleSignInFaild",e);
            }
        }
    }
    private void firebaseAuth(String idToken) {
        AuthCredential credential=GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Users users=new Users();
                            users.setUserId(user.getUid());
                            users.setUsername(user.getDisplayName());
                            users.setPrfilepic(user.getPhotoUrl().toString());
                            database.getReference().child("Users").child(user.getUid()).setValue(users);
                            

                            Intent intent=new Intent( LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Sign in with google", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this,"failed", Toast.LENGTH_SHORT).show();
                            Snackbar.make(binding.getRoot(),"Authentication failed",Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}

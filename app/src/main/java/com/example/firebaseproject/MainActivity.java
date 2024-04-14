package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.firebaseproject.Adapters.FragmentsAdapter;
import com.example.firebaseproject.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

   ActivityMainBinding binding;
    Toolbar toolbar;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar=findViewById(R.id.toolbar);
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        binding.viewpager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.viewpager);


        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String currentuser = null;
        toolbar.setSubtitle(currentuser);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.settings){
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
        } else if (id==R.id.logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            auth.signOut();
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.msg) {
            Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.more) {
            Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.groupchat) {
            Intent intent=new Intent(MainActivity.this,GroupChat.class);
            startActivity(intent);
        }


        // Add other menu item handling as needed

        return super.onOptionsItemSelected(item);
    }

}
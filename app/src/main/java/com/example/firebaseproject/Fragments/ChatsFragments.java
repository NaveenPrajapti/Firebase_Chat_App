package com.example.firebaseproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebaseproject.Adapters.UsersAdapter;
import com.example.firebaseproject.Modals.Users;
import com.example.firebaseproject.R;
import com.example.firebaseproject.databinding.FragmentChatsFragmentsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragments extends Fragment {


    public ChatsFragments() {

        // Required empty public constructor
    }
    FragmentChatsFragmentsBinding binding;

    ArrayList<Users>list=new ArrayList<>();
    FirebaseDatabase database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        binding=FragmentChatsFragmentsBinding.inflate(inflater,container,false);

        database=FirebaseDatabase.getInstance();
        UsersAdapter adapter=new UsersAdapter(list,getContext());
        binding.chatrecycleview.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.chatrecycleview.setLayoutManager(linearLayoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  list.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                        Users users=snapshot1.getValue(Users.class);
                   users.setUserId(snapshot1.getKey());
                    list.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();

    }
}
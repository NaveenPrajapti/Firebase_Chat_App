package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebaseproject.Adapters.ChatAdapter;
import com.example.firebaseproject.Modals.MessageModel;
import com.example.firebaseproject.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChat extends AppCompatActivity {
    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChat.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.username.setText("Friend Chat");

        final ChatAdapter adapter = new ChatAdapter(messageModels, this);
        binding.chatrecycleview.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatrecycleview.setLayoutManager(layoutManager);

        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                MessageModel model=dataSnapshot.getValue(MessageModel.class);
                messageModels.add(model);
            }
            adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message=binding.typeMessage.getText().toString();
                final MessageModel messageModel=new MessageModel(senderId,message);
                messageModel.setTimestamp(new Date().getTime());
                binding.typeMessage.setText("");
                if (message.isEmpty()){
                    Toast.makeText(GroupChat.this, "write message", Toast.LENGTH_SHORT).show();
                }
                else {
                    database.getReference().child("Group Chat")
                            .push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                }
                            });
                }

            }
        });


    }
}
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
import com.example.firebaseproject.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetail extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        final String senderId=auth.getUid();
        String recieveId=getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("userName");
        String profilePic=getIntent().getStringExtra("profilePic");

        binding.username.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileimage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatDetail.this, MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<MessageModel> messageModels=new ArrayList<>();
        final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this);

        binding.chatrecycleview.setAdapter(chatAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.chatrecycleview.setLayoutManager(linearLayoutManager);

          final String senderRoom=senderId+recieveId;
          final String recieverRoom=recieveId+senderId;


          //show msg on recycler view
          database.getReference().child("chats")
                          .child(senderRoom)
                                  .addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                                          messageModels.clear();
                                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                                            MessageModel model=snapshot1.getValue(MessageModel.class);
                                            messageModels.add(model);
                                        }
                                        chatAdapter.notifyDataSetChanged();
                                      }

                                      @Override
                                      public void onCancelled(@NonNull DatabaseError error) {

                                      }
                                  });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

         String message=  binding.typeMessage.getText().toString();
         final MessageModel messageModel=new MessageModel(senderId,message);
         messageModel.setTimestamp(new Date().getTime());
         binding.typeMessage.setText("");
         if (message.isEmpty()){
             Toast.makeText(ChatDetail.this, "write message", Toast.LENGTH_SHORT).show();
         }
         else {

             database.getReference().child("chats")
                     .child(senderRoom)
                     .push()
                     .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {
                             database.getReference().child("chats")
                                     .child(recieverRoom)
                                     .push()
                                     .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void unused) {

                                         }
                                     });
                         }
                     });
         }

            }
        });

    }
}
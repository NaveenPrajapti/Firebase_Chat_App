package com.example.firebaseproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseproject.Modals.MessageModel;
import com.example.firebaseproject.R;
import com.example.firebaseproject.databinding.SampleReciverBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> messageModels;
    Context context;
    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;


    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
       else {
            View view= LayoutInflater.from(context).inflate(R.layout.sample_reciver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override//sender reciever msg view
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
MessageModel messageModel=messageModels.get(position);
if (holder.getClass()==SenderViewHolder.class){
    ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
}
else {
    (( ReceiverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());
}
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg,receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
                receiverMsg=itemView.findViewById(R.id.recieverText);
                receiverTime=itemView.findViewById(R.id.reciverTime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMsg,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.sendermsg);
            senderTime=itemView.findViewById(R.id.senderTime);
        }
    }
}

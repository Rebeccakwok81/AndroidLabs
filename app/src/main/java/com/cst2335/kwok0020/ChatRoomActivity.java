package com.cst2335.kwok0020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity {

    Button sendBtn;
    Button receivedBtn;
    EditText edit;
    RecyclerView rView;
    MyAdapter theAdapter;
    ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        sendBtn = findViewById(R.id.sendButton);
        receivedBtn = findViewById(R.id.receiveButton);
        edit = findViewById(R.id.editMsg);
        rView = findViewById(R.id.myRecycleView);


        theAdapter = new MyAdapter();
        rView.setAdapter(theAdapter);
        rView.setLayoutManager(new LinearLayoutManager(this));

        sendBtn.setOnClickListener( click ->{
            String type = edit.getText().toString();

            //add a new message to history if not empty
            if (!type.isEmpty()) {
                messages.add(new Message(type, 0, 1));
                edit.setText(""); //clear the text
                //notify that new data was added at a row:
                theAdapter.notifyItemInserted(messages.size() - 1); //at the end of ArrayList,

            }

        });

        receivedBtn.setOnClickListener( click ->{
            String type = edit.getText().toString();
            //add a new message to history if not empty
            if (!type.isEmpty()) {
                messages.add(new Message(type, 1, 0));
                edit.setText(""); //clear the text
                //notify that new data was added at a row:
                theAdapter.notifyItemInserted(messages.size() - 1); //at the end of ArrayList,

            }
        });


    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int LAYOUT_LEFT = 0;
        private static final int LAYOUT_RIGHT = 1;



        @Override
        public int getItemViewType(int position) {
            if(messages.get(position).getSend() == 0)
                return 0;
            else
                return 1;
        }

        //this holds TextViews on a row:
        public class MyViewHolder1 extends RecyclerView.ViewHolder {

            TextView messageView1;

            public MyViewHolder1(View itemView) {
                super(itemView);


                messageView1 = itemView.findViewById(R.id.sendMsg);

                itemView.setOnClickListener(click -> {
                    int position = getAdapterPosition();//which row was clicked.
                    Message clicked = messages.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
                    builder.setTitle("Question:")
                            .setMessage("Do you want to delete this:" + clicked.getMessageTyped())
                            .setNegativeButton("Negetive", (dialog, click1) -> {
                            })
                            .setPositiveButton("Positive", (dialog, click2) -> {
                                //actually delete something:
                                messages.remove(position);
                                theAdapter.notifyItemRemoved(position);
                            }).create().show();


                });
            }

        }

        public class MyViewHolder2 extends RecyclerView.ViewHolder {

            TextView messageView2;

            public MyViewHolder2 (View itemView) {
                super(itemView);

                messageView2 = itemView.findViewById(R.id.receiveMsg);

                itemView.setOnClickListener(click -> {
                    int position = getAdapterPosition();//which row was clicked.
                    Message clicked = messages.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
                    builder.setTitle("Question:")
                            .setMessage("Do you want to delete this:" + clicked.getMessageTyped())
                            .setNegativeButton("Negetive", (dialog, click1) -> {
                            })
                            .setPositiveButton("Positive", (dialog, click2) -> {
                                //actually delete something:
                                messages.remove(position);
                                theAdapter.notifyItemRemoved(position);
                            }).create().show();


                });
            }

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            switch (viewType){
                case LAYOUT_LEFT:
                    return new MyViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chatroom_left, parent, false));
                case LAYOUT_RIGHT:
                    return new MyViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chatroom_right, parent, false));

            }

            return null;
        }

        @Override
        //Array to hold all the message
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            final Message msg = messages.get(position);

            if(holder.getItemViewType() == LAYOUT_LEFT){
                MyViewHolder1 v1 = (MyViewHolder1) holder;
                v1.messageView1.setText(messages.get(position).getMessageTyped());
            } else {
                MyViewHolder2 v2 = (MyViewHolder2) holder;
                v2.messageView2.setText(messages.get(position).getMessageTyped());
            }


        }


        @Override
        public int getItemCount() {
            return messages.size();
        }



    }

    public class Message {
        String messageTyped;
        int send;
        int received;


        public Message(String messageTyped, int send, int received) {

            this.messageTyped = messageTyped;
            this.send = send;
            this.received = received;
        }

        public String getMessageTyped() {

            return messageTyped;
        }

        public int getSend(){
            return send;
        }

        public int getReceived(){
            return received;
        }

    }

}



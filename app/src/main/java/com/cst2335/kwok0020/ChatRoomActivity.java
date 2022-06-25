package com.cst2335.kwok0020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;


public class ChatRoomActivity extends AppCompatActivity {

    Button sendBtn;
    Button receivedBtn;
    EditText edit;
    RecyclerView rView;
    MyAdapter theAdapter;
    ArrayList<Message> messages = new ArrayList<>();
    TextView messageView1;
    TextView messageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        sendBtn = findViewById(R.id.sendButton);
        receivedBtn = findViewById(R.id.receiveButton);
        edit = findViewById(R.id.editMsg);
        rView = findViewById(R.id.listview);

        theAdapter = new MyAdapter();
        rView.setAdapter(theAdapter);
        rView.setLayoutManager(new LinearLayoutManager(this));

        sendBtn.setOnClickListener( click ->{
            String type = edit.getText().toString();
            //add a new message to history if not empty
            if (!type.isEmpty()) {
                messages.add(new Message(type));
                edit.setText(""); //clear the text
                //notify that new data was added at a row:
                theAdapter.notifyItemInserted(messages.size() - 1); //at the end of ArrayList,

            }
        });

        receivedBtn.setOnClickListener( click ->{
            String type = edit.getText().toString();
            //add a new message to history if not empty
            if (!type.isEmpty()) {
                messages.add(new Message(type));
                edit.setText(""); //clear the text
                //notify that new data was added at a row:
                theAdapter.notifyItemInserted(messages.size() - 1); //at the end of ArrayList,

            }
        });

    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        RecyclerView.ViewHolder viewHolder;

        //this holds TextViews on a row:
        public class MyViewHolder1 extends RecyclerView.ViewHolder {

            public MyViewHolder1(View itemView) {
                super(itemView);

                messageView1 = itemView.findViewById(R.id.sendText);

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

            LayoutInflater li = getLayoutInflater();

                switch (viewType) {
                    case 0:
                        View leftRow = li.inflate(R.layout.activity_chatroom_left, parent, false);
                        viewHolder = new MyViewHolder1(leftRow);
                        break;

                    case 1:
                        View rightRow = li.inflate(R.layout.activity_chatroom_right, parent, false);
                        viewHolder = new MyViewHolder2(rightRow);

                }
                return viewHolder;
            }

            @Override
            //Array to hold all the message
            public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
                switch (viewHolder.getItemViewType()) {
                    case 0:
                        MyViewHolder1 vh1 = (MyViewHolder1) viewHolder;
                        configureViewHolder1(vh1, position);
                        break;

                    case 1:
                        MyViewHolder2 vh2 = (MyViewHolder2) viewHolder;
                        configureViewHolder2(vh2, position);
                        break;

                }
            }

        private void configureViewHolder1(MyViewHolder1 vh1, int position) {
            if (messages != null) {
                Message thisRow = messages.get(position);
                messageView1.setText(thisRow.getMessageTyped());
            }
        }

        private void configureViewHolder2(MyViewHolder2 vh2, int position) {
            if (messages != null) {
                Message thisRow = messages.get(position);
                messageView2.setText(thisRow.getMessageTyped());
            }
        }
            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
            return position;
            }

    }

    public class Message {
        String messageTyped;


        public Message(String messageTyped) {

            this.messageTyped = messageTyped;
        }

        public String getMessageTyped() {

            return messageTyped;
        }

    }

}





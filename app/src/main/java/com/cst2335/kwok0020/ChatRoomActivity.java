package com.cst2335.kwok0020;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatRoomActivity extends AppCompatActivity {

    Button sendBtn;
    Button receivedBtn;
    EditText edit;
    ListView listView;
    MyAdapter theAdapter;
    ArrayList<Message> messages = new ArrayList<>();
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        sendBtn = findViewById(R.id.sendButton);
        receivedBtn = findViewById(R.id.receiveButton);
        edit = findViewById(R.id.editMsg);
        listView = findViewById(R.id.ListView);

        theAdapter = new MyAdapter();
        listView.setAdapter(theAdapter);

        sendBtn.setOnClickListener( click ->{
            String type = edit.getText().toString();
            list.add(type);
            edit.setText("");


            if ( !type.isEmpty()) {
                messages.add(new Message(type));

                edit.setText("");//clear the text

                //notify that new data was added at a row:
                theAdapter.notifyDataSetChanged(); //at the end of ArrayList,

            }


        });

        receivedBtn.setOnClickListener( click ->{
            String type = edit.getText().toString();
            list.add(type);
            edit.setText("");



            if ( !type.isEmpty()) {
                messages.add(new Message(type));

                edit.setText("");//clear the text

                //notify that new data was added at a row:
                theAdapter.notifyDataSetChanged(); //at the end of ArrayList,

            }
        });

        listView.setOnItemLongClickListener( (p, b, pos, id) -> {

            Message whatWasClicked = messages.get(pos);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Make a choice")

                    .setMessage("The selected row is:" + whatWasClicked.getMessageTyped())
                    .setNegativeButton("Negative", (dialog, click1)->{ })
                    .setPositiveButton("Positive", (dialog, click2)->{
                        //actually delete something:
                        messages.remove(pos);
                        theAdapter.notifyDataSetChanged();
                    }).create().show();
            return true;
        });

    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = getLayoutInflater();


            if(position ==0) {

                    convertView = li.inflate(R.layout.activity_chatroom_left, parent, false);
                    TextView send = convertView.findViewById(R.id.sendMsg);
                    sendBtn = convertView.findViewById(R.id.sendButton);
                    send.setText(messages.get(position).getMessageTyped());

                }

            else{

                 convertView = li.inflate(R.layout.activity_chatroom_right, parent, false);
                 TextView receive = convertView.findViewById(R.id.receiveMsg);
                 receivedBtn = convertView.findViewById(R.id.receiveButton);
                 receive.setText(messages.get(position).getMessageTyped());

            }




            return convertView;
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




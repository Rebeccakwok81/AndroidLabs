package com.cst2335.kwok0020;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    Button sendBtn;
    Button receivedBtn;
    EditText edit;
    ListView listView;
    MyAdapter theAdapter;
    ArrayList<Message> messages = new ArrayList<>();
    SQLiteDatabase db;

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";

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

        loadDataFromDatabase(); //get any previously saved Contact objects



        sendBtn.setOnClickListener(click -> {
            String type = edit.getText().toString();

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //Now provide a value for every database column defined in MyOpener.java:
            //put string message in the MESSAGE column:
            newRowValues.put(MyOpener.COL_MESSAGE, type);

            //Now insert in the database:
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Message object
            Message newMsg = new Message(type, true, newId);

            //update the listView:
            theAdapter.notifyDataSetChanged();

            if (!type.isEmpty()) {
                messages.add(new Message(type, true, newId));

                edit.setText("");//clear the text

                //notify that new data was added at a row:
                theAdapter.notifyDataSetChanged(); //at the end of ArrayList,

            }
        });


        receivedBtn.setOnClickListener(click -> {
            String type = edit.getText().toString();

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //Now provide a value for every database column defined in MyOpener.java:
            //put string message in the MESSAGE column:
            newRowValues.put(MyOpener.COL_MESSAGE, type);

            //Now insert in the database:
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Message object
            Message newMsg = new Message(type, false, newId);

            //update the listView:
            theAdapter.notifyDataSetChanged();


            if (!type.isEmpty()) {
                messages.add(new Message(type, false, newId));

                edit.setText("");//clear the text

                //notify that new data was added at a row:
                theAdapter.notifyDataSetChanged(); //at the end of ArrayList,

            }


        });

        listView.setOnItemLongClickListener((p, b, pos, id) -> {

            Message clicked = messages.get(pos);

            AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
            builder.setTitle("Question:")
                    .setMessage("Do you want to delete this:" + clicked.getMessageTyped())
                    .setNegativeButton("Negetive", (dialog, click1) -> {
                    })
                    .setPositiveButton("Positive", (dialog, click2) -> {
                        deleteContact(messages.get(pos)); //remove the msg from database
                        messages.remove(pos);//remove the msg from message list
                        theAdapter.notifyDataSetChanged();
                    }).create().show();

            return false;
        });

        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        listView.setOnItemClickListener((list, item, position, id)->{
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, messages.get(position).messageTyped );
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);

            if(isTablet)
            {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });



    }

    private void loadDataFromDatabase() {

        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int msgColumnIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String msg = results.getString(msgColumnIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            messages.add(new Message(msg,true, id));
        }

    }

    protected void deleteContact(Message m)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(m.getId())});
    }

    public void printCursor( Cursor c, int version)
    {
        String result;
        String cursorRows = "";
        int dbVersion = db.getVersion();
        int col = c.getColumnCount();
        String[] columnNames = c.getColumnNames();
        int row =  c.getCount();
        result = "Version "+ dbVersion + "colums " + col + "Colum Names " + columnNames + "row " + row;
        for( c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            cursorRows += c.getString(0) + c.getString(1) + c.getString(2);

        }
        Log.i(result, "this is a test");

    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return position % 2;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = getLayoutInflater();

            if (messages.get(position).send() == true) {
                convertView = li.inflate(R.layout.activity_chatroom_left, parent, false);
                TextView send = convertView.findViewById(R.id.sendMsg);
                ImageView img = convertView.findViewById(R.id.sendIcon);
                send.setText(messages.get(position).getMessageTyped());
            } else {
                convertView = li.inflate(R.layout.activity_chatroom_right, parent, false);
                TextView receive = convertView.findViewById(R.id.receiveMsg);
                ImageView img = convertView.findViewById(R.id.receiveIcon);
                receive.setText(messages.get(position).getMessageTyped());
            }

            return convertView;

        }

    }

    public class Message {
        String messageTyped;
        boolean send;
        long id;

        public Message(String messageTyped, boolean send, long id) {

            this.messageTyped = messageTyped;
            this.send = send;
            this.id = id;
        }

        public String getMessageTyped() {

            return messageTyped;
        }

        public boolean send() {
            return send;
        }

        public long getId(){
            return id;
        }

    }

}


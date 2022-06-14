package com.cst2335.kwok0020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_lab3);

        EditText editText = findViewById(R.id.editText2);
        Button btn = findViewById(R.id.button);

        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        String previous = sp.getString("editEmail", "");
        editText.setText(previous);

        btn.setOnClickListener(clk -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("editEmail", editText.getText().toString());
            editor.apply();

            Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
            goToProfile.putExtra("EMAIL", editText.getText().toString());
            startActivity(goToProfile);

        });

        }

        @Override
        protected void onPause() {
        super.onPause();

        }
    }

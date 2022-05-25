package com.cst2335.kwok0020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);

        Button btn = findViewById(R.id.button);
        Switch sw = findViewById(R.id.switch1);


        btn.setOnClickListener( (vw) -> {
                Toast.makeText(MainActivity.this, getString(R.string.toast_message), Toast.LENGTH_LONG).show();

        });

        sw.setOnCheckedChangeListener((cb, b) ->{
            if(sw.isChecked()){
                Snackbar.make(cb, getString(R.string.sw_on_message), Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo_message, click -> cb.setChecked((!b)))
                        .show();
            } else
            Snackbar.make(cb, getString(R.string.sw_off_message), Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo_message, click -> cb.setChecked((!b)))
                    .show();
        });

        }
    }

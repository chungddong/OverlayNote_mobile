package com.sophra.overlaynote_mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    TextView edit_text;
    RelativeLayout mainlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edit_text = findViewById(R.id.edit_text);
        mainlayout = findViewById(R.id.mainlayout);

        edit_text.setText(MainActivity.filename);
        mainlayout.setBackgroundColor(Color.parseColor(MainActivity.color));

        Log.d("log_overlay", "color : " + MainActivity.color);


    }
}

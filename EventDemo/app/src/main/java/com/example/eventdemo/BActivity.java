package com.example.eventdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.event_lib.CEventCenter;

public class BActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        TextView tvWorld = findViewById(R.id.tv_world);
        tvWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchEvent();
//                finish();
            }
        });
    }

    public void dispatchEvent() {
        CEventCenter.dispatchEvent("Test", 0, 0, "来自BActivity的事件");
    }
}

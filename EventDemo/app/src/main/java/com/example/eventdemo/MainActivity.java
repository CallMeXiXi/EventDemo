package com.example.eventdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.event_lib.CEventCenter;
import com.example.event_lib.ICEventListener;

public class MainActivity extends AppCompatActivity implements ICEventListener {

    private TextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHello = findViewById(R.id.tv_hello);
        tvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSecondActivity();
            }
        });

        CEventCenter.registerEventListener(this, "Test");
    }

    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        switch (topic) {
            case "Test":
                tvHello.setText(obj.toString());
                break;
            default:
                tvHello.setText("default");
                break;
        }
    }

    public void gotoSecondActivity() {
        startActivity(new Intent(this, BActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CEventCenter.unregisterEventListener(this, "Test");
    }
}

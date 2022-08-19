package com.example.hrvmat3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityOption extends AppCompatActivity {

    Button videoBtn;
    Button breathActivity;
    Button userActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        videoBtn=findViewById(R.id.videoBtn);
        breathActivity=findViewById(R.id.breathActivity);
        userActivity=findViewById(R.id.userActivity);

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVideo();
            }
        });

        breathActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBreath();
            }
        });

        userActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUser();
            }
        });
    }

    public void openVideo(){
        Intent intent= new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    public void openBreath(){
        Intent intent= new Intent(this, DeepBreathingActivity.class);
        startActivity(intent);
    }

    public void openUser(){
        Intent intent= new Intent(this, InsertActivity.class);
        startActivity(intent);
    }
}
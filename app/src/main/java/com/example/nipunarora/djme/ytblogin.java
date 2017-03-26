package com.example.nipunarora.djme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ytblogin extends AppCompatActivity {
    private TextView conti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_login);
        conti=(TextView)findViewById(R.id.contiii);
        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),YoutubeWebview.class);
                startActivity(i);
            }
        });
    }
}

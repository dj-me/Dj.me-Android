package com.example.nipunarora.djme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    private Runnable mrunnable;
    private android.os.Handler mhandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mhandler=new android.os.Handler();
        mrunnable=new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Preferences",getApplicationContext().MODE_PRIVATE);
                Boolean isNamethere = sharedPref.getBoolean("isNameThere",false);
                if(isNamethere) {
                    Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(), GetNameActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        };
        mhandler.postDelayed(mrunnable,3000);
    }


}

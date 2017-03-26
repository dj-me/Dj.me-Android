package com.example.nipunarora.djme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import Fragments.HostRegister;

public class Host extends AppCompatActivity {
    private FrameLayout storyboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        getSupportFragmentManager().beginTransaction().replace(R.id.hostFrame, new HostRegister()).commit();
    }
}

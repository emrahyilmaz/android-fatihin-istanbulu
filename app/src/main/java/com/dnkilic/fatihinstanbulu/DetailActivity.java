package com.dnkilic.fatihinstanbulu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dnkilic.fatihinstanbulu.network.GetPlaceDetailsTask;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String name = getIntent().getStringExtra("NAME");
        String id = getIntent().getStringExtra("ID");

        new GetPlaceDetailsTask(id).execute();

    }
}

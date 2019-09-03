package com.example.venkatmugesh.hallprocv20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button adminOptions , hallPlan , hallAlloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adminOptions = findViewById(R.id.adminOptions);
        hallAlloc = findViewById(R.id.hallAlloc);
        hallPlan = findViewById(R.id.hallPlan);

        adminOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , LoginActivity.class));
            }
        });
        hallPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , HallPlan.class));
            }
        });
        hallAlloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , HallAllocate.class));
            }
        });



    }
}

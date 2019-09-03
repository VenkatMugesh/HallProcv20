package com.example.venkatmugesh.hallprocv20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExamRecords extends AppCompatActivity {

    Button examSchedule  , studentRecords , importButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_records);

        studentRecords = findViewById(R.id.studentRecords);
        examSchedule = findViewById(R.id.examShedule);
        importButton =findViewById(R.id.enterImport);

        studentRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExamRecords.this , StudentRecords.class));
            }
        });

        examSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExamRecords.this , ExamSchedule.class));
            }
        });

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExamRecords.this , ImportDetails.class));
            }
        });
    }
}

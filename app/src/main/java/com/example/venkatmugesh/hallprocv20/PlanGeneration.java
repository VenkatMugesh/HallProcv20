package com.example.venkatmugesh.hallprocv20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlanGeneration extends AppCompatActivity {

    TextView secondExam , thirdExam , fourthExam , noOfStud , noOfHall , hallChecker , examDate;
    EditText hallName;
    Button generateHall;

    String examDates , examTwo , examThree , examFour ;
    long count2 , count3 , count4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_generation);

        secondExam = findViewById(R.id.exam2);
        thirdExam = findViewById(R.id.exam3);
        fourthExam = findViewById(R.id.exam4);
        noOfStud = findViewById(R.id.noOfStud);
        noOfHall = findViewById(R.id.noOfHalls);
        hallChecker = findViewById(R.id.hallChecker);
        examDate = findViewById(R.id.examDate);
        hallName = findViewById(R.id.hallName);
        generateHall = findViewById(R.id.generateHall);

        Intent intent = getIntent();
        examDates = intent.getStringExtra("Date");



        examDate.setText(examDates);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Exams").child(examDates);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                examTwo = (String) dataSnapshot.child("secondYear").getValue();
                examThree = String.valueOf(dataSnapshot.child("thirdYear").getValue());
                examFour = String.valueOf(dataSnapshot.child("finalYear").getValue());

                secondExam.setText(examTwo);
                thirdExam.setText(examThree);
                fourthExam.setText(examFour);


                // Toast.makeText(PlanGeneration.this, examTwo , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









    }
}

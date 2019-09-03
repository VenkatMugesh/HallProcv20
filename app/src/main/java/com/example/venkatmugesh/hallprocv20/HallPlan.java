package com.example.venkatmugesh.hallprocv20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HallPlan extends AppCompatActivity {

    EditText examDate;
    Button viewDetails;
    String dateExam , key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_plan);

        examDate = findViewById(R.id.examDate);
        viewDetails = findViewById(R.id.viewDetails);

        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateExam = examDate.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Exams").child(dateExam);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){

                                Intent intent = new Intent(HallPlan.this , PlanGeneration.class);
                                intent.putExtra("Date" , dateExam);
                                startActivity(intent);
                            }else {
                                Toast.makeText(HallPlan.this, "Sorry no exams has been recorded on this date", Toast.LENGTH_SHORT).show();
                            }
                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}

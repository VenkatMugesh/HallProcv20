package com.example.venkatmugesh.hallprocv20;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StudentRecords extends AppCompatActivity {

    EditText start2 , start3 , start4 , end2 , end3 , end4;
    Button enterDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_records);

        start2 = findViewById(R.id.start2);
        start3 = findViewById(R.id.start3);
        start4 = findViewById(R.id.start4);
        end2 = findViewById(R.id.end2);
        end3 = findViewById(R.id.end3);
        end4 = findViewById(R.id.end4);
        enterDetails = findViewById(R.id.enterData);

        enterDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog pd =  new ProgressDialog(StudentRecords.this);
                pd.setMessage("Entering Details" );
                pd.show();

                if(TextUtils.isEmpty(start2.getText().toString()) || TextUtils.isEmpty(end2.getText().toString()) ){

                }else{
                    int startValue = Integer.parseInt(start2.getText().toString());
                    int endValue =  Integer.parseInt(end2.getText().toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SecondYear");

                    HashMap<Object , Integer> hashMap = new HashMap<>();

                    for (int i = startValue; i<=endValue ; i++){

                        hashMap.put("Reg No" , i);
                        reference.push().setValue(hashMap);
                    }


                }
                if(TextUtils.isEmpty(start3.getText().toString()) || TextUtils.isEmpty(end3.getText().toString()) ){

                }else{
                    int startValue = Integer.parseInt(start3.getText().toString());
                    int endValue =  Integer.parseInt(end3.getText().toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ThirdYear");

                    HashMap<Object , Integer> hashMap = new HashMap<>();

                    for (int i = startValue; i<=endValue ; i++){

                        hashMap.put("Reg No" , i);
                        reference.push().setValue(hashMap);
                    }


                }

                if(TextUtils.isEmpty(start4.getText().toString()) || TextUtils.isEmpty(end4.getText().toString()) ){

                }else{
                    int startValue = Integer.parseInt(start4.getText().toString());
                    int endValue =  Integer.parseInt(end4.getText().toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("FinalYear");

                    HashMap<Object , Integer> hashMap = new HashMap<>();

                    for (int i = startValue; i<=endValue ; i++){

                        hashMap.put("Reg No" , i);
                        reference.push().setValue(hashMap);
                    }


                }
                pd.dismiss();
                Toast.makeText(StudentRecords.this, "Entered Successfully..!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}

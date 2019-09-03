package com.example.venkatmugesh.hallprocv20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ExamSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Spinner secondSpinner , thirdSpinner , finalSpinner;
    EditText examDate;
    ArrayAdapter<CharSequence> secondYear , thirdYear , finalYear;
    String examSecond , examThird , examFinal , key;
    Button addData;
    Exams exams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_schedule);

        radioGroup = findViewById(R.id.radioGroup);
        secondSpinner = findViewById(R.id.secondSpinner);
        thirdSpinner = findViewById(R.id.thirdSpinner);
        finalSpinner = findViewById(R.id.finalSpinner);
        examDate = findViewById(R.id.examDate);
        addData = findViewById(R.id.addData);
        exams = new Exams();

        addOnDataChanged();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                addOnDataChanged();
            }
        });
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             examSecond = String.valueOf(adapterView.getItemAtPosition(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(ExamSchedule.this, "You Should Selecct Something..!", Toast.LENGTH_SHORT).show();

            }
        });
        thirdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                examThird = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(ExamSchedule.this, "You Should Selecct Something..!", Toast.LENGTH_SHORT).show();

            }
        });
        finalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                examFinal = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(ExamSchedule.this, "You Should Selecct Something..!", Toast.LENGTH_SHORT).show();

            }
        });

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addDataToDb();
            }
        });



    }

    public void addDataToDb(){

        String dateExam = examDate.getText().toString();
        if (TextUtils.isEmpty(dateExam)){
            Toast.makeText(ExamSchedule.this, "Date is Important..!", Toast.LENGTH_SHORT).show();

        }else{
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Exams");


            exams.setSecondYear(examSecond);
            exams.setThirdYear(examThird);
            exams.setFinalYear(examFinal);

            reference.child(dateExam).setValue(exams);
            Toast.makeText(this, "Entered Successfully", Toast.LENGTH_SHORT).show();
    }
    }


    public void addOnDataChanged(){
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(id);
        String value = (String) radioButton.getText();

        if (value.equals("ODD")){

            secondYear = ArrayAdapter.createFromResource(this , R.array.odd_second , android.R.layout.simple_spinner_item);
            thirdYear = ArrayAdapter.createFromResource(this , R.array.odd_third , android.R.layout.simple_spinner_item);
            finalYear = ArrayAdapter.createFromResource(this , R.array.odd_final , android.R.layout.simple_spinner_item);

            secondYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            thirdYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            finalYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            secondSpinner.setAdapter(secondYear);
            thirdSpinner.setAdapter(thirdYear);
            finalSpinner.setAdapter(finalYear);

        }else{

            secondYear = ArrayAdapter.createFromResource(this , R.array.even_second , android.R.layout.simple_spinner_item);
            thirdYear = ArrayAdapter.createFromResource(this , R.array.even_third , android.R.layout.simple_spinner_item);
            finalYear = ArrayAdapter.createFromResource(this , R.array.even_final , android.R.layout.simple_spinner_item);

            secondYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            thirdYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            finalYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            secondSpinner.setAdapter(secondYear);
            thirdSpinner.setAdapter(thirdYear);
            finalSpinner.setAdapter(finalYear);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

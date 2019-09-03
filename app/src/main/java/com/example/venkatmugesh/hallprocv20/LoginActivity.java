package com.example.venkatmugesh.hallprocv20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email , passWord;
    Button login;
    TextView txt_signup;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailAddress);
        passWord = (EditText) findViewById(R.id.passWord);
        login = (Button) findViewById(R.id.loginMain);
        txt_signup = (TextView) findViewById(R.id.txt_signup);

        auth = FirebaseAuth.getInstance();

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Please Wait..");
                pd.show();

                String str_email = email.getText().toString();
                String str_password = passWord.getText().toString();


                if(TextUtils.isEmpty(str_password) || TextUtils.isEmpty(str_email)){
                    Toast.makeText(LoginActivity.this , "All fields are Required" , Toast.LENGTH_LONG).show();
                }else{
                    auth.signInWithEmailAndPassword(str_email , str_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("aDMINS")
                                                .child(auth.getCurrentUser().getUid());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                pd.dismiss();
                                                Intent i = new Intent(LoginActivity.this , ExamRecords.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }else {
                                        pd.dismiss();
                                        Toast.makeText(LoginActivity.this  , "Authentication Failed" , Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}

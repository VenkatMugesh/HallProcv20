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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText adminName , idNumber , passWord , email;
    Button register;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        adminName = (EditText) findViewById(R.id.adminName);
        idNumber = (EditText) findViewById(R.id.idNumber);
        passWord = (EditText) findViewById(R.id.passWord);
        email = (EditText) findViewById(R.id.emailAddress);

        register = (Button)findViewById(R.id.registerMain);
        txt_login = (TextView) findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please Wait..");
                pd.show();

                String str_username = adminName.getText().toString();
                String str_fullname = idNumber.getText().toString();
                String str_password = passWord.getText().toString();
                String str_email = email.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_password) ||
                        TextUtils.isEmpty(str_email)){
                    Toast.makeText(RegisterActivity.this , "All fields are Required" , Toast.LENGTH_LONG).show();
                }else if (str_password.length() < 6){
                    Toast.makeText(RegisterActivity.this ,"Password should contain atleast 6 characters" , Toast.LENGTH_LONG).show();
                }else{

                    register(str_username , str_fullname , str_password , str_email);
                }
            }
        });
    }
    private void register (final String username , final String fullname , String password , String email){

        auth.createUserWithEmailAndPassword(email , password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Admins").child(userId);

                            HashMap<String , Object> hashMap = new HashMap<>();
                            hashMap.put("id" , userId);
                            hashMap.put("adminname" , username.toLowerCase());
                            hashMap.put("idnumber" , fullname);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        pd.dismiss();
                                        Intent i = new Intent(RegisterActivity.this , ExamRecords.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                    }
                                }
                            });
                        }else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this  , "You cannot register with this email or password" , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

package com.example.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class RegistrationActivity extends AppCompatActivity {
    @SuppressWarnings("deprecation")
    private EditText mEmail, mPassword;
    private Button btnReg;
    private TextView mlogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private ProgressDialog mDialog;

    //firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();
        mDialog= new ProgressDialog(this);
        registration();
    }

    private void registration(){
        mEmail = findViewById(R.id.email_reg);
        mPassword = findViewById(R.id.password_reg);
        btnReg = findViewById(R.id.btn_signup);
        mlogin = findViewById(R.id.login);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=mEmail.getText().toString().trim();
                String pass=mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Required Field");
                    return;
                }else if(!email.matches(emailPattern)){
                    mEmail.setError("Invalid Email Address");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    mPassword.setError("Required Field");
                    return;
                }
                mDialog.setMessage("Registering..");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }else{
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Unsuccessful",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
}
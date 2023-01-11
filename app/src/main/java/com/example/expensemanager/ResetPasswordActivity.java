package com.example.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();

        EditText mailText=findViewById(R.id.forgot_password_email);
        Button sendEmail_btn=findViewById(R.id.btn_reset_password);

        sendEmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mailText.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mailText.setError("Required Field");
                    return;
                }else if(!email.matches(emailPattern)){
                    mailText.setError("Invalid Email Address");
                    return;
                }

                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Mail sent!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
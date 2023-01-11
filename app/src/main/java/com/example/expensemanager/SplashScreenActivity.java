package com.example.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();

                if(mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                finish();
            }
        },1500);
    }
}
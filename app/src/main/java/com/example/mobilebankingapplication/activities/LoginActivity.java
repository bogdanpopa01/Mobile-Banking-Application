package com.example.mobilebankingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilebankingapplication.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserNameLogin, etPasswordLogin;
    private Button btnSignInLogin;

    private void initializeComponents(){
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        etUserNameLogin = findViewById(R.id.etUserNameLogin);
        btnSignInLogin = findViewById(R.id.btnSignInLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponents();

        btnSignInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUserNameLogin.getText().toString().equals("bogdan")
                        && etPasswordLogin.getText().toString().equals("bogdan")){
                    Intent intent = new Intent(getApplicationContext(),HomePageActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.error_username_or_password_incorrect,Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
}
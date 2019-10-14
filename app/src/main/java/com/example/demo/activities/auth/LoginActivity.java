package com.example.demo.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.activities.main.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText,passwordEditText;
    private Button submitButton;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText=findViewById(R.id.email);
        passwordEditText=findViewById(R.id.password);
        submitButton=findViewById(R.id.submit);
        sharedPreferences=getSharedPreferences("pref",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //storeData("root","root");

        //testing login
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr=emailEditText.getText().toString();
                String passwordStr=passwordEditText.getText().toString();
                String email=sharedPreferences.getString("email","");
                String password=sharedPreferences.getString("password","");
                if(emailStr.equals(email)&&passwordStr.equals(password)){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "username or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeData(String email, String pass) {
        editor.putString("email",email);
        editor.putString("password",pass);
        editor.apply();
    }
}

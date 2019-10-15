package com.example.demo.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.R;
import com.example.demo.data.model.User;
import com.example.demo.data.preference.LoginPreferencesImpl;
import com.example.demo.presentation.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    @BindView(R.id.email)
    EditText emailEditText;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.submit)
    Button submitButton;
    private LoginPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setListners();
        loginPresenter = new LoginPresenter(this);
        storeData("root", "root");
    }

    private void setListners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr=emailEditText.getText().toString();
                String passwordStr=passwordEditText.getText().toString();
                loginPresenter.validateLogin(emailStr, passwordStr);

            }
        });
    }


    private void storeData(String email, String pass) {
        LoginPreferencesImpl loginPreferences = new LoginPreferencesImpl();
        loginPreferences.setUser(new User(email, pass));
    }

    @Override
    public void showerror(String error) {
        if (error == null || error.isEmpty())
            return;
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMainScreen() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}

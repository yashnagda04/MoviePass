package com.example.demo.presentation.auth;

import com.example.demo.data.model.User;
import com.example.demo.data.preference.LoginPreferencesImpl;

public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View loginView;
    LoginPreferencesImpl loginPreferences;

    public LoginPresenter(LoginContract.View loginView) {
        this.loginView = loginView;
        loginPreferences = new LoginPreferencesImpl();
    }


    @Override
    public void validateLogin(String email, String password) {
        User user = loginPreferences.getUser();
        if (email.isEmpty() || password.isEmpty())
            loginView.showerror("please fill email and password.");
        else if (email.equals(user.getEmail()) && password.equals(user.getPassword()))
            loginView.showMainScreen();
        else {
            loginView.showerror("email or password is incorrect.");
        }
    }
}

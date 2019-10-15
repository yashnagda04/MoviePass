package com.example.demo.data.preference;

import android.content.SharedPreferences;

import com.example.demo.data.model.User;

public class LoginPreferencesImpl implements LoginPreferences {
    protected SharedPreferences mPreferences;


    public LoginPreferencesImpl() {
        this.mPreferences = PreferencesProvider.providePreferences();
    }

    @Override
    public User getUser() {
        String email = mPreferences.getString("email", "");
        String password = mPreferences.getString("password", "");
        return new User(email, password);
    }

    @Override
    public void setUser(User user) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.apply();

    }
}

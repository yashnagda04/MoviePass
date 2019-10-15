package com.example.demo.presentation;

import android.app.Application;

import com.example.demo.data.preference.PreferencesProvider;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesProvider.init(this);
    }
}
package com.example.demo.data.preference;

import com.example.demo.data.model.User;

public interface LoginPreferences {
    User getUser();

    void setUser(User user);
}

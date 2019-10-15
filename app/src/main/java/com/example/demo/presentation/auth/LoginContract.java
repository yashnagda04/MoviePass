package com.example.demo.presentation.auth;

public interface LoginContract {

    interface View {
        void showerror(String error);

        void showMainScreen();
    }

    interface Presenter {
        void validateLogin(String email, String password);
    }
}

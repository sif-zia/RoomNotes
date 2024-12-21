package com.example.roomnotes.loginFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roomnotes.R;
import com.example.roomnotes.NotesActivity;
import com.example.roomnotes.room.NotesDB;
import com.example.roomnotes.room.User;
import com.example.roomnotes.room.UserDao;

public class LoginFragment extends Fragment {
    Context context;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("email")) {
            navigateToActivity();
        }

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText emailEditText = view.findViewById(R.id.et_email);
        EditText passwordEditText = view.findViewById(R.id.et_password);

        Button loginButton = view.findViewById(R.id.btn_login);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        return view;
    }

    private void loginUser(String email, String password) {
        LoginTask loginTask = new LoginTask();
        loginTask.execute(email, password);
    }

    private class LoginTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... strings) {
            UserDao userDao = NotesDB.getInstance(getContext()).userDao();
            return userDao.loginUser(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            if(context == null) return;

            if (user == null) {
                Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();

                // Store user in shared preferences
                SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("name", user.name);
                editor.putString("email", user.email);
                editor.putInt("id", user.id);

                editor.apply();

                navigateToActivity();
            }
        }
    }

    void navigateToActivity() {
        if(context == null) return;
        Intent intent = new Intent(context, NotesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
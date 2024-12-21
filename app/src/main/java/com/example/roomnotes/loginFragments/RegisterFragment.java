package com.example.roomnotes.loginFragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roomnotes.R;
import com.example.roomnotes.room.NotesDB;
import com.example.roomnotes.room.User;
import com.example.roomnotes.room.UserDao;

public class RegisterFragment extends Fragment {
    Context context;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        EditText name = view.findViewById(R.id.et_name);
        EditText email = view.findViewById(R.id.et_email);
        EditText password = view.findViewById(R.id.et_password);
        EditText confirmPassword = view.findViewById(R.id.et_confirm_password);

        Button registerButton = view.findViewById(R.id.btn_register);

        registerButton.setOnClickListener(v -> {
            String nameStr = name.getText().toString();
            String emailStr = email.getText().toString();
            String passwordStr = password.getText().toString();
            String confirmPasswordStr = confirmPassword.getText().toString();

            if (nameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordStr.equals(confirmPasswordStr)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            context = getContext();

            // Register user
            registerUser(nameStr, emailStr, passwordStr);
        });

        return view;
    }

    private void registerUser(String name, String email, String password) {
        User user = new User(name, email, password);
        new InsertUser().execute(user);
    }

    private class InsertUser extends AsyncTask<User, Void, Long> {
        @Override
        protected Long doInBackground(User... users) {
            NotesDB notesDB = NotesDB.getInstance(getContext());
            UserDao userDao = notesDB.userDao();

            if (userDao.doesUserExist(users[0].email)) {
                return -2L;
            }

            return userDao.insert(users[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);


            if (aLong > 0) {
                Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show();

                // Navigate to login fragment
                navigateToLoginFragment();
            } else if (aLong == -2) {
                Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "User registration failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateToLoginFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }
}
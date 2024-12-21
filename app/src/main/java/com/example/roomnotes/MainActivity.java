package com.example.roomnotes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.roomnotes.loginFragments.LoginFragment;
import com.example.roomnotes.loginFragments.RegisterFragment;

public class MainActivity extends AppCompatActivity {

    TextView actionTextView;
    TextView questionTextView;
    Boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        actionTextView = findViewById(R.id.tv_action);
        questionTextView = findViewById(R.id.tv_question);

        actionTextView.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();

            Fragment fragment = isLogin ? new RegisterFragment() : new LoginFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            isLogin = !isLogin;

            actionTextView.setText(isLogin ? "Register" : "Login");
            questionTextView.setText(isLogin ? "Don't have an account?" : "Already have an account?");
        });
    }
}
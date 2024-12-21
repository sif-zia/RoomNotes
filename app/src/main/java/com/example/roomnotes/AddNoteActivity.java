package com.example.roomnotes;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.roomnotes.room.Note;
import com.example.roomnotes.room.NotesDB;

import java.time.LocalDateTime;

public class AddNoteActivity extends AppCompatActivity {

    EditText titleEditText;
    EditText noteEditText;

    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleEditText = findViewById(R.id.et_title);
        noteEditText = findViewById(R.id.et_note);
        saveButton = findViewById(R.id.btn_save);

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String note = noteEditText.getText().toString();

            if (title.isEmpty() || note.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save note to database
            saveNote(title, note);
        });
    }

    private void saveNote(String title, String note) {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", 0);

        Note newNote = new Note(title, note, LocalDateTime.now().toString(), userId);

        new SaveNoteTask().execute(newNote);
    }

    private class SaveNoteTask extends AsyncTask<Note, Void, Long> {

        @Override
        protected Long doInBackground(Note... notes) {
            return NotesDB.getInstance(AddNoteActivity.this).noteDao().insert(notes[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            if (aLong > 0) {
                Toast.makeText(AddNoteActivity.this, "Note saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddNoteActivity.this, "Failed to save note", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
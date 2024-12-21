package com.example.roomnotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomnotes.room.Note;
import com.example.roomnotes.room.NoteDao;
import com.example.roomnotes.room.NotesDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private TextView titleTextView;
    private Button logoutButton;
    private FloatingActionButton addNoteButton;
    private SearchView searchView;

    private RecyclerView recyclerView;
    private NoteAdapter adapter;

    private List<Note> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleTextView = findViewById(R.id.tv_title);
        logoutButton = findViewById(R.id.btn_logout);
        addNoteButton = findViewById(R.id.fab_add_note);
        recyclerView = findViewById(R.id.rv_notes);
        searchView = findViewById(R.id.sv);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new NoteAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");

        titleTextView.setText("Welcome, " + name.substring(0, name.indexOf(' ')) + "!");

        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.clear();
            editor.remove("email");
            editor.remove("name");
            editor.remove("id");
            editor.apply();

            Intent intent = new Intent(NotesActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        addNoteButton.setOnClickListener(v -> {
            Intent intent = new Intent(NotesActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });

        RadioGroup rgLayout = findViewById(R.id.rg_layout);
        rgLayout.setOnCheckedChangeListener((group, checkedId) -> {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (checkedId == R.id.rb_grid) {
                layoutManager = new GridLayoutManager(this, 2);
            }
            if (checkedId == R.id.rb_list) {
                layoutManager = new LinearLayoutManager(this);
            }
            recyclerView.setLayoutManager(layoutManager);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchNotes(newText);
                return true;
            }
        });
    }

    private void getNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", 0);

        new GetNotes().execute(userId);
    }

    private class GetNotes extends AsyncTask<Integer, Void, List<Note>> {
        @Override
        protected List<Note> doInBackground(Integer... ids) {
            NoteDao noteDao = NotesDB.getInstance(NotesActivity.this).noteDao();
            return noteDao.getNotesById(ids[0]);
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            super.onPostExecute(notes);

            if (notes.isEmpty()) {
                Toast.makeText(NotesActivity.this, "No notes found", Toast.LENGTH_SHORT).show();
            } else {
                // Display notes
                adapter.setNotes(notes);
                notesList = notes;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
        getNotes();
    }

    private void searchNotes(String query) {
        if(notesList == null) {
            return;
        }

        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : notesList) {
            if (note.title.toLowerCase().contains(query.toLowerCase()) || note.content.toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }
        adapter.setNotes(filteredNotes);
    }
}
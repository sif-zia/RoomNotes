package com.example.roomnotes.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Note.class}, version = 1)
public abstract class NotesDB extends RoomDatabase {
    public abstract NoteDao noteDao();
    public abstract UserDao userDao();

    private static NotesDB instance;

    protected NotesDB() {
    }

    public static NotesDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NotesDB.class, "notes.db").build();
        }
        return instance;
    }
}

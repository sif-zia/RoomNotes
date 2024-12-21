package com.example.roomnotes.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    long insert(Note note);

    @Delete
    int delete(Note note);

    @Update
    int update(Note note);

    @Query("SELECT * FROM Note WHERE userIdFk = :userId")
    List<Note> getNotesById(int userId);

}

package com.example.roomnotes.room;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userIdFk"))
public class Note {
    @PrimaryKey(autoGenerate = true)
    int id = 0;

    public String title;
    public String content;
    String timestamp;

    int userIdFk;

    public Note(String title, String content, String timestamp, int userIdFk) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.userIdFk = userIdFk;
    }
}

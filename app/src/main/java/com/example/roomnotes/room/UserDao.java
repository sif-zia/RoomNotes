package com.example.roomnotes.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Delete
    int delete(User user);

    @Update
    int update(User user);

    @Query("SELECT EXISTS (SELECT * FROM User WHERE email = :email)")
    boolean doesUserExist(String email);

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    User loginUser(String email, String password);
}

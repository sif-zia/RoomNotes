package com.example.roomnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTextView;
    public TextView contentTextView;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        titleTextView = itemView.findViewById(R.id.tv_title);
        contentTextView = itemView.findViewById(R.id.tv_note);
    }
}

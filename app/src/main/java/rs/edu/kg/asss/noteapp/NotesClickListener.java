package rs.edu.kg.asss.noteapp;

import androidx.cardview.widget.CardView;

import rs.edu.kg.asss.noteapp.model.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}

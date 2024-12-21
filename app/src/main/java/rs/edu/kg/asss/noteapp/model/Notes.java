package rs.edu.kg.asss.noteapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Notes implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int noteId = 0;

    @ColumnInfo(name = "title")
    private String title = "";

    @ColumnInfo(name = "notes")
    private String notes = "";

    @ColumnInfo(name = "date")
    private String date = "";

    @ColumnInfo(name = "pineed")
    private boolean pinned = false;

    public Notes(){

    }

    public Notes(String title, String description, String date) {
        this.title = title;
        this.notes = description;
        this.date = date;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}

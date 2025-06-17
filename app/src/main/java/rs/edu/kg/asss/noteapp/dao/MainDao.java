package rs.edu.kg.asss.noteapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import rs.edu.kg.asss.noteapp.model.Notes;

@Dao
public interface MainDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY noteId DESC")
    List<Notes> getAllNotes();

    @Query("UPDATE notes SET title = :title, notes = :notes WHERE noteId = :noteId")
    void update(int noteId, String title, String notes);

    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes SET pinned = :pin WHERE noteId = :noteId")
    void pin(int noteId, boolean pin);
}

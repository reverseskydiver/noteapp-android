package rs.edu.kg.asss.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import rs.edu.kg.asss.noteapp.adapters.NotesListAdapter;
import rs.edu.kg.asss.noteapp.dao.RoomDB;
import rs.edu.kg.asss.noteapp.model.Notes;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;

    SearchView searchView_home;
    Notes selectedNote;
    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;

            showPopup(cardView);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);

        database = RoomDB.getInstance(this);
        notes = database.mainDao().getAllNotes();

        updateRecycler(notes);

        fab_add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            startActivityForResult(intent, 101);
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        String lowerCase = newText.toLowerCase();

        List<Notes> filteredList = new ArrayList<>(notes.stream()
                .filter(note -> note.getTitle().toLowerCase().contains(lowerCase)
                        || note.getNotes().contains(lowerCase))
                .collect(Collectors.toList()));

        notesListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == MainActivity.RESULT_OK) {
            Notes newNotes = (Notes) data.getSerializableExtra("note");
            database.mainDao().insert(newNotes);
            notes.clear();
            notes.addAll(database.mainDao().getAllNotes());
            notesListAdapter.notifyDataSetChanged();
        } else if (requestCode == 102 && resultCode == MainActivity.RESULT_OK) {
            Notes newNote = (Notes) data.getSerializableExtra("note");
            database.mainDao().update(newNote.getNoteId(), newNote.getTitle(), newNote.getNotes());
            notes.clear();
            notes.addAll(database.mainDao().getAllNotes());
            notesListAdapter.notifyDataSetChanged();
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (R.id.pin == item.getItemId()) {
            database.mainDao().pin(selectedNote.getNoteId(), !selectedNote.isPinned());
            Toast.makeText(MainActivity.this, String.format("Note %s is %s", selectedNote.getTitle(), selectedNote.isPinned() ? "Pinned" : "Unpinned"), Toast.LENGTH_SHORT).show();
            notes.clear();
            notes.addAll(database.mainDao().getAllNotes());
            notesListAdapter.notifyDataSetChanged();
            return true;
        }

        if (R.id.delete == item.getItemId()) {
            database.mainDao().delete(selectedNote);
            notes.remove(selectedNote);
            notesListAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, String.format("Deleted %s note.", selectedNote.getTitle()), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
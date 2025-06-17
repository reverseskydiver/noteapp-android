package rs.edu.kg.asss.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import rs.edu.kg.asss.noteapp.model.Notes;

public class NotesTakerActivity extends AppCompatActivity {

    EditText editText_title, editText_notes;
    ImageView imageView_save;
    Notes notes;

    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes_taker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView_save = findViewById(R.id.imageView_save);
        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);

        notes = new Notes();
        try {
            notes  = (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());

            isOldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView_save.setOnClickListener(v -> {
            String title = editText_title.getText().toString();
            String description = editText_notes.getText().toString();

            if (description.isBlank()) {
                Toast.makeText(NotesTakerActivity.this, "Please add some notes.", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd.MM.yyyy HH:mm:ss");
            Date date = new Date();
            String formattedDate = sdf.format(date);

            if (!isOldNote) {
                notes = new Notes(title, description, formattedDate);
            } else {
                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(formattedDate);
            }

            Intent intent = new Intent();
            intent.putExtra("note", notes);
            setResult(NotesTakerActivity.RESULT_OK, intent);

            finish();
        });
    }
}
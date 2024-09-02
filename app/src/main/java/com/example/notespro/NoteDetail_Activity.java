package com.example.notespro;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetail_Activity extends AppCompatActivity {

    EditText tittleedittext, contentedittext;
    ImageButton savenotebtn;
    TextView pagetitletextview;
    String title, content, docid;
    boolean isEditmode = false;
    TextView deletenotebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        tittleedittext = findViewById(R.id.notes_titles_text);
        contentedittext = findViewById(R.id.notes_content__text);
        savenotebtn = findViewById(R.id.save_button);
        pagetitletextview = findViewById(R.id.page_title);
        deletenotebtn = findViewById(R.id.delete_note_textview_btn);

        // received data
        title = getIntent().getStringExtra("Title");
        content = getIntent().getStringExtra("content");
        docid = getIntent().getStringExtra("docid");

        tittleedittext.setText(title);
        contentedittext.setText(content);

        if (docid != null && !docid.isEmpty()) {
            pagetitletextview.setText("Edit your Note");
            isEditmode = true;
        } else {
            pagetitletextview.setText("Add a new Note");
        }

        if (isEditmode) {
            Log.d(TAG, "Setting delete button visible");
            deletenotebtn.setVisibility(View.VISIBLE);
            // Force redraw
            deletenotebtn.post(() -> {
                deletenotebtn.invalidate();
                deletenotebtn.requestLayout();
            });
        } else {
            deletenotebtn.setVisibility(View.GONE);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        savenotebtn.setOnClickListener(v -> Savenotebtn());
        deletenotebtn.setOnClickListener(v -> deletenotefromfirebase());
    }

    void Savenotebtn() {
        String noteTitle = tittleedittext.getText().toString();
        String notecontent = contentedittext.getText().toString();

        if (noteTitle == null || noteTitle.isEmpty()) {
            tittleedittext.setError("Title is required");
            return;
        }
        Note note = new Note();

        note.setTitle(noteTitle);
        note.setContent(notecontent);

        savenotetofirenase(note);
    }

    void savenotetofirenase(Note note) {
        DocumentReference documentReference;
        if (isEditmode) {
            // update the note
            documentReference = Utility.getCollectionreferencefornote().document(docid);
        } else {
            // create new note
            documentReference = Utility.getCollectionreferencefornote().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showtoast(NoteDetail_Activity.this, "Note Added successfully");
                    finish();
                } else {
                    Utility.showtoast(NoteDetail_Activity.this, "Failed while adding note");
                }
            }
        });
    }

    void deletenotefromfirebase() {
        if (docid == null || docid.isEmpty()) {
            Utility.showtoast(NoteDetail_Activity.this, "No document ID to delete");
            return;
        }

        DocumentReference documentReference = Utility.getCollectionreferencefornote().document(docid);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showtoast(NoteDetail_Activity.this, "Note Deleted successfully");
                    finish();
                } else {
                    Utility.showtoast(NoteDetail_Activity.this, "Failed while deleting note");
                }
            }
        });
    }
}

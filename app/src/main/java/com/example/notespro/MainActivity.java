package com.example.notespro;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addnotebtn;
    RecyclerView recyclerView;
    ImageButton menubtn;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        addnotebtn = findViewById(R.id.add_note_btn);
        recyclerView=findViewById(R.id.recycler_view);
        menubtn = findViewById(R.id.menu_button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addnotebtn.setOnClickListener((v)->startActivity(new Intent(MainActivity.this,NoteDetail_Activity.class)));
        menubtn.setOnClickListener((v)->showmenu());
        setupRecyclerview();







    }


    void showmenu(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menubtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout")
                    FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,Login_Activity.class));
                finish();
                return true;
            }
        });
    }
// shivamyt1806@gmail.com
  void  setupRecyclerview(){
      Query query = Utility.getCollectionreferencefornote().orderBy("timestamp", Query.Direction.DESCENDING);
      FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
              .setQuery(query,Note.class).build();
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      noteAdapter = new NoteAdapter(options,this);
      recyclerView.setAdapter(noteAdapter);
  }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }


    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}
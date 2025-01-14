package com.example.notespro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.Noteviewholder> {
  Context context;


    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Noteviewholder holder, int position, @NonNull Note note) {
     holder.titleTextView.setText(note.title);
     holder.contentTextView.setText(note.content);
  holder.timestampTextView.setText(Utility.timestamptostring(note.timestamp));


   holder.itemView.setOnClickListener((v)->{

       Intent intent = new Intent(context,NoteDetail_Activity.class);
       intent.putExtra("title",note.title);
       intent.putExtra("content",note.content);
       String docid = this.getSnapshots().getSnapshot(position).getId();
       intent.putExtra("docid",docid);
      context.startActivity(intent);

   });
    }

    @NonNull
    @Override
    public Noteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new Noteviewholder(view);
    }

    class  Noteviewholder extends RecyclerView.ViewHolder {

        TextView titleTextView, contentTextView,timestampTextView;


        public Noteviewholder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
           timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view);
        }
    }
}

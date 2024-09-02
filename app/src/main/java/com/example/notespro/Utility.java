package com.example.notespro;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Collection;

public class Utility {

    static  void  showtoast(Context context,String msg){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionreferencefornote(){
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();

       return FirebaseFirestore.getInstance().collection("notes")
               .document(currentuser.getUid()).collection("my_notes");
    }


    static String timestamptostring(Timestamp timestamp) {
        if (timestamp != null) {
            return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
        } else {
            return "";
        }
    }
}

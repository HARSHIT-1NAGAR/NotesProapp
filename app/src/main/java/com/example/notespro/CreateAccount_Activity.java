package com.example.notespro;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount_Activity extends AppCompatActivity {

    EditText email_edittext,Password,Confirm_Password;
    Button createAccountbtn;
    ProgressBar progressbar;
    TextView loginbtntextview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        email_edittext = findViewById(R.id.email_edittext);
        Password = findViewById(R.id.password_edittext);
        Confirm_Password = findViewById(R.id.confirmpassword_edittext);

        createAccountbtn = findViewById(R.id.create_account);
        progressbar = findViewById(R.id.progress_bar);
        loginbtntextview = findViewById(R.id.login_text_view_btn);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        createAccountbtn.setOnClickListener( v->createAccount() );
       loginbtntextview.setOnClickListener( v->startActivity(new Intent(CreateAccount_Activity.this,Login_Activity.class)));




    }

    void createAccount(){
  String email =  email_edittext.getText().toString();
  String password =  Password.getText().toString();
   String confirm_password =  Confirm_Password.getText().toString();
  boolean isvalidate  = validating(email,password,confirm_password);

  if(!isvalidate){
      return;
  }

  createAccountInFirebase( email, password );
        FirebaseAuth   firebaseAuth  = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccount_Activity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //creating account is done
                            Utility.showtoast(CreateAccount_Activity.this,"Successfully Created account ");                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else{
                            Utility.showtoast(CreateAccount_Activity.this,task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    void createAccountInFirebase(String email,String password){

    }
    void changeinProgressBar(boolean inProgress){
        if(inProgress){
            progressbar.setVisibility(View.VISIBLE);
            createAccountbtn.setVisibility(View.GONE);
        }
        else{
            progressbar.setVisibility(View.VISIBLE);
            createAccountbtn.setVisibility(View.GONE);
        }
    }

    boolean validating(String email, String password, String confirm_password) {
        // Validate the data which is input by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_edittext.setError("Invalid email address");
            return false;
        }
        if (password.length() < 6) {
            Password.setError("Password must be at least 6 characters long");
            return false;
        }
        if (!password.equals(confirm_password)) {
            Confirm_Password.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}
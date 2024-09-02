package com.example.notespro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {


    EditText email_edittext,Password;
    Button loginbtn;
    ProgressBar progressbar;
    TextView createAccountBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        email_edittext = findViewById(R.id.email_edittext);
        Password = findViewById(R.id.password_edittext);


        loginbtn = findViewById(R.id.login_button);
        progressbar = findViewById(R.id.progress_bar);
        createAccountBtn = findViewById(R.id.create_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loginbtn.setOnClickListener((v) -> loginuser());
        createAccountBtn.setOnClickListener((v)-> startActivity(new Intent(Login_Activity.this,CreateAccount_Activity.class)));
    }

    void  loginuser(){
        String email =  email_edittext.getText().toString();
        String password =  Password.getText().toString();
        boolean isvalidate  = validating(email,password);

        if(!isvalidate){
            return;
        }

        loginbAccountInFirebase( email, password );
    }

    void  loginbAccountInFirebase(String email , String password){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeinProgressBar(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeinProgressBar(false);
               if(task.isSuccessful()){
                   //login is success

                   if (firebaseAuth.getCurrentUser().isEmailVerified()){
                       // go to main activity
                       startActivity(new Intent(Login_Activity.this,MainActivity.class));
                       finish();
                   }
                   else{
                    Utility.showtoast(Login_Activity.this,"Email not Veified,Please Veify your email");
                   }
               }
               else {
                   //login failure
                   Utility.showtoast(Login_Activity.this,task.getException().getLocalizedMessage());

               }
            }
        });
    }



    void changeinProgressBar(boolean inProgress){
        if(inProgress){
            progressbar.setVisibility(View.VISIBLE);
            loginbtn.setVisibility(View.GONE);
        }
        else{
            progressbar.setVisibility(View.VISIBLE);  // This should be View.GONE
            loginbtn.setVisibility(View.GONE);
        }
    }

    boolean validating(String email, String password) {
        // Validate the data which is input by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_edittext.setError("Invalid email address");
            return false;
        }
        if (password.length() < 6) {
            Password.setError("Password must be at least 6 characters long");
            return false;
        }

        return true;








    }






}
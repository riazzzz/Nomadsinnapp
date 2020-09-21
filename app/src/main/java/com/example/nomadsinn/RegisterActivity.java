package com.example.nomadsinn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {
    Button buttonregister;
    Button  textviewlogin;
    EditText edittextemail;
    EditText edittextepassword;
 ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textviewlogin = (Button) findViewById(R.id.button_sendto_login);
        edittextemail=(EditText) findViewById(R.id.edit_text_email) ;
        edittextepassword=(EditText) findViewById(R.id.edit_text_password) ;
        progressBar=(ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        buttonregister = (Button) findViewById(R.id.button_register);

        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String email =edittextemail.getText().toString().trim();
               String password= edittextepassword.getText().toString().trim();
               if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
               {
                   edittextemail.setError("Please enter valid email");
                   edittextemail.requestFocus();
                   return;
               }
               if(email.isEmpty()){
                   edittextemail.setError("Email required");
                   edittextemail.requestFocus();
                   return;
               }
                if(password.isEmpty()){
                    edittextemail.setError("Password required");
                    edittextemail.requestFocus();
                    return;
                }
                if(password.length()<6)
                {
                    edittextepassword.setError("minimum length of password should be 6");

                    edittextemail.requestFocus();
                    return;
                }


             registration(email,password);
            }
        });


        textviewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });



    }


    //registration firebase part

    private void registration(String email, String password)
    {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                   if(task.getException() instanceof FirebaseAuthUserCollisionException){
                       Toast.makeText(getApplicationContext(), "Email registered already", Toast.LENGTH_SHORT).show();
                   }
                   else{
                       Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                   }
                }
            }
        });
    }
}
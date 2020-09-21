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

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button buttonlogin;
    Button textviewregister;
    EditText edittextemail;
    EditText edittextepassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initating section
        mAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.progressbar);
        edittextemail=(EditText) findViewById(R.id.edit_text_email) ;
        edittextepassword=(EditText) findViewById(R.id.edit_text_password) ;
        textviewregister = (Button) findViewById(R.id.button_sendto_signup);
        buttonlogin=(Button) findViewById(R.id.button_sign_in);



        buttonlogin.setOnClickListener(new View.OnClickListener() {
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



                userLogin(email,password);
            }
        });


        // end of ionitiation

        textviewregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });
    }

    public void openRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

        startActivity(intent);
    }


    private void userLogin(String email, String password)
    {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    finish();
                    Intent intent = new Intent(LoginActivity.this, Showprofileactivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this,Showprofileactivity.class));

        }
    }
}
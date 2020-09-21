package com.example.nomadsinn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Showprofileactivity extends AppCompatActivity {
    FirebaseAuth mAuth;
 FirebaseStorage storage;
    ImageView imageView;
    TextView name;
    TextView mail;
    Button edit;

    Button signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showprofileactivity);
        storage=FirebaseStorage.getInstance();
        mAuth= FirebaseAuth.getInstance();
        name=(TextView) findViewById(R.id.Name);
       mail=(TextView) findViewById(R.id.Email);
           edit= (Button) findViewById(R.id.button_Edit);
           signout=(Button) findViewById(R.id.button_Signout);
        imageView=(ImageView)findViewById(R.id.user_profile_photo);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Showprofileactivity.this, ProfileActivity.class));

            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           Signout();
            }
        });


        loadUserInformation();
    }


    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null){
          //  finish();
            startActivity(new Intent(this,LoginActivity.class));

        }
    }





    private void loadUserInformation(){

        FirebaseUser user=mAuth.getCurrentUser();

        String email =user.getEmail().toString();
        String Uid =user.getUid().toString();


if(user!=null){
   /* StorageReference ref=storage.getReferenceFromUrl("profilepics/"+ mAuth.getCurrentUser().getUid() +".jpg");

         Glide.with(this).load(ref).into(imageView);*/
   Glide.with(this).load(user.getPhotoUrl()).into(imageView);

if (user.getDisplayName()!=null)
{
   mail.setText(user.getEmail());
    name.setText(user.getDisplayName());
}}


    }



    private void Signout()
    {
        mAuth.signOut();
         finish();
        startActivity(new Intent(this,LoginActivity.class));
    }

}


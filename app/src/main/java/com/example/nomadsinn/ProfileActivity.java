package com.example.nomadsinn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 1000 ;
    ImageView imageView;
    EditText editname;

    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileimageUrl;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editname=(EditText) findViewById(R.id.Name);
        imageView=(ImageView)findViewById(R.id.user_profile_photo);
        progressBar=(ProgressBar)findViewById(R.id.progressbr);

        mAuth= FirebaseAuth.getInstance();


        // click listeners

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagechooser();

            }
        });



        findViewById(R.id.button_Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserinformation();

            }
        });

    }
// user information to firebase
    private void saveUserinformation() {

        String displayname = editname.getText().toString();

        if(displayname.isEmpty())
        {
            editname.setError("name required");
            editname.requestFocus();
            return;
        }
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null && profileimageUrl!=null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayname).setPhotoUri(Uri.parse(profileimageUrl)).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ProfileActivity.this, "profile updated",Toast.LENGTH_SHORT);
                        finish();
                        Intent intent = new Intent(ProfileActivity.this, Showprofileactivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
        }

    }

    // to get the image we have to override this on activityresult methode
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE&& resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            uriProfileImage=data.getData(); // gets uri
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage)  ;
                imageView.setImageBitmap(bitmap);
       uploadImagetofirebasestorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // to select image of user when tapped in image viewer
    private void showImagechooser(){
        Intent intent  = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select profile image"), CHOOSE_IMAGE);

    }
    // to upload the profile pic to to firebase

   private void uploadImagetofirebasestorage(){
       StorageReference profileimageref= FirebaseStorage.getInstance().getReference("profilepics/"+ mAuth.getCurrentUser().getUid().toString()+".jpg");
       if(uriProfileImage!=null)
       {
           progressBar.setVisibility(View.VISIBLE);
           profileimageref.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   progressBar.setVisibility(View.GONE);
                   profileimageUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();


               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   progressBar.setVisibility(View.GONE);
                   Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT);

               }
           });
       }

    }




}
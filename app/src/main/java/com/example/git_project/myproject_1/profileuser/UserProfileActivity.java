package com.example.git_project.myproject_1.profileuser;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.git_project.myproject_1.R;
import com.example.git_project.myproject_1.weconnectposthome.WeConnectHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;


public class UserProfileActivity extends AppCompatActivity {

    private ImageView dp;
    private EditText name, status;
    private TextView done;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    Uri imageHoldUri = null;
    Context context;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;

    //FIREBASE AUTH

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;

    //Firebase database

    DatabaseReference mUserDataBase;
    StorageReference mStorageRef;

    //PROGRESS DIALOG:
    ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        dp = (ImageView) findViewById(R.id.userprofileiv);
        name = (EditText) findViewById(R.id.userprofilename);
        status = (EditText) findViewById(R.id.userprofilesemester);
        done = (TextView) findViewById(R.id.userprofilebutton);

        //declaration of Firebase components
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // LOGIC for CHECK USER

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    finish();
                    Intent moveToHome = new Intent(UserProfileActivity.this, WeConnectHome.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);


                }
            }
        };
        //Progress Dialog
        mProgress = new ProgressDialog(this);

        //FIREBASE DATABASE FIELDS
        mUserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();


            }
        });

        //FOR IMAGE SELECTION
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //LOGIC FOR SELECTING PICTURE
               // Intent selectPhoto = new Intent(UserProfileActivity.this,SelectImage.class);
                //startActivity(selectPhoto);
                profilePicSelection();

            }
        });

    }

    private void profilePicSelection() {



        final CharSequence[] items = { "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                 if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                }/*if (items[item].equals("Camera")) {
                    cameraIntent();
                }*/ else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void galleryIntent() {


            //CHOOSE IMAGE FROM GALLERY
            Log.d("gola", "entered here");
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_FILE);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            //SAVE URI FROM CAMERA
            if(data!=null){
                Uri selectedImageUri = data.getData();
               String filestring = selectedImageUri.getPath();

                Bitmap thumbnail = BitmapFactory.decodeFile(filestring);

                System.out.println("Bitmap(CAMERA_IMAGES_REQUEST):"+thumbnail);
                //System.out.println("cap_image(CAMERA_IMAGES_REQUEST):"+cap_image);
                dp.setImageBitmap(thumbnail);

             //   Log.e("ImagePosition", position + "");
             //   Log.e("ImagePath", imagePath);

            }


            try{
/*
                Log.d("asdf", String.valueOf(s));
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
*/

            }catch (Exception e){

            }

        }


//image crop
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                dp.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void saveUserProfile() {
        final String username, userstatus;

        username = name.getText().toString().trim();
        userstatus = status.getText().toString().trim();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userstatus)) {
            if (imageHoldUri != null) {
                mProgress.setTitle("Saving Profile");
                mProgress.setMessage("PLEASE WAIT!");
                mProgress.show();
                StorageReference mChildStorage = mStorageRef.child("User_Profile").child(imageHoldUri.getLastPathSegment());
                String profilePicUrl = imageHoldUri.getLastPathSegment();

                mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        final Uri imageUrl = taskSnapshot.getDownloadUrl();

                        mUserDataBase.child("username").setValue(username);
                        mUserDataBase.child("status").setValue(userstatus);
                        mUserDataBase.child("userid").setValue(mAuth.getCurrentUser().getUid());
                        mUserDataBase.child("imageurl").setValue(imageUrl.toString());


                        mProgress.dismiss();

                        finish();
                        Intent moveToHome = new Intent(UserProfileActivity.this, WeConnectHome.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);
                    }
                });

            } else {
                Toast.makeText(UserProfileActivity.this, "Please ADD IMAGE ", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(UserProfileActivity.this, "Please Enter Username and status ", Toast.LENGTH_LONG).show();
        }
    }


}


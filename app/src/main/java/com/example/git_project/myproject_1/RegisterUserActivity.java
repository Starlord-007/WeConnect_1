package com.example.git_project.myproject_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.git_project.myproject_1.weconnectposthome.WeConnectHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView create;

    //FIREBASE RELATED
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //DIALOGS
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        email=(EditText)findViewById(R.id.et2);
        password=(EditText)findViewById(R.id.et3);
        create=(TextView) findViewById(R.id.tv3);

        //DIALOGS
        mProgressDialog=new ProgressDialog(this);

        //FIREBASE INSTANCE....
        mAuth=FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //will going to use this for knowing if the user_profiles is alredy logedin or not
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if (user!=null)
                {
                    startActivity(new Intent(RegisterUserActivity.this, WeConnectHome.class));
                }


            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Creating Account");
                mProgressDialog.setMessage("Please Wait!");
                mProgressDialog.show();
                createUserAccount();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void createUserAccount() {

        String useremail, userpass;

        useremail=email.getText().toString().trim();
        userpass=password.getText().toString().trim();

        if (!TextUtils.isEmpty(useremail) && !TextUtils.isEmpty(userpass))
        {
            mAuth.createUserWithEmailAndPassword(useremail,userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(RegisterUserActivity.this,"Account created", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                        startActivity(new Intent(RegisterUserActivity.this, WeConnectHome.class));

                    } else
                    {
                        Toast.makeText(RegisterUserActivity.this,"Account not created", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }





                }
            });


        }

    }
}

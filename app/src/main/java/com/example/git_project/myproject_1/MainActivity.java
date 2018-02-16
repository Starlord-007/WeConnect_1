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

public class MainActivity extends AppCompatActivity {

    private EditText editText1,editText2;
    private TextView login,signup;

    //FireBase stuff
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //DIALOGS
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 =(EditText)findViewById(R.id.et0);
        editText2 =(EditText)findViewById(R.id.et1);
        login =(TextView)findViewById(R.id.tv1);
        signup =(TextView)findViewById(R.id.tv2);


        //DIALOGS
        mProgressDialog=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //Chedk Users
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null)
                {
                    Intent move = new Intent(MainActivity.this, WeConnectHome.class);
                    move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(move);
                }

            }
        };

        mAuth.addAuthStateListener(mAuthListener);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,RegisterUserActivity.class));

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Login in the Account");
                mProgressDialog.setMessage("Please Wait!");
                loginUser();

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

    private void loginUser() {

        String useremail,userpass;

        useremail=editText1.getText().toString().trim();
        userpass=editText2.getText().toString().trim();

        if (!TextUtils.isEmpty(useremail) && !TextUtils.isEmpty(userpass))
        {
            mAuth.signInWithEmailAndPassword(useremail,userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        mProgressDialog.dismiss();
                        Intent move = new Intent(MainActivity.this, WeConnectHome.class);
                        move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(move);

                    }else
                    {
                        Toast.makeText(MainActivity.this,"Unable to loigin user",Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }

                }
            });

        }else
        {

            Toast.makeText(MainActivity.this,"Enter details",Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();

        }


    }
}

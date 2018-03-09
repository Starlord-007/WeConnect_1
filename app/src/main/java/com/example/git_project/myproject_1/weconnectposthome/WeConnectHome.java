package com.example.git_project.myproject_1.weconnectposthome;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.git_project.myproject_1.MainActivity;
import com.example.git_project.myproject_1.R;
import com.example.git_project.myproject_1.profileuser.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WeConnectHome extends AppCompatActivity implements View.OnClickListener {


    LinearLayout feed_fragment,chat_fragment,club_fragment,user_fragment;

    //FireBase stuf
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //Farebase DataBase
    DatabaseReference mDatabaseRef;


    //ToolBar Instances
    android.support.v7.widget.Toolbar toolbar;


    void call_fragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_connect_home3);

        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.customToolBar);
        setSupportActionBar(toolbar);


        feed_fragment=(LinearLayout)findViewById(R.id.feed_fragment);
        chat_fragment=(LinearLayout)findViewById(R.id.chat_fragment);
        club_fragment=(LinearLayout)findViewById(R.id.club_fragment);
        user_fragment=(LinearLayout)findViewById(R.id.user_fragment);


        // Database Ref...

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //FireBase intialization
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    final String userId = user.getUid();

                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(userId))

                            {


                            } else {
                                finish();
                                Intent setprofile = new Intent(WeConnectHome.this, UserProfileActivity.class);
                                setprofile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(setprofile);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {
                    finish();
                    startActivity(new Intent(WeConnectHome.this, MainActivity.class));
                }


            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        feed_fragment.setOnClickListener(this);
        chat_fragment.setOnClickListener(this);
        club_fragment.setOnClickListener(this);
        user_fragment.setOnClickListener(this);




      /*  BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.bnv);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
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

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.feed_fragment) {
            //Toast.makeText(getApplicationContext(), "feeds", Toast.LENGTH_SHORT).show();
            Fragment feed = new FeedFragment();
            call_fragment(feed);
        }
        if (v.getId() == R.id.chat_fragment) {
            Toast.makeText(getApplicationContext(), "chat", Toast.LENGTH_SHORT).show();
            Fragment chat = new ChatFragment();
            call_fragment(chat);
        }
        if (v.getId() == R.id.club_fragment) {
            Toast.makeText(getApplicationContext(), "club", Toast.LENGTH_SHORT).show();
            Fragment club = new ClubFragment();
            call_fragment(club);
        }
        if (v.getId() == R.id.user_fragment) {
            Toast.makeText(getApplicationContext(), "user", Toast.LENGTH_SHORT).show();
            Fragment user = new YouFragment();
            call_fragment(user);
        }



    }


}


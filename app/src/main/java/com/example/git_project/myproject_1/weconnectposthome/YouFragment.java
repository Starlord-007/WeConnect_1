package com.example.git_project.myproject_1.weconnectposthome;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class YouFragment extends Fragment {

View view;
TextView logout;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabaseRef;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.user_profiles, container, false);

        logout=(TextView)view.findViewById(R.id.logout);


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
                                getActivity().finish();
                                Intent setprofile = new Intent(getActivity(), UserProfileActivity.class);
                                setprofile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(setprofile);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }


            }
        };

        mAuth.addAuthStateListener(mAuthListener);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });




        return view;


    }

}
